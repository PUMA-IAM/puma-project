/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.saml.messages;

import org.joda.time.DateTime;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.AuthnContextComparisonTypeEnumeration;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.RequestedAuthnContext;
import org.opensaml.saml2.core.impl.RequestedAuthnContextBuilder;
import puma.util.saml.ObjectFactory;
import puma.util.saml.SAMLHelper;
import puma.util.saml.elements.IssuerFactory;
import puma.util.saml.elements.NameIDPolicyFactory;

/**
 *
 * @author jasper
 */
public class AuthnRequestFactory implements ObjectFactory<AuthnRequest> {
    private static String PREFERRED_BINDING = SAMLConstants.SAML2_REDIRECT_BINDING_URI;
    private String assertionIdentifier;
    private String destination;
    private String providerName;
    private String assertionConsumerServiceURL;
    private String binding;
    
    public AuthnRequestFactory(String assertionId, String destination, String providerName, String assertionConsumerServiceUrl) {
        SAMLHelper.initialize();
        this.destination = destination;
        this.providerName = providerName;
        this.assertionConsumerServiceURL = assertionConsumerServiceUrl;
        this.binding = AuthnRequestFactory.PREFERRED_BINDING;
        this.assertionIdentifier = assertionId;
    }

    @Override
    public AuthnRequest produce() {
        AuthnRequest result = SAMLHelper.createElement(AuthnRequest.class);
        result.setForceAuthn(false); // Sets whether the IdP should force the user to reauthenticate.
        result.setIsPassive(false); // Sets whether the IdP should refrain from interacting with the user during the authentication process.
        result.setProtocolBinding(this.binding); // Sets the protocol binding URI for the request.
        result.setProviderName(this.providerName);   // Sets the human-readable name of the requester for use by the presenter's user agent or the identity provider.
        result.setNameIDPolicy((new NameIDPolicyFactory()).produce()); // Sets the NameIDPolicy of the request.
        result.setIssueInstant(new DateTime());
        result.setDestination(this.destination);
        result.setIssuer((new IssuerFactory(this.providerName)).produce());
        result.setID(this.assertionIdentifier); // Set the id of the request
        // theRequest.setScoping(null);  <Scoping> defines the chain of identity providers through which the authnrequest is proxied
        result.setAssertionConsumerServiceURL(this.assertionConsumerServiceURL);
        // LATER Signing the request...
        // Sets the request authentication context
        RequestedAuthnContextBuilder contextBuilder = new RequestedAuthnContextBuilder();
        RequestedAuthnContext theContext = contextBuilder.buildObject();
        theContext.setComparison(AuthnContextComparisonTypeEnumeration.EXACT);
        result.setRequestedAuthnContext(theContext);
        return result;
    }
    
}
