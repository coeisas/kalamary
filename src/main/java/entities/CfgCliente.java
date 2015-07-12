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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cfg_cliente", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgCliente.findAll", query = "SELECT c FROM CfgCliente c"),
    @NamedQuery(name = "CfgCliente.findByIdCliente", query = "SELECT c FROM CfgCliente c WHERE c.idCliente = :idCliente"),
    @NamedQuery(name = "CfgCliente.findByNumDoc", query = "SELECT c FROM CfgCliente c WHERE c.numDoc = :numDoc"),
    @NamedQuery(name = "CfgCliente.findByNom1Cliente", query = "SELECT c FROM CfgCliente c WHERE c.nom1Cliente = :nom1Cliente"),
    @NamedQuery(name = "CfgCliente.findByNom2Cliente", query = "SELECT c FROM CfgCliente c WHERE c.nom2Cliente = :nom2Cliente"),
    @NamedQuery(name = "CfgCliente.findByApellido1", query = "SELECT c FROM CfgCliente c WHERE c.apellido1 = :apellido1"),
    @NamedQuery(name = "CfgCliente.findByApellido2", query = "SELECT c FROM CfgCliente c WHERE c.apellido2 = :apellido2"),
    @NamedQuery(name = "CfgCliente.findByDirCliente", query = "SELECT c FROM CfgCliente c WHERE c.dirCliente = :dirCliente"),
    @NamedQuery(name = "CfgCliente.findByTel1", query = "SELECT c FROM CfgCliente c WHERE c.tel1 = :tel1"),
    @NamedQuery(name = "CfgCliente.findByMail", query = "SELECT c FROM CfgCliente c WHERE c.mail = :mail"),
    @NamedQuery(name = "CfgCliente.findByFecNacimiento", query = "SELECT c FROM CfgCliente c WHERE c.fecNacimiento = :fecNacimiento"),
    @NamedQuery(name = "CfgCliente.findByCupoCredito", query = "SELECT c FROM CfgCliente c WHERE c.cupoCredito = :cupoCredito"),
    @NamedQuery(name = "CfgCliente.findByTarjetaMembresia", query = "SELECT c FROM CfgCliente c WHERE c.tarjetaMembresia = :tarjetaMembresia"),
    @NamedQuery(name = "CfgCliente.findByFecCrea", query = "SELECT c FROM CfgCliente c WHERE c.fecCrea = :fecCrea"),
    @NamedQuery(name = "CfgCliente.findByCodigoCliente", query = "SELECT c FROM CfgCliente c WHERE c.codigoCliente = :codigoCliente")})
public class CfgCliente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCliente", nullable = false)
    private Integer idCliente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "numDoc", nullable = false, length = 20)
    private String numDoc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nom1Cliente", nullable = false, length = 50)
    private String nom1Cliente;
    @Size(max = 50)
    @Column(name = "nom2Cliente", length = 50)
    private String nom2Cliente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "apellido1", nullable = false, length = 50)
    private String apellido1;
    @Size(max = 50)
    @Column(name = "apellido2", length = 50)
    private String apellido2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "dirCliente", nullable = false, length = 150)
    private String dirCliente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "tel1", nullable = false, length = 10)
    private String tel1;
    @Size(max = 30)
    @Column(name = "mail", length = 30)
    private String mail;
    @Column(name = "fecNacimiento")
    @Temporal(TemporalType.DATE)
    private Date fecNacimiento;
    @Lob
    @Column(name = "foto")
    private byte[] foto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cupoCredito", precision = 12)
    private Float cupoCredito;
    @Size(max = 20)
    @Column(name = "tarjetaMembresia", length = 20)
    private String tarjetaMembresia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecCrea", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecCrea;
    @Basic(optional = false)
    @Column(name = "codigoCliente", nullable = false, length = 10)
    private String codigoCliente;    
    @JoinColumn(name = "cfg_empresa_idEmpresa", referencedColumnName = "idEmpresa", nullable = false)
    @ManyToOne(optional = false)
    private CfgEmpresa cfgempresaidEmpresa;    
    @JoinColumns({
        @JoinColumn(name = "cfg_municipio_idMunicipio", referencedColumnName = "idMunicipio", nullable = false),
        @JoinColumn(name = "cfg_municipio_cfg_departamento_idDepartamento", referencedColumnName = "cfg_departamento_idDepartamento", nullable = false)})
    @ManyToOne(optional = false)
    private CfgMunicipio cfgMunicipio;
    @JoinColumn(name = "cfg_tipoempresa_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private CfgTipoempresa cfgTipoempresaId;    
    @JoinColumn(name = "cfg_tipoidentificacion_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private CfgTipoidentificacion cfgTipoidentificacionId;
    @JoinColumn(name = "seg_usuario_idUsuario", referencedColumnName = "idUsuario", nullable = false)
    @ManyToOne(optional = false)
    private SegUsuario segusuarioidUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgclienteidCliente")
    private List<FacDocumentosmaster> facDocumentosmasterList;

    public CfgCliente() {
    }

    public CfgCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public CfgCliente(Integer idCliente, String numDoc, String nom1Cliente, String apellido1, String dirCliente, String tel1, Date fecCrea) {
        this.idCliente = idCliente;
        this.numDoc = numDoc;
        this.nom1Cliente = nom1Cliente;
        this.apellido1 = apellido1;
        this.dirCliente = dirCliente;
        this.tel1 = tel1;
        this.fecCrea = fecCrea;
    }
    
    public String nombreCompleto() {
        String strNombre = "";
        if (nom1Cliente != null) {
            strNombre = strNombre + nom1Cliente + " ";
        }
        if (nom2Cliente != null) {
            strNombre = strNombre + nom2Cliente + " ";
        }
        if (apellido1 != null) {
            strNombre = strNombre + apellido1 + " ";
        }
        if (apellido2 != null) {
            strNombre = strNombre + apellido2;
        }
        return strNombre;
    }    

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getNom1Cliente() {
        return nom1Cliente;
    }

    public void setNom1Cliente(String nom1Cliente) {
        this.nom1Cliente = nom1Cliente;
    }

    public String getNom2Cliente() {
        return nom2Cliente;
    }

    public void setNom2Cliente(String nom2Cliente) {
        this.nom2Cliente = nom2Cliente;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getDirCliente() {
        return dirCliente;
    }

    public void setDirCliente(String dirCliente) {
        this.dirCliente = dirCliente;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Date getFecNacimiento() {
        return fecNacimiento;
    }

    public void setFecNacimiento(Date fecNacimiento) {
        this.fecNacimiento = fecNacimiento;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Float getCupoCredito() {
        return cupoCredito;
    }

    public void setCupoCredito(Float cupoCredito) {
        this.cupoCredito = cupoCredito;
    }

    public String getTarjetaMembresia() {
        return tarjetaMembresia;
    }

    public void setTarjetaMembresia(String tarjetaMembresia) {
        this.tarjetaMembresia = tarjetaMembresia;
    }

    public Date getFecCrea() {
        return fecCrea;
    }

    public void setFecCrea(Date fecCrea) {
        this.fecCrea = fecCrea;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
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

    public CfgTipoempresa getCfgTipoempresaId() {
        return cfgTipoempresaId;
    }

    public void setCfgTipoempresaId(CfgTipoempresa cfgTipoempresaId) {
        this.cfgTipoempresaId = cfgTipoempresaId;
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
        hash += (idCliente != null ? idCliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgCliente)) {
            return false;
        }
        CfgCliente other = (CfgCliente) object;
        if ((this.idCliente == null && other.idCliente != null) || (this.idCliente != null && !this.idCliente.equals(other.idCliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgCliente[ idCliente=" + idCliente + " ]";
    }
    
}
