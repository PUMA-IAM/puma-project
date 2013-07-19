package puma.proxy.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
        @NamedQuery(name = "SAMLMessageHeaderContext.all", query = "SELECT m FROM SAMLMessageHeaderContext m"),
	@NamedQuery(name = "SAMLMessageHeaderContext.byProxiedId", query = "SELECT m FROM SAMLMessageHeaderContext m WHERE m.proxiedSAMLIdentifier LIKE :id") 
	})
public class SAMLMessageHeaderContext implements Serializable {
	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(unique=true)
    private String originalSAMLIdentifier;
    @Column(unique=true)
    private String proxiedSAMLIdentifier;
    private String assertionConsumerServiceURL;
    @Basic(optional = false)
    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date generationTime; 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getOriginalSAMLIdentifier() {
        return this.originalSAMLIdentifier;
    }
    
    public void setOriginalSAMLIdentifier(String identifier) {
        this.originalSAMLIdentifier = identifier;
    }
    
    public String getProxiedSAMLIdentifier() {
        return this.proxiedSAMLIdentifier;
    }
    
    public void setProxiedSAMLIdentifier(String identifier) {
        this.proxiedSAMLIdentifier = identifier;
    }
    
    public String getAssertionConsumerServiceURL() {
        return this.assertionConsumerServiceURL;
    }
    
    public void setAssertionConsumerServiceURL(String url) {
        this.assertionConsumerServiceURL = url;
    }
    
    public void setGenerationTime(Date date) {
        this.generationTime = date;
    }
    
    public Date getGenerationTime() {
        return this.generationTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SAMLMessageHeaderContext)) {
            return false;
        }
        SAMLMessageHeaderContext other = (SAMLMessageHeaderContext) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.SAMLMessageHeaderContext[ id=" + id + " ]";
    }
}
