package logistics.schedule;

import java.util.Map;
import java.util.TreeMap;

import logistics.exceptions.InvalidDataException;

public class ScheduleImpl implements Schedule {
	private Map<Integer, Integer> usedDays = new TreeMap<>();
	private int dailyRate;

	public ScheduleImpl(int dailyRate) throws InvalidDataException {
		setDailyRate(dailyRate);
	}

	private void setDailyRate(int dailyRate) throws InvalidDataException {
		if (dailyRate < 1)
			throw new InvalidDataException(String.format("Facility Daily Rate can't be less than 1"));
		this.dailyRate = dailyRate;
	}

	private Map<Integer, Integer> getUsedDays() {
		return this.usedDays;
	}

	private int getDailyRate() {
		return this.dailyRate;
	}

	public int calculateProcessingEndDay(int startDay, int qty) throws InvalidDataException {
		if (startDay < 1 || qty < 1)
			throw new InvalidDataException(
					String.format("To calculate processing end time, Start Day and Qty can't be less than 1"));
		int currentDay = startDay;
		int pendingQty = qty;
		for (int i = 0; pendingQty > 0; i++) {
			if (i > 0)
				currentDay++;
			int dayCap = getUsedDays().getOrDefault(currentDay, getDailyRate());
			if (dayCap > 0) {
				int deduction = Integer.min(pendingQty, dayCap);
				pendingQty = pendingQty - deduction;
			}
		}
		return currentDay;
	}

	public void bookOrder(int startDay, int qty) throws InvalidDataException {
		if (startDay < 1 || qty < 1)
			throw new InvalidDataException(String.format("To book and order, Start Day and Qty can't be less than 1"));
		int currentDay = startDay;
		int pendingQty = qty;
		float billableTime = 0;
		for (int i = 0; pendingQty > 0; i++) {
			if (i > 0)
				currentDay++;
			int dayCap = getUsedDays().getOrDefault(currentDay, getDailyRate());
			if (dayCap > 0) {
				int deduction = Integer.min(pendingQty, dayCap);
				getUsedDays().put(currentDay, dayCap - deduction);
				pendingQty = pendingQty - deduction;
				billableTime = billableTime + deduction;
			}
		}
	}

	public String getReport() {
		String day = "";
		String available = "";
		String report = "Schedule:\n";
		int lineCounter = 0;
		int maxDays = 20;
		int max = getUsedDays().keySet().stream().max(Integer::compareTo).orElse(maxDays);
		max = (int) (Math.ceil(max / (float) maxDays) * maxDays);
		for (int i = 1; i <= max; i++) {
			day += String.format("%-2d ", i);
			if (getUsedDays().containsKey(i))
				available += String.format("%-2d ", getUsedDays().get(i));
			else
				available += String.format("%-2d ", getDailyRate());
			if (i % maxDays == 0) {
				if (lineCounter == 0)
					report = report + String.format("%-15s%s\n%-15s%s\n", "Day:", day, "Available:", available);
				else
					report = report + String.format("%-15s%s\n%-15s%s\n", "", day, "", available);

				available = "";
				day = "";
				lineCounter++;
			}
		}
		return report;
	}
}
