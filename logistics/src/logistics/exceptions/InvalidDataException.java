package logistics.exceptions;

@SuppressWarnings("serial")
public class InvalidDataException extends Exception {

	public InvalidDataException(String m) {
		System.err.println("InvalidDataException " + m);
		System.exit(-1);
	}

}
