package puma.sp.util.authorization;

import java.util.ArrayList;
import java.util.List;

import puma.sp.controllers.management.AttributeManagementController;
import puma.sp.model.Attribute;
import puma.sp.model.User;

public class MultipleAuthorizationHandler implements AuthorizationHandler {
	private static final String ATTRIBUTE_ROLE_SPECIFIER = "MiddlewareRole";
	private User authenticatedUser;
	private List<String> requiredAdministrativeRoles;

	public MultipleAuthorizationHandler(User context) {
		this.authenticatedUser = context;
		this.requiredAdministrativeRoles = new ArrayList<String>();
	}
	
	public void addRole(String role) {
		this.requiredAdministrativeRoles.add(role);
	}

	@Override
	public Boolean isAuthorized() {
		if (this.authenticatedUser == null)
			return false;
		AttributeManagementController attributeCtrl = new AttributeManagementController();
        List<Attribute> attributes = attributeCtrl.getAttribute(attributeCtrl.getAttributeType(ATTRIBUTE_ROLE_SPECIFIER), this.authenticatedUser);
        Boolean isAllowed = false;
        for (String role: this.requiredAdministrativeRoles) {
	        for (Attribute next: attributes) {
	        	if (((String) next.getAttributeValue()).equalsIgnoreCase(role)) {
	        		isAllowed = true;
	        		break;
	        	}
	        }
        }
		return this.authenticatedUser.getOrganization() != null && isAllowed;
	}
}
