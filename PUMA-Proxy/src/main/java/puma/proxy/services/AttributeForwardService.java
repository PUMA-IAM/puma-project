package puma.proxy.services;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(targetNamespace = "http://puma.com/proxy", name = "AttributeForwardService")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public interface AttributeForwardService {
	public String send(@WebParam(name = "context") String context);
}
