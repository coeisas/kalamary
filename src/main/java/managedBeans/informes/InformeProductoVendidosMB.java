/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.informes;

import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
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
@Named(value = "informeProductoVendidosMB")
@ManagedBean
@SessionScoped
public class InformeProductoVendidosMB implements Serializable {

    private Date fechaInicial;
    private Date fechaFinal;
    private CfgEmpresa empresaActual;
    private CfgEmpresasede sedeActual;
    
    /**
     * Creates a new instance of InformeRotacionProductoMB
     */
    public InformeProductoVendidosMB() {
    }
    
    @PostConstruct
    private void init() {
        
        
        
    }
    
    private boolean validar() {
        boolean ban = true;
        if (sedeActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro informacion de la sede"));
            return false;
        }
        if (fechaFinal != null && fechaInicial != null) {
            if (fechaFinal.before(fechaInicial)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Verifique el orden de las fechas"));
                return false;
            }
        }
        return ban;
    }
    
    public void generarReporte() throws IOException, JRException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        SesionMB sesionMB = facesContext.getApplication().evaluateExpressionGet(facesContext, "#{sesionMB}", SesionMB.class);
            empresaActual = sesionMB.getEmpresaActual();
            sedeActual = sesionMB.getSedeActual();
        if (!validar()) {
            return;
        }
        
        
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("/informes/reportes/informeProductosVendidos.jasper");
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("P_FECHA_INICIAL", fechaInicial);
            parametros.put("P_FECHA_FINAL", fechaFinal);
            parametros.put("P_EMPRESA", sedeActual.getIdSede());
            try{
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
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String fechaIni = sd.format(fechaInicial);
            String fechaFin = sd.format(fechaFinal);
            String url =  baseURL +"/ReportesCSV?tipo=productosVendidos&url="+baseURL+"&fechaInicial="+fechaIni+"&fechaFinal="+fechaFin+"&sede="+sedeActual.getIdSede();
            String encodeURL = context.getExternalContext().encodeResourceURL(url);
        
            context.getExternalContext().redirect(encodeURL);
        }  catch(Exception e)    {
            e.printStackTrace();
        }
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
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
    
}
