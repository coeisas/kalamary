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
@Table(name = "cfg_mov_inventario_detalle", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgMovInventarioDetalle.findAll", query = "SELECT c FROM CfgMovInventarioDetalle c"),
    @NamedQuery(name = "CfgMovInventarioDetalle.findByIdMovInventarioDetalle", query = "SELECT c FROM CfgMovInventarioDetalle c WHERE c.idMovInventarioDetalle = :idMovInventarioDetalle"),
    @NamedQuery(name = "CfgMovInventarioDetalle.findByCodMovInvetarioDetalle", query = "SELECT c FROM CfgMovInventarioDetalle c WHERE c.codMovInvetarioDetalle = :codMovInvetarioDetalle"),
    @NamedQuery(name = "CfgMovInventarioDetalle.findByNomMovimientoDetalle", query = "SELECT c FROM CfgMovInventarioDetalle c WHERE c.nomMovimientoDetalle = :nomMovimientoDetalle")})
public class CfgMovInventarioDetalle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idMovInventarioDetalle", nullable = false)
    private Integer idMovInventarioDetalle;
    @Column(name = "codMovInvetarioDetalle", length = 5)
    private String codMovInvetarioDetalle;
    @Basic(optional = false)
    @Column(name = "nomMovimientoDetalle", nullable = false, length = 50)
    private String nomMovimientoDetalle;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgmovinventariodetalleidMovInventarioDetalle")
    private List<InvMovimiento> invMovimientoList;
    @JoinColumn(name = "cfg_mov_inventario_maestro_idMovInventarioMaestro", referencedColumnName = "idMovInventarioMaestro", nullable = false)
    @ManyToOne(optional = false)
    private CfgMovInventarioMaestro cfgmovinventariomaestroidMovInventarioMaestro;

    public CfgMovInventarioDetalle() {
    }

    public CfgMovInventarioDetalle(Integer idMovInventarioDetalle) {
        this.idMovInventarioDetalle = idMovInventarioDetalle;
    }

    public CfgMovInventarioDetalle(Integer idMovInventarioDetalle, String nomMovimientoDetalle) {
        this.idMovInventarioDetalle = idMovInventarioDetalle;
        this.nomMovimientoDetalle = nomMovimientoDetalle;
    }

    public Integer getIdMovInventarioDetalle() {
        return idMovInventarioDetalle;
    }

    public void setIdMovInventarioDetalle(Integer idMovInventarioDetalle) {
        this.idMovInventarioDetalle = idMovInventarioDetalle;
    }

    public String getCodMovInvetarioDetalle() {
        return codMovInvetarioDetalle;
    }

    public void setCodMovInvetarioDetalle(String codMovInvetarioDetalle) {
        this.codMovInvetarioDetalle = codMovInvetarioDetalle;
    }

    public String getNomMovimientoDetalle() {
        return nomMovimientoDetalle;
    }

    public void setNomMovimientoDetalle(String nomMovimientoDetalle) {
        this.nomMovimientoDetalle = nomMovimientoDetalle;
    }

    @XmlTransient
    public List<InvMovimiento> getInvMovimientoList() {
        return invMovimientoList;
    }

    public void setInvMovimientoList(List<InvMovimiento> invMovimientoList) {
        this.invMovimientoList = invMovimientoList;
    }

    public CfgMovInventarioMaestro getCfgmovinventariomaestroidMovInventarioMaestro() {
        return cfgmovinventariomaestroidMovInventarioMaestro;
    }

    public void setCfgmovinventariomaestroidMovInventarioMaestro(CfgMovInventarioMaestro cfgmovinventariomaestroidMovInventarioMaestro) {
        this.cfgmovinventariomaestroidMovInventarioMaestro = cfgmovinventariomaestroidMovInventarioMaestro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMovInventarioDetalle != null ? idMovInventarioDetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgMovInventarioDetalle)) {
            return false;
        }
        CfgMovInventarioDetalle other = (CfgMovInventarioDetalle) object;
        if ((this.idMovInventarioDetalle == null && other.idMovInventarioDetalle != null) || (this.idMovInventarioDetalle != null && !this.idMovInventarioDetalle.equals(other.idMovInventarioDetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgMovInventarioDetalle[ idMovInventarioDetalle=" + idMovInventarioDetalle + " ]";
    }
    
}
