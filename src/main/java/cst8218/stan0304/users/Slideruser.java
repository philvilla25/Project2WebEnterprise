/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cst8218.stan0304.users;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author thpst
 */
@Entity
@Table(name = "SLIDERUSER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Slideruser.findAll", query = "SELECT s FROM Slideruser s"),
    @NamedQuery(name = "Slideruser.findById", query = "SELECT s FROM Slideruser s WHERE s.id = :id"),
    @NamedQuery(name = "Slideruser.findByUserid", query = "SELECT s FROM Slideruser s WHERE s.userid = :userid"),
    @NamedQuery(name = "Slideruser.findByPassword", query = "SELECT s FROM Slideruser s WHERE s.password = :password"),
    @NamedQuery(name = "Slideruser.findByGroupname", query = "SELECT s FROM Slideruser s WHERE s.groupname = :groupname")})
public class Slideruser implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Double id;
    @Size(max = 50)
    @Column(name = "USERID")
    private String userid;
    @Size(max = 500)
    @Column(name = "PASSWORD")
    private String password;
    @Size(max = 50)
    @Column(name = "GROUPNAME")
    private String groupname;

    public Slideruser() {
    }

    public Slideruser(Double id) {
        this.id = id;
    }

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Slideruser)) {
            return false;
        }
        Slideruser other = (Slideruser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cst8218.stan0304.users.Slideruser[ id=" + id + " ]";
    }
    
}
