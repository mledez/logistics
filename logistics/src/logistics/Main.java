package logistics;

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

		try {
			nm.init("Links.xml", 8, 50);
			im.init("Items.xml");
			fm.init("Facilities.xml");
			om.init("Orders.xml", 500);
			om.processOrders();

			System.out.println(new String(new char[82]).replace("\0", "-"));

			System.out.print(fm.getReport());
			System.out.print(om.getProcessingReport());
			System.out.print(fm.getReport());

		} catch (XmlReadingException | InvalidDataException | InitializationException e) {
			System.err.println(e.getMessage());
		}
	}
}