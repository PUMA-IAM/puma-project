package puma.proxy.support;
import puma.util.persistence.EntityManagerProducer;

/**
 *
 * @author jasper
 */
public class ProxyNamingProducer extends EntityManagerProducer {
    @Override
    public String getPersistenceUnitName() {
        return "ProxyPU";
    }    
}
