package logistics.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import logistics.exceptions.InitializationException;
import logistics.exceptions.InvalidDataException;
import logistics.facility.FacilityManager;
import logistics.facility.FacilityRecord;
import logistics.item.ItemManager;
import logistics.network.NetworkManager;

public class OrderProcessor {

	private static OrderProcessor ourInstance = new OrderProcessor();
	private ItemManager im = ItemManager.getInstance();
	private FacilityManager fm = FacilityManager.getInstance();
	private NetworkManager nm = NetworkManager.getInstance();
	private OrderManager om = OrderManager.getInstance();
	private int dailyTravelCost = 0;

	public static OrderProcessor getInstance() {
		return ourInstance;
	}

	private OrderProcessor() {}

	public void init(int dailyTravelCost) {
		setDailyTravelCost(dailyTravelCost);
	}

	private void setDailyTravelCost(int dailyTravelCost) {
		this.dailyTravelCost = dailyTravelCost;
	}

	public int getDailyTravelCost() {
		return dailyTravelCost;
	}

	public void startProcessing() throws InitializationException, InvalidDataException {
		int orderCounter = 1;
		for (String orderId : om.getOrderIds()) {
			String destination = om.getOrderDestination(orderId);
			int orderDay = om.getOrderStartDay(orderId);
			String currentItem = om.getOrderNextItem(orderId);
			LinkedHashMap<String, Integer> orderItems = new LinkedHashMap<>();
			LinkedHashMap<String, List<FacilityRecord>> solution = new LinkedHashMap<>();
			while (currentItem != null) {
				int orderQty = om.getOrderedItemQty(orderId, currentItem);
				orderItems.put(currentItem, orderQty);
				if (im.contains(currentItem)) {
					ArrayList<FacilityRecord> itemSolution = new ArrayList<>();
					while (orderQty > 0) {
						List<String> whoHasIt = fm.whoHasIt(currentItem, destination);
						if (whoHasIt.isEmpty()) {
							System.err.println("Generate Back-Order for item " + currentItem + " \n");
							orderQty = 0;
						} else {
							List<FacilityRecord> records = new ArrayList<>();
							for (String currentFacility : whoHasIt) {
								int qtyAvailable = fm.getItemCount(currentItem, currentFacility);
								int qtyTaken = Integer.min(qtyAvailable, orderQty);
								float travelTime = nm.getDistanceInDays(currentFacility, destination);
								int procEndDay = fm.quoteTime(currentFacility, orderDay, qtyTaken);
								float procTime = (float) qtyTaken / fm.getDailyRate(currentFacility);
								float cost = qtyTaken * im.getPrice(currentItem)
										+ procTime * fm.getDailyCost(currentFacility)
										+ (float) Math.ceil(travelTime) * getDailyTravelCost();
								records.add(new FacilityRecord(currentFacility, qtyAvailable, qtyTaken, procEndDay,
										travelTime, cost));
							}
							Collections.sort(records);
							FacilityRecord fr = records.get(0);
							itemSolution.add(fr);
							fm.bookOrder(fr.getName(), orderDay, fr.getQtyTaken());
							fm.reduceInventory(fr.getName(), currentItem, fr.getQtyTaken());
							orderQty = orderQty - fr.getQtyTaken();
						}
						solution.put(currentItem, itemSolution);
					}
				} else {
					System.err.println("Item " + currentItem + " not in Catalog\n");
				}
				currentItem = om.getOrderNextItem(orderId);
			}
			System.out.println(new String(new char[82]).replace("\0", "-"));
			System.out.println("Order #" + orderCounter + "\n" + getReport(orderId, orderItems, solution));
			orderCounter++;
		}
	}

	private String getReport(String orderId, LinkedHashMap<String, Integer> orderItems,
			LinkedHashMap<String, List<FacilityRecord>> solution) {
		float totalCost = 0;
		String report = String.format("  %-15s%s\n  %-15s%s\n  %-15s%s\n\n  List of Order Items: ", "Order Id:",
				orderId, "Order Time: ", "Day " + om.getOrderStartDay(orderId), "Destination:",
				om.getOrderDestination(orderId));
		int itemCounter = 1;
		for (String item : orderItems.keySet()) {
			report += String.format("\n%8d) Item ID: %10s,      Quantity: %d", itemCounter, item, orderItems.get(item));
			itemCounter++;
		}
		report += "\n\n  Processing Solution: ";
		for (String item : solution.keySet()) {
			int totalQty = 0;
			float totalCostItem = 0;
			report += String.format("\n\n  %s:\n  %8s%-20s%-15s%-20s%-15s", item, "", "Facility", "Quantity", "Cost",
					"Arrival Day");
			int facilityCounter = 1;
			List<Integer> arrivalDays = new ArrayList<>();
			for (FacilityRecord fr : solution.get(item)) {
				int qty = fr.getQtyTaken();
				float cost = fr.getCost();
				int arrivalDay = fr.getArrivalDay();
				arrivalDays.add(arrivalDay);
				report += String.format("\n%8d) %-20s%-15s$%-,19.2f%-15s", facilityCounter, fr.getName(), qty, cost,
						arrivalDay);
				totalQty += qty;
				totalCostItem += cost;
				facilityCounter++;
			}
			int maxArrivalDay = Collections.max(arrivalDays);
			int minArrivalDay = Collections.min(arrivalDays);
			report += String.format("\n  %8s%-20s%-15d$%-,19.2f%-15s", "", "TOTAL", totalQty, totalCostItem,
					maxArrivalDay == minArrivalDay ? "[" + maxArrivalDay + "]"
							: "[" + minArrivalDay + " - " + maxArrivalDay + "]");
			totalCost += totalCostItem;
		}
		report += String.format("\n\n  %-18s$%,.2f", "Total Cost:", totalCost);
		return report;
	}

}
