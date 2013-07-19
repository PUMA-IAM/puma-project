package puma.idp.beans;

import puma.idp.controllers.AttributeController;
import puma.idp.controllers.UserController;
import puma.idp.model.UserAttribute;
import puma.idp.model.User;
import puma.util.exceptions.ElementNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author jasper
 */
@ManagedBean
@SessionScoped
public class AttributeViewerBean implements Serializable {
	private static final long serialVersionUID = -437022121083098820L;
    private String user;
    private AttributeController attributeCtrl;
    private UserController userCtrl;
    
    public AttributeViewerBean() {
    	this.attributeCtrl = new AttributeController();
    	this.userCtrl = new UserController();
    }
    
    public void setUser(String u) {
        this.user = u;
    }
    
    public String getUser() {
        return this.user;
    }
    
    public List<String> getUserMapping() {
        List<String> result = new ArrayList<String>();
        for (Object u: this.userCtrl.allUsers())
            result.add(((User) u).getName());
        return result;
    }
    
	public List<UserAttribute> getList() throws ElementNotFoundException {
    	return this.attributeCtrl.getAttributes(this.userObject());
    }

	private User userObject() throws ElementNotFoundException {
        return this.userCtrl.getUser(this.user);
    }
    
    public String view() {
        return "viewattributes2";
    }
}
