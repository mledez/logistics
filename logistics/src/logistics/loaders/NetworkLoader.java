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
import logistics.network.Link;
import logistics.network.LinkFactory;

public class NetworkLoader {

	private NetworkLoader() {}

	public static List<Link> load(String fileName) throws XmlReadingException, InvalidDataException {

		try {
			Document doc = XmlDocLoader.loadDoc(fileName);
			NodeList nodeList = doc.getDocumentElement().getChildNodes();

			List<Link> links = new ArrayList<>();

			for (int i = 0; i < nodeList.getLength(); i++) {
				if (nodeList.item(i).getNodeType() == Node.TEXT_NODE) {
					continue;
				}

				String entryName = nodeList.item(i).getNodeName();
				if (!entryName.equals("Link")) {
					throw new XmlReadingException(
							String.format("Unexpected node found (%s) in file %s", entryName, fileName));
				}

				Element elem = (Element) nodeList.item(i);
				String linkFrom = elem.getElementsByTagName("From").item(0).getTextContent();
				String linkTo = elem.getElementsByTagName("To").item(0).getTextContent();
				int linkDistance = Integer.parseInt(elem.getElementsByTagName("Distance").item(0).getTextContent());

				links.add(LinkFactory.createLink(linkFrom, linkTo, linkDistance));
			}
			return links;

		} catch (DOMException e) {
			throw new XmlReadingException("DOM problem found operating file " + fileName);
		}
	}
}