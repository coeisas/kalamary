/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "fac_documentosmaster", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacDocumentosmaster.findAll", query = "SELECT f FROM FacDocumentosmaster f"),
    @NamedQuery(name = "FacDocumentosmaster.findByNumDocumento", query = "SELECT f FROM FacDocumentosmaster f WHERE f.numDocumento = :numDocumento"),
    @NamedQuery(name = "FacDocumentosmaster.findByValorFactura", query = "SELECT f FROM FacDocumentosmaster f WHERE f.valorFactura = :valorFactura"),
    @NamedQuery(name = "FacDocumentosmaster.findByFecCrea", query = "SELECT f FROM FacDocumentosmaster f WHERE f.fecCrea = :fecCrea"),
    @NamedQuery(name = "FacDocumentosmaster.findByEstado", query = "SELECT f FROM FacDocumentosmaster f WHERE f.estado = :estado"),
    @NamedQuery(name = "FacDocumentosmaster.findByIddocumentomaster", query = "SELECT f FROM FacDocumentosmaster f WHERE f.iddocumentomaster = :iddocumentomaster")})
public class FacDocumentosmaster implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numDocumento", nullable = false)
    private int numDocumento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valorFactura", nullable = false)
    private float valorFactura;
    @Lob
    @Size(max = 65535)
    @Column(name = "observaciones", length = 65535)
    private String observaciones;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecCrea", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCrea;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "estado", nullable = false, length = 10)
    private String estado;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "iddocumentomaster", nullable = false)
    private Long iddocumentomaster;
    @JoinColumn(name = "cfg_cliente_idCliente", referencedColumnName = "idCliente", nullable = false)
    @ManyToOne(optional = false)
    private CfgCliente cfgclienteidCliente;
    @JoinColumn(name = "cfg_documento_idDoc", referencedColumnName = "idDoc", nullable = false)
    @ManyToOne(optional = false)
    private CfgDocumento cfgdocumentoidDoc;
    @JoinColumn(name = "cfg_kitproductomaestro_idKit", referencedColumnName = "idKit")
    @ManyToOne
    private CfgKitproductomaestro cfgkitproductomaestroidKit;
    @JoinColumn(name = "seg_usuario_idUsuario", referencedColumnName = "idUsuario", nullable = false)
    @ManyToOne(optional = false)
    private SegUsuario segusuarioidUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facDocumentosmaster")
    private List<FacDocumentodetalle> facDocumentodetalleList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facDocumentosmasterIddocumentomaster")
    private List<FacMovcajadetalle> facMovcajadetalleList;

    public FacDocumentosmaster() {
    }

    public FacDocumentosmaster(Long iddocumentomaster) {
        this.iddocumentomaster = iddocumentomaster;
    }

    public FacDocumentosmaster(Long iddocumentomaster, int numDocumento, float valorFactura, Date fecCrea, String estado) {
        this.iddocumentomaster = iddocumentomaster;
        this.numDocumento = numDocumento;
        this.valorFactura = valorFactura;
        this.fecCrea = fecCrea;
        this.estado = estado;
    }

    public int getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(int numDocumento) {
        this.numDocumento = numDocumento;
    }

    public float getValorFactura() {
        return valorFactura;
    }

    public void setValorFactura(float valorFactura) {
        this.valorFactura = valorFactura;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFecCrea() {
        return fecCrea;
    }

    public void setFecCrea(Date fecCrea) {
        this.fecCrea = fecCrea;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getIddocumentomaster() {
        return iddocumentomaster;
    }

    public void setIddocumentomaster(Long iddocumentomaster) {
        this.iddocumentomaster = iddocumentomaster;
    }

    public CfgCliente getCfgclienteidCliente() {
        return cfgclienteidCliente;
    }

    public void setCfgclienteidCliente(CfgCliente cfgclienteidCliente) {
        this.cfgclienteidCliente = cfgclienteidCliente;
    }

    public CfgDocumento getCfgdocumentoidDoc() {
        return cfgdocumentoidDoc;
    }

    public void setCfgdocumentoidDoc(CfgDocumento cfgdocumentoidDoc) {
        this.cfgdocumentoidDoc = cfgdocumentoidDoc;
    }

    public CfgKitproductomaestro getCfgkitproductomaestroidKit() {
        return cfgkitproductomaestroidKit;
    }

    public void setCfgkitproductomaestroidKit(CfgKitproductomaestro cfgkitproductomaestroidKit) {
        this.cfgkitproductomaestroidKit = cfgkitproductomaestroidKit;
    }

    public SegUsuario getSegusuarioidUsuario() {
        return segusuarioidUsuario;
    }

    public void setSegusuarioidUsuario(SegUsuario segusuarioidUsuario) {
        this.segusuarioidUsuario = segusuarioidUsuario;
    }

    @XmlTransient
    public List<FacDocumentodetalle> getFacDocumentodetalleList() {
        return facDocumentodetalleList;
    }

    public void setFacDocumentodetalleList(List<FacDocumentodetalle> facDocumentodetalleList) {
        this.facDocumentodetalleList = facDocumentodetalleList;
    }

    @XmlTransient
    public List<FacMovcajadetalle> getFacMovcajadetalleList() {
        return facMovcajadetalleList;
    }

    public void setFacMovcajadetalleList(List<FacMovcajadetalle> facMovcajadetalleList) {
        this.facMovcajadetalleList = facMovcajadetalleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iddocumentomaster != null ? iddocumentomaster.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacDocumentosmaster)) {
            return false;
        }
        FacDocumentosmaster other = (FacDocumentosmaster) object;
        if ((this.iddocumentomaster == null && other.iddocumentomaster != null) || (this.iddocumentomaster != null && !this.iddocumentomaster.equals(other.iddocumentomaster))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FacDocumentosmaster[ iddocumentomaster=" + iddocumentomaster + " ]";
    }
    
}
