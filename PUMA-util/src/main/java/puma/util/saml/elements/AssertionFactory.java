package puma.util.saml.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.opensaml.saml2.core.Advice;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.AuthzDecisionStatement;
import org.opensaml.saml2.core.Condition;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Subject;
import org.opensaml.xml.XMLObject;
import puma.util.exceptions.ElementNotFoundException;
import puma.util.saml.CompoundFactory;
import puma.util.saml.CompoundFactoryHolder;
import puma.util.saml.ObjectFactory;
import puma.util.saml.RetrievableCompoundFactory;
import puma.util.saml.SAMLElementFactory;
import puma.util.saml.SAMLHelper;

public class AssertionFactory implements ObjectFactory<Assertion>, CompoundFactory<Condition> {
	private String assertionIdentifier;
	private Issuer issuer;
	private DateTime issueInstant;
	private Advice advice;
	private Subject subject;
	private Conditions conditionsObject;
	private List<SAMLElementFactory<? extends Condition>> conditionFactories;
	private List<AttributeStatementFactory> attributeStatementFactories;
	private Map<Class<? extends XMLObject>, CompoundFactoryHolder<RetrievableCompoundFactory<? extends SAMLElementFactory<?>>>> iterators;

	public AssertionFactory(String assertionId, Issuer issuer) {
		this(assertionId, issuer, new DateTime());
	}
	
	public AssertionFactory(String assertionId, Issuer issuer, DateTime issueInstant) {
		this(assertionId, issuer, issueInstant, null, null);
	}
	
	public AssertionFactory(String assertionId, Issuer issuer, DateTime issueInstant, Advice advice) {
		this(assertionId, issuer, issueInstant, null, advice);
	}
	
	public AssertionFactory(String assertionId, Issuer issuer, DateTime issueInstant, Subject subject) {
		this(assertionId, issuer, issueInstant, subject, null);
	}
	
	public AssertionFactory(String assertionId, Issuer issuer, DateTime issueInstant, Subject subject, Advice advice) {
		SAMLHelper.initialize();
		this.assertionIdentifier = assertionId;
		this.issuer = issuer;
		this.issueInstant = issueInstant;
		this.advice = advice;
		this.subject = subject;
		this.conditionFactories = new ArrayList<SAMLElementFactory<? extends Condition>>();
		this.attributeStatementFactories = new ArrayList<AttributeStatementFactory>();
		this.conditionsObject = SAMLHelper.createElement(Conditions.class);
		this.iterators = new HashMap<Class<? extends XMLObject>, CompoundFactoryHolder<RetrievableCompoundFactory<? extends SAMLElementFactory<?>>>>();
		this.iterators.put(AttributeStatement.class, null); // LATER Add new CompoundFactoryHolder<AttributeStatementFactory>()
		this.iterators.put(AuthnStatement.class, null); // LATER Add AuthnStatementFactory
		this.iterators.put(AuthzDecisionStatement.class, null); // LATER Add AuthzDecisionFactory
	}
	
	
	@Override
	public Assertion produce() {
		Assertion result = SAMLHelper.createElement(Assertion.class);
		result.setID(this.assertionIdentifier);
		if (this.advice != null)
			result.setAdvice(this.advice);
		result.setIssueInstant(this.issueInstant);
		result.setIssuer(this.issuer);
		if (this.subject != null)
			result.setSubject(this.subject);
		for (SAMLElementFactory<? extends Condition> factory: this.conditionFactories) 
			this.conditionsObject.getConditions().add(factory.produce());
		/*
		for (CompoundFactoryHolder<RetrievableCompoundFactory<? extends SAMLElementFactory<?>>> nextHolder: this.iterators.values()) {
			while (nextHolder.hasNext()) {
				RetrievableCompoundFactory<? extends SAMLElementFactory<?>> compoundFactory = nextHolder.next();
				for (SAMLElementFactory<?> factory: compoundFactory.retrieveFactories()) {
					factory.produce();	// LATER Use reflection to put these elements in their right place
				}
			}
		}
		*/
		result.setConditions(this.conditionsObject);
		// The code below should be outphased in favor of the more generic use of CompoundFactoryHolders (later)
		for (AttributeStatementFactory factory: this.attributeStatementFactories) {
			result.getAttributeStatements().add(factory.produce());
		}
		return result;
	}

	@Override
	public void addFactory(SAMLElementFactory<? extends Condition> element) {
		this.conditionFactories.add(element);		
	}
	
	public void setConditionProperties(DateTime notBefore, DateTime notOnOrAfter) {
		if (notBefore != null)
			this.conditionsObject.setNotBefore(notBefore);
		if (notOnOrAfter != null)
			this.conditionsObject.setNotOnOrAfter(notOnOrAfter);
		// LATER this.conditionsObject.getAudienceRestrictions().add(null);
	}

	public <T> CompoundFactoryHolder<?> getIterators(Class<T> classObject) throws ElementNotFoundException {
		if (!this.iterators.containsKey(classObject))
			throw new ElementNotFoundException(classObject.getName());
		return this.iterators.get(classObject);
	}
	
	public void addAttributeStatementFactory(AttributeStatementFactory element) {
		this.attributeStatementFactories.add(element);
	}
}
