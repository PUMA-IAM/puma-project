package puma.util.saml;

import org.opensaml.xml.XMLObject;

public interface CompoundFactory<T extends XMLObject> {
	public void addFactory(SAMLElementFactory<? extends T> element);
}
