package puma.idp.util.security;

import org.opensaml.xml.signature.Signature;

public class IssuerValidator {
	private String identifier;
	private Signature signature;
	
	public IssuerValidator(String issuerIdentifier, Signature issuerSignature) {
		this.identifier = issuerIdentifier;
		this.signature = issuerSignature;
	}
	
	public Boolean isValid() {
		return true;	// LATER Add issuer control (check if trusted, check signature)
	}

	public String getIdentifier() {
		return identifier;
	}

	public Signature getSignature() {
		return signature;
	}
}
