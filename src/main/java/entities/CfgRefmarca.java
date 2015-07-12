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
@Table(name = "cfg_refmarca", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgRefmarca.findAll", query = "SELECT c FROM CfgRefmarca c"),
    @NamedQuery(name = "CfgRefmarca.findByIdReferencia", query = "SELECT c FROM CfgRefmarca c WHERE c.idReferencia = :idReferencia"),
    @NamedQuery(name = "CfgRefmarca.findByIdMarca", query = "SELECT c FROM CfgRefmarca c WHERE c.idMarca = :idMarca"),
    @NamedQuery(name = "CfgRefmarca.findByNomMarca", query = "SELECT c FROM CfgRefmarca c WHERE c.nomMarca = :nomMarca"),
    @NamedQuery(name = "CfgRefmarca.findByFecCrea", query = "SELECT c FROM CfgRefmarca c WHERE c.fecCrea = :fecCrea"),
    @NamedQuery(name = "CfgRefmarca.findByUsrCrea", query = "SELECT c FROM CfgRefmarca c WHERE c.usrCrea = :usrCrea")})
public class CfgRefmarca implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idReferencia", nullable = false)
    private Integer idReferencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idMarca", nullable = false)
    private int idMarca;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nomMarca", nullable = false, length = 50)
    private String nomMarca;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecCrea", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecCrea;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usrCrea", nullable = false)
    private int usrCrea;

    public CfgRefmarca() {
    }

    public CfgRefmarca(Integer idReferencia) {
        this.idReferencia = idReferencia;
    }

    public CfgRefmarca(Integer idReferencia, int idMarca, String nomMarca, Date fecCrea, int usrCrea) {
        this.idReferencia = idReferencia;
        this.idMarca = idMarca;
        this.nomMarca = nomMarca;
        this.fecCrea = fecCrea;
        this.usrCrea = usrCrea;
    }

    public Integer getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(Integer idReferencia) {
        this.idReferencia = idReferencia;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getNomMarca() {
        return nomMarca;
    }

    public void setNomMarca(String nomMarca) {
        this.nomMarca = nomMarca;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReferencia != null ? idReferencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgRefmarca)) {
            return false;
        }
        CfgRefmarca other = (CfgRefmarca) object;
        if ((this.idReferencia == null && other.idReferencia != null) || (this.idReferencia != null && !this.idReferencia.equals(other.idReferencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgRefmarca[ idReferencia=" + idReferencia + " ]";
    }
    
}
