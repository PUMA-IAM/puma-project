package puma.idp.util.saml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeQuery;
import org.opensaml.saml2.core.Response;
import puma.idp.controllers.AttributeController;
import puma.idp.controllers.UserController;
import puma.idp.model.User;
import puma.util.SecureIdentifierGenerator;
import puma.util.exceptions.ElementNotFoundException;
import puma.util.exceptions.NoSubjectSpecifiedException;
import puma.util.saml.elements.AssertionFactory;
import puma.util.saml.elements.AttributeFactory;
import puma.util.saml.elements.AttributeStatementFactory;
import puma.util.saml.elements.AttributeValueFactory;
import puma.util.saml.elements.IssuerFactory;
import puma.util.saml.elements.StatusFactory;
import puma.util.saml.elements.SubjectFactory;
import puma.util.saml.messages.ResponseFactory;

public class QueryProcessor {
	private static final Logger logger = Logger.getLogger(QueryProcessor.class.getName());
	private static final int DEFAULT_INVALIDATION_HOURS = 12;
	private List<String> requestedAttributes;
	private String responseId;
	private String userIdentifier;
	
	public QueryProcessor(AttributeQuery query) throws NoSubjectSpecifiedException {
		if (query.getSubject() == null || query.getSubject().isNil() || query.getSubject().getNameID() == null || query.getSubject().getNameID().isNil() || query.getSubject().getNameID().getValue() == null || query.getSubject().getNameID().getValue().isEmpty())
			throw new NoSubjectSpecifiedException();
		this.requestedAttributes = extractAttributes(query);
		this.responseId = query.getID();
		this.userIdentifier = query.getSubject().getNameID().getValue();
	}
	
	public List<String> getRequestedAttributes() {
		return this.requestedAttributes;
	}
	
	public Response process() throws ElementNotFoundException {
		ResponseFactory resultFactory = (new ResponseFactory(SecureIdentifierGenerator.generate(), this.responseId, (new IssuerFactory(SAMLHTTPMessageProcessor.ISSUER_NAME)).produce(), (new StatusFactory()).produce()));
		Map<String, List<String>> attributes = this.fetchAttributeValues();
		DateTime now = new DateTime();
		AssertionFactory assertionFactory = new AssertionFactory(SecureIdentifierGenerator.generate(), (new IssuerFactory(SAMLHTTPMessageProcessor.ISSUER_NAME)).produce(), now, (new SubjectFactory(this.userIdentifier)).produce());
		AttributeStatementFactory stmtFactory = new AttributeStatementFactory();
		logger.log(Level.INFO, "Adding attributes to statement");
		for (String nextAttribute: attributes.keySet()) {
			AttributeFactory attributeFactory = new AttributeFactory(nextAttribute);
			for (String nextValue: attributes.get(nextAttribute)) {
				logger.log(Level.INFO, "Adding value for attribute " + nextAttribute + " (" + nextValue + ")");
				attributeFactory.addFactory(new AttributeValueFactory(nextValue));
			}
			stmtFactory.addFactory(attributeFactory);
		}
		assertionFactory.setConditionProperties(now, now.plusHours(DEFAULT_INVALIDATION_HOURS));
		assertionFactory.addAttributeStatementFactory(stmtFactory);
		resultFactory.addFactory(assertionFactory);
		return resultFactory.produce();
	}
	
	private static List<String> extractAttributes(AttributeQuery request) {
		List<String> result = new ArrayList<String>(request.getAttributes().size());
		for (Attribute attribute: request.getAttributes()) {
			result.add(attribute.getName());
		}
		return result;
	}
	
	private Map<String, List<String>> fetchAttributeValues() throws ElementNotFoundException {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		AttributeController fetcher = new AttributeController();
		UserController userFetcher = new UserController();
		User specifiedUser = userFetcher.getUser(this.userIdentifier);
		for (String next: this.requestedAttributes) {
			List<String> fetchedValues = fetcher.getAttributeValue(specifiedUser, next);
			if (fetchedValues != null) {
				result.put(next, fetchedValues);
			}
		}
		return result;
	}
}
