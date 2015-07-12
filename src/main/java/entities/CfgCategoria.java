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
@Table(name = "cfg_categoria", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgCategoria.findAll", query = "SELECT c FROM CfgCategoria c"),
    @NamedQuery(name = "CfgCategoria.findByIdCategoria", query = "SELECT c FROM CfgCategoria c WHERE c.idCategoria = :idCategoria"),
    @NamedQuery(name = "CfgCategoria.findByNomCategoria", query = "SELECT c FROM CfgCategoria c WHERE c.nomCategoria = :nomCategoria"),
    @NamedQuery(name = "CfgCategoria.findByFecCrea", query = "SELECT c FROM CfgCategoria c WHERE c.fecCrea = :fecCrea"),
    @NamedQuery(name = "CfgCategoria.findByUsrCrea", query = "SELECT c FROM CfgCategoria c WHERE c.usrCrea = :usrCrea")})
public class CfgCategoria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCategoria", nullable = false)
    private Integer idCategoria;
    @Size(max = 50)
    @Column(name = "nomCategoria", length = 50)
    private String nomCategoria;
    @Column(name = "fecCrea")
    @Temporal(TemporalType.DATE)
    private Date fecCrea;
    @Column(name = "usrCrea")
    private Integer usrCrea;

    public CfgCategoria() {
    }

    public CfgCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNomCategoria() {
        return nomCategoria;
    }

    public void setNomCategoria(String nomCategoria) {
        this.nomCategoria = nomCategoria;
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
        hash += (idCategoria != null ? idCategoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgCategoria)) {
            return false;
        }
        CfgCategoria other = (CfgCategoria) object;
        if ((this.idCategoria == null && other.idCategoria != null) || (this.idCategoria != null && !this.idCategoria.equals(other.idCategoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgCategoria[ idCategoria=" + idCategoria + " ]";
    }
    
}
