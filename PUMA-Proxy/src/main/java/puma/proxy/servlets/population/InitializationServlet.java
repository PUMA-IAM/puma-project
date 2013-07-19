package puma.proxy.servlets.population;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import puma.proxy.controllers.TenantController;

public class InitializationServlet extends HttpServlet {
	private static final long serialVersionUID = -5423604172809509217L;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TenantController controller = new TenantController();
		PrintWriter out = response.getWriter();
		controller.addTenant("Cynalco Medics Research", "http://localhost:8080/PUMA-IdP/ProcessAuthenticationRequest", "http://localhost:8080/PUMA-IdP/Query");
		controller.addTenant("Reuters Europe", "http://localhost:8080/PUMA-Proxy/SAMLReqEndpoint", "http://localhost:8080/PUMA-Proxy/PUMA-Proxy/Query");
		controller.addTenant("Reuters Europe Belgium", "http://localhost:8080/PUMA-IdP/ProcessAuthenticationRequest", "http://localhost:8080/PUMA-IdP/Query");
		out.write("Generated tenants");
	}
	
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
