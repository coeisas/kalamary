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
@Table(name = "fac_movcaja", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacMovcaja.findAll", query = "SELECT f FROM FacMovcaja f"),
    @NamedQuery(name = "FacMovcaja.findByIdMovimiento", query = "SELECT f FROM FacMovcaja f WHERE f.idMovimiento = :idMovimiento"),
    @NamedQuery(name = "FacMovcaja.findByBase", query = "SELECT f FROM FacMovcaja f WHERE f.base = :base"),
    @NamedQuery(name = "FacMovcaja.findByTrm", query = "SELECT f FROM FacMovcaja f WHERE f.trm = :trm"),
    @NamedQuery(name = "FacMovcaja.findByFecApertura", query = "SELECT f FROM FacMovcaja f WHERE f.fecApertura = :fecApertura"),
    @NamedQuery(name = "FacMovcaja.findByFecCierre", query = "SELECT f FROM FacMovcaja f WHERE f.fecCierre = :fecCierre"),
    @NamedQuery(name = "FacMovcaja.findByValorCierre", query = "SELECT f FROM FacMovcaja f WHERE f.valorCierre = :valorCierre"),
    @NamedQuery(name = "FacMovcaja.findByFeccrea", query = "SELECT f FROM FacMovcaja f WHERE f.feccrea = :feccrea"),
    @NamedQuery(name = "FacMovcaja.findByAbierta", query = "SELECT f FROM FacMovcaja f WHERE f.abierta = :abierta")})
public class FacMovcaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idMovimiento", nullable = false)
    private Integer idMovimiento;
    @Basic(optional = false)
    @Column(name = "base", nullable = false)
    private float base;
    @Basic(optional = false)
    @Column(name = "trm", nullable = false)
    private float trm;
    @Column(name = "fecApertura")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecApertura;
    @Column(name = "fecCierre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCierre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valorCierre", precision = 12)
    private Float valorCierre;
    @Basic(optional = false)
    @Column(name = "feccrea", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date feccrea;
    @Basic(optional = false)
    @Column(name = "abierta", nullable = false)
    private boolean abierta;
    @JoinColumn(name = "fac_caja_idCaja", referencedColumnName = "idCaja", nullable = false)
    @ManyToOne(optional = false)
    private FacCaja faccajaidCaja;
    @JoinColumn(name = "seg_usuario_idUsuario", referencedColumnName = "idUsuario", nullable = false)
    @ManyToOne(optional = false)
    private SegUsuario segusuarioidUsuario;
    @JoinColumn(name = "seg_usuario_idUsuario1", referencedColumnName = "idUsuario")
    @ManyToOne
    private SegUsuario segusuarioidUsuario1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facMovcaja")
    private List<FacMovcajadetalle> facMovcajadetalleList;

    public FacMovcaja() {
    }

    public FacMovcaja(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public FacMovcaja(Integer idMovimiento, float base, float trm, Date feccrea, boolean abierta) {
        this.idMovimiento = idMovimiento;
        this.base = base;
        this.trm = trm;
        this.feccrea = feccrea;
        this.abierta = abierta;
    }

    public Integer getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public float getBase() {
        return base;
    }

    public void setBase(float base) {
        this.base = base;
    }

    public float getTrm() {
        return trm;
    }

    public void setTrm(float trm) {
        this.trm = trm;
    }

    public Date getFecApertura() {
        return fecApertura;
    }

    public void setFecApertura(Date fecApertura) {
        this.fecApertura = fecApertura;
    }

    public Date getFecCierre() {
        return fecCierre;
    }

    public void setFecCierre(Date fecCierre) {
        this.fecCierre = fecCierre;
    }

    public Float getValorCierre() {
        return valorCierre;
    }

    public void setValorCierre(Float valorCierre) {
        this.valorCierre = valorCierre;
    }

    public Date getFeccrea() {
        return feccrea;
    }

    public void setFeccrea(Date feccrea) {
        this.feccrea = feccrea;
    }

    public boolean getAbierta() {
        return abierta;
    }

    public void setAbierta(boolean abierta) {
        this.abierta = abierta;
    }

    public FacCaja getFaccajaidCaja() {
        return faccajaidCaja;
    }

    public void setFaccajaidCaja(FacCaja faccajaidCaja) {
        this.faccajaidCaja = faccajaidCaja;
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
    public List<FacMovcajadetalle> getFacMovcajadetalleList() {
        return facMovcajadetalleList;
    }

    public void setFacMovcajadetalleList(List<FacMovcajadetalle> facMovcajadetalleList) {
        this.facMovcajadetalleList = facMovcajadetalleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMovimiento != null ? idMovimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacMovcaja)) {
            return false;
        }
        FacMovcaja other = (FacMovcaja) object;
        if ((this.idMovimiento == null && other.idMovimiento != null) || (this.idMovimiento != null && !this.idMovimiento.equals(other.idMovimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FacMovcaja[ idMovimiento=" + idMovimiento + " ]";
    }
    
}
