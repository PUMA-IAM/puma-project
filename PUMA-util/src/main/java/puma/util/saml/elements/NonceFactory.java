package puma.util.saml.elements;

import org.opensaml.common.SAMLObject;

import puma.util.saml.ObjectFactory;

public class NonceFactory<T extends SAMLObject> implements ObjectFactory<T> {
	private T contents;
	
	public NonceFactory(T contents) {
		this.contents = contents;
	}
	
	@Override
	public T produce() {
		return this.contents;
	}

}
