package puma.proxy.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import puma.proxy.controllers.TenantController;

@ManagedBean
@SessionScoped
public class TenantMgmtBean implements Serializable {
	private static final long serialVersionUID = 2857693911184105010L;
	private TenantController ctrl;
    private String name;
    private String authnAddress;
    private String attrAddress;
    
    public void setName(String n) {
        this.name = n;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setAuthnAddress(String auth) {
        this.authnAddress = auth;
    }
    
    public String getAuthnAddress() {
        return this.authnAddress;
    }
    
    public void setAttrAddress(String attr) {
        this.attrAddress = attr;
    }
    
    public String getAttrAddress() {
        return this.attrAddress;
    }  
    
    public void submit() {
    	this.ctrl.addTenant(this.getName(), this.getAuthnAddress(), this.getAttrAddress());
    }
}
