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
public class InvConsolidadoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "cfg_producto_idProducto", nullable = false)
    private int cfgproductoidProducto;
    @Basic(optional = false)
    @Column(name = "cfg_empresasede_idSede", nullable = false)
    private int cfgempresasedeidSede;

    public InvConsolidadoPK() {
    }

    public InvConsolidadoPK(int cfgproductoidProducto, int cfgempresasedeidSede) {
        this.cfgproductoidProducto = cfgproductoidProducto;
        this.cfgempresasedeidSede = cfgempresasedeidSede;
    }

    public int getCfgproductoidProducto() {
        return cfgproductoidProducto;
    }

    public void setCfgproductoidProducto(int cfgproductoidProducto) {
        this.cfgproductoidProducto = cfgproductoidProducto;
    }

    public int getCfgempresasedeidSede() {
        return cfgempresasedeidSede;
    }

    public void setCfgempresasedeidSede(int cfgempresasedeidSede) {
        this.cfgempresasedeidSede = cfgempresasedeidSede;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cfgproductoidProducto;
        hash += (int) cfgempresasedeidSede;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvConsolidadoPK)) {
            return false;
        }
        InvConsolidadoPK other = (InvConsolidadoPK) object;
        if (this.cfgproductoidProducto != other.cfgproductoidProducto) {
            return false;
        }
        if (this.cfgempresasedeidSede != other.cfgempresasedeidSede) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.InvConsolidadoPK[ cfgproductoidProducto=" + cfgproductoidProducto + ", cfgempresasedeidSede=" + cfgempresasedeidSede + " ]";
    }
    
}
