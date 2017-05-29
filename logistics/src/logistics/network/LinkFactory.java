package logistics.network;

import logistics.exceptions.InvalidDataException;

public class LinkFactory {
	private LinkFactory() {}

	public static Link createLink(String origin, String destination, int distance) throws InvalidDataException {
		return new LinkImpl(origin, destination, distance);
	}
}
