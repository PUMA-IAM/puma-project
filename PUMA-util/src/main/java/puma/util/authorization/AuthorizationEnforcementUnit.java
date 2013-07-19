package puma.util.authorization;

import java.util.ArrayList;
import java.util.List;

import puma.util.exceptions.authorization.ObligationExecutionException;

public abstract class AuthorizationEnforcementUnit implements AuthorizationHandler {
	private List<AuthorizationHandler> handlers;
	
	public AuthorizationEnforcementUnit() {
		this.handlers = new ArrayList<AuthorizationHandler>();
	}
	
	public boolean isAuthorized() {
		for (AuthorizationHandler handler: this.handlers) {
			if (!handler.isAuthorized())
				return false;
		}
		return true;
	} 
	
	public void performObligation() throws ObligationExecutionException {
		for (AuthorizationHandler handler: this.handlers) {
			handler.performObligation();
		}
	}
}
