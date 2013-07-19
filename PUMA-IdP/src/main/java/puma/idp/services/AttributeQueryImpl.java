package puma.idp.services;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;

import org.opensaml.saml2.core.AttributeQuery;
import org.opensaml.saml2.core.impl.ResponseMarshaller;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import puma.idp.util.saml.QueryProcessor;
import puma.util.exceptions.ElementNotFoundException;
import puma.util.exceptions.NoSubjectSpecifiedException;
import puma.util.exceptions.saml.ElementProcessingException;
import puma.util.exceptions.saml.ServiceParameterException;
import puma.util.saml.SAMLHelper;


@WebService(endpointInterface = "puma.idp.services.AttributeQueryService")
public class AttributeQueryImpl implements AttributeQueryService {
	private static final Logger logger = Logger.getLogger(AttributeQueryImpl.class.getName());

	@Override
	public String query(String context) {
		try {
			if (context == null) {
				throw new IllegalArgumentException("Web parameter given to send operation mustn't be a null pointer");
			}		
			QueryProcessor processor = new QueryProcessor(SAMLHelper.processString(context, AttributeQuery.class));
			Marshaller marshaller = new ResponseMarshaller();
			// DEBUG
			logger.log(Level.INFO, XMLHelper.prettyPrintXML(marshaller.marshall(processor.process())));
			String msg = "Attributes required:";
			for (String next: processor.getRequestedAttributes()) {
				msg = msg + next + ", ";
			}
			logger.log(Level.INFO, msg);
			return XMLHelper.prettyPrintXML(marshaller.marshall(processor.process()));
		} catch (ServiceParameterException e) {
			logger.log(Level.SEVERE, null, e);
		} catch (ElementProcessingException e) {
			logger.log(Level.SEVERE, null, e);
		} catch (NoSubjectSpecifiedException e) {
			logger.log(Level.SEVERE, null, e);
		} catch (MarshallingException e) {
			logger.log(Level.SEVERE, null, e);
		} catch (ElementNotFoundException e) {
			logger.log(Level.SEVERE, null, e);
		}		
		return null; // TODO Return error Response
	}

}
