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
import org.opensaml.ws.message.encoder.MessageEncodingException;
import puma.sp.model.Tenant;
import puma.sp.util.FlowDirecter;
import puma.sp.util.saml.AuthenticationRequestHandler;
import puma.util.exceptions.flow.FlowException;
import puma.util.exceptions.flow.RequestConstructionException;

/**
 *
 * @author jasper
 */
@WebServlet(name = "AuthenticationRequestServlet", urlPatterns = {"/AuthenticationRequestServlet"})
public class AuthenticationRequestServlet extends HttpServlet {
	private static final Logger logger = Logger.getLogger(AuthenticationRequestServlet.class.getName());
	private static final long serialVersionUID = 1L;
    // TODO To properties file
    public static String ERROR_LOCATION = "http://localhost:8080/PUMA-war/error.jsf";

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
        	Tenant tenant = (Tenant) request.getSession().getAttribute("Tenant");
            String relayState = (String) request.getSession().getAttribute("RelayState");
            if (relayState == null) {
                throw new FlowException("No relay state was found in the authentication process");
            }
            if (tenant == null) {
            	throw new FlowException("No tenant could be identified in the authentication process");
            }
            if (tenant.isLocallyManaged()) {
        		request.getSession().setAttribute("FlowRedirectionElement", new FlowDirecter("/AuthenticationResponseServlet"));
            	response.sendRedirect("login.jsf");
        		return;
            } else {
            	AuthenticationRequestHandler handler = new AuthenticationRequestHandler(relayState, tenant);
            	handler.prepareResponse(response, handler.buildRequest());
        		return;
            }        	 
        } catch (MessageEncodingException e) {  
        	logger.log(Level.SEVERE, e.getMessage(), e);
            response.sendRedirect(ERROR_LOCATION);
        } catch (RequestConstructionException e) {
        	logger.log(Level.SEVERE, e.getMessage(), e);  
            response.sendRedirect(ERROR_LOCATION);
        } catch (FlowException e) {
        	logger.log(Level.SEVERE, e.getMessage(), e);  
            response.sendRedirect(ERROR_LOCATION);
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
        return "Pre-authentication servlet in the authentication flow";
    }
}