package logistics.facility;

import logistics.exceptions.InvalidDataException;
import logistics.inventory.Inventory;
import logistics.schedule.Schedule;

public class FacilityFactory {

	public static Facility createFacility(String location, int dailyRate, int dailyCost, Inventory inventory,
			Schedule schedule) throws InvalidDataException {
		return new FacilityImpl(location, dailyRate, dailyCost, inventory, schedule);
	}
}
