package puma.sp.bean.management;

import puma.sp.model.User;
import puma.sp.util.authorization.AuthorizationHandler;
import puma.sp.util.authorization.TenantAuthorizationHandler;

public abstract class AbstractTenantAdministrativeDataSubmittionBean extends
		AbstractTenantDataSubmittionBean {

	private static final String ROLE_SPECIFIER = "TenantAdmin";

	@Override
	protected AuthorizationHandler getAuthorizationHandler(User context) {
		return new TenantAuthorizationHandler(context, ROLE_SPECIFIER);
	}
}
