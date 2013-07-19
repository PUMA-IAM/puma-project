package puma.idp.util.saml;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.NameIDType;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.Subject;
import org.opensaml.xml.signature.Signature;
import puma.util.SecureIdentifierGenerator;
import puma.util.exceptions.NotSupportedException;
import puma.util.saml.elements.AssertionFactory;
import puma.util.saml.elements.AuthnContextFactory;
import puma.util.saml.elements.AuthnStatementFactory;
import puma.util.saml.elements.IssuerFactory;
import puma.util.saml.elements.StatusFactory;
import puma.util.saml.elements.SubjectConfirmationFactory;
import puma.util.saml.elements.SubjectFactory;
import puma.util.saml.messages.ResponseFactory;

public class SAMLHTTPMessageProcessor {
	private static final Logger logger = Logger.getLogger(SAMLHTTPMessageProcessor.class.getName());
    public static final String ISSUER_NAME = "PUMA: Identity Provider"; // LATER properties file
    
	private AuthnRequest message;
	public SAMLHTTPMessageProcessor(AuthnRequest message) {
		this.message = message;
	}
	
	@SuppressWarnings("unused")
	public Boolean isValid() {
		try {
			this.message.validate(true);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, null, ex);
			return false;
		}
		return true || (this.message.isSigned() && this.checkSignature(this.message.getIssuer(), this.message.getSignature())); 
	}
	
    private boolean checkSignature(Issuer suer, Signature signature) {
        return true; // LATER Support signatures
    }
    
    public String getIssuerId() {
    	return this.message.getIssuer().getValue();
    }
    
    public Signature getSignature() {
    	return this.message.getSignature();
    }
    
    public Response generateResponse(String userName) throws NotSupportedException {
    	String identifier = SecureIdentifierGenerator.generate();
    	Response response = (new ResponseFactory(identifier, this.message.getID(), this.message.getAssertionConsumerServiceURL(), (new IssuerFactory(ISSUER_NAME)).produce())).produce();
        response.setVersion(SAMLVersion.VERSION_20);
        response.setStatus((new StatusFactory(StatusCode.SUCCESS_URI)).produce());
        response.getAssertions().add(setupAuthnStatementAssertion(userName));
        if (!this.message.getNameIDPolicy().getFormat().equalsIgnoreCase(NameIDType.PERSISTENT))
            throw new NotSupportedException("NameID Policy Format", this.message.getNameIDPolicy().getFormat());
        return response;
    }
    
    public AuthnRequest getMessage() {
    	return this.message;
    }
    
    private Assertion setupAuthnStatementAssertion(String userName) {
    	DateTime now = new DateTime();
    	AssertionFactory factory = new AssertionFactory(SecureIdentifierGenerator.generate(), (new IssuerFactory(ISSUER_NAME)).produce());
    	SubjectFactory subjectFactory = new SubjectFactory(userName, NameID.ENTITY);
    	Subject subject = subjectFactory.produce();
    	Assertion result;
    	// First, set the conditions for assertion
    	factory.setConditionProperties(now, now.plusHours(2));
    	// Next, produce it
    	result = factory.produce();
    	// Also provide a confirmation
    	subject.getSubjectConfirmations().add((new SubjectConfirmationFactory()).produce());
    	result.setSubject(subject);
    	result.getAuthnStatements().add((new AuthnStatementFactory(now, (new AuthnContextFactory(AuthnContext.PASSWORD_AUTHN_CTX)).produce())).produce());
        
        return result;
    }
}
