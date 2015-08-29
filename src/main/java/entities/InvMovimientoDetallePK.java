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
    @Column(name = "inv_movimiento_cfg_documento_idDoc", nullable = false)
    private int invmovimientocfgdocumentoidDoc;
    @Basic(optional = false)
    @Column(name = "inv_movimiento_numDoc", nullable = false)
    private int invmovimientonumDoc;
    @Basic(optional = false)
    @Column(name = "cfg_producto_idProducto", nullable = false)
    private int cfgproductoidProducto;

    public InvMovimientoDetallePK() {
    }

    public InvMovimientoDetallePK(int invmovimientocfgdocumentoidDoc, int invmovimientonumDoc, int cfgproductoidProducto) {
        this.invmovimientocfgdocumentoidDoc = invmovimientocfgdocumentoidDoc;
        this.invmovimientonumDoc = invmovimientonumDoc;
        this.cfgproductoidProducto = cfgproductoidProducto;
    }

    public int getInvmovimientocfgdocumentoidDoc() {
        return invmovimientocfgdocumentoidDoc;
    }

    public void setInvmovimientocfgdocumentoidDoc(int invmovimientocfgdocumentoidDoc) {
        this.invmovimientocfgdocumentoidDoc = invmovimientocfgdocumentoidDoc;
    }

    public int getInvmovimientonumDoc() {
        return invmovimientonumDoc;
    }

    public void setInvmovimientonumDoc(int invmovimientonumDoc) {
        this.invmovimientonumDoc = invmovimientonumDoc;
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
        hash += (int) invmovimientocfgdocumentoidDoc;
        hash += (int) invmovimientonumDoc;
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
        if (this.invmovimientocfgdocumentoidDoc != other.invmovimientocfgdocumentoidDoc) {
            return false;
        }
        if (this.invmovimientonumDoc != other.invmovimientonumDoc) {
            return false;
        }
        if (this.cfgproductoidProducto != other.cfgproductoidProducto) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.InvMovimientoDetallePK[ invmovimientocfgdocumentoidDoc=" + invmovimientocfgdocumentoidDoc + ", invmovimientonumDoc=" + invmovimientonumDoc + ", cfgproductoidProducto=" + cfgproductoidProducto + " ]";
    }
    
}
