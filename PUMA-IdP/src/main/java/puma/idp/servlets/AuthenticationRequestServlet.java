package puma.idp.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import org.opensaml.saml2.core.AuthnRequest;

import puma.idp.util.saml.SAMLHTTPMessageProcessor;
import puma.idp.util.saml.SAMLMessageDecoder;
import puma.idp.util.security.IssuerValidator;

/**
 *
 * @author jasper
 */
public class AuthenticationRequestServlet extends HttpServlet {
	private static final Logger logger = Logger.getLogger(AuthnRequestResponderServlet.class.getName());
	private static final int MAX_SESSION_TIME = 2;
	private static final long serialVersionUID = -3733309326912054296L;
	private static String ERROR_PAGE = "error_sig.jsf";
    private static String LOGIN_PAGE = "http://localhost:8080/PUMA-IdP/index.jsf";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        logger.log(Level.INFO, "Received request");
    	SAMLHTTPMessageProcessor processor = new SAMLHTTPMessageProcessor((AuthnRequest) SAMLMessageDecoder.decode(request).getInboundSAMLMessage());
        logger.log(Level.INFO, "Processed message. Assertion url is " + processor.getMessage().getAssertionConsumerServiceURL());
    	if (processor.isValid() && (new IssuerValidator(processor.getIssuerId(), processor.getSignature())).isValid()) {
    		request.getSession().setAttribute("messageProcessor", processor);
    		request.getSession().setAttribute("RelayState", request.getParameter("RelayState"));
    		DateTime loginTime = (DateTime) request.getSession().getAttribute("loginTime");
			request.getSession().setAttribute("RelayState", request.getParameter("RelayState"));
    		if (loginTime == null || loginTime.plusHours(MAX_SESSION_TIME).isAfterNow()) {
    			// Login (again)
    	        logger.log(Level.INFO, "Redirecting to login page");
                response.sendRedirect(LOGIN_PAGE);
                return;
    		} else {
    			// Process message again
    			request.getRequestDispatcher("/SendResponse");
    			return;
    		}
    	}
    	response.sendRedirect(ERROR_PAGE);
        out.close();
        return;
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
        return "IdP Authentication request servlet";
    }    
}
