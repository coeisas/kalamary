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
@Table(name = "cfg_documento", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgDocumento.findAll", query = "SELECT c FROM CfgDocumento c"),
    @NamedQuery(name = "CfgDocumento.findByIdDoc", query = "SELECT c FROM CfgDocumento c WHERE c.idDoc = :idDoc"),
    @NamedQuery(name = "CfgDocumento.findByNomDoc", query = "SELECT c FROM CfgDocumento c WHERE c.nomDoc = :nomDoc"),
    @NamedQuery(name = "CfgDocumento.findByAbreviatura", query = "SELECT c FROM CfgDocumento c WHERE c.abreviatura = :abreviatura"),
    @NamedQuery(name = "CfgDocumento.findByPrefijoDoc", query = "SELECT c FROM CfgDocumento c WHERE c.prefijoDoc = :prefijoDoc"),
    @NamedQuery(name = "CfgDocumento.findByActivo", query = "SELECT c FROM CfgDocumento c WHERE c.activo = :activo"),
    @NamedQuery(name = "CfgDocumento.findByIniDocumento", query = "SELECT c FROM CfgDocumento c WHERE c.iniDocumento = :iniDocumento"),
    @NamedQuery(name = "CfgDocumento.findByFinDocumento", query = "SELECT c FROM CfgDocumento c WHERE c.finDocumento = :finDocumento"),
    @NamedQuery(name = "CfgDocumento.findByActDocumento", query = "SELECT c FROM CfgDocumento c WHERE c.actDocumento = :actDocumento"),
    @NamedQuery(name = "CfgDocumento.findByResDian", query = "SELECT c FROM CfgDocumento c WHERE c.resDian = :resDian"),
    @NamedQuery(name = "CfgDocumento.findByFecCrea", query = "SELECT c FROM CfgDocumento c WHERE c.fecCrea = :fecCrea"),
    @NamedQuery(name = "CfgDocumento.findByCodDocumento", query = "SELECT c FROM CfgDocumento c WHERE c.codDocumento = :codDocumento")})
public class CfgDocumento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDoc", nullable = false)
    private Integer idDoc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "nomDoc", nullable = false, length = 25)
    private String nomDoc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "abreviatura", nullable = false, length = 3)
    private String abreviatura;
    @Size(max = 5)
    @Column(name = "prefijoDoc", length = 5)
    private String prefijoDoc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo", nullable = false)
    private boolean activo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iniDocumento", nullable = false)
    private int iniDocumento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "finDocumento", nullable = false)
    private int finDocumento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "actDocumento", nullable = false)
    private int actDocumento;
    @Size(max = 200)
    @Column(name = "resDian", length = 200)
    private String resDian;
    @Lob
    @Size(max = 65535)
    @Column(name = "obsDocumento", length = 65535)
    private String obsDocumento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecCrea", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCrea;
    @Column(name = "codDocumento", nullable = false, length = 10)
    private String codDocumento;    
    @JoinColumn(name = "cfg_empresasede_idSede", referencedColumnName = "idSede", nullable = false)
    @ManyToOne(optional = false)
    private CfgEmpresasede cfgempresasedeidSede;
    @JoinColumn(name = "cfg_tipoempresa_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private CfgTipoempresa cfgTipoempresaId;    
    @JoinColumn(name = "seg_usuario_idUsuario", referencedColumnName = "idUsuario", nullable = false)
    @ManyToOne(optional = false)
    private SegUsuario segusuarioidUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgdocumentoidDoc")
    private List<FacDocumentosmaster> facDocumentosmasterList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgdocumentoidDoc")
    private List<FacMovcajadetalle> facMovcajadetalleList;

    public CfgDocumento() {
    }

    public CfgDocumento(Integer idDoc) {
        this.idDoc = idDoc;
    }

    public CfgDocumento(Integer idDoc, String nomDoc, String abreviatura, boolean activo, int iniDocumento, int finDocumento, int actDocumento, Date fecCrea) {
        this.idDoc = idDoc;
        this.nomDoc = nomDoc;
        this.abreviatura = abreviatura;
        this.activo = activo;
        this.iniDocumento = iniDocumento;
        this.finDocumento = finDocumento;
        this.actDocumento = actDocumento;
        this.fecCrea = fecCrea;
    }

    public Integer getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(Integer idDoc) {
        this.idDoc = idDoc;
    }

    public String getNomDoc() {
        return nomDoc;
    }

    public void setNomDoc(String nomDoc) {
        this.nomDoc = nomDoc;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getPrefijoDoc() {
        return prefijoDoc;
    }

    public void setPrefijoDoc(String prefijoDoc) {
        this.prefijoDoc = prefijoDoc;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getIniDocumento() {
        return iniDocumento;
    }

    public void setIniDocumento(int iniDocumento) {
        this.iniDocumento = iniDocumento;
    }

    public int getFinDocumento() {
        return finDocumento;
    }

    public void setFinDocumento(int finDocumento) {
        this.finDocumento = finDocumento;
    }

    public int getActDocumento() {
        return actDocumento;
    }

    public void setActDocumento(int actDocumento) {
        this.actDocumento = actDocumento;
    }

    public String getResDian() {
        return resDian;
    }

    public void setResDian(String resDian) {
        this.resDian = resDian;
    }

    public String getObsDocumento() {
        return obsDocumento;
    }

    public void setObsDocumento(String obsDocumento) {
        this.obsDocumento = obsDocumento;
    }

    public Date getFecCrea() {
        return fecCrea;
    }

    public void setFecCrea(Date fecCrea) {
        this.fecCrea = fecCrea;
    }

    public String getCodDocumento() {
        return codDocumento;
    }

    public void setCodDocumento(String codDocumento) {
        this.codDocumento = codDocumento;
    }
    
    public CfgEmpresasede getCfgempresasedeidSede() {
        return cfgempresasedeidSede;
    }

    public void setCfgempresasedeidSede(CfgEmpresasede cfgempresasedeidSede) {
        this.cfgempresasedeidSede = cfgempresasedeidSede;
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
    public List<FacDocumentosmaster> getFacDocumentosmasterList() {
        return facDocumentosmasterList;
    }

    public void setFacDocumentosmasterList(List<FacDocumentosmaster> facDocumentosmasterList) {
        this.facDocumentosmasterList = facDocumentosmasterList;
    }

    @XmlTransient
    public List<FacMovcajadetalle> getFacMovcajadetalleList() {
        return facMovcajadetalleList;
    }

    public void setFacMovcajadetalleList(List<FacMovcajadetalle> facMovcajadetalleList) {
        this.facMovcajadetalleList = facMovcajadetalleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDoc != null ? idDoc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgDocumento)) {
            return false;
        }
        CfgDocumento other = (CfgDocumento) object;
        if ((this.idDoc == null && other.idDoc != null) || (this.idDoc != null && !this.idDoc.equals(other.idDoc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgDocumento[ idDoc=" + idDoc + " ]";
    }
    
}
