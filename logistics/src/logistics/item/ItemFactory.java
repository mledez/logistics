package logistics.item;

public class ItemFactory {
	private ItemFactory() {}

	public static Item createItem(String type, String id, int price) {
		if (type.equals("Regular"))
			return new ItemImpl(id, price);
		return null;
	}
}