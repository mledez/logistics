package logistics.path;

import java.util.Map;

import logistics.exceptions.InvalidDataException;

public class PathProcessorFactory {
	private PathProcessorFactory() {}

	public static PathProcessor createPathProcessor(Map<String, Integer> network) throws InvalidDataException {
		return new PathProcessorImpl(network);
	}
}
