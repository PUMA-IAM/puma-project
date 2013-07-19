/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.util.saml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.Response;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSString;
import puma.sp.model.AttributeType;
import puma.util.exceptions.flow.ResponseProcessingException;
import puma.util.exceptions.saml.ElementProcessingException;
import puma.util.exceptions.saml.ServiceParameterException;
import puma.util.saml.SAMLHelper;

/**
 *
 * @author jasper
 */
public class AttributeResponseHandler {
	private static final Logger logger = Logger.getLogger(AttributeResponseHandler.class.getName());
    private Set<AttributeType> attributes;
    public AttributeResponseHandler(Set<AttributeType> types) {
        SAMLHelper.initialize();
        this.attributes = types;
        // DEBUG
        for (AttributeType type: types)
        	logger.log(Level.INFO, "Required attributetype: " + type.getName());
        // /DEBUG
    }
    
    public Map<String, Serializable> interpret(String message) throws ResponseProcessingException, ServiceParameterException, ElementProcessingException {
        Map<String, Serializable> result = new HashMap<String, Serializable>(); // TODO Cache this data, and also store the condition's NotOnOrAfter.
        logger.log(Level.INFO, message);
        Response response = SAMLHelper.processString(message, Response.class);
        SAMLHelper.verifyResponse(response);
        SAMLHelper.verifySignature(response.getSignature());
        for (Assertion assertion: response.getAssertions()) {
        	logger.log(Level.INFO, "Checking next assertion... [" + assertion.getConditions().getNotOnOrAfter().isAfterNow() + "]");
            if (assertion.getConditions().getNotOnOrAfter().isAfterNow()) {
                for (AttributeStatement statement: assertion.getAttributeStatements()) {
                	logger.log(Level.INFO, "Checking next statment...");
                    for (Attribute attribute: statement.getAttributes()) {
                    	logger.log(Level.INFO, "Checking next attribute...");
                        if (attribute.getAttributeValues().isEmpty()) {
                            throw new ElementProcessingException("attribute " + attribute.getName(), "No values given");
                        }                        
                        if (attribute.getAttributeValues().size() > 1) {
                            List<String> values = new ArrayList<String>();
                            for (XMLObject next: attribute.getAttributeValues()) {
                            	logger.log(Level.INFO, "Found attribute as " + attribute.getName() + "=" + ((XSString) next).getValue());
                                values.add(((XSString) next).getValue());
                            }
                            result.put(attribute.getName(), (Serializable) values);
                        } else {
                            List<String> values = new ArrayList<String>();
                            values.add(((XSString) attribute.getAttributeValues().get(0)).getValue());
                        	logger.log(Level.INFO, "Found one attribute as " + attribute.getName() + "=" + ((XSString) attribute.getAttributeValues().get(0)).getValue());
                            result.put(attribute.getName(), (Serializable) values);
                        }
                    }
                }
            }
        }
        logger.log(Level.INFO, "Sizes of the attributes: " + result.size() + " [Found attributes]; " + this.attributes.size() + " [Required attributes]");
        if (result == null || result.size() != this.attributes.size()) {
            throw new ResponseProcessingException("Size of returned attribute assertion does not match the size of the attribute query");
        }        
        return result;
    }
}
