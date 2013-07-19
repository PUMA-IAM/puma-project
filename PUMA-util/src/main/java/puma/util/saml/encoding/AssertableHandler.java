/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.saml.encoding;

import javax.servlet.http.HttpServletResponse;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.BasicSAMLMessageContext;
import org.opensaml.saml2.binding.encoding.HTTPRedirectDeflateEncoder;
import org.opensaml.saml2.core.RequestAbstractType;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.saml2.metadata.impl.SingleSignOnServiceBuilder;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.ws.transport.http.HttpServletResponseAdapter;
import puma.util.SecureIdentifierGenerator;

/**
 *
 * @author jasper
 */
public class AssertableHandler {
    private String assertionId;
    
    public AssertableHandler() {
        this.assertionId = SecureIdentifierGenerator.generate();
    }
    
    public final String getAssertionId() {
        return this.assertionId;
    }
    
    protected void prepareResponse(HttpServletResponse response, RequestAbstractType unencodedSAMLRequest, String redirectLocation, String relayState) throws MessageEncodingException {
        // @see: http://mylifewithjava.blogspot.be/2011/01/redirect-with-authnrequest.html
        HttpServletResponseAdapter adapter = new HttpServletResponseAdapter(response, true);  
        BasicSAMLMessageContext<SAMLObject, SAMLObject, SAMLObject> context = new BasicSAMLMessageContext<SAMLObject, SAMLObject, SAMLObject>(); 
        HTTPRedirectDeflateEncoder encoder = new HTTPRedirectDeflateEncoder();  
        Endpoint endpoint = new SingleSignOnServiceBuilder().buildObject(); 
        endpoint.setLocation(redirectLocation);
        context.setPeerEntityEndpoint(endpoint);
        context.setOutboundSAMLMessage(unencodedSAMLRequest);  
        // LATER context.setOutboundSAMLMessageSigningCredential(getSigningCredential());  
        context.setOutboundMessageTransport(adapter);
        context.setRelayState(relayState); // SAML 2.0 RelayState, i.e. string data it needs to use after the SAML2 protocol flow is complete (e.g. the original URL the user was trying to access). 
        encoder.encode(context);
    }
}
