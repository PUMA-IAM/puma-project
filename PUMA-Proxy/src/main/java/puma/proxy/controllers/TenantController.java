package puma.proxy.controllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import puma.proxy.model.RedirectedTenant;
import puma.util.persistence.TransactionalWriter;
import puma.proxy.support.ProxyNamingProducer;

public class TenantController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2483938298535191971L;
	private EntityManager em;
	private TransactionalWriter writer;
	
	public TenantController() {
		ProxyNamingProducer producer = new ProxyNamingProducer();
		this.em = producer.produce();
		this.writer = new TransactionalWriter(producer);
	}
	
	@SuppressWarnings("unchecked")
	public List<RedirectedTenant> getAllTenants() {
        Query q = em.createNamedQuery("RedirectedTenant.all");
        return q.getResultList();
    }
    
    public RedirectedTenant addTenant(String name, String authnAddress, String attrAddress) {
        RedirectedTenant tenant = new RedirectedTenant();
        tenant.setName(name);
        tenant.setAuthnHandler(authnAddress);
        tenant.setAttrHandler(attrAddress);
        this.writer.write(RedirectedTenant.class, tenant);
        return tenant;
    } 

    @SuppressWarnings("unchecked")
    public RedirectedTenant getTenant(Long chosenTenant) {
        Query q = this.em.createNamedQuery("RedirectedTenant.byId");
		List<RedirectedTenant> tenants = q.setParameter("id", chosenTenant).getResultList();
        if (tenants.size() != 1)
            throw new RuntimeException("Multiple instances for id " + chosenTenant);
        return tenants.get(0);
    }

    @SuppressWarnings("unchecked")
    public RedirectedTenant getTenant(String tenantName) {
        Query q = this.em.createNamedQuery("RedirectedTenant.byName");
        List<RedirectedTenant> tenants = q.setParameter("name", tenantName).getResultList();
        if (tenants.size() != 1)
            throw new RuntimeException("Multiple instances for name " + tenantName);
        return tenants.get(0);
    }
}
