package logistics.order.processor;

import logistics.exceptions.InvalidDataException;

public class FacilityRecord implements Comparable<FacilityRecord> {
	private String name;
	private int numberOfItems;
	private int processingEndDay;
	private float travelTime;

	public FacilityRecord(String name, int qtyTaken, int procEndDay, float travelTime) throws InvalidDataException {
		setName(name);
		setNumberOfItems(qtyTaken);
		setProcessingEndDay(procEndDay);
		setTravelTime(travelTime);
	}

	private void setName(String name) throws InvalidDataException {
		if (name == null || name.equals(""))
			throw new InvalidDataException("Facility name in Order Records can't be null or empty");
		this.name = name;
	}

	private void setNumberOfItems(int qtyTaken) throws InvalidDataException {
		if (qtyTaken < 1)
			throw new InvalidDataException(
					"Number of items taken from a Facility in Order Records can't be less than 1");
		this.numberOfItems = qtyTaken;
	}

	private void setProcessingEndDay(int procEndDay) throws InvalidDataException {
		if (procEndDay < 1)
			throw new InvalidDataException("Processing End Day in Order Records can't be less than 1");
		this.processingEndDay = procEndDay;
	}

	private void setTravelTime(float travelTime) throws InvalidDataException {
		if (travelTime <= 0)
			throw new InvalidDataException("Travel time in Order Records can't be zero or less");
		this.travelTime = travelTime;
	}

	public String getName() {
		return name;
	}

	public int getNumberOfItems() {
		return numberOfItems;
	}

	public int getProcessingEndDay() {
		return processingEndDay;
	}

	public float getTravelTime() {
		return travelTime;
	}

	public Integer getArrivalDay() {
		return (int) Math.ceil(getProcessingEndDay() + getTravelTime());
	}

	@Override
	public int compareTo(FacilityRecord o) {
		int comparison = getArrivalDay().compareTo(o.getArrivalDay());
		if (comparison == 0)
			return getName().compareTo(o.getName());
		else
			return comparison;
	}
}
