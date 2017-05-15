package logistics.order;

public interface Order {
	public String getNextItem();

	public String getDestination();

	public int getItemQty(String item);

	public String getId();

	public int getDay();
}
