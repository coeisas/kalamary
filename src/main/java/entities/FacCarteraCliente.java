/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "fac_cartera_cliente", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacCarteraCliente.findAll", query = "SELECT f FROM FacCarteraCliente f"),
    @NamedQuery(name = "FacCarteraCliente.findByCfgclienteidCliente", query = "SELECT f FROM FacCarteraCliente f WHERE f.facCarteraClientePK.cfgclienteidCliente = :cfgclienteidCliente"),
    @NamedQuery(name = "FacCarteraCliente.findByFacdocumentosmastercfgdocumentoidDoc", query = "SELECT f FROM FacCarteraCliente f WHERE f.facCarteraClientePK.facdocumentosmastercfgdocumentoidDoc = :facdocumentosmastercfgdocumentoidDoc"),
    @NamedQuery(name = "FacCarteraCliente.findByFacdocumentosmasternumDocumento", query = "SELECT f FROM FacCarteraCliente f WHERE f.facCarteraClientePK.facdocumentosmasternumDocumento = :facdocumentosmasternumDocumento"),
    @NamedQuery(name = "FacCarteraCliente.findByValor", query = "SELECT f FROM FacCarteraCliente f WHERE f.valor = :valor"),
    @NamedQuery(name = "FacCarteraCliente.findBySaldo", query = "SELECT f FROM FacCarteraCliente f WHERE f.saldo = :saldo"),
    @NamedQuery(name = "FacCarteraCliente.findByTotalcuotas", query = "SELECT f FROM FacCarteraCliente f WHERE f.totalcuotas = :totalcuotas"),
    @NamedQuery(name = "FacCarteraCliente.findByCuotaactual", query = "SELECT f FROM FacCarteraCliente f WHERE f.cuotaactual = :cuotaactual"),
    @NamedQuery(name = "FacCarteraCliente.findByEstado", query = "SELECT f FROM FacCarteraCliente f WHERE f.estado = :estado")})
public class FacCarteraCliente implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacCarteraClientePK facCarteraClientePK;
    @Basic(optional = false)
    @Column(name = "valor", nullable = false)
    private float valor;
    @Basic(optional = false)
    @Column(name = "saldo", nullable = false)
    private float saldo;
    @Basic(optional = false)
    @Column(name = "totalcuotas", nullable = false)
    private int totalcuotas;
    @Basic(optional = false)
    @Column(name = "cuotaactual", nullable = false)
    private int cuotaactual;
    @Basic(optional = false)
    @Column(name = "estado", nullable = false, length = 45)
    private String estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facCarteraCliente")
    private List<FacCarteraDetalle> facCarteraDetalleList;
    @JoinColumn(name = "cfg_cliente_idCliente", referencedColumnName = "idCliente", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgCliente cfgCliente;
    @JoinColumns({
        @JoinColumn(name = "fac_documentosmaster_cfg_documento_idDoc", referencedColumnName = "cfg_documento_idDoc", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "fac_documentosmaster_numDocumento", referencedColumnName = "numDocumento", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private FacDocumentosmaster facDocumentosmaster;

    public FacCarteraCliente() {
    }

    public FacCarteraCliente(FacCarteraClientePK facCarteraClientePK) {
        this.facCarteraClientePK = facCarteraClientePK;
    }

    public FacCarteraCliente(FacCarteraClientePK facCarteraClientePK, float valor, float saldo, int totalcuotas, int cuotaactual, String estado) {
        this.facCarteraClientePK = facCarteraClientePK;
        this.valor = valor;
        this.saldo = saldo;
        this.totalcuotas = totalcuotas;
        this.cuotaactual = cuotaactual;
        this.estado = estado;
    }

    public FacCarteraCliente(int cfgclienteidCliente, int facdocumentosmastercfgdocumentoidDoc, int facdocumentosmasternumDocumento) {
        this.facCarteraClientePK = new FacCarteraClientePK(cfgclienteidCliente, facdocumentosmastercfgdocumentoidDoc, facdocumentosmasternumDocumento);
    }

    public FacCarteraClientePK getFacCarteraClientePK() {
        return facCarteraClientePK;
    }

    public void setFacCarteraClientePK(FacCarteraClientePK facCarteraClientePK) {
        this.facCarteraClientePK = facCarteraClientePK;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public int getTotalcuotas() {
        return totalcuotas;
    }

    public void setTotalcuotas(int totalcuotas) {
        this.totalcuotas = totalcuotas;
    }

    public int getCuotaactual() {
        return cuotaactual;
    }

    public void setCuotaactual(int cuotaactual) {
        this.cuotaactual = cuotaactual;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<FacCarteraDetalle> getFacCarteraDetalleList() {
        return facCarteraDetalleList;
    }

    public void setFacCarteraDetalleList(List<FacCarteraDetalle> facCarteraDetalleList) {
        this.facCarteraDetalleList = facCarteraDetalleList;
    }

    public CfgCliente getCfgCliente() {
        return cfgCliente;
    }

    public void setCfgCliente(CfgCliente cfgCliente) {
        this.cfgCliente = cfgCliente;
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
        hash += (facCarteraClientePK != null ? facCarteraClientePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacCarteraCliente)) {
            return false;
        }
        FacCarteraCliente other = (FacCarteraCliente) object;
        if ((this.facCarteraClientePK == null && other.facCarteraClientePK != null) || (this.facCarteraClientePK != null && !this.facCarteraClientePK.equals(other.facCarteraClientePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FacCarteraCliente[ facCarteraClientePK=" + facCarteraClientePK + " ]";
    }
    
}
