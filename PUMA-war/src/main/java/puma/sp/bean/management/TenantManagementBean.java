package puma.sp.bean.management;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;

import puma.sp.model.User;
import puma.sp.util.authorization.AuthorizationHandler;
import puma.sp.util.authorization.MultipleAuthorizationHandler;

@ManagedBean
@SessionScoped
public class TenantManagementBean extends AbstractTenantDataSubmittionBean {

	@Override
	protected void performSpecificSuccessAction() {
		// Stub
	}

	@Override
	protected void performSpecificFailureAction() {
		// Stub
	}

	@Override
	protected AuthorizationHandler getAuthorizationHandler(User context) {
		MultipleAuthorizationHandler handler = new MultipleAuthorizationHandler(context);
		handler.addRole("TenantAdmin");
		handler.addRole("UserAdmin");
		return handler;
	}
	
	public String userName() {
		if (this.getCurrentUser() == null)
			return "";
		return this.getCurrentUser().getLoginName();
	}
	
	public String tenantName() {
		if (this.getActiveTenant() == null)
			return "";
		return this.getActiveTenant().getName();
	}

}
