package puma.sp.services;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(targetNamespace = "http://puma/sp", name = "AccessControlService")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public interface AccessControlService {	
	public Boolean isAllowedAccess(String userAlias, String serviceName, String actionIdentifier, String resourceName);
	public Boolean registerAccess(String userAlias, String serviceName, String actionIdentifier, String resourceName);
	public Boolean isAuthenticated(String userAlias);
	public Integer getTenant(String userAlias);
	public String getAuthenticationAddress();
	public String retrieveAttributes(String userAlias, String serviceName);
	public String generateSharedPassword(String publicKey);
}
