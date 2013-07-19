/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.saml.elements;

import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.xml.schema.XSAny;
import puma.util.saml.ObjectFactory;
import puma.util.saml.SAMLHelper;

/**
 * @author jasper
 */
public class CustomProxyExtensionFactory implements ObjectFactory<AttributeStatement> {
    private static final String ELEMENT_NAME = "Proxy";
    private String content;
    
    public CustomProxyExtensionFactory(String proxyPath) {
        this.content = proxyPath;
    }
    
    @Override
    public AttributeStatement produce() {
        AttributeStatement stmt = SAMLHelper.createElement(AttributeStatement.class);
        Attribute attr = SAMLHelper.createElement(Attribute.class);
        XSAny val = SAMLHelper.createElement(XSAny.class, AttributeValue.DEFAULT_ELEMENT_NAME, XSAny.TYPE_NAME);
        val.setTextContent(this.content);
        attr.setFriendlyName(CustomProxyExtensionFactory.ELEMENT_NAME);
        attr.getAttributeValues().add(val);
        stmt.getAttributes().add(attr);
        return stmt;
    }
    
}
