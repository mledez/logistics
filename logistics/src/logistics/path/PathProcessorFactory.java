package logistics.path;

import java.util.Map;

public class PathProcessorFactory {
	private PathProcessorFactory() {}

	public static PathProcessor createPathProcessor(Map<String, Integer> network) {
		return new PathProcessorImpl(network);
	}
}
