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
@Table(name = "cfg_tipoidentificacion", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgTipoidentificacion.findAll", query = "SELECT c FROM CfgTipoidentificacion c"),
    @NamedQuery(name = "CfgTipoidentificacion.findById", query = "SELECT c FROM CfgTipoidentificacion c WHERE c.id = :id"),
    @NamedQuery(name = "CfgTipoidentificacion.findByCodigo", query = "SELECT c FROM CfgTipoidentificacion c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "CfgTipoidentificacion.findByIdentificacion", query = "SELECT c FROM CfgTipoidentificacion c WHERE c.identificacion = :identificacion"),
    @NamedQuery(name = "CfgTipoidentificacion.findByAbreviatura", query = "SELECT c FROM CfgTipoidentificacion c WHERE c.abreviatura = :abreviatura")})
public class CfgTipoidentificacion implements Serializable {
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
    @Size(min = 1, max = 50)
    @Column(name = "identificacion", nullable = false, length = 50)
    private String identificacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "abreviatura", nullable = false, length = 5)
    private String abreviatura;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgTipoidentificacionId")
    private List<CfgCliente> cfgClienteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgTipoidentificacionId")
    private List<CfgProveedor> cfgProveedorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgTipoidentificacionId")
    private List<SegUsuario> segUsuarioList;

    public CfgTipoidentificacion() {
    }

    public CfgTipoidentificacion(Integer id) {
        this.id = id;
    }

    public CfgTipoidentificacion(Integer id, String codigo, String identificacion, String abreviatura) {
        this.id = id;
        this.codigo = codigo;
        this.identificacion = identificacion;
        this.abreviatura = abreviatura;
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

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
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
    public List<CfgProveedor> getCfgProveedorList() {
        return cfgProveedorList;
    }

    public void setCfgProveedorList(List<CfgProveedor> cfgProveedorList) {
        this.cfgProveedorList = cfgProveedorList;
    }

    @XmlTransient
    public List<SegUsuario> getSegUsuarioList() {
        return segUsuarioList;
    }

    public void setSegUsuarioList(List<SegUsuario> segUsuarioList) {
        this.segUsuarioList = segUsuarioList;
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
        if (!(object instanceof CfgTipoidentificacion)) {
            return false;
        }
        CfgTipoidentificacion other = (CfgTipoidentificacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgTipoidentificacion[ id=" + id + " ]";
    }
    
}
