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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Mario
 */
@Entity
@Table(name = "cnt_puc", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CntPuc.findAll", query = "SELECT c FROM CntPuc c"),
    @NamedQuery(name = "CntPuc.findByCodigoCuenta", query = "SELECT c FROM CntPuc c WHERE c.codigoCuenta = :codigoCuenta"),
    @NamedQuery(name = "CntPuc.findByNombreCuentas", query = "SELECT c FROM CntPuc c WHERE c.nombreCuentas = :nombreCuentas")})
public class CntPuc implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigoCuenta", nullable = false, length = 15)
    private String codigoCuenta;
    @Basic(optional = false)
    @Column(name = "nombreCuentas", nullable = false, length = 100)
    private String nombreCuentas;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cntpuccodigoCuenta")
    private List<CntMovdetalle> cntMovdetalleList;

    public CntPuc() {
    }

    public CntPuc(String codigoCuenta) {
        this.codigoCuenta = codigoCuenta;
    }

    public CntPuc(String codigoCuenta, String nombreCuentas) {
        this.codigoCuenta = codigoCuenta;
        this.nombreCuentas = nombreCuentas;
    }

    public String getCodigoCuenta() {
        return codigoCuenta;
    }

    public void setCodigoCuenta(String codigoCuenta) {
        this.codigoCuenta = codigoCuenta;
    }

    public String getNombreCuentas() {
        return nombreCuentas;
    }

    public void setNombreCuentas(String nombreCuentas) {
        this.nombreCuentas = nombreCuentas;
    }

    @XmlTransient
    public List<CntMovdetalle> getCntMovdetalleList() {
        return cntMovdetalleList;
    }

    public void setCntMovdetalleList(List<CntMovdetalle> cntMovdetalleList) {
        this.cntMovdetalleList = cntMovdetalleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoCuenta != null ? codigoCuenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CntPuc)) {
            return false;
        }
        CntPuc other = (CntPuc) object;
        if ((this.codigoCuenta == null && other.codigoCuenta != null) || (this.codigoCuenta != null && !this.codigoCuenta.equals(other.codigoCuenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CntPuc[ codigoCuenta=" + codigoCuenta + " ]";
    }
    
}
