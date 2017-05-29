package logistics.loaders;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import logistics.exceptions.XmlReadingException;

public class XmlDocLoader {

	public static Document loadDoc(String fileName) throws DOMException, XmlReadingException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		Document doc = null;
		InputStream is = XmlDocLoader.class.getResourceAsStream("/res/" + fileName);

		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new XmlReadingException("Parser configuration error found: loading " + fileName);
		}

		try {
			doc = db.parse(is);
			doc.getDocumentElement().normalize();
		} catch (SAXException e) {
			throw new XmlReadingException("File " + fileName + " not properly formatted");
		} catch (IOException e) {
			throw new XmlReadingException("File " + fileName + " not accessible");
		} catch (IllegalArgumentException e) {
			throw new XmlReadingException("File " + fileName + " not found");
		}
		return doc;
	}

}