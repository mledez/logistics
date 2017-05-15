package logistics.order;

import java.util.Iterator;
import java.util.Map;

public class OrderImpl implements Order {
	private String id;
	private int day;
	private String destination;
	private Map<String, Integer> items;
	private Iterator<String> itemIterator;

	public OrderImpl(String id, int day, String destination, Map<String, Integer> items) {
		setId(id);
		setDay(day);
		setDestination(destination);
		setItems(items);
		setItemIterator(getItems().keySet().iterator());
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

	private Map<String, Integer> getItems() {
		return items;
	}

	private void setId(String id) {
		this.id = id;
	}

	private void setDay(int day) {
		this.day = day;
	}

	private void setDestination(String destination) {
		this.destination = destination;
	}

	private void setItems(Map<String, Integer> items) {
		this.items = items;
	}

	public String getNextItem() {
		if (getItemIterator().hasNext())
			return getItemIterator().next();
		return null;
	}

	private Iterator<String> getItemIterator() {
		return itemIterator;
	}

	private void setItemIterator(Iterator<String> itemIterator) {
		this.itemIterator = itemIterator;
	}

	public String toString() {
		return String.format("Order ID: %s\nOrder Time: %d\nDestination: %s\nOrder Items:\n%s\n", getId(), getDay(),
				getDestination(), getItems().toString());
	}

	public int getItemQty(String item) {
		return getItems().get(item);
	}

}
