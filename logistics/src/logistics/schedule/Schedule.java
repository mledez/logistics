package logistics.schedule;

public interface Schedule {
	public int getEndDay(int startDay, int qty);

	public void bookOrder(int startDay, int qty);

	public String getReport();
}
