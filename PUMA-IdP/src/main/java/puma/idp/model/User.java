package puma.idp.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author jasper
 */
@Entity
@Table(name = "IDP_USER")
@NamedQueries({
	@NamedQuery(name = "User.byName", query = "SELECT u FROM User u WHERE u.name = :name"),
	@NamedQuery(name = "User.all", query = "SELECT u FROM User u")
})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private byte[] passwordHash;
    private byte[] passwordSalt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String theName) {
        this.name = theName;
    }
    
    public byte[] getPasswordHash() {
        return this.passwordHash;
    }
    
    public void setPasswordHash(byte[] thePassword) {
        this.passwordHash = thePassword;
    }
    
    public byte[] getPasswordSalt() {
    	return this.passwordSalt;
    }
    
    public void setPasswordSalt(byte[] thePassword) {
    	this.passwordSalt = thePassword;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.User[ id=" + id + " ]";
    }    
}
