package logistics.order.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import logistics.exceptions.InitializationException;
import logistics.exceptions.InvalidDataException;
import logistics.facility.FacilityManager;
import logistics.item.ItemManager;
import logistics.network.NetworkManager;
import logistics.order.OrderManager;

public class OrderProcessorImpl implements OrderProcessor {
	private ItemManager im;
	private FacilityManager fm;
	private NetworkManager nm;
	private OrderManager om;
	private int dailyTravelCost = 0;
	private Map<String, Map<String, List<FacilityRecord>>> solution;
	private Map<String, String> comments;

	public OrderProcessorImpl(int dailyTravelCost) {
		setIm(ItemManager.getInstance());
		setFm(FacilityManager.getInstance());
		setNm(NetworkManager.getInstance());
		setOm(OrderManager.getInstance());
		setDailyTravelCost(dailyTravelCost);
	}

	public void processOrders() throws InitializationException, InvalidDataException {
		setSolution(new LinkedHashMap<>());
		setComments(new HashMap<>());
		for (String orderId : getOm().getOrderIds()) {
			String destination = getOm().getOrderDestination(orderId);
			int orderDay = getOm().getOrderStartDay(orderId);
			LinkedHashMap<String, List<FacilityRecord>> orderSolution = new LinkedHashMap<>();
			for (String currentItem : getOm().getOrderedItemList(orderId)) {
				int orderQty = getOm().getOrderedItemQty(orderId, currentItem);
				if (getIm().contains(currentItem)) {
					ArrayList<FacilityRecord> itemSolution = new ArrayList<>();
					while (orderQty > 0) {
						List<String> whoHasIt = getFm().whoHasIt(currentItem);
						whoHasIt.remove(destination);
						if (whoHasIt.isEmpty()) {
							String comment = getComments().getOrDefault(orderId, "") + String.format(
									"  Remaining %d units of item %s were Back-Ordered.\n", orderQty, currentItem);
							getComments().put(orderId, comment);
							orderQty = 0;
						} else {
							List<FacilityRecord> records = new ArrayList<>();
							for (String currentFacility : whoHasIt) {
								int qtyAvailable = getFm().getItemCount(currentItem, currentFacility);
								int qtyTaken = Integer.min(qtyAvailable, orderQty);
								float travelTime = getNm().getDistanceInDays(currentFacility, destination);
								int procEndDay = getFm().calculateProcessingEndDay(currentFacility, orderDay, qtyTaken);
								records.add(new FacilityRecord(currentFacility, qtyTaken, procEndDay, travelTime));
							}
							Collections.sort(records);
							FacilityRecord fr = records.get(0);
							itemSolution.add(fr);
							getFm().bookOrder(fr.getName(), orderDay, currentItem, fr.getNumberOfItems());
							orderQty = orderQty - fr.getNumberOfItems();
						}
						orderSolution.put(currentItem, itemSolution);
					}
				} else {
					System.err.println("Item " + currentItem + " not in Catalog\n");
				}
			}
			getSolution().put(orderId, orderSolution);
		}
	}

	public String getReport() throws InitializationException, InvalidDataException {
		String report = "";
		String line = new String(new char[82]).replace("\0", "-");
		int orderCounter = 1;
		for (String orderId : getSolution().keySet()) {
			float totalCost = 0;
			report += String.format("Order #%d\n  %-15s%s\n  %-15s%s\n  %-15s%s\n\n  List of Order Items: ",
					orderCounter, "Order Id:", orderId, "Order Time: ", "Day " + getOm().getOrderStartDay(orderId),
					"Destination:", getOm().getOrderDestination(orderId));
			int itemCounter = 1;
			for (String item : getOm().getOrderedItemList(orderId)) {
				report += String.format("\n%8d) Item ID: %10s,      Quantity: %d", itemCounter, item,
						getOm().getOrderedItemQty(orderId, item));
				itemCounter++;
			}
			report += "\n\n  Processing Solution: ";
			for (String item : getSolution().get(orderId).keySet()) {
				int totalQty = 0;
				float totalCostItem = 0;
				report += String.format("\n\n  %s:\n  %8s%-20s%-15s%-20s%-15s", item, "", "Facility", "Quantity",
						"Cost", "Arrival Day");
				int facilityCounter = 1;
				List<Integer> arrivalDays = new ArrayList<>();
				for (FacilityRecord fr : getSolution().get(orderId).get(item)) {
					int qty = fr.getNumberOfItems();
					float cost = fr.getNumberOfItems() * getIm().getPrice(item)
							+ (float) fr.getNumberOfItems() / getFm().getDailyRate(fr.getName())
									* getFm().getDailyCost(fr.getName())
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
			if (getComments().containsKey(orderId))
				report += String.format("\n\n  %-18s$%,.2f\n\n  Comments:\n%s%s\n", "Total Cost:", totalCost,
						getComments().get(orderId), line);
			else
				report += String.format("\n\n  %-18s$%,.2f\n%s\n", "Total Cost:", totalCost, line);
			orderCounter++;
		}
		return report;
	}

	private void setIm(ItemManager im) {
		this.im = im;
	}

	private void setFm(FacilityManager fm) {
		this.fm = fm;
	}

	private void setNm(NetworkManager nm) {
		this.nm = nm;
	}

	private void setOm(OrderManager om) {
		this.om = om;
	}

	private void setDailyTravelCost(int dailyTravelCost) {
		this.dailyTravelCost = dailyTravelCost;
	}

	private void setSolution(Map<String, Map<String, List<FacilityRecord>>> solution) {
		this.solution = solution;
	}

	private void setComments(Map<String, String> comments) {
		this.comments = comments;
	}

	private ItemManager getIm() {
		return im;
	}

	private FacilityManager getFm() {
		return fm;
	}

	private NetworkManager getNm() {
		return nm;
	}

	private OrderManager getOm() {
		return om;
	}

	private Map<String, Map<String, List<FacilityRecord>>> getSolution() {
		return solution;
	}

	private Map<String, String> getComments() {
		return comments;
	}

	private int getDailyTravelCost() {
		return dailyTravelCost;
	}
}
