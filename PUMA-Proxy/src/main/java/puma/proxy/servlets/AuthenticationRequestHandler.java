package puma.proxy.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.validation.ValidationException;
import puma.proxy.support.ProviderFetcher;
import puma.proxy.support.Redirecter;
import puma.proxy.support.proxy.AuthnRequestProxy;
import puma.proxy.support.saml.ProxyExtensionMarshaller;
import puma.util.exceptions.TenantIdentificationException;

public class AuthenticationRequestHandler extends HttpServlet {
	private static final Logger logger = Logger.getLogger(AuthenticationRequestHandler.class.getName());
	private static final long serialVersionUID = 3831094729337404438L;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            AuthnRequestProxy proxy = new AuthnRequestProxy();
        	// TODO No more default values. Omit everything and constrain to ProviderFetcher fetcher = new ProviderFetcher(this.extractProxyExtensionValue(context));
            ProviderFetcher fetcher;
            String relayStateParam = (String) request.getParameter("RelayState");
            //logger.log(Level.INFO, "Received request to proxy for " + relayStateParam);
            List<String> proxyValues = (new ProxyExtensionMarshaller(proxy.decode(request).getExtensions())).get();
            //if (relayStateParam == null) {
            if (proxyValues.isEmpty()) {
                fetcher = new ProviderFetcher(); 
            } else {
                //URL relayState = new URL(relayStateParam);
                //fetcher = new ProviderFetcher(relayState);
            	fetcher = new ProviderFetcher(proxyValues);
            }
            AuthnRequest authReq = proxy.reencode(request, fetcher.getAuthenticationRequestHandler());
            logger.log(Level.INFO, "Proxy: redirecting " + authReq.toString() + " to " + fetcher.getAuthenticationRequestHandler() + "...");
            Redirecter.sendRequestRedirect(response, authReq, fetcher.getAuthenticationRequestHandler(), relayStateParam);
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
        } catch (org.opensaml.xml.security.SecurityException ex) {
        	logger.log(Level.SEVERE, null, ex);
		} catch (TenantIdentificationException ex) {
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
        return "Authentication request proxy";
    }
}
