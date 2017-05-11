package logistics.item;

import logistics.exceptions.InvalidDataException;

public class ItemFactory {
	private ItemFactory() {}

	public static Item createItem(String id, int price) throws InvalidDataException {
		return new ItemImpl(id, price);
	}
}