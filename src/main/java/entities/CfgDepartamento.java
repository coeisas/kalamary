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
import javax.persistence.Id;
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
@Table(name = "cfg_departamento", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgDepartamento.findAll", query = "SELECT c FROM CfgDepartamento c"),
    @NamedQuery(name = "CfgDepartamento.findByIdDepartamento", query = "SELECT c FROM CfgDepartamento c WHERE c.idDepartamento = :idDepartamento"),
    @NamedQuery(name = "CfgDepartamento.findByNomDepartamento", query = "SELECT c FROM CfgDepartamento c WHERE c.nomDepartamento = :nomDepartamento"),
    @NamedQuery(name = "CfgDepartamento.findByFecCrea", query = "SELECT c FROM CfgDepartamento c WHERE c.fecCrea = :fecCrea"),
    @NamedQuery(name = "CfgDepartamento.findByUsrCrea", query = "SELECT c FROM CfgDepartamento c WHERE c.usrCrea = :usrCrea")})
public class CfgDepartamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "idDepartamento", nullable = false, length = 2)
    private String idDepartamento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nomDepartamento", nullable = false, length = 100)
    private String nomDepartamento;
    @Column(name = "fecCrea")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCrea;
    @Column(name = "usrCrea")
    private Integer usrCrea;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgDepartamento")
    private List<CfgMunicipio> cfgMunicipioList;

    public CfgDepartamento() {
    }

    public CfgDepartamento(String idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public CfgDepartamento(String idDepartamento, String nomDepartamento) {
        this.idDepartamento = idDepartamento;
        this.nomDepartamento = nomDepartamento;
    }

    public String getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(String idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getNomDepartamento() {
        return nomDepartamento;
    }

    public void setNomDepartamento(String nomDepartamento) {
        this.nomDepartamento = nomDepartamento;
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
    public List<CfgMunicipio> getCfgMunicipioList() {
        return cfgMunicipioList;
    }

    public void setCfgMunicipioList(List<CfgMunicipio> cfgMunicipioList) {
        this.cfgMunicipioList = cfgMunicipioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDepartamento != null ? idDepartamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgDepartamento)) {
            return false;
        }
        CfgDepartamento other = (CfgDepartamento) object;
        if ((this.idDepartamento == null && other.idDepartamento != null) || (this.idDepartamento != null && !this.idDepartamento.equals(other.idDepartamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgDepartamento[ idDepartamento=" + idDepartamento + " ]";
    }
    
}
