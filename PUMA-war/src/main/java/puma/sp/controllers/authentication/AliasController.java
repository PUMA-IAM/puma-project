package puma.sp.controllers.authentication;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import puma.sp.controllers.ServiceProviderNamingProducer;
import puma.sp.model.Tenant;
import puma.sp.model.UserAlias;
import puma.util.PasswordHasher;
import puma.util.exceptions.ElementNotFoundException;
import puma.util.exceptions.MultipleElementsFoundException;
import puma.util.persistence.TransactionalWriter;

public class AliasController {
	private EntityManager em;
    private TransactionalWriter writer;
    public AliasController() {
        ServiceProviderNamingProducer producer = new ServiceProviderNamingProducer();
        this.em = producer.produce();
        this.writer = new TransactionalWriter(producer);    	
    }
    
    public Integer generateAlias(String userIdentifier, Tenant tenant) {
    	// Generate instance variables
    	UserAlias alias = this.hasAlias(userIdentifier, tenant);
    	Date now = new Date();
    	String tenantIdentifier = tenant.getId().toString();
    	if (alias == null) {
	    	byte[] result = generateHash(userIdentifier, tenantIdentifier, now);
	    	// Instantiate object 
	    	alias = new UserAlias();
	    	alias.setUserIdentifier(userIdentifier);
	    	alias.setTenantIdentifier(tenantIdentifier);
	    	alias.setTimestamp(now);
	    	alias.setAlias(result);
	    	// Persist object
	    	this.writer.write(UserAlias.class, alias);
    	}
    	// Return result
    	return alias.getId();
    }
    
    @SuppressWarnings("unchecked")
	private UserAlias hasAlias(String userIdentifier, Tenant tenant) {
    	Query q = this.em.createNamedQuery("UserAlias.byUserTenant");
    	List<UserAlias> aliases = q.setParameter("userId", userIdentifier).setParameter("tenantId", tenant.getId().toString()).getResultList();
    	if (aliases.size() == 0)
    		return null;
    	return aliases.get(0);
    	
    }
    
    @SuppressWarnings("unchecked")
	public UserAlias retrieveAlias(String aliasIdentifier) throws MultipleElementsFoundException, ElementNotFoundException {
    	Query q = this.em.createNamedQuery("UserAlias.byId");
    	List<UserAlias> aliases = q.setParameter("id", Integer.parseInt(aliasIdentifier)).getResultList();
    	if (aliases.size() > 1)
    		throw new MultipleElementsFoundException("UserAlias (value = " + new String(aliasIdentifier) + ")");
    	if (aliases.size() == 0)
    		throw new ElementNotFoundException("UserAlias (value = " + new String(aliasIdentifier) + ")");
    	return aliases.get(0);
    }
    
    private static byte[] generateHash(String userIdentifier, String tenantIdentifier, Date time) {
    	return PasswordHasher.getHashValue((tenantIdentifier + "." + userIdentifier), time.toString().getBytes());
    }
}
