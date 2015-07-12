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
@Table(name = "cfg_catmarcareferencia", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgCatmarcareferencia.findAll", query = "SELECT c FROM CfgCatmarcareferencia c"),
    @NamedQuery(name = "CfgCatmarcareferencia.findByIdMarca", query = "SELECT c FROM CfgCatmarcareferencia c WHERE c.idMarca = :idMarca"),
    @NamedQuery(name = "CfgCatmarcareferencia.findByIdReferencia", query = "SELECT c FROM CfgCatmarcareferencia c WHERE c.idReferencia = :idReferencia"),
    @NamedQuery(name = "CfgCatmarcareferencia.findByNomMarca", query = "SELECT c FROM CfgCatmarcareferencia c WHERE c.nomMarca = :nomMarca"),
    @NamedQuery(name = "CfgCatmarcareferencia.findByFecCrea", query = "SELECT c FROM CfgCatmarcareferencia c WHERE c.fecCrea = :fecCrea"),
    @NamedQuery(name = "CfgCatmarcareferencia.findByUsrCrea", query = "SELECT c FROM CfgCatmarcareferencia c WHERE c.usrCrea = :usrCrea")})
public class CfgCatmarcareferencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idMarca", nullable = false)
    private Integer idMarca;
    @Column(name = "idReferencia")
    private Integer idReferencia;
    @Size(max = 50)
    @Column(name = "nomMarca", length = 50)
    private String nomMarca;
    @Column(name = "fecCrea")
    @Temporal(TemporalType.DATE)
    private Date fecCrea;
    @Column(name = "usrCrea")
    private Integer usrCrea;

    public CfgCatmarcareferencia() {
    }

    public CfgCatmarcareferencia(Integer idMarca) {
        this.idMarca = idMarca;
    }

    public Integer getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(Integer idMarca) {
        this.idMarca = idMarca;
    }

    public Integer getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(Integer idReferencia) {
        this.idReferencia = idReferencia;
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

    public Integer getUsrCrea() {
        return usrCrea;
    }

    public void setUsrCrea(Integer usrCrea) {
        this.usrCrea = usrCrea;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMarca != null ? idMarca.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgCatmarcareferencia)) {
            return false;
        }
        CfgCatmarcareferencia other = (CfgCatmarcareferencia) object;
        if ((this.idMarca == null && other.idMarca != null) || (this.idMarca != null && !this.idMarca.equals(other.idMarca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgCatmarcareferencia[ idMarca=" + idMarca + " ]";
    }
    
}
