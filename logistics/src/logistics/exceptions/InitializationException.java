package logistics.exceptions;

@SuppressWarnings("serial")
public class InitializationException extends Exception {

	public InitializationException(String m) {
		System.err.println(m);
		System.exit(-1);
	}
}
