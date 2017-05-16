package logistics.facility;

public class FacilityRecord implements Comparable<FacilityRecord> {
	private String name;
	private int qtyAvailable;
	private int qtyTaken;
	private int procEndDay;
	private float travelTime;

	public FacilityRecord(String name, int qtyAvailable, int qtyTaken, int procEndDay, float travelTime) {
		setName(name);
		setQtyAvailable(qtyAvailable);
		setQtyTaken(qtyTaken);
		setProcEndDay(procEndDay);
		setTravelTime(travelTime);
	}

	private void setName(String name) {
		this.name = name;
	}

	private void setQtyAvailable(int qtyAvailable) {
		this.qtyAvailable = qtyAvailable;
	}

	private void setQtyTaken(int qtyTaken) {
		this.qtyTaken = qtyTaken;
	}

	private void setProcEndDay(int procEndDay) {
		this.procEndDay = procEndDay;
	}

	private void setTravelTime(float travelTime) {
		this.travelTime = travelTime;
	}

	public String getName() {
		return name;
	}

	public int getQtyAvailable() {
		return qtyAvailable;
	}

	public int getQtyTaken() {
		return qtyTaken;
	}

	public int getProcEndDay() {
		return procEndDay;
	}

	public float getTravelTime() {
		return travelTime;
	}

	private Integer getArrivalDay() {
		return (int) Math.ceil(getProcEndDay() + getTravelTime());
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
