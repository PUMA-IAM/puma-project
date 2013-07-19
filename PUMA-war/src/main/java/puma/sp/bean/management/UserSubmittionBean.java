/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.bean.management;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import puma.sp.controllers.management.TenantManagementController;
import puma.sp.controllers.management.UserManagementController;
import puma.sp.model.Tenant;

/**
 *
 * @author jasper
 */
@ManagedBean
@SessionScoped
public class UserSubmittionBean extends AbstractTenantUserDataSubmittionBean implements Serializable {
	private static final long serialVersionUID = 1L;
    private UserManagementController userBean;
    private TenantManagementController tenantBean;
    private String name;
    private String password;
    
    public UserSubmittionBean() {
        this.userBean = new UserManagementController();
        this.tenantBean = new TenantManagementController();
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public Integer getTenant() {
    	if (this.getActiveTenant() != null)
    		return this.getActiveTenant().getId();
    	return 1; // DEBUG return null
    }
    
    public Map<String, Integer> getPossibleTenants() {
        Map<String, Integer> result = new HashMap<String, Integer>();
        for (Tenant t: this.tenantBean.allTenants()) {
            result.put(t.getName(), t.getId());
        }
        return result;
    }
    
    public void add() {
        Tenant newTenant;
        newTenant = this.tenantBean.getTenant(this.getTenant());
        this.userBean.addUser(this.getName(), this.getPassword(), newTenant);
        if (newTenant.isLocallyManaged())
            ; // LATER this.userBean.addAttribute() -> Difficulty: how to make adaptive form useradd.jsf that gives possibility to add attributes when selected tenant is locally managed
    }

	@Override
	protected void performSpecificSuccessAction() {
		this.add();		
	}

	@Override
	protected void performSpecificFailureAction() {}
}
