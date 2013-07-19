package puma.util.saml.elements;

import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnContextDecl;
import org.opensaml.saml2.core.AuthnContextDeclRef;

import puma.util.saml.ObjectFactory;
import puma.util.saml.SAMLHelper;

public class AuthnContextFactory implements ObjectFactory<AuthnContext> {
	private final static String DEFAULT_CONTEXT_CLASS_REF = org.opensaml.saml2.core.AuthnContext.PASSWORD_AUTHN_CTX;
	
	private String classRef;
	private String declRef;
	private String decl;
	
	public AuthnContextFactory() {
		this(DEFAULT_CONTEXT_CLASS_REF);
	}
	
	public AuthnContextFactory(String contextClassRef) {
		this(contextClassRef, null);
	}
	
	public AuthnContextFactory(String contextClassRef, String contextDeclRef) {
		this(contextClassRef, contextDeclRef, null);
	}
	
	public AuthnContextFactory(String contextClassRef, String contextDeclRef, String contextDecl) {
		this.classRef = contextClassRef;
		this.declRef = contextDeclRef;
		this.decl = contextDecl;
	}
	
	@Override
	public AuthnContext produce() {
		AuthnContext result = SAMLHelper.createElement(AuthnContext.class);
		AuthnContextClassRef classRef = SAMLHelper.createElement(AuthnContextClassRef.class);
		classRef.setAuthnContextClassRef(this.classRef);
		result.setAuthnContextClassRef(classRef);		
		if (this.declRef != null) {
			AuthnContextDeclRef declRef = SAMLHelper.createElement(AuthnContextDeclRef.class);
			result.setAuthnContextDeclRef(declRef);
			if (this.decl != null) {
				AuthnContextDecl decl = SAMLHelper.createElement(AuthnContextDecl.class);
				result.setAuthnContextDecl(decl);
			}
		}
		return result;
	}

}
