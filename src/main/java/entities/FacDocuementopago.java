/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
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
@Table(name = "fac_docuementopago", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacDocuementopago.findAll", query = "SELECT f FROM FacDocuementopago f"),
    @NamedQuery(name = "FacDocuementopago.findByCfgformapagoidFormaPago", query = "SELECT f FROM FacDocuementopago f WHERE f.facDocuementopagoPK.cfgformapagoidFormaPago = :cfgformapagoidFormaPago"),
    @NamedQuery(name = "FacDocuementopago.findByValorPago", query = "SELECT f FROM FacDocuementopago f WHERE f.valorPago = :valorPago"),
    @NamedQuery(name = "FacDocuementopago.findByFacdocumentosmastercfgdocumentoidDoc", query = "SELECT f FROM FacDocuementopago f WHERE f.facDocuementopagoPK.facdocumentosmastercfgdocumentoidDoc = :facdocumentosmastercfgdocumentoidDoc"),
    @NamedQuery(name = "FacDocuementopago.findByFacdocumentosmasternumDocumento", query = "SELECT f FROM FacDocuementopago f WHERE f.facDocuementopagoPK.facdocumentosmasternumDocumento = :facdocumentosmasternumDocumento")})
public class FacDocuementopago implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacDocuementopagoPK facDocuementopagoPK;
    @Basic(optional = false)
    @Column(name = "valorPago", nullable = false)
    private float valorPago;
    @JoinColumn(name = "cfg_formapago_idFormaPago", referencedColumnName = "idFormaPago", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgFormapago cfgFormapago;
    @JoinColumns({
        @JoinColumn(name = "fac_documentosmaster_cfg_documento_idDoc", referencedColumnName = "cfg_documento_idDoc", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "fac_documentosmaster_numDocumento", referencedColumnName = "numDocumento", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private FacDocumentosmaster facDocumentosmaster;

    public FacDocuementopago() {
    }

    public FacDocuementopago(FacDocuementopagoPK facDocuementopagoPK) {
        this.facDocuementopagoPK = facDocuementopagoPK;
    }

    public FacDocuementopago(FacDocuementopagoPK facDocuementopagoPK, float valorPago) {
        this.facDocuementopagoPK = facDocuementopagoPK;
        this.valorPago = valorPago;
    }

    public FacDocuementopago(int cfgformapagoidFormaPago, int facdocumentosmastercfgdocumentoidDoc, int facdocumentosmasternumDocumento) {
        this.facDocuementopagoPK = new FacDocuementopagoPK(cfgformapagoidFormaPago, facdocumentosmastercfgdocumentoidDoc, facdocumentosmasternumDocumento);
    }

    public FacDocuementopagoPK getFacDocuementopagoPK() {
        return facDocuementopagoPK;
    }

    public void setFacDocuementopagoPK(FacDocuementopagoPK facDocuementopagoPK) {
        this.facDocuementopagoPK = facDocuementopagoPK;
    }

    public float getValorPago() {
        return valorPago;
    }

    public void setValorPago(float valorPago) {
        this.valorPago = valorPago;
    }

    public CfgFormapago getCfgFormapago() {
        return cfgFormapago;
    }

    public void setCfgFormapago(CfgFormapago cfgFormapago) {
        this.cfgFormapago = cfgFormapago;
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
        hash += (facDocuementopagoPK != null ? facDocuementopagoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacDocuementopago)) {
            return false;
        }
        FacDocuementopago other = (FacDocuementopago) object;
        if ((this.facDocuementopagoPK == null && other.facDocuementopagoPK != null) || (this.facDocuementopagoPK != null && !this.facDocuementopagoPK.equals(other.facDocuementopagoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FacDocuementopago[ facDocuementopagoPK=" + facDocuementopagoPK + " ]";
    }
    
}