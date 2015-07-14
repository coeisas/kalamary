/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.seguridad;

import entities.CfgEmpresasede;
import entities.SegUsuario;
import facades.CfgEmpresasedeFacade;
import facades.SegUsuarioFacade;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import java.util.List;
import javax.annotation.PostConstruct;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class SesionMB implements Serializable {

    private String path;
    private String usuario;
    private Integer idSede;
    private Integer idEmpresa;
    private String password;
    private boolean autenticado = false;
    private String idSesion;
    private SegUsuario usuarioActual;

    private List<CfgEmpresasede> listaSedes;

    @EJB
    SegUsuarioFacade usuarioFacade;

    @EJB
    CfgEmpresasedeFacade empresasedeFacade;

    /**
     * Creates a new instance of SesionMB
     */
    @PostConstruct
    private void init() {
        setAutenticado(false);
        ExternalContext econtext = FacesContext.getCurrentInstance().getExternalContext();
        setIdSesion(econtext.getSessionId(false));
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

    public void cargarSedes() {
        if (idEmpresa != null) {
//            System.out.println("cargando sedes");
            listaSedes = empresasedeFacade.buscarSedesPorEmpresa(idEmpresa);
        } else {
            listaSedes.clear();
        }

    }

    public String iniciarSesion() {
        
        if (usuario.trim().isEmpty()) {
//            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese el usuario"));
            RequestContext.getCurrentInstance().update("message");
            return "";
        }
        if (password.trim().isEmpty()) {
//            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese contraseña"));
            RequestContext.getCurrentInstance().update("message");
            return "";
        }
        //se comprueba si es un superusuario
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = FacesContext.getCurrentInstance().getExternalContext();
        AplicacionMB aplicacionMB = context.getApplication().evaluateExpressionGet(context, "#{aplicacionMB}", AplicacionMB.class);
        usuarioActual = usuarioFacade.buscarUsuarioSuper(usuario, password);
        if (usuarioActual != null) {
            if (!aplicacionMB.getListaUsuariosActivos().contains(usuarioActual)) {
                usuarioActual.setRememberToken(econtext.getSessionId(false));
                aplicacionMB.insertarUsuario(usuarioActual);
            }
            setAutenticado(true);
//        System.out.println("creada sesion " + FacesContext.getCurrentInstance().getExternalContext().getSessionId(false));
            return "main?faces-redirect=true";
        } else {
            if (idSede == null) {
                FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Elija la sede"));
                RequestContext.getCurrentInstance().update("message");
                return "";
            } else {
                usuarioActual = usuarioFacade.buscarUsuario(usuario, password, idSede);
                if (usuarioActual == null) {
//                    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                    setAutenticado(false);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Revice las credenciales"));
                    RequestContext.getCurrentInstance().update("message");
                    return "";
                } else {
                    if (!aplicacionMB.getListaUsuariosActivos().contains(usuarioActual)) {
                        usuarioActual.setRememberToken(econtext.getSessionId(false));
                        aplicacionMB.insertarUsuario(usuarioActual);
                    }
                    setAutenticado(true);
//        System.out.println("creada sesion " + FacesContext.getCurrentInstance().getExternalContext().getSessionId(false));
                    return "main?faces-redirect=true";
                }
            }
        }
    }

    public String cerrarSesion() {
        FacesContext context = FacesContext.getCurrentInstance();
        AplicacionMB aplicacionMB = context.getApplication().evaluateExpressionGet(context, "#{aplicacionMB}", AplicacionMB.class);
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIdSede() {
        return idSede;
    }

    public void setIdSede(Integer idSede) {
        this.idSede = idSede;
    }

    public SegUsuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(SegUsuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public List<CfgEmpresasede> getListaSedes() {
        return listaSedes;
    }

    public void setListaSedes(List<CfgEmpresasede> listaSedes) {
        this.listaSedes = listaSedes;
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
