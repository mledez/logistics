package logistics.item;

public class ItemFactory {
	private ItemFactory() {}

	public static Item createItem(String id, int price) {
		return new ItemImpl(id, price);
	}
}