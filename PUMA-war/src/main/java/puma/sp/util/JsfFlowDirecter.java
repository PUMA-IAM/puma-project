package puma.sp.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class JsfFlowDirecter extends FlowDirecter {
	private static final Logger logger = Logger.getLogger(JsfFlowDirecter.class.getName());

	public JsfFlowDirecter(String redirection) {
		super(redirection);
	}

	public void redirect() {
        try {
			ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
	        HttpServletRequest request = (HttpServletRequest) ectx.getRequest();
	        for (String next: super.attributes.keySet()) {
	        	request.getSession().setAttribute(next, super.attributes.get(next));
	        }
			ectx.redirect(super.address);
			return;
		} catch (IOException e) {
			logger.log(Level.SEVERE, null, e);
		}
	}
}
