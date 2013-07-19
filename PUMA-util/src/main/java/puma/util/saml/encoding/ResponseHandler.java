/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.saml.encoding;

import javax.servlet.http.HttpServletRequest;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.BasicSAMLMessageContext;
import org.opensaml.saml2.binding.decoding.HTTPRedirectDeflateDecoder;
import org.opensaml.saml2.core.Response;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.transport.http.HttpServletRequestAdapter;
import puma.util.exceptions.flow.ResponseProcessingException;

/**
 *
 * @author jasper
 */
public abstract class ResponseHandler {
    protected Response decodeMessage(HttpServletRequest request) throws MessageDecodingException, SecurityException, org.opensaml.xml.security.SecurityException {
        // Decode
        BasicSAMLMessageContext<Response, ? extends SAMLObject, ? extends SAMLObject> messageContext = new BasicSAMLMessageContext<Response, SAMLObject, SAMLObject>();
        messageContext.setInboundMessageTransport(new HttpServletRequestAdapter(request));
        HTTPRedirectDeflateDecoder decoder = new HTTPRedirectDeflateDecoder();
        decoder.decode(messageContext);
        // Fetch attributes
        return messageContext.getInboundSAMLMessage();
    }
    
    public abstract Object interpret(HttpServletRequest request) throws MessageDecodingException, org.opensaml.xml.security.SecurityException, ResponseProcessingException;
}
