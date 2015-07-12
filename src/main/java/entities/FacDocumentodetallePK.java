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
public class FacDocumentodetallePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "cfg_producto_idProducto", nullable = false)
    private int cfgproductoidProducto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fac_documentosmaster_iddocumentomaster", nullable = false)
    private long facDocumentosmasterIddocumentomaster;

    public FacDocumentodetallePK() {
    }

    public FacDocumentodetallePK(int cfgproductoidProducto, long facDocumentosmasterIddocumentomaster) {
        this.cfgproductoidProducto = cfgproductoidProducto;
        this.facDocumentosmasterIddocumentomaster = facDocumentosmasterIddocumentomaster;
    }

    public int getCfgproductoidProducto() {
        return cfgproductoidProducto;
    }

    public void setCfgproductoidProducto(int cfgproductoidProducto) {
        this.cfgproductoidProducto = cfgproductoidProducto;
    }

    public long getFacDocumentosmasterIddocumentomaster() {
        return facDocumentosmasterIddocumentomaster;
    }

    public void setFacDocumentosmasterIddocumentomaster(long facDocumentosmasterIddocumentomaster) {
        this.facDocumentosmasterIddocumentomaster = facDocumentosmasterIddocumentomaster;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cfgproductoidProducto;
        hash += (int) facDocumentosmasterIddocumentomaster;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacDocumentodetallePK)) {
            return false;
        }
        FacDocumentodetallePK other = (FacDocumentodetallePK) object;
        if (this.cfgproductoidProducto != other.cfgproductoidProducto) {
            return false;
        }
        if (this.facDocumentosmasterIddocumentomaster != other.facDocumentosmasterIddocumentomaster) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FacDocumentodetallePK[ cfgproductoidProducto=" + cfgproductoidProducto + ", facDocumentosmasterIddocumentomaster=" + facDocumentosmasterIddocumentomaster + " ]";
    }
    
}
