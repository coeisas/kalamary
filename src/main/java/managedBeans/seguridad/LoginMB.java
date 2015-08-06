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
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author mario
 */
@ManagedBean
@ViewScoped
public class LoginMB implements Serializable {

    private String usuario;
    private Integer idSede;
    private Integer idEmpresa;
    private String password;

    private List<CfgEmpresasede> listaSedes;

    @EJB
    SegUsuarioFacade usuarioFacade;

    @EJB
    CfgEmpresasedeFacade empresasedeFacade;

    @PostConstruct
    private void init() {
        listaSedes = new ArrayList();
    }

    public LoginMB() {
    }

    public void cargarSedes() {
        if (idEmpresa != null) {
//            System.out.println("cargando sedes");
            setListaSedes(empresasedeFacade.buscarSedesPorEmpresa(idEmpresa));
        } else {
            getListaSedes().clear();
        }

    }

    public String iniciarSesion() {
        FacesContext context = FacesContext.getCurrentInstance();
//        ExternalContext econtext = FacesContext.getCurrentInstance().getExternalContext();
        AplicacionMB aplicacionMB = context.getApplication().evaluateExpressionGet(context, "#{aplicacionMB}", AplicacionMB.class);
        CfgEmpresasede sede = null;
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
        if (!aplicacionMB.getListaEmpresas().isEmpty()) {
            if (idSede == null) {
//                FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Elija la sede"));
                RequestContext.getCurrentInstance().update("message");
                return "";
            }
            sede = empresasedeFacade.find(idSede);
        }
        //comprueba primero si es un superusuario
        SegUsuario usuarioActual = usuarioFacade.buscarUsuarioSuper(usuario, password);
        if (usuarioActual == null) {
            usuarioActual = usuarioFacade.buscarUsuario(usuario, password, idSede);
        }
        if (usuarioActual == null) {
//                    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Revice las credenciales"));
            RequestContext.getCurrentInstance().update("message");
            return "";
        } else {
            
            if (!aplicacionMB.getListaUsuariosActivos().contains(usuarioActual)) {
//                        usuarioActual.setRememberToken(econtext.getSessionId(false));
                aplicacionMB.insertarUsuario(usuarioActual);
                SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
                sesionMB.setUsuarioActual(usuarioActual);
                sesionMB.tokenSession();
                sesionMB.setAutenticado(true);
                sesionMB.setSedeActual(sede);
//                        sesionMB.insertarItemSession(usuarioActual);
                return "procesos/main?faces-redirect=true";
            } else {
                SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
                if (sesionMB.getUsuarioActual() != null) {
                    return "procesos/main?faces-redirect=true";
                } else {
                    String path = aplicacionMB.descartarSession(usuarioActual);
                    if (!path.isEmpty()) {
                        sesionMB.setUsuarioActual(usuarioActual);
                        sesionMB.tokenSession();
                        sesionMB.setAutenticado(true);
//                                sesionMB.insertarItemSession(usuarioActual);
                        aplicacionMB.insertarUsuario(usuarioActual);
                        sesionMB.setSedeActual(sede);
                    }
                    return path;
                }
            }
//        System.out.println("creada sesion " + FacesContext.getCurrentInstance().getExternalContext().getSessionId(false));

        }
//            }
//        }
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getIdSede() {
        return idSede;
    }

    public void setIdSede(Integer idSede) {
        this.idSede = idSede;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<CfgEmpresasede> getListaSedes() {
        return listaSedes;
    }

    public void setListaSedes(List<CfgEmpresasede> listaSedes) {
        this.listaSedes = listaSedes;
    }

}
