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
public class FacDocuementopagoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "fac_documentosmaster_iddocumentomaster", nullable = false)
    private long facDocumentosmasterIddocumentomaster;
    @Basic(optional = false)
    @Column(name = "cfg_formapago_idFormaPago", nullable = false)
    private int cfgformapagoidFormaPago;

    public FacDocuementopagoPK() {
    }

    public FacDocuementopagoPK(long facDocumentosmasterIddocumentomaster, int cfgformapagoidFormaPago) {
        this.facDocumentosmasterIddocumentomaster = facDocumentosmasterIddocumentomaster;
        this.cfgformapagoidFormaPago = cfgformapagoidFormaPago;
    }

    public long getFacDocumentosmasterIddocumentomaster() {
        return facDocumentosmasterIddocumentomaster;
    }

    public void setFacDocumentosmasterIddocumentomaster(long facDocumentosmasterIddocumentomaster) {
        this.facDocumentosmasterIddocumentomaster = facDocumentosmasterIddocumentomaster;
    }

    public int getCfgformapagoidFormaPago() {
        return cfgformapagoidFormaPago;
    }

    public void setCfgformapagoidFormaPago(int cfgformapagoidFormaPago) {
        this.cfgformapagoidFormaPago = cfgformapagoidFormaPago;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) facDocumentosmasterIddocumentomaster;
        hash += (int) cfgformapagoidFormaPago;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacDocuementopagoPK)) {
            return false;
        }
        FacDocuementopagoPK other = (FacDocuementopagoPK) object;
        if (this.facDocumentosmasterIddocumentomaster != other.facDocumentosmasterIddocumentomaster) {
            return false;
        }
        if (this.cfgformapagoidFormaPago != other.cfgformapagoidFormaPago) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FacDocuementopagoPK[ facDocumentosmasterIddocumentomaster=" + facDocumentosmasterIddocumentomaster + ", cfgformapagoidFormaPago=" + cfgformapagoidFormaPago + " ]";
    }
    
}
