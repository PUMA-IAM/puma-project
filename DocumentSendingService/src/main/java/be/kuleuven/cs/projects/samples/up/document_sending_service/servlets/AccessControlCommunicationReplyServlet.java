package be.kuleuven.cs.projects.samples.up.document_sending_service.servlets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import be.kuleuven.cs.projects.samples.up.document_sending_service.clients.AccessControlImplService;
import be.kuleuven.cs.projects.samples.up.document_sending_service.clients.AccessControlService;

public class AccessControlCommunicationReplyServlet extends HttpServlet {
	private static final Logger logger = Logger.getLogger(AccessControlCommunicationReplyServlet.class.getName());
	private static final long serialVersionUID = -5681475743424796252L;
	
	@SuppressWarnings("unchecked")
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
		AccessControlImplService impl = new AccessControlImplService();
		AccessControlService service = impl.getAccessControlImplPort();
        // Retrieve user
        String userAlias = (String) request.getParameter("Alias");
        Boolean auth = service.isAuthenticated(userAlias);
        Integer tenantIdentifier = service.getTenant(userAlias);
        out.write("Authenticated: \n\tUser Identifier: " + userAlias + " (Authenticated = " + auth.toString() + ")\n\tTenant Identifer: " + tenantIdentifier.toString() + "\n");
        // Retrieve attributes
        Map<String, List<String>> attributes = null;
        try {
	        byte [] data = Base64.decodeBase64(service.retrieveAttributes(userAlias, "DocumentSendingService").getBytes());
	        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
	        attributes = (Map<String, List<String>>) ois.readObject();
	        ois.close();
		} catch (ClassNotFoundException e) {
			logger.log(Level.INFO, null, e);
		}
        if (attributes != null) {
        	out.write("\nAttributes found:\n");
        	for (String nextKey: attributes.keySet()) {
        		out.write("\t" + nextKey + ":\n");
        		if (attributes.get(nextKey) instanceof List) {
	        		List<String> next = (List<String>) attributes.get(nextKey);
	        		for (String nextValue: next) {
	        			out.write("\t\t\t" + nextValue + "\n");
	        		}
        		} else {
        			out.write("\t\t\t" + attributes.get(nextKey).toString() + "\n");
        		}
        	}
        }
        out.write(".");
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
        return "Access Control Communication Receiver";
    }
}
