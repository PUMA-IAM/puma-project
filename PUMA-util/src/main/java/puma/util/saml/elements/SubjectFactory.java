/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.saml.elements;

import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import puma.util.saml.ObjectFactory;
import puma.util.saml.SAMLHelper;

/**
 *
 * @author jasper
 */
public class SubjectFactory implements ObjectFactory<Subject> {
    private static final String DEFAULT_FORMAT = NameID.X509_SUBJECT;
    private String subjectIdentifier;
    private String format;
    
    public SubjectFactory(String subject) { // LATER Set correct format
        this(subject, SubjectFactory.DEFAULT_FORMAT);
    }
    
    public SubjectFactory(String subject, String format) {
        SAMLHelper.initialize();
        this.subjectIdentifier = subject;
        this.format = format;
    }
    
    @Override
    public Subject produce() {
        Subject subject = SAMLHelper.createElement(Subject.class);       
        NameID nameId = SAMLHelper.createElement(NameID.class); // NameId
        nameId.setFormat(this.format); 
        nameId.setValue(this.subjectIdentifier);
        subject.setNameID(nameId);
        return subject;
    }
    
}
