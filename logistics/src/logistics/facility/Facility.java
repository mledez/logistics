package logistics.facility;

public interface Facility {

	public String getLocation();

	public int getDailyRate();

	public int getDailyCost();

	public String getReport();

	public boolean containsItem(String item);

	public int getItemCount(String item);

	public int quoteTime(int day, int qty);

	public void bookOrder(int day, String item, int qty);

}
