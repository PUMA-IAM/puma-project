package puma.sp.bean.management;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import puma.sp.controllers.management.AttributeManagementController;
import puma.sp.controllers.management.UserManagementController;
import puma.sp.model.Attribute;
import puma.sp.model.AttributeType;
import puma.sp.model.User;

@ManagedBean
@SessionScoped
public class UserAdministrationBean extends AbstractTenantUserDataSubmittionBean implements Serializable {
	private static final long serialVersionUID = -8138106446853889730L;
	private UserManagementController userCtrl;
	private AttributeManagementController attrCtrl;
	private Integer selectedUser;
	private Long selectedAttributeType;
	private String attributeValue;
	
	public UserAdministrationBean() {
		this.userCtrl = new UserManagementController();
		this.attrCtrl = new AttributeManagementController();
	}
	
	public Map<String, Integer> listUsers() {
		List<User> users = this.userCtrl.getUsers(this.getActiveTenant());
		Map<String, Integer> result = new HashMap<String, Integer>(users.size());
		for (User next: users) {
			result.put(next.getLoginName(), next.getId());
		}
		return result;
	}
	
	public Integer getSelectedUser() {
		return this.selectedUser;
	}
	
	public void setSelectedUser(Integer i) {
		this.selectedUser = i;
	}
	
	public Long getSelectedAttributeType() {
		return this.selectedAttributeType;
	}
	
	public void setSelectedAttributeType(Long id) {
		this.selectedAttributeType = id;
	}
	
	public Map<String, Long> listAttributes() {
		User selected = this.userCtrl.getUser(this.getSelectedUser().longValue());
		if (selected == null) {
			return new HashMap<String, Long>(0);
		}
		Map<String, Long> result = new HashMap<String, Long>(selected.getAttributes().size());
		for (Attribute next: selected.getAttributes()) {
			result.put(next.getAttributeKey().getName(), next.getAttributeKey().getId());
		}
		return result;
	}
	
	public String getAttributeValue() {
		return this.attributeValue;
	}
	
	public void setAttributeValue(String value) {
		this.attributeValue = value;
	}

	@Override
	protected void performSpecificSuccessAction() {
		AttributeType type = this.attrCtrl.getAttributeType(this.selectedAttributeType);
		User user = this.userCtrl.getUser(this.getSelectedUser().longValue());
		if (type != null && this.getAttributeValue() != null && user != null)
			this.attrCtrl.addAttribute(type, this.getAttributeValue(), this.getCurrentUser());
	}

	@Override
	protected void performSpecificFailureAction() {
		// Stub		
	}
}
