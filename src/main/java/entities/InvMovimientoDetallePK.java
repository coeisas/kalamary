/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author mario
 */
@Embeddable
public class InvMovimientoDetallePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "inv_movimiento_idMovInventario", nullable = false)
    private long invmovimientoidMovInventario;
    @Basic(optional = false)
    @Column(name = "cfg_producto_idProducto", nullable = false)
    private int cfgproductoidProducto;

    public InvMovimientoDetallePK() {
    }

    public InvMovimientoDetallePK(long invmovimientoidMovInventario, int cfgproductoidProducto) {
        this.invmovimientoidMovInventario = invmovimientoidMovInventario;
        this.cfgproductoidProducto = cfgproductoidProducto;
    }

    public long getInvmovimientoidMovInventario() {
        return invmovimientoidMovInventario;
    }

    public void setInvmovimientoidMovInventario(long invmovimientoidMovInventario) {
        this.invmovimientoidMovInventario = invmovimientoidMovInventario;
    }

    public int getCfgproductoidProducto() {
        return cfgproductoidProducto;
    }

    public void setCfgproductoidProducto(int cfgproductoidProducto) {
        this.cfgproductoidProducto = cfgproductoidProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) invmovimientoidMovInventario;
        hash += (int) cfgproductoidProducto;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvMovimientoDetallePK)) {
            return false;
        }
        InvMovimientoDetallePK other = (InvMovimientoDetallePK) object;
        if (this.invmovimientoidMovInventario != other.invmovimientoidMovInventario) {
            return false;
        }
        if (this.cfgproductoidProducto != other.cfgproductoidProducto) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.InvMovimientoDetallePK[ invmovimientoidMovInventario=" + invmovimientoidMovInventario + ", cfgproductoidProducto=" + cfgproductoidProducto + " ]";
    }
    
}
