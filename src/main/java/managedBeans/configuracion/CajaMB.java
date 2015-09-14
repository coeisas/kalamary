/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.configuracion;

import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.FacCaja;
import entities.SegUsuario;
import facades.CfgEmpresaFacade;
import facades.CfgEmpresasedeFacade;
import facades.FacCajaFacade;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
public class CajaMB implements Serializable {
    
    private String codigoCaja;
    private String nombreCaja;
    private float baseCaja;
    
    private List<CfgEmpresasede> listaSedes;
    private List<FacCaja> listaCajas;
    
    private FacCaja cajaSeleccionada;
    private CfgEmpresa empresaActual;
    private CfgEmpresasede sedeActual;
    private SegUsuario usuarioActual;
    private SesionMB sesionMB;
    
    @EJB
    CfgEmpresasedeFacade sedeFacade;
    
    @EJB
    CfgEmpresaFacade empresaFacade;
    
    @EJB
    FacCajaFacade cajaFacade;
    
    public CajaMB() {
    }
    
    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
        sedeActual = sesionMB.getSedeActual();
        empresaActual = sesionMB.getEmpresaActual();
        if (sedeActual != null) {
            listaCajas = cajaFacade.buscarCajasPorSede(getSedeActual());
        }
    }
    
    public void limpiarFormulario() {
        setCodigoCaja("");
        setNombreCaja("");
        setBaseCaja(0);
    }
    
    public void buscarCajaPorCodigo() {
        cajaSeleccionada = cajaFacade.buscarCajasPorSedeAndCodigo(getSedeActual(), codigoCaja);
        cargarInformacionCaja();
    }
    
    private void cargarInformacionCaja() {
        if (cajaSeleccionada != null) {
            setCodigoCaja(cajaSeleccionada.getCodigoCaja());
            setNombreCaja(cajaSeleccionada.getNomCaja());
            setBaseCaja(cajaSeleccionada.getBase());
        }
        RequestContext.getCurrentInstance().update("IdFormCaja");
    }
    
    public void cancelar() {
        limpiarFormulario();
        listaCajas = cajaFacade.buscarCajasPorSede(getSedeActual());
        RequestContext.getCurrentInstance().update("IdDataCaja");
        RequestContext.getCurrentInstance().update("IdFormCaja");
    }
    
    public void accion() {
        if (cajaSeleccionada == null) {
            crearCaja();
        } else {
            modificarCaja();
        }
        
    }
    
    private void crearCaja() {
        if (sedeActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay sede elegida"));
            return;
        }
        //        solo los usuarios super y admin pueden modificar
        if (usuarioActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay usuario"));
            return;
        }
        if (!usuarioActual.getCfgRolIdrol().getCodrol().equals("00001") && !usuarioActual.getCfgRolIdrol().getCodrol().equals("00002")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No tiene permisos para efectuar esta accion"));
            return;
        }
        if (nombreCaja.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese el nombre de la caja"));
            return;
        }
        if (codigoCaja.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese el codigo de la caja"));
            return;
        }
        if (cajaSeleccionada != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Codigo de caja existente para la sede"));
            return;
        }
        if (baseCaja < 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Valor de la base es invalido"));
            return;
        }
        if (sedeActual == null) {
            return;
        }
        try {
            FacCaja caja = new FacCaja();
            caja.setCfgempresasedeidSede(getSedeActual());
            caja.setCodigoCaja(codigoCaja.toUpperCase());
            caja.setNomCaja(nombreCaja.toUpperCase());
            caja.setActiva(true);
            caja.setFeccrea(new Date());
            caja.setUsrcrea(usuarioActual.getIdUsuario());
            caja.setBase(baseCaja);
            cajaFacade.create(caja);
            cancelar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion", "Caja creada"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Caja no creada"));
        }
    }
    
    private void modificarCaja() {
        //        solo los usuarios super y admin pueden modificar
        if (usuarioActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay usuario"));
            return;
        }
        if (!usuarioActual.getCfgRolIdrol().getCodrol().equals("00001") && !usuarioActual.getCfgRolIdrol().getCodrol().equals("00002")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No tiene permisos para efectuar esta accion"));
            return;
        }
        if (cajaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No tiene una caja seleccionada"));
            return;
        }
        if (nombreCaja.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese el nombre de la caja"));
            return;
        }
        try {
            cajaSeleccionada.setNomCaja(nombreCaja.toUpperCase());
//            cajaSeleccionada.setUsrcrea(usuarioActual.getIdUsuario());
            cajaSeleccionada.setBase(baseCaja);
            cajaFacade.edit(cajaSeleccionada);
            cancelar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion", "Caja modificada"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Caja no modificada"));
        }
    }
    
    public String getCodigoCaja() {
        return codigoCaja;
    }
    
    public void setCodigoCaja(String codigoCaja) {
        this.codigoCaja = codigoCaja;
    }
    
    public String getNombreCaja() {
        return nombreCaja;
    }
    
    public void setNombreCaja(String nombreCaja) {
        this.nombreCaja = nombreCaja;
    }
    
    public List<CfgEmpresasede> getListaSedes() {
        return listaSedes;
    }
    
    public void setListaSedes(List<CfgEmpresasede> listaSedes) {
        this.listaSedes = listaSedes;
    }
    
    public List<FacCaja> getListaCajas() {
        return listaCajas;
    }
    
    public void setListaCajas(List<FacCaja> listaCajas) {
        this.listaCajas = listaCajas;
    }
    
    public FacCaja getCajaSeleccionada() {
        return cajaSeleccionada;
    }
    
    public void setCajaSeleccionada(FacCaja cajaSeleccionada) {
        this.cajaSeleccionada = cajaSeleccionada;
    }
    
    public CfgEmpresa getEmpresaActual() {
        return empresaActual;
    }
    
    public CfgEmpresasede getSedeActual() {
        return sedeActual;
    }
    
    public float getBaseCaja() {
        return baseCaja;
    }
    
    public void setBaseCaja(float baseCaja) {
        this.baseCaja = baseCaja;
    }
    
}
