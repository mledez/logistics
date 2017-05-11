package logistics.facility;

import java.util.List;

import logistics.exceptions.InitializationException;
import logistics.exceptions.XmlDataException;
import logistics.loaders.FacilityLoader;
import logistics.network.NetworkManager;

public class FacilityManager {

	private static FacilityManager ourInstance = new FacilityManager();
	private List<Facility> facilities;
	private boolean status = false;

	public static FacilityManager getInstance() {
		return ourInstance;
	}

	private FacilityManager() {}

	public void init(String fileName) throws XmlDataException {
		this.facilities = FacilityLoader.load(fileName);
		this.status = true;
	}

	public boolean getStatus() {
		return this.status;
	}

	public String getReport() throws InitializationException {
		String line = new String(new char[82]).replace("\0", "-") + "\n";
		String report = line;
		NetworkManager nm = NetworkManager.getInstance();
		String neighbors = "";
		for (Facility facility : facilities) {
			if (nm.getStatus())
				neighbors = nm.getNeighborsReport(facility.getLocation());
			else {
				throw new InitializationException("Network is not initialized");
			}
			report = report + facility.getReport().replaceFirst("\\n\\n", "\n\n" + neighbors + "\n") + line;
		}
		if (report == line)
			return "No facilities found";
		else
			return report;
	}
}
