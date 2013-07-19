/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.bean.management;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import puma.sp.controllers.management.ServiceManagementController;
import puma.sp.model.AttributeType;

/**
 *
 * @author jasper
 */
@ManagedBean
@SessionScoped
public class ServiceSubmittionBean extends AbstractMiddlewareDataSubmittionBean implements Serializable {
	private static final long serialVersionUID = 1L;
    private ServiceManagementController bean;
    private String name;
    private Set<String> attributes;
    
    public ServiceSubmittionBean() {
        this.bean = new ServiceManagementController();
    }
    
    public void setName(String theName) {
        this.name = theName;
    }
    
    public String getName() {
        return this.name;
    }
        
    public void setAttributes(Set<String> attr) {
        this.attributes = attr;
    }
    
    public Set<String> getAttributes() {
        return this.attributes;
    }
    
    public Map<String, String> getPossibleAttributes() {
        Map<String, String> result = new HashMap<String, String>();
        for (AttributeType t: this.bean.getAttributeTypes()) {
            result.put(t.getName(), t.getName());
        }
        return result;
    }
    
    public void submitService() {
        this.bean.createService(this.getName(), this.getAttributes());
    }

	@Override
	protected void performSpecificSuccessAction() {
		this.submitService();
	}

	@Override
	protected void performSpecificFailureAction() {}
}