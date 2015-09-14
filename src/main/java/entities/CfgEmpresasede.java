/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cfg_empresasede", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgEmpresasede.findAll", query = "SELECT c FROM CfgEmpresasede c"),
    @NamedQuery(name = "CfgEmpresasede.findByIdSede", query = "SELECT c FROM CfgEmpresasede c WHERE c.idSede = :idSede"),
    @NamedQuery(name = "CfgEmpresasede.findByCodSede", query = "SELECT c FROM CfgEmpresasede c WHERE c.codSede = :codSede"),
    @NamedQuery(name = "CfgEmpresasede.findByNomSede", query = "SELECT c FROM CfgEmpresasede c WHERE c.nomSede = :nomSede"),
    @NamedQuery(name = "CfgEmpresasede.findByActivo", query = "SELECT c FROM CfgEmpresasede c WHERE c.activo = :activo"),
    @NamedQuery(name = "CfgEmpresasede.findByNumDocumento", query = "SELECT c FROM CfgEmpresasede c WHERE c.numDocumento = :numDocumento"),
    @NamedQuery(name = "CfgEmpresasede.findByTel1", query = "SELECT c FROM CfgEmpresasede c WHERE c.tel1 = :tel1"),
    @NamedQuery(name = "CfgEmpresasede.findByTel2", query = "SELECT c FROM CfgEmpresasede c WHERE c.tel2 = :tel2")})
public class CfgEmpresasede implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idSede", nullable = false)
    private Integer idSede;
    @Basic(optional = false)
    @Column(name = "cod_Sede", nullable = false, length = 5)
    private String codSede;
    @Basic(optional = false)
    @Column(name = "nomSede", nullable = false, length = 100)
    private String nomSede;
    @Column(name = "activo")
    private Boolean activo;
    @Lob
    @Column(name = "logo")
    private byte[] logo;
    @Column(name = "num_documento", length = 20)
    private String numDocumento;
    @Lob
    @Column(name = "direccion", length = 65535)
    private String direccion;
    @Lob
    @Column(name = "website", length = 65535)
    private String website;
    @Column(name = "tel1", length = 30)
    private String tel1;
    @Column(name = "tel2", length = 30)
    private String tel2;
    @Lob
    @Column(name = "mail", length = 65535)
    private String mail;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgempresasedeidSede")
    private List<InvMovimiento> invMovimientoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgEmpresasede")
    private List<InvConsolidado> invConsolidadoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgempresasedeidSede")
    private List<FacCaja> facCajaList;
    @OneToMany(mappedBy = "cfgempresasedeidSede")
    private List<SegUsuario> segUsuarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgempresasedeidSede")
    private List<CfgImpuesto> cfgImpuestoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgempresasedeidSede")
    private List<CfgDocumento> cfgDocumentoList;
    @JoinColumn(name = "cfg_empresa_idEmpresa", referencedColumnName = "idEmpresa", nullable = false)
    @ManyToOne(optional = false)
    private CfgEmpresa cfgempresaidEmpresa;
    @JoinColumns({
        @JoinColumn(name = "cfg_municipio_idMunicipio", referencedColumnName = "idMunicipio", nullable = false),
        @JoinColumn(name = "cfg_municipio_cfg_departamento_idDepartamento", referencedColumnName = "cfg_departamento_idDepartamento", nullable = false)})
    @ManyToOne(optional = false)
    private CfgMunicipio cfgMunicipio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgempresasedeidSede")
    private List<FacDocumentosmaster> facDocumentosmasterList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgEmpresasede")
    private List<ConsolidadoMovcaja> consolidadoMovcajaList;

    public CfgEmpresasede() {
    }

    public CfgEmpresasede(Integer idSede) {
        this.idSede = idSede;
    }

    public CfgEmpresasede(Integer idSede, String codSede, String nomSede) {
        this.idSede = idSede;
        this.codSede = codSede;
        this.nomSede = nomSede;
    }

    public Integer getIdSede() {
        return idSede;
    }

    public void setIdSede(Integer idSede) {
        this.idSede = idSede;
    }

    public String getCodSede() {
        return codSede;
    }

    public void setCodSede(String codSede) {
        this.codSede = codSede;
    }

    public String getNomSede() {
        return nomSede;
    }

    public void setNomSede(String nomSede) {
        this.nomSede = nomSede;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @XmlTransient
    public List<InvMovimiento> getInvMovimientoList() {
        return invMovimientoList;
    }

    public void setInvMovimientoList(List<InvMovimiento> invMovimientoList) {
        this.invMovimientoList = invMovimientoList;
    }

    @XmlTransient
    public List<InvConsolidado> getInvConsolidadoList() {
        return invConsolidadoList;
    }

    public void setInvConsolidadoList(List<InvConsolidado> invConsolidadoList) {
        this.invConsolidadoList = invConsolidadoList;
    }

    @XmlTransient
    public List<FacCaja> getFacCajaList() {
        return facCajaList;
    }

    public void setFacCajaList(List<FacCaja> facCajaList) {
        this.facCajaList = facCajaList;
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
    public List<CfgDocumento> getCfgDocumentoList() {
        return cfgDocumentoList;
    }

    public void setCfgDocumentoList(List<CfgDocumento> cfgDocumentoList) {
        this.cfgDocumentoList = cfgDocumentoList;
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

    @XmlTransient
    public List<FacDocumentosmaster> getFacDocumentosmasterList() {
        return facDocumentosmasterList;
    }

    public void setFacDocumentosmasterList(List<FacDocumentosmaster> facDocumentosmasterList) {
        this.facDocumentosmasterList = facDocumentosmasterList;
    }

    @XmlTransient
    public List<ConsolidadoMovcaja> getConsolidadoMovcajaList() {
        return consolidadoMovcajaList;
    }

    public void setConsolidadoMovcajaList(List<ConsolidadoMovcaja> consolidadoMovcajaList) {
        this.consolidadoMovcajaList = consolidadoMovcajaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSede != null ? idSede.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgEmpresasede)) {
            return false;
        }
        CfgEmpresasede other = (CfgEmpresasede) object;
        if ((this.idSede == null && other.idSede != null) || (this.idSede != null && !this.idSede.equals(other.idSede))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgEmpresasede[ idSede=" + idSede + " ]";
    }

}
