package logistics.facility;

import logistics.exceptions.InvalidDataException;
import logistics.inventory.Inventory;
import logistics.schedule.Schedule;
import logistics.schedule.ScheduleImpl;

public class FacilityImpl implements Facility {
	private String location;
	private int dailyRate;
	private int dailyCost;
	private Inventory inventory;
	private Schedule schedule;

	public FacilityImpl(String location, int dailyRate, int dailyCost, Inventory inventory)
			throws InvalidDataException {
		setLocation(location);
		setDailyRate(dailyRate);
		setDailyCost(dailyCost);
		setInventory(inventory);
		setSchedule(new ScheduleImpl(dailyRate));
	}

	private void setSchedule(Schedule schedule) {
		this.schedule = schedule;

	}

	private Schedule getSchedule() {
		return schedule;
	}

	private Inventory getInventory() {
		return inventory;
	}

	private void setInventory(Inventory inventory) throws InvalidDataException {
		if (inventory == null)
			throw new InvalidDataException("Facility inventory can not be null");
		this.inventory = inventory;
	}

	private void setLocation(String location) throws InvalidDataException {
		if (location == null || location.equals(""))
			throw new InvalidDataException("Facility location can not be null or empty");
		this.location = location;
	}

	private void setDailyRate(int dailyRate) throws InvalidDataException {
		if (dailyRate < 1)
			throw new InvalidDataException("Facility Daily Rate must be 1 or more");
		this.dailyRate = dailyRate;
	}

	private void setDailyCost(int dailyCost) throws InvalidDataException {
		if (dailyCost < 1)
			throw new InvalidDataException("Facility Daily Cost must be 1 or more");
		this.dailyCost = dailyCost;
	}

	public String getLocation() {
		return this.location;
	}

	public int getDailyRate() {
		return this.dailyRate;
	}

	public int getDailyCost() {
		return this.dailyCost;
	}

	@Override
	public String toString() {
		return getReport();
	}

	public String getReport() {
		String report = "";
		report = String.format("%s\n%s\nRate per Day: %d\nCost per Day: $%d\n\n%s\n%s", getLocation(),
				new String(new char[getLocation().length()]).replace("\0", "-"), getDailyRate(), getDailyCost(),
				getInventoryReport(), getSchedule().getReport());
		return report;
	}

	private String getInventoryReport() {
		String actInventory = "";
		String depInventory = "";
		for (String id : getInventory().getIdSet()) {
			if (getInventory().getQty(id) == 0)
				depInventory += id + "; ";
			else
				actInventory += String.format("   %-11s %s\n", id, getInventory().getQty(id));
		}
		if (depInventory.equals(""))
			depInventory = "None\n";
		else
			depInventory = depInventory + "\n";

		if (actInventory.equals(""))
			actInventory = "None\n";
		else
			actInventory = String.format("\n   %-11s%s\n%s", "Item ID", " Quantity", actInventory);

		return "Active Inventory: " + actInventory + "\nDepleted (Used-Up) Inventory: " + depInventory;
	}

	public boolean containsItem(String item) {
		if (getInventory().containsId(item))
			if (getInventory().getQty(item) > 0)
				return true;
		return false;
	}

	public int getItemCount(String item) {
		return getInventory().getQty(item);
	}

	public int quoteTime(int day, int qty) {
		return getSchedule().getEndDay(day, qty);
	}

<<<<<<< HEAD
	public void bookOrder(int day, String item, int qty) {
		getInventory().deduct(item, qty);
=======
	public void reduceInventory(String item, int qty) {
		getInventory().reduceQty(item, qty);
		;
	}

	public void bookOrder(int day, int qty) {
>>>>>>> refs/remotes/origin/master
		getSchedule().bookOrder(day, qty);
	}
}
