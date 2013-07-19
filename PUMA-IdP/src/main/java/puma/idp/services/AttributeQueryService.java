package puma.idp.services;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(targetNamespace = "http://puma/idp", name = "AttributeQueryService")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public interface AttributeQueryService {
	public String query(@WebParam(name = "context") String context);
}
