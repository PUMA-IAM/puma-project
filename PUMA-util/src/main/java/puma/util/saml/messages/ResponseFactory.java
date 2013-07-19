package puma.util.saml.messages;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.Status;

import puma.util.saml.CompoundFactory;
import puma.util.saml.ObjectFactory;
import puma.util.saml.SAMLElementFactory;
import puma.util.saml.SAMLHelper;
import puma.util.saml.elements.StatusFactory;

public class ResponseFactory implements ObjectFactory<Response>, CompoundFactory<Assertion> {
	private static final SAMLVersion DEFAULT_VERSION = SAMLVersion.VERSION_20;
	private static final Status DEFAULT_STATUS = (new StatusFactory()).produce();
	
	private String assertionIdentifier;
	private String inResponseTo;
	private String destination;
	private Issuer issuer;
	private Status status;
	private List<SAMLElementFactory<? extends Assertion>> assertions;
	
	public ResponseFactory(String assertionId, String inResponseTo, String destination, Issuer issuer) {
		this(assertionId, inResponseTo, destination, issuer, DEFAULT_STATUS);
	}
	
	public ResponseFactory(String assertionId, String inResponseTo, Issuer issuer, Status status) {
		this(assertionId, inResponseTo, null, issuer, status);
	}
	
	public ResponseFactory(String assertionId, String inResponseTo, String destination, Issuer issuer, Status status) {
		SAMLHelper.initialize();
		this.assertionIdentifier = assertionId;
		this.inResponseTo = inResponseTo;
		this.destination = destination;
		this.issuer = issuer;
		this.status = status;
		this.assertions = new ArrayList<SAMLElementFactory<? extends Assertion>>();		
	}
	
	
	@Override
	public Response produce() {
		Response result = SAMLHelper.createElement(Response.class);
		result.setID(this.assertionIdentifier);
		result.setInResponseTo(this.inResponseTo);
		if (this.destination != null)
			result.setDestination(this.destination);
		result.setIssuer(this.issuer);
		result.setIssueInstant(new DateTime());
		result.setStatus(this.status);
		for (SAMLElementFactory<? extends Assertion> factory: this.assertions)
			result.getAssertions().add(factory.produce());
		result.setVersion(ResponseFactory.DEFAULT_VERSION);
		return result;
	}

	@Override
	public void addFactory(SAMLElementFactory<? extends Assertion> element) {
		this.assertions.add(element);
	}
}
