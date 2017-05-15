package logistics.facility;

import java.util.Map;
import java.util.TreeMap;

import logistics.exceptions.InvalidDataException;

public class FacilityImpl implements Facility {
	private String location;
	private int dailyRate;
	private int dailyCost;
	private Map<String, Integer> inventory;
	private Map<Integer, Integer> schedule = new TreeMap<>();

	public FacilityImpl(String location, int dailyRate, int dailyCost, Map<String, Integer> inventory)
			throws InvalidDataException {
		setLocation(location);
		setDailyRate(dailyRate);
		setDailyCost(dailyCost);
		setInventory(inventory);
	}

	private Map<Integer, Integer> getSchedule() {
		return schedule;
	}

	private Map<String, Integer> getInventory() {
		return inventory;
	}

	private void setInventory(Map<String, Integer> inventory) throws InvalidDataException {
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
				getInventoryReport(), getScheduleReport());
		return report;
	}

	private String getInventoryReport() {
		String actInventory = "";
		String depInventory = "";
		for (String key : getInventory().keySet()) {
			if (getInventory().get(key) == 0)
				depInventory += key + "; ";
			else
				actInventory += String.format("   %-11s %s\n", key, getInventory().get(key));
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

	private String getScheduleReport() {
		String day = "";
		String available = "";

		int max = getSchedule().keySet().stream().max(Integer::compareTo).orElse(0);

		if (getSchedule().isEmpty() || max < 20)
			max = 20;

		for (int i = max - 19; i <= max; i++) {
			day += String.format("%-2d ", i);
			if (getSchedule().containsKey(i))
				available += String.format("%-2d ", getDailyRate() - getSchedule().get(i));
			else
				available += String.format("%-2d ", getDailyRate());
		}

		return String.format("Schedule:\n%-15s%s\n%-15s%s\n", "Day:", day, "Available:", available);
	}

	public boolean contains(String item) {
		if (getInventory().containsKey(item))
			if (getInventory().get(item) > 0)
				return true;
		return false;
	}

	public int getItemCount(String item) {
		return getInventory().get(item);
	}

	public int quoteTime(String item, int day, int qty) {
		qty = Integer.min(qty, getItemCount(item));
		while (qty > 0) {
			if (getInventory().containsKey(day)) {
				if (getInventory().get(day) > 0) {
					qty = qty - getInventory().get(day);
				}
			} else {
				qty = qty - getDailyRate();
			}
			if (qty > 0)
				day++;
		}
		return day;
	}
}
