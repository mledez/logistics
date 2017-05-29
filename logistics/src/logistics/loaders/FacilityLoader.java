package logistics.loaders;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import logistics.exceptions.DuplicatedDataException;
import logistics.exceptions.InvalidDataException;
import logistics.exceptions.XmlReadingException;
import logistics.facility.Facility;
import logistics.facility.FacilityFactory;
import logistics.inventory.Inventory;
import logistics.inventory.InventoryImpl;
import logistics.schedule.ScheduleImpl;

public class FacilityLoader {
	private FacilityLoader() {}

	public static List<Facility> load(String fileName)
			throws XmlReadingException, InvalidDataException, DuplicatedDataException {
		List<String> loadedFacilities = new ArrayList<>();
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
					throw new XmlReadingException(
							String.format("Unexpected node found (%s) in file %s", entryName, fileName));
				}
				Element elem = (Element) nodeList.item(i);
				String location = elem.getElementsByTagName("Location").item(0).getTextContent();
				if (loadedFacilities.contains(location))
					throw new DuplicatedDataException(
							String.format("Duplicated Location (%s) found in file %s", location, fileName));

				int dailyRate = Integer.parseInt(elem.getElementsByTagName("DailyRate").item(0).getTextContent());
				int dailyCost = Integer.parseInt(elem.getElementsByTagName("Cost").item(0).getTextContent());

				// Facility Inventory: Get all nodes named "Item" - there can be 0 or more
				Inventory inventory = new InventoryImpl();
				NodeList items = elem.getElementsByTagName("Item");
				List<String> loadedItems = new ArrayList<>();
				for (int j = 0; j < items.getLength(); j++) {
					if (items.item(j).getNodeType() == Node.TEXT_NODE) {
						continue;
					}

					entryName = items.item(j).getNodeName();
					if (!entryName.equals("Item")) {
						throw new XmlReadingException(
								String.format("Unexpected node found (%s) in file %s", entryName, fileName));
					}
					elem = (Element) items.item(j);
					String itemId = elem.getElementsByTagName("Id").item(0).getTextContent();
					if (loadedItems.contains(itemId))
						throw new DuplicatedDataException(String.format(
								"Duplicated Inventory Item (%s) found for %s in file %s", itemId, location, fileName));
					int itemQty = Integer.parseInt(elem.getElementsByTagName("Qty").item(0).getTextContent());
					inventory.add(itemId, itemQty);
					loadedItems.add(itemId);
				}

				facilities.add(FacilityFactory.createFacility(location, dailyRate, dailyCost, inventory,
						new ScheduleImpl(dailyRate)));
				loadedFacilities.add(location);
			}
			return facilities;
		} catch (DOMException e) {
			throw new XmlReadingException("DOM problem found operating file " + fileName);
		}
	}
}