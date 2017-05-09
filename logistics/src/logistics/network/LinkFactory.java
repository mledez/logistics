package logistics.network;

public class LinkFactory {

	private LinkFactory() {}

	public static Link createLink(String type, String origin, String destination, int distance) {
		if (type.equals("Regular"))
			return new LinkImpl(origin, destination, distance);
		return null;
	}

}
