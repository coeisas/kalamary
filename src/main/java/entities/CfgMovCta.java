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
@Table(name = "cfg_mov_cta", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgMovCta.findAll", query = "SELECT c FROM CfgMovCta c"),
    @NamedQuery(name = "CfgMovCta.findByCfgempresasedeidSede", query = "SELECT c FROM CfgMovCta c WHERE c.cfgMovCtaPK.cfgempresasedeidSede = :cfgempresasedeidSede"),
    @NamedQuery(name = "CfgMovCta.findByCfgAplicaciondocumentoIdaplicacion", query = "SELECT c FROM CfgMovCta c WHERE c.cfgMovCtaPK.cfgAplicaciondocumentoIdaplicacion = :cfgAplicaciondocumentoIdaplicacion"),
    @NamedQuery(name = "CfgMovCta.findByCntDetalleIdcntDetalle", query = "SELECT c FROM CfgMovCta c WHERE c.cfgMovCtaPK.cntDetalleIdcntDetalle = :cntDetalleIdcntDetalle"),
    @NamedQuery(name = "CfgMovCta.findByFormaPago", query = "SELECT c FROM CfgMovCta c WHERE c.cfgMovCtaPK.formaPago = :formaPago"),
    @NamedQuery(name = "CfgMovCta.findByAplica", query = "SELECT c FROM CfgMovCta c WHERE c.aplica = :aplica")})
public class CfgMovCta implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CfgMovCtaPK cfgMovCtaPK;
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

    public CfgMovCta() {
    }

    public CfgMovCta(CfgMovCtaPK cfgMovCtaPK) {
        this.cfgMovCtaPK = cfgMovCtaPK;
    }

    public CfgMovCta(int cfgempresasedeidSede, int cfgAplicaciondocumentoIdaplicacion, int cntDetalleIdcntDetalle, int formaPago) {
        this.cfgMovCtaPK = new CfgMovCtaPK(cfgempresasedeidSede, cfgAplicaciondocumentoIdaplicacion, cntDetalleIdcntDetalle, formaPago);
    }

    public CfgMovCtaPK getCfgMovCtaPK() {
        return cfgMovCtaPK;
    }

    public void setCfgMovCtaPK(CfgMovCtaPK cfgMovCtaPK) {
        this.cfgMovCtaPK = cfgMovCtaPK;
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
        hash += (cfgMovCtaPK != null ? cfgMovCtaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgMovCta)) {
            return false;
        }
        CfgMovCta other = (CfgMovCta) object;
        if ((this.cfgMovCtaPK == null && other.cfgMovCtaPK != null) || (this.cfgMovCtaPK != null && !this.cfgMovCtaPK.equals(other.cfgMovCtaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgMovCta[ cfgMovCtaPK=" + cfgMovCtaPK + " ]";
    }
    
}
