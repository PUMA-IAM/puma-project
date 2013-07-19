package puma.sp.bean.management;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import puma.sp.controllers.management.ServiceManagementController;
import puma.sp.controllers.management.TenantManagementController;
import puma.sp.model.Service;
import puma.util.exceptions.management.SubscribeException;

@ManagedBean
@SessionScoped
public class ServiceSubscriptionBean extends AbstractTenantAdministrativeDataSubmittionBean implements Serializable {
	private static final Logger logger = Logger.getLogger(ServiceSubscriptionBean.class.getName());
	private static final long serialVersionUID = 2578304131379815925L;
	private ServiceManagementController serviceCtrl;
	private Long selectedService;
	
	public ServiceSubscriptionBean() {
		 this.serviceCtrl = new ServiceManagementController();
	}
	
	public void setSelectedService(Long id) {
		this.selectedService = id;
	}
	
	public Long getSelectedService() {
		return this.selectedService;
	}
	
	public Map<String, Long> getListServices() {
		// DEBUG Demo purposes modification
		/*
		if (this.getActiveTenant() == null)
			return new HashMap<String, Long>(0);*/
		TenantManagementController tenantCtrl = new TenantManagementController();
		List<Service> subscribedServices;
		if (this.getActiveTenant() == null) // <- Remove
			subscribedServices = tenantCtrl.getTenant(1).getSubscribedTo(); // <- Remove
		else	// <- Remove
			subscribedServices = this.getActiveTenant().getSubscribedTo();
		Map<String, Long> result = new HashMap<String, Long>();
		for (Service next: this.serviceCtrl.getServices()) {
			if (!subscribedServices.contains(next)) {
				result.put(next.getName(), next.getId());
			}
		}
		return result;
	}

	@Override
	protected void performSpecificSuccessAction() {
		try {
			TenantManagementController tenantCtrl = new TenantManagementController();
			Service service = this.serviceCtrl.getService(this.getSelectedService());
			if (this.getActiveTenant() != null && service != null) { 
					tenantCtrl.subscribe(this.getActiveTenant(), service);
			}
		} catch (SubscribeException e) {
			logger.log(Level.SEVERE, null, e);
		}
	}

	@Override
	protected void performSpecificFailureAction() {
		// Stub		
	}
}
