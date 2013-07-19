package puma.proxy.support.saml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSAny;

public abstract class CustomExtensionsMarshaller {
	protected Map<String, List<String>> mapping;
	public CustomExtensionsMarshaller(Extensions messageExtensions) {
		this.mapping = new HashMap<String, List<String>>();
		for (XMLObject object: messageExtensions.getUnknownXMLObjects()) { // based on: ((XSAny) ((Attribute) ((AttributeStatement) sentQuery.getExtensions().getUnknownXMLObjects().get(0)).getAttributes().get(0)).getAttributeValues().get(0)).getTextContent().toString();
			if (object instanceof AttributeStatement) {	// This is currently the only supported Extension type, so it will be the only one handled here
				AttributeStatement stmt = (AttributeStatement) object;
				for (Attribute attribute: stmt.getAttributes()) {
					this.mapping.put(attribute.getFriendlyName(), new ArrayList<String>()); 
					for (XMLObject objectValue: attribute.getAttributeValues()) {
						if (objectValue instanceof AttributeValue || objectValue instanceof XSAny) { // This is currently the only supported child of the Attribute type, so it will be the only one handled here
							XSAny value = (XSAny) objectValue;
							this.mapping.get(attribute.getFriendlyName()).add(value.getTextContent().toString());
						}
					}
				}
			}
		}
	}
	
	public abstract List<String> get();
}
