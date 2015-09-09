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
public class FacMovcajadetallePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "fac_movcaja_idMovimiento", nullable = false)
    private int facmovcajaidMovimiento;
    @Basic(optional = false)
    @Column(name = "fac_documentosmaster_cfg_documento_idDoc", nullable = false)
    private int facdocumentosmastercfgdocumentoidDoc;
    @Basic(optional = false)
    @Column(name = "fac_documentosmaster_numDocumento", nullable = false)
    private int facdocumentosmasternumDocumento;
    @Basic(optional = false)
    @Column(name = "cfg_formapago_idFormaPago", nullable = false)
    private int cfgformapagoidFormaPago;

    public FacMovcajadetallePK() {
    }

    public FacMovcajadetallePK(int facmovcajaidMovimiento, int facdocumentosmastercfgdocumentoidDoc, int facdocumentosmasternumDocumento, int cfgformapagoidFormaPago) {
        this.facmovcajaidMovimiento = facmovcajaidMovimiento;
        this.facdocumentosmastercfgdocumentoidDoc = facdocumentosmastercfgdocumentoidDoc;
        this.facdocumentosmasternumDocumento = facdocumentosmasternumDocumento;
        this.cfgformapagoidFormaPago = cfgformapagoidFormaPago;
    }

    public int getFacmovcajaidMovimiento() {
        return facmovcajaidMovimiento;
    }

    public void setFacmovcajaidMovimiento(int facmovcajaidMovimiento) {
        this.facmovcajaidMovimiento = facmovcajaidMovimiento;
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

    public int getCfgformapagoidFormaPago() {
        return cfgformapagoidFormaPago;
    }

    public void setCfgformapagoidFormaPago(int cfgformapagoidFormaPago) {
        this.cfgformapagoidFormaPago = cfgformapagoidFormaPago;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) facmovcajaidMovimiento;
        hash += (int) facdocumentosmastercfgdocumentoidDoc;
        hash += (int) facdocumentosmasternumDocumento;
        hash += (int) cfgformapagoidFormaPago;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacMovcajadetallePK)) {
            return false;
        }
        FacMovcajadetallePK other = (FacMovcajadetallePK) object;
        if (this.facmovcajaidMovimiento != other.facmovcajaidMovimiento) {
            return false;
        }
        if (this.facdocumentosmastercfgdocumentoidDoc != other.facdocumentosmastercfgdocumentoidDoc) {
            return false;
        }
        if (this.facdocumentosmasternumDocumento != other.facdocumentosmasternumDocumento) {
            return false;
        }
        if (this.cfgformapagoidFormaPago != other.cfgformapagoidFormaPago) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FacMovcajadetallePK[ facmovcajaidMovimiento=" + facmovcajaidMovimiento + ", facdocumentosmastercfgdocumentoidDoc=" + facdocumentosmastercfgdocumentoidDoc + ", facdocumentosmasternumDocumento=" + facdocumentosmasternumDocumento + ", cfgformapagoidFormaPago=" + cfgformapagoidFormaPago + " ]";
    }
    
}
