package puma.idp.model;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author jasper
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "UserAttribute.all", query = "SELECT a FROM UserAttribute a"),
	@NamedQuery(name = "UserAttribute.byUser", query = "SELECT a FROM UserAttribute a WHERE a.attributeUser = :user"),
    @NamedQuery(name = "UserAttribute.byKeyUser", query = "SELECT a FROM UserAttribute a WHERE a.attributeUser = :user AND a.attributeKey = :key")
})
public class UserAttribute implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String attributeKey;
    private String attributeValue;
    @ManyToOne
    private User attributeUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getAttributeKey() {
        return this.attributeKey;
    }
    
    public void setAttributeKey(String k) {
        this.attributeKey = k;
    }
    
    public String getAttributeValue() {
        return this.attributeValue;
    }
    
    public void setAttributeValue(String v) {
        this.attributeValue = v;
    }
    
    public User getAttributeUser() {
        return this.attributeUser;
    }
    
    public void setAttributeUser(User u) {
        this.attributeUser = u;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserAttribute)) {
            return false;
        }
        UserAttribute other = (UserAttribute) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.UserAttribute[ id=" + id + " ]";
    }
    
}

