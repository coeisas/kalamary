/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author mario
 */
@Embeddable
public class CfgMunicipioPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idMunicipio", nullable = false, length = 3)
    private String idMunicipio;
    @Basic(optional = false)
    @Column(name = "cfg_departamento_idDepartamento", nullable = false, length = 2)
    private String cfgdepartamentoidDepartamento;

    public CfgMunicipioPK() {
    }

    public CfgMunicipioPK(String idMunicipio, String cfgdepartamentoidDepartamento) {
        this.idMunicipio = idMunicipio;
        this.cfgdepartamentoidDepartamento = cfgdepartamentoidDepartamento;
    }

    public String getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(String idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public String getCfgdepartamentoidDepartamento() {
        return cfgdepartamentoidDepartamento;
    }

    public void setCfgdepartamentoidDepartamento(String cfgdepartamentoidDepartamento) {
        this.cfgdepartamentoidDepartamento = cfgdepartamentoidDepartamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMunicipio != null ? idMunicipio.hashCode() : 0);
        hash += (cfgdepartamentoidDepartamento != null ? cfgdepartamentoidDepartamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgMunicipioPK)) {
            return false;
        }
        CfgMunicipioPK other = (CfgMunicipioPK) object;
        if ((this.idMunicipio == null && other.idMunicipio != null) || (this.idMunicipio != null && !this.idMunicipio.equals(other.idMunicipio))) {
            return false;
        }
        if ((this.cfgdepartamentoidDepartamento == null && other.cfgdepartamentoidDepartamento != null) || (this.cfgdepartamentoidDepartamento != null && !this.cfgdepartamentoidDepartamento.equals(other.cfgdepartamentoidDepartamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgMunicipioPK[ idMunicipio=" + idMunicipio + ", cfgdepartamentoidDepartamento=" + cfgdepartamentoidDepartamento + " ]";
    }
    
}
