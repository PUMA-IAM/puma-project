package puma.proxy.support.proxy;

import javax.servlet.http.HttpServletRequest;
import org.opensaml.saml2.core.RequestAbstractType;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.validation.ValidationException;
import puma.util.exceptions.saml.ElementProcessingException;
import puma.util.exceptions.saml.ServiceParameterException;

public abstract class RequestProxy<K extends RequestAbstractType> extends ProxyHandler<K> {    
    public K reencode(HttpServletRequest request, String destination) throws ConfigurationException, MessageDecodingException, ValidationException, org.opensaml.xml.security.SecurityException {
        K rQ = this.decode(request);
        return this.setUp(rQ, destination);
    }
    
    public abstract K reencode(String message, String destination) throws ServiceParameterException, ElementProcessingException;
    
    //protected abstract <T extends RequestAbstractType> T setUp(T request, String destination);
    protected abstract K setUp(K request, String destination);
}

