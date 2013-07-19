package puma.idp.servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import org.opensaml.saml2.core.Response;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import puma.idp.util.saml.SAMLHTTPMessageProcessor;
import puma.idp.util.saml.SAMLMessageEncoder;
import puma.util.exceptions.NotSupportedException;
import puma.util.exceptions.flow.FlowException;


public class AuthnRequestResponderServlet extends HttpServlet {
	private static final Logger logger = Logger.getLogger(AuthnRequestResponderServlet.class.getName());
	private static final long serialVersionUID = -5923124999886590017L;
	

    private void processRequest(HttpServletRequest request, HttpServletResponse response) {
    	logger.log(Level.INFO, "Arrived at /SendResponse");
		try {
				SAMLHTTPMessageProcessor processor = (SAMLHTTPMessageProcessor) request.getSession().getAttribute("messageProcessor");
				DateTime loginTime = (DateTime) request.getSession().getAttribute("loginTime");
                String userName = (String) request.getSession().getAttribute("UserName");
                String relayState = (String) request.getSession().getAttribute("RelayState");
				if (processor == null) {
					throw new FlowException("Could not find the SAML HTTP Message Processor at the identity provider");
				}
				if (loginTime == null) {
					throw new FlowException("Could not find the login time of the authenticated user at the identity provider");
				}
				if (userName == null) {
					throw new FlowException("Could not identify the user which previously authenticated at the identity provider");
				}
				Response samlMessage = processor.generateResponse(userName);
				logger.log(Level.INFO, "Sending redirect to " + samlMessage.getDestination()); // DEBUG Should be the current host, which is a bug. In fact, find out how to retrieve the sending party of a message.
				SAMLMessageEncoder.encode(response, samlMessage.getDestination(), samlMessage, relayState);				
			} catch (FlowException e) {
				logger.log(Level.SEVERE, null, e);
			} catch (MessageEncodingException e) {
				logger.log(Level.SEVERE, null, e);
			} catch (NotSupportedException e) {
				logger.log(Level.SEVERE, null, e);
			}
	}
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "IdP Authentication request responder servlet";
    }    
}
