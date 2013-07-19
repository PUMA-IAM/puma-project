/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.saml.elements;

import org.opensaml.saml2.core.NameIDPolicy;
import org.opensaml.saml2.core.NameIDType;
import puma.util.saml.ObjectFactory;
import puma.util.saml.SAMLHelper;

/**
 *
 * @author jasper
 */
public class NameIDPolicyFactory implements ObjectFactory<NameIDPolicy> {
    private static final String DEFAULT_FORMAT = NameIDType.PERSISTENT;
    private String format;
    
    public NameIDPolicyFactory() {
        this.format = NameIDPolicyFactory.DEFAULT_FORMAT;
    }
    
    public NameIDPolicyFactory(String preferredFormat) {
        this.format = preferredFormat;
    }

    @Override
    public NameIDPolicy produce() {
        NameIDPolicy policy = SAMLHelper.createElement(NameIDPolicy.class);
        policy.setAllowCreate(true);
        policy.setFormat(this.format);
        return policy;
    }
    
}
