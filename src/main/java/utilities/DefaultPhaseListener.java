package utilities;

import java.io.IOException;
import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import managedBeans.seguridad.AplicacionMB;
import managedBeans.seguridad.SesionMB;

/**
 *
 * @author mario
 */

/*
 * ingresa a esta clase cada que hay un cambio de fase
 * (fase = solicitud al servidor)
 */
public class DefaultPhaseListener implements PhaseListener {

    private AplicacionMB aplicacionMB;
    private SesionMB sesionMB;
    FacesContext facesContext;
    ExternalContext ext;
    String paginaActual;

    @Override
    public void afterPhase(PhaseEvent event) {
        facesContext = event.getFacesContext();
        paginaActual = facesContext.getViewRoot().getViewId();
//        if (!paginaActual.endsWith("xhtml")) {//si no es xhtml no procesar(ejemplo pagina inicial: index.html)
//            //System.out.println("1) NO ES XHTML: "+paginaActual);
//            return;
//        }
        try {//System.out.println("salida del programa por que sesion es null" + ctxPath);
            ext = facesContext.getExternalContext();
            //verificar que solo sea la pagina login o home
//            if (!paginaActual.endsWith("home.xhtml")&&!paginaActual.endsWith("login.xhtml")){//agregar controles de lectura escritura y modificacion
//                ext.redirect(((ServletContext) ext.getContext()).getContextPath() + "//index.html?v=noautorizado");
//            }
            //---------------------------------------------------------------------------
            //si un usario tiene sesion desde otro equipo pudo terminar la sesion(cuando no aparece en aplicacionGeneralMB)
            //---------------------------------------------------------------------------        
//            sesionMB = (SesionMB) facesContext.getApplication().evaluateExpressionGet(facesContext, "#{sesionMB}", SesionMB.class);
//            if (sesionMB.isAutenticado()) {
//                //System.out.println("2) esta autenticado");
//                aplicacionMB = (AplicacionMB) ext.getApplicationMap().get("aplicacionMB");
//                if (!aplicacionMB.buscarPorIdSesion(sesionMB.getIdSesion())) {//System.out.println("salida del programa por que se inicio nueva sesion en otro equipo");
//                    //System.out.println("3) NO esta registrado en applicatioGeneralMB");
//                    sesionMB.setAutenticado(false);
//                    ((HttpSession) ext.getSession(false)).invalidate();
//                    if (paginaActual.contains("main.xhtml")) {//si la pagina es home se redirecciona a pagina inicial                        
//                        //System.out.println("4) esta en home y se redirecciona a index.Html close");
//                        ext.redirect(((ServletContext) ext.getContext()).getContextPath() + "//index.html?v=close");//System.out.println("enviado a: " + ctxPath + "/index.html?v=timeout");
//                    } else {//si la pagina no es home se redirecciona a mensaje de inactividad
//                        //System.out.println("4) NO esta en home y se redirecciona a iincatividad.html");
//                        ext.redirect(((ServletContext) ext.getContext()).getContextPath() + "/inactividad.html");//System.out.println("enviado a: " + ctxPath + "/index.html?v=timeout");
//                    }
//                    return;
//                }
//            }
            //---------------------------------------------------------------------------
            //--se determina si hubo inactividad o no se a iniciado session
            //---------------------------------------------------------------------------              
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
            if (!paginaActual.endsWith("index.xhtml")) {//System.out.println("5) pagina diferente de login.xhtml"+paginaActual);                
                if (session == null) { //System.out.println("6) sesion http esta en null");
//                    sesionMB.setAutenticado(false);
//                    aplicacionMB.removeSession(loginMB.getIdSession());
//                    if (paginaActual.contains("main.xhtml")) {//si la pagina es home se redirecciona a pagina inicial
                    ext.redirect(((ServletContext) ext.getContext()).getContextPath() + "/?v=timeout");//System.out.println("enviado a: " + ctxPath + "/index.html?v=timeout");
//                    } else {//si la pagina no es home se redirecciona a mensaje de inactividad
//                        ext.redirect(((ServletContext) ext.getContext()).getContextPath() + "/index.html");//System.out.println("enviado a: " + ctxPath + "/index.html?v=timeout");
//                    }
                }
            }
//            else {//es la pagina de login
//                if (session == null) {//finalizo la sesion
//                    ext.redirect(((ServletContext) ext.getContext()).getContextPath() + "/");//System.out.println("enviado a: " + ctxPath + "/index.html?v=timeout");
//                }
//            }
        } catch (ELException | IOException t) {//System.out.println("Fallo al expirar sesion " + t.toString());
            throw new FacesException("Session timed out", t);
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
