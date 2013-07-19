/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.util.saml;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.opensaml.saml2.core.AttributeQuery;
import org.opensaml.saml2.core.impl.AttributeQueryMarshaller;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Element;
import puma.sp.model.AttributeType;
import puma.sp.model.Tenant;
import puma.util.saml.SAMLHelper;
import puma.util.saml.elements.AttributeFactory;
import puma.util.saml.elements.CustomProxyExtensionFactory;
import puma.util.saml.elements.ExtensionsFactory;
import puma.util.saml.elements.IssuerFactory;
import puma.util.saml.elements.SubjectFactory;
import puma.util.saml.encoding.AssertableHandler;
import puma.util.saml.messages.AttributeQueryFactory;

/**
 *
 * @author jasper
 */
public class AttributeRequestHandler extends AssertableHandler {
	private static final Logger logger = Logger.getLogger(AttributeRequestHandler.class.getName());
    private Tenant tenant;
    private String subject;
    private Set<AttributeType> attributes;
    
    public AttributeRequestHandler(Set<AttributeType> attributes, String subject, Tenant requestingTenantParty) {
        super();
        this.tenant = requestingTenantParty;
        this.subject = subject;
        this.attributes = attributes;
        SAMLHelper.initialize();
    }
    
    public String prepareResponse(HttpServletResponse response, AttributeQuery unencodedSAMLRequest) throws MessageEncodingException {
            try {
                // Add the extension to make the proxy recognize the next tenant
                ExtensionsFactory factory = new ExtensionsFactory();
                factory.addFactory((new CustomProxyExtensionFactory(this.tenant.toHierarchy())));
                unencodedSAMLRequest.setExtensions(factory.produce());
                // Marshall the message
                Marshaller marshaller = new AttributeQueryMarshaller();
                Element query = marshaller.marshall(unencodedSAMLRequest);
                // Return result
                return XMLHelper.prettyPrintXML(query);                
            } catch (MarshallingException ex) {
            	logger.log(Level.SEVERE, null, ex);
                return null;
            }            
    }
    
    
    public AttributeQuery buildRequest() {
        AttributeQueryFactory factory = new AttributeQueryFactory(this.getAssertionId(), (new SubjectFactory(this.subject)).produce(), this.tenant.getAttrRequestEndpoint(), (new IssuerFactory(AuthenticationRequestHandler.SP_NAME)).produce());
        for (AttributeType attribute: this.attributes) {
            factory.addFactory(new AttributeFactory(attribute.getName()));
        }
        return (factory.produce());        
    }
}
