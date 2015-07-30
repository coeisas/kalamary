/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.proceso;

import entities.FacCaja;
import entities.FacMovcaja;
import entities.SegUsuario;
import facades.FacMovcajaFacade;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import managedBeans.seguridad.SesionMB;
import org.primefaces.context.RequestContext;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class AbrirCajaMB implements Serializable {

    private Date fechaApertura;//equivale a la fecha de apertura de la caja
    private float base;
    private float trm;

    private FacCaja cajaActual;//corresponde la caja asociada que tiene el usuario logeado
    private FacMovcaja movcajaActual;

    private SesionMB sesionMB;
    private SegUsuario usuarioActual;

    @EJB
    FacMovcajaFacade movcajaFacade;

    public AbrirCajaMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
//        actualizarInformacionCaja();
        if (sesionMB.getUsuarioActual() != null) {
            usuarioActual = sesionMB.getUsuarioActual();
        }
    }

    public void actualizarInformacionCaja() {
        if (usuarioActual != null) {
            setCajaActual(usuarioActual.getFaccajaidCaja());
            if (getCajaActual() != null) {
                movcajaActual = movcajaFacade.buscarMovimientoCaja(getCajaActual());
                if (movcajaActual != null) {
                    if (movcajaActual.getAbierta()) {
                        setFechaApertura(movcajaActual.getFecApertura());
                        setBase(movcajaActual.getBase());
                        setTrm(movcajaActual.getTrm());
                    } else {
                        limpiarForm();
                    }
                }
            }
        } else {
            movcajaActual = null;
        }
        RequestContext.getCurrentInstance().update("IdFormAbrirCaja");
    }

    public void limpiarForm() {
        setFechaApertura(null);
        setBase(0);
        setTrm(0);

    }

    public void abrirCaja() {
        if (cajaActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El usuario no tiene caja asociada"));
            return;
        }
        movcajaActual = movcajaFacade.buscarMovimientoCaja(cajaActual);
        if (movcajaActual != null) {
            if (movcajaActual.getAbierta()) {
                RequestContext.getCurrentInstance().update("IdFormAbrirCaja");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La caja se encuentra abierta"));
                return;
            }
        }
        try {
            FacMovcaja movcaja = new FacMovcaja();
            movcaja.setAbierta(true);
            movcaja.setBase(base);
            movcaja.setFaccajaidCaja(cajaActual);
            movcaja.setFeccrea(new Date());
            movcaja.setFecApertura(new Date());
            movcaja.setFecCierre(null);
            movcaja.setTrm(trm);
            movcaja.setSegusuarioidUsuario(usuarioActual);//usuario que abre la caja
            movcajaFacade.create(movcaja);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Caja abierta"));
            actualizarInformacionCaja();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se logro abrir la caja"));
        }
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public float getBase() {
        return base;
    }

    public void setBase(float base) {
        this.base = base;
    }

    public float getTrm() {
        return trm;
    }

    public void setTrm(float trm) {
        this.trm = trm;
    }

    public FacCaja getCajaActual() {
        return cajaActual;
    }

    public void setCajaActual(FacCaja cajaActual) {
        this.cajaActual = cajaActual;
    }

}