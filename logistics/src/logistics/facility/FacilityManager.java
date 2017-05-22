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

	public static FacilityManager getInstance() {
		return ourInstance;
	}

	private List<Facility> facilities;

	private boolean status = false;

	private FacilityManager() {}

	public String getReport() throws InitializationException {
		if (!getStatus())
			throw new InitializationException("Facility manager is not initialized");
		String line = new String(new char[82]).replace("\0", "-") + "\n";
		String report = line;
		NetworkManager nm = NetworkManager.getInstance();
		String neighbors = "";
		for (Facility facility : getFacilities()) {
			neighbors = nm.getNeighborsReport(facility.getLocation());
			report = report + facility.getReport().replaceFirst("\\n\\n", "\n\n" + neighbors + "\n") + line;
		}
		if (report == line)
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

	public List<String> whoHasIt(String item, String destination) {
		List<String> hasIt = new ArrayList<>();
		for (Facility facility : getFacilities()) {
			if (!facility.getLocation().equals(destination) && facility.contains(item))
				hasIt.add(facility.getLocation());
		}
		return hasIt;
	}

	public int getItemCount(String item, String location) {
		return getFacility(location).getItemCount(item);
	}

	private Facility getFacility(String location) {
		for (Facility facility : getFacilities()) {
			if (facility.getLocation().equals(location))
				return facility;
		}
		return null;
	}

	public int getDailyCost(String location) {
		return getFacility(location).getDailyCost();
	}

	public int quoteTime(String location, int day, int qty) {
		return getFacility(location).quoteTime(day, qty);
	}

	public void reduceInventory(String location, String item, int qty) {
		getFacility(location).reduceInventory(item, qty);
	}

	public float bookOrder(String location, int day, int qty) {
		return getFacility(location).bookOrder(day, qty);
	}
}
