package puma.util.exceptions.authorization;

public class ObligationExecutionException extends Exception {
	private static final long serialVersionUID = 1L;

	public ObligationExecutionException(String obligationName) {
		super("Could not perform obligation " + obligationName);
	}
}
