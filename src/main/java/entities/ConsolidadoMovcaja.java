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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "consolidado_movcaja", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConsolidadoMovcaja.findAll", query = "SELECT c FROM ConsolidadoMovcaja c"),
    @NamedQuery(name = "ConsolidadoMovcaja.findByCfgempresasedeidSede", query = "SELECT c FROM ConsolidadoMovcaja c WHERE c.consolidadoMovcajaPK.cfgempresasedeidSede = :cfgempresasedeidSede"),
    @NamedQuery(name = "ConsolidadoMovcaja.findByFaccajaidCaja", query = "SELECT c FROM ConsolidadoMovcaja c WHERE c.consolidadoMovcajaPK.faccajaidCaja = :faccajaidCaja"),
    @NamedQuery(name = "ConsolidadoMovcaja.findByFecha", query = "SELECT c FROM ConsolidadoMovcaja c WHERE c.consolidadoMovcajaPK.fecha = :fecha"),
    @NamedQuery(name = "ConsolidadoMovcaja.findByCfgformapagoidFormaPago", query = "SELECT c FROM ConsolidadoMovcaja c WHERE c.consolidadoMovcajaPK.cfgformapagoidFormaPago = :cfgformapagoidFormaPago"),
    @NamedQuery(name = "ConsolidadoMovcaja.findByAcumulado", query = "SELECT c FROM ConsolidadoMovcaja c WHERE c.acumulado = :acumulado")})
public class ConsolidadoMovcaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ConsolidadoMovcajaPK consolidadoMovcajaPK;
    @Basic(optional = false)
    @Column(name = "acumulado", nullable = false)
    private float acumulado;
    @JoinColumn(name = "cfg_formapago_idFormaPago", referencedColumnName = "idFormaPago", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgFormapago cfgFormapago;
    @JoinColumn(name = "cfg_empresasede_idSede", referencedColumnName = "idSede", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgEmpresasede cfgEmpresasede;
    @JoinColumn(name = "fac_caja_idCaja", referencedColumnName = "idCaja", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FacCaja facCaja;

    public ConsolidadoMovcaja() {
    }

    public ConsolidadoMovcaja(ConsolidadoMovcajaPK consolidadoMovcajaPK) {
        this.consolidadoMovcajaPK = consolidadoMovcajaPK;
    }

    public ConsolidadoMovcaja(ConsolidadoMovcajaPK consolidadoMovcajaPK, float acumulado) {
        this.consolidadoMovcajaPK = consolidadoMovcajaPK;
        this.acumulado = acumulado;
    }

    public ConsolidadoMovcaja(int cfgempresasedeidSede, int faccajaidCaja, Date fecha, int cfgformapagoidFormaPago) {
        this.consolidadoMovcajaPK = new ConsolidadoMovcajaPK(cfgempresasedeidSede, faccajaidCaja, fecha, cfgformapagoidFormaPago);
    }

    public ConsolidadoMovcajaPK getConsolidadoMovcajaPK() {
        return consolidadoMovcajaPK;
    }

    public void setConsolidadoMovcajaPK(ConsolidadoMovcajaPK consolidadoMovcajaPK) {
        this.consolidadoMovcajaPK = consolidadoMovcajaPK;
    }

    public float getAcumulado() {
        return acumulado;
    }

    public void setAcumulado(float acumulado) {
        this.acumulado = acumulado;
    }

    public CfgFormapago getCfgFormapago() {
        return cfgFormapago;
    }

    public void setCfgFormapago(CfgFormapago cfgFormapago) {
        this.cfgFormapago = cfgFormapago;
    }

    public CfgEmpresasede getCfgEmpresasede() {
        return cfgEmpresasede;
    }

    public void setCfgEmpresasede(CfgEmpresasede cfgEmpresasede) {
        this.cfgEmpresasede = cfgEmpresasede;
    }

    public FacCaja getFacCaja() {
        return facCaja;
    }

    public void setFacCaja(FacCaja facCaja) {
        this.facCaja = facCaja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (consolidadoMovcajaPK != null ? consolidadoMovcajaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConsolidadoMovcaja)) {
            return false;
        }
        ConsolidadoMovcaja other = (ConsolidadoMovcaja) object;
        if ((this.consolidadoMovcajaPK == null && other.consolidadoMovcajaPK != null) || (this.consolidadoMovcajaPK != null && !this.consolidadoMovcajaPK.equals(other.consolidadoMovcajaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ConsolidadoMovcaja[ consolidadoMovcajaPK=" + consolidadoMovcajaPK + " ]";
    }
    
}
