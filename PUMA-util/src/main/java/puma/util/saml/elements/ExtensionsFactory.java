/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.saml.elements;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.saml2.common.Extensions;

import puma.util.saml.CompoundFactory;
import puma.util.saml.ObjectFactory;
import puma.util.saml.SAMLElementFactory;
import puma.util.saml.SAMLHelper;
/**
 *
 * @author jasper
 */
public class ExtensionsFactory implements ObjectFactory<Extensions>, CompoundFactory<SAMLObject> {
    private List<SAMLElementFactory<? extends SAMLObject>> extensionElements;
    
    public ExtensionsFactory() {
        this.extensionElements = new ArrayList<SAMLElementFactory<? extends SAMLObject>>();
        SAMLHelper.initialize();
    }
    
    @Override
    public Extensions produce() {
        Extensions extensions = SAMLHelper.createElement(Extensions.class, new QName("urn:oasis:names:tc:SAML:2.0:protocol", Extensions.LOCAL_NAME, "saml2p"));
        for (SAMLElementFactory<? extends SAMLObject> element: this.extensionElements) {
            extensions.getUnknownXMLObjects().add(element.produce());
        }
        return extensions;
    }
    
    @Override
    public void addFactory(SAMLElementFactory<? extends SAMLObject> element) {
        this.extensionElements.add(element);
    }
    
}
