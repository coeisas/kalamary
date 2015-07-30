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
import javax.persistence.Transient;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "seg_usuario", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SegUsuario.findAll", query = "SELECT s FROM SegUsuario s"),
    @NamedQuery(name = "SegUsuario.findByIdUsuario", query = "SELECT s FROM SegUsuario s WHERE s.idUsuario = :idUsuario"),
    @NamedQuery(name = "SegUsuario.findByUsuario", query = "SELECT s FROM SegUsuario s WHERE s.usuario = :usuario"),
    @NamedQuery(name = "SegUsuario.findByNumDoc", query = "SELECT s FROM SegUsuario s WHERE s.numDoc = :numDoc"),
    @NamedQuery(name = "SegUsuario.findByNom1Usuario", query = "SELECT s FROM SegUsuario s WHERE s.nom1Usuario = :nom1Usuario"),
    @NamedQuery(name = "SegUsuario.findByNom2Usuario", query = "SELECT s FROM SegUsuario s WHERE s.nom2Usuario = :nom2Usuario"),
    @NamedQuery(name = "SegUsuario.findByApellido1", query = "SELECT s FROM SegUsuario s WHERE s.apellido1 = :apellido1"),
    @NamedQuery(name = "SegUsuario.findByApellido2", query = "SELECT s FROM SegUsuario s WHERE s.apellido2 = :apellido2"),
    @NamedQuery(name = "SegUsuario.findByComisionVenta", query = "SELECT s FROM SegUsuario s WHERE s.comisionVenta = :comisionVenta"),
    @NamedQuery(name = "SegUsuario.findByDirUsuario", query = "SELECT s FROM SegUsuario s WHERE s.dirUsuario = :dirUsuario"),
    @NamedQuery(name = "SegUsuario.findByTel1", query = "SELECT s FROM SegUsuario s WHERE s.tel1 = :tel1"),
    @NamedQuery(name = "SegUsuario.findByMail", query = "SELECT s FROM SegUsuario s WHERE s.mail = :mail"),
    @NamedQuery(name = "SegUsuario.findByActivo", query = "SELECT s FROM SegUsuario s WHERE s.activo = :activo"),
    @NamedQuery(name = "SegUsuario.findByFecCrea", query = "SELECT s FROM SegUsuario s WHERE s.fecCrea = :fecCrea"),
    @NamedQuery(name = "SegUsuario.findByUsrCrea", query = "SELECT s FROM SegUsuario s WHERE s.usrCrea = :usrCrea"),
    @NamedQuery(name = "SegUsuario.findByFecNacimiento", query = "SELECT s FROM SegUsuario s WHERE s.fecNacimiento = :fecNacimiento")})
public class SegUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idUsuario", nullable = false)
    private Integer idUsuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "usuario", nullable = false, length = 255)
    private String usuario;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "password", nullable = false, length = 65535)
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "numDoc", nullable = false, length = 20)
    private String numDoc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nom1Usuario", nullable = false, length = 50)
    private String nom1Usuario;
    @Size(max = 50)
    @Column(name = "nom2Usuario", length = 50)
    private String nom2Usuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "apellido1", nullable = false, length = 50)
    private String apellido1;
    @Size(max = 50)
    @Column(name = "apellido2", length = 50)
    private String apellido2;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "comisionVenta", precision = 12)
    private Float comisionVenta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "dirUsuario", nullable = false, length = 150)
    private String dirUsuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "tel1", nullable = false, length = 10)
    private String tel1;
    @Size(max = 30)
    @Column(name = "mail", length = 30)
    private String mail;
    @Lob
    @Column(name = "foto")
    private byte[] foto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo", nullable = false)
    private boolean activo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecCrea", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecCrea;
    @Column(name = "usrCrea")
    private Integer usrCrea;
    @Column(name = "fecNacimiento")
    @Temporal(TemporalType.DATE)
    private Date fecNacimiento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "segusuarioidUsuario")
    private List<CfgCliente> cfgClienteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "segusuarioidUsuario")
    private List<CfgProveedor> cfgProveedorList;
    @JoinColumn(name = "cfg_empresasede_idSede", referencedColumnName = "idSede")
    @ManyToOne
    private CfgEmpresasede cfgempresasedeidSede;
    @JoinColumn(name = "cfg_rol_idrol", referencedColumnName = "idrol", nullable = false)
    @ManyToOne(optional = false)
    private CfgRol cfgRolIdrol;
    @JoinColumn(name = "cfg_tipoidentificacion_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private CfgTipoidentificacion cfgTipoidentificacionId;
    @JoinColumn(name = "fac_caja_idCaja", referencedColumnName = "idCaja")
    @ManyToOne
    private FacCaja faccajaidCaja;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "segusuarioidUsuario")
    private List<CfgImpuesto> cfgImpuestoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "segusuarioidUsuario")
    private List<FacMovcaja> facMovcajaList;//usuario que abren caja
    @OneToMany(mappedBy = "segusuarioidUsuario1")
    private List<FacMovcaja> facMovcajaList1;//usuario que cierran caja    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "segusuarioidUsuario")
    private List<CfgDocumento> cfgDocumentoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "segusuarioidUsuario")
    private List<CfgKitproductomaestro> cfgKitproductomaestroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "segusuarioidUsuario")
    private List<CfgEmpresa> cfgEmpresaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "segusuarioidUsuario")
    private List<CfgProducto> cfgProductoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "segusuarioidUsuario")
    private List<FacDocumentosmaster> facDocumentosmasterList;
    @Transient
    private HttpSession session;

    public SegUsuario() {
    }

    public SegUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public SegUsuario(Integer idUsuario, String usuario, String password, String numDoc, String nom1Usuario, String apellido1, String dirUsuario, String tel1, boolean activo, Date fecCrea) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.password = password;
        this.numDoc = numDoc;
        this.nom1Usuario = nom1Usuario;
        this.apellido1 = apellido1;
        this.dirUsuario = dirUsuario;
        this.tel1 = tel1;
        this.activo = activo;
        this.fecCrea = fecCrea;
    }

    public String nombreCompleto() {
        String strNombre = "";
        if (nom1Usuario != null) {
            strNombre = strNombre + nom1Usuario + " ";
        }
        if (nom2Usuario != null) {
            strNombre = strNombre + nom2Usuario + " ";
        }
        if (apellido1 != null) {
            strNombre = strNombre + apellido1 + " ";
        }
        if (apellido2 != null) {
            strNombre = strNombre + apellido2;
        }
        return strNombre;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getNom1Usuario() {
        return nom1Usuario;
    }

    public void setNom1Usuario(String nom1Usuario) {
        this.nom1Usuario = nom1Usuario;
    }

    public String getNom2Usuario() {
        return nom2Usuario;
    }

    public void setNom2Usuario(String nom2Usuario) {
        this.nom2Usuario = nom2Usuario;
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

    public Float getComisionVenta() {
        return comisionVenta;
    }

    public void setComisionVenta(Float comisionVenta) {
        this.comisionVenta = comisionVenta;
    }

    public String getDirUsuario() {
        return dirUsuario;
    }

    public void setDirUsuario(String dirUsuario) {
        this.dirUsuario = dirUsuario;
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

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
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

    public Integer getUsrCrea() {
        return usrCrea;
    }

    public void setUsrCrea(Integer usrCrea) {
        this.usrCrea = usrCrea;
    }

    public Date getFecNacimiento() {
        return fecNacimiento;
    }

    public void setFecNacimiento(Date fecNacimiento) {
        this.fecNacimiento = fecNacimiento;
    }

    @XmlTransient
    public List<CfgCliente> getCfgClienteList() {
        return cfgClienteList;
    }

    public void setCfgClienteList(List<CfgCliente> cfgClienteList) {
        this.cfgClienteList = cfgClienteList;
    }

    @XmlTransient
    public List<CfgProveedor> getCfgProveedorList() {
        return cfgProveedorList;
    }

    public void setCfgProveedorList(List<CfgProveedor> cfgProveedorList) {
        this.cfgProveedorList = cfgProveedorList;
    }

    public CfgEmpresasede getCfgempresasedeidSede() {
        return cfgempresasedeidSede;
    }

    public void setCfgempresasedeidSede(CfgEmpresasede cfgempresasedeidSede) {
        this.cfgempresasedeidSede = cfgempresasedeidSede;
    }

    public CfgRol getCfgRolIdrol() {
        return cfgRolIdrol;
    }

    public void setCfgRolIdrol(CfgRol cfgRolIdrol) {
        this.cfgRolIdrol = cfgRolIdrol;
    }

    public CfgTipoidentificacion getCfgTipoidentificacionId() {
        return cfgTipoidentificacionId;
    }

    public void setCfgTipoidentificacionId(CfgTipoidentificacion cfgTipoidentificacionId) {
        this.cfgTipoidentificacionId = cfgTipoidentificacionId;
    }

    public FacCaja getFaccajaidCaja() {
        return faccajaidCaja;
    }

    public void setFaccajaidCaja(FacCaja faccajaidCaja) {
        this.faccajaidCaja = faccajaidCaja;
    }

    @XmlTransient
    public List<CfgImpuesto> getCfgImpuestoList() {
        return cfgImpuestoList;
    }

    public void setCfgImpuestoList(List<CfgImpuesto> cfgImpuestoList) {
        this.cfgImpuestoList = cfgImpuestoList;
    }

    @XmlTransient
    public List<FacMovcaja> getFacMovcajaList() {
        return facMovcajaList;
    }

    public void setFacMovcajaList(List<FacMovcaja> facMovcajaList) {
        this.facMovcajaList = facMovcajaList;
    }

    @XmlTransient
    public List<FacMovcaja> getFacMovcajaList1() {
        return facMovcajaList1;
    }

    public void setFacMovcajaList1(List<FacMovcaja> facMovcajaList1) {
        this.facMovcajaList1 = facMovcajaList1;
    }

    @XmlTransient
    public List<CfgDocumento> getCfgDocumentoList() {
        return cfgDocumentoList;
    }

    public void setCfgDocumentoList(List<CfgDocumento> cfgDocumentoList) {
        this.cfgDocumentoList = cfgDocumentoList;
    }

    @XmlTransient
    public List<CfgKitproductomaestro> getCfgKitproductomaestroList() {
        return cfgKitproductomaestroList;
    }

    public void setCfgKitproductomaestroList(List<CfgKitproductomaestro> cfgKitproductomaestroList) {
        this.cfgKitproductomaestroList = cfgKitproductomaestroList;
    }

    @XmlTransient
    public List<CfgEmpresa> getCfgEmpresaList() {
        return cfgEmpresaList;
    }

    public void setCfgEmpresaList(List<CfgEmpresa> cfgEmpresaList) {
        this.cfgEmpresaList = cfgEmpresaList;
    }

    @XmlTransient
    public List<CfgProducto> getCfgProductoList() {
        return cfgProductoList;
    }

    public void setCfgProductoList(List<CfgProducto> cfgProductoList) {
        this.cfgProductoList = cfgProductoList;
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
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SegUsuario)) {
            return false;
        }
        SegUsuario other = (SegUsuario) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.SegUsuario[ idUsuario=" + idUsuario + " ]";
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

}
