package puma.util.saml;

import org.opensaml.xml.XMLObject;

public interface SAMLElementFactory<T extends XMLObject> {
    public T produce();
}
