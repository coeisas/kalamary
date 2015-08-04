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
public class FacDocumentoimpuestoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "fac_documentosmaster_iddocumentomaster", nullable = false)
    private long facDocumentosmasterIddocumentomaster;
    @Basic(optional = false)
    @Column(name = "cfg_impuesto_idImpuesto", nullable = false)
    private int cfgimpuestoidImpuesto;

    public FacDocumentoimpuestoPK() {
    }

    public FacDocumentoimpuestoPK(long facDocumentosmasterIddocumentomaster, int cfgimpuestoidImpuesto) {
        this.facDocumentosmasterIddocumentomaster = facDocumentosmasterIddocumentomaster;
        this.cfgimpuestoidImpuesto = cfgimpuestoidImpuesto;
    }

    public long getFacDocumentosmasterIddocumentomaster() {
        return facDocumentosmasterIddocumentomaster;
    }

    public void setFacDocumentosmasterIddocumentomaster(long facDocumentosmasterIddocumentomaster) {
        this.facDocumentosmasterIddocumentomaster = facDocumentosmasterIddocumentomaster;
    }

    public int getCfgimpuestoidImpuesto() {
        return cfgimpuestoidImpuesto;
    }

    public void setCfgimpuestoidImpuesto(int cfgimpuestoidImpuesto) {
        this.cfgimpuestoidImpuesto = cfgimpuestoidImpuesto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) facDocumentosmasterIddocumentomaster;
        hash += (int) cfgimpuestoidImpuesto;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacDocumentoimpuestoPK)) {
            return false;
        }
        FacDocumentoimpuestoPK other = (FacDocumentoimpuestoPK) object;
        if (this.facDocumentosmasterIddocumentomaster != other.facDocumentosmasterIddocumentomaster) {
            return false;
        }
        if (this.cfgimpuestoidImpuesto != other.cfgimpuestoidImpuesto) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FacDocumentoimpuestoPK[ facDocumentosmasterIddocumentomaster=" + facDocumentosmasterIddocumentomaster + ", cfgimpuestoidImpuesto=" + cfgimpuestoidImpuesto + " ]";
    }
    
}
