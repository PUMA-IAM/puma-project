package puma.sp.util.authorization;

import java.util.List;

import puma.sp.controllers.management.AttributeManagementController;
import puma.sp.model.Attribute;
import puma.sp.model.User;

public class TenantAuthorizationHandler implements AuthorizationHandler {
	private static final String ATTRIBUTE_ROLE_SPECIFIER = "MiddlewareRole";
	private User authenticatedUser;
	private String requiredAdministrativeRole;

	public TenantAuthorizationHandler(User context, String administrativeRole) {
		this.authenticatedUser = context;
		this.requiredAdministrativeRole = administrativeRole;
	}

	@Override
	public Boolean isAuthorized() {
		if (this.authenticatedUser == null)
			return false;
		AttributeManagementController attributeCtrl = new AttributeManagementController();
        List<Attribute> attributes = attributeCtrl.getAttribute(attributeCtrl.getAttributeType(ATTRIBUTE_ROLE_SPECIFIER), this.authenticatedUser);
        Boolean isAllowed = false;
        for (Attribute next: attributes) {
        	if (((String) next.getAttributeValue()).equalsIgnoreCase(this.requiredAdministrativeRole)) {
        		isAllowed = true;
        		break;
        	}
        }
		return this.authenticatedUser.getOrganization() != null && isAllowed;
	}

}
