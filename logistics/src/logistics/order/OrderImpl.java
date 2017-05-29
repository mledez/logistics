package logistics.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderImpl implements Order {
	private String id;
	private int day;
	private String destination;
	private Map<String, Integer> items;

	public OrderImpl(String id, int day, String destination, Map<String, Integer> items) {
		setId(id);
		setDay(day);
		setDestination(destination);
		setItems(items);
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
		return getItems().get(item);
	}

	public List<String> getItemList() {
		List<String> itemList = new ArrayList<>();
		for (String item : getItems().keySet())
			itemList.add(item);
		return itemList;
	}

}
