package logistics.loaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import logistics.facility.Facility;
import logistics.facility.FacilityFactory;

public class FacilityLoader {

	private FacilityLoader() {}

	public static List<Facility> load(String fileName) {

		try {
			Document doc = XmlDocLoader.loadDoc(fileName);
			NodeList nodeList = doc.getDocumentElement().getChildNodes();

			List<Facility> facilities = new ArrayList<>();

			for (int i = 0; i < nodeList.getLength(); i++) {
				if (nodeList.item(i).getNodeType() == Node.TEXT_NODE) {
					continue;
				}

				String entryName = nodeList.item(i).getNodeName();
				if (!entryName.equals("Facility")) {
					System.err.println("Unexpected node found: " + entryName);
					return null;
				}

				Element elem = (Element) nodeList.item(i);
				String location = elem.getElementsByTagName("Location").item(0).getTextContent();
				int dailyRate = Integer.parseInt(elem.getElementsByTagName("DailyRate").item(0).getTextContent());
				int dailyCost = Integer.parseInt(elem.getElementsByTagName("Cost").item(0).getTextContent());

				// Facility Inventory: Get all nodes named "Item" - there can be 0 or more
				Map<String, Integer> inventory = new HashMap<>();
				NodeList items = elem.getElementsByTagName("Item");
				for (int j = 0; j < items.getLength(); j++) {
					if (items.item(j).getNodeType() == Node.TEXT_NODE) {
						continue;
					}

					entryName = items.item(j).getNodeName();
					if (!entryName.equals("Item")) {
						System.err.println("Unexpected node found: " + entryName);
						return null;
					}

					elem = (Element) items.item(j);
					String itemId = elem.getElementsByTagName("Id").item(0).getTextContent();
					int itemQty = Integer.parseInt(elem.getElementsByTagName("Qty").item(0).getTextContent());

					inventory.put(itemId, itemQty);
				}

				Facility facility = FacilityFactory.createFacility(location, dailyRate, dailyCost, inventory);

				if (facility.getStatus())
					facilities.add(facility);
				else {
					System.err.println("[Facility not added (Illegal values)]\n" + facility);
				}
			}
			return facilities;

		} catch (DOMException e) {
			e.printStackTrace();
		}

		return null;
	}
}