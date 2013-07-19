package puma.util.saml.elements;

import java.util.ArrayList;
import java.util.List;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import puma.util.saml.ObjectFactory;
import puma.util.saml.RetrievableCompoundFactory;
import puma.util.saml.SAMLElementFactory;
import puma.util.saml.SAMLHelper;

public class AttributeStatementFactory implements RetrievableCompoundFactory<Attribute>, ObjectFactory<AttributeStatement> {
	private List<SAMLElementFactory<? extends Attribute>> attributeFactories;
	
	public AttributeStatementFactory() {
		SAMLHelper.initialize();
		this.attributeFactories = new ArrayList<SAMLElementFactory<? extends Attribute>>();
	}
	
	@Override
	public AttributeStatement produce() {
		AttributeStatement result = SAMLHelper.createElement(AttributeStatement.class);
		for (SAMLElementFactory<? extends Attribute> factory: this.attributeFactories) {
			result.getAttributes().add(factory.produce());
		}
		return result;
	}

	@Override
	public void addFactory(SAMLElementFactory<? extends Attribute> element) {
		this.attributeFactories.add(element);		
	}

	@Override
	public List<SAMLElementFactory<? extends Attribute>> retrieveFactories() {
		return this.attributeFactories;
	}

}
