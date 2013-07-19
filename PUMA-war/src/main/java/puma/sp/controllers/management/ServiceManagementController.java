/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.controllers.management;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import puma.sp.controllers.ServiceProviderNamingProducer;
import puma.sp.model.AttributeType;
import puma.sp.model.Service;
import puma.util.persistence.TransactionalWriter;

/**
 *
 * @author jasper
 */
public class ServiceManagementController {
    private EntityManager em;
    private TransactionalWriter writer;
    public ServiceManagementController() {
        ServiceProviderNamingProducer producer = new ServiceProviderNamingProducer();
        this.em = producer.produce();
        this.writer = new TransactionalWriter(producer);
    }
    
    @SuppressWarnings("unchecked")
	public Service createService(String name, Set<String> attributes) {
        Query q = em.createNamedQuery("AttributeType.byName");
        Set<AttributeType> attributeTypes = new HashSet<AttributeType>();
        for (String attrName: attributes) {
            List<AttributeType> tmp = q.setParameter("name", attrName).getResultList();
            if (tmp.isEmpty()) {
                throw new RuntimeException("Could not create service: could not find an attribute type with name " + attrName + ".");
            }
            attributeTypes.add(tmp.get(0));
        }
        q = em.createNamedQuery("Service.byName");
        if (q.setParameter("name", name).getResultList().size() >= 1) {
            throw new RuntimeException("Could not create service: service with name " + name + " already present.");
        }
        Service service = new Service();
        service.setName(name);
        service.setAttributes(attributeTypes);
        writer.write(Service.class, service);
        return service;
    }

    public AttributeType createAttributeType(String name) {
        Query q = em.createNamedQuery("AttributeType.byName");
        if (q.setParameter("name", name).getResultList().size() >= 1) {
            throw new RuntimeException("Could not create attribute type: attribute type with name " + name + " already present.");
        }
        AttributeType attribute = new AttributeType();
        attribute.setName(name);
        writer.write(AttributeType.class, attribute);
        return attribute;
    }
    
    @SuppressWarnings("unchecked")
	public Set<AttributeType> getAttributeTypes() {
        Query q = em.createQuery("SELECT t FROM AttributeType t");
        return new HashSet<AttributeType>(q.getResultList());
    }

    public Service getService(String name) {
         Query q = em.createNamedQuery("Service.byName");
         if (q.setParameter("name", name).getResultList().size() > 0) {
            return (Service) q.setParameter("name", name).getResultList().get(0);
        }
         return null;
    }

    public Service getService(Long id) {
        Query q = em.createNamedQuery("Service.byId");
        if (q.setParameter("id", id).getResultList().size() > 0) {
           return (Service) q.setParameter("id", id).getResultList().get(0);
       }
        return null;
   }
    
    public Set<AttributeType> getRequiredAttributes(Service service) {
        Set<AttributeType> result = new HashSet<AttributeType>();
        for (AttributeType t: service.getAttributes()) {
            result.add(t);
        }
        return result;
    }

    public void clearAll() {
        for (AttributeType t: this.getAttributeTypes()) {
            this.writer.remove(AttributeType.class, t);
        }
        for (Service s: this.getServices()) {
            this.writer.remove(Service.class, s);
        }
    }

    @SuppressWarnings("unchecked")
	public Set<Service> getServices() {
        Query q = this.em.createQuery("SELECT s FROM Service s");
        return new HashSet<Service>(q.getResultList());
    }

}
