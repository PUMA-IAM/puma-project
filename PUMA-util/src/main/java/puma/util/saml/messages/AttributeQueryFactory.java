/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.saml.messages;

import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeQuery;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Subject;
import puma.util.saml.CompoundFactory;
import puma.util.saml.ObjectFactory;
import puma.util.saml.SAMLElementFactory;
import puma.util.saml.SAMLHelper;

/**
 *
 * @author jasper
 */
public class AttributeQueryFactory implements ObjectFactory<AttributeQuery>, CompoundFactory<Attribute> {
    private String assertionIdentifier;
    private Subject subject;
    private String destination;
    private Issuer issuer;
    private List<SAMLElementFactory<? extends Attribute>> attributeFactories;
    
    public AttributeQueryFactory(String assertionId, Subject subject, String destination, Issuer issuer) {
        SAMLHelper.initialize();
        this.assertionIdentifier = assertionId;
        this.subject = subject;
        this.destination = destination;
        this.issuer = issuer;
        this.attributeFactories = new ArrayList<SAMLElementFactory<? extends Attribute>>();
    }
    
    @Override
    public AttributeQuery produce() {
        DateTime now = new DateTime();
        AttributeQuery query = SAMLHelper.createElement(AttributeQuery.class);
        query.setID(this.assertionIdentifier);
        query.setIssueInstant(now);
        query.setVersion(SAMLVersion.VERSION_20);        
        query.setDestination(this.destination);
        // Issuer
        query.setIssuer(this.issuer);        
        // Subject
        query.setSubject(this.subject);        
        // Attributes
        for (SAMLElementFactory<? extends Attribute> attributeFactory: this.attributeFactories) {
            query.getAttributes().add(attributeFactory.produce());
        }        
        // Signature 
        // LATER query.setSignature(null);        
        // Return
        return query;
    }
    
	@Override
	public void addFactory(SAMLElementFactory<? extends Attribute> element) {
		this.attributeFactories.add(element);		
	}
    
}
