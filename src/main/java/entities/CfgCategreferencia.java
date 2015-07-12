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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cfg_categreferencia", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgCategreferencia.findAll", query = "SELECT c FROM CfgCategreferencia c"),
    @NamedQuery(name = "CfgCategreferencia.findByIdReferencia", query = "SELECT c FROM CfgCategreferencia c WHERE c.idReferencia = :idReferencia"),
    @NamedQuery(name = "CfgCategreferencia.findByIdCategoria", query = "SELECT c FROM CfgCategreferencia c WHERE c.idCategoria = :idCategoria"),
    @NamedQuery(name = "CfgCategreferencia.findByNomReferencia", query = "SELECT c FROM CfgCategreferencia c WHERE c.nomReferencia = :nomReferencia"),
    @NamedQuery(name = "CfgCategreferencia.findByFecCrea", query = "SELECT c FROM CfgCategreferencia c WHERE c.fecCrea = :fecCrea"),
    @NamedQuery(name = "CfgCategreferencia.findByUsrCrea", query = "SELECT c FROM CfgCategreferencia c WHERE c.usrCrea = :usrCrea")})
public class CfgCategreferencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idReferencia", nullable = false)
    private Integer idReferencia;
    @Column(name = "idCategoria")
    private Integer idCategoria;
    @Size(max = 50)
    @Column(name = "nomReferencia", length = 50)
    private String nomReferencia;
    @Column(name = "fecCrea")
    @Temporal(TemporalType.DATE)
    private Date fecCrea;
    @Column(name = "usrCrea")
    private Integer usrCrea;

    public CfgCategreferencia() {
    }

    public CfgCategreferencia(Integer idReferencia) {
        this.idReferencia = idReferencia;
    }

    public Integer getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(Integer idReferencia) {
        this.idReferencia = idReferencia;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNomReferencia() {
        return nomReferencia;
    }

    public void setNomReferencia(String nomReferencia) {
        this.nomReferencia = nomReferencia;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReferencia != null ? idReferencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgCategreferencia)) {
            return false;
        }
        CfgCategreferencia other = (CfgCategreferencia) object;
        if ((this.idReferencia == null && other.idReferencia != null) || (this.idReferencia != null && !this.idReferencia.equals(other.idReferencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgCategreferencia[ idReferencia=" + idReferencia + " ]";
    }
    
}
