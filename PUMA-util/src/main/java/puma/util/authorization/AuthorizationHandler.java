package puma.util.authorization;

import puma.util.exceptions.authorization.ObligationExecutionException;

public interface AuthorizationHandler {
	public void performObligation() throws ObligationExecutionException;
	public boolean isAuthorized();	
}