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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
@Table(name = "inv_movimiento", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvMovimiento.findAll", query = "SELECT i FROM InvMovimiento i"),
    @NamedQuery(name = "InvMovimiento.findByCfgdocumentoidDoc", query = "SELECT i FROM InvMovimiento i WHERE i.invMovimientoPK.cfgdocumentoidDoc = :cfgdocumentoidDoc"),
    @NamedQuery(name = "InvMovimiento.findByNumDoc", query = "SELECT i FROM InvMovimiento i WHERE i.invMovimientoPK.numDoc = :numDoc"),
    @NamedQuery(name = "InvMovimiento.findBySubtotal", query = "SELECT i FROM InvMovimiento i WHERE i.subtotal = :subtotal"),
    @NamedQuery(name = "InvMovimiento.findByDescuento", query = "SELECT i FROM InvMovimiento i WHERE i.descuento = :descuento"),
    @NamedQuery(name = "InvMovimiento.findByIva", query = "SELECT i FROM InvMovimiento i WHERE i.iva = :iva"),
    @NamedQuery(name = "InvMovimiento.findByTotal", query = "SELECT i FROM InvMovimiento i WHERE i.total = :total"),
    @NamedQuery(name = "InvMovimiento.findByFecha", query = "SELECT i FROM InvMovimiento i WHERE i.fecha = :fecha")})
public class InvMovimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected InvMovimientoPK invMovimientoPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "subtotal", precision = 12)
    private Float subtotal;
    @Column(name = "descuento", precision = 12)
    private Float descuento;
    @Column(name = "iva", precision = 12)
    private Float iva;
    @Column(name = "total", precision = 12)
    private Float total;
    @Lob
    @Column(name = "observacion", length = 65535)
    private String observacion;
    @Basic(optional = false)
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @JoinColumn(name = "cfg_documento_idDoc", referencedColumnName = "idDoc", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgDocumento cfgDocumento;
    @JoinColumn(name = "cfg_empresasede_idSede", referencedColumnName = "idSede", nullable = false)
    @ManyToOne(optional = false)
    private CfgEmpresasede cfgempresasedeidSede;
    @JoinColumn(name = "cfg_formaPago_proveedor_idFormaPago", referencedColumnName = "idFormaPago")
    @ManyToOne
    private CfgformaPagoproveedor cfgformaPagoproveedoridFormaPago;
    @JoinColumn(name = "cfg_mov_inventario_detalle_idMovInventarioDetalle", referencedColumnName = "idMovInventarioDetalle", nullable = false)
    @ManyToOne(optional = false)
    private CfgMovInventarioDetalle cfgmovinventariodetalleidMovInventarioDetalle;
    @JoinColumn(name = "cfg_proveedor_idProveedor", referencedColumnName = "idProveedor")
    @ManyToOne
    private CfgProveedor cfgproveedoridProveedor;
    @JoinColumns({
        @JoinColumn(name = "fac_documentosmaster_cfg_documento_idDoc", referencedColumnName = "cfg_documento_idDoc"),
        @JoinColumn(name = "fac_documentosmaster_numDocumento", referencedColumnName = "numDocumento")})
    @ManyToOne
    private FacDocumentosmaster facDocumentosmaster;
    @JoinColumn(name = "seg_usuario_idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne
    private SegUsuario segusuarioidUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invMovimiento")
    private List<InvMovimientoDetalle> invMovimientoDetalleList;

    public InvMovimiento() {
    }

    public InvMovimiento(InvMovimientoPK invMovimientoPK) {
        this.invMovimientoPK = invMovimientoPK;
    }

    public InvMovimiento(InvMovimientoPK invMovimientoPK, Date fecha) {
        this.invMovimientoPK = invMovimientoPK;
        this.fecha = fecha;
    }

    public InvMovimiento(int cfgdocumentoidDoc, int numDoc) {
        this.invMovimientoPK = new InvMovimientoPK(cfgdocumentoidDoc, numDoc);
    }

    public InvMovimientoPK getInvMovimientoPK() {
        return invMovimientoPK;
    }

    public void setInvMovimientoPK(InvMovimientoPK invMovimientoPK) {
        this.invMovimientoPK = invMovimientoPK;
    }

    public Float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Float subtotal) {
        this.subtotal = subtotal;
    }

    public Float getDescuento() {
        return descuento;
    }

    public void setDescuento(Float descuento) {
        this.descuento = descuento;
    }

    public Float getIva() {
        return iva;
    }

    public void setIva(Float iva) {
        this.iva = iva;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public CfgDocumento getCfgDocumento() {
        return cfgDocumento;
    }

    public void setCfgDocumento(CfgDocumento cfgDocumento) {
        this.cfgDocumento = cfgDocumento;
    }

    public CfgEmpresasede getCfgempresasedeidSede() {
        return cfgempresasedeidSede;
    }

    public void setCfgempresasedeidSede(CfgEmpresasede cfgempresasedeidSede) {
        this.cfgempresasedeidSede = cfgempresasedeidSede;
    }

    public CfgformaPagoproveedor getCfgformaPagoproveedoridFormaPago() {
        return cfgformaPagoproveedoridFormaPago;
    }

    public void setCfgformaPagoproveedoridFormaPago(CfgformaPagoproveedor cfgformaPagoproveedoridFormaPago) {
        this.cfgformaPagoproveedoridFormaPago = cfgformaPagoproveedoridFormaPago;
    }

    public CfgMovInventarioDetalle getCfgmovinventariodetalleidMovInventarioDetalle() {
        return cfgmovinventariodetalleidMovInventarioDetalle;
    }

    public void setCfgmovinventariodetalleidMovInventarioDetalle(CfgMovInventarioDetalle cfgmovinventariodetalleidMovInventarioDetalle) {
        this.cfgmovinventariodetalleidMovInventarioDetalle = cfgmovinventariodetalleidMovInventarioDetalle;
    }

    public CfgProveedor getCfgproveedoridProveedor() {
        return cfgproveedoridProveedor;
    }

    public void setCfgproveedoridProveedor(CfgProveedor cfgproveedoridProveedor) {
        this.cfgproveedoridProveedor = cfgproveedoridProveedor;
    }

    public FacDocumentosmaster getFacDocumentosmaster() {
        return facDocumentosmaster;
    }

    public void setFacDocumentosmaster(FacDocumentosmaster facDocumentosmaster) {
        this.facDocumentosmaster = facDocumentosmaster;
    }

    public SegUsuario getSegusuarioidUsuario() {
        return segusuarioidUsuario;
    }

    public void setSegusuarioidUsuario(SegUsuario segusuarioidUsuario) {
        this.segusuarioidUsuario = segusuarioidUsuario;
    }

    @XmlTransient
    public List<InvMovimientoDetalle> getInvMovimientoDetalleList() {
        return invMovimientoDetalleList;
    }

    public void setInvMovimientoDetalleList(List<InvMovimientoDetalle> invMovimientoDetalleList) {
        this.invMovimientoDetalleList = invMovimientoDetalleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invMovimientoPK != null ? invMovimientoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvMovimiento)) {
            return false;
        }
        InvMovimiento other = (InvMovimiento) object;
        if ((this.invMovimientoPK == null && other.invMovimientoPK != null) || (this.invMovimientoPK != null && !this.invMovimientoPK.equals(other.invMovimientoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.InvMovimiento[ invMovimientoPK=" + invMovimientoPK + " ]";
    }

}
