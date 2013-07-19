/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.controllers.authentication;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import puma.sp.controllers.ServiceProviderNamingProducer;
import puma.sp.model.Tenant;

/**
 *
 * @author jasper
 */
public class WAYFController {
    private EntityManager em;
    public WAYFController() {
        ServiceProviderNamingProducer producer = new ServiceProviderNamingProducer();
        this.em = producer.produce();
    }
    
    @SuppressWarnings("unchecked")
	public List<Tenant> getAllTenants() {
    	Query q = this.em.createNamedQuery("Tenant.all");
    	return q.getResultList(); 
    }
    
    public Boolean existsTenant(Integer tenantId) {
    	if (this.em.find(Tenant.class, tenantId) == null)
    		return false;
    	return true;
    }
    
    @SuppressWarnings("unchecked")
	public Tenant getTenant(Integer tenantId) {
    	Query q = this.em.createNamedQuery("Tenant.byId");
    	List<Tenant> result = q.setParameter("id", tenantId).getResultList();
    	if (result.isEmpty())
    		return null; //throw new Exception("Could not find user with id " + i.toString());
    	return (Tenant) result.get(0);
    }
    
    @SuppressWarnings("unchecked")
	public Tenant getTenant(String tenantName) {
    	Query q = em.createNamedQuery("Tenant.byName");
    	List<Tenant> result = q.setParameter("name", tenantName).getResultList();
    	if (result.isEmpty())
    		return null; //throw new Exception("Could not find user with id " + i.toString());
    	return (Tenant) result.get(0);
    }
}
