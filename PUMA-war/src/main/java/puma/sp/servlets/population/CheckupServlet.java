/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.servlets.population;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import puma.sp.controllers.authentication.WAYFController;
import puma.sp.controllers.management.ServiceManagementController;
import puma.sp.controllers.management.UserManagementController;
import puma.sp.model.Service;
import puma.sp.model.Tenant;
import puma.sp.model.User;

/**
 *
 * @author jasper
 */
public class CheckupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckupServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		WAYFController tenantMgmt = new WAYFController();
                    UserManagementController userMgmt = new UserManagementController();
                    ServiceManagementController serviceMgmt = new ServiceManagementController();
                    String output = "Available tenants:\n";
		for (Tenant t: tenantMgmt.getAllTenants()) {
                        output = output + t.getName() + "\n";
                    }
                    output += "Users:\n";
		for (User u: userMgmt.getUsers()) {
                        output = output + u.getLoginName() + "\n";
                    }
                    output += "Services:\n";
		for (Service s: serviceMgmt.getServices()) {
                        output = output + s.getName() + "\n";
                    }
                    output += ".";
		response.getOutputStream().write(output.getBytes());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}