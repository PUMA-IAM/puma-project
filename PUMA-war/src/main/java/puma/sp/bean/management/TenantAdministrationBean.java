/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.bean.management;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import puma.sp.controllers.management.TenantManagementController;
import puma.sp.util.FlowDirecter;
import puma.sp.util.JsfFlowDirecter;

/**
 *
 * @author jasper
 */
@ManagedBean
@SessionScoped
public class TenantAdministrationBean extends AbstractMiddlewareDataSubmittionBean implements Serializable {
	private static final Logger logger = Logger.getLogger(TenantAdministrationBean.class.getName());
	private static final long serialVersionUID = 1L;
    private String name;
    private Boolean federated;
    private String authnRqEndpoint;
    private String attrRqEndpoint;
    private String publicKey;
    private Integer superTenant;
    private static String defaultImage = "PUMA.png";
    
    private TenantManagementController bean;
    
    public TenantAdministrationBean() {
        this.bean = new TenantManagementController();
    }
    
    public void setName(String theName) {
        this.name = theName;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setFederated(Boolean theValue) {
        this.federated = theValue;
    }
    
    public Boolean getFederated() {
        return this.federated;
    }
    
    public void setAuthnRqEndpoint(String theURL) {
        this.authnRqEndpoint = theURL;
    }
    
    public String getAuthnRqEndpoint() {
        return this.authnRqEndpoint;
    }
    
    public void setAttrRqEndpoint(String theURL) {
        this.attrRqEndpoint = theURL;
    }
    
    public String getAttrRqEndpoint() {
        return this.attrRqEndpoint;
    }
    
    public void setPublicKey(String key) {
        this.publicKey = key;
    }
    
    public String getPublicKey() {
        return this.publicKey;
    }
    
    public void setSuperTenant(Integer index) {
        this.superTenant = index;
    }
    
    public Integer getSuperTenant() {
        return this.superTenant;
    }
    
    public void add() {
        try {
        	/*
        	// Check for a role 'MiddlewareAdmin' in the currently logged on user
        	String subjectIdentifier = (String) ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession().getAttribute("SubjectIdentifier");
        	UserManagementController userCtrl = new UserManagementController();        	
        	User loggedOnUser = userCtrl.getUser(Long.parseLong(subjectIdentifier));
        	if (loggedOnUser != null) {
	            AttributeManagementController attributeCtrl = new AttributeManagementController();
	            List<Attribute> attributes;
	            Boolean isAllowed = false;
	            if (loggedOnUser != null && loggedOnUser.getOrganization() == null) {		// Only when a user has logged on directly to the middleware can he/she perform administration and does not belong to a tenant
		            attributes = attributeCtrl.getAttribute(attributeCtrl.getAttributeType("MiddlewareRole"), loggedOnUser);
		            for (Attribute next: attributes) {
		            	if (((String) next.getAttributeValue()).equalsIgnoreCase("MiddlewareAdmin")) {
		            		isAllowed = true;
		            	}
		            }
	            }
	        	if (isAllowed) {*/
		            if (!this.federated) {
		                this.bean.addTenant(this.name, TenantAdministrationBean.defaultImage, this.getSuperTenant());
		            }
		            else {
		                this.bean.addTenant(this.name, TenantAdministrationBean.defaultImage, !this.federated, this.authnRqEndpoint, this.attrRqEndpoint, this.publicKey, this.getSuperTenant());
		            }
		            FacesContext.getCurrentInstance().getExternalContext().redirect("index.jsf");
	        	/*} else {
	        		FacesContext.getCurrentInstance().getExternalContext().redirect("error.jsf");
	        	}
        	} else {
        		// If the user could not be found, redirect to an error page. The user is probably authenticated remotely or an illegal user identifier has slipped in.
        		FacesContext.getCurrentInstance().getExternalContext().redirect("error.jsf");
        	}*/
        } catch (NumberFormatException ex) {
        	logger.log(Level.SEVERE, null, ex);
        	try {
        		FacesContext.getCurrentInstance().getExternalContext().redirect("error.jsf");
        	} catch (IOException e) {
            	logger.log(Level.SEVERE, null, e);
            }         	
        } catch (IOException ex) {
        	logger.log(Level.SEVERE, null, ex);
        } 
    }

	@Override
	protected void performSpecificSuccessAction() {
		FlowDirecter directer = new JsfFlowDirecter("middlewaremgmtindex.jsf");
		if (!this.federated) {
            this.bean.addTenant(this.name, TenantAdministrationBean.defaultImage, this.getSuperTenant());
        }
        else {
            this.bean.addTenant(this.name, TenantAdministrationBean.defaultImage, !this.federated, this.authnRqEndpoint, this.attrRqEndpoint, this.publicKey, this.getSuperTenant());
        }
        directer.redirect();
	}

	@Override
	protected void performSpecificFailureAction() {}   
}
