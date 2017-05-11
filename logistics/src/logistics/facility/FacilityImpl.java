package logistics.facility;

import java.util.Map;
import java.util.TreeMap;

public class FacilityImpl implements Facility {
	private String location;
	private int dailyRate;
	private int dailyCost;
	private Map<String, Integer> inventory;
	// private Map<String, Float> neighbors;
	private Map<Integer, Integer> schedule = new TreeMap<>();

	public FacilityImpl(String location, int dailyRate, int dailyCost, Map<String, Integer> inventory) {
		this.location = location;
		this.dailyRate = dailyRate;
		this.dailyCost = dailyCost;
		this.inventory = inventory;

		// this.neighbors = NetworkManager.getInstance().getNeighbors(location);
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
		for (String key : inventory.keySet()) {
			if (inventory.get(key) == 0)
				depInventory += key + "; ";
			else
				actInventory += String.format("   %-11s %s\n", key, inventory.get(key));
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

	// private String getNeighborsReport() {
	// String neighborsReport = "";
	// String currentEntry;
	// int lineCounter = 0;
	// for (String neighbor : neighbors.keySet()) {
	// currentEntry = String.format("%s (%.1fd); ", neighbor, neighbors.get(neighbor));
	// if ((neighborsReport + currentEntry).length() - lineCounter * 86 <= 86)
	// neighborsReport = neighborsReport + currentEntry;
	// else {
	// neighborsReport = neighborsReport + "\n" + currentEntry;
	// lineCounter++;
	// }
	// }
	// if (neighborsReport.equals(""))
	// neighborsReport = "No neighbors found";
	//
	// return "Direct Links:\n" + neighborsReport + "\n";
	// }

	private String getScheduleReport() {
		String day = "";
		String available = "";

		int max = schedule.keySet().stream().max(Integer::compareTo).orElse(0);

		if (schedule.isEmpty() || max < 20)
			max = 20;

		for (int i = max - 19; i <= max; i++) {
			day += String.format("%-2d ", i);
			if (schedule.containsKey(i))
				available += String.format("%-2d ", getDailyRate() - schedule.get(i));
			else
				available += String.format("%-2d ", getDailyRate());
		}

		return String.format("Schedule:\n%-15s%s\n%-15s%s\n", "Day:", day, "Available:", available);
	}

	@Override
	public boolean getStatus() {
		// TODO Auto-generated method stub
		return true;
	}
}
