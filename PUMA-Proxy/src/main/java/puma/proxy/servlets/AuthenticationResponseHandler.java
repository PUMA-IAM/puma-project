package puma.proxy.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opensaml.saml2.core.Response;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.validation.ValidationException;
import puma.proxy.support.Redirecter;
import puma.proxy.support.proxy.ResponseProxy;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationResponseHandler extends HttpServlet {
	private static final Logger logger = Logger.getLogger(AuthenticationResponseHandler.class.getName());
	private static final long serialVersionUID = 2137878379122138759L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            String relayState = (String) request.getParameter("RelayState");
            ResponseProxy proxy = new ResponseProxy();
            Response samlResponse = proxy.reencode(request);
            logger.log(Level.INFO, "Proxy: Sending authentication response to " + samlResponse.getDestination() + " * ");
            Redirecter.sendResponseRedirect(response, samlResponse, samlResponse.getDestination(), relayState);
        } catch (MessageEncodingException ex) {
        	logger.log(Level.SEVERE, null, ex);
        } catch (ConfigurationException ex) {
        	logger.log(Level.SEVERE, null, ex);
        } catch (MessageDecodingException ex) {
        	logger.log(Level.SEVERE, null, ex);
        } catch (ValidationException ex) {
        	logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
        	logger.log(Level.SEVERE, null, ex);
        } finally {
            out.close();
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
        return "Authentication Response Handler";
    }
}
