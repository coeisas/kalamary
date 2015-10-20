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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "cfg_municipio", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
   @NamedQuery(name = "CfgMunicipio.findAll", query = "SELECT c FROM CfgMunicipio c"),
    @NamedQuery(name = "CfgMunicipio.findByIdMunicipio", query = "SELECT c FROM CfgMunicipio c WHERE c.cfgMunicipioPK.idMunicipio = :idMunicipio"),
    @NamedQuery(name = "CfgMunicipio.findByCfgdepartamentoidDepartamento", query = "SELECT c FROM CfgMunicipio c WHERE c.cfgMunicipioPK.cfgdepartamentoidDepartamento = :cfgdepartamentoidDepartamento"),
    @NamedQuery(name = "CfgMunicipio.findByNomMunicipio", query = "SELECT c FROM CfgMunicipio c WHERE c.nomMunicipio = :nomMunicipio"),
    @NamedQuery(name = "CfgMunicipio.findByFecCrea", query = "SELECT c FROM CfgMunicipio c WHERE c.fecCrea = :fecCrea"),
    @NamedQuery(name = "CfgMunicipio.findByUsrCrea", query = "SELECT c FROM CfgMunicipio c WHERE c.usrCrea = :usrCrea")})
public class CfgMunicipio implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CfgMunicipioPK cfgMunicipioPK;
    @Basic(optional = false)
    @Column(name = "nomMunicipio", nullable = false, length = 100)
    private String nomMunicipio;
    @Column(name = "fecCrea")
    @Temporal(TemporalType.DATE)
    private Date fecCrea;
    @Column(name = "usrCrea")
    private Integer usrCrea;
    @OneToMany(mappedBy = "cfgMunicipio")
    private List<CfgCliente> cfgClienteList;
    @JoinColumn(name = "cfg_departamento_idDepartamento", referencedColumnName = "idDepartamento", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgDepartamento cfgDepartamento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgMunicipio")
    private List<CfgProveedor> cfgProveedorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgMunicipio")
    private List<CfgEmpresa> cfgEmpresaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgMunicipio")
    private List<CfgEmpresasede> cfgEmpresasedeList;

    public CfgMunicipio() {
    }

    public CfgMunicipio(CfgMunicipioPK cfgMunicipioPK) {
        this.cfgMunicipioPK = cfgMunicipioPK;
    }

    public CfgMunicipio(CfgMunicipioPK cfgMunicipioPK, String nomMunicipio) {
        this.cfgMunicipioPK = cfgMunicipioPK;
        this.nomMunicipio = nomMunicipio;
    }

    public CfgMunicipio(String idMunicipio, String cfgdepartamentoidDepartamento) {
        this.cfgMunicipioPK = new CfgMunicipioPK(idMunicipio, cfgdepartamentoidDepartamento);
    }

    public CfgMunicipioPK getCfgMunicipioPK() {
        return cfgMunicipioPK;
    }

    public void setCfgMunicipioPK(CfgMunicipioPK cfgMunicipioPK) {
        this.cfgMunicipioPK = cfgMunicipioPK;
    }

    public String getNomMunicipio() {
        return nomMunicipio;
    }

    public void setNomMunicipio(String nomMunicipio) {
        this.nomMunicipio = nomMunicipio;
    }

    public Date getFecCrea() {
        return fecCrea;
    }

    public void setFecCrea(Date fecCrea) {
        this.fecCrea = fecCrea;
    }

    public Integer getUsrCrea() {
        return usrCrea;
    }

    public void setUsrCrea(Integer usrCrea) {
        this.usrCrea = usrCrea;
    }

    @XmlTransient
    public List<CfgCliente> getCfgClienteList() {
        return cfgClienteList;
    }

    public void setCfgClienteList(List<CfgCliente> cfgClienteList) {
        this.cfgClienteList = cfgClienteList;
    }

    public CfgDepartamento getCfgDepartamento() {
        return cfgDepartamento;
    }

    public void setCfgDepartamento(CfgDepartamento cfgDepartamento) {
        this.cfgDepartamento = cfgDepartamento;
    }

    @XmlTransient
    public List<CfgProveedor> getCfgProveedorList() {
        return cfgProveedorList;
    }

    public void setCfgProveedorList(List<CfgProveedor> cfgProveedorList) {
        this.cfgProveedorList = cfgProveedorList;
    }

    @XmlTransient
    public List<CfgEmpresa> getCfgEmpresaList() {
        return cfgEmpresaList;
    }

    public void setCfgEmpresaList(List<CfgEmpresa> cfgEmpresaList) {
        this.cfgEmpresaList = cfgEmpresaList;
    }

    @XmlTransient
    public List<CfgEmpresasede> getCfgEmpresasedeList() {
        return cfgEmpresasedeList;
    }

    public void setCfgEmpresasedeList(List<CfgEmpresasede> cfgEmpresasedeList) {
        this.cfgEmpresasedeList = cfgEmpresasedeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cfgMunicipioPK != null ? cfgMunicipioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgMunicipio)) {
            return false;
        }
        CfgMunicipio other = (CfgMunicipio) object;
        if ((this.cfgMunicipioPK == null && other.cfgMunicipioPK != null) || (this.cfgMunicipioPK != null && !this.cfgMunicipioPK.equals(other.cfgMunicipioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgMunicipio[ cfgMunicipioPK=" + cfgMunicipioPK + " ]";
    }
    
}
