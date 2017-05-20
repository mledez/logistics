package logistics.inventory;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class InventoryImpl {
	private Map<String, Integer> shelf = new TreeMap<>();

	public void put(String item, int qty) {
		getShelf().put(item, qty);
	}

	private Map<String, Integer> getShelf() {
		return this.shelf;
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
}
