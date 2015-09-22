/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cfg_talla", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgTalla.findAll", query = "SELECT c FROM CfgTalla c"),
    @NamedQuery(name = "CfgTalla.findByIdTalla", query = "SELECT c FROM CfgTalla c WHERE c.idTalla = :idTalla"),
    @NamedQuery(name = "CfgTalla.findByTalla", query = "SELECT c FROM CfgTalla c WHERE c.talla = :talla")})
public class CfgTalla implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_talla", nullable = false)
    private Integer idTalla;
    @Basic(optional = false)
    @Column(name = "talla", nullable = false, length = 45)
    private String talla;
    @OneToMany(mappedBy = "cfgTallaIdTalla")
    private List<CfgProducto> cfgProductoList;

    public CfgTalla() {
    }

    public CfgTalla(Integer idTalla) {
        this.idTalla = idTalla;
    }

    public CfgTalla(Integer idTalla, String talla) {
        this.idTalla = idTalla;
        this.talla = talla;
    }

    public Integer getIdTalla() {
        return idTalla;
    }

    public void setIdTalla(Integer idTalla) {
        this.idTalla = idTalla;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    @XmlTransient
    public List<CfgProducto> getCfgProductoList() {
        return cfgProductoList;
    }

    public void setCfgProductoList(List<CfgProducto> cfgProductoList) {
        this.cfgProductoList = cfgProductoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTalla != null ? idTalla.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgTalla)) {
            return false;
        }
        CfgTalla other = (CfgTalla) object;
        if ((this.idTalla == null && other.idTalla != null) || (this.idTalla != null && !this.idTalla.equals(other.idTalla))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgTalla[ idTalla=" + idTalla + " ]";
    }
    
}
