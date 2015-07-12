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
import javax.persistence.Lob;
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
@Table(name = "cfg_tipoempresa", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgTipoempresa.findAll", query = "SELECT c FROM CfgTipoempresa c"),
    @NamedQuery(name = "CfgTipoempresa.findById", query = "SELECT c FROM CfgTipoempresa c WHERE c.id = :id"),
    @NamedQuery(name = "CfgTipoempresa.findByCodigo", query = "SELECT c FROM CfgTipoempresa c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "CfgTipoempresa.findByAbreviatura", query = "SELECT c FROM CfgTipoempresa c WHERE c.abreviatura = :abreviatura")})
public class CfgTipoempresa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "codigo", nullable = false, length = 10)
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "descripcion", nullable = false, length = 65535)
    private String descripcion;
    @Size(max = 5)
    @Column(name = "abreviatura", length = 5)
    private String abreviatura;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgTipoempresaId")
    private List<CfgCliente> cfgClienteList;    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgTipoempresaId")
    private List<CfgEmpresa> cfgEmpresaList;

    public CfgTipoempresa() {
    }

    public CfgTipoempresa(Integer id) {
        this.id = id;
    }

    public CfgTipoempresa(Integer id, String codigo, String descripcion) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }
    
    @XmlTransient
    public List<CfgCliente> getCfgClienteList() {
        return cfgClienteList;
    }

    public void setCfgClienteList(List<CfgCliente> cfgClienteList) {
        this.cfgClienteList = cfgClienteList;
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
        if (!(object instanceof CfgTipoempresa)) {
            return false;
        }
        CfgTipoempresa other = (CfgTipoempresa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgTipoempresa[ id=" + id + " ]";
    }
    
}
