package logistics.facility;

import java.util.Map;

public class FacilityFactory {

	public static Facility createFacility(String criteria, String location, int dailyRate, int dailyCost,
			Map<String, Integer> inventory) {
		if (criteria.equals("Regular"))
			return new FacilityImpl(location, dailyRate, dailyCost, inventory);

		return null;
	}
}
