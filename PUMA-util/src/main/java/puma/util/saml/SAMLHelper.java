/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.saml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.validation.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import puma.util.exceptions.saml.ElementProcessingException;
import puma.util.exceptions.saml.ServiceParameterException;
import puma.util.exceptions.saml.WrongSignatureException;

/**
 *
 * @author jasper
 */
public class SAMLHelper {
	private static final Logger logger = Logger.getLogger(SAMLHelper.class.getName());
    public SAMLHelper() {
        SAMLHelper.initialize();     
    }
    
    public static void initialize() {
        try {
            DefaultBootstrap.bootstrap();
        } catch (ConfigurationException ex) {
            logger.log(Level.SEVERE, null, ex);
        }   
    }
    
    /**
     * Creates a SAML element corresponding to the given class name and with the specified qualified name
     * @param elementClassName The class for which to make an element
     * @param qname The qualified name for the element to create
     * @return An object of class <code>elementClassName</code>.
     */
    @SuppressWarnings("unchecked")
    public static <T> T createElement(Class<T> elementClassName, QName qname) {
        if (qname == null) {
            return createElement(elementClassName);
        }
        return (T) ((XMLObjectBuilder<? extends XMLObject>) Configuration.getBuilderFactory().getBuilder(qname)).buildObject(qname);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T createElement(Class<T> elementClassName, QName elementName, QName typeName) {
        return (T) ((XMLObjectBuilder<? extends XMLObject>) Configuration.getBuilderFactory().getBuilder(typeName)).buildObject(elementName, typeName);
    }
    
    /**
     * Creates a SAML element corresponding to the given class name and with the specified qualified name
     * @param elementClassName The class for which to make an element. Takes DEFAULT_ELEMENT_NAME.
     * @return An object of class <code>elementClassName</code>.
     */
    public static <T> T createElement(Class<T> elementClassName) {
        try {
            Field field = elementClassName.getDeclaredField("DEFAULT_ELEMENT_NAME");
            field.setAccessible(true);
            return SAMLHelper.createElement(elementClassName, (QName) field.get(null));
        } catch (IllegalAccessException e) {
            logger.log(Level.SEVERE, null, e);
            return null;
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, null, e);      
            return null;      
        } catch (NoSuchFieldException e) {
            logger.log(Level.SEVERE, null, e); 
            return null;           
        }
    }
    
    @SuppressWarnings("unchecked")
	public static <T> T processString(String message, Class<T> className) throws ServiceParameterException, ElementProcessingException {
        try {
            DocumentBuilderFactory newFactory = DocumentBuilderFactory.newInstance();
            newFactory.setNamespaceAware(true);
            DocumentBuilder builder = newFactory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(message.getBytes("UTF-8")));
            Element samlElement = doc.getDocumentElement();            
            Unmarshaller unmarshaller = Configuration.getUnmarshallerFactory().getUnmarshaller(samlElement);
            if (unmarshaller == null) {
                throw new ElementProcessingException(samlElement.toString(), "No unmarshaller");
            }
            XMLObject samlObject = unmarshaller.unmarshall(samlElement);
            if (!SAMLHelper.isSpecifiedElement(samlObject, className)) {
                throw new ServiceParameterException("Parameter " + message + " is not of type AttributeQuery");
            }
            return (T) samlObject;
        } catch (UnmarshallingException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new ElementProcessingException(message, ex.getMessage());
        } catch (ParserConfigurationException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new ElementProcessingException(message, ex.getMessage());            
        } catch (SAXException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new ElementProcessingException(message, ex.getMessage());            
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new ElementProcessingException(message, ex.getMessage());            
        }
    }

    private static <T> boolean isSpecifiedElement(XMLObject message, Class<T> className) {
        try {
            Field field = className.getDeclaredField("DEFAULT_ELEMENT_LOCAL_NAME");
            field.setAccessible(true); 
            return message.getElementQName().getLocalPart().equals((field.get(null)));
        } catch (IllegalAccessException e) {
            logger.log(Level.SEVERE, null, e);
        } catch (NoSuchFieldException e) {
            logger.log(Level.SEVERE, null, e);
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, null, e);
        }
        return false;
    }
    
    public static Boolean verifyResponse(Response authnResponse) {
        try {
            authnResponse.validate(true);
        } catch (ValidationException ex) {
            logger.log(Level.SEVERE, null, ex);
            return false;
        }
        if (!authnResponse.getStatus().getStatusCode().getValue().equals(StatusCode.SUCCESS_URI)) {
            return false;
        }
        if (!SAMLHelper.verifySignature(authnResponse.getSignature())) {
            logger.log(Level.SEVERE, null, new WrongSignatureException(authnResponse.getIssuer().getValue(), authnResponse.getInResponseTo()));
            return false;
        }
        for (Assertion ass: authnResponse.getAssertions()) {
            // Checks on assertions of the authentication response are done here
            if (!SAMLHelper.verifyAssertion(ass)) {
                return false;
            }
        }
        return true;
    }
    
    public static Boolean verifyAssertion(Assertion attrResponse) {
        try {
            attrResponse.validate(true);
        } catch (ValidationException ex) {
            logger.log(Level.SEVERE, null, ex);
            return false;
        }
        if (attrResponse.getIssueInstant() != null && attrResponse.getIssueInstant().isAfterNow()) {
            return false;
        }
        if (!SAMLHelper.verifySignature(attrResponse.getSignature())) {
            logger.log(Level.SEVERE, null, new WrongSignatureException(attrResponse.getIssuer().getValue(), "(signed assertion)"));
            return false;
        }
        return true;
    }
    
    public static Boolean verifySignature(Signature sig) {
        return true; // LATER Signature verification module here
    }
}
