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
			String currentItem = order.getNextItem();
			while (currentItem != null) {
				System.out.println("Current Item: " + currentItem);
				int orderQty = order.getItemQty(currentItem);
				if (im.contains(currentItem)) {
					List<String> whoHasIt = fm.whoHasIt(currentItem, order.getDestination());
					if (whoHasIt.isEmpty()) {
						System.err.println("Generate Back-Order for item " + currentItem + " \n");
					} else {
						List<FacilityRecord> records = new ArrayList<>();
						for (String currentFacility : whoHasIt) {
							// System.out.println("Current Facility " + currentFacility);
							int qtyAvailable = fm.getItemCount(currentItem, currentFacility);
							int qtyNeeded = order.getItemQty(currentItem);
							int qtyTaken = Integer.min(qtyAvailable, qtyNeeded);
							float travelTime = nm.getDistanceInDays(currentFacility, order.getDestination());
							int procEndDay = fm.quoteTime(currentFacility, order.getDay(), qtyTaken);
							records.add(new FacilityRecord(currentFacility, qtyAvailable, qtyTaken, procEndDay,
									travelTime));
						}
						Collections.sort(records);
						FacilityRecord fr = records.get(0);
						System.out.println("Facility: " + fr.getName() + " Qty: " + fr.getQtyTaken() + " Travel time: "
								+ (int) Math.ceil(fr.getTravelTime()) + " Proccesing End Day: " + fr.getProcEndDay());
						fm.reduceInventory(fr.getName(), currentItem, fr.getQtyTaken());
						orderQty = orderQty - fr.getQtyTaken();
						fm.scheduleOrder(fr.getName(), order.getDay(), fr.getQtyTaken());

						// for (FacilityRecord fr : records)
						// System.out.println(fr.getName());
					}

				} else {
					System.err.println("Item " + currentItem + " not in Catalog\n");
				}
				currentItem = order.getNextItem();
			}
		}
	}
}