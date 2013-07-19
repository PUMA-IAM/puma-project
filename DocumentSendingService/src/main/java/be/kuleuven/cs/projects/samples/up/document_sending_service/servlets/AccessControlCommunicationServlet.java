package be.kuleuven.cs.projects.samples.up.document_sending_service.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccessControlCommunicationServlet extends HttpServlet {
	private static final long serialVersionUID = -615479800928592464L;
	private final static String DEFAULT_TENANT = "3"; //"Cynalco Medics Research"=3, "Cynalco Medics"=2, "Vermaelen Projects"=1,"Cynalco Medics Customer Service"=5;
	private final static String RELATIVE_PATH = "../PUMA-war/ServiceAccessServlet?";
	private String DEFAULT_RELAY_STATE;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
        	int index = request.getRequestURL().toString().indexOf(this.getServletName());
        	DEFAULT_RELAY_STATE = request.getRequestURL().toString().substring(0, index - 1) + "/AccessControlCommunicationReplyServlet";
            String redirectionURL = RELATIVE_PATH;
            if (request.getParameter("Tenant") == null || request.getParameter("Tenant").isEmpty()) // TODO fetch tenant from bean or current url 
                redirectionURL = redirectionURL + "Tenant=" + DEFAULT_TENANT; 
            else
                redirectionURL = redirectionURL + "Tenant=" + (String) request.getParameter("Tenant");
            if (request.getParameter("RelayState") == null || request.getParameter("RelayState").isEmpty()) 
            	redirectionURL = redirectionURL + "&RelayState=" + DEFAULT_RELAY_STATE;
            else
            	redirectionURL = redirectionURL + "&RelayState=" + (String) request.getParameter("RelayState");
            response.sendRedirect(redirectionURL);
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
        return "Access Control Communication Initiator";
    }
}
