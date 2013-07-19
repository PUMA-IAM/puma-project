/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author jasper
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "Tenant.all", query = "SELECT t FROM Tenant t"),
	@NamedQuery(name = "Tenant.byId", query = "SELECT t FROM Tenant t WHERE t.id = :id"),
	@NamedQuery(name = "Tenant.byName", query = "SELECT t FROM Tenant t WHERE t.name = :name")//,        
	//@NamedQuery(name = "Tenant.byNameTenant", query = "SELECT u FROM Tenant t JOIN t.users u WHERE u.loginName LIKE :name AND t.name = :tenant")
	})
public class Tenant implements Serializable {
	private static final long serialVersionUID = 746994564247243569L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
    @Column(unique=true)
	private String name;
    private String authnRequestEndpoint;
    private String attrRequestEndpoint;
	private String imageName;
	private Boolean locallyManaged; // For Federated, hierarchical authentication use IdP Proxying (https://spaces.internet2.edu/display/GS/SAMLIdPProxy)
    private String identityProviderPublicKey;
    @ManyToMany 
    private List<Service> subscribedTo;
    @ManyToOne
    private Tenant superTenant;
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<puma.sp.model.User> users;
	
	public Tenant() {}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
        
    public List<User> getUsers() {
        return this.users;
    }
    
    public void setUsers(List<User> users) {
        this.users = users;
    }
	
	public String getAuthnRequestEndpoint() {
		return this.authnRequestEndpoint;
	}
	
	public void setAuthnRequestEndpoint(String redirect) {
		this.authnRequestEndpoint = redirect;
	}
        
    public String getAttrRequestEndpoint() {
        return this.attrRequestEndpoint;
    }
    
    public void setAttrRequestEndpoint(String endpoint) {
        this.attrRequestEndpoint = endpoint;
    }

	public Boolean isLocallyManaged() {
		return this.locallyManaged.booleanValue();
	}
	
	public void setLocallyManaged(Boolean mgmt) {
		this.locallyManaged = mgmt;
	}

	public String getImageName() {
		return this.imageName;
	}
	
	public void setImageName(String name) {
            this.imageName = name;
	}
        
    public String getIdentityProviderPublicKey() {
        return this.identityProviderPublicKey;
    }
    
    public void setIdentityProviderPublicKey(String key) {
        this.identityProviderPublicKey = key;
    }
    
    public List<Service> getSubscribedTo() {
        return this.subscribedTo;
    }
    
    public void setSubscribedTo(List<Service> services) {
        this.subscribedTo = services;
    }
    
    public Tenant getSuperTenant() {
        return this.superTenant;
    }
    
    public void setSuperTenant(Tenant tenant) {
        this.superTenant = tenant;
    }          
    
    public String toHierarchy() {
    	String result = this.getName();
        Tenant current = this.getSuperTenant();
        while (current != null) {
            result = result + "." + current.getName();
            current = current.getSuperTenant();
        }
        return result;
    }
    
    @Override
    public boolean equals(Object o) {
        Tenant other;
        if (o instanceof Tenant) {
            other = (Tenant) o;
            return this.getId() == other.getId();
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
 }
