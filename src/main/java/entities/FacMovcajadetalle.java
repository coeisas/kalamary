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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
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
    @NamedQuery(name = "FacMovcajadetalle.findByValor", query = "SELECT f FROM FacMovcajadetalle f WHERE f.valor = :valor"),
    @NamedQuery(name = "FacMovcajadetalle.findByFecha", query = "SELECT f FROM FacMovcajadetalle f WHERE f.fecha = :fecha"),
    @NamedQuery(name = "FacMovcajadetalle.findByNumRecibocaja", query = "SELECT f FROM FacMovcajadetalle f WHERE f.numRecibocaja = :numRecibocaja"),
    @NamedQuery(name = "FacMovcajadetalle.findByIdmovcajadetalle", query = "SELECT f FROM FacMovcajadetalle f WHERE f.idmovcajadetalle = :idmovcajadetalle")})
public class FacMovcajadetalle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor", nullable = false)
    private float valor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_recibocaja", nullable = false)
    private int numRecibocaja;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idmovcajadetalle", nullable = false)
    private Long idmovcajadetalle;
    @JoinColumn(name = "cfg_documento_idDoc", referencedColumnName = "idDoc", nullable = false)
    @ManyToOne(optional = false)
    private CfgDocumento cfgdocumentoidDoc;
    @JoinColumn(name = "cfg_formapago_idFormaPago", referencedColumnName = "idFormaPago", nullable = false)
    @ManyToOne(optional = false)
    private CfgFormapago cfgformapagoidFormaPago;
    @JoinColumns({
        @JoinColumn(name = "fac_documentosmaster_cfg_documento_idDoc", referencedColumnName = "cfg_documento_idDoc", nullable = false),
        @JoinColumn(name = "fac_documentosmaster_numDocumento", referencedColumnName = "numDocumento", nullable = false)})
    @ManyToOne(optional = false)
    private FacDocumentosmaster facDocumentosmaster;
    @JoinColumn(name = "fac_movcaja_idMovimiento", referencedColumnName = "idMovimiento", nullable = false)
    @ManyToOne(optional = false)
    private FacMovcaja facmovcajaidMovimiento;

    public FacMovcajadetalle() {
    }

    public FacMovcajadetalle(Long idmovcajadetalle) {
        this.idmovcajadetalle = idmovcajadetalle;
    }

    public FacMovcajadetalle(Long idmovcajadetalle, float valor, Date fecha, int numRecibocaja) {
        this.idmovcajadetalle = idmovcajadetalle;
        this.valor = valor;
        this.fecha = fecha;
        this.numRecibocaja = numRecibocaja;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getNumRecibocaja() {
        return numRecibocaja;
    }

    public void setNumRecibocaja(int numRecibocaja) {
        this.numRecibocaja = numRecibocaja;
    }

    public Long getIdmovcajadetalle() {
        return idmovcajadetalle;
    }

    public void setIdmovcajadetalle(Long idmovcajadetalle) {
        this.idmovcajadetalle = idmovcajadetalle;
    }

    public CfgDocumento getCfgdocumentoidDoc() {
        return cfgdocumentoidDoc;
    }

    public void setCfgdocumentoidDoc(CfgDocumento cfgdocumentoidDoc) {
        this.cfgdocumentoidDoc = cfgdocumentoidDoc;
    }

    public CfgFormapago getCfgformapagoidFormaPago() {
        return cfgformapagoidFormaPago;
    }

    public void setCfgformapagoidFormaPago(CfgFormapago cfgformapagoidFormaPago) {
        this.cfgformapagoidFormaPago = cfgformapagoidFormaPago;
    }

    public FacDocumentosmaster getFacDocumentosmaster() {
        return facDocumentosmaster;
    }

    public void setFacDocumentosmaster(FacDocumentosmaster facDocumentosmaster) {
        this.facDocumentosmaster = facDocumentosmaster;
    }

    public FacMovcaja getFacmovcajaidMovimiento() {
        return facmovcajaidMovimiento;
    }

    public void setFacmovcajaidMovimiento(FacMovcaja facmovcajaidMovimiento) {
        this.facmovcajaidMovimiento = facmovcajaidMovimiento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idmovcajadetalle != null ? idmovcajadetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacMovcajadetalle)) {
            return false;
        }
        FacMovcajadetalle other = (FacMovcajadetalle) object;
        if ((this.idmovcajadetalle == null && other.idmovcajadetalle != null) || (this.idmovcajadetalle != null && !this.idmovcajadetalle.equals(other.idmovcajadetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FacMovcajadetalle[ idmovcajadetalle=" + idmovcajadetalle + " ]";
    }
    
}
