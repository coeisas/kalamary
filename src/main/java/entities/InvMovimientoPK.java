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
public class InvMovimientoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "cfg_documento_idDoc", nullable = false)
    private int cfgdocumentoidDoc;
    @Basic(optional = false)
    @Column(name = "numDoc", nullable = false)
    private int numDoc;

    public InvMovimientoPK() {
    }

    public InvMovimientoPK(int cfgdocumentoidDoc, int numDoc) {
        this.cfgdocumentoidDoc = cfgdocumentoidDoc;
        this.numDoc = numDoc;
    }

    public int getCfgdocumentoidDoc() {
        return cfgdocumentoidDoc;
    }

    public void setCfgdocumentoidDoc(int cfgdocumentoidDoc) {
        this.cfgdocumentoidDoc = cfgdocumentoidDoc;
    }

    public int getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(int numDoc) {
        this.numDoc = numDoc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cfgdocumentoidDoc;
        hash += (int) numDoc;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvMovimientoPK)) {
            return false;
        }
        InvMovimientoPK other = (InvMovimientoPK) object;
        if (this.cfgdocumentoidDoc != other.cfgdocumentoidDoc) {
            return false;
        }
        if (this.numDoc != other.numDoc) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.InvMovimientoPK[ cfgdocumentoidDoc=" + cfgdocumentoidDoc + ", numDoc=" + numDoc + " ]";
    }
    
}
