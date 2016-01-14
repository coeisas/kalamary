/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
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
 * @author Mario
 */
@Entity
@Table(name = "res_cfg_mov_cta", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResCfgMovCta.findAll", query = "SELECT r FROM ResCfgMovCta r"),
    @NamedQuery(name = "ResCfgMovCta.findByCfgempresasedeidSede", query = "SELECT r FROM ResCfgMovCta r WHERE r.resCfgMovCtaPK.cfgempresasedeidSede = :cfgempresasedeidSede"),
    @NamedQuery(name = "ResCfgMovCta.findByCfgAplicaciondocumentoIdaplicacion", query = "SELECT r FROM ResCfgMovCta r WHERE r.resCfgMovCtaPK.cfgAplicaciondocumentoIdaplicacion = :cfgAplicaciondocumentoIdaplicacion"),
    @NamedQuery(name = "ResCfgMovCta.findByCntDetalleIdcntDetalle", query = "SELECT r FROM ResCfgMovCta r WHERE r.resCfgMovCtaPK.cntDetalleIdcntDetalle = :cntDetalleIdcntDetalle"),
    @NamedQuery(name = "ResCfgMovCta.findByFormaPago", query = "SELECT r FROM ResCfgMovCta r WHERE r.resCfgMovCtaPK.formaPago = :formaPago"),
    @NamedQuery(name = "ResCfgMovCta.findByFecha", query = "SELECT r FROM ResCfgMovCta r WHERE r.resCfgMovCtaPK.fecha = :fecha"),
    @NamedQuery(name = "ResCfgMovCta.findByAplica", query = "SELECT r FROM ResCfgMovCta r WHERE r.aplica = :aplica")})
public class ResCfgMovCta implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ResCfgMovCtaPK resCfgMovCtaPK;
    @Column(name = "aplica")
    private Integer aplica;
    @JoinColumn(name = "cfg_aplicaciondocumento_idaplicacion", referencedColumnName = "idaplicacion", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgAplicaciondocumento cfgAplicaciondocumento;
    @JoinColumn(name = "cfg_empresasede_idSede", referencedColumnName = "idSede", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgEmpresasede cfgEmpresasede;
    @JoinColumn(name = "cnt_detalle_idcnt_detalle", referencedColumnName = "idcnt_detalle", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CntDetalle cntDetalle;
    @JoinColumn(name = "cnt_puc_codigoCuenta", referencedColumnName = "codigoCuenta")
    @ManyToOne
    private CntPuc cntpuccodigoCuenta;

    public ResCfgMovCta() {
    }

    public ResCfgMovCta(ResCfgMovCtaPK resCfgMovCtaPK) {
        this.resCfgMovCtaPK = resCfgMovCtaPK;
    }

    public ResCfgMovCta(int cfgempresasedeidSede, int cfgAplicaciondocumentoIdaplicacion, int cntDetalleIdcntDetalle, int formaPago, Date fecha) {
        this.resCfgMovCtaPK = new ResCfgMovCtaPK(cfgempresasedeidSede, cfgAplicaciondocumentoIdaplicacion, cntDetalleIdcntDetalle, formaPago, fecha);
    }

    public ResCfgMovCtaPK getResCfgMovCtaPK() {
        return resCfgMovCtaPK;
    }

    public void setResCfgMovCtaPK(ResCfgMovCtaPK resCfgMovCtaPK) {
        this.resCfgMovCtaPK = resCfgMovCtaPK;
    }

    public Integer getAplica() {
        return aplica;
    }

    public void setAplica(Integer aplica) {
        this.aplica = aplica;
    }

    public CfgAplicaciondocumento getCfgAplicaciondocumento() {
        return cfgAplicaciondocumento;
    }

    public void setCfgAplicaciondocumento(CfgAplicaciondocumento cfgAplicaciondocumento) {
        this.cfgAplicaciondocumento = cfgAplicaciondocumento;
    }

    public CfgEmpresasede getCfgEmpresasede() {
        return cfgEmpresasede;
    }

    public void setCfgEmpresasede(CfgEmpresasede cfgEmpresasede) {
        this.cfgEmpresasede = cfgEmpresasede;
    }

    public CntDetalle getCntDetalle() {
        return cntDetalle;
    }

    public void setCntDetalle(CntDetalle cntDetalle) {
        this.cntDetalle = cntDetalle;
    }

    public CntPuc getCntpuccodigoCuenta() {
        return cntpuccodigoCuenta;
    }

    public void setCntpuccodigoCuenta(CntPuc cntpuccodigoCuenta) {
        this.cntpuccodigoCuenta = cntpuccodigoCuenta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (resCfgMovCtaPK != null ? resCfgMovCtaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResCfgMovCta)) {
            return false;
        }
        ResCfgMovCta other = (ResCfgMovCta) object;
        if ((this.resCfgMovCtaPK == null && other.resCfgMovCtaPK != null) || (this.resCfgMovCtaPK != null && !this.resCfgMovCtaPK.equals(other.resCfgMovCtaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ResCfgMovCta[ resCfgMovCtaPK=" + resCfgMovCtaPK + " ]";
    }
    
}
