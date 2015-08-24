/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "inv_consolidado", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvConsolidado.findAll", query = "SELECT i FROM InvConsolidado i"),
    @NamedQuery(name = "InvConsolidado.findByCfgproductoidProducto", query = "SELECT i FROM InvConsolidado i WHERE i.invConsolidadoPK.cfgproductoidProducto = :cfgproductoidProducto"),
    @NamedQuery(name = "InvConsolidado.findByCfgempresasedeidSede", query = "SELECT i FROM InvConsolidado i WHERE i.invConsolidadoPK.cfgempresasedeidSede = :cfgempresasedeidSede"),
    @NamedQuery(name = "InvConsolidado.findByExistencia", query = "SELECT i FROM InvConsolidado i WHERE i.existencia = :existencia"),
    @NamedQuery(name = "InvConsolidado.findByEntradas", query = "SELECT i FROM InvConsolidado i WHERE i.entradas = :entradas"),
    @NamedQuery(name = "InvConsolidado.findBySalidas", query = "SELECT i FROM InvConsolidado i WHERE i.salidas = :salidas"),
    @NamedQuery(name = "InvConsolidado.findByFechaUltSalida", query = "SELECT i FROM InvConsolidado i WHERE i.fechaUltSalida = :fechaUltSalida"),
    @NamedQuery(name = "InvConsolidado.findByFechaUltEntrada", query = "SELECT i FROM InvConsolidado i WHERE i.fechaUltEntrada = :fechaUltEntrada")})
public class InvConsolidado implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected InvConsolidadoPK invConsolidadoPK;
    @Basic(optional = false)
    @Column(name = "existencia", nullable = false)
    private int existencia;
    @Basic(optional = false)
    @Column(name = "entradas", nullable = false)
    private int entradas;
    @Basic(optional = false)
    @Column(name = "salidas", nullable = false)
    private int salidas;
    @Column(name = "fecha_ult_salida")
    @Temporal(TemporalType.DATE)
    private Date fechaUltSalida;
    @Column(name = "fecha_ult_entrada")
    @Temporal(TemporalType.DATE)
    private Date fechaUltEntrada;
    @JoinColumn(name = "cfg_empresasede_idSede", referencedColumnName = "idSede", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgEmpresasede cfgEmpresasede;
    @JoinColumn(name = "cfg_producto_idProducto", referencedColumnName = "idProducto", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgProducto cfgProducto;

    public InvConsolidado() {
    }

    public InvConsolidado(InvConsolidadoPK invConsolidadoPK) {
        this.invConsolidadoPK = invConsolidadoPK;
    }

    public InvConsolidado(InvConsolidadoPK invConsolidadoPK, int existencia, int entradas, int salidas) {
        this.invConsolidadoPK = invConsolidadoPK;
        this.existencia = existencia;
        this.entradas = entradas;
        this.salidas = salidas;
    }

    public InvConsolidado(int cfgproductoidProducto, int cfgempresasedeidSede) {
        this.invConsolidadoPK = new InvConsolidadoPK(cfgproductoidProducto, cfgempresasedeidSede);
    }

    public InvConsolidadoPK getInvConsolidadoPK() {
        return invConsolidadoPK;
    }

    public void setInvConsolidadoPK(InvConsolidadoPK invConsolidadoPK) {
        this.invConsolidadoPK = invConsolidadoPK;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public int getEntradas() {
        return entradas;
    }

    public void setEntradas(int entradas) {
        this.entradas = entradas;
    }

    public int getSalidas() {
        return salidas;
    }

    public void setSalidas(int salidas) {
        this.salidas = salidas;
    }

    public Date getFechaUltSalida() {
        return fechaUltSalida;
    }

    public void setFechaUltSalida(Date fechaUltSalida) {
        this.fechaUltSalida = fechaUltSalida;
    }

    public Date getFechaUltEntrada() {
        return fechaUltEntrada;
    }

    public void setFechaUltEntrada(Date fechaUltEntrada) {
        this.fechaUltEntrada = fechaUltEntrada;
    }

    public CfgEmpresasede getCfgEmpresasede() {
        return cfgEmpresasede;
    }

    public void setCfgEmpresasede(CfgEmpresasede cfgEmpresasede) {
        this.cfgEmpresasede = cfgEmpresasede;
    }

    public CfgProducto getCfgProducto() {
        return cfgProducto;
    }

    public void setCfgProducto(CfgProducto cfgProducto) {
        this.cfgProducto = cfgProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invConsolidadoPK != null ? invConsolidadoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvConsolidado)) {
            return false;
        }
        InvConsolidado other = (InvConsolidado) object;
        if ((this.invConsolidadoPK == null && other.invConsolidadoPK != null) || (this.invConsolidadoPK != null && !this.invConsolidadoPK.equals(other.invConsolidadoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.InvConsolidado[ invConsolidadoPK=" + invConsolidadoPK + " ]";
    }
    
}
