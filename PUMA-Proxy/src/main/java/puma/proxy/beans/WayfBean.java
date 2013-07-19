package puma.proxy.beans;

import java.io.Serializable;
import puma.proxy.controllers.TenantController;
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
import puma.proxy.model.RedirectedTenant;

public class WayfBean implements Serializable {
	private static final Logger logger = Logger.getLogger(WayfBean.class.getName());
	private static final long serialVersionUID = 880738534911885424L;
	private TenantController ctrl;
	private Long chosenTenant;
	
	public Map<String, Long> getTenantListing() {
        Map<String, Long> result = new HashMap<String, Long>();
        for (RedirectedTenant t: this.ctrl.getAllTenants()) {
                result.put(t.getName(), t.getId());
        }
        return result;
}
    
    public void setChosenTenant(Long id) {
        this.chosenTenant = id;
    }
    
    public Long getChosenTenant() {
        return this.chosenTenant;
    }
    
    public String next() {
        try {
                // Add chosen tenant to session
                FacesContext ctx = FacesContext.getCurrentInstance();
                ExternalContext ectx = ctx.getExternalContext();
                HttpServletRequest request = (HttpServletRequest) ectx.getRequest();
                HttpServletResponse response = (HttpServletResponse) ectx.getResponse();
                RequestDispatcher dispatcher = request.getRequestDispatcher("/SAMLReqEndpoint");   // TODO appropriate servlet here                     
                request.getSession().setAttribute("Tenant", this.ctrl.getTenant(this.getChosenTenant()));
                dispatcher.forward(request, response);
                ctx.responseComplete();
            } catch (ServletException ex) {
            	logger.log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            	logger.log(Level.SEVERE, null, ex);
            }        
        // Redirect to error page        
        return "error";
    }
}
