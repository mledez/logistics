package logistics.order;

import java.util.ArrayList;
import java.util.List;

import logistics.exceptions.InitializationException;
import logistics.exceptions.InvalidDataException;
import logistics.exceptions.XmlReadingException;
import logistics.loaders.OrderLoader;
import logistics.order.processor.OrderProcessorImpl;

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

	public List<String> getOrderIds() {
		ArrayList<String> orderIds = new ArrayList<>();
		for (Order order : getOrders()) {
			orderIds.add(order.getId());
		}
		return orderIds;
	}

	public String getOrderDestination(String orderId) {
		return getOrder(orderId).getDestination();
	}

	public int getOrderStartDay(String orderId) {
		return getOrder(orderId).getDay();
	}

	public String getOrderNextItem(String orderId) {
		return getOrder(orderId).getNextItem();
	}

	public int getOrderedItemQty(String orderId, String itemId) {
		return getOrder(orderId).getItemQty(itemId);
	}

	private Order getOrder(String orderId) {
		for (Order order : getOrders()) {
			if (order.getId().equals(orderId))
				return order;
		}
		return null;
	}

	public void startProcessing(int dailyTravelCost) throws InitializationException, InvalidDataException {
		OrderProcessorImpl op = OrderProcessorImpl.getInstance();
		op.init(dailyTravelCost);
		op.startProcessing();
	}
}