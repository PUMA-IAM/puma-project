package puma.idp.util.saml;

import javax.servlet.http.HttpServletResponse;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.BasicSAMLMessageContext;
import org.opensaml.saml2.binding.encoding.HTTPRedirectDeflateEncoder;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.saml2.metadata.impl.SingleSignOnServiceBuilder;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.ws.transport.http.HttpServletResponseAdapter;

public class SAMLMessageEncoder {
	public static void encode(HttpServletResponse response, String destination, Response unencodedResponse, String relayState) throws MessageEncodingException {
        HttpServletResponseAdapter adapter = new HttpServletResponseAdapter(response, true);  
        BasicSAMLMessageContext<SAMLObject, SAMLObject, SAMLObject> context = new BasicSAMLMessageContext<SAMLObject, SAMLObject, SAMLObject>(); 
        HTTPRedirectDeflateEncoder encoder = new HTTPRedirectDeflateEncoder();  
        Endpoint endpoint = new SingleSignOnServiceBuilder().buildObject(); 
        endpoint.setLocation(destination);
        context.setPeerEntityEndpoint(endpoint);
        context.setOutboundSAMLMessage(unencodedResponse);  
        // LATER context.setOutboundSAMLMessageSigningCredential(getSigningCredential());  
        context.setOutboundMessageTransport(adapter);
        if (relayState != null && !relayState.isEmpty())
            context.setRelayState(relayState); // SAML 2.0 RelayState, i.e. string data it needs to use after the SAML2 protocol flow is complete (e.g. the original URL the user was trying to access). 
        encoder.encode(context);     
    } 
}
