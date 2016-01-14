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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mario
 */
@Entity
@Table(name = "cnt_movdetalle", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CntMovdetalle.findAll", query = "SELECT c FROM CntMovdetalle c"),
    @NamedQuery(name = "CntMovdetalle.findByIdcntmovDetalle", query = "SELECT c FROM CntMovdetalle c WHERE c.idcntmovDetalle = :idcntmovDetalle"),
    @NamedQuery(name = "CntMovdetalle.findByFecha", query = "SELECT c FROM CntMovdetalle c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "CntMovdetalle.findByTercero", query = "SELECT c FROM CntMovdetalle c WHERE c.tercero = :tercero"),
    @NamedQuery(name = "CntMovdetalle.findByDebito", query = "SELECT c FROM CntMovdetalle c WHERE c.debito = :debito"),
    @NamedQuery(name = "CntMovdetalle.findByCredito", query = "SELECT c FROM CntMovdetalle c WHERE c.credito = :credito"),
    @NamedQuery(name = "CntMovdetalle.findByTotal", query = "SELECT c FROM CntMovdetalle c WHERE c.total = :total")})
public class CntMovdetalle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idcnt_movDetalle", nullable = false)
    private Long idcntmovDetalle;
    @Basic(optional = false)
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "tercero", length = 150)
    private String tercero;
    @Lob
    @Column(name = "detalle", length = 65535)
    private String detalle;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "debito", precision = 12)
    private Float debito;
    @Column(name = "credito", precision = 12)
    private Float credito;
    @Column(name = "total", precision = 12)
    private Float total;
    @JoinColumn(name = "cnt_puc_codigoCuenta", referencedColumnName = "codigoCuenta", nullable = false)
    @ManyToOne(optional = false)
    private CntPuc cntpuccodigoCuenta;
    @JoinColumns({
        @JoinColumn(name = "fac_documentosmaster_cfg_documento_idDoc", referencedColumnName = "cfg_documento_idDoc"),
        @JoinColumn(name = "fac_documentosmaster_numDocumento", referencedColumnName = "numDocumento")})
    @ManyToOne
    private FacDocumentosmaster facDocumentosmaster;
    @JoinColumns({
        @JoinColumn(name = "inv_movimiento_cfg_documento_idDoc", referencedColumnName = "cfg_documento_idDoc"),
        @JoinColumn(name = "inv_movimiento_numDoc", referencedColumnName = "numDoc")})
    @ManyToOne
    private InvMovimiento invMovimiento;
    @JoinColumn(name = "cfg_empresasede_idSede", referencedColumnName = "idSede")
    @ManyToOne
    private CfgEmpresasede cfgempresasedeidSede;    

    public CntMovdetalle() {
    }

    public CntMovdetalle(Long idcntmovDetalle) {
        this.idcntmovDetalle = idcntmovDetalle;
    }

    public CntMovdetalle(Long idcntmovDetalle, Date fecha) {
        this.idcntmovDetalle = idcntmovDetalle;
        this.fecha = fecha;
    }

    public Long getIdcntmovDetalle() {
        return idcntmovDetalle;
    }

    public void setIdcntmovDetalle(Long idcntmovDetalle) {
        this.idcntmovDetalle = idcntmovDetalle;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTercero() {
        return tercero;
    }

    public void setTercero(String tercero) {
        this.tercero = tercero;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Float getDebito() {
        return debito;
    }

    public void setDebito(Float debito) {
        this.debito = debito;
    }

    public Float getCredito() {
        return credito;
    }

    public void setCredito(Float credito) {
        this.credito = credito;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public CntPuc getCntpuccodigoCuenta() {
        return cntpuccodigoCuenta;
    }

    public void setCntpuccodigoCuenta(CntPuc cntpuccodigoCuenta) {
        this.cntpuccodigoCuenta = cntpuccodigoCuenta;
    }

    public FacDocumentosmaster getFacDocumentosmaster() {
        return facDocumentosmaster;
    }

    public void setFacDocumentosmaster(FacDocumentosmaster facDocumentosmaster) {
        this.facDocumentosmaster = facDocumentosmaster;
    }

    public InvMovimiento getInvMovimiento() {
        return invMovimiento;
    }

    public void setInvMovimiento(InvMovimiento invMovimiento) {
        this.invMovimiento = invMovimiento;
    }

    public CfgEmpresasede getCfgempresasedeidSede() {
        return cfgempresasedeidSede;
    }

    public void setCfgempresasedeidSede(CfgEmpresasede cfgempresasedeidSede) {
        this.cfgempresasedeidSede = cfgempresasedeidSede;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcntmovDetalle != null ? idcntmovDetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CntMovdetalle)) {
            return false;
        }
        CntMovdetalle other = (CntMovdetalle) object;
        if ((this.idcntmovDetalle == null && other.idcntmovDetalle != null) || (this.idcntmovDetalle != null && !this.idcntmovDetalle.equals(other.idcntmovDetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CntMovdetalle[ idcntmovDetalle=" + idcntmovDetalle + " ]";
    }
    
}
