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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

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
        if (usuarioActual != null) {
//            System.out.println("LOGOUT: " + usuarioActual.getUsuario());
            aplicacionMB.descartarHttpSession(usuarioActual.getSession());
            aplicacionMB.descartarUsuario(usuarioActual);
        }
    }

    public SesionMB() {

    }

    public void determinarPath(String file) {
        path = file + ".xhtml";
        RequestContext.getCurrentInstance().update("IdFormMain"); 
    }

    public void cerrarSesion() throws IOException {
        ExternalContext econtext = FacesContext.getCurrentInstance().getExternalContext();
        String contextPath = ((ServletContext) econtext.getContext()).getContextPath();
        setAutenticado(false);
        econtext.invalidateSession();
        econtext.redirect(contextPath  + "/");        
//        return "index?faces-redirect=true";
//        return "index";
    }

    public void controlSesion() throws IOException {
        if (!isAutenticado()) {

            ExternalContext econtext = FacesContext.getCurrentInstance().getExternalContext();
            String contextPath = ((ServletContext) econtext.getContext()).getContextPath();
            econtext.redirect(contextPath  + "/");

        }
    }

    public void cerrarSession() {
        ExternalContext econtext = FacesContext.getCurrentInstance().getExternalContext();
        econtext.invalidateSession();
    }

    public void tokenSession() {
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        usuarioActual.setSession(httpSession);
//        System.out.println("LOGIN: " + usuarioActual.getUsuario());
        aplicacionMB.insertarHttpSession(httpSession);        
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
