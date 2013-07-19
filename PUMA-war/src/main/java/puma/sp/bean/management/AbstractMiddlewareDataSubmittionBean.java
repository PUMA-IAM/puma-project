package puma.sp.bean.management;

import puma.sp.model.User;
import puma.sp.util.FlowDirecter;
import puma.sp.util.JsfFlowDirecter;
import puma.sp.util.authorization.AuthorizationHandler;
import puma.sp.util.authorization.MiddlewareAuthorizationHandler;

public abstract class AbstractMiddlewareDataSubmittionBean extends AbstractDataSubmittionBean {	
	private static final String DEFAULT_SUCCESS_REDIRECTION_PAGE = "success.xhtml";
	private static final String DEFAULT_FAILURE_REDIRECTION_PAGE = "error.jsf";
	
	@Override
	public final void submit() {
		MiddlewareAuthorizationHandler handler = new MiddlewareAuthorizationHandler(super.getCurrentUser());
		if (handler.isAuthorized()) {
			FlowDirecter directer = new JsfFlowDirecter(this.getSuccessRedirectionPage());
			this.performSpecificSuccessAction();
			directer.redirect();
			return;
		} else {
			FlowDirecter directer = new JsfFlowDirecter(this.getFailureRedirectionPage());
			this.performSpecificFailureAction();
			directer.redirect();
			return;
		}
	}
	
	protected String getSuccessRedirectionPage() {
		return DEFAULT_SUCCESS_REDIRECTION_PAGE;
	}
	
	protected String getFailureRedirectionPage() {
		return DEFAULT_FAILURE_REDIRECTION_PAGE;
	}

	protected abstract void performSpecificSuccessAction();	
	protected abstract void performSpecificFailureAction();
	
	@Override
	protected AuthorizationHandler getAuthorizationHandler(User context) {
		return new MiddlewareAuthorizationHandler(context);
	}
}
