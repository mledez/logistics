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

	private void checkStatus() throws InitializationException {
		if (!status)
			throw new InitializationException("Order Manager is not initialized");
	}

	private void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	private void setStatus(boolean status) {
		this.status = status;
	}

	private OrderProcessor getOp() {
		return op;
	}

	private void setOp(OrderProcessor op) {
		this.op = op;
	}

	public static OrderManager getInstance() {
		return ourInstance;
	}

	public void init(String fileName, int dailyTravelCost)
			throws XmlReadingException, DuplicatedDataException, InitializationException, InvalidDataException {
		setOrders(OrderLoader.load(fileName));
		setStatus(true);
		setOp(new OrderProcessorImpl(dailyTravelCost));
	}

	public List<String> getOrderIds() throws InitializationException {
		checkStatus();
		ArrayList<String> orderIds = new ArrayList<>();
		for (Order order : getOrders()) {
			orderIds.add(order.getId());
		}
		return orderIds;
	}

	public String getOrderDestination(String orderId) throws InitializationException {
		checkStatus();
		return getOrder(orderId).getDestination();
	}

	public int getOrderStartDay(String orderId) throws InitializationException {
		checkStatus();
		return getOrder(orderId).getDay();
	}

	public List<String> getOrderedItemList(String orderId) throws InitializationException {
		checkStatus();
		return getOrder(orderId).getItemList();
	}

	public int getOrderedItemQty(String orderId, String itemId) throws InitializationException {
		checkStatus();
		return getOrder(orderId).getItemQty(itemId);
	}

	public void processOrders() throws InitializationException, InvalidDataException {
		checkStatus();
		getOp().processOrders();
	}

	public String getProcessingReport() throws InitializationException, InvalidDataException {
		checkStatus();
		return getOp().getReport();
	}
}