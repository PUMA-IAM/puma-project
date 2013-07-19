package puma.sp.bean.management;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import puma.sp.model.Tenant;
import puma.sp.util.FlowDirecter;
import puma.sp.util.JsfFlowDirecter;
import puma.sp.util.authorization.AuthorizationHandler;

public abstract class AbstractTenantDataSubmittionBean extends AbstractDataSubmittionBean {
	private static final String DEFAULT_SUCCESS_REDIRECTION_PAGE = "success.xhtml";
	private static final String DEFAULT_FAILURE_REDIRECTION_PAGE = "error.jsf";
	@SuppressWarnings("unused")
	private static final String DEFAULT_WAYF_PAGE = "index.jsf";
	@SuppressWarnings("unused")
	private static final String DEFAULT_LOGIN_PAGE = "login.jsf";
	
	@Override
	public final void submit() {
		if (this.getAuthorizationHandler(this.getCurrentUser()).isAuthorized()) { // DEBUG ...
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

	protected String getFailureRedirectionPage() {
		return DEFAULT_SUCCESS_REDIRECTION_PAGE;
	}

	protected String getSuccessRedirectionPage() {
		return DEFAULT_FAILURE_REDIRECTION_PAGE;
	}
	
	protected abstract void performSpecificSuccessAction();
	protected abstract void performSpecificFailureAction();

	protected Tenant getActiveTenant() {
		return (Tenant) ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession().getAttribute("Tenant");
	}
	
	@Override
	public void canViewPageRedirecter() {
		@SuppressWarnings("unused")
		AuthorizationHandler handler = this.getAuthorizationHandler(this.getCurrentUser());
		// DEBUG  Uncomment (Commented for demo purposes)
		/*
		if (!handler.isAuthorized()) {
			FlowDirecter directer;
			if (this.getActiveTenant() == null)
				directer = new JsfFlowDirecter(DEFAULT_WAYF_PAGE);
			else 
				directer = new JsfFlowDirecter(DEFAULT_LOGIN_PAGE);
			directer.redirect();
		}*/
	}
}
