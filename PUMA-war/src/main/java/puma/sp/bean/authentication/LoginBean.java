/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.bean.authentication;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import puma.sp.bean.TenantDescriptionBean;
import puma.sp.controllers.authentication.LoginController;
import puma.sp.model.Tenant;
import puma.sp.model.User;
import puma.sp.util.FlowDirecter;
import puma.sp.util.JsfFlowDirecter;

/**
 *
 * @author jasper
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {
	private static final Logger logger = Logger.getLogger(LoginBean.class.getName());
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value="#{tenantDescriptionBean}")
	private TenantDescriptionBean tenantBean;
    private LoginController bean;
	private String userName;
	private String password;
	
	public LoginBean() {
            this.bean = new LoginController();
	}
	
	public void setUserName(String name) {
		this.userName = name;
	}
        
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public String getUserName() {
        return this.userName;
    }
	
	public void setAttempt(String attempt) {
		this.password = attempt;
	}
	
	public void setTenantBean(TenantDescriptionBean bean) {
		this.tenantBean = bean;
	}
                
    private Tenant getTenantObject() {
        Tenant tenant = (Tenant) ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession().getAttribute("Tenant");
        if (tenant == null) {
            if (this.tenantBean == null || this.tenantBean.tenantObject() == null) {
                return null;
            }
            else {
                return this.tenantBean.tenantObject();
            }
        }
        return tenant;
    }
		
	public Boolean authenticate() {
		User theUser = this.bean.getUser(this.userName, this.getTenantObject());
		if (theUser != null)
			return this.bean.logIn(theUser, this.password);
		return false;
	}
	
	public String performLogin() throws ServletException {
        FlowDirecter directer = (FlowDirecter) ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession().getAttribute("FlowRedirectionElement");
        User authenticatedUser = this.bean.getUser(this.getUserName(), this.getTenantObject());
        
        if (directer == null) {
        	if (this.getTenantObject() == null) {
        		directer = new JsfFlowDirecter("middlewaremgmtindex.jsf");
        		directer.addAttribute("Tenant", null);
        	} else {
        		directer = new JsfFlowDirecter("tenantmgmtindex.jsf");
        	}
        }
        logger.log(Level.INFO, "Current status: directer " + directer==null?"null":directer.getClass().getCanonicalName().toString());
        if (authenticatedUser == null)
        	logger.log(Level.INFO, "Current status: user null");
        else
        	logger.log(Level.INFO, "Current status: user " + authenticatedUser.getLoginName());
        logger.log(Level.INFO, "Current status: authenticated " + this.authenticate().toString());
        if (authenticatedUser != null && this.authenticate()) {
        	directer.addAttribute("SubjectIdentifier", authenticatedUser.getId().toString());
        	directer.redirect();
        	return "";
        }
        return "error";
	}
}
