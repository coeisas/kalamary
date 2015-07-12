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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "fac_caja", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacCaja.findAll", query = "SELECT f FROM FacCaja f"),
    @NamedQuery(name = "FacCaja.findByIdCaja", query = "SELECT f FROM FacCaja f WHERE f.idCaja = :idCaja"),
    @NamedQuery(name = "FacCaja.findByNomCaja", query = "SELECT f FROM FacCaja f WHERE f.nomCaja = :nomCaja"),
    @NamedQuery(name = "FacCaja.findByBase", query = "SELECT f FROM FacCaja f WHERE f.base = :base"),
    @NamedQuery(name = "FacCaja.findByFeccrea", query = "SELECT f FROM FacCaja f WHERE f.feccrea = :feccrea"),
    @NamedQuery(name = "FacCaja.findByUsrcrea", query = "SELECT f FROM FacCaja f WHERE f.usrcrea = :usrcrea"),
    @NamedQuery(name = "FacCaja.findByCodigoCaja", query = "SELECT f FROM FacCaja f WHERE f.codigoCaja = :codigoCaja"),
    @NamedQuery(name = "FacCaja.findByCerrada", query = "SELECT f FROM FacCaja f WHERE f.cerrada = :cerrada")})
public class FacCaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCaja", nullable = false)
    private Integer idCaja;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nomCaja", nullable = false, length = 100)
    private String nomCaja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "base", nullable = false)
    private float base;
    @Basic(optional = false)
    @NotNull
    @Column(name = "feccrea", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date feccrea;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usrcrea", nullable = false)
    private int usrcrea;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codigoCaja", nullable = false, length = 5)
    private String codigoCaja;
    @Column(name = "cerrada")
    private Boolean cerrada;
    @JoinColumn(name = "cfg_empresasede_idSede", referencedColumnName = "idSede", nullable = false)
    @ManyToOne(optional = false)
    private CfgEmpresasede cfgempresasedeidSede;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "faccajaidCaja")
    private List<FacMovcaja> facMovcajaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "faccajaidCaja")
    private List<FacMovcajadetalle> facMovcajadetalleList;

    public FacCaja() {
    }

    public FacCaja(Integer idCaja) {
        this.idCaja = idCaja;
    }

    public FacCaja(Integer idCaja, String nomCaja, float base, Date feccrea, int usrcrea, String codigoCaja) {
        this.idCaja = idCaja;
        this.nomCaja = nomCaja;
        this.base = base;
        this.feccrea = feccrea;
        this.usrcrea = usrcrea;
        this.codigoCaja = codigoCaja;
    }

    public Integer getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(Integer idCaja) {
        this.idCaja = idCaja;
    }

    public String getNomCaja() {
        return nomCaja;
    }

    public void setNomCaja(String nomCaja) {
        this.nomCaja = nomCaja;
    }

    public float getBase() {
        return base;
    }

    public void setBase(float base) {
        this.base = base;
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

    public String getCodigoCaja() {
        return codigoCaja;
    }

    public void setCodigoCaja(String codigoCaja) {
        this.codigoCaja = codigoCaja;
    }

    public Boolean getCerrada() {
        return cerrada;
    }

    public void setCerrada(Boolean cerrada) {
        this.cerrada = cerrada;
    }

    public CfgEmpresasede getCfgempresasedeidSede() {
        return cfgempresasedeidSede;
    }

    public void setCfgempresasedeidSede(CfgEmpresasede cfgempresasedeidSede) {
        this.cfgempresasedeidSede = cfgempresasedeidSede;
    }

    @XmlTransient
    public List<FacMovcaja> getFacMovcajaList() {
        return facMovcajaList;
    }

    public void setFacMovcajaList(List<FacMovcaja> facMovcajaList) {
        this.facMovcajaList = facMovcajaList;
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
        hash += (idCaja != null ? idCaja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacCaja)) {
            return false;
        }
        FacCaja other = (FacCaja) object;
        if ((this.idCaja == null && other.idCaja != null) || (this.idCaja != null && !this.idCaja.equals(other.idCaja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FacCaja[ idCaja=" + idCaja + " ]";
    }
    
}
