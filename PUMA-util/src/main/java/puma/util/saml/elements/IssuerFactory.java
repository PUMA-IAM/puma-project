/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.saml.elements;

import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameIDType;
import puma.util.saml.ObjectFactory;
import puma.util.saml.SAMLHelper;

/**
 *
 * @author jasper
 */
public class IssuerFactory implements ObjectFactory<Issuer> {
    private static final String PREFERRED_FORMAT = NameIDType.ENTITY;
    private String format;
    private String issuerName;
    
    public IssuerFactory(String issuerName, String format) {
    	SAMLHelper.initialize();
        this.issuerName = issuerName;
        this.format = format;
    	
    }
    
    public IssuerFactory(String issuerName) {
    	this(issuerName, IssuerFactory.PREFERRED_FORMAT);
    }
    
    @Override
    public Issuer produce() {
        Issuer result = SAMLHelper.createElement(Issuer.class);
        result.setValue(this.issuerName);
        result.setFormat(this.format);
        return result;
    }
    
}
