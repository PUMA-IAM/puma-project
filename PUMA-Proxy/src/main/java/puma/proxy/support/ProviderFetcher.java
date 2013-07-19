package puma.proxy.support;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import puma.proxy.model.RedirectedTenant;
import puma.proxy.controllers.TenantController;
import puma.util.exceptions.TenantIdentificationException;

public class ProviderFetcher {
    private TenantController tenantCtrl;
    private RedirectedTenant designatedTenant;
    
    public ProviderFetcher(List<String> context) throws TenantIdentificationException {
        this.tenantCtrl = new TenantController();
        this.designatedTenant = this.getTenant(context);
    }
   
    public ProviderFetcher(URL url) throws TenantIdentificationException {
        this.tenantCtrl = new TenantController();
        List<String> hosts = new ArrayList<String>(1);
        hosts.add(url.getHost());
        this.designatedTenant = this.getTenant(hosts);
    }
    
    
    public ProviderFetcher() {
        this.designatedTenant = null;
    }
    
    public RedirectedTenant getDesignatedTenant() {
        return this.designatedTenant;
    }
        
    public String getAttributeRequestHandler() {
        if (this.designatedTenant == null)
            return getDefaultAttributeAddress();    // DEBUG Should be: throw exception
        return this.designatedTenant.getAttrHandler();
    }
    
    public String getAuthenticationRequestHandler() {
        if (this.designatedTenant ==  null)
            return getDefaultAuthenticationAddress();   // DEBUG Should be: throw exception
        return this.designatedTenant.getAuthnHandler();
    }
  
    // TODO Currently, the first name in the chain is taken to determine which IdP to send to. Possibly, a chain of proxies could exist. In this case, a more complicated routing system should be developed.
    private RedirectedTenant getTenant(List<String> list) throws TenantIdentificationException {
    	for (String context: list) {
	        int index = context.indexOf(".");
	        if (index > 0) {
	            String tenantName = context.substring(0, index);
	            RedirectedTenant result = this.tenantCtrl.getTenant(tenantName);
	            if (result != null)
	                return result;
	        }       
    	}
        throw new TenantIdentificationException(list);
    }
    
    private static String getDefaultAttributeAddress() {
        return "";
    }

    private static String getDefaultAuthenticationAddress() {
        return "http://localhost:8080/PUMA-IdP/ProcessAuthenticationRequest"; // TODO Return error page
    }
}
