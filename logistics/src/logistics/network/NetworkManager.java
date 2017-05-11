package logistics.network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import logistics.exceptions.XmlDataException;
import logistics.loaders.NetworkLoader;
import logistics.path.PathProcessor;
import logistics.path.PathProcessorFactory;

public class NetworkManager {

	private List<Link> network;
	private Map<String, Integer> mappedNetwork = null;
	private int hoursDay = 0;
	private int milesHour = 0;
	private PathProcessor pathProcessor = null;
	private String separator = ":::";
	private boolean status = false;

	private static NetworkManager instance = new NetworkManager();

	public static NetworkManager getInstance() {
		return instance;
	}

	private NetworkManager() {}

	private List<Link> getNetwork() {
		return network;
	}

	private void setNetwork(List<Link> network) {
		this.network = network;
	}

	private int getHoursDay() {
		return hoursDay;
	}

	private void setHoursDay(int hoursDay) {
		this.hoursDay = hoursDay;
	}

	private int getMilesHour() {
		return milesHour;
	}

	private void setMilesHour(int milesHour) {
		this.milesHour = milesHour;
	}

	public void init(String fileName, int hoursDay, int milesHour) throws XmlDataException {
		setNetwork(NetworkLoader.load(fileName));
		setHoursDay(hoursDay);
		setMilesHour(milesHour);
		this.status = true;
	}

	public boolean getStatus() {
		return this.status;
	}

	private PathProcessor getPathProcessor() {
		if (this.pathProcessor == null) {
			PathProcessor pathProcessor = PathProcessorFactory.createPathProcessor(new HashMap<>(getMappedNetwork()));
			setPathProcessor(pathProcessor);
			this.pathProcessor = pathProcessor;
		}
		return this.pathProcessor;
	}

	private void setPathProcessor(PathProcessor pathProcessor) {
		this.pathProcessor = pathProcessor;
	}

	private Map<String, Integer> getMappedNetwork() {
		if (this.mappedNetwork == null) {
			Map<String, Integer> hashMappedNetwork = new HashMap<>();
			for (Link link : getNetwork()) {
				hashMappedNetwork.put(link.getOrigin() + getSeparator() + link.getDestination(), link.getDistance());
			}
			this.mappedNetwork = hashMappedNetwork;
		}
		return this.mappedNetwork;
	}

	private String getSeparator() {
		return separator;
	}

	public Map<String, Float> getNeighbors(String location) {
		Map<String, Float> neighbors = new TreeMap<>();
		Float travelTime;
		for (Link link : getNetwork()) {
			if (link.getOrigin().equals(location)) {
				travelTime = (float) link.getDistance() / (getHoursDay() * getMilesHour());
				neighbors.put(link.getDestination(), travelTime);
			}
		}
		return neighbors;
	}

	public String getNeighborsReport(String location) {
		Map<String, Float> neighbors = new TreeMap<>();
		Float travelTime;
		for (Link link : getNetwork()) {
			if (link.getOrigin().equals(location)) {
				travelTime = (float) link.getDistance() / (getHoursDay() * getMilesHour());
				neighbors.put(link.getDestination(), travelTime);
			}
		}
		String neighborsReport = "";
		String currentEntry;
		int lineCounter = 0;
		for (String neighbor : neighbors.keySet()) {
			currentEntry = String.format("%s (%.1fd); ", neighbor, neighbors.get(neighbor));
			if ((neighborsReport + currentEntry).length() - lineCounter * 86 <= 86)
				neighborsReport = neighborsReport + currentEntry;
			else {
				neighborsReport = neighborsReport + "\n" + currentEntry;
				lineCounter++;
			}
		}
		if (neighborsReport.equals(""))
			neighborsReport = "No neighbors found";

		return "Direct Links:\n" + neighborsReport + "\n";
	}

	public String getPathReport(String origin, String destination) {
		List<String> bestPath = getPathProcessor().findBestPath(origin, destination);
		String firstCity = bestPath.get(0);
		String secondCity;
		String sequence = firstCity;
		int totalDistance = 0;
		for (int i = 1; i < bestPath.size(); i++) {
			secondCity = bestPath.get(i);
			sequence = sequence + " ➔ " + secondCity;
			totalDistance = totalDistance + getDistance(firstCity, secondCity);
			firstCity = secondCity;
		}
		return String.format("%s to %s:\n%s = %,d mi\n%,d mi / (%d hours per day * %d mph) = %.2f days", origin,
				destination, sequence, totalDistance, totalDistance, getHoursDay(), getMilesHour(),
				(float) totalDistance / (getHoursDay() * getMilesHour()));
	}

	private int getDistance(String origin, String destination) {
		return getMappedNetwork().get(origin + getSeparator() + destination);
	}
}
