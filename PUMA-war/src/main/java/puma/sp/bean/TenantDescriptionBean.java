/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import puma.sp.controllers.authentication.WAYFController;
import puma.sp.model.Tenant;
import puma.sp.util.FlowDirecter;
import puma.sp.util.JsfFlowDirecter;

/**
 *
 * @author jasper
 */
@ManagedBean
@SessionScoped
public class TenantDescriptionBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private WAYFController bean;
	private Integer chosenTenant;
	
	public TenantDescriptionBean() {
		this.bean = new WAYFController();
    }
        	
	public String getTenantName(Integer tenantId) {
		if (!this.isValid(tenantId)) {
	            return "";
	        }
		return this.bean.getTenant(tenantId).getName();
	}
	
	public Map<String, Integer> getTenantListing() {
        Map<String, Integer> result = new HashMap<String, Integer>();
        result.put("PUMA Middleware", Integer.MIN_VALUE);
        for (Tenant t: this.bean.getAllTenants()) {
                result.put(t.getName(), t.getId());
        }
        return result;
	}
        	
	public void setChosenTenant(Integer id) {
		if (this.isValid(id)) {
                this.chosenTenant = id;
        }
		else {
                this.chosenTenant = null;
        }
	}
	
	public Integer getChosenTenant() {
		return this.chosenTenant;
	}
	
    public Tenant tenantObject() {
    	if (this.chosenTenant == null || this.chosenTenant < 0)
    		return null;
        return this.bean.getTenant(this.chosenTenant);
    }
    
	public Boolean isValid(Integer tenantId) {
		return this.bean.existsTenant(tenantId);
	}
	
	public String login() {
    	FlowDirecter directer = null;
    	directer = (FlowDirecter) ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession().getAttribute("FlowRedirectionElement");                        
    	if (directer == null) {
    		directer = new JsfFlowDirecter("login.jsf");
    	}
    	directer.addAttribute("Tenant", this.tenantObject());
    	directer.redirect();
        return "error"; // Will not be executed unless an error occurs
	}
	
	public String getAuthnRqConsumer(Integer tenantId) {
        if (this.isValid(tenantId)) {
            return this.bean.getTenant(tenantId).getAuthnRequestEndpoint().toString();
        }
        return "error";		
	}
        
	public String getAttrRqConsumer(Integer tenantId) {
        if (this.isValid(tenantId)) {
            return this.bean.getTenant(tenantId).getAttrRequestEndpoint().toString();
        }
        return "error";		
	}
	
	public String getTenantImage() {
        if (this.chosenTenant == null) {
            return "puma.png";
        }
		String result = this.bean.getTenant(this.chosenTenant).getImageName();
		if (result.isEmpty()) {
            return "puma.png";
        }
		return result;
	}
}
