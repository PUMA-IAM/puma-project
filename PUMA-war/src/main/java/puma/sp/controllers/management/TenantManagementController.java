/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.controllers.management;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import puma.sp.controllers.ServiceProviderNamingProducer;
import puma.sp.model.Service;
import puma.sp.model.Tenant;
import puma.util.exceptions.management.SubscribeException;
import puma.util.exceptions.management.UnsubscribeException;
import puma.util.persistence.TransactionalWriter;

/**
 *
 * @author jasper
 */
public class TenantManagementController {
    private EntityManager em;
    private TransactionalWriter writer;
    public TenantManagementController() {
        ServiceProviderNamingProducer producer = new ServiceProviderNamingProducer();
        this.em = producer.produce();
        this.writer = new TransactionalWriter(producer);
    }
    
    @SuppressWarnings("unchecked")
	public Tenant getTenant(Integer tenantId) {
    	Query q = em.createNamedQuery("Tenant.byId");
    	List<Tenant> result = q.setParameter("id", tenantId).getResultList();
    	if (result.isEmpty())
    		return null; //throw new Exception("Could not find user with id " + i.toString());
    	return (Tenant) result.get(0);
    }
    
    public Tenant addTenant(String name, String imageName, Integer superTenant) {
    	return this.addTenant(name, imageName, true, "", "", "", superTenant);
    }
    
    public Tenant addTenant(String name, String imageName, Boolean locallyManaged, String authnRqEndpoint, String attrRqEndpoint, String publicKey, Integer superTenant) {
        Tenant theTenant = new Tenant();
        theTenant.setName(name);
        theTenant.setImageName(imageName);
        theTenant.setLocallyManaged(locallyManaged);
        theTenant.setAuthnRequestEndpoint(authnRqEndpoint);
        theTenant.setAttrRequestEndpoint(attrRqEndpoint);
        theTenant.setIdentityProviderPublicKey(publicKey);
        theTenant.setSubscribedTo(new ArrayList<Service>());
        if (superTenant != null) {
            theTenant.setSuperTenant(this.getTenant(superTenant));
        }
        
        Query q = em.createNamedQuery("Tenant.byName");
        if (q.setParameter("name", name).getResultList().size() >= 1) {
            throw new RuntimeException("Could not create tenant: name " + name + " already in database!");
        }
        this.writer.write(Tenant.class, theTenant);
        return theTenant;
    }
    
    public void subscribe(Tenant t, Service s) throws SubscribeException {
        if (t.getSubscribedTo().contains(s)) {
            throw new SubscribeException(t.getName(), s.getName());
        }        
        t.getSubscribedTo().add(s);
    }
    
    public void unsubscribe(Tenant t, Service s) throws UnsubscribeException {
        if (!t.getSubscribedTo().contains(s))
            throw new UnsubscribeException(t.getName(), s.getName());
        t.getSubscribedTo().remove(s);
    }
    
    @SuppressWarnings("unchecked")
	public Set<Tenant> allTenants() {
        Query q = this.em.createQuery("SELECT t FROM Tenant t");
        return new HashSet<Tenant>(q.getResultList());
    }
    
    public void modifyTenant(Tenant tenant) {
    	this.writer.merge(Tenant.class, tenant);
    }
    
    public void clearAll() {
        for (Tenant t: this.allTenants()) {
            this.writer.remove(Tenant.class, t);
        }
    }
}
