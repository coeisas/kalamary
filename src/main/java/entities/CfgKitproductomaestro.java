/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cfg_kitproductomaestro", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgKitproductomaestro.findAll", query = "SELECT c FROM CfgKitproductomaestro c"),
    @NamedQuery(name = "CfgKitproductomaestro.findByIdKit", query = "SELECT c FROM CfgKitproductomaestro c WHERE c.idKit = :idKit"),
    @NamedQuery(name = "CfgKitproductomaestro.findByNomKit", query = "SELECT c FROM CfgKitproductomaestro c WHERE c.nomKit = :nomKit"),
    @NamedQuery(name = "CfgKitproductomaestro.findByIdUnidad", query = "SELECT c FROM CfgKitproductomaestro c WHERE c.idUnidad = :idUnidad"),
    @NamedQuery(name = "CfgKitproductomaestro.findByCosto", query = "SELECT c FROM CfgKitproductomaestro c WHERE c.costo = :costo"),
    @NamedQuery(name = "CfgKitproductomaestro.findByUtilidad", query = "SELECT c FROM CfgKitproductomaestro c WHERE c.utilidad = :utilidad"),
    @NamedQuery(name = "CfgKitproductomaestro.findByActivo", query = "SELECT c FROM CfgKitproductomaestro c WHERE c.activo = :activo"),
    @NamedQuery(name = "CfgKitproductomaestro.findByFecCrea", query = "SELECT c FROM CfgKitproductomaestro c WHERE c.fecCrea = :fecCrea")})
public class CfgKitproductomaestro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idKit", nullable = false)
    private Integer idKit;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nomKit", nullable = false, length = 100)
    private String nomKit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idUnidad", nullable = false)
    private int idUnidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "costo", nullable = false)
    private float costo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "utilidad", nullable = false)
    private float utilidad;
    @Lob
    @Column(name = "imgkit")
    private byte[] imgkit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo", nullable = false)
    private boolean activo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecCrea", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecCrea;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgKitproductomaestro")
    private List<CfgKitproductodetalle> cfgKitproductodetalleList;
    @JoinColumn(name = "cfg_empresa_idEmpresa", referencedColumnName = "idEmpresa", nullable = false)
    @ManyToOne(optional = false)
    private CfgEmpresa cfgempresaidEmpresa;
    @JoinColumn(name = "seg_usuario_idUsuario", referencedColumnName = "idUsuario", nullable = false)
    @ManyToOne(optional = false)
    private SegUsuario segusuarioidUsuario;
    @OneToMany(mappedBy = "cfgkitproductomaestroidKit")
    private List<FacDocumentosmaster> facDocumentosmasterList;

    public CfgKitproductomaestro() {
    }

    public CfgKitproductomaestro(Integer idKit) {
        this.idKit = idKit;
    }

    public CfgKitproductomaestro(Integer idKit, String nomKit, int idUnidad, float costo, float utilidad, boolean activo, Date fecCrea) {
        this.idKit = idKit;
        this.nomKit = nomKit;
        this.idUnidad = idUnidad;
        this.costo = costo;
        this.utilidad = utilidad;
        this.activo = activo;
        this.fecCrea = fecCrea;
    }

    public Integer getIdKit() {
        return idKit;
    }

    public void setIdKit(Integer idKit) {
        this.idKit = idKit;
    }

    public String getNomKit() {
        return nomKit;
    }

    public void setNomKit(String nomKit) {
        this.nomKit = nomKit;
    }

    public int getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(int idUnidad) {
        this.idUnidad = idUnidad;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public float getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(float utilidad) {
        this.utilidad = utilidad;
    }

    public byte[] getImgkit() {
        return imgkit;
    }

    public void setImgkit(byte[] imgkit) {
        this.imgkit = imgkit;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Date getFecCrea() {
        return fecCrea;
    }

    public void setFecCrea(Date fecCrea) {
        this.fecCrea = fecCrea;
    }

    @XmlTransient
    public List<CfgKitproductodetalle> getCfgKitproductodetalleList() {
        return cfgKitproductodetalleList;
    }

    public void setCfgKitproductodetalleList(List<CfgKitproductodetalle> cfgKitproductodetalleList) {
        this.cfgKitproductodetalleList = cfgKitproductodetalleList;
    }

    public CfgEmpresa getCfgempresaidEmpresa() {
        return cfgempresaidEmpresa;
    }

    public void setCfgempresaidEmpresa(CfgEmpresa cfgempresaidEmpresa) {
        this.cfgempresaidEmpresa = cfgempresaidEmpresa;
    }

    public SegUsuario getSegusuarioidUsuario() {
        return segusuarioidUsuario;
    }

    public void setSegusuarioidUsuario(SegUsuario segusuarioidUsuario) {
        this.segusuarioidUsuario = segusuarioidUsuario;
    }

    @XmlTransient
    public List<FacDocumentosmaster> getFacDocumentosmasterList() {
        return facDocumentosmasterList;
    }

    public void setFacDocumentosmasterList(List<FacDocumentosmaster> facDocumentosmasterList) {
        this.facDocumentosmasterList = facDocumentosmasterList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idKit != null ? idKit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgKitproductomaestro)) {
            return false;
        }
        CfgKitproductomaestro other = (CfgKitproductomaestro) object;
        if ((this.idKit == null && other.idKit != null) || (this.idKit != null && !this.idKit.equals(other.idKit))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgKitproductomaestro[ idKit=" + idKit + " ]";
    }
    
}
