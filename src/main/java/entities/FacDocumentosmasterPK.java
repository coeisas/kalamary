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
public class FacDocumentosmasterPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "cfg_documento_idDoc", nullable = false)
    private int cfgdocumentoidDoc;
    @Basic(optional = false)
    @Column(name = "numDocumento", nullable = false)
    private int numDocumento;

    public FacDocumentosmasterPK() {
    }

    public FacDocumentosmasterPK(int cfgdocumentoidDoc, int numDocumento) {
        this.cfgdocumentoidDoc = cfgdocumentoidDoc;
        this.numDocumento = numDocumento;
    }

    public int getCfgdocumentoidDoc() {
        return cfgdocumentoidDoc;
    }

    public void setCfgdocumentoidDoc(int cfgdocumentoidDoc) {
        this.cfgdocumentoidDoc = cfgdocumentoidDoc;
    }

    public int getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(int numDocumento) {
        this.numDocumento = numDocumento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cfgdocumentoidDoc;
        hash += (int) numDocumento;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacDocumentosmasterPK)) {
            return false;
        }
        FacDocumentosmasterPK other = (FacDocumentosmasterPK) object;
        if (this.cfgdocumentoidDoc != other.cfgdocumentoidDoc) {
            return false;
        }
        if (this.numDocumento != other.numDocumento) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return cfgdocumentoidDoc + "," + numDocumento;
    }

}
