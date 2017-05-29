package logistics.inventory;

import java.util.List;

import logistics.exceptions.InvalidDataException;

public interface Inventory {
	public void add(String item, int qty) throws InvalidDataException;

	public List<String> getIdList();

	public int getQty(String id);

	public boolean containsId(String id);

	public void deduct(String item, int qty) throws InvalidDataException;
}
