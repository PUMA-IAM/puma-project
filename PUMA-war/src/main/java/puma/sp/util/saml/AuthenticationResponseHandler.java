/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.util.saml;

import javax.servlet.http.HttpServletRequest;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Response;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.xml.security.SecurityException;
import puma.sp.controllers.authentication.LoginController;
import puma.util.exceptions.flow.ResponseProcessingException;
import puma.util.saml.SAMLHelper;
import puma.util.saml.encoding.ResponseHandler;

/**
 *
 * @author jasper
 */
public class AuthenticationResponseHandler extends ResponseHandler {
    private LoginController loginService;
    public static String ERROR_LOCATION = "http://localhost:8080/PUMA-war/error.jsf";
    
    public AuthenticationResponseHandler() {
        this.loginService = new LoginController();
    }
    
    @Override
    public String interpret(HttpServletRequest request) throws MessageDecodingException, org.opensaml.xml.security.SecurityException, ResponseProcessingException {
        SAMLHelper.initialize();
        Response authnResponse = (Response) super.decodeMessage(request);
        // Perform apropriate actions
        if (SAMLHelper.verifyResponse(authnResponse)) {
            // Make redirection to 
            String redirect = this.loginService.getRelayState(authnResponse.getInResponseTo(), true);
            if (redirect != null) {
                return redirect;
            }
        }
        else {
            throw new ResponseProcessingException("Could not verify the response.");
        }
        throw new ResponseProcessingException("No redirection address found");
    }
    
    public String getSubject(HttpServletRequest request) throws SecurityException, MessageDecodingException, ResponseProcessingException  {
        SAMLHelper.initialize();
        Response authnResponse = super.decodeMessage(request);
        for (Assertion ass: authnResponse.getAssertions()) {
            if (ass.getSubject() != null) {
                return ass.getSubject().getNameID().getValue();
            }
        }
        throw new ResponseProcessingException("Could not find the authenticated subject");
    }    
}
