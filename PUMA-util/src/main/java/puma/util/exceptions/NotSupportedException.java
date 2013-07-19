package puma.util.exceptions;

public class NotSupportedException extends Exception {
	private static final long serialVersionUID = -1659071562291902492L;
	public NotSupportedException(String type, String value) {
        super("Could not perform operation: " + type + ": value '" + value + "' is not supported by the system");
    }
}
