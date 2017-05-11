package logistics;

import java.util.ArrayList;

import logistics.facility.FacilityManager;
import logistics.item.ItemManager;
import logistics.network.NetworkManager;

public class Main {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		NetworkManager nm = NetworkManager.getInstance();
		nm.init(8, 50);
		ItemManager im = ItemManager.getInstance();

		FacilityManager fm = FacilityManager.getInstance();

		ArrayList<String[]> samplePairs = new ArrayList<>();
		samplePairs.add(new String[] { "Santa Fe, NM", "Chicago, IL" });
		samplePairs.add(new String[] { "Atlanta, GA", "St. Louis, MO" });
		samplePairs.add(new String[] { "Seattle, WA", "Nashville, TN" });
		samplePairs.add(new String[] { "New York City, NY", "Phoenix, AZ" });
		samplePairs.add(new String[] { "Fargo, ND", "Austin, TX" });
		samplePairs.add(new String[] { "Denver, CO", "Miami, FL" });
		samplePairs.add(new String[] { "Austin, TX", "Norfolk, VA" });
		samplePairs.add(new String[] { "Miami, FL", "Seattle, WA" });
		samplePairs.add(new String[] { "Los Angeles, CA", "Chicago, IL" });
		samplePairs.add(new String[] { "Detroit, MI", "Nashville, TN" });

		fm.printReport();
		im.printReport();

		char letter = 'a';
		String report = "";
		for (String[] pair : samplePairs) {
			report += formatLinkReport(nm.getPathReport(pair[0], pair[1]), letter) + "\n\n";
			letter++;
		}
		System.out.println(report);

		long endTime = System.currentTimeMillis();
		System.out.println("Took " + (endTime - startTime) + " ms");
	}

	public static String formatLinkReport(String string, char letter) {
		String report = string.replaceAll("(?m)(^.*\\n)(^.*\\n)+(^.*$)", letter + ") $1 • $2 • $3");
		return report;
	}
}