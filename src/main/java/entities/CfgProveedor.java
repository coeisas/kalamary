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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cfg_proveedor", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgProveedor.findAll", query = "SELECT c FROM CfgProveedor c"),
    @NamedQuery(name = "CfgProveedor.findByIdProveedor", query = "SELECT c FROM CfgProveedor c WHERE c.idProveedor = :idProveedor"),
    @NamedQuery(name = "CfgProveedor.findByNumDoc", query = "SELECT c FROM CfgProveedor c WHERE c.numDoc = :numDoc"),
    @NamedQuery(name = "CfgProveedor.findByNomProveedor", query = "SELECT c FROM CfgProveedor c WHERE c.nomProveedor = :nomProveedor"),
    @NamedQuery(name = "CfgProveedor.findByContactoProveedor", query = "SELECT c FROM CfgProveedor c WHERE c.contactoProveedor = :contactoProveedor"),
    @NamedQuery(name = "CfgProveedor.findByIdFormaPago", query = "SELECT c FROM CfgProveedor c WHERE c.idFormaPago = :idFormaPago"),
    @NamedQuery(name = "CfgProveedor.findByDirProveedor", query = "SELECT c FROM CfgProveedor c WHERE c.dirProveedor = :dirProveedor"),
    @NamedQuery(name = "CfgProveedor.findByTel1Proveedor", query = "SELECT c FROM CfgProveedor c WHERE c.tel1Proveedor = :tel1Proveedor"),
    @NamedQuery(name = "CfgProveedor.findByTel2Proveedor", query = "SELECT c FROM CfgProveedor c WHERE c.tel2Proveedor = :tel2Proveedor"),
    @NamedQuery(name = "CfgProveedor.findByWebsiteProveedor", query = "SELECT c FROM CfgProveedor c WHERE c.websiteProveedor = :websiteProveedor"),
    @NamedQuery(name = "CfgProveedor.findByMailProveedor", query = "SELECT c FROM CfgProveedor c WHERE c.mailProveedor = :mailProveedor"),
    @NamedQuery(name = "CfgProveedor.findByFecCrea", query = "SELECT c FROM CfgProveedor c WHERE c.fecCrea = :fecCrea")})
public class CfgProveedor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idProveedor", nullable = false)
    private Integer idProveedor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "numDoc", nullable = false, length = 20)
    private String numDoc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nomProveedor", nullable = false, length = 150)
    private String nomProveedor;
    @Size(max = 150)
    @Column(name = "contactoProveedor", length = 150)
    private String contactoProveedor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idFormaPago", nullable = false)
    private int idFormaPago;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "dirProveedor", nullable = false, length = 150)
    private String dirProveedor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "tel1Proveedor", nullable = false, length = 10)
    private String tel1Proveedor;
    @Size(max = 10)
    @Column(name = "tel2Proveedor", length = 10)
    private String tel2Proveedor;
    @Size(max = 30)
    @Column(name = "websiteProveedor", length = 30)
    private String websiteProveedor;
    @Size(max = 30)
    @Column(name = "mailProveedor", length = 30)
    private String mailProveedor;
    @Lob
    @Column(name = "logo")
    private byte[] logo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecCrea", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecCrea;
    @JoinColumn(name = "cfg_empresa_idEmpresa", referencedColumnName = "idEmpresa", nullable = false)
    @ManyToOne(optional = false)
    private CfgEmpresa cfgempresaidEmpresa;
    @JoinColumns({
        @JoinColumn(name = "cfg_municipio_idMunicipio", referencedColumnName = "idMunicipio", nullable = false),
        @JoinColumn(name = "cfg_municipio_cfg_departamento_idDepartamento", referencedColumnName = "cfg_departamento_idDepartamento", nullable = false)})
    @ManyToOne(optional = false)
    private CfgMunicipio cfgMunicipio;
    @JoinColumn(name = "cfg_tipoidentificacion_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private CfgTipoidentificacion cfgTipoidentificacionId;
    @JoinColumn(name = "seg_usuario_idUsuario", referencedColumnName = "idUsuario", nullable = false)
    @ManyToOne(optional = false)
    private SegUsuario segusuarioidUsuario;

    public CfgProveedor() {
    }

    public CfgProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public CfgProveedor(Integer idProveedor, String numDoc, String nomProveedor, int idFormaPago, String dirProveedor, String tel1Proveedor, Date fecCrea) {
        this.idProveedor = idProveedor;
        this.numDoc = numDoc;
        this.nomProveedor = nomProveedor;
        this.idFormaPago = idFormaPago;
        this.dirProveedor = dirProveedor;
        this.tel1Proveedor = tel1Proveedor;
        this.fecCrea = fecCrea;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getNomProveedor() {
        return nomProveedor;
    }

    public void setNomProveedor(String nomProveedor) {
        this.nomProveedor = nomProveedor;
    }

    public String getContactoProveedor() {
        return contactoProveedor;
    }

    public void setContactoProveedor(String contactoProveedor) {
        this.contactoProveedor = contactoProveedor;
    }

    public int getIdFormaPago() {
        return idFormaPago;
    }

    public void setIdFormaPago(int idFormaPago) {
        this.idFormaPago = idFormaPago;
    }

    public String getDirProveedor() {
        return dirProveedor;
    }

    public void setDirProveedor(String dirProveedor) {
        this.dirProveedor = dirProveedor;
    }

    public String getTel1Proveedor() {
        return tel1Proveedor;
    }

    public void setTel1Proveedor(String tel1Proveedor) {
        this.tel1Proveedor = tel1Proveedor;
    }

    public String getTel2Proveedor() {
        return tel2Proveedor;
    }

    public void setTel2Proveedor(String tel2Proveedor) {
        this.tel2Proveedor = tel2Proveedor;
    }

    public String getWebsiteProveedor() {
        return websiteProveedor;
    }

    public void setWebsiteProveedor(String websiteProveedor) {
        this.websiteProveedor = websiteProveedor;
    }

    public String getMailProveedor() {
        return mailProveedor;
    }

    public void setMailProveedor(String mailProveedor) {
        this.mailProveedor = mailProveedor;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public Date getFecCrea() {
        return fecCrea;
    }

    public void setFecCrea(Date fecCrea) {
        this.fecCrea = fecCrea;
    }

    public CfgEmpresa getCfgempresaidEmpresa() {
        return cfgempresaidEmpresa;
    }

    public void setCfgempresaidEmpresa(CfgEmpresa cfgempresaidEmpresa) {
        this.cfgempresaidEmpresa = cfgempresaidEmpresa;
    }

    public CfgMunicipio getCfgMunicipio() {
        return cfgMunicipio;
    }

    public void setCfgMunicipio(CfgMunicipio cfgMunicipio) {
        this.cfgMunicipio = cfgMunicipio;
    }

    public CfgTipoidentificacion getCfgTipoidentificacionId() {
        return cfgTipoidentificacionId;
    }

    public void setCfgTipoidentificacionId(CfgTipoidentificacion cfgTipoidentificacionId) {
        this.cfgTipoidentificacionId = cfgTipoidentificacionId;
    }

    public SegUsuario getSegusuarioidUsuario() {
        return segusuarioidUsuario;
    }

    public void setSegusuarioidUsuario(SegUsuario segusuarioidUsuario) {
        this.segusuarioidUsuario = segusuarioidUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProveedor != null ? idProveedor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgProveedor)) {
            return false;
        }
        CfgProveedor other = (CfgProveedor) object;
        if ((this.idProveedor == null && other.idProveedor != null) || (this.idProveedor != null && !this.idProveedor.equals(other.idProveedor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgProveedor[ idProveedor=" + idProveedor + " ]";
    }
    
}
