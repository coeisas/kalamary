/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "fac_documentodetalle", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacDocumentodetalle.findAll", query = "SELECT f FROM FacDocumentodetalle f"),
    @NamedQuery(name = "FacDocumentodetalle.findByCantidad", query = "SELECT f FROM FacDocumentodetalle f WHERE f.cantidad = :cantidad"),
    @NamedQuery(name = "FacDocumentodetalle.findByValorUnitario", query = "SELECT f FROM FacDocumentodetalle f WHERE f.valorUnitario = :valorUnitario"),
    @NamedQuery(name = "FacDocumentodetalle.findByValorTotal", query = "SELECT f FROM FacDocumentodetalle f WHERE f.valorTotal = :valorTotal"),
    @NamedQuery(name = "FacDocumentodetalle.findByDescuento", query = "SELECT f FROM FacDocumentodetalle f WHERE f.descuento = :descuento"),
    @NamedQuery(name = "FacDocumentodetalle.findByCfgproductoidProducto", query = "SELECT f FROM FacDocumentodetalle f WHERE f.facDocumentodetallePK.cfgproductoidProducto = :cfgproductoidProducto"),
    @NamedQuery(name = "FacDocumentodetalle.findByFacdocumentosmastercfgdocumentoidDoc", query = "SELECT f FROM FacDocumentodetalle f WHERE f.facDocumentodetallePK.facdocumentosmastercfgdocumentoidDoc = :facdocumentosmastercfgdocumentoidDoc"),
    @NamedQuery(name = "FacDocumentodetalle.findByFacdocumentosmasternumDocumento", query = "SELECT f FROM FacDocumentodetalle f WHERE f.facDocumentodetallePK.facdocumentosmasternumDocumento = :facdocumentosmasternumDocumento")})
public class FacDocumentodetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacDocumentodetallePK facDocumentodetallePK;
    @Basic(optional = false)
    @Column(name = "cantidad", nullable = false)
    private int cantidad;
    @Basic(optional = false)
    @Column(name = "valorUnitario", nullable = false)
    private float valorUnitario;
    @Basic(optional = false)
    @Column(name = "valorTotal", nullable = false)
    private float valorTotal;
    @Basic(optional = false)
    @Column(name = "descuento", nullable = false)
    private int descuento;
    @JoinColumn(name = "cfg_producto_idProducto", referencedColumnName = "idProducto", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgProducto cfgProducto;
    @JoinColumns({
        @JoinColumn(name = "fac_documentosmaster_cfg_documento_idDoc", referencedColumnName = "cfg_documento_idDoc", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "fac_documentosmaster_numDocumento", referencedColumnName = "numDocumento", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private FacDocumentosmaster facDocumentosmaster;
    @Transient
    private float valorDescuento;
    @Transient
    private float precioOriginal;
    @Transient
    private int cantidadPosible;//cantidad posible dependiendo del inventario

    public FacDocumentodetalle() {
    }

    public FacDocumentodetalle(FacDocumentodetallePK facDocumentodetallePK) {
        this.facDocumentodetallePK = facDocumentodetallePK;
    }

    public FacDocumentodetalle(FacDocumentodetallePK facDocumentodetallePK, int cantidad, float valorUnitario, float valorTotal, int descuento) {
        this.facDocumentodetallePK = facDocumentodetallePK;
        this.cantidad = cantidad;
        this.valorUnitario = valorUnitario;
        this.valorTotal = valorTotal;
        this.descuento = descuento;
    }

    public FacDocumentodetalle(int cfgproductoidProducto, int facdocumentosmastercfgdocumentoidDoc, int facdocumentosmasternumDocumento) {
        this.facDocumentodetallePK = new FacDocumentodetallePK(cfgproductoidProducto, facdocumentosmastercfgdocumentoidDoc, facdocumentosmasternumDocumento);
    }

    public FacDocumentodetallePK getFacDocumentodetallePK() {
        return facDocumentodetallePK;
    }

    public void setFacDocumentodetallePK(FacDocumentodetallePK facDocumentodetallePK) {
        this.facDocumentodetallePK = facDocumentodetallePK;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(float valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(float valorTotal) {
        this.valorTotal = valorTotal;
    }

    public int getDescuento() {
        return descuento;
    }

    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }

    public CfgProducto getCfgProducto() {
        return cfgProducto;
    }

    public void setCfgProducto(CfgProducto cfgProducto) {
        this.cfgProducto = cfgProducto;
    }

    public FacDocumentosmaster getFacDocumentosmaster() {
        return facDocumentosmaster;
    }

    public void setFacDocumentosmaster(FacDocumentosmaster facDocumentosmaster) {
        this.facDocumentosmaster = facDocumentosmaster;
    }

    public float getValorDescuento() {
        return valorDescuento;
    }

    public void setValorDescuento(float valorDescuento) {
        this.valorDescuento = valorDescuento;
    }

    public float getPrecioOriginal() {
        return precioOriginal;
    }

    public void setPrecioOriginal(float precioOriginal) {
        this.precioOriginal = precioOriginal;
    }

    public int getCantidadPosible() {
        return cantidadPosible;
    }

    public void setCantidadPosible(int cantidadPosible) {
        this.cantidadPosible = cantidadPosible;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facDocumentodetallePK != null ? facDocumentodetallePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacDocumentodetalle)) {
            return false;
        }
        FacDocumentodetalle other = (FacDocumentodetalle) object;
        if ((this.facDocumentodetallePK == null && other.facDocumentodetallePK != null) || (this.facDocumentodetallePK != null && !this.facDocumentodetallePK.equals(other.facDocumentodetallePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FacDocumentodetalle[ facDocumentodetallePK=" + facDocumentodetallePK + " ]";
    }

}
