/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cfg_rol", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgRol.findAll", query = "SELECT c FROM CfgRol c"),
    @NamedQuery(name = "CfgRol.findByIdrol", query = "SELECT c FROM CfgRol c WHERE c.idrol = :idrol"),
    @NamedQuery(name = "CfgRol.findByCodrol", query = "SELECT c FROM CfgRol c WHERE c.codrol = :codrol"),
    @NamedQuery(name = "CfgRol.findByNomrol", query = "SELECT c FROM CfgRol c WHERE c.nomrol = :nomrol")})
public class CfgRol implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idrol", nullable = false)
    private Integer idrol;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codrol", nullable = false, length = 5)
    private String codrol;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nomrol", nullable = false, length = 20)
    private String nomrol;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgRolIdrol")
    private List<SegUsuario> segUsuarioList;

    public CfgRol() {
    }

    public CfgRol(Integer idrol) {
        this.idrol = idrol;
    }

    public CfgRol(Integer idrol, String codrol, String nomrol) {
        this.idrol = idrol;
        this.codrol = codrol;
        this.nomrol = nomrol;
    }

    public Integer getIdrol() {
        return idrol;
    }

    public void setIdrol(Integer idrol) {
        this.idrol = idrol;
    }

    public String getCodrol() {
        return codrol;
    }

    public void setCodrol(String codrol) {
        this.codrol = codrol;
    }

    public String getNomrol() {
        return nomrol;
    }

    public void setNomrol(String nomrol) {
        this.nomrol = nomrol;
    }

    @XmlTransient
    public List<SegUsuario> getSegUsuarioList() {
        return segUsuarioList;
    }

    public void setSegUsuarioList(List<SegUsuario> segUsuarioList) {
        this.segUsuarioList = segUsuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idrol != null ? idrol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgRol)) {
            return false;
        }
        CfgRol other = (CfgRol) object;
        if ((this.idrol == null && other.idrol != null) || (this.idrol != null && !this.idrol.equals(other.idrol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgRol[ idrol=" + idrol + " ]";
    }
    
}
