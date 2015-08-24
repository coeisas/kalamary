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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cfg_producto", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgProducto.findAll", query = "SELECT c FROM CfgProducto c"),
    @NamedQuery(name = "CfgProducto.findByIdProducto", query = "SELECT c FROM CfgProducto c WHERE c.idProducto = :idProducto"),
    @NamedQuery(name = "CfgProducto.findByCodProducto", query = "SELECT c FROM CfgProducto c WHERE c.codProducto = :codProducto"),
    @NamedQuery(name = "CfgProducto.findByCodBarProducto", query = "SELECT c FROM CfgProducto c WHERE c.codBarProducto = :codBarProducto"),
    @NamedQuery(name = "CfgProducto.findByNomProducto", query = "SELECT c FROM CfgProducto c WHERE c.nomProducto = :nomProducto"),
    @NamedQuery(name = "CfgProducto.findByIdUnidad", query = "SELECT c FROM CfgProducto c WHERE c.idUnidad = :idUnidad"),
    @NamedQuery(name = "CfgProducto.findByStockMin", query = "SELECT c FROM CfgProducto c WHERE c.stockMin = :stockMin"),
    @NamedQuery(name = "CfgProducto.findByCostoAdquisicion", query = "SELECT c FROM CfgProducto c WHERE c.costoAdquisicion = :costoAdquisicion"),
    @NamedQuery(name = "CfgProducto.findByIva", query = "SELECT c FROM CfgProducto c WHERE c.iva = :iva"),
    @NamedQuery(name = "CfgProducto.findByFlete", query = "SELECT c FROM CfgProducto c WHERE c.flete = :flete"),
    @NamedQuery(name = "CfgProducto.findByCostoIndirecto", query = "SELECT c FROM CfgProducto c WHERE c.costoIndirecto = :costoIndirecto"),
    @NamedQuery(name = "CfgProducto.findByCosto", query = "SELECT c FROM CfgProducto c WHERE c.costo = :costo"),
    @NamedQuery(name = "CfgProducto.findByUtilidad", query = "SELECT c FROM CfgProducto c WHERE c.utilidad = :utilidad"),
    @NamedQuery(name = "CfgProducto.findByPrecio", query = "SELECT c FROM CfgProducto c WHERE c.precio = :precio"),
    @NamedQuery(name = "CfgProducto.findByActivo", query = "SELECT c FROM CfgProducto c WHERE c.activo = :activo"),
    @NamedQuery(name = "CfgProducto.findByFecCrea", query = "SELECT c FROM CfgProducto c WHERE c.fecCrea = :fecCrea")})
public class CfgProducto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idProducto", nullable = false)
    private Integer idProducto;
    @Size(max = 45)
    @Column(name = "codProducto", length = 45)
    private String codProducto;
    @Size(max = 45)
    @Column(name = "codBarProducto", length = 45)
    private String codBarProducto;
    @Size(max = 400)
    @Column(name = "nomProducto", length = 400)
    private String nomProducto;
    @Column(name = "idUnidad")
    private Integer idUnidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "stockMin", precision = 12)
    private Float stockMin;
    @Column(name = "costoAdquisicion", precision = 12)
    private Float costoAdquisicion;
    @Column(name = "iva", precision = 12)
    private Float iva;
    @Column(name = "flete", precision = 12)
    private Float flete;
    @Column(name = "costoIndirecto", precision = 12)
    private Float costoIndirecto;
    @Column(name = "costo", precision = 12)
    private Float costo;
    @Column(name = "utilidad", precision = 12)
    private Float utilidad;
    @Column(name = "precio", precision = 12)
    private Float precio;
    @Lob
    @Column(name = "imgProducto")
    private byte[] imgProducto;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "fecCrea")
    @Temporal(TemporalType.DATE)
    private Date fecCrea;
    @Lob
    @Column(name = "color", length = 65535)
    private String color;
    @Lob
    @Column(name = "talla", length = 65535)
    private String talla;
    @Basic(optional = false)
    @Column(name = "esServicio", nullable = false)
    private boolean esServicio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgProducto")
    private List<InvMovimientoDetalle> invMovimientoDetalleList;    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgProducto")
    private List<InvConsolidado> invConsolidadoList;    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgProducto")
    private List<CfgKitproductodetalle> cfgKitproductodetalleList;
    @JoinColumn(name = "cfg_empresa_idEmpresa", referencedColumnName = "idEmpresa", nullable = false)
    @ManyToOne(optional = false)
    private CfgEmpresa cfgempresaidEmpresa;
    @JoinColumn(name = "cfg_marcaproducto_idMarca", referencedColumnName = "idMarca", nullable = false)
    @ManyToOne(optional = false)
    private CfgMarcaproducto cfgmarcaproductoidMarca;
    @JoinColumn(name = "seg_usuario_idUsuario", referencedColumnName = "idUsuario", nullable = false)
    @ManyToOne(optional = false)
    private SegUsuario segusuarioidUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgProducto")
    private List<FacDocumentodetalle> facDocumentodetalleList;

    public CfgProducto() {
    }

    public CfgProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(String codProducto) {
        this.codProducto = codProducto;
    }

    public String getCodBarProducto() {
        return codBarProducto;
    }

    public void setCodBarProducto(String codBarProducto) {
        this.codBarProducto = codBarProducto;
    }

    public String getNomProducto() {
        return nomProducto;
    }

    public void setNomProducto(String nomProducto) {
        this.nomProducto = nomProducto;
    }

    public Integer getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(Integer idUnidad) {
        this.idUnidad = idUnidad;
    }

    public Float getStockMin() {
        return stockMin;
    }

    public void setStockMin(Float stockMin) {
        this.stockMin = stockMin;
    }

    public Float getCostoAdquisicion() {
        return costoAdquisicion;
    }

    public void setCostoAdquisicion(Float costoAdquisicion) {
        this.costoAdquisicion = costoAdquisicion;
    }

    public Float getIva() {
        return iva;
    }

    public void setIva(Float iva) {
        this.iva = iva;
    }

    public Float getFlete() {
        return flete;
    }

    public void setFlete(Float flete) {
        this.flete = flete;
    }

    public Float getCostoIndirecto() {
        return costoIndirecto;
    }

    public void setCostoIndirecto(Float costoIndirecto) {
        this.costoIndirecto = costoIndirecto;
    }

    public Float getCosto() {
        return costo;
    }

    public void setCosto(Float costo) {
        this.costo = costo;
    }

    public Float getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(Float utilidad) {
        this.utilidad = utilidad;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public byte[] getImgProducto() {
        return imgProducto;
    }

    public void setImgProducto(byte[] imgProducto) {
        this.imgProducto = imgProducto;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Date getFecCrea() {
        return fecCrea;
    }

    public void setFecCrea(Date fecCrea) {
        this.fecCrea = fecCrea;
    }
    
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }
    
    public boolean getEsServicio() {
        return esServicio;
    }

    public void setEsServicio(boolean esServicio) {
        this.esServicio = esServicio;
    }

    @XmlTransient
    public List<InvMovimientoDetalle> getInvMovimientoDetalleList() {
        return invMovimientoDetalleList;
    }

    public void setInvMovimientoDetalleList(List<InvMovimientoDetalle> invMovimientoDetalleList) {
        this.invMovimientoDetalleList = invMovimientoDetalleList;
    }

    @XmlTransient
    public List<InvConsolidado> getInvConsolidadoList() {
        return invConsolidadoList;
    }

    public void setInvConsolidadoList(List<InvConsolidado> invConsolidadoList) {
        this.invConsolidadoList = invConsolidadoList;
    }
    
    @XmlTransient
    public List<CfgKitproductodetalle> getCfgKitproductodetalleList() {
        return cfgKitproductodetalleList;
    }

    public void setCfgKitproductodetalleList(List<CfgKitproductodetalle> cfgKitproductodetalleList) {
        this.cfgKitproductodetalleList = cfgKitproductodetalleList;
    }

    public CfgEmpresa getCfgempresaidEmpresa() {
        return cfgempresaidEmpresa;
    }

    public void setCfgempresaidEmpresa(CfgEmpresa cfgempresaidEmpresa) {
        this.cfgempresaidEmpresa = cfgempresaidEmpresa;
    }

    public CfgMarcaproducto getCfgmarcaproductoidMarca() {
        return cfgmarcaproductoidMarca;
    }

    public void setCfgmarcaproductoidMarca(CfgMarcaproducto cfgmarcaproductoidMarca) {
        this.cfgmarcaproductoidMarca = cfgmarcaproductoidMarca;
    }

    public SegUsuario getSegusuarioidUsuario() {
        return segusuarioidUsuario;
    }

    public void setSegusuarioidUsuario(SegUsuario segusuarioidUsuario) {
        this.segusuarioidUsuario = segusuarioidUsuario;
    }

    @XmlTransient
    public List<FacDocumentodetalle> getFacDocumentodetalleList() {
        return facDocumentodetalleList;
    }

    public void setFacDocumentodetalleList(List<FacDocumentodetalle> facDocumentodetalleList) {
        this.facDocumentodetalleList = facDocumentodetalleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProducto != null ? idProducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgProducto)) {
            return false;
        }
        CfgProducto other = (CfgProducto) object;
        if ((this.idProducto == null && other.idProducto != null) || (this.idProducto != null && !this.idProducto.equals(other.idProducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgProducto[ idProducto=" + idProducto + " ]";
    }
    
}
