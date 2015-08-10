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
    @Column(name = "cfg_formapago_idFormaPago", nullable = false)
    private int cfgformapagoidFormaPago;
    @Basic(optional = false)
    @Column(name = "fac_documentosmaster_cfg_documento_idDoc", nullable = false)
    private int facdocumentosmastercfgdocumentoidDoc;
    @Basic(optional = false)
    @Column(name = "fac_documentosmaster_numDocumento", nullable = false)
    private int facdocumentosmasternumDocumento;

    public FacDocuementopagoPK() {
    }

    public FacDocuementopagoPK(int cfgformapagoidFormaPago, int facdocumentosmastercfgdocumentoidDoc, int facdocumentosmasternumDocumento) {
        this.cfgformapagoidFormaPago = cfgformapagoidFormaPago;
        this.facdocumentosmastercfgdocumentoidDoc = facdocumentosmastercfgdocumentoidDoc;
        this.facdocumentosmasternumDocumento = facdocumentosmasternumDocumento;
    }

    public int getCfgformapagoidFormaPago() {
        return cfgformapagoidFormaPago;
    }

    public void setCfgformapagoidFormaPago(int cfgformapagoidFormaPago) {
        this.cfgformapagoidFormaPago = cfgformapagoidFormaPago;
    }

    public int getFacdocumentosmastercfgdocumentoidDoc() {
        return facdocumentosmastercfgdocumentoidDoc;
    }

    public void setFacdocumentosmastercfgdocumentoidDoc(int facdocumentosmastercfgdocumentoidDoc) {
        this.facdocumentosmastercfgdocumentoidDoc = facdocumentosmastercfgdocumentoidDoc;
    }

    public int getFacdocumentosmasternumDocumento() {
        return facdocumentosmasternumDocumento;
    }

    public void setFacdocumentosmasternumDocumento(int facdocumentosmasternumDocumento) {
        this.facdocumentosmasternumDocumento = facdocumentosmasternumDocumento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cfgformapagoidFormaPago;
        hash += (int) facdocumentosmastercfgdocumentoidDoc;
        hash += (int) facdocumentosmasternumDocumento;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacDocuementopagoPK)) {
            return false;
        }
        FacDocuementopagoPK other = (FacDocuementopagoPK) object;
        if (this.cfgformapagoidFormaPago != other.cfgformapagoidFormaPago) {
            return false;
        }
        if (this.facdocumentosmastercfgdocumentoidDoc != other.facdocumentosmastercfgdocumentoidDoc) {
            return false;
        }
        if (this.facdocumentosmasternumDocumento != other.facdocumentosmasternumDocumento) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FacDocuementopagoPK[ cfgformapagoidFormaPago=" + cfgformapagoidFormaPago + ", facdocumentosmastercfgdocumentoidDoc=" + facdocumentosmastercfgdocumentoidDoc + ", facdocumentosmasternumDocumento=" + facdocumentosmasternumDocumento + " ]";
    }
    
}
