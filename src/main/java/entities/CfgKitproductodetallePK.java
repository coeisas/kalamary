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
import javax.validation.constraints.NotNull;

/**
 *
 * @author mario
 */
@Embeddable
public class CfgKitproductodetallePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "cfg_kitproductomaestro_idKit", nullable = false)
    private int cfgkitproductomaestroidKit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cfg_producto_idProducto", nullable = false)
    private int cfgproductoidProducto;

    public CfgKitproductodetallePK() {
    }

    public CfgKitproductodetallePK(int cfgkitproductomaestroidKit, int cfgproductoidProducto) {
        this.cfgkitproductomaestroidKit = cfgkitproductomaestroidKit;
        this.cfgproductoidProducto = cfgproductoidProducto;
    }

    public int getCfgkitproductomaestroidKit() {
        return cfgkitproductomaestroidKit;
    }

    public void setCfgkitproductomaestroidKit(int cfgkitproductomaestroidKit) {
        this.cfgkitproductomaestroidKit = cfgkitproductomaestroidKit;
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
        hash += (int) cfgkitproductomaestroidKit;
        hash += (int) cfgproductoidProducto;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgKitproductodetallePK)) {
            return false;
        }
        CfgKitproductodetallePK other = (CfgKitproductodetallePK) object;
        if (this.cfgkitproductomaestroidKit != other.cfgkitproductomaestroidKit) {
            return false;
        }
        if (this.cfgproductoidProducto != other.cfgproductoidProducto) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgKitproductodetallePK[ cfgkitproductomaestroidKit=" + cfgkitproductomaestroidKit + ", cfgproductoidProducto=" + cfgproductoidProducto + " ]";
    }
    
}
