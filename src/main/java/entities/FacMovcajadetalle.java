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
@Table(name = "fac_movcajadetalle", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacMovcajadetalle.findAll", query = "SELECT f FROM FacMovcajadetalle f"),
    @NamedQuery(name = "FacMovcajadetalle.findByFacmovcajaidMovimiento", query = "SELECT f FROM FacMovcajadetalle f WHERE f.facMovcajadetallePK.facmovcajaidMovimiento = :facmovcajaidMovimiento"),
    @NamedQuery(name = "FacMovcajadetalle.findByFacdocumentosmastercfgdocumentoidDoc", query = "SELECT f FROM FacMovcajadetalle f WHERE f.facMovcajadetallePK.facdocumentosmastercfgdocumentoidDoc = :facdocumentosmastercfgdocumentoidDoc"),
    @NamedQuery(name = "FacMovcajadetalle.findByFacdocumentosmasternumDocumento", query = "SELECT f FROM FacMovcajadetalle f WHERE f.facMovcajadetallePK.facdocumentosmasternumDocumento = :facdocumentosmasternumDocumento"),
    @NamedQuery(name = "FacMovcajadetalle.findByCfgformapagoidFormaPago", query = "SELECT f FROM FacMovcajadetalle f WHERE f.facMovcajadetallePK.cfgformapagoidFormaPago = :cfgformapagoidFormaPago"),
    @NamedQuery(name = "FacMovcajadetalle.findByFecha", query = "SELECT f FROM FacMovcajadetalle f WHERE f.facMovcajadetallePK.fecha = :fecha"),
    @NamedQuery(name = "FacMovcajadetalle.findByValor", query = "SELECT f FROM FacMovcajadetalle f WHERE f.valor = :valor")})
public class FacMovcajadetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacMovcajadetallePK facMovcajadetallePK;
    @Basic(optional = false)
    @Column(name = "valor", nullable = false)
    private float valor;
    @JoinColumn(name = "cfg_formapago_idFormaPago", referencedColumnName = "idFormaPago", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgFormapago cfgFormapago;
    @JoinColumns({
        @JoinColumn(name = "fac_documentosmaster_cfg_documento_idDoc", referencedColumnName = "cfg_documento_idDoc", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "fac_documentosmaster_numDocumento", referencedColumnName = "numDocumento", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private FacDocumentosmaster facDocumentosmaster;
    @JoinColumn(name = "fac_movcaja_idMovimiento", referencedColumnName = "idMovimiento", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FacMovcaja facMovcaja;

    public FacMovcajadetalle() {
    }

    public FacMovcajadetalle(FacMovcajadetallePK facMovcajadetallePK) {
        this.facMovcajadetallePK = facMovcajadetallePK;
    }

    public FacMovcajadetalle(FacMovcajadetallePK facMovcajadetallePK, float valor) {
        this.facMovcajadetallePK = facMovcajadetallePK;
        this.valor = valor;
    }

    public FacMovcajadetalle(int facmovcajaidMovimiento, int facdocumentosmastercfgdocumentoidDoc, int facdocumentosmasternumDocumento, int cfgformapagoidFormaPago, Date fecha) {
        this.facMovcajadetallePK = new FacMovcajadetallePK(facmovcajaidMovimiento, facdocumentosmastercfgdocumentoidDoc, facdocumentosmasternumDocumento, cfgformapagoidFormaPago, fecha);
    }

    public FacMovcajadetallePK getFacMovcajadetallePK() {
        return facMovcajadetallePK;
    }

    public void setFacMovcajadetallePK(FacMovcajadetallePK facMovcajadetallePK) {
        this.facMovcajadetallePK = facMovcajadetallePK;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
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

    public FacMovcaja getFacMovcaja() {
        return facMovcaja;
    }

    public void setFacMovcaja(FacMovcaja facMovcaja) {
        this.facMovcaja = facMovcaja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facMovcajadetallePK != null ? facMovcajadetallePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacMovcajadetalle)) {
            return false;
        }
        FacMovcajadetalle other = (FacMovcajadetalle) object;
        if ((this.facMovcajadetallePK == null && other.facMovcajadetallePK != null) || (this.facMovcajadetallePK != null && !this.facMovcajadetallePK.equals(other.facMovcajadetallePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FacMovcajadetalle[ facMovcajadetallePK=" + facMovcajadetallePK + " ]";
    }
    
}
