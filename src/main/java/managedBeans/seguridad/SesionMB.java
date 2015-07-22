/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.seguridad;

import entities.SegUsuario;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class SesionMB implements Serializable {

    private String path;
    private boolean autenticado = false;
    private String idSesion;
    private SegUsuario usuarioActual;

    private AplicacionMB aplicacionMB;

    /**
     * Creates a new instance of SesionMB
     */
    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        aplicacionMB = context.getApplication().evaluateExpressionGet(context, "#{aplicacionMB}", AplicacionMB.class);
        ExternalContext econtext = FacesContext.getCurrentInstance().getExternalContext();
        setIdSesion(econtext.getSessionId(false));
    }

    @PreDestroy
    private void finish() {
        aplicacionMB.descartarUsuario(usuarioActual);
    }

    public SesionMB() {

    }

    public void determinarPath(String file) {
        path = file + ".xhtml";
//        System.out.println(path);
        RequestContext.getCurrentInstance().update("IdFormMain");
//        RequestContext.getCurrentInstance().update("contentPanel");
//        System.out.println(path);
//        RequestContext.getCurrentInstance().update("IdFormEmpresa");        
    }

    public String cerrarSesion() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        AplicacionMB aplicacionMB = context.getApplication().evaluateExpressionGet(context, "#{aplicacionMB}", AplicacionMB.class);
        aplicacionMB.descartarUsuario(usuarioActual);
//        System.out.println("cerrando sesion " + FacesContext.getCurrentInstance().getExternalContext().getSessionId(false));
        ExternalContext econtext = FacesContext.getCurrentInstance().getExternalContext();
//        String contextPath = ((ServletContext) context.getContext()).getContextPath();
        setAutenticado(false);
        econtext.invalidateSession();

//        context.redirect(contextPath  + "/index.xhtml");        
        return "index?faces-redirect=true";
    }

    public void controlSesion() throws IOException {
        if (!isAutenticado()) {

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect("index.xhtml");

        }
    }

    public void cerrarSession() {
        ExternalContext econtext = FacesContext.getCurrentInstance().getExternalContext();
        econtext.invalidateSession();
    }

    public void tokenSession() {
        usuarioActual.setRememberToken(idSesion);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public SegUsuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(SegUsuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public boolean isAutenticado() {
        return autenticado;
    }

    public void setAutenticado(boolean autenticado) {
        this.autenticado = autenticado;
    }

    public String getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(String idSesion) {
        this.idSesion = idSesion;
    }

}
