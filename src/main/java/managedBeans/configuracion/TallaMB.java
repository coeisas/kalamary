/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.configuracion;

import entities.CfgEmpresa;
import entities.SegUsuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import entities.CfgTalla;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import managedBeans.seguridad.SesionMB;
import facades.CfgTallaFacade;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
/**
 *
 * @author ArcoSoft-Pc1
 */
@Named(value = "tallaMB")
@SessionScoped
public class TallaMB implements Serializable {

    private String nombreTalla;
    private List<CfgTalla> listaTalla;
    
    private SegUsuario usuarioActual;
    private CfgEmpresa empresaActual;
    
    private CfgTalla talla;
    @EJB
    CfgTallaFacade tallaFacade;
    public TallaMB() {
        
    }
    
    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
        empresaActual = sesionMB.getEmpresaActual();
        listaTalla = tallaFacade.findAll();
    }
    
    private boolean validar(){
        if(nombreTalla==null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese un nombre talla"));
            return false;
        }
        if(nombreTalla.equals("")){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese un nombre talla"));
            return false;
        }
        return true;
    }
    public void accion(){
        try{
            if(validar()){
                if(talla!=null){
                    talla.setTalla(nombreTalla);
                    tallaFacade.edit(talla);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Talla Modificada"));
                }else{
                    talla = new CfgTalla();
                    talla.setTalla(nombreTalla);
                    tallaFacade.create(talla);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Talla Modificada"));
                }
                cancelar();
                }
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Talla no Modificada"));
        }
    }
    
    
    public void cancelar(){
        talla = null;
        nombreTalla = null;
        listaTalla = tallaFacade.findAll();
    }
    
    public void cargar(int idTalla){
        talla = tallaFacade.getTallaXid(idTalla);
        if(talla!=null)nombreTalla = talla.getTalla();
    }

    public void eliminar(int idTalla){
        talla = tallaFacade.getTallaXid(idTalla);
        try {
            tallaFacade.remove(talla);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Talla Eliminada"));
            cancelar();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Talla Se encuentra asociada a un producto"));
        }
    }

    public String getNombreTalla() {
        return nombreTalla;
    }

    public void setNombreTalla(String nombreTalla) {
        this.nombreTalla = nombreTalla;
    }

    public List<CfgTalla> getListaTalla() {
        return listaTalla;
    }

    public void setListaTalla(List<CfgTalla> listaTalla) {
        this.listaTalla = listaTalla;
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

    public CfgTallaFacade getTallaFacade() {
        return tallaFacade;
    }

    public void setTallaFacade(CfgTallaFacade tallaFacade) {
        this.tallaFacade = tallaFacade;
    }

    public CfgTalla getTalla() {
        return talla;
    }

    public void setTalla(CfgTalla talla) {
        this.talla = talla;
    }
    
    
    
}
