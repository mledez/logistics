package logistics.facility;

import logistics.exceptions.InvalidDataException;

public interface Facility {

	public String getLocation();

	public int getDailyRate();

	public int getDailyCost();

	public String getReport();

	public boolean containsItem(String item);

	public int getItemCount(String item);

	public int calculateProcessingEndDay(int day, int qty) throws InvalidDataException;

	public void bookOrder(int day, String item, int qty) throws InvalidDataException;

}
