/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.bean.management;

import java.io.Serializable;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import puma.sp.controllers.management.ServiceManagementController;

/**
 *
 * @author jasper
 */
@ManagedBean
@SessionScoped
public class AttributeSubmittionBean extends AbstractMiddlewareDataSubmittionBean implements Serializable {
	private static final long serialVersionUID = 1L;
    private ServiceManagementController bean;
    private String name;
    
    public AttributeSubmittionBean() {        
        this.bean = new ServiceManagementController();
    }
    
    public void setName(String theName) {
        this.name = theName;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void createAttribute() {
        this.bean.createAttributeType(this.getName());
    }

	@Override
	protected void performSpecificSuccessAction() {
		this.createAttribute();
	}

	@Override
	protected void performSpecificFailureAction() {}
}
