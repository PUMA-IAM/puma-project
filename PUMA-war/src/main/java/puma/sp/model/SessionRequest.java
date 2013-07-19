/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.model;

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

/**
 *
 * @author jasper
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "SessionRequest.all", query = "SELECT s FROM SessionRequest s"),
	@NamedQuery(name = "SessionRequest.bySessionId", query = "SELECT s FROM SessionRequest s WHERE s.requestId = :id")
	})
public class SessionRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String requestId;
    private String relayState;
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
    
    public Date getGenerationTime() {
        return this.generationTime;
    }
    
    public void setGenerationTime(Date d) {
        this.generationTime = d;
    }
    
    public String getRequestId() {
        return this.requestId;
    }
    
    public void setRequestId(String id) {
        this.requestId = id;
    }
    
    public String getRelayState() {
        return this.relayState;
    }
    
    public void setRelayState(String relayState) {
        this.relayState = relayState;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SessionRequest)) {
            return false;
        }
        SessionRequest other = (SessionRequest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.SessionRequest[ id=" + id + " ]";
    }
    
}