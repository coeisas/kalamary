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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "fac_cartera_detalle", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacCarteraDetalle.findAll", query = "SELECT f FROM FacCarteraDetalle f"),
    @NamedQuery(name = "FacCarteraDetalle.findByFaccarteraclientecfgclienteidCliente", query = "SELECT f FROM FacCarteraDetalle f WHERE f.facCarteraDetallePK.faccarteraclientecfgclienteidCliente = :faccarteraclientecfgclienteidCliente"),
    @NamedQuery(name = "FacCarteraDetalle.findByFaccarteraclientefacdocumentosmastercfgdocumentoidDoc", query = "SELECT f FROM FacCarteraDetalle f WHERE f.facCarteraDetallePK.faccarteraclientefacdocumentosmastercfgdocumentoidDoc = :faccarteraclientefacdocumentosmastercfgdocumentoidDoc"),
    @NamedQuery(name = "FacCarteraDetalle.findByFaccarteraclientefacdocumentosmasternumDocumento", query = "SELECT f FROM FacCarteraDetalle f WHERE f.facCarteraDetallePK.faccarteraclientefacdocumentosmasternumDocumento = :faccarteraclientefacdocumentosmasternumDocumento"),
    @NamedQuery(name = "FacCarteraDetalle.findByFecha", query = "SELECT f FROM FacCarteraDetalle f WHERE f.facCarteraDetallePK.fecha = :fecha"),
    @NamedQuery(name = "FacCarteraDetalle.findByAbono", query = "SELECT f FROM FacCarteraDetalle f WHERE f.abono = :abono"),
    @NamedQuery(name = "FacCarteraDetalle.findBySaldo", query = "SELECT f FROM FacCarteraDetalle f WHERE f.saldo = :saldo")})
public class FacCarteraDetalle implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacCarteraDetallePK facCarteraDetallePK;
    @Basic(optional = false)
    @Column(name = "abono", nullable = false)
    private float abono;
    @Basic(optional = false)
    @Column(name = "saldo", nullable = false)
    private float saldo;
    @JoinColumns({
        @JoinColumn(name = "fac_cartera_cliente_cfg_cliente_idCliente", referencedColumnName = "cfg_cliente_idCliente", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "fac_cartera_cliente_fac_documentosmaster_cfg_documento_idDoc", referencedColumnName = "fac_documentosmaster_cfg_documento_idDoc", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "fac_cartera_cliente_fac_documentosmaster_numDocumento", referencedColumnName = "fac_documentosmaster_numDocumento", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private FacCarteraCliente facCarteraCliente;
    @JoinColumns({
        @JoinColumn(name = "fac_documentosmaster_cfg_documento_idDoc", referencedColumnName = "cfg_documento_idDoc", nullable = false),
        @JoinColumn(name = "fac_documentosmaster_numDocumento", referencedColumnName = "numDocumento", nullable = false)})
    @ManyToOne(optional = false)
    private FacDocumentosmaster facDocumentosmaster;

    public FacCarteraDetalle() {
    }

    public FacCarteraDetalle(FacCarteraDetallePK facCarteraDetallePK) {
        this.facCarteraDetallePK = facCarteraDetallePK;
    }

    public FacCarteraDetalle(FacCarteraDetallePK facCarteraDetallePK, float abono, float saldo) {
        this.facCarteraDetallePK = facCarteraDetallePK;
        this.abono = abono;
        this.saldo = saldo;
    }

    public FacCarteraDetalle(int faccarteraclientecfgclienteidCliente, int faccarteraclientefacdocumentosmastercfgdocumentoidDoc, int faccarteraclientefacdocumentosmasternumDocumento, Date fecha) {
        this.facCarteraDetallePK = new FacCarteraDetallePK(faccarteraclientecfgclienteidCliente, faccarteraclientefacdocumentosmastercfgdocumentoidDoc, faccarteraclientefacdocumentosmasternumDocumento, fecha);
    }

    public FacCarteraDetallePK getFacCarteraDetallePK() {
        return facCarteraDetallePK;
    }

    public void setFacCarteraDetallePK(FacCarteraDetallePK facCarteraDetallePK) {
        this.facCarteraDetallePK = facCarteraDetallePK;
    }

    public float getAbono() {
        return abono;
    }

    public void setAbono(float abono) {
        this.abono = abono;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public FacCarteraCliente getFacCarteraCliente() {
        return facCarteraCliente;
    }

    public void setFacCarteraCliente(FacCarteraCliente facCarteraCliente) {
        this.facCarteraCliente = facCarteraCliente;
    }

    public FacDocumentosmaster getFacDocumentosmaster() {
        return facDocumentosmaster;
    }

    public void setFacDocumentosmaster(FacDocumentosmaster facDocumentosmaster) {
        this.facDocumentosmaster = facDocumentosmaster;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facCarteraDetallePK != null ? facCarteraDetallePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacCarteraDetalle)) {
            return false;
        }
        FacCarteraDetalle other = (FacCarteraDetalle) object;
        if ((this.facCarteraDetallePK == null && other.facCarteraDetallePK != null) || (this.facCarteraDetallePK != null && !this.facCarteraDetallePK.equals(other.facCarteraDetallePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FacCarteraDetalle[ facCarteraDetallePK=" + facCarteraDetallePK + " ]";
    }

}
