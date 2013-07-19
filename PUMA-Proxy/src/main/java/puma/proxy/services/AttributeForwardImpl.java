package puma.proxy.services;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opensaml.saml2.core.AttributeQuery;
import org.opensaml.saml2.core.impl.AttributeQueryMarshaller;
import org.opensaml.saml2.core.impl.ResponseMarshaller;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import puma.proxy.clients.AttributeQueryImplService;
import puma.proxy.clients.AttributeQueryService;
import puma.proxy.support.ProviderFetcher;
import puma.proxy.support.proxy.AttributeQueryRequestProxy;
import puma.proxy.support.proxy.ResponseProxy;
import puma.proxy.support.saml.ProxyExtensionMarshaller;
import puma.util.exceptions.TenantIdentificationException;
import puma.util.exceptions.saml.ElementProcessingException;
import puma.util.exceptions.saml.ServiceParameterException;
import puma.util.saml.SAMLHelper;
import javax.jws.WebService;

@WebService(endpointInterface = "puma.proxy.services.AttributeForwardService")
public class AttributeForwardImpl implements AttributeForwardService {
		private static final Logger logger = Logger.getLogger(AttributeForwardImpl.class.getName());
		public String send(String context) {
			try {
	            if (context == null) {
	               throw new IllegalArgumentException("Web parameter given to send operation mustn't be a null pointer");
				}
				Marshaller marshaller = new AttributeQueryMarshaller();
				Marshaller responseMarshaller = new ResponseMarshaller();
				ProviderFetcher fetcher;
				// TODO No more default values. Omit everything and constrain to ProviderFetcher fetcher = new ProviderFetcher(this.extractProxyExtensionValue(context));
				List<String> relayStateParam = this.extractProxyExtensionValue(context);            
				if (relayStateParam == null) {
				    fetcher = new ProviderFetcher(); 
				} else {
				    fetcher = new ProviderFetcher(relayStateParam);
				}
				logger.log(Level.INFO, "Proxy: tenant " +  fetcher.getDesignatedTenant() + " found. Redirecting to " + fetcher.getAttributeRequestHandler() + ".");
		        AttributeQueryRequestProxy proxy = new AttributeQueryRequestProxy();
		        AttributeQuery query = proxy.reencode(context, fetcher.getAttributeRequestHandler());
		        String queryResponse = this.retrieve(XMLHelper.prettyPrintXML(marshaller.marshall(query)));
		        logger.log(Level.INFO, "********  PROXY: **********\n" + queryResponse);
		        ResponseProxy respProxy = new ResponseProxy();
		        return XMLHelper.prettyPrintXML(responseMarshaller.marshall(respProxy.reencode(queryResponse)));
		   } catch (ServiceParameterException ex) {
			   logger.log(Level.SEVERE, null, ex);
		   } catch (ElementProcessingException ex) {
			   logger.log(Level.SEVERE, null, ex);
		   } catch (MarshallingException ex) {
			   logger.log(Level.SEVERE, null, ex);
		   } catch (TenantIdentificationException ex) {
			   logger.log(Level.SEVERE, null, ex);
		}
		   return "error"; // TODO Return error Response
		}
		
	    private List<String> extractProxyExtensionValue(String context) throws ServiceParameterException, ElementProcessingException {
	        /*
	        if (sentQuery.getExtensions().getUnknownXMLObjects().isEmpty() || !(sentQuery.getExtensions().getUnknownXMLObjects().get(0) instanceof AttributeStatement))
	            throw new IllegalArgumentException("Proxy: Could not parse attribute query. No destination attribute statement in extensions.");
	        if (((AttributeStatement) sentQuery.getExtensions().getUnknownXMLObjects().get(0)).getAttributes().isEmpty() || !(((AttributeStatement) sentQuery.getExtensions().getUnknownXMLObjects().get(0)).getAttributes().get(0) instanceof Attribute))
	            throw new IllegalArgumentException("Proxy: Could not parse attribute query. No destination attribute in extensions' attribute statement.");
	        if (!(((Attribute) ((AttributeStatement) sentQuery.getExtensions().getUnknownXMLObjects().get(0)).getAttributes().get(0)).getAttributeValues().get(0) instanceof XSAny))
	            throw new IllegalArgumentException("Proxy: Could not parse attribute query. No destination attribute value in extensions' attribute.");
	        */
	        return (new ProxyExtensionMarshaller(SAMLHelper.processString(context, AttributeQuery.class).getExtensions())).get();
	    }
	    
	    private String retrieve(String samlAttrRequest) {
	    	AttributeQueryImplService forwarder = new AttributeQueryImplService();
	    	AttributeQueryService service = forwarder.getAttributeQueryImplPort();
	        return service.query(samlAttrRequest);
	    }
}

