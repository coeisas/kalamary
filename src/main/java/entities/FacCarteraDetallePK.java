/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author mario
 */
@Embeddable
public class FacCarteraDetallePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "fac_cartera_cliente_cfg_cliente_idCliente", nullable = false)
    private int faccarteraclientecfgclienteidCliente;
    @Basic(optional = false)
    @Column(name = "fac_cartera_cliente_fac_documentosmaster_cfg_documento_idDoc", nullable = false)
    private int faccarteraclientefacdocumentosmastercfgdocumentoidDoc;
    @Basic(optional = false)
    @Column(name = "fac_cartera_cliente_fac_documentosmaster_numDocumento", nullable = false)
    private int faccarteraclientefacdocumentosmasternumDocumento;
    @Basic(optional = false)
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    public FacCarteraDetallePK() {
    }

    public FacCarteraDetallePK(int faccarteraclientecfgclienteidCliente, int faccarteraclientefacdocumentosmastercfgdocumentoidDoc, int faccarteraclientefacdocumentosmasternumDocumento, Date fecha) {
        this.faccarteraclientecfgclienteidCliente = faccarteraclientecfgclienteidCliente;
        this.faccarteraclientefacdocumentosmastercfgdocumentoidDoc = faccarteraclientefacdocumentosmastercfgdocumentoidDoc;
        this.faccarteraclientefacdocumentosmasternumDocumento = faccarteraclientefacdocumentosmasternumDocumento;
        this.fecha = fecha;
    }

    public int getFaccarteraclientecfgclienteidCliente() {
        return faccarteraclientecfgclienteidCliente;
    }

    public void setFaccarteraclientecfgclienteidCliente(int faccarteraclientecfgclienteidCliente) {
        this.faccarteraclientecfgclienteidCliente = faccarteraclientecfgclienteidCliente;
    }

    public int getFaccarteraclientefacdocumentosmastercfgdocumentoidDoc() {
        return faccarteraclientefacdocumentosmastercfgdocumentoidDoc;
    }

    public void setFaccarteraclientefacdocumentosmastercfgdocumentoidDoc(int faccarteraclientefacdocumentosmastercfgdocumentoidDoc) {
        this.faccarteraclientefacdocumentosmastercfgdocumentoidDoc = faccarteraclientefacdocumentosmastercfgdocumentoidDoc;
    }

    public int getFaccarteraclientefacdocumentosmasternumDocumento() {
        return faccarteraclientefacdocumentosmasternumDocumento;
    }

    public void setFaccarteraclientefacdocumentosmasternumDocumento(int faccarteraclientefacdocumentosmasternumDocumento) {
        this.faccarteraclientefacdocumentosmasternumDocumento = faccarteraclientefacdocumentosmasternumDocumento;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) faccarteraclientecfgclienteidCliente;
        hash += (int) faccarteraclientefacdocumentosmastercfgdocumentoidDoc;
        hash += (int) faccarteraclientefacdocumentosmasternumDocumento;
        hash += (fecha != null ? fecha.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacCarteraDetallePK)) {
            return false;
        }
        FacCarteraDetallePK other = (FacCarteraDetallePK) object;
        if (this.faccarteraclientecfgclienteidCliente != other.faccarteraclientecfgclienteidCliente) {
            return false;
        }
        if (this.faccarteraclientefacdocumentosmastercfgdocumentoidDoc != other.faccarteraclientefacdocumentosmastercfgdocumentoidDoc) {
            return false;
        }
        if (this.faccarteraclientefacdocumentosmasternumDocumento != other.faccarteraclientefacdocumentosmasternumDocumento) {
            return false;
        }
        if ((this.fecha == null && other.fecha != null) || (this.fecha != null && !this.fecha.equals(other.fecha))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FacCarteraDetallePK[ faccarteraclientecfgclienteidCliente=" + faccarteraclientecfgclienteidCliente + ", faccarteraclientefacdocumentosmastercfgdocumentoidDoc=" + faccarteraclientefacdocumentosmastercfgdocumentoidDoc + ", faccarteraclientefacdocumentosmasternumDocumento=" + faccarteraclientefacdocumentosmasternumDocumento + ", fecha=" + fecha + " ]";
    }
    
}
