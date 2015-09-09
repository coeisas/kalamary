/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    @NamedQuery(name = "FacDocumentosmaster.findByCfgdocumentoidDoc", query = "SELECT f FROM FacDocumentosmaster f WHERE f.facDocumentosmasterPK.cfgdocumentoidDoc = :cfgdocumentoidDoc"),
    @NamedQuery(name = "FacDocumentosmaster.findByNumDocumento", query = "SELECT f FROM FacDocumentosmaster f WHERE f.facDocumentosmasterPK.numDocumento = :numDocumento"),
    @NamedQuery(name = "FacDocumentosmaster.findByTotalFactura", query = "SELECT f FROM FacDocumentosmaster f WHERE f.totalFactura = :totalFactura"),
    @NamedQuery(name = "FacDocumentosmaster.findByFecCrea", query = "SELECT f FROM FacDocumentosmaster f WHERE f.fecCrea = :fecCrea"),
    @NamedQuery(name = "FacDocumentosmaster.findByEstado", query = "SELECT f FROM FacDocumentosmaster f WHERE f.estado = :estado"),
    @NamedQuery(name = "FacDocumentosmaster.findByTotalFacturaUSD", query = "SELECT f FROM FacDocumentosmaster f WHERE f.totalFacturaUSD = :totalFacturaUSD"),
    @NamedQuery(name = "FacDocumentosmaster.findBySubtotal", query = "SELECT f FROM FacDocumentosmaster f WHERE f.subtotal = :subtotal"),
    @NamedQuery(name = "FacDocumentosmaster.findByDescuento", query = "SELECT f FROM FacDocumentosmaster f WHERE f.descuento = :descuento")})
public class FacDocumentosmaster implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacDocumentosmasterPK facDocumentosmasterPK;
    @Basic(optional = false)
    @Column(name = "totalFactura", nullable = false)
    private float totalFactura;
    @Lob
    @Column(name = "observaciones", length = 65535)
    private String observaciones;
    @Basic(optional = false)
    @Column(name = "fecCrea", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCrea;
    @Column(name = "estado", length = 10)
    private String estado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "totalFacturaUSD", precision = 12)
    private Float totalFacturaUSD;
    @Column(name = "subtotal", precision = 12)
    private Float subtotal;
    @Column(name = "descuento", precision = 12)
    private Float descuento;
    @OneToMany(mappedBy = "facDocumentosmaster")
    private List<InvMovimiento> invMovimientoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facDocumentosmaster")
    private List<FacDocuementopago> facDocuementopagoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facDocumentosmaster")
    private List<FacDocumentoimpuesto> facDocumentoimpuestoList;
    @JoinColumn(name = "cfg_cliente_idCliente", referencedColumnName = "idCliente", nullable = false)
    @ManyToOne(optional = false)
    private CfgCliente cfgclienteidCliente;
    @JoinColumn(name = "cfg_documento_idDoc", referencedColumnName = "idDoc", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgDocumento cfgDocumento;
    @JoinColumn(name = "cfg_empresasede_idSede", referencedColumnName = "idSede", nullable = false)
    @ManyToOne(optional = false)
    private CfgEmpresasede cfgempresasedeidSede;
    @JoinColumn(name = "cfg_kitproductomaestro_idKit", referencedColumnName = "idKit")
    @ManyToOne
    private CfgKitproductomaestro cfgkitproductomaestroidKit;
    @JoinColumn(name = "fac_caja_idCaja", referencedColumnName = "idCaja")
    @ManyToOne
    private FacCaja faccajaidCaja;
    @OneToMany(mappedBy = "facDocumentosmaster")
    private List<FacDocumentosmaster> facDocumentosmasterList;
    @JoinColumns({
        @JoinColumn(name = "fac_documentosmaster_cfg_documento_idDoc", referencedColumnName = "cfg_documento_idDoc"),
        @JoinColumn(name = "fac_documentosmaster_numDocumento", referencedColumnName = "numDocumento")})
    @ManyToOne
    private FacDocumentosmaster facDocumentosmaster;
    @JoinColumn(name = "seg_usuario_idUsuario", referencedColumnName = "idUsuario", nullable = false)
    @ManyToOne(optional = false)
    private SegUsuario segusuarioidUsuario;
    @JoinColumn(name = "seg_usuario_idUsuario1", referencedColumnName = "idUsuario")
    @ManyToOne
    private SegUsuario segusuarioidUsuario1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facDocumentosmaster")
    private List<FacDocumentodetalle> facDocumentodetalleList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facDocumentosmaster")
    private List<FacMovcajadetalle> facMovcajadetalleList;

    public FacDocumentosmaster() {
    }

    public FacDocumentosmaster(FacDocumentosmasterPK facDocumentosmasterPK) {
        this.facDocumentosmasterPK = facDocumentosmasterPK;
    }

    public FacDocumentosmaster(FacDocumentosmasterPK facDocumentosmasterPK, float totalFactura, Date fecCrea) {
        this.facDocumentosmasterPK = facDocumentosmasterPK;
        this.totalFactura = totalFactura;
        this.fecCrea = fecCrea;
    }

    public FacDocumentosmaster(int cfgdocumentoidDoc, int numDocumento) {
        this.facDocumentosmasterPK = new FacDocumentosmasterPK(cfgdocumentoidDoc, numDocumento);
    }

    public FacDocumentosmasterPK getFacDocumentosmasterPK() {
        return facDocumentosmasterPK;
    }

    public void setFacDocumentosmasterPK(FacDocumentosmasterPK facDocumentosmasterPK) {
        this.facDocumentosmasterPK = facDocumentosmasterPK;
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

    @XmlTransient
    public List<InvMovimiento> getInvMovimientoList() {
        return invMovimientoList;
    }

    public void setInvMovimientoList(List<InvMovimiento> invMovimientoList) {
        this.invMovimientoList = invMovimientoList;
    }

    @XmlTransient
    public List<FacDocuementopago> getFacDocuementopagoList() {
        return facDocuementopagoList;
    }

    public void setFacDocuementopagoList(List<FacDocuementopago> facDocuementopagoList) {
        this.facDocuementopagoList = facDocuementopagoList;
    }

    @XmlTransient
    public List<FacDocumentoimpuesto> getFacDocumentoimpuestoList() {
        return facDocumentoimpuestoList;
    }

    public void setFacDocumentoimpuestoList(List<FacDocumentoimpuesto> facDocumentoimpuestoList) {
        this.facDocumentoimpuestoList = facDocumentoimpuestoList;
    }

    public CfgCliente getCfgclienteidCliente() {
        return cfgclienteidCliente;
    }

    public void setCfgclienteidCliente(CfgCliente cfgclienteidCliente) {
        this.cfgclienteidCliente = cfgclienteidCliente;
    }

    public CfgDocumento getCfgDocumento() {
        return cfgDocumento;
    }

    public void setCfgDocumento(CfgDocumento cfgDocumento) {
        this.cfgDocumento = cfgDocumento;
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

    public FacCaja getFaccajaidCaja() {
        return faccajaidCaja;
    }

    public void setFaccajaidCaja(FacCaja faccajaidCaja) {
        this.faccajaidCaja = faccajaidCaja;
    }

    @XmlTransient
    public List<FacDocumentosmaster> getFacDocumentosmasterList() {
        return facDocumentosmasterList;
    }

    public void setFacDocumentosmasterList(List<FacDocumentosmaster> facDocumentosmasterList) {
        this.facDocumentosmasterList = facDocumentosmasterList;
    }

    public FacDocumentosmaster getFacDocumentosmaster() {
        return facDocumentosmaster;
    }

    public void setFacDocumentosmaster(FacDocumentosmaster facDocumentosmaster) {
        this.facDocumentosmaster = facDocumentosmaster;
    }

    public SegUsuario getSegusuarioidUsuario() {
        return segusuarioidUsuario;
    }

    public void setSegusuarioidUsuario(SegUsuario segusuarioidUsuario) {
        this.segusuarioidUsuario = segusuarioidUsuario;
    }

    public SegUsuario getSegusuarioidUsuario1() {
        return segusuarioidUsuario1;
    }

    public void setSegusuarioidUsuario1(SegUsuario segusuarioidUsuario1) {
        this.segusuarioidUsuario1 = segusuarioidUsuario1;
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

    public String formasPagoString() {
        String pago = "";
        for (FacDocuementopago fd : facDocuementopagoList) {
            pago += fd.getCfgFormapago().getAbreviatura() + " ";
        }
        return pago;
    }
    
    public String determinarNumFactura(){
        String numfac = "";
        int fin = cfgDocumento.getFinDocumento();
        String aux = String.valueOf(fin);
        String numDocumento = String.valueOf(getFacDocumentosmasterPK().getNumDocumento());
        return cfgDocumento.getPrefijoDoc().concat(Strings.padStart(numDocumento, aux.length(), '0'));       
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facDocumentosmasterPK != null ? facDocumentosmasterPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacDocumentosmaster)) {
            return false;
        }
        FacDocumentosmaster other = (FacDocumentosmaster) object;
        if ((this.facDocumentosmasterPK == null && other.facDocumentosmasterPK != null) || (this.facDocumentosmasterPK != null && !this.facDocumentosmasterPK.equals(other.facDocumentosmasterPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FacDocumentosmaster[ facDocumentosmasterPK=" + facDocumentosmasterPK + " ]";
    }

}
