package logistics.path;

import java.util.List;

public interface PathProcessor {
	public List<String> findBestPath(String origin, String destination);
}
