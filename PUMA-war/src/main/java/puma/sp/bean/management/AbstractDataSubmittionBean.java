package puma.sp.bean.management;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import puma.sp.controllers.management.UserManagementController;
import puma.sp.model.User;
import puma.sp.util.FlowDirecter;
import puma.sp.util.JsfFlowDirecter;
import puma.sp.util.authorization.AuthorizationHandler;

public abstract class AbstractDataSubmittionBean {
	private static final String DEFAULT_LOGIN_PAGE = "login.jsf";
	
	public abstract void submit();
	protected abstract AuthorizationHandler getAuthorizationHandler(User context);
	
	protected User getCurrentUser() {
        String subjectIdentifier = (String) ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession().getAttribute("SubjectIdentifier");
    	UserManagementController userCtrl = new UserManagementController();
    	if (subjectIdentifier != null)
    		return userCtrl.getUser(Long.parseLong(subjectIdentifier));
    	return null;
	}

	public void canViewPageRedirecter() {
		AuthorizationHandler handler = this.getAuthorizationHandler(this.getCurrentUser());
		if (!handler.isAuthorized()) {
			FlowDirecter directer = new JsfFlowDirecter(DEFAULT_LOGIN_PAGE);
			directer.redirect();
		}
	}
}
