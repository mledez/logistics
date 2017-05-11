package logistics.facility;

import java.util.Map;

import logistics.exceptions.InvalidDataException;

public class FacilityFactory {

	public static Facility createFacility(String location, int dailyRate, int dailyCost, Map<String, Integer> inventory)
			throws InvalidDataException {
		return new FacilityImpl(location, dailyRate, dailyCost, inventory);
	}
}
