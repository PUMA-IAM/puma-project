package puma.sp.bean.management;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@SessionScoped
public class MiddlewareManagementBean extends AbstractMiddlewareDataSubmittionBean {
	private final static String REDIRECT_LOCATION = "index.jsf";

	@Override
	protected void performSpecificSuccessAction() {
		// Stub	
	}

	@Override
	protected void performSpecificFailureAction() {
		// Stub
	}

	public String invalidate() {
		((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession().invalidate();
		return REDIRECT_LOCATION;
	}
	
	public String userName() {
		if (super.getCurrentUser() == null)
			return "";
		return super.getCurrentUser().getLoginName();
	}
}
