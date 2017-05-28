package logistics.facility;

import logistics.exceptions.InvalidDataException;
import logistics.inventory.Inventory;

public class FacilityFactory {

	public static Facility createFacility(String location, int dailyRate, int dailyCost, Inventory inventory)
			throws InvalidDataException {
		return new FacilityImpl(location, dailyRate, dailyCost, inventory);
	}
}
