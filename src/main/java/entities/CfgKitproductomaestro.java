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
    @NamedQuery(name = "CfgKitproductomaestro.findByFecCrea", query = "SELECT c FROM CfgKitproductomaestro c WHERE c.fecCrea = :fecCrea"),
    @NamedQuery(name = "CfgKitproductomaestro.findByCodKit", query = "SELECT c FROM CfgKitproductomaestro c WHERE c.codKit = :codKit"),
    @NamedQuery(name = "CfgKitproductomaestro.findByPrecio", query = "SELECT c FROM CfgKitproductomaestro c WHERE c.precio = :precio")})
public class CfgKitproductomaestro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idKit", nullable = false)
    private Integer idKit;
    @Basic(optional = false)
    @Column(name = "nomKit", nullable = false, length = 100)
    private String nomKit;
    @Basic(optional = false)
    @Column(name = "idUnidad", nullable = false)
    private int idUnidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "costo", precision = 12)
    private Float costo;
    @Basic(optional = false)
    @Column(name = "utilidad", nullable = false)
    private float utilidad;
    @Lob
    @Column(name = "imgkit")
    private byte[] imgkit;
    @Basic(optional = false)
    @Column(name = "activo", nullable = false)
    private boolean activo;
    @Basic(optional = false)
    @Column(name = "fecCrea", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecCrea;
    @Basic(optional = false)
    @Column(name = "codKit", nullable = false, length = 10)
    private String codKit;
    @Column(name = "precio", precision = 12)
    private Float precio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgKitproductomaestro")
    private List<CfgKitproductodetalle> cfgKitproductodetalleList;
    @JoinColumn(name = "cfg_empresa_idEmpresa", referencedColumnName = "idEmpresa", nullable = false)
    @ManyToOne(optional = false)
    private CfgEmpresa cfgempresaidEmpresa;
    @JoinColumn(name = "seg_usuario_idUsuario", referencedColumnName = "idUsuario", nullable = false)
    @ManyToOne(optional = false)
    private SegUsuario segusuarioidUsuario;
    @OneToMany(mappedBy = "cfgkitproductomaestroidKit")
    private List<CfgProducto> cfgProductoList;

    public CfgKitproductomaestro() {
    }

    public CfgKitproductomaestro(Integer idKit) {
        this.idKit = idKit;
    }

    public CfgKitproductomaestro(Integer idKit, String nomKit, int idUnidad, float utilidad, boolean activo, Date fecCrea, String codKit) {
        this.idKit = idKit;
        this.nomKit = nomKit;
        this.idUnidad = idUnidad;
        this.utilidad = utilidad;
        this.activo = activo;
        this.fecCrea = fecCrea;
        this.codKit = codKit;
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

    public Float getCosto() {
        return costo;
    }

    public void setCosto(Float costo) {
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

    public String getCodKit() {
        return codKit;
    }

    public void setCodKit(String codKit) {
        this.codKit = codKit;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
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
    public List<CfgProducto> getCfgProductoList() {
        return cfgProductoList;
    }

    public void setCfgProductoList(List<CfgProducto> cfgProductoList) {
        this.cfgProductoList = cfgProductoList;
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
