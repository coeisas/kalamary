/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cfg_pais", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgPais.findAll", query = "SELECT c FROM CfgPais c"),
    @NamedQuery(name = "CfgPais.findByCodPais", query = "SELECT c FROM CfgPais c WHERE c.codPais = :codPais"),
    @NamedQuery(name = "CfgPais.findByNomPais", query = "SELECT c FROM CfgPais c WHERE c.nomPais = :nomPais")})
public class CfgPais implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codPais", nullable = false, length = 3)
    private String codPais;
    @Basic(optional = false)
    @Column(name = "nomPais", nullable = false, length = 70)
    private String nomPais;
    @OneToMany(mappedBy = "cfgpaiscodPais")
    private List<CfgCliente> cfgClienteList;

    public CfgPais() {
    }

    public CfgPais(String codPais) {
        this.codPais = codPais;
    }

    public CfgPais(String codPais, String nomPais) {
        this.codPais = codPais;
        this.nomPais = nomPais;
    }

    public String getCodPais() {
        return codPais;
    }

    public void setCodPais(String codPais) {
        this.codPais = codPais;
    }

    public String getNomPais() {
        return nomPais;
    }

    public void setNomPais(String nomPais) {
        this.nomPais = nomPais;
    }

    @XmlTransient
    public List<CfgCliente> getCfgClienteList() {
        return cfgClienteList;
    }

    public void setCfgClienteList(List<CfgCliente> cfgClienteList) {
        this.cfgClienteList = cfgClienteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codPais != null ? codPais.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgPais)) {
            return false;
        }
        CfgPais other = (CfgPais) object;
        if ((this.codPais == null && other.codPais != null) || (this.codPais != null && !this.codPais.equals(other.codPais))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgPais[ codPais=" + codPais + " ]";
    }
    
}
