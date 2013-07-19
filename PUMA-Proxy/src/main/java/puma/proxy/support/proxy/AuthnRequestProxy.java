package puma.proxy.support.proxy;

import java.util.logging.Level;
import java.util.logging.Logger;
import puma.util.SecureIdentifierGenerator;
import puma.util.saml.cloners.AuthnRequestCloner;
import org.opensaml.saml2.core.AuthnRequest;
import puma.util.exceptions.saml.ElementProcessingException;
import puma.util.exceptions.saml.ServiceParameterException;

public class AuthnRequestProxy extends RequestProxy<AuthnRequest> {
	private static final Logger logger = Logger.getLogger(AuthnRequestProxy.class.getName());

	@Override
	public AuthnRequest reencode(String message, String destination)
			throws ServiceParameterException, ElementProcessingException {
		throw new UnsupportedOperationException("Could not reencode the AuthnRequest: not supported!");
	}

	@Override
	protected AuthnRequest setUp(AuthnRequest req, String destination) {
		AuthnRequestCloner cloner;
		AuthnRequest request = (AuthnRequest) req;  
        String id = SecureIdentifierGenerator.generate();
        logger.log(Level.INFO, "Saving " + request.getID() + "... (to " + request.getAssertionConsumerServiceURL() + ")");
        this.bean.add(request.getID(), id, request.getAssertionConsumerServiceURL());
        cloner = new AuthnRequestCloner(id, destination, request.getProviderName(), SP_ENDPOINT);
        return cloner.cloneElement(request);
	}
}
