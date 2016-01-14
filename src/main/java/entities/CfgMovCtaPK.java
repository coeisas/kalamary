/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Mario
 */
@Embeddable
public class CfgMovCtaPK implements Serializable {
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

    public CfgMovCtaPK() {
    }

    public CfgMovCtaPK(int cfgempresasedeidSede, int cfgAplicaciondocumentoIdaplicacion, int cntDetalleIdcntDetalle, int formaPago) {
        this.cfgempresasedeidSede = cfgempresasedeidSede;
        this.cfgAplicaciondocumentoIdaplicacion = cfgAplicaciondocumentoIdaplicacion;
        this.cntDetalleIdcntDetalle = cntDetalleIdcntDetalle;
        this.formaPago = formaPago;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cfgempresasedeidSede;
        hash += (int) cfgAplicaciondocumentoIdaplicacion;
        hash += (int) cntDetalleIdcntDetalle;
        hash += (int) formaPago;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgMovCtaPK)) {
            return false;
        }
        CfgMovCtaPK other = (CfgMovCtaPK) object;
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
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgMovCtaPK[ cfgempresasedeidSede=" + cfgempresasedeidSede + ", cfgAplicaciondocumentoIdaplicacion=" + cfgAplicaciondocumentoIdaplicacion + ", cntDetalleIdcntDetalle=" + cntDetalleIdcntDetalle + ", formaPago=" + formaPago + " ]";
    }
    
}
