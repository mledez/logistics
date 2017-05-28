package logistics.inventory;

import java.util.Set;

public interface Inventory {
	public void put(String item, int qty);

	public Set<String> getIdSet();

	public int getQty(String id);

	public boolean containsId(String id);

	public void deduct(String item, int qty);
}
