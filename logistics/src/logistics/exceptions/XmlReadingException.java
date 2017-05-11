package logistics.exceptions;

@SuppressWarnings("serial")
public class XmlReadingException extends Exception {

	public XmlReadingException(String m) {
		System.err.println(m);
		System.exit(-1);
	}

}
