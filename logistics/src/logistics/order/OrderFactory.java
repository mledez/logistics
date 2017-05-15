package logistics.order;

import java.util.Map;

public class OrderFactory {

	public static Order createOrder(String id, int day, String destination, Map<String, Integer> items) {
		return new OrderImpl(id, day, destination, items);
	}

}