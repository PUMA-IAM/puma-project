package puma.proxy.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.BasicSAMLMessageContext;
import org.opensaml.saml2.binding.decoding.HTTPRedirectDeflateDecoder;
import org.opensaml.saml2.binding.encoding.HTTPRedirectDeflateEncoder;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.saml2.metadata.impl.SingleSignOnServiceBuilder;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.ws.transport.http.HttpServletRequestAdapter;
import org.opensaml.ws.transport.http.HttpServletResponseAdapter;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.validation.ValidatingXMLObject;
import org.opensaml.xml.validation.ValidationException;

public class Redirecter {
	public static void sendRequestRedirect(HttpServletResponse response, AuthnRequest message, String redirection, String relayState) throws MessageEncodingException {
        	HttpServletResponseAdapter adapter = new HttpServletResponseAdapter(response, true);  
            BasicSAMLMessageContext<AuthnRequest, AuthnRequest, SAMLObject> context = new BasicSAMLMessageContext<AuthnRequest, AuthnRequest, SAMLObject>(); 
            HTTPRedirectDeflateEncoder encoder = new HTTPRedirectDeflateEncoder();  
            Endpoint endpoint = new SingleSignOnServiceBuilder().buildObject(); 
            endpoint.setLocation(redirection);
            context.setPeerEntityEndpoint(endpoint);
            context.setOutboundSAMLMessage(message);  
            // LATER context.setOutboundSAMLMessageSigningCredential(getSigningCredential());  
            context.setOutboundMessageTransport(adapter);
            if (relayState != null && !relayState.isEmpty())
                context.setRelayState(relayState); // SAML 2.0 RelayState, i.e. string data it needs to use after the SAML2 protocol flow is complete (e.g. the original URL the user was trying to access). 
            encoder.encode(context);     
    }
	
	public static void sendResponseRedirect(HttpServletResponse response, Response message, String redirection, String relayState) throws MessageEncodingException {
        HttpServletResponseAdapter adapter = new HttpServletResponseAdapter(response, true);  
            BasicSAMLMessageContext<SAMLObject, Response, SAMLObject> context = new BasicSAMLMessageContext<SAMLObject, Response, SAMLObject>(); 
            HTTPRedirectDeflateEncoder encoder = new HTTPRedirectDeflateEncoder();  
            Endpoint endpoint = new SingleSignOnServiceBuilder().buildObject(); 
            endpoint.setLocation(redirection);
            context.setPeerEntityEndpoint(endpoint);
            context.setOutboundSAMLMessage(message);  
            // LATER context.setOutboundSAMLMessageSigningCredential(getSigningCredential());  
            context.setOutboundMessageTransport(adapter);
            if (relayState != null && !relayState.isEmpty())
                context.setRelayState(relayState); // SAML 2.0 RelayState, i.e. string data it needs to use after the SAML2 protocol flow is complete (e.g. the original URL the user was trying to access). 
            encoder.encode(context);     
    }
    
    // @see also http://stackoverflow.com/questions/745756/java-generics-wildcarding-with-multiple-classes
    public static <T extends ValidatingXMLObject & SAMLObject> T decode(HttpServletRequest request, Class<? extends T> theClass) throws ConfigurationException, MessageDecodingException, SecurityException, ValidationException {
        DefaultBootstrap.bootstrap();
        BasicSAMLMessageContext<Response, ? extends SAMLObject, ? extends SAMLObject> messageContext = new BasicSAMLMessageContext<Response, SAMLObject, SAMLObject>();
        messageContext.setInboundMessageTransport(new HttpServletRequestAdapter(request));
        HTTPRedirectDeflateDecoder decoder = new HTTPRedirectDeflateDecoder();
        decoder.decode(messageContext);
        T authRq = theClass.cast(messageContext.getInboundSAMLMessage()); // DEBUG might not work properly
        authRq.validate(true);
        return authRq;
    }
}
