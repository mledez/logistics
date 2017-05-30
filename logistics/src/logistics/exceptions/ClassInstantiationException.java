package logistics.exceptions;

@SuppressWarnings("serial")
public class ClassInstantiationException extends Exception {

	public ClassInstantiationException(String m) {
		System.err.println(m);
		System.exit(-1);
	}

}
