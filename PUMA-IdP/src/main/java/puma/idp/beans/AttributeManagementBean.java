package puma.idp.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import puma.idp.controllers.AttributeController;
import puma.idp.controllers.UserController;
import puma.idp.model.User;

@ManagedBean
@SessionScoped
public class AttributeManagementBean implements Serializable {
	private static final Logger logger = Logger.getLogger(AttributeManagementBean.class.getName());
	private static final long serialVersionUID = 1L;
    private String user;
    private String key;
    private String value;
    private UserController userCtrl;
    private AttributeController attributeCtrl;
    
    public AttributeManagementBean() {
    	this.userCtrl = new UserController();
    	this.attributeCtrl = new AttributeController();
    }
    
    public void setUser(String u) {
        this.user = u;
    }
    
    public String getUser() {
        return this.user;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public List<String> getUserMapping() {
        List<String> result = new ArrayList<String>();
        for (Object u: this.userCtrl.allUsers())
            result.add(((User) u).getName());
        return result;
    }
    
    @SuppressWarnings("finally")
	public String add() {
        try {
            this.attributeCtrl.addAttribute(this.userCtrl.getUser(this.getUser()), this.getKey(), this.getValue());
        } catch (SecurityException ex) {
        	logger.log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            return "index";
        }
    }
}
