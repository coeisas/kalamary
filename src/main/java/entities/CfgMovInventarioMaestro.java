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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cfg_mov_inventario_maestro", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgMovInventarioMaestro.findAll", query = "SELECT c FROM CfgMovInventarioMaestro c"),
    @NamedQuery(name = "CfgMovInventarioMaestro.findByIdMovInventarioMaestro", query = "SELECT c FROM CfgMovInventarioMaestro c WHERE c.idMovInventarioMaestro = :idMovInventarioMaestro"),
    @NamedQuery(name = "CfgMovInventarioMaestro.findByCodMovInvetario", query = "SELECT c FROM CfgMovInventarioMaestro c WHERE c.codMovInvetario = :codMovInvetario"),
    @NamedQuery(name = "CfgMovInventarioMaestro.findByNombreMovimiento", query = "SELECT c FROM CfgMovInventarioMaestro c WHERE c.nombreMovimiento = :nombreMovimiento")})
public class CfgMovInventarioMaestro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idMovInventarioMaestro", nullable = false)
    private Integer idMovInventarioMaestro;
    @Column(name = "codMovInvetario", length = 5)
    private String codMovInvetario;
    @Basic(optional = false)
    @Column(name = "nombreMovimiento", nullable = false, length = 45)
    private String nombreMovimiento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgmovinventariomaestroidMovInventarioMaestro")
    private List<CfgMovInventarioDetalle> cfgMovInventarioDetalleList;

    public CfgMovInventarioMaestro() {
    }

    public CfgMovInventarioMaestro(Integer idMovInventarioMaestro) {
        this.idMovInventarioMaestro = idMovInventarioMaestro;
    }

    public CfgMovInventarioMaestro(Integer idMovInventarioMaestro, String nombreMovimiento) {
        this.idMovInventarioMaestro = idMovInventarioMaestro;
        this.nombreMovimiento = nombreMovimiento;
    }

    public Integer getIdMovInventarioMaestro() {
        return idMovInventarioMaestro;
    }

    public void setIdMovInventarioMaestro(Integer idMovInventarioMaestro) {
        this.idMovInventarioMaestro = idMovInventarioMaestro;
    }

    public String getCodMovInvetario() {
        return codMovInvetario;
    }

    public void setCodMovInvetario(String codMovInvetario) {
        this.codMovInvetario = codMovInvetario;
    }

    public String getNombreMovimiento() {
        return nombreMovimiento;
    }

    public void setNombreMovimiento(String nombreMovimiento) {
        this.nombreMovimiento = nombreMovimiento;
    }

    @XmlTransient
    public List<CfgMovInventarioDetalle> getCfgMovInventarioDetalleList() {
        return cfgMovInventarioDetalleList;
    }

    public void setCfgMovInventarioDetalleList(List<CfgMovInventarioDetalle> cfgMovInventarioDetalleList) {
        this.cfgMovInventarioDetalleList = cfgMovInventarioDetalleList;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMovInventarioMaestro != null ? idMovInventarioMaestro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgMovInventarioMaestro)) {
            return false;
        }
        CfgMovInventarioMaestro other = (CfgMovInventarioMaestro) object;
        if ((this.idMovInventarioMaestro == null && other.idMovInventarioMaestro != null) || (this.idMovInventarioMaestro != null && !this.idMovInventarioMaestro.equals(other.idMovInventarioMaestro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgMovInventarioMaestro[ idMovInventarioMaestro=" + idMovInventarioMaestro + " ]";
    }

}
