package puma.proxy.controllers;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import puma.util.persistence.TransactionalWriter;
import puma.proxy.support.ProxyNamingProducer;
import puma.proxy.model.SAMLMessageHeaderContext;

public class HeaderContextController implements Serializable {
	private static final Logger logger = Logger.getLogger(HeaderContextController.class.getName());
	private static final long serialVersionUID = -983609523068465432L;
	private EntityManager em;
    private TransactionalWriter writer;
    
    public HeaderContextController() {
    	ProxyNamingProducer producer = new ProxyNamingProducer();
    	this.em = producer.produce();
    	this.writer = new TransactionalWriter(producer);
    }
        
    @SuppressWarnings("unchecked")
	public SAMLMessageHeaderContext get(String id) {
            Query q = em.createNamedQuery("SAMLMessageHeaderContext.byProxiedId");
            List<SAMLMessageHeaderContext> queryResults = q.setParameter("id", id).getResultList();
            if (queryResults.size() != 1) {
                System.err.println("Headers stored: ");
                for (Object md: this.em.createQuery("SELECT m FROM SAMLMessageHeaderContext m").getResultList()) {
                    SAMLMessageHeaderContext m = (SAMLMessageHeaderContext) md;
                    System.err.println("\tItem " + m.getOriginalSAMLIdentifier() + "/" + m.getProxiedSAMLIdentifier() + " (" + m.getAssertionConsumerServiceURL() + ")");
                }
                throw new RuntimeException("Could not find a unique header context with id " + id + ".");    
            }
            return queryResults.get(0);
    }
    
    public void add(String originalId, String proxiedId, String consumerService) {
        try {
            SAMLMessageHeaderContext ctx = new SAMLMessageHeaderContext();
            ctx.setOriginalSAMLIdentifier(originalId);
            ctx.setProxiedSAMLIdentifier(proxiedId);
            ctx.setAssertionConsumerServiceURL(consumerService);
            this.writer.write(SAMLMessageHeaderContext.class, ctx);
        } catch (SecurityException ex) {
        	logger.log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
        	logger.log(Level.SEVERE, null, ex);
        }
    }
}
