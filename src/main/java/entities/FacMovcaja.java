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
@Table(name = "fac_movcaja", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacMovcaja.findAll", query = "SELECT f FROM FacMovcaja f"),
    @NamedQuery(name = "FacMovcaja.findByIdMovimiento", query = "SELECT f FROM FacMovcaja f WHERE f.idMovimiento = :idMovimiento"),
    @NamedQuery(name = "FacMovcaja.findByIdusuario", query = "SELECT f FROM FacMovcaja f WHERE f.idusuario = :idusuario"),
    @NamedQuery(name = "FacMovcaja.findByBase", query = "SELECT f FROM FacMovcaja f WHERE f.base = :base"),
    @NamedQuery(name = "FacMovcaja.findByTrm", query = "SELECT f FROM FacMovcaja f WHERE f.trm = :trm"),
    @NamedQuery(name = "FacMovcaja.findByFecApertura", query = "SELECT f FROM FacMovcaja f WHERE f.fecApertura = :fecApertura"),
    @NamedQuery(name = "FacMovcaja.findByFecCierre", query = "SELECT f FROM FacMovcaja f WHERE f.fecCierre = :fecCierre"),
    @NamedQuery(name = "FacMovcaja.findByValorCierre", query = "SELECT f FROM FacMovcaja f WHERE f.valorCierre = :valorCierre"),
    @NamedQuery(name = "FacMovcaja.findByFeccrea", query = "SELECT f FROM FacMovcaja f WHERE f.feccrea = :feccrea"),
    @NamedQuery(name = "FacMovcaja.findByUsrcrea", query = "SELECT f FROM FacMovcaja f WHERE f.usrcrea = :usrcrea")})
public class FacMovcaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idMovimiento", nullable = false)
    private Integer idMovimiento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idusuario", nullable = false)
    private int idusuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "base", nullable = false)
    private float base;
    @Basic(optional = false)
    @NotNull
    @Column(name = "trm", nullable = false)
    private int trm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecApertura", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecApertura;
    @Column(name = "fecCierre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCierre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valorCierre", precision = 12)
    private Float valorCierre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "feccrea", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date feccrea;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usrcrea", nullable = false)
    private int usrcrea;
    @JoinColumn(name = "fac_caja_idCaja", referencedColumnName = "idCaja", nullable = false)
    @ManyToOne(optional = false)
    private FacCaja faccajaidCaja;

    public FacMovcaja() {
    }

    public FacMovcaja(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public FacMovcaja(Integer idMovimiento, int idusuario, float base, int trm, Date fecApertura, Date feccrea, int usrcrea) {
        this.idMovimiento = idMovimiento;
        this.idusuario = idusuario;
        this.base = base;
        this.trm = trm;
        this.fecApertura = fecApertura;
        this.feccrea = feccrea;
        this.usrcrea = usrcrea;
    }

    public Integer getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public float getBase() {
        return base;
    }

    public void setBase(float base) {
        this.base = base;
    }

    public int getTrm() {
        return trm;
    }

    public void setTrm(int trm) {
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

    public int getUsrcrea() {
        return usrcrea;
    }

    public void setUsrcrea(int usrcrea) {
        this.usrcrea = usrcrea;
    }

    public FacCaja getFaccajaidCaja() {
        return faccajaidCaja;
    }

    public void setFaccajaidCaja(FacCaja faccajaidCaja) {
        this.faccajaidCaja = faccajaidCaja;
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
