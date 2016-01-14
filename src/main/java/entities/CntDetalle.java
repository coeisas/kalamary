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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Mario
 */
@Entity
@Table(name = "cnt_detalle", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CntDetalle.findAll", query = "SELECT c FROM CntDetalle c"),
    @NamedQuery(name = "CntDetalle.findByIdcntDetalle", query = "SELECT c FROM CntDetalle c WHERE c.idcntDetalle = :idcntDetalle"),
    @NamedQuery(name = "CntDetalle.findByNombreCntDetalle", query = "SELECT c FROM CntDetalle c WHERE c.nombreCntDetalle = :nombreCntDetalle")})
public class CntDetalle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idcnt_detalle", nullable = false)
    private Integer idcntDetalle;
    @Basic(optional = false)
    @Column(name = "nombre_cnt_detalle", nullable = false, length = 100)
    private String nombreCntDetalle;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cntDetalle")
    private List<CfgMovCta> cfgMovCtaList;

    public CntDetalle() {
    }

    public CntDetalle(Integer idcntDetalle) {
        this.idcntDetalle = idcntDetalle;
    }

    public CntDetalle(Integer idcntDetalle, String nombreCntDetalle) {
        this.idcntDetalle = idcntDetalle;
        this.nombreCntDetalle = nombreCntDetalle;
    }

    public Integer getIdcntDetalle() {
        return idcntDetalle;
    }

    public void setIdcntDetalle(Integer idcntDetalle) {
        this.idcntDetalle = idcntDetalle;
    }

    public String getNombreCntDetalle() {
        return nombreCntDetalle;
    }

    public void setNombreCntDetalle(String nombreCntDetalle) {
        this.nombreCntDetalle = nombreCntDetalle;
    }

    @XmlTransient
    public List<CfgMovCta> getCfgMovCtaList() {
        return cfgMovCtaList;
    }

    public void setCfgMovCtaList(List<CfgMovCta> cfgMovCtaList) {
        this.cfgMovCtaList = cfgMovCtaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcntDetalle != null ? idcntDetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CntDetalle)) {
            return false;
        }
        CntDetalle other = (CntDetalle) object;
        if ((this.idcntDetalle == null && other.idcntDetalle != null) || (this.idcntDetalle != null && !this.idcntDetalle.equals(other.idcntDetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CntDetalle[ idcntDetalle=" + idcntDetalle + " ]";
    }
    
}
