package logistics.facility;

import java.util.List;

import logistics.loaders.FacilityLoader;

public class FacilityManager {

	private static FacilityManager ourInstance = new FacilityManager();
	private static List<Facility> facilities = FacilityLoader.load("Facilities.xml");

	public static FacilityManager getInstance() {
		return ourInstance;
	}

	private FacilityManager() {}

	public String getReport() {
		String line = new String(new char[82]).replace("\0", "-") + "\n";
		String report = line;
		for (Facility facility : facilities) {
			report = report + facility.getReport() + line;
		}
		if (report == line)
			return "No facilities found";
		else
			return report;
	}

	public void printReport() {
		System.out.println(getReport());
	}

	@Override
	public String toString() {
		return getReport();
	}
}
