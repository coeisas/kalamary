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
    @NamedQuery(name = "FacDocumentosmaster.findByTotalFactura", query = "SELECT f FROM FacDocumentosmaster f WHERE f.totalFactura = :totalFactura"),
    @NamedQuery(name = "FacDocumentosmaster.findByFecCrea", query = "SELECT f FROM FacDocumentosmaster f WHERE f.fecCrea = :fecCrea"),
    @NamedQuery(name = "FacDocumentosmaster.findByEstado", query = "SELECT f FROM FacDocumentosmaster f WHERE f.estado = :estado"),
    @NamedQuery(name = "FacDocumentosmaster.findByIddocumentomaster", query = "SELECT f FROM FacDocumentosmaster f WHERE f.iddocumentomaster = :iddocumentomaster"),
    @NamedQuery(name = "FacDocumentosmaster.findByTotalFacturaUSD", query = "SELECT f FROM FacDocumentosmaster f WHERE f.totalFacturaUSD = :totalFacturaUSD"),
    @NamedQuery(name = "FacDocumentosmaster.findBySubtotal", query = "SELECT f FROM FacDocumentosmaster f WHERE f.subtotal = :subtotal"),
    @NamedQuery(name = "FacDocumentosmaster.findByDescuento", query = "SELECT f FROM FacDocumentosmaster f WHERE f.descuento = :descuento")})
public class FacDocumentosmaster implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numDocumento", nullable = false)
    private int numDocumento;
    @Basic(optional = false)
    @Column(name = "totalFactura", nullable = false)
    private float totalFactura;
    @Lob
    @Size(max = 65535)
    @Column(name = "observaciones", length = 65535)
    private String observaciones;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecCrea", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCrea;
    @Column(name = "estado", length = 10)
    private String estado;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "iddocumentomaster", nullable = false)
    private Long iddocumentomaster;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "totalFacturaUSD", precision = 12)
    private Float totalFacturaUSD;
    @Column(name = "subtotal", precision = 12)
    private Float subtotal;
    @Column(name = "descuento", precision = 12)
    private Float descuento;
    @JoinColumn(name = "cfg_cliente_idCliente", referencedColumnName = "idCliente", nullable = false)
    @ManyToOne(optional = false)
    private CfgCliente cfgclienteidCliente;
    @JoinColumn(name = "cfg_documento_idDoc", referencedColumnName = "idDoc", nullable = false)
    @ManyToOne(optional = false)
    private CfgDocumento cfgdocumentoidDoc;
    @JoinColumn(name = "cfg_empresasede_idSede", referencedColumnName = "idSede", nullable = false)
    @ManyToOne(optional = false)
    private CfgEmpresasede cfgempresasedeidSede;
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

    public FacDocumentosmaster(Long iddocumentomaster, int numDocumento, float totalFactura, Date fecCrea) {
        this.iddocumentomaster = iddocumentomaster;
        this.numDocumento = numDocumento;
        this.totalFactura = totalFactura;
        this.fecCrea = fecCrea;
    }

    public int getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(int numDocumento) {
        this.numDocumento = numDocumento;
    }

    public float getTotalFactura() {
        return totalFactura;
    }

    public void setTotalFactura(float totalFactura) {
        this.totalFactura = totalFactura;
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

    public Float getTotalFacturaUSD() {
        return totalFacturaUSD;
    }

    public void setTotalFacturaUSD(Float totalFacturaUSD) {
        this.totalFacturaUSD = totalFacturaUSD;
    }

    public Float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Float subtotal) {
        this.subtotal = subtotal;
    }

    public Float getDescuento() {
        return descuento;
    }

    public void setDescuento(Float descuento) {
        this.descuento = descuento;
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

    public CfgEmpresasede getCfgempresasedeidSede() {
        return cfgempresasedeidSede;
    }

    public void setCfgempresasedeidSede(CfgEmpresasede cfgempresasedeidSede) {
        this.cfgempresasedeidSede = cfgempresasedeidSede;
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
