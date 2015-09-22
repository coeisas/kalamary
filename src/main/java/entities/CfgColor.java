/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "cfg_color", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgColor.findAll", query = "SELECT c FROM CfgColor c"),
    @NamedQuery(name = "CfgColor.findByIdColor", query = "SELECT c FROM CfgColor c WHERE c.idColor = :idColor"),
    @NamedQuery(name = "CfgColor.findByColor", query = "SELECT c FROM CfgColor c WHERE c.color = :color")})
public class CfgColor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_color", nullable = false)
    private Integer idColor;
    @Basic(optional = false)
    @Column(name = "color", nullable = false, length = 45)
    private String color;
    @OneToMany(mappedBy = "cfgColorIdColor")
    private List<CfgProducto> cfgProductoList;

    public CfgColor() {
    }

    public CfgColor(Integer idColor) {
        this.idColor = idColor;
    }

    public CfgColor(Integer idColor, String color) {
        this.idColor = idColor;
        this.color = color;
    }

    public Integer getIdColor() {
        return idColor;
    }

    public void setIdColor(Integer idColor) {
        this.idColor = idColor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @XmlTransient
    public List<CfgProducto> getCfgProductoList() {
        return cfgProductoList;
    }

    public void setCfgProductoList(List<CfgProducto> cfgProductoList) {
        this.cfgProductoList = cfgProductoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idColor != null ? idColor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgColor)) {
            return false;
        }
        CfgColor other = (CfgColor) object;
        if ((this.idColor == null && other.idColor != null) || (this.idColor != null && !this.idColor.equals(other.idColor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgColor[ idColor=" + idColor + " ]";
    }
    
}
