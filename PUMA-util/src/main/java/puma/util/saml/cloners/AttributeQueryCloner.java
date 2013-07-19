package puma.util.saml.cloners;

import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeQuery;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSAny;
import puma.util.saml.elements.AttributeFactory;
import puma.util.saml.elements.AttributeStatementFactory;
import puma.util.saml.elements.AttributeValueFactory;
import puma.util.saml.elements.ExtensionsFactory;
import puma.util.saml.elements.SubjectFactory;
import puma.util.saml.messages.AttributeQueryFactory;

public class AttributeQueryCloner implements SAMLObjectCloner<AttributeQuery> {
	private String assertionIdentifier;
	private String destination;
	private Issuer issuer;
	
	public AttributeQueryCloner(String assertionId, String destination, Issuer issuer) {
		this.assertionIdentifier = assertionId;
		this.destination = destination;
		this.issuer = issuer;
	}
	
	@Override
	public AttributeQuery cloneElement(AttributeQuery item) {
		AttributeQuery result = (new AttributeQueryFactory(this.assertionIdentifier, (new SubjectFactory(item.getSubject().getNameID().getValue())).produce(), this.destination, this.issuer)).produce();
		result.setIssueInstant(item.getIssueInstant());
        result.setVersion(item.getVersion());
        
        // Attributes
        for (Attribute itemAttribute: item.getAttributes()) {
            AttributeFactory attributeFactory = new AttributeFactory(itemAttribute.getName(), itemAttribute.getFriendlyName(), itemAttribute.getNameFormat());
            Attribute resultAttribute = attributeFactory.produce();
            resultAttribute.setName(itemAttribute.getName());
            resultAttribute.setNameFormat(itemAttribute.getNameFormat());
            result.getAttributes().add(resultAttribute);
        }
        
        // LATER Extensions - Custom extensions copier (should be refractored)
        if (item.getExtensions() != null && item.getExtensions().hasChildren() && !item.getExtensions().isNil()) {
        	ExtensionsFactory factory = new ExtensionsFactory();
        	for (XMLObject object: item.getExtensions().getUnknownXMLObjects()) {
        		if (object instanceof AttributeStatement) {
	        		AttributeStatement itemStatement = (AttributeStatement) object;
	        		AttributeStatementFactory stmtFactory = (new AttributeStatementFactory());
	                for (Attribute reqAttr: itemStatement.getAttributes()) {
	                    AttributeFactory attrFactory = new AttributeFactory(reqAttr.getName(), reqAttr.getFriendlyName(), reqAttr.getNameFormat());
	                    for (XMLObject obj2: reqAttr.getAttributeValues()) {
	                        XSAny reqVal = (XSAny) obj2;
	                        AttributeValueFactory valueFactory = new AttributeValueFactory(reqVal.getTextContent());
	                        attrFactory.addFactory(valueFactory);
	                    }
	                    stmtFactory.addFactory(attrFactory);
	                }
	                factory.addFactory(stmtFactory);
        		}
        		else 
        			;
        	}
        	result.setExtensions(factory.produce());
        }
        return result;
	}
	
}
