package logistics.order;

import java.util.ArrayList;
import java.util.List;

import logistics.exceptions.DuplicatedDataException;
import logistics.exceptions.InitializationException;
import logistics.exceptions.InvalidDataException;
import logistics.exceptions.XmlReadingException;
import logistics.loaders.OrderLoader;
import logistics.order.processor.OrderProcessor;
import logistics.order.processor.OrderProcessorImpl;

public class OrderManager {
	private static OrderManager ourInstance = new OrderManager();
	private OrderProcessor op;
	private List<Order> orders;
	private boolean status = false;

	private OrderManager() {}

	private Order getOrder(String orderId) {
		for (Order order : getOrders()) {
			if (order.getId().equals(orderId))
				return order;
		}
		return null;
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

	public static OrderManager getInstance() {
		return ourInstance;
	}

	public void init(String fileName, int dailyTravelCost) throws XmlReadingException, DuplicatedDataException {
		setOrders(OrderLoader.load(fileName));
		setStatus(true);
		op = new OrderProcessorImpl(dailyTravelCost);
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

	public List<String> getOrderedItemList(String orderId) {
		return getOrder(orderId).getItemList();
	}

	public int getOrderedItemQty(String orderId, String itemId) {
		return getOrder(orderId).getItemQty(itemId);
	}

	public void processOrders() throws InitializationException, InvalidDataException {
		op.processOrders();
	}

	public String getProcessingReport() throws InitializationException, InvalidDataException {
		return op.getReport();
	}
}