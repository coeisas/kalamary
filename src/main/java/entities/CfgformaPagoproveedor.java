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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "cfg_formapago_proveedor", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgformaPagoproveedor.findAll", query = "SELECT c FROM CfgformaPagoproveedor c"),
    @NamedQuery(name = "CfgformaPagoproveedor.findByIdFormaPago", query = "SELECT c FROM CfgformaPagoproveedor c WHERE c.idFormaPago = :idFormaPago"),
    @NamedQuery(name = "CfgformaPagoproveedor.findByCodigo", query = "SELECT c FROM CfgformaPagoproveedor c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "CfgformaPagoproveedor.findByDescripcion", query = "SELECT c FROM CfgformaPagoproveedor c WHERE c.descripcion = :descripcion")})
public class CfgformaPagoproveedor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idFormaPago", nullable = false)
    private Integer idFormaPago;
    @Basic(optional = false)
    @Column(name = "codigo", nullable = false, length = 5)
    private String codigo;
    @Basic(optional = false)
    @Column(name = "descripcion", nullable = false, length = 60)
    private String descripcion;
    @OneToMany(mappedBy = "cfgformaPagoproveedoridFormaPago")
    private List<InvMovimiento> invMovimientoList;
    @JoinColumn(name = "cfg_empresa_idEmpresa", referencedColumnName = "idEmpresa", nullable = false)
    @ManyToOne(optional = false)
    private CfgEmpresa cfgempresaidEmpresa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgformaPagoproveedoridFormaPago")
    private List<CfgProveedor> cfgProveedorList;

    public CfgformaPagoproveedor() {
    }

    public CfgformaPagoproveedor(Integer idFormaPago) {
        this.idFormaPago = idFormaPago;
    }

    public CfgformaPagoproveedor(Integer idFormaPago, String codigo, String descripcion) {
        this.idFormaPago = idFormaPago;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public Integer getIdFormaPago() {
        return idFormaPago;
    }

    public void setIdFormaPago(Integer idFormaPago) {
        this.idFormaPago = idFormaPago;
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

    @XmlTransient
    public List<InvMovimiento> getInvMovimientoList() {
        return invMovimientoList;
    }

    public void setInvMovimientoList(List<InvMovimiento> invMovimientoList) {
        this.invMovimientoList = invMovimientoList;
    }

    public CfgEmpresa getCfgempresaidEmpresa() {
        return cfgempresaidEmpresa;
    }

    public void setCfgempresaidEmpresa(CfgEmpresa cfgempresaidEmpresa) {
        this.cfgempresaidEmpresa = cfgempresaidEmpresa;
    }

    @XmlTransient
    public List<CfgProveedor> getCfgProveedorList() {
        return cfgProveedorList;
    }

    public void setCfgProveedorList(List<CfgProveedor> cfgProveedorList) {
        this.cfgProveedorList = cfgProveedorList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFormaPago != null ? idFormaPago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgformaPagoproveedor)) {
            return false;
        }
        CfgformaPagoproveedor other = (CfgformaPagoproveedor) object;
        if ((this.idFormaPago == null && other.idFormaPago != null) || (this.idFormaPago != null && !this.idFormaPago.equals(other.idFormaPago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgformaPagoproveedor[ idFormaPago=" + idFormaPago + " ]";
    }
    
}
