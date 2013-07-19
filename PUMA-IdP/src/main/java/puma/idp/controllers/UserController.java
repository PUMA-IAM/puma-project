package puma.idp.controllers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import puma.idp.model.User;
import puma.idp.util.IdPNamingProvider;
import puma.util.PasswordHasher;
import puma.util.exceptions.ElementNotFoundException;
import puma.util.persistence.TransactionalWriter;

public class UserController {
	private static final Logger logger = Logger.getLogger(UserController.class.getName());
    private EntityManager em;
    private TransactionalWriter writer;
	
	public UserController() {
    	IdPNamingProvider provider = new IdPNamingProvider();
        this.em = provider.produce();
        this.writer = new TransactionalWriter(provider);
	}
	
	public User addUser(String name, String password) {
		byte[] salt = PasswordHasher.generateSalt();
        User u = new User();
        u.setName(name);
        u.setPasswordSalt(salt);
        u.setPasswordHash(PasswordHasher.getHashValue(password, salt));
        try {
            this.writer.write(User.class, u);
            return u;
        } catch (SecurityException ex) {
        	logger.log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
        	logger.log(Level.SEVERE, null, ex);
        } 
        return null;
	}

	public User getUser(String userName) throws ElementNotFoundException {
		Query q = this.em.createNamedQuery("User.byName");
		@SuppressWarnings("unchecked")
		List<User> values = q.setParameter("name", userName).getResultList();
		if (values.isEmpty())
			throw new ElementNotFoundException(userName);
		return values.get(0);
	}
	
	public boolean checkLogin(String userName, String passwordAttempt) {
		try {
            Query q = this.em.createNamedQuery("User.byName");
            @SuppressWarnings("unchecked")
			List<User> result = q.setParameter("name", userName.toString()).getResultList();
            if (result.isEmpty())
                return false;
            if (result.size() > 1)
                return false;
            User theUser = result.get(0);
            byte[] theHash = PasswordHasher.getHashValue(passwordAttempt, theUser.getPasswordSalt());
            if (PasswordHasher.equalHash(theUser.getPasswordHash(), theHash)) {
                return true;
            }
            return false;
        } catch (SecurityException ex) {
        	logger.log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
        	logger.log(Level.SEVERE, null, ex);
        }
        return false;
	}

	@SuppressWarnings("unchecked")
	public List<User> allUsers() {
		Query q = this.em.createNamedQuery("User.all");
		return q.getResultList();
	}

	public void remove(User u) {
		this.writer.remove(User.class, u);
	}

	public void clearAll() {
        this.writer.customQuery("DELETE FROM User u");
    }
}
