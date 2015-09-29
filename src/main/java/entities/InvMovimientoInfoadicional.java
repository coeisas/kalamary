/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "inv_movimiento_infoadicional", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvMovimientoInfoadicional.findAll", query = "SELECT i FROM InvMovimientoInfoadicional i"),
    @NamedQuery(name = "InvMovimientoInfoadicional.findByInvmovimientocfgdocumentoidDoc", query = "SELECT i FROM InvMovimientoInfoadicional i WHERE i.invMovimientoInfoadicionalPK.invmovimientocfgdocumentoidDoc = :invmovimientocfgdocumentoidDoc"),
    @NamedQuery(name = "InvMovimientoInfoadicional.findByInvmovimientonumDoc", query = "SELECT i FROM InvMovimientoInfoadicional i WHERE i.invMovimientoInfoadicionalPK.invmovimientonumDoc = :invmovimientonumDoc"),
    @NamedQuery(name = "InvMovimientoInfoadicional.findByTransportadora", query = "SELECT i FROM InvMovimientoInfoadicional i WHERE i.transportadora = :transportadora"),
    @NamedQuery(name = "InvMovimientoInfoadicional.findByConductor", query = "SELECT i FROM InvMovimientoInfoadicional i WHERE i.conductor = :conductor"),
    @NamedQuery(name = "InvMovimientoInfoadicional.findByCiudad", query = "SELECT i FROM InvMovimientoInfoadicional i WHERE i.ciudad = :ciudad"),
    @NamedQuery(name = "InvMovimientoInfoadicional.findByPlaca", query = "SELECT i FROM InvMovimientoInfoadicional i WHERE i.placa = :placa"),
    @NamedQuery(name = "InvMovimientoInfoadicional.findByPesoTotal", query = "SELECT i FROM InvMovimientoInfoadicional i WHERE i.pesoTotal = :pesoTotal")})
public class InvMovimientoInfoadicional implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected InvMovimientoInfoadicionalPK invMovimientoInfoadicionalPK;
    @Lob
    @Column(name = "direccion", length = 65535)
    private String direccion;
    @Column(name = "transportadora", length = 50)
    private String transportadora;
    @Column(name = "conductor", length = 50)
    private String conductor;
    @Column(name = "ciudad", length = 45)
    private String ciudad;
    @Column(name = "placa", length = 45)
    private String placa;
    @Column(name = "pesoTotal", length = 45)
    private String pesoTotal;
    @JoinColumns({
        @JoinColumn(name = "inv_movimiento_cfg_documento_idDoc", referencedColumnName = "cfg_documento_idDoc", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "inv_movimiento_numDoc", referencedColumnName = "numDoc", nullable = false, insertable = false, updatable = false)})
    @OneToOne(optional = false)
    private InvMovimiento invMovimiento;

    public InvMovimientoInfoadicional() {
    }

    public InvMovimientoInfoadicional(InvMovimientoInfoadicionalPK invMovimientoInfoadicionalPK) {
        this.invMovimientoInfoadicionalPK = invMovimientoInfoadicionalPK;
    }

    public InvMovimientoInfoadicional(int invmovimientocfgdocumentoidDoc, int invmovimientonumDoc) {
        this.invMovimientoInfoadicionalPK = new InvMovimientoInfoadicionalPK(invmovimientocfgdocumentoidDoc, invmovimientonumDoc);
    }

    public InvMovimientoInfoadicionalPK getInvMovimientoInfoadicionalPK() {
        return invMovimientoInfoadicionalPK;
    }

    public void setInvMovimientoInfoadicionalPK(InvMovimientoInfoadicionalPK invMovimientoInfoadicionalPK) {
        this.invMovimientoInfoadicionalPK = invMovimientoInfoadicionalPK;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(String transportadora) {
        this.transportadora = transportadora;
    }

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getPesoTotal() {
        return pesoTotal;
    }

    public void setPesoTotal(String pesoTotal) {
        this.pesoTotal = pesoTotal;
    }

    public InvMovimiento getInvMovimiento() {
        return invMovimiento;
    }

    public void setInvMovimiento(InvMovimiento invMovimiento) {
        this.invMovimiento = invMovimiento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invMovimientoInfoadicionalPK != null ? invMovimientoInfoadicionalPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvMovimientoInfoadicional)) {
            return false;
        }
        InvMovimientoInfoadicional other = (InvMovimientoInfoadicional) object;
        if ((this.invMovimientoInfoadicionalPK == null && other.invMovimientoInfoadicionalPK != null) || (this.invMovimientoInfoadicionalPK != null && !this.invMovimientoInfoadicionalPK.equals(other.invMovimientoInfoadicionalPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.InvMovimientoInfoadicional[ invMovimientoInfoadicionalPK=" + invMovimientoInfoadicionalPK + " ]";
    }
    
}
