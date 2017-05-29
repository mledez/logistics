package logistics.inventory;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class InventoryImpl implements Inventory {
	private Map<String, Integer> shelf = new TreeMap<>();

	private Map<String, Integer> getShelf() {
		return this.shelf;
	}

	public void put(String item, int qty) {
		getShelf().put(item, qty);
	}

	public Set<String> getIdSet() {
		return getShelf().keySet();
	}

	public int getQty(String id) {
		return getShelf().get(id);
	}

	public boolean containsId(String id) {
		return getShelf().containsKey(id);
	}

	public void deduct(String item, int qty) {
		int currentQty = getShelf().get(item);
		int newQty = currentQty - qty;
		getShelf().put(item, newQty);
	}
}
