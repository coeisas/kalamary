/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.movimientoCaja;

import entities.CfgFormapago;
import entities.FacCaja;
import entities.FacMovcaja;
import entities.FacMovcajadetalle;
import entities.SegUsuario;
import facades.CfgFormapagoFacade;
import facades.FacMovcajaFacade;
import facades.FacMovcajadetalleFacade;
import facades.SegUsuarioFacade;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import java.util.List;
import java.util.ArrayList;
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
public class CerrarCajaMB implements Serializable {
    
    private Date fechaCierre;//equivale a la fecha de cierre de la caja
    private float valorCierre;
    
    private FacCaja cajaActual;//corresponde la caja asociada que tiene el usuario logeado
    private FacMovcaja movcajaActual;
    
    private SesionMB sesionMB;
    private SegUsuario usuarioActual;
    
    private List<CfgFormapago> listaFormaPago;
    
    @EJB
    SegUsuarioFacade usuarioFacade;
    
    @EJB
    CfgFormapagoFacade formapagoFacade;
    
    @EJB
    FacMovcajaFacade movcajaFacade;
    
    @EJB
    FacMovcajadetalleFacade movcajadetalleFacade;
    
    public CerrarCajaMB() {
    }
    
    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
        //carga las formas de pago de la empresa elegida en el login
        if (usuarioActual != null && sesionMB.getSedeActual() != null) {
            setListaFormaPago(formapagoFacade.buscarPorEmpresa(sesionMB.getSedeActual().getCfgempresaidEmpresa()));
        } else {
            setListaFormaPago((List<CfgFormapago>) new ArrayList());
        }
        if (usuarioActual != null) {
            setCajaActual(usuarioActual.getFaccajaidCaja());
            if (cajaActual != null) {
                movcajaActual = movcajaActual = movcajaFacade.buscarMovimientoCaja(getCajaActual());
            }
        }else{
            cajaActual = null;
            movcajaActual = null;
        }
    }
    
    public void actualizarInformacionCaja() {
        if (cajaActual == null && usuarioActual != null) {
            usuarioActual = usuarioFacade.find(usuarioActual.getIdUsuario());
            cajaActual = usuarioActual.getFaccajaidCaja();
            if (cajaActual != null) {
                sesionMB.getUsuarioActual().setFaccajaidCaja(cajaActual);
            }
        }
        if (getCajaActual() != null) {            
                movcajaActual = movcajaFacade.buscarMovimientoCaja(getCajaActual());
            if (movcajaActual != null) {
                calcularInformacionCierre();
                if (!movcajaActual.getAbierta()) {
                    setFechaCierre(movcajaActual.getFecCierre());
                    setValorCierre(movcajaActual.getValorCierre());
                } else {
                    setValorCierre(0);
                    setFechaCierre(null);
                    calcularValorCierre();
                }                
            }
        } else {
            movcajaActual = null;
            setCajaActual(null);
        }
        RequestContext.getCurrentInstance().update("IdFormCerrarCaja");
    }
    
    private void calcularInformacionCierre() {
        List<FacMovcajadetalle> listaMovDetalle = movcajadetalleFacade.buscarMovDetallePorMovCaja(movcajaActual);
        if (listaMovDetalle != null) {
            clearSubtotales();
            for (FacMovcajadetalle movcajadetalle : listaMovDetalle) {
                CfgFormapago formapago = obtenerItemFormaPago(movcajadetalle.getCfgFormapago());
                formapago.setSubtotal(formapago.getSubtotal() + movcajadetalle.getValor());
            }
        }
    }
    
    private CfgFormapago obtenerItemFormaPago(CfgFormapago formapago) {
        for (CfgFormapago cfgFormapago : getListaFormaPago()) {
            if (cfgFormapago.equals(formapago)) {
                return cfgFormapago;
            }
        }
        return null;
    }
    
    private void clearSubtotales(){
         for (CfgFormapago cfgFormapago : getListaFormaPago()) {
             cfgFormapago.setSubtotal(0);
        }       
    }
    
    private void calcularValorCierre() {
        for (CfgFormapago cfgFormapago : getListaFormaPago()) {
            valorCierre += cfgFormapago.getSubtotal();
        }
    }
    
    public void cerrarCaja() {
        if (cajaActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El usuario no tiene caja asociada"));
            return;
        }
        movcajaActual = movcajaFacade.buscarMovimientoCaja(cajaActual);
        if (movcajaActual != null) {
            if (!movcajaActual.getAbierta()) {
                RequestContext.getCurrentInstance().update("IdFormCerrarCaja");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La caja se encuentra cerrada"));
                return;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La caja no se encuentra abierta"));
            return;
        }
        try {
            movcajaActual.setAbierta(false);
            movcajaActual.setFecCierre(new Date());
            //calcular valor del cierre buscando en movcajadetalle
            movcajaActual.setValorCierre(valorCierre);
            movcajaActual.setSegusuarioidUsuario1(usuarioActual);//usuario que cierra la caja
            movcajaFacade.edit(movcajaActual);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Caja cerrada"));
            actualizarInformacionCaja();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se logro cerrar la caja"));
        }
    }
    
    public Date getFechaCierre() {
        return fechaCierre;
    }
    
    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }
    
    public FacCaja getCajaActual() {
        return cajaActual;
    }
    
    public void setCajaActual(FacCaja cajaActual) {
        this.cajaActual = cajaActual;
    }
    
    public float getValorCierre() {
        return valorCierre;
    }
    
    public void setValorCierre(float valorCierre) {
        this.valorCierre = valorCierre;
    }
    
    public List<CfgFormapago> getListaFormaPago() {
        return listaFormaPago;
    }
    
    public void setListaFormaPago(List<CfgFormapago> listaFormaPago) {
        this.listaFormaPago = listaFormaPago;
    }
}
