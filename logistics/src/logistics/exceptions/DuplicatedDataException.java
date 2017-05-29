package logistics.exceptions;

@SuppressWarnings("serial")
public class DuplicatedDataException extends Exception {

	public DuplicatedDataException(String m) {
		System.err.println(m);
		System.exit(-1);
	}
}
