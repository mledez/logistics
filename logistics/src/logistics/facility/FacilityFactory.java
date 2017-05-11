package logistics.facility;

import java.util.Map;

public class FacilityFactory {

	public static Facility createFacility(String location, int dailyRate, int dailyCost,
			Map<String, Integer> inventory) {
		return new FacilityImpl(location, dailyRate, dailyCost, inventory);
	}
}
