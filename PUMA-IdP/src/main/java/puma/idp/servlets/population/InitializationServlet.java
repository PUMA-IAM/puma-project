package puma.idp.servlets.population;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import puma.idp.controllers.AttributeController;
import puma.idp.controllers.UserController;
import puma.idp.model.User;

public class InitializationServlet extends HttpServlet {
	private static final Logger logger = Logger.getLogger(InitializationServlet.class.getName());
	private static final long serialVersionUID = 6969012229559227678L;
	private UserController userCtrl;
	private AttributeController attributeCtrl;
	
	public InitializationServlet() {
		this.userCtrl = new UserController();
		this.attributeCtrl = new AttributeController();
	}
	
    private void processRequest(HttpServletRequest request,
			HttpServletResponse response) {
    	try {
	            // Delete all users
	        	this.userCtrl.clearAll();
	            // Create user test
	            User test = this.userCtrl.addUser("test", "changeit");
	            User one = this.userCtrl.addUser("one", "pwOne");
	            User two = this.userCtrl.addUser("two", "pwTwo");
	            User three = this.userCtrl.addUser("three", "pwThree");
	            // Delete all attributes
	            this.attributeCtrl.clearAll();
	            // Create attribute test
	            this.attributeCtrl.addAttribute(test, "E-Mail", "test@IdP.net");
	            this.attributeCtrl.addAttribute(test, "Full Name", "Test Test");
	            this.attributeCtrl.addAttribute(one, "E-Mail", "one.pointone@IdP.net");
	            this.attributeCtrl.addAttribute(one, "Full Name", "One Point-One");
	            this.attributeCtrl.addAttribute(two, "E-Mail", "two-yu@IdP.net");
	            this.attributeCtrl.addAttribute(two, "Full Name", "Two Yu");
	            this.attributeCtrl.addAttribute(three, "E-Mail", "threehougar");
	            this.attributeCtrl.addAttribute(three, "Full Name", "Three Hougar");
	        } catch (SecurityException ex) {
	        	logger.log(Level.SEVERE, null, ex);
	        } catch (IllegalStateException ex) {
	        	logger.log(Level.SEVERE, null, ex);
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
        return "IdP init servlet";
    }    
}
