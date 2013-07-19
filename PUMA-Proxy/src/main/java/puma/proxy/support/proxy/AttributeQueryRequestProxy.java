package puma.proxy.support.proxy;

import org.opensaml.saml2.core.AttributeQuery;
import puma.util.SecureIdentifierGenerator;
import puma.util.exceptions.saml.ElementProcessingException;
import puma.util.exceptions.saml.ServiceParameterException;
import puma.util.saml.SAMLHelper;
import puma.util.saml.cloners.AttributeQueryCloner;
import puma.util.saml.elements.IssuerFactory;

public class AttributeQueryRequestProxy extends RequestProxy<AttributeQuery> {

	@Override
	public AttributeQuery reencode(String message, String destination) throws ServiceParameterException, ElementProcessingException {
        AttributeQuery query = SAMLHelper.processString(message, AttributeQuery.class);
        return this.setUp(query, destination);
	}

	@Override
	protected AttributeQuery setUp(AttributeQuery req, String destination) {
		AttributeQuery request = (AttributeQuery) req;
        String id = SecureIdentifierGenerator.generate();
        this.bean.add(request.getID(), id, "http://localhost:8080/PUMA-war/AttributeResponseServlet");// DEBUG req.getAssertionConsumerServiceURL());
        return (new AttributeQueryCloner(id, destination, (new IssuerFactory(SP_ISSUERNAME)).produce())).cloneElement(request);
	}


}
