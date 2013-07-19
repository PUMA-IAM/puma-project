package puma.util.saml.elements;

import org.opensaml.saml2.core.SubjectConfirmation;

import puma.util.saml.ObjectFactory;
import puma.util.saml.SAMLHelper;

public class SubjectConfirmationFactory implements ObjectFactory<SubjectConfirmation> {
	private final static String DEFAULT_METHOD = SubjectConfirmation.METHOD_BEARER;
	
	private String method;
	
	public SubjectConfirmationFactory() {
		this(SubjectConfirmationFactory.DEFAULT_METHOD);
	}
	
	public SubjectConfirmationFactory(String method) {
		this.method = method;
	}
	
	@Override
	public SubjectConfirmation produce() {
		SubjectConfirmation result = SAMLHelper.createElement(SubjectConfirmation.class);
		result.setMethod(this.method);		
		return result;
	}
}
