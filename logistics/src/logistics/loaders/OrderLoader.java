package logistics.loaders;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import logistics.exceptions.InvalidDataException;
import logistics.exceptions.XmlReadingException;
import logistics.order.Order;
import logistics.order.OrderFactory;

public class OrderLoader {
	private OrderLoader() {}

	public static List<Order> load(String fileName) throws XmlReadingException, InvalidDataException {
		try {
			Document doc = XmlDocLoader.loadDoc(fileName);
			NodeList nodeList = doc.getDocumentElement().getChildNodes();

			List<Order> orders = new ArrayList<>();

			for (int i = 0; i < nodeList.getLength(); i++) {
				if (nodeList.item(i).getNodeType() == Node.TEXT_NODE) {
					continue;
				}

				String entryName = nodeList.item(i).getNodeName();
				if (!entryName.equals("Order")) {
					System.err.println("Unexpected node found: " + entryName);
					return null;
				}

				Element elem = (Element) nodeList.item(i);
				String id = elem.getElementsByTagName("Id").item(0).getTextContent();
				int day = Integer.parseInt(elem.getElementsByTagName("Day").item(0).getTextContent());
				String destination = elem.getElementsByTagName("Destination").item(0).getTextContent();

				// Order Items list: Get all nodes named "Item" - there can be 0 or more
				Map<String, Integer> orderItems = new LinkedHashMap<>();
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

					orderItems.put(itemId, itemQty);
				}

				orders.add(OrderFactory.createOrder(id, day, destination, orderItems));
			}
			return orders;
		} catch (DOMException e) {
			e.printStackTrace();
		}
		return null;
	}
}