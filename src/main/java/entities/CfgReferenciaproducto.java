/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cfg_referenciaproducto", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgReferenciaproducto.findAll", query = "SELECT c FROM CfgReferenciaproducto c"),
    @NamedQuery(name = "CfgReferenciaproducto.findByIdReferencia", query = "SELECT c FROM CfgReferenciaproducto c WHERE c.idReferencia = :idReferencia"),
    @NamedQuery(name = "CfgReferenciaproducto.findByCodigoReferencia", query = "SELECT c FROM CfgReferenciaproducto c WHERE c.codigoReferencia = :codigoReferencia"),
    @NamedQuery(name = "CfgReferenciaproducto.findByNombreReferencia", query = "SELECT c FROM CfgReferenciaproducto c WHERE c.nombreReferencia = :nombreReferencia"),
    @NamedQuery(name = "CfgReferenciaproducto.findByFecCrea", query = "SELECT c FROM CfgReferenciaproducto c WHERE c.fecCrea = :fecCrea")})
public class CfgReferenciaproducto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idReferencia", nullable = false)
    private Integer idReferencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codigoReferencia", nullable = false, length = 5)
    private String codigoReferencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombreReferencia", nullable = false, length = 50)
    private String nombreReferencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecCrea", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecCrea;
    @JoinColumn(name = "cfg_empresa_idEmpresa", referencedColumnName = "idEmpresa", nullable = false)
    @ManyToOne(optional = false)
    private CfgEmpresa cfgempresaidEmpresa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgreferenciaproductoidReferencia")
    private List<CfgProducto> cfgProductoList;

    public CfgReferenciaproducto() {
    }

    public CfgReferenciaproducto(Integer idReferencia) {
        this.idReferencia = idReferencia;
    }

    public CfgReferenciaproducto(Integer idReferencia, String codigoReferencia, String nombreReferencia, Date fecCrea) {
        this.idReferencia = idReferencia;
        this.codigoReferencia = codigoReferencia;
        this.nombreReferencia = nombreReferencia;
        this.fecCrea = fecCrea;
    }

    public Integer getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(Integer idReferencia) {
        this.idReferencia = idReferencia;
    }

    public String getCodigoReferencia() {
        return codigoReferencia;
    }

    public void setCodigoReferencia(String codigoReferencia) {
        this.codigoReferencia = codigoReferencia;
    }

    public String getNombreReferencia() {
        return nombreReferencia;
    }

    public void setNombreReferencia(String nombreReferencia) {
        this.nombreReferencia = nombreReferencia;
    }

    public Date getFecCrea() {
        return fecCrea;
    }

    public void setFecCrea(Date fecCrea) {
        this.fecCrea = fecCrea;
    }

    public CfgEmpresa getCfgempresaidEmpresa() {
        return cfgempresaidEmpresa;
    }

    public void setCfgempresaidEmpresa(CfgEmpresa cfgempresaidEmpresa) {
        this.cfgempresaidEmpresa = cfgempresaidEmpresa;
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
        hash += (idReferencia != null ? idReferencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgReferenciaproducto)) {
            return false;
        }
        CfgReferenciaproducto other = (CfgReferenciaproducto) object;
        if ((this.idReferencia == null && other.idReferencia != null) || (this.idReferencia != null && !this.idReferencia.equals(other.idReferencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgReferenciaproducto[ idReferencia=" + idReferencia + " ]";
    }

}
