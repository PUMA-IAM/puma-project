/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.saml.elements;

import java.util.ArrayList;
import java.util.List;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.xml.schema.XSString;
import puma.util.saml.CompoundFactory;
import puma.util.saml.ObjectFactory;
import puma.util.saml.SAMLElementFactory;
import puma.util.saml.SAMLHelper;

/**
 *
 * @author jasper
 */
public class AttributeFactory implements ObjectFactory<Attribute>, CompoundFactory<XSString> {
    public final static String DEFAULT_FORMAT = Attribute.UNSPECIFIED;
    private String attributeName;
    private String attributeFriendlyName;
    private String format;
    private List<SAMLElementFactory<? extends XSString>> factories;
    
    public AttributeFactory(String attributeFriendlyName) {
        this(attributeFriendlyName, attributeFriendlyName, AttributeFactory.DEFAULT_FORMAT);
    }
    
    public AttributeFactory(String attributeName, String attributeFriendlyName, String format) {
        SAMLHelper.initialize();
        this.attributeFriendlyName = attributeFriendlyName;
        this.attributeName = attributeName;
        this.format = format;
        this.factories = new ArrayList<SAMLElementFactory<? extends XSString>>();
    }
    
    @Override
    public Attribute produce() {
        Attribute attribute = SAMLHelper.createElement(Attribute.class);
        attribute.setNameFormat(this.format);
        attribute.setName(this.attributeName);
        attribute.setFriendlyName(this.attributeFriendlyName);
        for (SAMLElementFactory<? extends XSString> factory: this.factories) {
        	attribute.getAttributeValues().add(factory.produce());
        }
        return attribute;
    }

    /**
     * VAR element the factory for an AttributeValue
     */
	@Override
	public void addFactory(SAMLElementFactory<? extends XSString> element) {
		this.factories.add(element);		
	}
    
}
