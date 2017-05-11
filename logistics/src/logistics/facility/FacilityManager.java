package logistics.facility;

import java.util.List;

import logistics.exceptions.InitializationException;
import logistics.exceptions.InvalidDataException;
import logistics.exceptions.XmlDataException;
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

	public void init(String fileName) throws XmlDataException, InvalidDataException {
		setFacilities(FacilityLoader.load(fileName));
		setStatus(true);
	}

	private List<Facility> getFacilities() {
		return facilities;
	}

	private void setFacilities(List<Facility> facilities) throws InvalidDataException {
		if (facilities == null)
			throw new InvalidDataException("A list of facilities can't be null");
		this.facilities = facilities;
	}

	private void setStatus(boolean status) {
		this.status = status;
	}
}
