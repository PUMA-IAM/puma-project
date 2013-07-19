/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.persistence;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 *
 * @author jasper
 */
public abstract class EntityManagerProducer {
    public final EntityManager produce() {
        return Persistence.createEntityManagerFactory(this.getPersistenceUnitName()).createEntityManager();
    }
    
    public abstract String getPersistenceUnitName();
}
