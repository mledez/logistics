package logistics.facility;

import java.util.ArrayList;
import java.util.List;

import logistics.exceptions.InitializationException;
import logistics.exceptions.InvalidDataException;
import logistics.exceptions.XmlReadingException;
import logistics.loaders.FacilityLoader;
import logistics.network.NetworkManager;

public class FacilityManager {
	private static FacilityManager ourInstance = new FacilityManager();
	private List<Facility> facilities;
	private boolean status = false;

	private FacilityManager() {}

	private List<Facility> getFacilities() {
		return this.facilities;
	}

	private void setFacilities(List<Facility> facilities) throws InvalidDataException {
		if (facilities == null)
			throw new InvalidDataException("A list of facilities can't be null");
		this.facilities = facilities;
	}

	private void setStatus(boolean status) {
		this.status = status;
	}

	private Facility getFacility(String location) {
		for (Facility facility : getFacilities()) {
			if (facility.getLocation().equals(location))
				return facility;
		}
		return null;
	}

	public static FacilityManager getInstance() {
		return ourInstance;
	}

	public String getReport() throws InitializationException {
		if (!getStatus())
			throw new InitializationException("Facility manager is not initialized");
		String line = new String(new char[82]).replace("\0", "-") + "\n";
		String report = "";
		NetworkManager nm = NetworkManager.getInstance();
		String neighbors = "";
		for (Facility facility : getFacilities()) {
			neighbors = nm.getNeighborsReport(facility.getLocation());
			report = report + facility.getReport().replaceFirst("\\n\\n", "\n\n" + neighbors + "\n") + line;
		}
		if (report.equals(""))
			return "No facilities found";
		else
			return report;
	}

	public boolean getStatus() {
		return this.status;
	}

	public void init(String fileName) throws XmlReadingException, InvalidDataException {
		setFacilities(FacilityLoader.load(fileName));
		setStatus(true);
	}

	public List<String> whoHasIt(String item) {
		List<String> hasIt = new ArrayList<>();
		for (Facility facility : getFacilities()) {
			if (facility.containsItem(item))
				hasIt.add(facility.getLocation());
		}
		return hasIt;
	}

	public int getItemCount(String item, String location) {
		return getFacility(location).getItemCount(item);
	}

	public int getDailyCost(String location) {
		return getFacility(location).getDailyCost();
	}

	public int getDailyRate(String location) {
		return getFacility(location).getDailyRate();
	}

	public int calculateProcessingEndDay(String location, int day, int qty) {
		return getFacility(location).calculateProcessingEndDay(day, qty);
	}

	public void bookOrder(String location, int day, String item, int qty) {
		getFacility(location).bookOrder(day, item, qty);
	}
}
