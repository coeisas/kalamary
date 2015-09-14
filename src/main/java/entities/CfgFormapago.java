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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cfg_formapago", catalog = "kalamarypos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgFormapago.findAll", query = "SELECT c FROM CfgFormapago c"),
    @NamedQuery(name = "CfgFormapago.findByIdFormaPago", query = "SELECT c FROM CfgFormapago c WHERE c.idFormaPago = :idFormaPago"),
    @NamedQuery(name = "CfgFormapago.findByNomFormaPago", query = "SELECT c FROM CfgFormapago c WHERE c.nomFormaPago = :nomFormaPago"),
    @NamedQuery(name = "CfgFormapago.findByAbreviatura", query = "SELECT c FROM CfgFormapago c WHERE c.abreviatura = :abreviatura"),
    @NamedQuery(name = "CfgFormapago.findByAplicaA", query = "SELECT c FROM CfgFormapago c WHERE c.aplicaA = :aplicaA"),
    @NamedQuery(name = "CfgFormapago.findByFecCrea", query = "SELECT c FROM CfgFormapago c WHERE c.fecCrea = :fecCrea"),
    @NamedQuery(name = "CfgFormapago.findByUsrCrea", query = "SELECT c FROM CfgFormapago c WHERE c.usrCrea = :usrCrea"),
    @NamedQuery(name = "CfgFormapago.findByCodFormaPago", query = "SELECT c FROM CfgFormapago c WHERE c.codFormaPago = :codFormaPago")})
public class CfgFormapago implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idFormaPago", nullable = false)
    private Integer idFormaPago;
    @Basic(optional = false)
    @Column(name = "nomFormaPago", nullable = false, length = 50)
    private String nomFormaPago;
    @Basic(optional = false)
    @Column(name = "Abreviatura", nullable = false, length = 3)
    private String abreviatura;
    @Basic(optional = false)
    @Column(name = "aplicaA", nullable = false, length = 1)
    private String aplicaA;
    @Basic(optional = false)
    @Column(name = "fecCrea", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecCrea;
    @Basic(optional = false)
    @Column(name = "usrCrea", nullable = false)
    private int usrCrea;
    @Basic(optional = false)
    @Column(name = "codFormaPago", nullable = false, length = 5)
    private String codFormaPago;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgFormapago")
    private List<FacDocuementopago> facDocuementopagoList;
    @JoinColumn(name = "cfg_empresa_idEmpresa", referencedColumnName = "idEmpresa", nullable = false)
    @ManyToOne(optional = false)
    private CfgEmpresa cfgempresaidEmpresa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgFormapago")
    private List<ConsolidadoMovcaja> consolidadoMovcajaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgFormapago")
    private List<FacMovcajadetalle> facMovcajadetalleList;
    @Transient
    private float subtotal;//campo utilizado en factura, para guardar el monto a pagar por cada forma de pago

    public CfgFormapago() {
    }

    public CfgFormapago(Integer idFormaPago) {
        this.idFormaPago = idFormaPago;
    }

    public CfgFormapago(Integer idFormaPago, String nomFormaPago, String abreviatura, String aplicaA, Date fecCrea, int usrCrea, String codFormaPago) {
        this.idFormaPago = idFormaPago;
        this.nomFormaPago = nomFormaPago;
        this.abreviatura = abreviatura;
        this.aplicaA = aplicaA;
        this.fecCrea = fecCrea;
        this.usrCrea = usrCrea;
        this.codFormaPago = codFormaPago;
    }

    public Integer getIdFormaPago() {
        return idFormaPago;
    }

    public void setIdFormaPago(Integer idFormaPago) {
        this.idFormaPago = idFormaPago;
    }

    public String getNomFormaPago() {
        return nomFormaPago;
    }

    public void setNomFormaPago(String nomFormaPago) {
        this.nomFormaPago = nomFormaPago;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getAplicaA() {
        return aplicaA;
    }

    public void setAplicaA(String aplicaA) {
        this.aplicaA = aplicaA;
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

    public String getCodFormaPago() {
        return codFormaPago;
    }

    public void setCodFormaPago(String codFormaPago) {
        this.codFormaPago = codFormaPago;
    }

    @XmlTransient
    public List<FacDocuementopago> getFacDocuementopagoList() {
        return facDocuementopagoList;
    }

    public void setFacDocuementopagoList(List<FacDocuementopago> facDocuementopagoList) {
        this.facDocuementopagoList = facDocuementopagoList;
    }

    public CfgEmpresa getCfgempresaidEmpresa() {
        return cfgempresaidEmpresa;
    }

    public void setCfgempresaidEmpresa(CfgEmpresa cfgempresaidEmpresa) {
        this.cfgempresaidEmpresa = cfgempresaidEmpresa;
    }

    @XmlTransient
    public List<ConsolidadoMovcaja> getConsolidadoMovcajaList() {
        return consolidadoMovcajaList;
    }

    public void setConsolidadoMovcajaList(List<ConsolidadoMovcaja> consolidadoMovcajaList) {
        this.consolidadoMovcajaList = consolidadoMovcajaList;
    }

    @XmlTransient
    public List<FacMovcajadetalle> getFacMovcajadetalleList() {
        return facMovcajadetalleList;
    }

    public void setFacMovcajadetalleList(List<FacMovcajadetalle> facMovcajadetalleList) {
        this.facMovcajadetalleList = facMovcajadetalleList;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFormaPago != null ? idFormaPago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgFormapago)) {
            return false;
        }
        CfgFormapago other = (CfgFormapago) object;
        if ((this.idFormaPago == null && other.idFormaPago != null) || (this.idFormaPago != null && !this.idFormaPago.equals(other.idFormaPago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CfgFormapago[ idFormaPago=" + idFormaPago + " ]";
    }

}
