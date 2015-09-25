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
import javax.persistence.JoinColumns;
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
@Table(name = "cfg_empresa", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgEmpresa.findAll", query = "SELECT c FROM CfgEmpresa c"),
    @NamedQuery(name = "CfgEmpresa.findByIdEmpresa", query = "SELECT c FROM CfgEmpresa c WHERE c.idEmpresa = :idEmpresa"),
    @NamedQuery(name = "CfgEmpresa.findByNumDoc", query = "SELECT c FROM CfgEmpresa c WHERE c.numDoc = :numDoc"),
    @NamedQuery(name = "CfgEmpresa.findByNomEmpresa", query = "SELECT c FROM CfgEmpresa c WHERE c.nomEmpresa = :nomEmpresa"),
    @NamedQuery(name = "CfgEmpresa.findByDirEmpresa", query = "SELECT c FROM CfgEmpresa c WHERE c.dirEmpresa = :dirEmpresa"),
    @NamedQuery(name = "CfgEmpresa.findByTel1", query = "SELECT c FROM CfgEmpresa c WHERE c.tel1 = :tel1"),
    @NamedQuery(name = "CfgEmpresa.findByTel2", query = "SELECT c FROM CfgEmpresa c WHERE c.tel2 = :tel2"),
    @NamedQuery(name = "CfgEmpresa.findByWebsite", query = "SELECT c FROM CfgEmpresa c WHERE c.website = :website"),
    @NamedQuery(name = "CfgEmpresa.findByMail", query = "SELECT c FROM CfgEmpresa c WHERE c.mail = :mail"),
    @NamedQuery(name = "CfgEmpresa.findByFecCrea", query = "SELECT c FROM CfgEmpresa c WHERE c.fecCrea = :fecCrea"),
    @NamedQuery(name = "CfgEmpresa.findByCodEmpresa", query = "SELECT c FROM CfgEmpresa c WHERE c.codEmpresa = :codEmpresa")})
public class CfgEmpresa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idEmpresa", nullable = false)
    private Integer idEmpresa;
    @Basic(optional = false)
    @Column(name = "numDoc", nullable = false, length = 20)
    private String numDoc;
    @Basic(optional = false)
    @Column(name = "nomEmpresa", nullable = false, length = 150)
    private String nomEmpresa;
    @Basic(optional = false)
    @Column(name = "dirEmpresa", nullable = false, length = 150)
    private String dirEmpresa;
    @Basic(optional = false)
    @Column(name = "tel1", nullable = false, length = 10)
    private String tel1;
    @Column(name = "tel2", length = 10)
    private String tel2;
    @Column(name = "website", length = 30)
    private String website;
    @Column(name = "mail", length = 30)
    private String mail;
    @Lob
    @Column(name = "logo")
    private byte[] logo;
    @Basic(optional = false)
    @Column(name = "fecCrea", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecCrea;
    @Basic(optional = false)
    @Column(name = "cod_empresa", nullable = false, length = 5)
    private String codEmpresa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgempresaidEmpresa")
    private List<CfgCliente> cfgClienteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgempresaidEmpresa")
    private List<CfgformaPagoproveedor> cfgformaPagoproveedorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgempresaidEmpresa")
    private List<CfgReferenciaproducto> cfgReferenciaproductoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgempresaidEmpresa")
    private List<CfgMarcaproducto> cfgMarcaproductoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgempresaidEmpresa")
    private List<CfgProveedor> cfgProveedorList;
    @OneToMany(mappedBy = "cfgempresaidEmpresa")
    private List<SegUsuario> segUsuarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgempresaidEmpresa")
    private List<CfgImpuesto> cfgImpuestoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgempresaidEmpresa")
    private List<CfgFormapago> cfgFormapagoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgempresaidEmpresa")
    private List<CfgKitproductomaestro> cfgKitproductomaestroList;
    @JoinColumns({
        @JoinColumn(name = "cfg_municipio_idMunicipio", referencedColumnName = "idMunicipio", nullable = false),
        @JoinColumn(name = "cfg_municipio_cfg_departamento_idDepartamento", referencedColumnName = "cfg_departamento_idDepartamento", nullable = false)})
    @ManyToOne(optional = false)
    private CfgMunicipio cfgMunicipio;
    @JoinColumn(name = "cfg_tipodocempresa_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private CfgTipodocempresa cfgTipodocempresaId;
    @JoinColumn(name = "cfg_tipoempresa_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private CfgTipoempresa cfgTipoempresaId;
    @JoinColumn(name = "seg_usuario_idUsuario", referencedColumnName = "idUsuario", nullable = false)
    @ManyToOne(optional = false)
    private SegUsuario segusuarioidUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgempresaidEmpresa")
    private List<CfgEmpresasede> cfgEmpresasedeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgempresaidEmpresa")
    private List<CfgProducto> cfgProductoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgempresaidEmpresa")
    private List<CfgCategoriaproducto> cfgCategoriaproductoList;

    public CfgEmpresa() {
    }

    public CfgEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public CfgEmpresa(Integer idEmpresa, String numDoc, String nomEmpresa, String dirEmpresa, String tel1, Date fecCrea, String codEmpresa) {
        this.idEmpresa = idEmpresa;
        this.numDoc = numDoc;
        this.nomEmpresa = nomEmpresa;
        this.dirEmpresa = dirEmpresa;
        this.tel1 = tel1;
        this.fecCrea = fecCrea;
        this.codEmpresa = codEmpresa;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getNomEmpresa() {
        return nomEmpresa;
    }

    public void setNomEmpresa(String nomEmpresa) {
        this.nomEmpresa = nomEmpresa;
    }

    public String getDirEmpresa() {
        return dirEmpresa;
    }

    public void setDirEmpresa(String dirEmpresa) {
        this.dirEmpresa = dirEmpresa;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

    public String getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(String codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    @XmlTransient
    public List<CfgCliente> getCfgClienteList() {
        return cfgClienteList;
    }

    public void setCfgClienteList(List<CfgCliente> cfgClienteList) {
        this.cfgClienteList = cfgClienteList;
    }

    @XmlTransient
    public List<CfgformaPagoproveedor> getCfgformaPagoproveedorList() {
        return cfgformaPagoproveedorList;
    }

    public void setCfgformaPagoproveedorList(List<CfgformaPagoproveedor> cfgformaPagoproveedorList) {
        this.cfgformaPagoproveedorList = cfgformaPagoproveedorList;
    }

    @XmlTransient
    public List<CfgReferenciaproducto> getCfgReferenciaproductoList() {
        return cfgReferenciaproductoList;
    }

    public void setCfgReferenciaproductoList(List<CfgReferenciaproducto> cfgReferenciaproductoList) {
        this.cfgReferenciaproductoList = cfgReferenciaproductoList;
    }

    @XmlTransient
    public List<CfgMarcaproducto> getCfgMarcaproductoList() {
        return cfgMarcaproductoList;
    }

    public void setCfgMarcaproductoList(List<CfgMarcaproducto> cfgMarcaproductoList) {
        this.cfgMarcaproductoList = cfgMarcaproductoList;
    }

    @XmlTransient
    public List<CfgProveedor> getCfgProveedorList() {
        return cfgProveedorList;
    }

    public void setCfgProveedorList(List<CfgProveedor> cfgProveedorList) {
        this.cfgProveedorList = cfgProveedorList;
    }

    @XmlTransient
    public List<SegUsuario> getSegUsuarioList() {
        return segUsuarioList;
    }

    public void setSegUsuarioList(List<SegUsuario> segUsuarioList) {
        this.segUsuarioList = segUsuarioList;
    }

    @XmlTransient
    public List<CfgImpuesto> getCfgImpuestoList() {
        return cfgImpuestoList;
    }

    public void setCfgImpuestoList(List<CfgImpuesto> cfgImpuestoList) {
        this.cfgImpuestoList = cfgImpuestoList;
    }

    @XmlTransient
    public List<CfgFormapago> getCfgFormapagoList() {
        return cfgFormapagoList;
    }

    public void setCfgFormapagoList(List<CfgFormapago> cfgFormapagoList) {
        this.cfgFormapagoList = cfgFormapagoList;
    }

    @XmlTransient
    public List<CfgKitproductomaestro> getCfgKitproductomaestroList() {
        return cfgKitproductomaestroList;
    }

    public void setCfgKitproductomaestroList(List<CfgKitproductomaestro> cfgKitproductomaestroList) {
        this.cfgKitproductomaestroList = cfgKitproductomaestroList;
    }

    public CfgMunicipio getCfgMunicipio() {
        return cfgMunicipio;
    }

    public void setCfgMunicipio(CfgMunicipio cfgMunicipio) {
        this.cfgMunicipio = cfgMunicipio;
    }

    public CfgTipodocempresa getCfgTipodocempresaId() {
        return cfgTipodocempresaId;
    }

    public void setCfgTipodocempresaId(CfgTipodocempresa cfgTipodocempresaId) {
        this.cfgTipodocempresaId = cfgTipodocempresaId;
    }

    public CfgTipoempresa getCfgTipoempresaId() {
        return cfgTipoempresaId;
    }

    public void setCfgTipoempresaId(CfgTipoempresa cfgTipoempresaId) {
        this.cfgTipoempresaId = cfgTipoempresaId;
    }

    public SegUsuario getSegusuarioidUsuario() {
        return segusuarioidUsuario;
    }

    public void setSegusuarioidUsuario(SegUsuario segusuarioidUsuario) {
        this.segusuarioidUsuario = segusuarioidUsuario;
    }

    @XmlTransient
    public List<CfgEmpresasede> getCfgEmpresasedeList() {
        return cfgEmpresasedeList;
    }

    public void setCfgEmpresasedeList(List<CfgEmpresasede> cfgEmpresasedeList) {
        this.cfgEmpresasedeList = cfgEmpresasedeList;
    }

    @XmlTransient
    public List<CfgProducto> getCfgProductoList() {
        return cfgProductoList;
    }

    public void setCfgProductoList(List<CfgProducto> cfgProductoList) {
        this.cfgProductoList = cfgProductoList;
    }

    @XmlTransient
    public List<CfgCategoriaproducto> getCfgCategoriaproductoList() {
        return cfgCategoriaproductoList;
    }

    public void setCfgCategoriaproductoList(List<CfgCategoriaproducto> cfgCategoriaproductoList) {
        this.cfgCategoriaproductoList = cfgCategoriaproductoList;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmpresa != null ? idEmpresa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgEmpresa)) {
            return false;
        }
        CfgEmpresa other = (CfgEmpresa) object;
        if ((this.idEmpresa == null && other.idEmpresa != null) || (this.idEmpresa != null && !this.idEmpresa.equals(other.idEmpresa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgEmpresa[ idEmpresa=" + idEmpresa + " ]";
    }
    
}
