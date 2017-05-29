package logistics.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import logistics.exceptions.InvalidDataException;

public class OrderImpl implements Order {
	private String id;
	private int day;
	private String destination;
	private Map<String, Integer> items;

	public OrderImpl(String id, int day, String destination, Map<String, Integer> items) throws InvalidDataException {
		setId(id);
		setDay(day);
		setDestination(destination);
		setItems(items);
	}

	private Map<String, Integer> getItems() {
		return items;
	}

	private void setId(String id) throws InvalidDataException {
		if (id == null || id.equals(""))
			throw new InvalidDataException("Order Id can't be null or empty");
		this.id = id;
	}

	private void setDay(int day) throws InvalidDataException {
		if (day < 1)
			throw new InvalidDataException("Order start day can't be less than 1");
		this.day = day;
	}

	private void setDestination(String destination) throws InvalidDataException {
		if (destination == null || destination.equals(""))
			throw new InvalidDataException("Order destination can't be null or empty");
		this.destination = destination;
	}

	private void setItems(Map<String, Integer> items) throws InvalidDataException {
		if (items == null)
			throw new InvalidDataException("Order Items can't be null");
		this.items = items;
	}

	public String getId() {
		return id;
	}

	public int getDay() {
		return day;
	}

	public String getDestination() {
		return destination;
	}

	public int getItemQty(String item) {
		return getItems().getOrDefault(item, 0);
	}

	public List<String> getItemList() {
		List<String> itemList = new ArrayList<>();
		for (String item : getItems().keySet())
			itemList.add(item);
		return itemList;
	}

}
