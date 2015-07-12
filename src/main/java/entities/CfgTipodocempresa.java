/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cfg_tipodocempresa", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgTipodocempresa.findAll", query = "SELECT c FROM CfgTipodocempresa c"),
    @NamedQuery(name = "CfgTipodocempresa.findById", query = "SELECT c FROM CfgTipodocempresa c WHERE c.id = :id"),
    @NamedQuery(name = "CfgTipodocempresa.findByCodigo", query = "SELECT c FROM CfgTipodocempresa c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "CfgTipodocempresa.findByDocumentoempresa", query = "SELECT c FROM CfgTipodocempresa c WHERE c.documentoempresa = :documentoempresa")})
public class CfgTipodocempresa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codigo", nullable = false, length = 5)
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "documentoempresa", nullable = false, length = 45)
    private String documentoempresa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgTipodocempresaId")
    private List<CfgEmpresa> cfgEmpresaList;

    public CfgTipodocempresa() {
    }

    public CfgTipodocempresa(Integer id) {
        this.id = id;
    }

    public CfgTipodocempresa(Integer id, String codigo, String documentoempresa) {
        this.id = id;
        this.codigo = codigo;
        this.documentoempresa = documentoempresa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDocumentoempresa() {
        return documentoempresa;
    }

    public void setDocumentoempresa(String documentoempresa) {
        this.documentoempresa = documentoempresa;
    }

    @XmlTransient
    public List<CfgEmpresa> getCfgEmpresaList() {
        return cfgEmpresaList;
    }

    public void setCfgEmpresaList(List<CfgEmpresa> cfgEmpresaList) {
        this.cfgEmpresaList = cfgEmpresaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgTipodocempresa)) {
            return false;
        }
        CfgTipodocempresa other = (CfgTipodocempresa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgTipodocempresa[ id=" + id + " ]";
    }
    
}
