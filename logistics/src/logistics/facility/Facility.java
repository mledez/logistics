package logistics.facility;

public interface Facility {

	public String getLocation();

	public int getDailyRate();

	public int getDailyCost();

	public String getReport();

	public boolean contains(String item);

	public int getItemCount(String item);

	public int quoteTime(String item, int day, int qty);

}
