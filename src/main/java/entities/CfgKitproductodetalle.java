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
@Table(name = "cfg_kitproductodetalle", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgKitproductodetalle.findAll", query = "SELECT c FROM CfgKitproductodetalle c"),
    @NamedQuery(name = "CfgKitproductodetalle.findByCfgkitproductomaestroidKit", query = "SELECT c FROM CfgKitproductodetalle c WHERE c.cfgKitproductodetallePK.cfgkitproductomaestroidKit = :cfgkitproductomaestroidKit"),
    @NamedQuery(name = "CfgKitproductodetalle.findByCfgproductoidProducto", query = "SELECT c FROM CfgKitproductodetalle c WHERE c.cfgKitproductodetallePK.cfgproductoidProducto = :cfgproductoidProducto"),
    @NamedQuery(name = "CfgKitproductodetalle.findByPrecioUnitario", query = "SELECT c FROM CfgKitproductodetalle c WHERE c.precioUnitario = :precioUnitario"),
    @NamedQuery(name = "CfgKitproductodetalle.findByCant", query = "SELECT c FROM CfgKitproductodetalle c WHERE c.cant = :cant"),
    @NamedQuery(name = "CfgKitproductodetalle.findByPrecioTotal", query = "SELECT c FROM CfgKitproductodetalle c WHERE c.precioTotal = :precioTotal")})
public class CfgKitproductodetalle implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CfgKitproductodetallePK cfgKitproductodetallePK;
    @Basic(optional = false)
    @Column(name = "precioUnitario", nullable = false)
    private float precioUnitario;
    @Basic(optional = false)
    @Column(name = "cant", nullable = false)
    private float cant;
    @Basic(optional = false)
    @Column(name = "precioTotal", nullable = false)
    private float precioTotal;
    @JoinColumn(name = "cfg_kitproductomaestro_idKit", referencedColumnName = "idKit", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgKitproductomaestro cfgKitproductomaestro;
    @JoinColumn(name = "cfg_producto_idProducto", referencedColumnName = "idProducto", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgProducto cfgProducto;
    @Transient
    private boolean nuevo;

    public CfgKitproductodetalle() {
    }

    public CfgKitproductodetalle(CfgKitproductodetallePK cfgKitproductodetallePK) {
        this.cfgKitproductodetallePK = cfgKitproductodetallePK;
    }

    public CfgKitproductodetalle(CfgKitproductodetallePK cfgKitproductodetallePK, float precioUnitario, float cant, float precioTotal) {
        this.cfgKitproductodetallePK = cfgKitproductodetallePK;
        this.precioUnitario = precioUnitario;
        this.cant = cant;
        this.precioTotal = precioTotal;
    }

    public CfgKitproductodetalle(int cfgkitproductomaestroidKit, int cfgproductoidProducto) {
        this.cfgKitproductodetallePK = new CfgKitproductodetallePK(cfgkitproductomaestroidKit, cfgproductoidProducto);
    }

    public CfgKitproductodetallePK getCfgKitproductodetallePK() {
        return cfgKitproductodetallePK;
    }

    public void setCfgKitproductodetallePK(CfgKitproductodetallePK cfgKitproductodetallePK) {
        this.cfgKitproductodetallePK = cfgKitproductodetallePK;
    }

    public float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public float getCant() {
        return cant;
    }

    public void setCant(float cant) {
        this.cant = cant;
    }

    public float getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(float precioTotal) {
        this.precioTotal = precioTotal;
    }

    public CfgKitproductomaestro getCfgKitproductomaestro() {
        return cfgKitproductomaestro;
    }

    public void setCfgKitproductomaestro(CfgKitproductomaestro cfgKitproductomaestro) {
        this.cfgKitproductomaestro = cfgKitproductomaestro;
    }

    public CfgProducto getCfgProducto() {
        return cfgProducto;
    }

    public void setCfgProducto(CfgProducto cfgProducto) {
        this.cfgProducto = cfgProducto;
    }

    public boolean isNuevo() {
        return nuevo;
    }

    public void setNuevo(boolean nuevo) {
        this.nuevo = nuevo;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cfgKitproductodetallePK != null ? cfgKitproductodetallePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgKitproductodetalle)) {
            return false;
        }
        CfgKitproductodetalle other = (CfgKitproductodetalle) object;
        if ((this.cfgKitproductodetallePK == null && other.cfgKitproductodetallePK != null) || (this.cfgKitproductodetallePK != null && !this.cfgKitproductodetallePK.equals(other.cfgKitproductodetallePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgKitproductodetalle[ cfgKitproductodetallePK=" + cfgKitproductodetallePK + " ]";
    }
  
}
