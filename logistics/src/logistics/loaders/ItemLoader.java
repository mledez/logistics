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
import logistics.item.Item;
import logistics.item.ItemFactory;

public class ItemLoader {
	private ItemLoader() {}

	public static List<Item> load(String fileName)
			throws XmlReadingException, InvalidDataException, DuplicatedDataException {
		List<String> loadedIds = new ArrayList<>();
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
					throw new XmlReadingException(
							String.format("Unexpected node found (%s) in file %s", entryName, fileName));
				}

				Element elem = (Element) nodeList.item(i);
				String id = elem.getElementsByTagName("Id").item(0).getTextContent();
				if (loadedIds.contains(id))
					throw new DuplicatedDataException(
							String.format("Duplicated Item (%s) found in file %s", id, fileName));

				int price = Integer.parseInt(elem.getElementsByTagName("Price").item(0).getTextContent());

				items.add(ItemFactory.createItem(id, price));
				loadedIds.add(id);
			}
			return items;
		} catch (DOMException e) {
			throw new XmlReadingException("DOM problem found operating file " + fileName);
		}
	}
}