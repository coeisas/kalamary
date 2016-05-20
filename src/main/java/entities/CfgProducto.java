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
    @NamedQuery(name = "CfgProducto.findByCodigoInterno", query = "SELECT c FROM CfgProducto c WHERE c.codigoInterno = :codigoInterno"),
    @NamedQuery(name = "CfgProducto.findByCodProducto", query = "SELECT c FROM CfgProducto c WHERE c.codProducto = :codProducto"),
    @NamedQuery(name = "CfgProducto.findByCodBarProducto", query = "SELECT c FROM CfgProducto c WHERE c.codBarProducto = :codBarProducto"),
    @NamedQuery(name = "CfgProducto.findByNomProducto", query = "SELECT c FROM CfgProducto c WHERE c.nomProducto = :nomProducto"),
    @NamedQuery(name = "CfgProducto.findByStockMin", query = "SELECT c FROM CfgProducto c WHERE c.stockMin = :stockMin"),
    @NamedQuery(name = "CfgProducto.findByCostoAdquisicion", query = "SELECT c FROM CfgProducto c WHERE c.costoAdquisicion = :costoAdquisicion"),
    @NamedQuery(name = "CfgProducto.findByIva", query = "SELECT c FROM CfgProducto c WHERE c.iva = :iva"),
    @NamedQuery(name = "CfgProducto.findByFlete", query = "SELECT c FROM CfgProducto c WHERE c.flete = :flete"),
    @NamedQuery(name = "CfgProducto.findByCostoIndirecto", query = "SELECT c FROM CfgProducto c WHERE c.costoIndirecto = :costoIndirecto"),
    @NamedQuery(name = "CfgProducto.findByCosto", query = "SELECT c FROM CfgProducto c WHERE c.costo = :costo"),
    @NamedQuery(name = "CfgProducto.findByUtilidad", query = "SELECT c FROM CfgProducto c WHERE c.utilidad = :utilidad"),
    @NamedQuery(name = "CfgProducto.findByPrecio", query = "SELECT c FROM CfgProducto c WHERE c.precio = :precio"),
    @NamedQuery(name = "CfgProducto.findByActivo", query = "SELECT c FROM CfgProducto c WHERE c.activo = :activo"),
    @NamedQuery(name = "CfgProducto.findByFecCrea", query = "SELECT c FROM CfgProducto c WHERE c.fecCrea = :fecCrea"),
    @NamedQuery(name = "CfgProducto.findByEsServicio", query = "SELECT c FROM CfgProducto c WHERE c.esServicio = :esServicio"),
    @NamedQuery(name = "CfgProducto.findByEsKit", query = "SELECT c FROM CfgProducto c WHERE c.esKit = :esKit")})
public class CfgProducto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idProducto", nullable = false)
    private Integer idProducto;
    @Column(name = "codigoInterno", length = 50)
    private String codigoInterno;
    @Column(name = "codProducto", length = 10)
    private String codProducto;
    @Column(name = "codBarProducto", length = 45)
    private String codBarProducto;
    @Column(name = "nomProducto", length = 400)
    private String nomProducto;
    @Column(name = "stockMin")
    private Integer stockMin;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
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
    @Column(name = "imgProducto", length = 65535)
    private String imgProducto;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "fecCrea")
    @Temporal(TemporalType.DATE)
    private Date fecCrea;
    @Basic(optional = false)
    @Column(name = "esServicio", nullable = false)
    private boolean esServicio;
    @Basic(optional = false)
    @Column(name = "esKit", nullable = false)
    private boolean esKit;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgProducto")
    private List<InvMovimientoDetalle> invMovimientoDetalleList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgProducto")
    private List<InvConsolidado> invConsolidadoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgProducto")
    private List<CfgKitproductodetalle> cfgKitproductodetalleList;
    @JoinColumn(name = "cfg_color_id_color", referencedColumnName = "id_color")
    @ManyToOne
    private CfgColor cfgColorIdColor;
    @JoinColumn(name = "cfg_empresa_idEmpresa", referencedColumnName = "idEmpresa", nullable = false)
    @ManyToOne(optional = false)
    private CfgEmpresa cfgempresaidEmpresa;
    @JoinColumn(name = "cfg_kitproductomaestro_idKit", referencedColumnName = "idKit")
    @ManyToOne
    private CfgKitproductomaestro cfgkitproductomaestroidKit;
    @JoinColumn(name = "cfg_categoriaproducto_idCategoria", referencedColumnName = "idCategoria", nullable = false)
    @ManyToOne(optional = false)
    private CfgCategoriaproducto cfgcategoriaproductoidCategoria;
    @JoinColumn(name = "cfg_referenciaproducto_idReferencia", referencedColumnName = "idReferencia", nullable = false)
    @ManyToOne(optional = false)
    private CfgReferenciaproducto cfgreferenciaproductoidReferencia;    
    @JoinColumn(name = "cfg_marcaproducto_idMarca", referencedColumnName = "idMarca", nullable = false)
    @ManyToOne(optional = false)
    private CfgMarcaproducto cfgmarcaproductoidMarca;
    @JoinColumn(name = "cfg_talla_id_talla", referencedColumnName = "id_talla")
    @ManyToOne
    private CfgTalla cfgTallaIdTalla;
    @JoinColumn(name = "cfg_unidad_id_unidad", referencedColumnName = "id_unidad")
    @ManyToOne
    private CfgUnidad cfgUnidadIdUnidad;
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

    public CfgProducto(Integer idProducto, boolean esServicio, boolean esKit) {
        this.idProducto = idProducto;
        this.esServicio = esServicio;
        this.esKit = esKit;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
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

    public Integer getStockMin() {
        return stockMin;
    }

    public void setStockMin(Integer stockMin) {
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

    public String getImgProducto() {
        return imgProducto;
    }

    public void setImgProducto(String imgProducto) {
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

    public boolean getEsServicio() {
        return esServicio;
    }

    public void setEsServicio(boolean esServicio) {
        this.esServicio = esServicio;
    }

    public boolean getEsKit() {
        return esKit;
    }

    public void setEsKit(boolean esKit) {
        this.esKit = esKit;
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

    public CfgColor getCfgColorIdColor() {
        return cfgColorIdColor;
    }

    public void setCfgColorIdColor(CfgColor cfgColorIdColor) {
        this.cfgColorIdColor = cfgColorIdColor;
    }

    public CfgEmpresa getCfgempresaidEmpresa() {
        return cfgempresaidEmpresa;
    }

    public void setCfgempresaidEmpresa(CfgEmpresa cfgempresaidEmpresa) {
        this.cfgempresaidEmpresa = cfgempresaidEmpresa;
    }

    public CfgKitproductomaestro getCfgkitproductomaestroidKit() {
        return cfgkitproductomaestroidKit;
    }

    public void setCfgkitproductomaestroidKit(CfgKitproductomaestro cfgkitproductomaestroidKit) {
        this.cfgkitproductomaestroidKit = cfgkitproductomaestroidKit;
    }
    
    public CfgCategoriaproducto getCfgcategoriaproductoidCategoria() {
        return cfgcategoriaproductoidCategoria;
    }

    public void setCfgcategoriaproductoidCategoria(CfgCategoriaproducto cfgcategoriaproductoidCategoria) {
        this.cfgcategoriaproductoidCategoria = cfgcategoriaproductoidCategoria;
    }
    
    public CfgReferenciaproducto getCfgreferenciaproductoidReferencia() {
        return cfgreferenciaproductoidReferencia;
    }

    public void setCfgreferenciaproductoidReferencia(CfgReferenciaproducto cfgreferenciaproductoidReferencia) {
        this.cfgreferenciaproductoidReferencia = cfgreferenciaproductoidReferencia;
    }
    
    public CfgMarcaproducto getCfgmarcaproductoidMarca() {
        return cfgmarcaproductoidMarca;
    }

    public void setCfgmarcaproductoidMarca(CfgMarcaproducto cfgmarcaproductoidMarca) {
        this.cfgmarcaproductoidMarca = cfgmarcaproductoidMarca;
    }

    public CfgTalla getCfgTallaIdTalla() {
        return cfgTallaIdTalla;
    }

    public void setCfgTallaIdTalla(CfgTalla cfgTallaIdTalla) {
        this.cfgTallaIdTalla = cfgTallaIdTalla;
    }

    public CfgUnidad getCfgUnidadIdUnidad() {
        return cfgUnidadIdUnidad;
    }

    public void setCfgUnidadIdUnidad(CfgUnidad cfgUnidadIdUnidad) {
        this.cfgUnidadIdUnidad = cfgUnidadIdUnidad;
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

    public int unidadesDisponibles(CfgEmpresasede sede) {
        if (!invConsolidadoList.isEmpty()) {
            InvConsolidado consolidado = null;
            //el producto tiene consolidados por cada sede de la empresa
            for (InvConsolidado ic : invConsolidadoList) {
                //se busca el consolidado para la sede en cuestion
                if (ic.getCfgEmpresasede().equals(sede)) {
                    consolidado = ic;
                    break;
                }
            }
            if (consolidado != null) {
                return consolidado.getExistencia();
            } else {//si no se encontro consolidado significa que no esta registrado el producto en el inventario
                return 0;
            }
        } else {
            return 0;
        }
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
