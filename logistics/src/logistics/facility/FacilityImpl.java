package logistics.facility;

import java.util.Map;
import java.util.TreeMap;

import logistics.exceptions.InvalidDataException;
import logistics.inventory.InventoryImpl;

public class FacilityImpl implements Facility {
	private String location;
	private int dailyRate;
	private int dailyCost;
	private InventoryImpl inventory;
	private Map<Integer, Integer> schedule = new TreeMap<>();

	public FacilityImpl(String location, int dailyRate, int dailyCost, InventoryImpl inventory)
			throws InvalidDataException {
		setLocation(location);
		setDailyRate(dailyRate);
		setDailyCost(dailyCost);
		setInventory(inventory);
	}

	private Map<Integer, Integer> getSchedule() {
		return schedule;
	}

	private InventoryImpl getInventory() {
		return inventory;
	}

	private void setInventory(InventoryImpl inventory) throws InvalidDataException {
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
		if (getInventory().containsId(item))
			if (getInventory().getQty(item) > 0)
				return true;
		return false;
	}

	public int getItemCount(String item) {
		return getInventory().getQty(item);
	}

	public int quoteTime(String item, int day, int qty) {
		// qty = Integer.min(qty, getItemCount(item));
		// while (qty > 0) {
		// if (getInventory().containsKey(day)) {
		// if (getInventory().get(day) > 0) {
		// qty = qty - getInventory().get(day);
		// }
		// } else {
		// qty = qty - getDailyRate();
		// }
		// if (qty > 0)
		// day++;
		// }
		return day;
	}

	public void reduceInventory(String item, int qty) {
		// getInventory().put(item, getInventory().get(item) - qty);
	}

	public void scheduleOrder(int day, int qty) {
		while (qty > 0) {
			if (getSchedule().containsKey(day)) {
				if (getSchedule().get(day) > 0) {
					int deduction = Math.min(qty, getSchedule().get(day));
					getSchedule().put(day, getSchedule().get(day) - deduction);
					qty = qty - deduction;
				}
			} else {
				int deduction = Math.min(qty, getDailyRate());
				getSchedule().put(day, getDailyRate() - deduction);
				qty = qty - deduction;
			}

			if (qty > 0)
				day++;
		}
	}
}
