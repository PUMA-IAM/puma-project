package puma.proxy.support.proxy;

import javax.servlet.http.HttpServletRequest;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.BasicSAMLMessageContext;
import org.opensaml.saml2.binding.decoding.HTTPRedirectDeflateDecoder;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.transport.http.HttpServletRequestAdapter;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.validation.ValidationException;
import puma.proxy.controllers.HeaderContextController;
import puma.util.saml.SAMLHelper;

public abstract class ProxyHandler<E extends SAMLObject> {
	protected final static String SP_ENDPOINT = "http://localhost:8080/PUMA-Proxy/SAMLRespEndpoint"; // LATER Read from properties file
    protected final static String SP_ISSUERNAME = "Proxy";
    protected HeaderContextController bean;
    
    public ProxyHandler() {
        this.bean = new HeaderContextController();
    }

    @SuppressWarnings("unchecked")
	public <T extends E> T decode(HttpServletRequest request) throws ConfigurationException, MessageDecodingException, ValidationException, org.opensaml.xml.security.SecurityException {
        SAMLHelper.initialize();
        BasicSAMLMessageContext<SAMLObject, ? extends SAMLObject, ? extends SAMLObject> messageContext = new BasicSAMLMessageContext<SAMLObject, SAMLObject, SAMLObject>();
        messageContext.setInboundMessageTransport(new HttpServletRequestAdapter(request));
        HTTPRedirectDeflateDecoder decoder = new HTTPRedirectDeflateDecoder();
        decoder.decode(messageContext);
        T rQ = (T) messageContext.getInboundSAMLMessage();
        rQ.validate(true);
        return rQ;
    }
}
