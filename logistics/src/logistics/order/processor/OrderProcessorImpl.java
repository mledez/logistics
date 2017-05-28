package logistics.order.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import logistics.exceptions.InitializationException;
import logistics.exceptions.InvalidDataException;
import logistics.facility.FacilityManager;
import logistics.facility.FacilityRecord;
import logistics.item.ItemManager;
import logistics.network.NetworkManager;
import logistics.order.OrderManager;

public class OrderProcessorImpl {

	private static OrderProcessorImpl ourInstance = new OrderProcessorImpl();
	private ItemManager im;
	private FacilityManager fm;
	private NetworkManager nm;
	private OrderManager om;
	private int dailyTravelCost = 0;

	// Insertion ordered HashMap <OrderId, Solution>
	private Map<String, Map<String, List<FacilityRecord>>> solution;

	public static OrderProcessorImpl getInstance() {
		return ourInstance;
	}

	private OrderProcessorImpl() {
		this.im = ItemManager.getInstance();
		this.fm = FacilityManager.getInstance();
		this.nm = NetworkManager.getInstance();
		this.om = OrderManager.getInstance();
	}

	public void init(int dailyTravelCost) {
		setDailyTravelCost(dailyTravelCost);
	}

	private void setDailyTravelCost(int dailyTravelCost) {
		this.dailyTravelCost = dailyTravelCost;
	}

	private int getDailyTravelCost() {
		return dailyTravelCost;
	}

	public void startProcessing() throws InitializationException, InvalidDataException {
		solution = new LinkedHashMap<>();
		for (String orderId : om.getOrderIds()) {
			String destination = om.getOrderDestination(orderId);
			int orderDay = om.getOrderStartDay(orderId);

			// Insertion ordered HashMap <ItemId, SelectedFacilities>
			LinkedHashMap<String, List<FacilityRecord>> orderSolution = new LinkedHashMap<>();

			for (String currentItem : om.getOrderedItemList(orderId)) {
				int orderQty = om.getOrderedItemQty(orderId, currentItem);
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
								records.add(new FacilityRecord(currentFacility, qtyTaken, procEndDay, travelTime));
							}
							Collections.sort(records);
							FacilityRecord fr = records.get(0);
							itemSolution.add(fr);
							fm.bookOrder(fr.getName(), orderDay, currentItem, fr.getNumberOfItems());
							orderQty = orderQty - fr.getNumberOfItems();
						}
						orderSolution.put(currentItem, itemSolution);
					}
				} else {
					System.err.println("Item " + currentItem + " not in Catalog\n");
				}
			}
			solution.put(orderId, orderSolution);
		}
	}

	public String getReport() {
		String report = "";
		String line = new String(new char[82]).replace("\0", "-");
		int orderCounter = 1;
		for (String orderId : solution.keySet()) {
			float totalCost = 0;
			report += String.format("Order #%d\n  %-15s%s\n  %-15s%s\n  %-15s%s\n\n  List of Order Items: ",
					orderCounter, "Order Id:", orderId, "Order Time: ", "Day " + om.getOrderStartDay(orderId),
					"Destination:", om.getOrderDestination(orderId));
			int itemCounter = 1;
			for (String item : om.getOrderedItemList(orderId)) {
				report += String.format("\n%8d) Item ID: %10s,      Quantity: %d", itemCounter, item,
						om.getOrderedItemQty(orderId, item));
				itemCounter++;
			}
			report += "\n\n  Processing Solution: ";
			for (String item : solution.get(orderId).keySet()) {
				int totalQty = 0;
				float totalCostItem = 0;
				report += String.format("\n\n  %s:\n  %8s%-20s%-15s%-20s%-15s", item, "", "Facility", "Quantity",
						"Cost", "Arrival Day");
				int facilityCounter = 1;
				List<Integer> arrivalDays = new ArrayList<>();
				for (FacilityRecord fr : solution.get(orderId).get(item)) {
					int qty = fr.getNumberOfItems();
					float cost = fr.getNumberOfItems() * im.getPrice(item)
							+ (float) fr.getNumberOfItems() / fm.getDailyRate(fr.getName())
									* fm.getDailyCost(fr.getName())
							+ (float) Math.ceil(fr.getTravelTime()) * getDailyTravelCost();
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
			report += String.format("\n\n  %-18s$%,.2f\n%s\n", "Total Cost:", totalCost, line);
			orderCounter++;
		}
		return report;
	}
}
