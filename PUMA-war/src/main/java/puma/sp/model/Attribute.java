/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author jasper
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Attribute.all", query = "SELECT a FROM Attribute a"),
	@NamedQuery(name = "Attribute.byKey", query = "SELECT a FROM Attribute a WHERE a.attributeKey = :key"),
	@NamedQuery(name = "Attribute.byUser", query = "SELECT a FROM Attribute a WHERE a.user = :user"),
	@NamedQuery(name = "Attribute.byKeyUser", query = "SELECT a FROM Attribute a WHERE a.attributeKey = :key AND a.user = :user")
	})
@Table(name = "SP_ATTR")
public class Attribute implements Serializable {
	private static final long serialVersionUID = 1320207421079676078L;
	// TODO Implement class hierarchy with specification to entity field.
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private AttributeType attributeKey;
	private Serializable attributeValue;
	@ManyToOne
	private User user;
	
	public Attribute() {}
	
	public Integer getId() {
		return this.id;
	}
	
    public void setAttributeKey(AttributeType key) {
        this.attributeKey = key;
    }
        
	public AttributeType getAttributeKey() {
		return this.attributeKey;
	}
        
    public void setAttributeValue(Serializable value) {
        this.attributeValue = value;
    }
	
	public Serializable getAttributeValue() {
		return this.attributeValue;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void setUser(User u) {
		this.user = u;
	}
}