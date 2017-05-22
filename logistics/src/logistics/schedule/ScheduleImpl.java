package logistics.schedule;

import java.util.Map;
import java.util.TreeMap;

public class ScheduleImpl {
	private Map<Integer, Integer> usedDays = new TreeMap<>();
	private int dailyRate;

	public ScheduleImpl(int dailyRate) {
		setDailyRate(dailyRate);
	}

	private void setDailyRate(int dailyRate) {
		this.dailyRate = dailyRate;
	}

	private Map<Integer, Integer> getUsedDays() {
		return this.usedDays;
	}

	private int getDailyRate() {
		return this.dailyRate;
	}

	public int getEndDay(int startDay, int qty) {
		int currentDay = startDay;
		int pendingQty = qty;
		for (int i = 0; pendingQty > 0; i++) {
			if (i > 0)
				currentDay++;
			int dayCap = getUsedDays().getOrDefault(currentDay, getDailyRate());
			if (dayCap > 0) {
				pendingQty = pendingQty - Integer.min(pendingQty, dayCap);
			}
		}
		return currentDay;
	}

	public void insertOrder(int startDay, int qty) {
		int currentDay = startDay;
		int pendingQty = qty;
		for (int i = 0; pendingQty > 0; i++) {
			if (i > 0)
				currentDay++;
			int dayCap = getUsedDays().getOrDefault(currentDay, getDailyRate());
			if (dayCap > 0) {
				int deduction = Integer.min(pendingQty, dayCap);
				getUsedDays().put(currentDay, dayCap - deduction);
				pendingQty = pendingQty - deduction;
			}
		}
	}

	public String getReport() {
		String day = "";
		String available = "";
		int max = getUsedDays().keySet().stream().max(Integer::compareTo).orElse(0);
		if (getUsedDays().isEmpty() || max < 20)
			max = 20;
		for (int i = max - 19; i <= max; i++) {
			day += String.format("%-2d ", i);
			if (getUsedDays().containsKey(i))
				available += String.format("%-2d ", getDailyRate() - getUsedDays().get(i));
			else
				available += String.format("%-2d ", getDailyRate());
		}
		return String.format("Schedule:\n%-15s%s\n%-15s%s\n", "Day:", day, "Available:", available);
	}
}
