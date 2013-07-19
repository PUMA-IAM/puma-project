/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.controllers.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import puma.sp.controllers.ServiceProviderNamingProducer;
import puma.sp.model.Attribute;
import puma.sp.model.AttributeType;
import puma.sp.model.Service;
import puma.sp.model.Tenant;
import puma.sp.model.User;
import puma.util.PasswordHasher;
import puma.util.exceptions.ElementNotFoundException;
import puma.util.persistence.TransactionalWriter;

/**
 *
 * @author jasper
 */
public class UserManagementController {
    private EntityManager em;
    private TransactionalWriter writer;
    public UserManagementController() {
        ServiceProviderNamingProducer producer = new ServiceProviderNamingProducer();
        this.em = producer.produce();
        this.writer = new TransactionalWriter(producer);
    }
    
    public User addUser(String name, String password, Tenant tenant) {
    	byte[] salt = PasswordHasher.generateSalt();
        User u = new User();        
    	u.setLoginName(name);
        u.setOrganization(tenant);
    	u.setPasswordSalt(salt);
    	u.setPasswordHash(PasswordHasher.getHashValue(password, salt));
    	this.writer.write(User.class, u);
    	if (tenant != null)
    		this.writer.merge(Tenant.class, tenant);
    	return u;
    }

    public void removeUser(User u) {
    	this.writer.remove(User.class, u);
    }

    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
    	Query q = em.createNamedQuery("User.all");
    	return q.getResultList();    	    	
    }

    public Boolean existsUser(User u) {
    	if (em.find(User.class, u.getId()) == null) {
            return false;
        }
    	return true;
    }

    @SuppressWarnings("unchecked")
    public User getUser(Long i) {
    	Query q = em.createNamedQuery("User.byId");
    	List<User> result = q.setParameter("id", i).getResultList();
    	if (result.isEmpty()) {
            return null;
        }
    	return (User) result.get(0);
    }
    
    @SuppressWarnings("unchecked")
	public Map<String, List<Attribute>> getAttributes(Service service, User user) throws ElementNotFoundException {
    	Map<String, List<Attribute>> result = new HashMap<String, List<Attribute>>();
    	Query q = this.em.createNamedQuery("Attribute.byKeyUser");
    	for (AttributeType type: service.getAttributes()) {
    		List<Attribute> queryResult = q.setParameter("user", user).setParameter("key", type).getResultList();
    		if (queryResult.isEmpty())
    			throw new ElementNotFoundException(type.getName());
    		for (Attribute next: queryResult) {
    			if (!result.containsKey(next.getAttributeKey().getName()))
    				result.put(next.getAttributeKey().getName(), new ArrayList<Attribute>());
    			result.get(next.getAttributeKey().getName()).add(next);
    		}
    	}
    	return result;
    }

    public void clearAll() {
    	this.writer.customQuery("DELETE FROM User");
    	this.writer.customQuery("DELETE FROM SessionRequest");
    	this.writer.customQuery("DELETE FROM Attribute");
    	this.writer.customQuery("DELETE FROM UserAlias");
    }

	@SuppressWarnings("unchecked")
	public List<User> getUsers(Tenant tenant) {
    	Query q = em.createNamedQuery("User.byTenant");
    	return q.setParameter("tenant", tenant).getResultList();    	    
	}
}
