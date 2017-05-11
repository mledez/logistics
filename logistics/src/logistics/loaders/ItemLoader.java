package logistics.loaders;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import logistics.exceptions.InvalidDataException;
import logistics.exceptions.XmlReadingException;
import logistics.item.Item;
import logistics.item.ItemFactory;

public class ItemLoader {

	private ItemLoader() {}

	public static List<Item> load(String fileName) throws XmlReadingException, InvalidDataException {

		try {
			Document doc = XmlDocLoader.loadDoc(fileName);
			NodeList nodeList = doc.getDocumentElement().getChildNodes();

			List<Item> items = new ArrayList<>();

			for (int i = 0; i < nodeList.getLength(); i++) {
				if (nodeList.item(i).getNodeType() == Node.TEXT_NODE) {
					continue;
				}

				String entryName = nodeList.item(i).getNodeName();
				if (!entryName.equals("Item")) {
					System.err.println("Unexpected node found: " + entryName);
					return null;
				}

				Element elem = (Element) nodeList.item(i);
				String id = elem.getElementsByTagName("Id").item(0).getTextContent();
				int price = Integer.parseInt(elem.getElementsByTagName("Price").item(0).getTextContent());

				items.add(ItemFactory.createItem(id, price));
			}
			return items;

		} catch (DOMException e) {
			e.printStackTrace();
		}

		return null;
	}
}