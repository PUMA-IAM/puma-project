package puma.sp.controllers.management;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import puma.sp.controllers.ServiceProviderNamingProducer;
import puma.sp.model.Attribute;
import puma.sp.model.AttributeType;
import puma.sp.model.User;
import puma.util.persistence.TransactionalWriter;

public class AttributeManagementController {
    private EntityManager em;
    private TransactionalWriter writer;
    public AttributeManagementController() {
        ServiceProviderNamingProducer producer = new ServiceProviderNamingProducer();
        this.em = producer.produce();
        this.writer = new TransactionalWriter(producer);
    }
    
    public Attribute addAttribute(AttributeType key, Serializable value, User user) {
    	Attribute attr = new Attribute();
    	attr.setAttributeKey(key);
    	attr.setAttributeValue(value);
    	attr.setUser(user);
    	this.writer.write(Attribute.class, attr);
    	return attr;
    }
    
    @SuppressWarnings("unchecked")
	public AttributeType getAttributeType(String name) {
    	Query q = em.createNamedQuery("AttributeType.byName");
    	List<AttributeType> list = q.setParameter("name", name).getResultList();
        if (list.size() > 1) {
            throw new RuntimeException("Could not fetch attribute type: attribute type with name " + name + " has multiple instances.");
        }
        if (list.size() == 0) {
        	throw new RuntimeException("Could not fetch attribute type: attribute type with name " + name + " not found");
        }
        return list.get(0);
    }    
    
    @SuppressWarnings("unchecked")
	public AttributeType getAttributeType(Long id) {
    	Query q = em.createNamedQuery("AttributeType.byId");
    	List<AttributeType> list = q.setParameter("id", id).getResultList();
        if (list.size() > 1) {
            throw new RuntimeException("Could not fetch attribute type: attribute type with id " + id + " has multiple instances.");
        }
        if (list.size() == 0) {
        	throw new RuntimeException("Could not fetch attribute type: attribute type with id " + id + " not found");
        }
        return list.get(0);
    }
    
    @SuppressWarnings("unchecked")
	public List<Attribute> getAttribute(AttributeType key, User user) {
    	Query q = em.createNamedQuery("Attribute.byKeyUser");
    	List<Attribute> list = q.setParameter("key", key).setParameter("user", user).getResultList();
        if (list.size() == 0) {
        	return new ArrayList<Attribute>(0);
        }
        return list;    	
    }
    
    @SuppressWarnings("unchecked")
    public List<AttributeType> getAttributeTypes() {
    	Query q = em.createNamedQuery("Attribute.all");
    	return q.getResultList();
    }

}
