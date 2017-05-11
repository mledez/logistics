package logistics.loaders;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import logistics.exceptions.XmlDataException;

public class XmlDocLoader {

	public static Document loadDoc(String fileName) throws DOMException, XmlDataException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		Document doc = null;
		InputStream is = XmlDocLoader.class.getResourceAsStream("/res/" + fileName);

		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.err.println("[Parser configuration error found: loading " + fileName + "]");
			System.exit(-1);
		}

		try {
			doc = db.parse(is);
			doc.getDocumentElement().normalize();
		} catch (SAXException e) {
			throw new XmlDataException("[File " + fileName + " not properly formatted]");
			// System.err.println("[File " + fileName + " not properly formatted]");
			// System.exit(-1);
		} catch (IOException e) {
			// System.err.println("[File " + fileName + " not accessible]");
			throw new XmlDataException("[File " + fileName + " not accessible]");
		} catch (IllegalArgumentException e) {
			// System.err.println("[File " + fileName + " not found]");
			throw new XmlDataException("[File " + fileName + " not found]");
		}

		return doc;
	}

}