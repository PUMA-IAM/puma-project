package puma.idp.controllers;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import puma.idp.model.User;
import puma.idp.model.UserAttribute;
import puma.idp.util.IdPNamingProvider;
import puma.util.persistence.TransactionalWriter;

public class AttributeController {
    private EntityManager em;
    private TransactionalWriter writer;
    
	public AttributeController() {
    	IdPNamingProvider provider = new IdPNamingProvider();
        this.em = provider.produce();
        this.writer = new TransactionalWriter(provider);
	}
	
	public UserAttribute addAttribute(User u, String key, String value) {
		UserAttribute attr = new UserAttribute();
		attr.setAttributeKey(key);
		attr.setAttributeValue(value);
		attr.setAttributeUser(u);
		this.writer.write(UserAttribute.class, attr);
		return attr;
	}
	
	public List<String> getAttributeValue(User u, String key) {
		Query q = this.em.createNamedQuery("UserAttribute.byKeyUser");
		@SuppressWarnings("unchecked")
		List<UserAttribute> values = q.setParameter("key", key).setParameter("user", u).getResultList();
		if (values.isEmpty())
			return null;
		List<String> result = new ArrayList<String>();
		for (UserAttribute attr: values) {
			result.add(attr.getAttributeValue());
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<UserAttribute> allAttributes() {
		Query q = this.em.createNamedQuery("UserAttribute.all");
		return q.getResultList();
	}

	public void remove(UserAttribute a) {
		this.writer.remove(UserAttribute.class, a);
	}

	@SuppressWarnings("unchecked")
	public List<UserAttribute> getAttributes(User userObject) {
		return this.em.createNamedQuery("UserAttribute.byUser").setParameter("user", userObject).getResultList();
	}
	
	public void clearAll() {
		this.writer.customQuery("DELETE FROM UserAttribute ua");
    }
}
