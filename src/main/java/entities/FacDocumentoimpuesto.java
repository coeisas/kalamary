/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
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
@Table(name = "fac_documentoimpuesto", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacDocumentoimpuesto.findAll", query = "SELECT f FROM FacDocumentoimpuesto f"),
    @NamedQuery(name = "FacDocumentoimpuesto.findByFacDocumentosmasterIddocumentomaster", query = "SELECT f FROM FacDocumentoimpuesto f WHERE f.facDocumentoimpuestoPK.facDocumentosmasterIddocumentomaster = :facDocumentosmasterIddocumentomaster"),
    @NamedQuery(name = "FacDocumentoimpuesto.findByCfgimpuestoidImpuesto", query = "SELECT f FROM FacDocumentoimpuesto f WHERE f.facDocumentoimpuestoPK.cfgimpuestoidImpuesto = :cfgimpuestoidImpuesto"),
    @NamedQuery(name = "FacDocumentoimpuesto.findByValorImpuesto", query = "SELECT f FROM FacDocumentoimpuesto f WHERE f.valorImpuesto = :valorImpuesto")})
public class FacDocumentoimpuesto implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacDocumentoimpuestoPK facDocumentoimpuestoPK;
    @Basic(optional = false)
    @Column(name = "valorImpuesto", nullable = false)
    private float valorImpuesto;
    @Basic(optional = false)
    @Column(name = "porcentajeImpuesto", nullable = false)
    private float porcentajeImpuesto;    
    @JoinColumn(name = "cfg_impuesto_idImpuesto", referencedColumnName = "idImpuesto", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgImpuesto cfgImpuesto;
    @JoinColumn(name = "fac_documentosmaster_iddocumentomaster", referencedColumnName = "iddocumentomaster", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FacDocumentosmaster facDocumentosmaster;

    public FacDocumentoimpuesto() {
    }

    public FacDocumentoimpuesto(FacDocumentoimpuestoPK facDocumentoimpuestoPK) {
        this.facDocumentoimpuestoPK = facDocumentoimpuestoPK;
    }

    public FacDocumentoimpuesto(FacDocumentoimpuestoPK facDocumentoimpuestoPK, float valorImpuesto) {
        this.facDocumentoimpuestoPK = facDocumentoimpuestoPK;
        this.valorImpuesto = valorImpuesto;
    }

    public FacDocumentoimpuesto(long facDocumentosmasterIddocumentomaster, int cfgimpuestoidImpuesto) {
        this.facDocumentoimpuestoPK = new FacDocumentoimpuestoPK(facDocumentosmasterIddocumentomaster, cfgimpuestoidImpuesto);
    }

    public FacDocumentoimpuestoPK getFacDocumentoimpuestoPK() {
        return facDocumentoimpuestoPK;
    }

    public void setFacDocumentoimpuestoPK(FacDocumentoimpuestoPK facDocumentoimpuestoPK) {
        this.facDocumentoimpuestoPK = facDocumentoimpuestoPK;
    }

    public float getValorImpuesto() {
        return valorImpuesto;
    }

    public void setValorImpuesto(float valorImpuesto) {
        this.valorImpuesto = valorImpuesto;
    }
    
    public float getPorcentajeImpuesto() {
        return porcentajeImpuesto;
    }

    public void setPorcentajeImpuesto(float porcentajeImpuesto) {
        this.porcentajeImpuesto = porcentajeImpuesto;
    }
    
    public CfgImpuesto getCfgImpuesto() {
        return cfgImpuesto;
    }

    public void setCfgImpuesto(CfgImpuesto cfgImpuesto) {
        this.cfgImpuesto = cfgImpuesto;
    }

    public FacDocumentosmaster getFacDocumentosmaster() {
        return facDocumentosmaster;
    }

    public void setFacDocumentosmaster(FacDocumentosmaster facDocumentosmaster) {
        this.facDocumentosmaster = facDocumentosmaster;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facDocumentoimpuestoPK != null ? facDocumentoimpuestoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacDocumentoimpuesto)) {
            return false;
        }
        FacDocumentoimpuesto other = (FacDocumentoimpuesto) object;
        if ((this.facDocumentoimpuestoPK == null && other.facDocumentoimpuestoPK != null) || (this.facDocumentoimpuestoPK != null && !this.facDocumentoimpuestoPK.equals(other.facDocumentoimpuestoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FacDocumentoimpuesto[ facDocumentoimpuestoPK=" + facDocumentoimpuestoPK + " ]";
    }
    
}
