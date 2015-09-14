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
 * @author mario
 */
@Embeddable
public class ConsolidadoMovcajaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "cfg_empresasede_idSede", nullable = false)
    private int cfgempresasedeidSede;
    @Basic(optional = false)
    @Column(name = "fac_caja_idCaja", nullable = false)
    private int faccajaidCaja;
    @Basic(optional = false)
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "cfg_formapago_idFormaPago", nullable = false)
    private int cfgformapagoidFormaPago;

    public ConsolidadoMovcajaPK() {
    }

    public ConsolidadoMovcajaPK(int cfgempresasedeidSede, int faccajaidCaja, Date fecha, int cfgformapagoidFormaPago) {
        this.cfgempresasedeidSede = cfgempresasedeidSede;
        this.faccajaidCaja = faccajaidCaja;
        this.fecha = fecha;
        this.cfgformapagoidFormaPago = cfgformapagoidFormaPago;
    }

    public int getCfgempresasedeidSede() {
        return cfgempresasedeidSede;
    }

    public void setCfgempresasedeidSede(int cfgempresasedeidSede) {
        this.cfgempresasedeidSede = cfgempresasedeidSede;
    }

    public int getFaccajaidCaja() {
        return faccajaidCaja;
    }

    public void setFaccajaidCaja(int faccajaidCaja) {
        this.faccajaidCaja = faccajaidCaja;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getCfgformapagoidFormaPago() {
        return cfgformapagoidFormaPago;
    }

    public void setCfgformapagoidFormaPago(int cfgformapagoidFormaPago) {
        this.cfgformapagoidFormaPago = cfgformapagoidFormaPago;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cfgempresasedeidSede;
        hash += (int) faccajaidCaja;
        hash += (fecha != null ? fecha.hashCode() : 0);
        hash += (int) cfgformapagoidFormaPago;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConsolidadoMovcajaPK)) {
            return false;
        }
        ConsolidadoMovcajaPK other = (ConsolidadoMovcajaPK) object;
        if (this.cfgempresasedeidSede != other.cfgempresasedeidSede) {
            return false;
        }
        if (this.faccajaidCaja != other.faccajaidCaja) {
            return false;
        }
        if ((this.fecha == null && other.fecha != null) || (this.fecha != null && !this.fecha.equals(other.fecha))) {
            return false;
        }
        if (this.cfgformapagoidFormaPago != other.cfgformapagoidFormaPago) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ConsolidadoMovcajaPK[ cfgempresasedeidSede=" + cfgempresasedeidSede + ", faccajaidCaja=" + faccajaidCaja + ", fecha=" + fecha + ", cfgformapagoidFormaPago=" + cfgformapagoidFormaPago + " ]";
    }
    
}
