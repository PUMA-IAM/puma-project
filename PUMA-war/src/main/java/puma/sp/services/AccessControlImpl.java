package puma.sp.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;

import org.apache.commons.ssl.Base64;
import org.opensaml.ws.message.encoder.MessageEncodingException;

import puma.sp.clients.AttributeForwardImplService;
import puma.sp.clients.AttributeForwardService;
import puma.sp.controllers.authentication.AliasController;
import puma.sp.controllers.management.ServiceManagementController;
import puma.sp.controllers.management.TenantManagementController;
import puma.sp.controllers.management.UserManagementController;
import puma.sp.model.Attribute;
import puma.sp.model.Service;
import puma.sp.model.Tenant;
import puma.sp.model.User;
import puma.sp.model.UserAlias;
import puma.sp.util.saml.AttributeRequestHandler;
import puma.sp.util.saml.AttributeResponseHandler;
import puma.util.exceptions.ElementNotFoundException;
import puma.util.exceptions.MultipleElementsFoundException;
import puma.util.exceptions.NoServiceSpecifiedException;
import puma.util.exceptions.TenantLostException;
import puma.util.exceptions.flow.ResponseProcessingException;
import puma.util.exceptions.saml.ElementProcessingException;
import puma.util.exceptions.saml.ServiceParameterException;

@WebService(endpointInterface = "puma.sp.services.AccessControlService")
public class AccessControlImpl implements AccessControlService {
	private static final Logger logger = Logger.getLogger(AccessControlImpl.class.getName());

	@Override
	public Boolean isAllowedAccess(String userIdentifier, String serviceName, String actionIdentifier, String resourceName) {
		return true; // LATER Authorization endpoint here
	}

	@SuppressWarnings("unchecked")
	@Override
	public String retrieveAttributes(String userAlias,	String serviceName) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream wr = new ObjectOutputStream(baos);
			UserManagementController userCtrl = new UserManagementController();
			ServiceManagementController serviceCtrl = new ServiceManagementController();
			TenantManagementController tenantCtrl = new TenantManagementController();
			AliasController aliasCtrl = new AliasController();
			String userIdentifier = aliasCtrl.retrieveAlias(userAlias).getUserIdentifier();
			Tenant tenant = tenantCtrl.getTenant(Integer.parseInt(aliasCtrl.retrieveAlias(userAlias).getTenantIdentifier()));
			Service service = serviceCtrl.getService(serviceName);
			if (tenant == null) {
				throw new TenantLostException();
			}
			if (service == null) {
				throw new NoServiceSpecifiedException();
			}
			HashMap<String, ArrayList<String>> result = null;
			if (tenant.isLocallyManaged()) {
				// Local lookup
				User user = userCtrl.getUser(Long.parseLong(userIdentifier));
				Map<String, List<Attribute>> attr = userCtrl.getAttributes(
						service, user);
				result = new HashMap<String, ArrayList<String>>();
				for (String key : attr.keySet()) {
					ArrayList<String> nextValues = new ArrayList<String>();
					for (Attribute next : attr.get(key))
						nextValues.add(next.getAttributeValue().toString());
					result.put(key, nextValues);
				}
			} else {
				// Remote lookup
				/// Set up message
				AttributeRequestHandler handler = new AttributeRequestHandler(service.getAttributes(), userIdentifier, tenant);
				String samlAttrRequest = handler.prepareResponse(null, handler.buildRequest());
				/// Retrieve result of message
				AttributeResponseHandler responseHandler = new AttributeResponseHandler(service.getAttributes());
				String reply = send(samlAttrRequest); // Performs the actual request
				Map<String, Serializable> attributes = responseHandler.interpret(reply);
				result = new HashMap<String, ArrayList<String>>();
				for (String key : attributes.keySet()) {
					ArrayList<String> next = (ArrayList<String>) attributes.get(key);
					result.put(key, next);
				}
			}
            wr.writeObject(result);
            wr.flush();
            wr.close();
            return new String(Base64.encodeBase64(baos.toByteArray()));
		} catch (TenantLostException e) {
			logger.log(Level.SEVERE, null, e);
		} catch (NoServiceSpecifiedException e) {
			logger.log(Level.SEVERE, null, e);
		} catch (MessageEncodingException e) {
			logger.log(Level.SEVERE, null, e);
		} catch (ElementNotFoundException e) {
			logger.log(Level.SEVERE, null, e);
		} catch (ElementProcessingException e) {
			logger.log(Level.SEVERE, null, e);
		} catch (ServiceParameterException e) {
			logger.log(Level.SEVERE, null, e);
		} catch (ResponseProcessingException e) {
			logger.log(Level.SEVERE, null, e);
		} catch (MultipleElementsFoundException e) {
			logger.log(Level.SEVERE, null, e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, null, e);
		}
        return new String(Base64.encodeBase64(new byte[0]));
	}

	@Override
	public String getAuthenticationAddress() {
		return "http://localhost:8080/PUMA-war/ServiceAccessServlet";
	}

	private String send(String samlAttrRequest) {
		AttributeForwardImplService forwarder = new AttributeForwardImplService();
		AttributeForwardService service = forwarder.getAttributeForwardImplPort();
		return service.send(samlAttrRequest);
	}

	@Override
	public String generateSharedPassword(String publicKey) {
		// LATER Generate a symmetric key and encrypt with the public key
		return null;
	}

	@Override
	public Boolean isAuthenticated(String userIdentifier) {
		try {
			AliasController aliasCtrl = new AliasController();
			UserAlias userAlias = aliasCtrl.retrieveAlias(userIdentifier);
			if (userAlias != null)
				return true;
			return false;
		} catch (MultipleElementsFoundException e) {
			logger.log(Level.SEVERE, null, e);
			return false;
		} catch (ElementNotFoundException e) {
			return false;
		}
	}

	@Override
	public Boolean registerAccess(String userAlias, String serviceName,
			String actionIdentifier, String resourceName) {
		// Used for special authorization cases (e.g. Chinese wall, break-glass, ...)
		// Return false if the access was not allowed and hence is not registered
		return true;
	}

	@Override
	public Integer getTenant(String userAlias) {
		try {
			AliasController aliasCtrl = new AliasController();
			UserAlias aliasObject;
			aliasObject = aliasCtrl.retrieveAlias(userAlias);
			if (aliasObject != null)
				return Integer.parseInt(aliasObject.getTenantIdentifier());
		} catch (MultipleElementsFoundException e) {
			logger.log(Level.SEVERE, null, e);
		} catch (ElementNotFoundException e) {
			logger.log(Level.SEVERE, null, e);
		}
		return null;
	}
}
