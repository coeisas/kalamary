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
@Table(name = "fac_caja", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacCaja.findAll", query = "SELECT f FROM FacCaja f"),
    @NamedQuery(name = "FacCaja.findByIdCaja", query = "SELECT f FROM FacCaja f WHERE f.idCaja = :idCaja"),
    @NamedQuery(name = "FacCaja.findByNomCaja", query = "SELECT f FROM FacCaja f WHERE f.nomCaja = :nomCaja"),
    @NamedQuery(name = "FacCaja.findByFeccrea", query = "SELECT f FROM FacCaja f WHERE f.feccrea = :feccrea"),
    @NamedQuery(name = "FacCaja.findByUsrcrea", query = "SELECT f FROM FacCaja f WHERE f.usrcrea = :usrcrea"),
    @NamedQuery(name = "FacCaja.findByCodigoCaja", query = "SELECT f FROM FacCaja f WHERE f.codigoCaja = :codigoCaja"),
    @NamedQuery(name = "FacCaja.findByActiva", query = "SELECT f FROM FacCaja f WHERE f.activa = :activa"),
    @NamedQuery(name = "FacCaja.findByBase", query = "SELECT f FROM FacCaja f WHERE f.base = :base")})
public class FacCaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCaja", nullable = false)
    private Integer idCaja;
    @Basic(optional = false)
    @Column(name = "nomCaja", nullable = false, length = 100)
    private String nomCaja;
    @Basic(optional = false)
    @Column(name = "feccrea", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date feccrea;
    @Basic(optional = false)
    @Column(name = "usrcrea", nullable = false)
    private int usrcrea;
    @Basic(optional = false)
    @Column(name = "codigoCaja", nullable = false, length = 5)
    private String codigoCaja;
    @Column(name = "activa")
    private Boolean activa;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "base", precision = 12)
    private Float base;
    @JoinColumn(name = "cfg_empresasede_idSede", referencedColumnName = "idSede", nullable = false)
    @ManyToOne(optional = false)
    private CfgEmpresasede cfgempresasedeidSede;
    @OneToMany(mappedBy = "faccajaidCaja")
    private List<SegUsuario> segUsuarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "faccajaidCaja")
    private List<FacMovcaja> facMovcajaList;
    @OneToMany(mappedBy = "faccajaidCaja")
    private List<FacDocumentosmaster> facDocumentosmasterList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facCaja")
    private List<ConsolidadoMovcaja> consolidadoMovcajaList;

    public FacCaja() {
    }

    public FacCaja(Integer idCaja) {
        this.idCaja = idCaja;
    }

    public FacCaja(Integer idCaja, String nomCaja, Date feccrea, int usrcrea, String codigoCaja) {
        this.idCaja = idCaja;
        this.nomCaja = nomCaja;
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

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public Float getBase() {
        return base;
    }

    public void setBase(Float base) {
        this.base = base;
    }

    public CfgEmpresasede getCfgempresasedeidSede() {
        return cfgempresasedeidSede;
    }

    public void setCfgempresasedeidSede(CfgEmpresasede cfgempresasedeidSede) {
        this.cfgempresasedeidSede = cfgempresasedeidSede;
    }

    @XmlTransient
    public List<SegUsuario> getSegUsuarioList() {
        return segUsuarioList;
    }

    public void setSegUsuarioList(List<SegUsuario> segUsuarioList) {
        this.segUsuarioList = segUsuarioList;
    }

    @XmlTransient
    public List<FacMovcaja> getFacMovcajaList() {
        return facMovcajaList;
    }

    public void setFacMovcajaList(List<FacMovcaja> facMovcajaList) {
        this.facMovcajaList = facMovcajaList;
    }

    @XmlTransient
    public List<FacDocumentosmaster> getFacDocumentosmasterList() {
        return facDocumentosmasterList;
    }

    public void setFacDocumentosmasterList(List<FacDocumentosmaster> facDocumentosmasterList) {
        this.facDocumentosmasterList = facDocumentosmasterList;
    }

    @XmlTransient
    public List<ConsolidadoMovcaja> getConsolidadoMovcajaList() {
        return consolidadoMovcajaList;
    }

    public void setConsolidadoMovcajaList(List<ConsolidadoMovcaja> consolidadoMovcajaList) {
        this.consolidadoMovcajaList = consolidadoMovcajaList;
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
