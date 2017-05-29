package logistics.order;

import java.util.Map;

import logistics.exceptions.InvalidDataException;

public class OrderFactory {

	public static Order createOrder(String id, int day, String destination, Map<String, Integer> items)
			throws InvalidDataException {
		return new OrderImpl(id, day, destination, items);
	}

}