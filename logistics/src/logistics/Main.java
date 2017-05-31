package logistics;

import java.util.ArrayList;

import logistics.exceptions.ClassInstantiationException;
import logistics.exceptions.DuplicatedDataException;
import logistics.exceptions.InitializationException;
import logistics.exceptions.InvalidDataException;
import logistics.exceptions.XmlReadingException;
import logistics.facility.FacilityManager;
import logistics.item.ItemManager;
import logistics.network.NetworkManager;
import logistics.order.OrderManager;

public class Main {
	public static void main(String[] args) {

		try {
			phaseTwo();
		} catch (XmlReadingException | InvalidDataException | InitializationException | DuplicatedDataException
				| ClassInstantiationException e) {
			System.err.println(e.getMessage());
		}
	}

	public static void phaseTwo() throws InitializationException, InvalidDataException, XmlReadingException,
			DuplicatedDataException, ClassInstantiationException {

		NetworkManager nm = NetworkManager.getInstance();
		ItemManager im = ItemManager.getInstance();
		FacilityManager fm = FacilityManager.getInstance();
		OrderManager om = OrderManager.getInstance();

		nm.init("Links.xml", 8, 50);
		im.init("Items.xml");
		fm.init("Facilities.xml");
		om.init("Orders.xml", 500);

		System.out.println(new String(new char[82]).replace("\0", "-"));

		System.out.print(fm.getReport());

		om.processOrders();
		System.out.print(om.getProcessingReport());

		System.out.print(fm.getReport());

	}

	public static void phaseOne() throws DuplicatedDataException, ClassInstantiationException, XmlReadingException,
			InvalidDataException, InitializationException {

		NetworkManager nm = NetworkManager.getInstance();
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

		nm.init("Links.xml", 8, 50);
		im.init("Items.xml");
		fm.init("Facilities.xml");

		System.out.println(fm.getReport());
		System.out.println(im.getReport());

		char letter = 'a';
		String report = "Shortest Path Tests:\n\n";
		for (String[] pair : samplePairs) {
			report += nm.getPathReport(pair[0], pair[1]).replaceAll("(?m)(^.*\\n)(^.*\\n)+(^.*$)",
					letter + ") $1   •  $2   •  $3\n\n");
			letter++;
		}
		System.out.println(report);
	}
}