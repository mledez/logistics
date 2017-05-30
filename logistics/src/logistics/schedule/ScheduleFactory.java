package logistics.schedule;

import logistics.exceptions.InvalidDataException;

public class ScheduleFactory {
	private ScheduleFactory() {}

	public static Schedule createSchedule(int dailyRate) throws InvalidDataException {
		return new ScheduleImpl(dailyRate);
	}
}
