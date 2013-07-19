/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.controllers.authentication;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import puma.sp.controllers.ServiceProviderNamingProducer;
import puma.sp.model.Attribute;
import puma.sp.model.AttributeType;
import puma.sp.model.Service;
import puma.sp.model.SessionRequest;
import puma.sp.model.Tenant;
import puma.sp.model.User;
import puma.util.PasswordHasher;
import puma.util.persistence.TransactionalWriter;

/**
 *
 * @author jasper
 */
public class LoginController {
    private EntityManager em;
    private TransactionalWriter writer;
    public LoginController() {
        ServiceProviderNamingProducer producer = new ServiceProviderNamingProducer();
        this.em = producer.produce();
        this.writer = new TransactionalWriter(producer);
    }
    
    public Boolean logIn(User u, String attemptedPassword) {
    	byte[] theHash;
    	if (u == null) {
            return false;
        }
    	theHash = PasswordHasher.getHashValue(attemptedPassword, u.getPasswordSalt());
    	if (PasswordHasher.equalHash(u.getPasswordHash(), theHash)) {
            return true;
        }
    	return false;
    }   
    
    @SuppressWarnings("unchecked")
	public User getUser(String loginName, Tenant tenantId) {
    	Query q;
    	if (tenantId == null)
    		q = this.em.createNamedQuery("User.byNameTenantNULL");
    	else {
    		q = this.em.createNamedQuery("User.byNameTenant");
    		q.setParameter("tenant", tenantId);
    	}
    	List<User> result = q.setParameter("name", loginName.toString()).getResultList();
    	if (result.isEmpty()) {
            return null;
        }
    	return (User) result.get(0);
    }
    
    public void createSessionRequest(String assertionId, String relayState) {
        SessionRequest req = new SessionRequest();
        req.setRelayState(relayState);
        req.setRequestId(assertionId);
        this.writer.write(SessionRequest.class, req);
    }
    
    public String getRelayState(String assertionId) {
    	return this.getRelayState(assertionId, false);
    }
    
    @SuppressWarnings("unchecked")
	public String getRelayState(String assertionId, Boolean remove) {
    	Query q = em.createNamedQuery("SessionRequest.bySessionId");
        List<SessionRequest> result = q.setParameter("id", assertionId).getResultList();
        if (result.isEmpty()) {
            return null;
        }
        String toReturn = (String) result.get(0).getRelayState();
        em.remove(result.get(0));
        return toReturn;
    }
    
    @SuppressWarnings("unchecked")
	public Service fetchService(String name) {
        Query q = em.createNamedQuery("Service.byName");
        List<Service> services = q.setParameter("name", name).getResultList();
        if (services.isEmpty()) {
            return null;
        }
        return services.get(0);
    }
    
    public Map<String, Serializable> getAttributes(Service service, User user) {        
        Map<String, Serializable> result = new HashMap<String, Serializable>();
        for (AttributeType type: service.getAttributes()) {
            for (Attribute attribute: user.getAttributes()) {
                if (attribute.getAttributeKey().equals(type)) {
                    result.put(attribute.getAttributeKey().getName(), attribute.getAttributeValue());
                }
            }
        }
        return result;
    }
}
