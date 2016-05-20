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
@Table(name = "cfg_categoriaproducto", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgCategoriaproducto.findAll", query = "SELECT c FROM CfgCategoriaproducto c"),
    @NamedQuery(name = "CfgCategoriaproducto.findByIdCategoria", query = "SELECT c FROM CfgCategoriaproducto c WHERE c.idCategoria = :idCategoria"),
    @NamedQuery(name = "CfgCategoriaproducto.findByCodigoCategoria", query = "SELECT c FROM CfgCategoriaproducto c WHERE c.codigoCategoria = :codigoCategoria"),
    @NamedQuery(name = "CfgCategoriaproducto.findByNombreCategoria", query = "SELECT c FROM CfgCategoriaproducto c WHERE c.nombreCategoria = :nombreCategoria"),
    @NamedQuery(name = "CfgCategoriaproducto.findByFecCrea", query = "SELECT c FROM CfgCategoriaproducto c WHERE c.fecCrea = :fecCrea")})
public class CfgCategoriaproducto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCategoria", nullable = false)
    private Integer idCategoria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codigoCategoria", nullable = false, length = 5)
    private String codigoCategoria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombreCategoria", nullable = false, length = 50)
    private String nombreCategoria;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecCrea", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecCrea;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgcategoriaproductoidCategoria")
    private List<CfgProducto> cfgProductoList;    
    @JoinColumn(name = "cfg_empresa_idEmpresa", referencedColumnName = "idEmpresa", nullable = false)
    @ManyToOne(optional = false)
    private CfgEmpresa cfgempresaidEmpresa;
    
    public CfgCategoriaproducto() {
    }

    public CfgCategoriaproducto(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public CfgCategoriaproducto(Integer idCategoria, String codigoCategoria, String nombreCategoria, Date fecCrea) {
        this.idCategoria = idCategoria;
        this.codigoCategoria = codigoCategoria;
        this.nombreCategoria = nombreCategoria;
        this.fecCrea = fecCrea;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(String codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public Date getFecCrea() {
        return fecCrea;
    }

    public void setFecCrea(Date fecCrea) {
        this.fecCrea = fecCrea;
    }
    
    @XmlTransient
    public List<CfgProducto> getCfgProductoList() {
        return cfgProductoList;
    }

    public void setCfgProductoList(List<CfgProducto> cfgProductoList) {
        this.cfgProductoList = cfgProductoList;
    }    
   
    public CfgEmpresa getCfgempresaidEmpresa() {
        return cfgempresaidEmpresa;
    }

    public void setCfgempresaidEmpresa(CfgEmpresa cfgempresaidEmpresa) {
        this.cfgempresaidEmpresa = cfgempresaidEmpresa;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCategoria != null ? idCategoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgCategoriaproducto)) {
            return false;
        }
        CfgCategoriaproducto other = (CfgCategoriaproducto) object;
        if ((this.idCategoria == null && other.idCategoria != null) || (this.idCategoria != null && !this.idCategoria.equals(other.idCategoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgCategoriaproducto[ idCategoria=" + idCategoria + " ]";
    }
    
}
