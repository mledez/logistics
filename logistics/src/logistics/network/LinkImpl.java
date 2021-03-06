package logistics.network;

import logistics.exceptions.InvalidDataException;

public class LinkImpl implements Link {
	private String origin;
	private String destination;
	private int distance;

	public LinkImpl(String origin, String destination, int distance) throws InvalidDataException {
		setOrigin(origin);
		setDestination(destination);
		setDistance(distance);
	}

	private void setOrigin(String origin) throws InvalidDataException {
		if (origin == null || origin.equals(""))
			throw new InvalidDataException("Link origin can't be null or empty");
		this.origin = origin;
	}

	private void setDestination(String destination) throws InvalidDataException {
		if (origin == null || origin.equals(""))
			throw new InvalidDataException("Link destination can't be null or empty");
		this.destination = destination;
	}

	private void setDistance(int distance) throws InvalidDataException {
		if (distance < 1)
			throw new InvalidDataException("Distance between locations (links) can't be less than 1");
		this.distance = distance;
	}

	public String getOrigin() {
		return origin;
	}

	public String getDestination() {
		return destination;
	}

	public int getDistance() {
		return this.distance;
	}
}
