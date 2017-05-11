package logistics.network;

public class LinkFactory {

	private LinkFactory() {}

	public static Link createLink(String origin, String destination, int distance) {
		return new LinkImpl(origin, destination, distance);
	}

}
