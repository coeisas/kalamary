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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cfg_impuesto", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgImpuesto.findAll", query = "SELECT c FROM CfgImpuesto c"),
    @NamedQuery(name = "CfgImpuesto.findByIdImpuesto", query = "SELECT c FROM CfgImpuesto c WHERE c.idImpuesto = :idImpuesto"),
    @NamedQuery(name = "CfgImpuesto.findByNomImpuesto", query = "SELECT c FROM CfgImpuesto c WHERE c.nomImpuesto = :nomImpuesto"),
    @NamedQuery(name = "CfgImpuesto.findByPorcentaje", query = "SELECT c FROM CfgImpuesto c WHERE c.porcentaje = :porcentaje"),
    @NamedQuery(name = "CfgImpuesto.findByTipoEmpresa", query = "SELECT c FROM CfgImpuesto c WHERE c.tipoEmpresa = :tipoEmpresa"),
    @NamedQuery(name = "CfgImpuesto.findByFecCrea", query = "SELECT c FROM CfgImpuesto c WHERE c.fecCrea = :fecCrea")})
public class CfgImpuesto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idImpuesto", nullable = false)
    private Integer idImpuesto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nomImpuesto", nullable = false, length = 50)
    private String nomImpuesto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje", nullable = false)
    private float porcentaje;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "tipoEmpresa", nullable = false, length = 1)
    private String tipoEmpresa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecCrea", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecCrea;
    @JoinColumn(name = "cfg_empresasede_idSede", referencedColumnName = "idSede", nullable = false)
    @ManyToOne(optional = false)
    private CfgEmpresasede cfgempresasedeidSede;
    @JoinColumn(name = "seg_usuario_idUsuario", referencedColumnName = "idUsuario", nullable = false)
    @ManyToOne(optional = false)
    private SegUsuario segusuarioidUsuario;

    public CfgImpuesto() {
    }

    public CfgImpuesto(Integer idImpuesto) {
        this.idImpuesto = idImpuesto;
    }

    public CfgImpuesto(Integer idImpuesto, String nomImpuesto, float porcentaje, String tipoEmpresa, Date fecCrea) {
        this.idImpuesto = idImpuesto;
        this.nomImpuesto = nomImpuesto;
        this.porcentaje = porcentaje;
        this.tipoEmpresa = tipoEmpresa;
        this.fecCrea = fecCrea;
    }

    public Integer getIdImpuesto() {
        return idImpuesto;
    }

    public void setIdImpuesto(Integer idImpuesto) {
        this.idImpuesto = idImpuesto;
    }

    public String getNomImpuesto() {
        return nomImpuesto;
    }

    public void setNomImpuesto(String nomImpuesto) {
        this.nomImpuesto = nomImpuesto;
    }

    public float getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(float porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getTipoEmpresa() {
        return tipoEmpresa;
    }

    public void setTipoEmpresa(String tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
    }

    public Date getFecCrea() {
        return fecCrea;
    }

    public void setFecCrea(Date fecCrea) {
        this.fecCrea = fecCrea;
    }

    public CfgEmpresasede getCfgempresasedeidSede() {
        return cfgempresasedeidSede;
    }

    public void setCfgempresasedeidSede(CfgEmpresasede cfgempresasedeidSede) {
        this.cfgempresasedeidSede = cfgempresasedeidSede;
    }

    public SegUsuario getSegusuarioidUsuario() {
        return segusuarioidUsuario;
    }

    public void setSegusuarioidUsuario(SegUsuario segusuarioidUsuario) {
        this.segusuarioidUsuario = segusuarioidUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idImpuesto != null ? idImpuesto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgImpuesto)) {
            return false;
        }
        CfgImpuesto other = (CfgImpuesto) object;
        if ((this.idImpuesto == null && other.idImpuesto != null) || (this.idImpuesto != null && !this.idImpuesto.equals(other.idImpuesto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgImpuesto[ idImpuesto=" + idImpuesto + " ]";
    }
    
}
