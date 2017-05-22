package logistics.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import logistics.exceptions.InitializationException;
import logistics.exceptions.InvalidDataException;
import logistics.exceptions.XmlReadingException;
import logistics.facility.FacilityManager;
import logistics.facility.FacilityRecord;
import logistics.item.ItemManager;
import logistics.loaders.OrderLoader;
import logistics.network.NetworkManager;

public class OrderManager {

	private static OrderManager ourInstance = new OrderManager();

	public static OrderManager getInstance() {
		return ourInstance;
	}

	private OrderManager() {}

	private List<Order> orders;

	private boolean status = false;

	public void init(String fileName) throws XmlReadingException, InvalidDataException {
		setOrders(OrderLoader.load(fileName));
		setStatus(true);
	}

	private List<Order> getOrders() {
		return orders;
	}

	private boolean isStatus() {
		return status;
	}

	private void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	private void setStatus(boolean status) {
		this.status = status;
	}

	public String getReport() {
		String report = "";
		for (Order order : getOrders()) {
			report += order.toString();
		}
		return report;
	}

	public void startProcessing() throws InitializationException, InvalidDataException {
		ItemManager im = ItemManager.getInstance();
		FacilityManager fm = FacilityManager.getInstance();
		NetworkManager nm = NetworkManager.getInstance();
		for (Order order : getOrders()) {
			System.out.println("Order " + order.getId());
			String destination = order.getDestination();
			int orderDay = order.getDay();
			String currentItem = order.getNextItem();
			while (currentItem != null) {
				System.out.println("Current Item: " + currentItem);
				int orderQty = order.getItemQty(currentItem);
				if (im.contains(currentItem)) {
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
								records.add(new FacilityRecord(currentFacility, qtyAvailable, qtyTaken, procEndDay,
										travelTime));
							}
							Collections.sort(records);
							FacilityRecord fr = records.get(0);
							fm.reduceInventory(fr.getName(), currentItem, fr.getQtyTaken());
							orderQty = orderQty - fr.getQtyTaken();
							float billableProcTime = fm.bookOrder(fr.getName(), order.getDay(), fr.getQtyTaken());
							float totalCost = fr.getQtyTaken() * im.getPrice(currentItem)
									+ billableProcTime * fm.getDailyCost(fr.getName())
									+ (float) Math.ceil(fr.getTravelTime()) * 500;

							System.out.println("Facility: " + fr.getName() + " Qty: " + fr.getQtyTaken()
									+ " Arrival Day: " + ((int) Math.ceil(fr.getTravelTime()) + fr.getProcEndDay())
									+ " Cost: " + totalCost);
							System.out.println("Qty taken: " + fr.getQtyTaken() + " Item price: "
									+ im.getPrice(currentItem) + " Billable processing time: " + billableProcTime
									+ " Processing cost (day): " + fm.getDailyCost(fr.getName()) + " Travel Time: "
									+ (float) Math.ceil(fr.getTravelTime()));

						}
					}

				} else {
					System.err.println("Item " + currentItem + " not in Catalog\n");
				}
				currentItem = order.getNextItem();
			}
		}
	}
}