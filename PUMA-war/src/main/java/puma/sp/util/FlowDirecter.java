package puma.sp.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FlowDirecter {
	private static final Logger logger = Logger.getLogger(FlowDirecter.class.getName());
	protected String address;
	protected Map<String, Object> attributes;
	
	public FlowDirecter(String redirection) {
		this.address = redirection;
		this.attributes = new HashMap<String, Object>();
	}
	
	public void redirect() {
        try {
			FacesContext ctx = FacesContext.getCurrentInstance();
	        ExternalContext ectx = ctx.getExternalContext();
	        HttpServletRequest request = (HttpServletRequest) ectx.getRequest();
	        HttpServletResponse response = (HttpServletResponse) ectx.getResponse();
	        RequestDispatcher dispatcher = request.getRequestDispatcher(this.address);
	        for (String next: this.attributes.keySet()) {
	        	request.getSession().setAttribute(next, this.attributes.get(next));
	        }
			dispatcher.forward(request, response);
	        ctx.responseComplete();
		} catch (ServletException e) {
			logger.log(Level.SEVERE, null, e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, null, e);
		}
	}
	
	public void addAttribute(String name, Object attribute) {
		this.attributes.put(name, attribute);
	}

}
