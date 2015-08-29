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
@Table(name = "inv_movimiento_detalle", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvMovimientoDetalle.findAll", query = "SELECT i FROM InvMovimientoDetalle i"),
    @NamedQuery(name = "InvMovimientoDetalle.findByInvmovimientocfgdocumentoidDoc", query = "SELECT i FROM InvMovimientoDetalle i WHERE i.invMovimientoDetallePK.invmovimientocfgdocumentoidDoc = :invmovimientocfgdocumentoidDoc"),
    @NamedQuery(name = "InvMovimientoDetalle.findByInvmovimientonumDoc", query = "SELECT i FROM InvMovimientoDetalle i WHERE i.invMovimientoDetallePK.invmovimientonumDoc = :invmovimientonumDoc"),
    @NamedQuery(name = "InvMovimientoDetalle.findByCfgproductoidProducto", query = "SELECT i FROM InvMovimientoDetalle i WHERE i.invMovimientoDetallePK.cfgproductoidProducto = :cfgproductoidProducto"),
    @NamedQuery(name = "InvMovimientoDetalle.findByCantidad", query = "SELECT i FROM InvMovimientoDetalle i WHERE i.cantidad = :cantidad"),
    @NamedQuery(name = "InvMovimientoDetalle.findByCostoAdquisicion", query = "SELECT i FROM InvMovimientoDetalle i WHERE i.costoAdquisicion = :costoAdquisicion"),
    @NamedQuery(name = "InvMovimientoDetalle.findByDescuento", query = "SELECT i FROM InvMovimientoDetalle i WHERE i.descuento = :descuento"),
    @NamedQuery(name = "InvMovimientoDetalle.findByIva", query = "SELECT i FROM InvMovimientoDetalle i WHERE i.iva = :iva"),
    @NamedQuery(name = "InvMovimientoDetalle.findByFlete", query = "SELECT i FROM InvMovimientoDetalle i WHERE i.flete = :flete"),
    @NamedQuery(name = "InvMovimientoDetalle.findByCostoIndirecto", query = "SELECT i FROM InvMovimientoDetalle i WHERE i.costoIndirecto = :costoIndirecto"),
    @NamedQuery(name = "InvMovimientoDetalle.findByCostoFinal", query = "SELECT i FROM InvMovimientoDetalle i WHERE i.costoFinal = :costoFinal")})
public class InvMovimientoDetalle implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected InvMovimientoDetallePK invMovimientoDetallePK;
    @Basic(optional = false)
    @Column(name = "cantidad", nullable = false)
    private int cantidad;
    @Basic(optional = false)
    @Column(name = "costoAdquisicion", nullable = false)
    private float costoAdquisicion;
    @Basic(optional = false)
    @Column(name = "descuento", nullable = false)
    private float descuento;
    @Basic(optional = false)
    @Column(name = "iva", nullable = false)
    private float iva;
    @Basic(optional = false)
    @Column(name = "flete", nullable = false)
    private float flete;
    @Basic(optional = false)
    @Column(name = "costoIndirecto", nullable = false)
    private float costoIndirecto;
    @Basic(optional = false)
    @Column(name = "costoFinal", nullable = false)
    private float costoFinal;
    @JoinColumn(name = "cfg_producto_idProducto", referencedColumnName = "idProducto", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgProducto cfgProducto;
    @JoinColumns({
        @JoinColumn(name = "inv_movimiento_cfg_documento_idDoc", referencedColumnName = "cfg_documento_idDoc", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "inv_movimiento_numDoc", referencedColumnName = "numDoc", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private InvMovimiento invMovimiento;
    @Transient
    private float valorDescuento;//usado en movimiento inventario para guardar el valor descuento
//    @Transient
//    private float valorIva;
    @Transient
    private float  costoFinalIndividual;//usado en movimiento inventario para determinar el costo del producto independiente de la cantidad comprada

    public InvMovimientoDetalle() {
    }

    public InvMovimientoDetalle(InvMovimientoDetallePK invMovimientoDetallePK) {
        this.invMovimientoDetallePK = invMovimientoDetallePK;
    }

    public InvMovimientoDetalle(InvMovimientoDetallePK invMovimientoDetallePK, int cantidad, float costoAdquisicion, float descuento, float iva, float flete, float costoIndirecto, float costoFinal) {
        this.invMovimientoDetallePK = invMovimientoDetallePK;
        this.cantidad = cantidad;
        this.costoAdquisicion = costoAdquisicion;
        this.descuento = descuento;
        this.iva = iva;
        this.flete = flete;
        this.costoIndirecto = costoIndirecto;
        this.costoFinal = costoFinal;
    }

    public InvMovimientoDetalle(int invmovimientocfgdocumentoidDoc, int invmovimientonumDoc, int cfgproductoidProducto) {
        this.invMovimientoDetallePK = new InvMovimientoDetallePK(invmovimientocfgdocumentoidDoc, invmovimientonumDoc, cfgproductoidProducto);
    }

    public InvMovimientoDetallePK getInvMovimientoDetallePK() {
        return invMovimientoDetallePK;
    }

    public void setInvMovimientoDetallePK(InvMovimientoDetallePK invMovimientoDetallePK) {
        this.invMovimientoDetallePK = invMovimientoDetallePK;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getCostoAdquisicion() {
        return costoAdquisicion;
    }

    public void setCostoAdquisicion(float costoAdquisicion) {
        this.costoAdquisicion = costoAdquisicion;
    }

    public float getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

    public float getIva() {
        return iva;
    }

    public void setIva(float iva) {
        this.iva = iva;
    }

    public float getFlete() {
        return flete;
    }

    public void setFlete(float flete) {
        this.flete = flete;
    }

    public float getCostoIndirecto() {
        return costoIndirecto;
    }

    public void setCostoIndirecto(float costoIndirecto) {
        this.costoIndirecto = costoIndirecto;
    }

    public float getCostoFinal() {
        return costoFinal;
    }

    public void setCostoFinal(float costoFinal) {
        this.costoFinal = costoFinal;
    }

    public CfgProducto getCfgProducto() {
        return cfgProducto;
    }

    public void setCfgProducto(CfgProducto cfgProducto) {
        this.cfgProducto = cfgProducto;
    }

    public InvMovimiento getInvMovimiento() {
        return invMovimiento;
    }

    public void setInvMovimiento(InvMovimiento invMovimiento) {
        this.invMovimiento = invMovimiento;
    }

    public float getValorDescuento() {
        return valorDescuento;
    }

    public void setValorDescuento(float valorDescuento) {
        this.valorDescuento = valorDescuento;
    }

//    public float getValorIva() {
//        return valorIva;
//    }
//
//    public void setValorIva(float valorIva) {
//        this.valorIva = valorIva;
//    }

    public float getCostoFinalIndividual() {
        return costoFinalIndividual;
    }

    public void setCostoFinalIndividual(float costoFinalIndividual) {
        this.costoFinalIndividual = costoFinalIndividual;
    }    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invMovimientoDetallePK != null ? invMovimientoDetallePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvMovimientoDetalle)) {
            return false;
        }
        InvMovimientoDetalle other = (InvMovimientoDetalle) object;
        if ((this.invMovimientoDetallePK == null && other.invMovimientoDetallePK != null) || (this.invMovimientoDetallePK != null && !this.invMovimientoDetallePK.equals(other.invMovimientoDetallePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.InvMovimientoDetalle[ invMovimientoDetallePK=" + invMovimientoDetallePK + " ]";
    }
   
}
