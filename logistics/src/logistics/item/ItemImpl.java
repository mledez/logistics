package logistics.item;

public class ItemImpl implements Item {

	private String id = null;
	private int price = 0;

	@Override
	public String getId() {
		return this.id;
	}

	public ItemImpl(String id, int price) {
		this.id = id;
		this.price = price;
	}

	@Override
	public int getPrice() {
		return this.price;
	}

	@Override
	public boolean getStatus() {
		if (id == null || price < 1)
			return false;
		return true;
	}

}
