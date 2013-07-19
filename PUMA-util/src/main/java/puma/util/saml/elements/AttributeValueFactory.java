package puma.util.saml.elements;

import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.schema.XSString;
import puma.util.saml.SAMLElementFactory;
import puma.util.saml.SAMLHelper;

public class AttributeValueFactory implements SAMLElementFactory<XSString> {
	private final static String TYPE_IDENTIFIER = "xsi:type";
	private final static String DEFAULT_TYPE = "xs:string";
	
	private String attributeValue;
	private String attributeType;
	
	public AttributeValueFactory(String value) {
		this(value, DEFAULT_TYPE);
	}
	
	public AttributeValueFactory(String value, String type) {
		SAMLHelper.initialize();
		this.attributeType = type;
		this.attributeValue = value;
	}

	@Override
	public XSString produce() {
		XSString result = (XSString) Configuration.getBuilderFactory().getBuilder(XSString.TYPE_NAME).buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
//		XSAny result = (XSAny) Configuration.getBuilderFactory().getBuilder(XSAny.TYPE_NAME).buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
//		XSAny result = SAMLHelper.createElement(XSAny.class, AttributeValue.DEFAULT_ELEMENT_NAME, XSAny.TYPE_NAME);
//		result.getUnknownAttributes().put(new QName(TYPE_IDENTIFIER), this.attributeType);
//		result.setTextContent(this.attributeValue);		
		if (this.attributeType == null || TYPE_IDENTIFIER == null); // DEBUG Introduce XSAny as type here - will also require a change in AttributeQueryCloner
		result.setValue(this.attributeValue);
		return result;
	}

}
