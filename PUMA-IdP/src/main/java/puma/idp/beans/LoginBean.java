package puma.idp.beans;

import puma.idp.controllers.UserController;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;

/**
 *
 * @author jasper
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {
	private static final Logger logger = Logger.getLogger(LoginBean.class.getName());
	private static final long serialVersionUID = -7821647232556411726L;
	private UserController ctrl;
	private String userName;
    private String passwordAttempt;
    
    public LoginBean() {
    	this.ctrl = new UserController();
    }
    
    public void setUserName(String username) {
        this.userName = username;
    }
    
    public String getUserName() {
        return this.userName;
    }
    
    public void setPasswordAttempt(String attempt) {
        this.passwordAttempt = attempt;
    }
    
    public String getPasswordAttempt() {
        return this.passwordAttempt;
    }
    
	public Boolean logIn() {
        return this.ctrl.checkLogin(this.getUserName(), this.getPasswordAttempt());
    }
    
    public String performLogin() {
        if (this.logIn()) {
            try {
                    FacesContext ctx = FacesContext.getCurrentInstance();
                    ExternalContext ectx = ctx.getExternalContext();
                    HttpServletRequest request = (HttpServletRequest) ectx.getRequest();
                    HttpServletResponse response = (HttpServletResponse) ectx.getResponse();
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/SendResponse");
                    request.getSession().setAttribute("loginTime", new DateTime());
                    request.getSession().setAttribute("UserName", this.getUserName().toString());
                    dispatcher.forward(request, response);
                    ctx.responseComplete();
                } catch (ServletException ex) {
                	logger.log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                	logger.log(Level.SEVERE, null, ex);
                }
        }
        else {
            return "error";
        }
        return "index";
    }
    
    public void create(String name, String password) {
    	this.ctrl.addUser(name, password);
    }
    
    public String add() {
        this.create(userName, passwordAttempt);
        return "index";
    }
}
