package logistics.path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PathProcessorImpl implements PathProcessor {

	private Map<String, Integer> network;;
	private List<String> lowPath;
	private String separator = ":::";

	public PathProcessorImpl(Map<String, Integer> network) {
		setNetwork(network);
	}

	private void setNetwork(Map<String, Integer> network) {
		this.network = network;

	}

	private Map<String, Integer> getNetwork() {
		return network;
	}

	private List<String> getLowPath() {
		return lowPath;
	}

	private void setLowPath(List<String> lowPath) {
		this.lowPath = lowPath;
	}

	private String getSeparator() {
		return separator;
	}

	public List<String> findBestPath(String start, String end) {
		setLowPath(new ArrayList<>());
		List<String> pathList = new ArrayList<>();
		pathList.add(start);
		findPath(start, end, pathList);
		return getLowPath();
	}

	private void findPath(String start, String end, List<String> pathList) {
		if (start.equals(end)) {
			if (getLowPath().isEmpty()) {
				setLowPath(pathList);
				return;
			} else {
				if (getTotalDistance(pathList) < getTotalDistance(lowPath)) {
					setLowPath(pathList);
				} else {
					return;
				}
			}
		} else {
			Set<String> fromHere = new HashSet<>();
			for (String link : getNetwork().keySet()) {
				String origin = link.split(getSeparator(), 2)[0];
				if (origin.equals(start)) {
					fromHere.add(link);
				}
			}
			for (String link : fromHere) {
				String destination = link.split(getSeparator(), 2)[1];
				if (!pathList.contains(destination)) {
					ArrayList<String> newPath = new ArrayList<>(pathList);
					newPath.add(destination);
					findPath(destination, end, newPath);
				}
			}
			return;
		}
	}

	public int getTotalDistance(List<String> path) {
		int length = 0;
		for (int i = 0; i < path.size() - 1; i++) {
			String pair = path.get(i) + getSeparator() + path.get(i + 1);
			length = length + getNetwork().get(pair);
		}
		return length;
	}

}
