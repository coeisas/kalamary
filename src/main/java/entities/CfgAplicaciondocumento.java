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
 * @author mario
 */
@Entity
@Table(name = "cfg_aplicaciondocumento", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgAplicaciondocumento.findAll", query = "SELECT c FROM CfgAplicaciondocumento c"),
    @NamedQuery(name = "CfgAplicaciondocumento.findByIdaplicacion", query = "SELECT c FROM CfgAplicaciondocumento c WHERE c.idaplicacion = :idaplicacion"),
    @NamedQuery(name = "CfgAplicaciondocumento.findByCodaplicacion", query = "SELECT c FROM CfgAplicaciondocumento c WHERE c.codaplicacion = :codaplicacion"),
    @NamedQuery(name = "CfgAplicaciondocumento.findByNomaplicacion", query = "SELECT c FROM CfgAplicaciondocumento c WHERE c.nomaplicacion = :nomaplicacion")})
public class CfgAplicaciondocumento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idaplicacion", nullable = false)
    private Integer idaplicacion;
    @Basic(optional = false)
    @Column(name = "codaplicacion", nullable = false, length = 5)
    private String codaplicacion;
    @Basic(optional = false)
    @Column(name = "nomaplicacion", nullable = false, length = 45)
    private String nomaplicacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgAplicaciondocumento")
    private List<CfgMovCta> cfgMovCtaList;    
    @OneToMany(mappedBy = "cfgAplicaciondocumentoIdaplicacion")
    private List<CfgDocumento> cfgDocumentoList;

    public CfgAplicaciondocumento() {
    }

    public CfgAplicaciondocumento(Integer idaplicacion) {
        this.idaplicacion = idaplicacion;
    }

    public CfgAplicaciondocumento(Integer idaplicacion, String codaplicacion, String nomaplicacion) {
        this.idaplicacion = idaplicacion;
        this.codaplicacion = codaplicacion;
        this.nomaplicacion = nomaplicacion;
    }

    public Integer getIdaplicacion() {
        return idaplicacion;
    }

    public void setIdaplicacion(Integer idaplicacion) {
        this.idaplicacion = idaplicacion;
    }

    public String getCodaplicacion() {
        return codaplicacion;
    }

    public void setCodaplicacion(String codaplicacion) {
        this.codaplicacion = codaplicacion;
    }

    public String getNomaplicacion() {
        return nomaplicacion;
    }

    public void setNomaplicacion(String nomaplicacion) {
        this.nomaplicacion = nomaplicacion;
    }
    
    @XmlTransient
    public List<CfgMovCta> getCfgMovCtaList() {
        return cfgMovCtaList;
    }

    public void setCfgMovCtaList(List<CfgMovCta> cfgMovCtaList) {
        this.cfgMovCtaList = cfgMovCtaList;
    }    

    @XmlTransient
    public List<CfgDocumento> getCfgDocumentoList() {
        return cfgDocumentoList;
    }

    public void setCfgDocumentoList(List<CfgDocumento> cfgDocumentoList) {
        this.cfgDocumentoList = cfgDocumentoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idaplicacion != null ? idaplicacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgAplicaciondocumento)) {
            return false;
        }
        CfgAplicaciondocumento other = (CfgAplicaciondocumento) object;
        if ((this.idaplicacion == null && other.idaplicacion != null) || (this.idaplicacion != null && !this.idaplicacion.equals(other.idaplicacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgAplicaciondocumento[ idaplicacion=" + idaplicacion + " ]";
    }
    
}
