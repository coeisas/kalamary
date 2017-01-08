/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.configuracion;

import entities.CfgEmpresa;
import entities.CfgTalla;
import entities.SegUsuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import entities.CfgUnidad;
import facades.CfgTallaFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import managedBeans.seguridad.SesionMB;
import facades.CfgUnidadFacade;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

/**
 *
 * @author ArcoSoft-Pc1
 */
@Named(value = "unidadMB")
@SessionScoped
public class UnidadMB implements Serializable {

    private String nombreUnidad;
    private String abreviaturaUnidad;
    private CfgUnidad  unidad;
    private List<CfgUnidad> listaUnidad;
    
    private SegUsuario usuarioActual;
    private CfgEmpresa empresaActual;
    
    @EJB
    CfgUnidadFacade unidadFacade;
    /**
     * Creates a new instance of UnidadMB
     */
    public UnidadMB() {
    }
    
    
    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
        empresaActual = sesionMB.getEmpresaActual();
        listaUnidad = unidadFacade.findAll();
    }
    
    private boolean validar(){
        if(nombreUnidad==null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese un nombre unidad"));
            return false;
        }
        if(nombreUnidad.equals("")){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese un nombre unidad"));
            return false;
        }
        return true;
    }
    public void accion(){
        try{
            if(validar()){
                if(unidad!=null){
                    unidad.setUnidad(nombreUnidad);
                    unidad.setAbreviatura(abreviaturaUnidad);
                    unidadFacade.edit(unidad);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Unidad Modificada"));
                }else{
                    unidad = new CfgUnidad();
                    unidad.setUnidad(nombreUnidad);
                    unidad.setAbreviatura(abreviaturaUnidad);
                    unidadFacade.create(unidad);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Unidad Modificada"));
                }
                cancelar();
                }
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unidad no Modificada"));
        }
    }
    
    
    public void cancelar(){
        unidad = null;
        nombreUnidad = null;
        abreviaturaUnidad = null;
        listaUnidad = unidadFacade.findAll();
    }
    
    public void cargar(int idUnidad){
        unidad = unidadFacade.getUnidadXid(idUnidad);
        if(unidad!=null){
            nombreUnidad =unidad.getUnidad();
            abreviaturaUnidad = unidad.getAbreviatura();
        }
    }

    public void eliminar(int idUnidad){
        unidad = unidadFacade.getUnidadXid(idUnidad);
        try {
            unidadFacade.remove(unidad);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Unidad Eliminada"));
            cancelar();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unidad Se encuentra asociada a un producto"));
        }
    }

    public SegUsuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(SegUsuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public CfgEmpresa getEmpresaActual() {
        return empresaActual;
    }

    public void setEmpresaActual(CfgEmpresa empresaActual) {
        this.empresaActual = empresaActual;
    }

    public String getNombreUnidad() {
        return nombreUnidad;
    }

    public void setNombreUnidad(String nombreUnidad) {
        this.nombreUnidad = nombreUnidad;
    }

    public String getAbreviaturaUnidad() {
        return abreviaturaUnidad;
    }

    public void setAbreviaturaUnidad(String abreviaturaUnidad) {
        this.abreviaturaUnidad = abreviaturaUnidad;
    }

    public CfgUnidad getUnidad() {
        return unidad;
    }

    public void setUnidad(CfgUnidad unidad) {
        this.unidad = unidad;
    }

    public List<CfgUnidad> getListaUnidad() {
        return listaUnidad;
    }

    public void setListaUnidad(List<CfgUnidad> listaUnidad) {
        this.listaUnidad = listaUnidad;
    }

    
    
}
