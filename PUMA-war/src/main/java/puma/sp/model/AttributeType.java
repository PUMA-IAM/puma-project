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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author jasper
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "AttributeType.all", query = "SELECT a FROM AttributeType a"),
	@NamedQuery(name = "AttributeType.byId", query = "SELECT a FROM AttributeType a WHERE a.id = :id"),
	@NamedQuery(name = "AttributeType.byName", query = "SELECT a FROM AttributeType a WHERE a.name = :name")
	})
@Table(name = "SP_ATTRTYPE")
public class AttributeType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    String name;

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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AttributeType)) {
            return false;
        }
        AttributeType other = (AttributeType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.AttributeType[ id=" + id + " ]";
    }
    
}