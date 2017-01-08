/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.informes;

import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.SegUsuario;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import managedBeans.seguridad.SesionMB;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import utilities.DBConnector;


/**
 *
 * @author ArcoSoft-Pc1
 */
@Named(value = "vencimientoFacturasMB")
@SessionScoped
public class VencimientoFacturasMB implements Serializable {

    private CfgEmpresa empresaActual;
    private CfgEmpresasede sedeActual;
    private SegUsuario usuarioActual;
    private SesionMB sesionMB;
    
    public VencimientoFacturasMB() {
    
    }
    
    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        empresaActual = sesionMB.getEmpresaActual();
        sedeActual = sesionMB.getSedeActual();
        usuarioActual = sesionMB.getUsuarioActual();
    }
    
    private boolean validar() {
        boolean ban = true;
        if (sedeActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro informacion de la sede"));
            return false;
        }
        if (usuarioActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro informacion del usuario"));
            return false;
        }
        
        return ban;
    }
    public void generarReporte() throws IOException, JRException {
        if (!validar()) {
            return;
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta ;
                ruta = servletContext.getRealPath("/informes/reportes/vencimientosFacturas.jasper");
            Map<String, Object> parametros = new HashMap<>();
            try{
                parametros.put("P_EMPRESA", sedeActual.getIdSede());
                Connection con = DBConnector.getInstance().getConnection();
                JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, con);
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
                FacesContext.getCurrentInstance().responseComplete();
                con.close();
                DBConnector.getInstance().closeConnection();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    
    public void generarReporteCSV()  {
        if (!validar()) {
            return;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        try{
            FacesContext.getCurrentInstance().responseComplete();
            String baseURL = context.getExternalContext().getRequestContextPath();
            String url =  baseURL +"/ReportesCSV?tipo=vencimientosFacturas&sede="+sedeActual.getIdSede();
            String encodeURL = context.getExternalContext().encodeResourceURL(url);
        
            context.getExternalContext().redirect(encodeURL);
        }  catch(Exception e)    {
            e.printStackTrace();
        }
    }

    public CfgEmpresa getEmpresaActual() {
        return empresaActual;
    }

    public void setEmpresaActual(CfgEmpresa empresaActual) {
        this.empresaActual = empresaActual;
    }

    public CfgEmpresasede getSedeActual() {
        return sedeActual;
    }

    public void setSedeActual(CfgEmpresasede sedeActual) {
        this.sedeActual = sedeActual;
    }

    public SegUsuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(SegUsuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public SesionMB getSesionMB() {
        return sesionMB;
    }

    public void setSesionMB(SesionMB sesionMB) {
        this.sesionMB = sesionMB;
    }
    
}
