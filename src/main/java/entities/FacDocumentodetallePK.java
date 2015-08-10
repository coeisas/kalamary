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
public class FacDocumentodetallePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "cfg_producto_idProducto", nullable = false)
    private int cfgproductoidProducto;
    @Basic(optional = false)
    @Column(name = "fac_documentosmaster_cfg_documento_idDoc", nullable = false)
    private int facdocumentosmastercfgdocumentoidDoc;
    @Basic(optional = false)
    @Column(name = "fac_documentosmaster_numDocumento", nullable = false)
    private int facdocumentosmasternumDocumento;

    public FacDocumentodetallePK() {
    }

    public FacDocumentodetallePK(int cfgproductoidProducto, int facdocumentosmastercfgdocumentoidDoc, int facdocumentosmasternumDocumento) {
        this.cfgproductoidProducto = cfgproductoidProducto;
        this.facdocumentosmastercfgdocumentoidDoc = facdocumentosmastercfgdocumentoidDoc;
        this.facdocumentosmasternumDocumento = facdocumentosmasternumDocumento;
    }

    public int getCfgproductoidProducto() {
        return cfgproductoidProducto;
    }

    public void setCfgproductoidProducto(int cfgproductoidProducto) {
        this.cfgproductoidProducto = cfgproductoidProducto;
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
        hash += (int) cfgproductoidProducto;
        hash += (int) facdocumentosmastercfgdocumentoidDoc;
        hash += (int) facdocumentosmasternumDocumento;
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
        return "entities.FacDocumentodetallePK[ cfgproductoidProducto=" + cfgproductoidProducto + ", facdocumentosmastercfgdocumentoidDoc=" + facdocumentosmastercfgdocumentoidDoc + ", facdocumentosmasternumDocumento=" + facdocumentosmasternumDocumento + " ]";
    }
    
}
