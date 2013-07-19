/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.servlets.population;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import puma.sp.controllers.management.AttributeManagementController;
import puma.sp.controllers.management.ServiceManagementController;
import puma.sp.controllers.management.TenantManagementController;
import puma.sp.controllers.management.UserManagementController;
import puma.sp.model.AttributeType;
import puma.sp.model.Tenant;
import puma.sp.model.User;

/**
 *
 * @author jasper
 */

public class InitializationServlet extends HttpServlet {
	private static final Logger logger = Logger.getLogger(InitializationServlet.class.getName());
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InitializationServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            PrintWriter out = response.getWriter();    
            try {
                    UserManagementController userMgmt = new UserManagementController(); //(UserManagementController) context.lookup("UserManagementService"); //!management.UserManagementBean");
                    TenantManagementController tenantMgmt = new TenantManagementController(); //(TenantManagementController) context.lookup("TenantManagementService"); //!management.TenantManagementFacade");
                    ServiceManagementController serviceMgmt = new ServiceManagementController(); //(ServiceManagementController) context.lookup("ServiceManagementService");
                    AttributeManagementController attributeMgmt = new AttributeManagementController();
                    // Remove all
                    userMgmt.clearAll();
                    tenantMgmt.clearAll();
                    serviceMgmt.clearAll();
                    out.write("Initialization: cleared all data");

                    // Create all
                    Tenant vermaelenProjects = tenantMgmt.addTenant("Vermaelen Projects", "vermaelen.jpg", null);
                    Tenant cynalcoMedics = tenantMgmt.addTenant("Cynalco Medics", "cynalco.jpg", Boolean.FALSE, "http://localhost:8080/PUMA-IdP/ProcessAuthenticationRequest", "http://localhost:8080/PUMA-IdP/AttributeQueryService?Query", "", null);
                    tenantMgmt.addTenant("Cynalco Medics Research", "cynalco.jpg", Boolean.FALSE, "http://localhost:8080/PUMA-Proxy/SAMLReqEndpoint", "http://localhost:8080/PUMA-Proxy/PUMA-Proxy/Query?Send", "", cynalcoMedics.getId());
                    tenantMgmt.addTenant("Cynalco Medics Accounting", "cynalco.jpg", Boolean.FALSE, null, null, "", cynalcoMedics.getId());
                    Tenant customerService = tenantMgmt.addTenant("Cynalco Medics Customer Service", "cynalco.jpg", cynalcoMedics.getId());
                    User jos = userMgmt.addUser("Jos", "pwOne", vermaelenProjects);
                    User thomas = userMgmt.addUser("Thomas", "pwTwo", vermaelenProjects);
                    User alain = userMgmt.addUser("Alain", "Alain", customerService);
                    User admin = userMgmt.addUser("admin", "admin", null);
                    AttributeType email = serviceMgmt.createAttributeType("E-Mail");
                    AttributeType fullName = serviceMgmt.createAttributeType("Full Name");
                    AttributeType role = serviceMgmt.createAttributeType("MiddlewareRole");
                    Set<String> types = new HashSet<String>(1);
                    types.add(email.getName());
                    serviceMgmt.createService("DocumentSendingService", types);
                    types.add(fullName.getName());
                    serviceMgmt.createService("TemplateUploadService", types);
                    serviceMgmt.createService("DocumentReadingService", new HashSet<String>());
                    attributeMgmt.addAttribute(email, "jos@vermaelenprojects.be", jos);
                    attributeMgmt.addAttribute(fullName, "Jos Vermaelen", jos);
                    attributeMgmt.addAttribute(email, "thomas@vermaelen.be", thomas);
                    attributeMgmt.addAttribute(fullName, "Thomas Vermaelen", thomas);
                    attributeMgmt.addAttribute(email, "alain.vandam@cynalcomedics.be", alain);
                    attributeMgmt.addAttribute(fullName, "Alain Vandam", alain);
                    attributeMgmt.addAttribute(role, "MiddlewareAdmin", admin);
                    out.write("Initialization: created all data");
            } catch (Exception e) {                        
            	logger.log(Level.SEVERE, null, e);
            }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
