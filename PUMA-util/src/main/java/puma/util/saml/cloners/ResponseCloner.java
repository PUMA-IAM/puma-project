package puma.util.saml.cloners;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Condition;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.Subject;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSString;
import puma.util.saml.SAMLHelper;
import puma.util.saml.elements.AssertionFactory;
import puma.util.saml.elements.AttributeFactory;
import puma.util.saml.elements.AttributeStatementFactory;
import puma.util.saml.elements.AttributeValueFactory;
import puma.util.saml.elements.AuthnContextFactory;
import puma.util.saml.elements.AuthnStatementFactory;
import puma.util.saml.elements.ConditionFactory;
import puma.util.saml.elements.IssuerFactory;
import puma.util.saml.elements.StatusFactory;
import puma.util.saml.elements.SubjectFactory;
import puma.util.saml.messages.ResponseFactory;

public class ResponseCloner implements SAMLObjectCloner<Response> {
	private String assertionIdentifier;
	private String inResponseTo;
	private String destination;
	private Issuer issuer;
	
	public ResponseCloner(String assertionId, String inResponseTo, String destination, Issuer issuer) {
		SAMLHelper.initialize();
		this.assertionIdentifier = assertionId;
		this.inResponseTo = inResponseTo;
		this.destination = destination;
		this.issuer = issuer;
	}
	
	@Override
	public Response cloneElement(Response item) {
		Response result = (new ResponseFactory(this.assertionIdentifier, this.inResponseTo, this.destination, this.issuer, (new StatusFactory(item.getStatus().getStatusCode().getValue())).produce())).produce();
        result.setVersion(item.getVersion());
        result.setIssueInstant(item.getIssueInstant());
        
        for (Assertion assertion: item.getAssertions()) {
        	// LATER also clone other elements e.g. Advice (assertion.getAdvice() given here), ...
            AssertionFactory respAssFactory = (new AssertionFactory(assertion.getID(), (new IssuerFactory(assertion.getIssuer().getValue(), assertion.getIssuer().getFormat())).produce(), assertion.getIssueInstant(), (new SubjectFactory(assertion.getSubject().getNameID().getValue(), assertion.getSubject().getNameID().getFormat()).produce()), assertion.getAdvice()));
            respAssFactory.setConditionProperties(assertion.getConditions().getNotBefore(), assertion.getConditions().getNotOnOrAfter());
            for (@SuppressWarnings("unused") Condition assCond: assertion.getConditions().getConditions())
                respAssFactory.addFactory(new ConditionFactory());
            Assertion respAss = respAssFactory.produce();
            respAss.setID(assertion.getID());
            respAss.setIssueInstant(assertion.getIssueInstant());
            respAss.setIssuer((new IssuerFactory(assertion.getIssuer().getValue(), assertion.getIssuer().getFormat())).produce());
            Subject respAssSubject = (new SubjectFactory(assertion.getSubject().getNameID().getValue(), assertion.getSubject().getNameID().getFormat())).produce();
            //respAssSubject.setBaseID(assertion.getSubject().getBaseID());
            //respAssSubject.setEncryptedID(assertion.getSubject().getEncryptedID());
            respAss.setSubject(respAssSubject);            
            respAss.setVersion(assertion.getVersion());
            // AuthnStatement
            for (AuthnStatement stmt: assertion.getAuthnStatements()) {
            	respAss.getAuthnStatements().add((new AuthnStatementFactory(stmt.getAuthnInstant(), (new AuthnContextFactory(stmt.getAuthnContext().getAuthnContextClassRef().getAuthnContextClassRef())).produce())).produce());
            }
            // AttributeStatement
            for (AttributeStatement stmt: assertion.getAttributeStatements()) {
            	AttributeStatementFactory stmtFactory = new AttributeStatementFactory();
            	for (Attribute attribute: stmt.getAttributes()) {
            		AttributeFactory attrFactory = new AttributeFactory(attribute.getName(), attribute.getFriendlyName(), attribute.getNameFormat());
            		for (XMLObject value: attribute.getAttributeValues()) {
            			if (value instanceof XSString) {		// LATER Only of type XSString supported for now
            				XSString attributeValue = (XSString) value;
            				AttributeValueFactory valueFactory = new AttributeValueFactory(attributeValue.getValue());
            				attrFactory.addFactory(valueFactory);
            			}
            		}
            		stmtFactory.addFactory(attrFactory);
            	}
            	respAss.getAttributeStatements().add(stmtFactory.produce());
            }            
            result.getAssertions().add(respAss);
        }
		return result;
	}

}
