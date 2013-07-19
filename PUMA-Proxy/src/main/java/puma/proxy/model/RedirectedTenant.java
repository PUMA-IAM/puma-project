package puma.proxy.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.GeneratedValue;

@Entity
@NamedQueries({
            @NamedQuery(name = "RedirectedTenant.all", query = "SELECT t FROM RedirectedTenant t"),
            @NamedQuery(name = "RedirectedTenant.byId", query = "SELECT t FROM RedirectedTenant t WHERE t.id = :id"),
            @NamedQuery(name = "RedirectedTenant.byName", query = "SELECT t FROM RedirectedTenant t WHERE t.name = :name")
})
public class RedirectedTenant implements Serializable{
	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String authnHandler;
    private String attrHandler;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAuthnHandler() {
        return this.authnHandler;
    }
    
    public void setAuthnHandler(String authnHandler) {
        this.authnHandler = authnHandler;
    }
    
    public String getAttrHandler() {
        return this.attrHandler;
    }
    
    public void setAttrHandler(String handler) {
        this.attrHandler = handler;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RedirectedTenant)) {
            return false;
        }
        RedirectedTenant other = (RedirectedTenant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getName();
    }   
}
