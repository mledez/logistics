package logistics.network;

public class LinkImpl implements Link {

	private String origin;
	private String destination;
	private int distance;
	private boolean status = true;

	public String getOrigin() {
		return origin;
	}

	private void setOrigin(String origin) {
		if (origin.trim().isEmpty())
			setStatus(false);
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	private void setDestination(String destination) {
		if (destination.trim().isEmpty())
			setStatus(false);
		this.destination = destination;
	}

	public int getDistance() {
		return distance;
	}

	public boolean getStatus() {
		return status;
	}

	private void setStatus(boolean status) {
		this.status = status;
	}

	private void setDistance(int distance) {
		if (distance < 1)
			setStatus(false);
		this.distance = distance;

	}

	public LinkImpl(String origin, String destination, int distance) {
		setOrigin(origin);
		setDestination(destination);
		setDistance(distance);
	}
}
