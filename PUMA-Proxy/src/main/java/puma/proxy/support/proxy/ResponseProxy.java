package puma.proxy.support.proxy;

import javax.servlet.http.HttpServletRequest;
import puma.proxy.model.SAMLMessageHeaderContext;
import org.opensaml.saml2.core.Response;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.validation.ValidationException;
import puma.util.SecureIdentifierGenerator;
import puma.util.exceptions.saml.ElementProcessingException;
import puma.util.exceptions.saml.ServiceParameterException;
import puma.util.saml.SAMLHelper;
import puma.util.saml.cloners.ResponseCloner;
import puma.util.saml.elements.IssuerFactory;
import puma.proxy.support.Redirecter;

public class ResponseProxy extends ProxyHandler<Response> {
	public Response reencode(HttpServletRequest request) throws ConfigurationException, MessageDecodingException, ValidationException, org.opensaml.xml.security.SecurityException {
        Response authResp = Redirecter.decode(request, Response.class);
        return this.setUp(authResp);
    }
    
    public Response reencode(String message) throws ServiceParameterException, ElementProcessingException {
        Response authResp = SAMLHelper.processString(message, Response.class);
        return this.setUp(authResp);
    }
    
    private Response setUp(Response response) {
        SAMLMessageHeaderContext data = this.bean.get(response.getInResponseTo());
        return (new ResponseCloner(SecureIdentifierGenerator.generate(), data.getOriginalSAMLIdentifier(), data.getAssertionConsumerServiceURL(), (new IssuerFactory(SP_ISSUERNAME)).produce())).cloneElement(response);
    }
}
