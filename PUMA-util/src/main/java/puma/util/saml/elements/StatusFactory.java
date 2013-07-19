package puma.util.saml.elements;

import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import puma.util.saml.ObjectFactory;
import puma.util.saml.SAMLHelper;

public class StatusFactory implements ObjectFactory<Status> {
	private final static String DEFAULT_STATUS_CODE = StatusCode.SUCCESS_URI;
	
	private String statusCodeValue;
	
	public StatusFactory() {
		this(DEFAULT_STATUS_CODE);
	}
	
	// LATER Add StatusDetail, StatusMessage
	public StatusFactory(String statusCode) {
		SAMLHelper.initialize();
		this.statusCodeValue = statusCode;
	}

	@Override
	public Status produce() {
		Status status = SAMLHelper.createElement(Status.class);
        StatusCode code = SAMLHelper.createElement(StatusCode.class);
        code.setValue(this.statusCodeValue);
        status.setStatusCode(code);
		return status;
	}

}
