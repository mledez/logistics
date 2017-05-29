package logistics.order;

import java.util.List;

public interface Order {
	public String getDestination();

	public int getItemQty(String item);

	public String getId();

	public int getDay();

	public List<String> getItemList();
}
