package logistics.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import logistics.exceptions.InvalidDataException;

public class InventoryImpl implements Inventory {
	private Map<String, Integer> shelf = new TreeMap<>();

	private Map<String, Integer> getShelf() {
		return this.shelf;
	}

	public void add(String item, int qty) throws InvalidDataException {
		if (qty < 1)
			throw new InvalidDataException(
					String.format("Quantity of %s to be added to the inventory can't be less than 1", item));
		getShelf().put(item, qty);
	}

	public List<String> getIdList() {
		List<String> idList = new ArrayList<>();
		for (String id : getShelf().keySet())
			idList.add(id);
		return idList;
	}

	public int getQty(String id) {
		return getShelf().getOrDefault(id, 0);
	}

	public boolean containsId(String id) {
		return getShelf().containsKey(id);
	}

	public void deduct(String item, int qty) throws InvalidDataException {
		int currentQty = getShelf().get(item);
		if (qty > currentQty)
			throw new InvalidDataException(
					String.format("Quantity of units taken from the inventory (%d) can't exceed those on hand (%d)",
							qty, currentQty));
		int newQty = currentQty - qty;
		getShelf().put(item, newQty);
	}
}
