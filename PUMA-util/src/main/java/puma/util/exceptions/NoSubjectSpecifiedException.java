package puma.util.exceptions;

public class NoSubjectSpecifiedException extends Exception {
	private static final long serialVersionUID = 1745027980623423572L;

	public NoSubjectSpecifiedException() {
        super("Could not process the SAML request. No subject identifier was specified, although this was required");
    }
}
