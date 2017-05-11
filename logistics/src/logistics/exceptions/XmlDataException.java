package logistics.exceptions;

@SuppressWarnings("serial")
public class XmlDataException extends Exception {

	public XmlDataException(String m) {
		System.err.println("XmlDataException " + m);
		System.exit(-1);
	}

}
