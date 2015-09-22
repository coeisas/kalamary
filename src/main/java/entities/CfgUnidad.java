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
@Table(name = "cfg_unidad", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgUnidad.findAll", query = "SELECT c FROM CfgUnidad c"),
    @NamedQuery(name = "CfgUnidad.findByIdUnidad", query = "SELECT c FROM CfgUnidad c WHERE c.idUnidad = :idUnidad"),
    @NamedQuery(name = "CfgUnidad.findByUnidad", query = "SELECT c FROM CfgUnidad c WHERE c.unidad = :unidad"),
    @NamedQuery(name = "CfgUnidad.findByAbreviatura", query = "SELECT c FROM CfgUnidad c WHERE c.abreviatura = :abreviatura")})
public class CfgUnidad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_unidad", nullable = false)
    private Integer idUnidad;
    @Basic(optional = false)
    @Column(name = "unidad", nullable = false, length = 45)
    private String unidad;
    @Column(name = "abreviatura", length = 45)
    private String abreviatura;
    @OneToMany(mappedBy = "cfgUnidadIdUnidad")
    private List<CfgProducto> cfgProductoList;

    public CfgUnidad() {
    }

    public CfgUnidad(Integer idUnidad) {
        this.idUnidad = idUnidad;
    }

    public CfgUnidad(Integer idUnidad, String unidad) {
        this.idUnidad = idUnidad;
        this.unidad = unidad;
    }

    public Integer getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(Integer idUnidad) {
        this.idUnidad = idUnidad;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
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
        hash += (idUnidad != null ? idUnidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgUnidad)) {
            return false;
        }
        CfgUnidad other = (CfgUnidad) object;
        if ((this.idUnidad == null && other.idUnidad != null) || (this.idUnidad != null && !this.idUnidad.equals(other.idUnidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgUnidad[ idUnidad=" + idUnidad + " ]";
    }
    
}
