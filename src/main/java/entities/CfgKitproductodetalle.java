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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cfg_kitproductodetalle", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgKitproductodetalle.findAll", query = "SELECT c FROM CfgKitproductodetalle c"),
    @NamedQuery(name = "CfgKitproductodetalle.findByCfgkitproductomaestroidKit", query = "SELECT c FROM CfgKitproductodetalle c WHERE c.cfgKitproductodetallePK.cfgkitproductomaestroidKit = :cfgkitproductomaestroidKit"),
    @NamedQuery(name = "CfgKitproductodetalle.findByCant", query = "SELECT c FROM CfgKitproductodetalle c WHERE c.cant = :cant"),
    @NamedQuery(name = "CfgKitproductodetalle.findByCosto", query = "SELECT c FROM CfgKitproductodetalle c WHERE c.costo = :costo"),
    @NamedQuery(name = "CfgKitproductodetalle.findByEstado", query = "SELECT c FROM CfgKitproductodetalle c WHERE c.estado = :estado"),
    @NamedQuery(name = "CfgKitproductodetalle.findByFecCrea", query = "SELECT c FROM CfgKitproductodetalle c WHERE c.fecCrea = :fecCrea"),
    @NamedQuery(name = "CfgKitproductodetalle.findByUsrCrea", query = "SELECT c FROM CfgKitproductodetalle c WHERE c.usrCrea = :usrCrea"),
    @NamedQuery(name = "CfgKitproductodetalle.findByCfgproductoidProducto", query = "SELECT c FROM CfgKitproductodetalle c WHERE c.cfgKitproductodetallePK.cfgproductoidProducto = :cfgproductoidProducto"),
    @NamedQuery(name = "CfgKitproductodetalle.findByFacDocumentosmasterIddocumentomaster", query = "SELECT c FROM CfgKitproductodetalle c WHERE c.facDocumentosmasterIddocumentomaster = :facDocumentosmasterIddocumentomaster")})
public class CfgKitproductodetalle implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CfgKitproductodetallePK cfgKitproductodetallePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant", nullable = false)
    private float cant;
    @Basic(optional = false)
    @NotNull
    @Column(name = "costo", nullable = false)
    private float costo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado", nullable = false)
    private boolean estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecCrea", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecCrea;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usrCrea", nullable = false)
    private int usrCrea;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fac_documentosmaster_iddocumentomaster", nullable = false)
    private long facDocumentosmasterIddocumentomaster;
    @JoinColumn(name = "cfg_kitproductomaestro_idKit", referencedColumnName = "idKit", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgKitproductomaestro cfgKitproductomaestro;
    @JoinColumn(name = "cfg_producto_idProducto", referencedColumnName = "idProducto", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgProducto cfgProducto;

    public CfgKitproductodetalle() {
    }

    public CfgKitproductodetalle(CfgKitproductodetallePK cfgKitproductodetallePK) {
        this.cfgKitproductodetallePK = cfgKitproductodetallePK;
    }

    public CfgKitproductodetalle(CfgKitproductodetallePK cfgKitproductodetallePK, float cant, float costo, boolean estado, Date fecCrea, int usrCrea, long facDocumentosmasterIddocumentomaster) {
        this.cfgKitproductodetallePK = cfgKitproductodetallePK;
        this.cant = cant;
        this.costo = costo;
        this.estado = estado;
        this.fecCrea = fecCrea;
        this.usrCrea = usrCrea;
        this.facDocumentosmasterIddocumentomaster = facDocumentosmasterIddocumentomaster;
    }

    public CfgKitproductodetalle(int cfgkitproductomaestroidKit, int cfgproductoidProducto) {
        this.cfgKitproductodetallePK = new CfgKitproductodetallePK(cfgkitproductomaestroidKit, cfgproductoidProducto);
    }

    public CfgKitproductodetallePK getCfgKitproductodetallePK() {
        return cfgKitproductodetallePK;
    }

    public void setCfgKitproductodetallePK(CfgKitproductodetallePK cfgKitproductodetallePK) {
        this.cfgKitproductodetallePK = cfgKitproductodetallePK;
    }

    public float getCant() {
        return cant;
    }

    public void setCant(float cant) {
        this.cant = cant;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Date getFecCrea() {
        return fecCrea;
    }

    public void setFecCrea(Date fecCrea) {
        this.fecCrea = fecCrea;
    }

    public int getUsrCrea() {
        return usrCrea;
    }

    public void setUsrCrea(int usrCrea) {
        this.usrCrea = usrCrea;
    }

    public long getFacDocumentosmasterIddocumentomaster() {
        return facDocumentosmasterIddocumentomaster;
    }

    public void setFacDocumentosmasterIddocumentomaster(long facDocumentosmasterIddocumentomaster) {
        this.facDocumentosmasterIddocumentomaster = facDocumentosmasterIddocumentomaster;
    }

    public CfgKitproductomaestro getCfgKitproductomaestro() {
        return cfgKitproductomaestro;
    }

    public void setCfgKitproductomaestro(CfgKitproductomaestro cfgKitproductomaestro) {
        this.cfgKitproductomaestro = cfgKitproductomaestro;
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
        hash += (cfgKitproductodetallePK != null ? cfgKitproductodetallePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgKitproductodetalle)) {
            return false;
        }
        CfgKitproductodetalle other = (CfgKitproductodetalle) object;
        if ((this.cfgKitproductodetallePK == null && other.cfgKitproductodetallePK != null) || (this.cfgKitproductodetallePK != null && !this.cfgKitproductodetallePK.equals(other.cfgKitproductodetallePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgKitproductodetalle[ cfgKitproductodetallePK=" + cfgKitproductodetallePK + " ]";
    }
   
}
