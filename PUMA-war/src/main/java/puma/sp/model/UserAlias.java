package puma.sp.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "APPLICATION_USER_ALIAS")
@NamedQueries({
	@NamedQuery(name = "UserAlias.all", query = "SELECT a FROM UserAlias a"),
	@NamedQuery(name = "UserAlias.byId", query = "SELECT a FROM UserAlias a WHERE a.id = :id"),
	@NamedQuery(name = "UserAlias.byUserTenant", query = "SELECT a FROM UserAlias a WHERE a.userIdentifier = :userId AND a.tenantIdentifier = :tenantId"),
	@NamedQuery(name = "UserAlias.byAlias", query = "SELECT a FROM UserAlias a WHERE a.alias = :alias")
	})
public class UserAlias implements Serializable {
	private static final long serialVersionUID = -6798472142995331817L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	//@Column(unique=true)
	private byte[] alias;
	private String tenantIdentifier;
	private String userIdentifier;
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer identifier) {
		this.id = identifier;
	}
	
	public byte[] getAlias() {
		return this.alias;
	}
	
	public void setAlias(byte[] alias) {
		this.alias = alias;
	}
	
	public String getTenantIdentifier() {
		return this.tenantIdentifier;
	}
	
	public void setTenantIdentifier(String id) {
		this.tenantIdentifier = id;
	}
	
	public String getUserIdentifier() {
		return this.userIdentifier;
	}
	
	public void setUserIdentifier(String id) {
		this.userIdentifier = id;
	}
	
	public Date getTimestamp() {
		return this.timestamp;
	}
	
	public void setTimestamp(Date time) {
		this.timestamp = time;
	}
}
