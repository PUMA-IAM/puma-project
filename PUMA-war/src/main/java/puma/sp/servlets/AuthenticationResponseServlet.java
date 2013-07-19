/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import puma.sp.controllers.authentication.AliasController;
import puma.sp.model.Tenant;
import puma.sp.util.saml.AuthenticationResponseHandler;
import puma.util.exceptions.flow.FlowException;
import puma.util.exceptions.flow.ResponseProcessingException;

/**
 *
 * @author jasper
 */
@WebServlet(name = "AuthenticationResponseServlet", urlPatterns = {"/SAMLAuthenticationResponseHandlerServlet"})
public class AuthenticationResponseServlet extends HttpServlet {
	private static final Logger logger = Logger.getLogger(AuthenticationResponseServlet.class.getName());
	private static final long serialVersionUID = 1L;
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
        	String subjectIdentifier, alias;
        	Tenant tenant = (Tenant) request.getSession().getAttribute("Tenant");
        	String relayState = (String) request.getSession().getAttribute("RelayState");
        	if (tenant == null) {
            	throw new FlowException("No tenant could be identified in the authentication process");
            }
        	if (relayState == null) {
        		throw new FlowException("No relay state could be found in the authentication process");
        	}
        	// Retrieve the identifier for the current subject
        	if (tenant.isLocallyManaged()) {
        		subjectIdentifier = (String) request.getSession().getAttribute("SubjectIdentifier");
        		if (subjectIdentifier == null || subjectIdentifier.isEmpty())
        			throw new FlowException("Could not identify the user: null pointer or empty identifier found");
        	} else {
        		AuthenticationResponseHandler handler = new AuthenticationResponseHandler();
        		String redirectionAddress = handler.interpret(request);
        		if (!redirectionAddress.equalsIgnoreCase((String) request.getSession().getAttribute("RelayState")))
        			throw new FlowException("Illegal relay state modification in the process");
        		subjectIdentifier = handler.getSubject(request);
        		if (subjectIdentifier == null || subjectIdentifier.isEmpty())
        			throw new FlowException("Could not identify the user: null pointer or empty identifier found");
        		request.getSession().setAttribute("SubjectIdentifier", subjectIdentifier);
        	}
        	// Store the alias for the current subject in the database
        	AliasController aliasCtrl = new AliasController();
        	alias = new String(aliasCtrl.generateAlias(subjectIdentifier, tenant).toString());
        	// Generate a cookie which indicates that the user has authenticated (should only hold for the current session)
        	// TODO
        	// Redirect back to the relay state, include the alias
        	String aliasParameter = "Alias=" + alias;
        	if (relayState.indexOf("?") >= 0)
        		response.sendRedirect(relayState + "&" + aliasParameter);
        	else
        		response.sendRedirect(relayState + "?" + aliasParameter);
        } catch (MessageDecodingException ex) {
        	logger.log(Level.SEVERE, null, ex);
            response.sendRedirect(AuthenticationResponseHandler.ERROR_LOCATION);
        } catch (org.opensaml.xml.security.SecurityException ex) {
        	logger.log(Level.SEVERE, null, ex);
            response.sendRedirect(AuthenticationResponseHandler.ERROR_LOCATION);
        } catch (ResponseProcessingException ex) {
        	logger.log(Level.SEVERE, null, ex);
        } catch (FlowException ex) {
        	logger.log(Level.SEVERE, null, ex);
		} finally {            
            out.close();
        }
    }

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Post-Authentication servlet in the authentication flow";
    }
}