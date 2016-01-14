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
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mario
 */
@Embeddable
public class ResCfgMovCtaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "cfg_empresasede_idSede", nullable = false)
    private int cfgempresasedeidSede;
    @Basic(optional = false)
    @Column(name = "cfg_aplicaciondocumento_idaplicacion", nullable = false)
    private int cfgAplicaciondocumentoIdaplicacion;
    @Basic(optional = false)
    @Column(name = "cnt_detalle_idcnt_detalle", nullable = false)
    private int cntDetalleIdcntDetalle;
    @Basic(optional = false)
    @Column(name = "forma_pago", nullable = false)
    private int formaPago;
    @Basic(optional = false)
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    public ResCfgMovCtaPK() {
    }

    public ResCfgMovCtaPK(int cfgempresasedeidSede, int cfgAplicaciondocumentoIdaplicacion, int cntDetalleIdcntDetalle, int formaPago, Date fecha) {
        this.cfgempresasedeidSede = cfgempresasedeidSede;
        this.cfgAplicaciondocumentoIdaplicacion = cfgAplicaciondocumentoIdaplicacion;
        this.cntDetalleIdcntDetalle = cntDetalleIdcntDetalle;
        this.formaPago = formaPago;
        this.fecha = fecha;
    }

    public int getCfgempresasedeidSede() {
        return cfgempresasedeidSede;
    }

    public void setCfgempresasedeidSede(int cfgempresasedeidSede) {
        this.cfgempresasedeidSede = cfgempresasedeidSede;
    }

    public int getCfgAplicaciondocumentoIdaplicacion() {
        return cfgAplicaciondocumentoIdaplicacion;
    }

    public void setCfgAplicaciondocumentoIdaplicacion(int cfgAplicaciondocumentoIdaplicacion) {
        this.cfgAplicaciondocumentoIdaplicacion = cfgAplicaciondocumentoIdaplicacion;
    }

    public int getCntDetalleIdcntDetalle() {
        return cntDetalleIdcntDetalle;
    }

    public void setCntDetalleIdcntDetalle(int cntDetalleIdcntDetalle) {
        this.cntDetalleIdcntDetalle = cntDetalleIdcntDetalle;
    }

    public int getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(int formaPago) {
        this.formaPago = formaPago;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cfgempresasedeidSede;
        hash += (int) cfgAplicaciondocumentoIdaplicacion;
        hash += (int) cntDetalleIdcntDetalle;
        hash += (int) formaPago;
        hash += (fecha != null ? fecha.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResCfgMovCtaPK)) {
            return false;
        }
        ResCfgMovCtaPK other = (ResCfgMovCtaPK) object;
        if (this.cfgempresasedeidSede != other.cfgempresasedeidSede) {
            return false;
        }
        if (this.cfgAplicaciondocumentoIdaplicacion != other.cfgAplicaciondocumentoIdaplicacion) {
            return false;
        }
        if (this.cntDetalleIdcntDetalle != other.cntDetalleIdcntDetalle) {
            return false;
        }
        if (this.formaPago != other.formaPago) {
            return false;
        }
        if ((this.fecha == null && other.fecha != null) || (this.fecha != null && !this.fecha.equals(other.fecha))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ResCfgMovCtaPK[ cfgempresasedeidSede=" + cfgempresasedeidSede + ", cfgAplicaciondocumentoIdaplicacion=" + cfgAplicaciondocumentoIdaplicacion + ", cntDetalleIdcntDetalle=" + cntDetalleIdcntDetalle + ", formaPago=" + formaPago + ", fecha=" + fecha + " ]";
    }
    
}
