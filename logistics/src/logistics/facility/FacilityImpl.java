package logistics.facility;

import java.util.Map;
import java.util.TreeMap;

import logistics.network.NetworkManager;

public class FacilityImpl implements Facility {
	private String location;
	private int dailyRate;
	private int dailyCost;
	private Map<String, Integer> inventory;
	private Map<String, Float> neighbors;
	private Map<Integer, Integer> schedule = new TreeMap<>();

	public FacilityImpl(String location, int dailyRate, int dailyCost, Map<String, Integer> inventory) {
		this.location = location;
		this.dailyRate = dailyRate;
		this.dailyCost = dailyCost;
		this.inventory = inventory;

		this.neighbors = NetworkManager.getInstance().getNeighbors(location);
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
		String underline = new String(new char[location.length()]).replace("\0", "-");
		String report = String.format("%s\n" + "%s\n" + "Rate per Day: %s\n" + "Cost per Day: $%s\n" +
		/*
		 * "Direct Links:\n" + "***\n\n" +
		 */
				"\n%s\n" + "%s" + "%s", location, underline, dailyRate, dailyCost, getNeighborsReport(),
				getInventoryReport(), getScheduleReport());
		return report;
	}

	public void printReport() {
		System.out.println(getReport());
	}

	private String getInventoryReport() {
		String activeInventory = "";
		String depletedInventory = "";
		int currentQty;
		for (String key : inventory.keySet()) {
			currentQty = inventory.get(key);
			if (currentQty == 0)
				depletedInventory = depletedInventory + key + "; ";
			else
				activeInventory = String.format("%s   %-11s %s\n", activeInventory, key, inventory.get(key));
		}
		if (depletedInventory.equals(""))
			depletedInventory = "None\n";

		if (activeInventory.equals(""))
			activeInventory = "None\n";
		else
			activeInventory = String.format("\n   %-11s%s\n%s", "Item ID", " Quantity", activeInventory);

		return "Active Inventory: " + activeInventory + "\nDepleted (Used-Up) Inventory: " + depletedInventory + "\n";
	}

	private String getNeighborsReport() {
		String neighborsReport = "";
		String currentEntry;
		int lineCounter = 0;
		for (String neighbor : neighbors.keySet()) {
			currentEntry = String.format("%s (%.1fd); ", neighbor, neighbors.get(neighbor));
			if ((neighborsReport + currentEntry).length() - lineCounter * 86 <= 86)
				neighborsReport = neighborsReport + currentEntry;
			else {
				neighborsReport = neighborsReport + "\n" + currentEntry;
				lineCounter++;
			}
		}
		if (neighborsReport.equals(""))
			neighborsReport = "There are no neighbors";

		return "Direct Links:\n" + neighborsReport + "\n";
	}

	private String getScheduleReport() {
		String day = "";
		String available = "";

		int max;

		if (schedule.isEmpty())
			max = 20;
		else
			max = schedule.keySet().stream().max(Integer::compareTo).orElse(0);

		for (int i = 1; i <= max; i++) {
			day = String.format("%s%-2d ", day, i);
			if (schedule.containsKey(i))
				available = String.format("%s%-2d ", available, 10 - schedule.get(i));
			else
				available = String.format("%s%-2d ", available, 10);
		}

		return String.format("Schedule:\n%-15s%s\n%-15s%s\n", "Day:", day, "Available:", available);
	}

	@Override
	public boolean getStatus() {
		// TODO Auto-generated method stub
		return true;
	}
}
