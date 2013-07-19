/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.bean;

import java.io.Serializable;
import java.util.Set;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import puma.sp.controllers.management.ServiceManagementController;
import puma.sp.model.AttributeType;
import puma.sp.model.Service;

/**
 *
 * @author jasper
 */
@SessionScoped
@ManagedBean
public class ServiceInformationBean implements Serializable {
	private static final long serialVersionUID = 1L;
    private ServiceManagementController bean;
    
    private Service service;
    
    public ServiceInformationBean() {
        this.bean = new ServiceManagementController();
    }
    
    public void setService(String theService) {
        this.service = this.bean.getService(theService);
    }
    
    public Service getService() {
        return this.service;
    }
    
    public Set<AttributeType> getAttributes() {
        return this.bean.getRequiredAttributes(this.getService());
    }
}
