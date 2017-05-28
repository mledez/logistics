package logistics.facility;

public class FacilityRecord implements Comparable<FacilityRecord> {
	private String name;
	private int numberOfItems;
	private int processingEndDay;
	private float travelTime;

	public FacilityRecord(String name, int qtyTaken, int procEndDay, float travelTime) {
		setName(name);
		setNumberOfItems(qtyTaken);
		setProcessingEndDay(procEndDay);
		setTravelTime(travelTime);
	}

	private void setName(String name) {
		this.name = name;
	}

	private void setNumberOfItems(int qtyTaken) {
		this.numberOfItems = qtyTaken;
	}

	private void setProcessingEndDay(int procEndDay) {
		this.processingEndDay = procEndDay;
	}

	private void setTravelTime(float travelTime) {
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

	// @Override
	// public String toString() {
	// return String.format(
	// "Name: %s, Qty Available: %d, Qty Taken: %d, Processing End Date: %d, Travel Time: %.2f, Arrival date: %d",
	// getName(), getQtyAvailable(), getNumberOfItems(), getProcessingEndDay(), getTravelTime(),
	// getArrivalDay());
	// }

}
