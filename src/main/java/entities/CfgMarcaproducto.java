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
@Table(name = "cfg_marcaproducto", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgMarcaproducto.findAll", query = "SELECT c FROM CfgMarcaproducto c"),
    @NamedQuery(name = "CfgMarcaproducto.findByIdMarca", query = "SELECT c FROM CfgMarcaproducto c WHERE c.idMarca = :idMarca"),
    @NamedQuery(name = "CfgMarcaproducto.findByCodigoMarca", query = "SELECT c FROM CfgMarcaproducto c WHERE c.codigoMarca = :codigoMarca"),
    @NamedQuery(name = "CfgMarcaproducto.findByNombreMarca", query = "SELECT c FROM CfgMarcaproducto c WHERE c.nombreMarca = :nombreMarca"),
    @NamedQuery(name = "CfgMarcaproducto.findByFecCrea", query = "SELECT c FROM CfgMarcaproducto c WHERE c.fecCrea = :fecCrea")})
public class CfgMarcaproducto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idMarca", nullable = false)
    private Integer idMarca;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "codigoMarca", nullable = false, length = 10)
    private String codigoMarca;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombreMarca", nullable = false, length = 50)
    private String nombreMarca;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecCrea", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecCrea;
    @JoinColumn(name = "cfg_empresa_idEmpresa", referencedColumnName = "idEmpresa", nullable = false)
    @ManyToOne(optional = false)
    private CfgEmpresa cfgempresaidEmpresa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgmarcaproductoidMarca")
    private List<CfgProducto> cfgProductoList;

    public CfgMarcaproducto() {
    }

    public CfgMarcaproducto(Integer idMarca) {
        this.idMarca = idMarca;
    }

    public CfgMarcaproducto(Integer idMarca, String codigoMarca, String nombreMarca, Date fecCrea) {
        this.idMarca = idMarca;
        this.codigoMarca = codigoMarca;
        this.nombreMarca = nombreMarca;
        this.fecCrea = fecCrea;
    }

    public Integer getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(Integer idMarca) {
        this.idMarca = idMarca;
    }

    public String getCodigoMarca() {
        return codigoMarca;
    }

    public void setCodigoMarca(String codigoMarca) {
        this.codigoMarca = codigoMarca;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
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
        hash += (idMarca != null ? idMarca.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgMarcaproducto)) {
            return false;
        }
        CfgMarcaproducto other = (CfgMarcaproducto) object;
        if ((this.idMarca == null && other.idMarca != null) || (this.idMarca != null && !this.idMarca.equals(other.idMarca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgMarcaproducto[ idMarca=" + idMarca + " ]";
    }

}
