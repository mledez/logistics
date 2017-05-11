package logistics.item;

import logistics.exceptions.InvalidDataException;

public class ItemImpl implements Item {

	private String id;
	private int price;

	private void setId(String id) throws InvalidDataException {
		if (id == null || id.equals(""))
			throw new InvalidDataException("Item ID can't be null or empty");
		this.id = id;
	}

	private void setPrice(int price) throws InvalidDataException {
		if (price < 1)
			throw new InvalidDataException("Item price can't be less than 1");
		this.price = price;
	}

	public String getId() {
		return this.id;
	}

	public ItemImpl(String id, int price) throws InvalidDataException {
		setId(id);
		setPrice(price);
	}

	public int getPrice() {
		return this.price;
	}
}
