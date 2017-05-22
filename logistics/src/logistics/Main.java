package logistics;

import java.util.ArrayList;

import logistics.exceptions.InitializationException;
import logistics.exceptions.InvalidDataException;
import logistics.exceptions.XmlReadingException;
import logistics.facility.FacilityManager;
import logistics.item.ItemManager;
import logistics.network.NetworkManager;
import logistics.order.OrderManager;

public class Main {

	public static void main(String[] args) {
		NetworkManager nm = NetworkManager.getInstance();
		ItemManager im = ItemManager.getInstance();
		FacilityManager fm = FacilityManager.getInstance();
		OrderManager om = OrderManager.getInstance();

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

		samplePairs.add(new String[] { "Fargo, ND", "Miami, FL" });

		try {
			nm.init("Links.xml", 8, 50);
			im.init("Items.xml");
			fm.init("Facilities.xml");
			om.init("Orders.xml");

			// System.out.println(outputOne(fm));
			// System.out.println(outputTwo(im));
			// System.out.println(outputThree(nm, samplePairs));
			// System.out.println(om.getReport());

			om.startProcessing();

		} catch (XmlReadingException | InvalidDataException | InitializationException e) {
			System.err.println(e.getMessage());
		}
	}

	// Facility Status Output and Format
	private static String outputOne(FacilityManager fm) throws InitializationException {
		return fm.getReport();
	}

	// Item Catalog Content Output
	private static String outputTwo(ItemManager im) throws InitializationException, InvalidDataException {
		return im.getReport();
	}

	// Example Shortest Path output results
	private static String outputThree(NetworkManager nm, ArrayList<String[]> samplePairs)
			throws InitializationException {
		char letter = 'a';
		String report = "Shortest Path Tests:\n\n";
		for (String[] pair : samplePairs) {
			report += nm.getPathReport(pair[0], pair[1]).replaceAll("(?m)(^.*\\n)(^.*\\n)+(^.*$)",
					letter + ") $1   •  $2   •  $3\n\n");
			letter++;
		}
		return report;
	}
}