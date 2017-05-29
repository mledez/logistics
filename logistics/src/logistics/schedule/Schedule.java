package logistics.schedule;

import logistics.exceptions.InvalidDataException;

public interface Schedule {
	public int calculateProcessingEndDay(int startDay, int qty) throws InvalidDataException;

	public void bookOrder(int startDay, int qty) throws InvalidDataException;

	public String getReport();
}
