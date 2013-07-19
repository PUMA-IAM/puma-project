package puma.sp.util.authorization;

import java.util.List;

import puma.sp.controllers.management.AttributeManagementController;
import puma.sp.model.Attribute;
import puma.sp.model.User;

public class MiddlewareAuthorizationHandler implements AuthorizationHandler {
	private final static String MIDDLEWARE_ADMIN_ROLE = "MiddlewareAdmin";
	private final static String ATTRIBUTE_ROLE_SPECIFIER = "MiddlewareRole";
	private User currentUser;
	
	public MiddlewareAuthorizationHandler(User user) {
		this.currentUser = user;
	}
	
	@Override
	public Boolean isAuthorized() {
		if (this.currentUser == null) 
			return false;
		AttributeManagementController attributeCtrl = new AttributeManagementController();
        List<Attribute> attributes = attributeCtrl.getAttribute(attributeCtrl.getAttributeType(ATTRIBUTE_ROLE_SPECIFIER), this.currentUser);
        Boolean isAllowed = false;
        for (Attribute next: attributes) {
        	if (((String) next.getAttributeValue()).equalsIgnoreCase(MIDDLEWARE_ADMIN_ROLE)) {
        		isAllowed = true;
        		break;
        	}
        }
		return this.currentUser.getOrganization() == null && isAllowed;
	}
}
