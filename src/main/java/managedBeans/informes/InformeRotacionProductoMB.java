/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.informes;

import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import managedBeans.seguridad.SesionMB;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import utilities.DBConnector;

/**
 *
 * @author ArcoSoft-Pc1
 */
@Named(value = "informeRotacionProductoMB")
@ManagedBean
@SessionScoped
public class InformeRotacionProductoMB {

    private Date fechaInicial;
    private Date fechaFinal;
    private CfgEmpresa empresaActual;
    private CfgEmpresasede sedeActual;
    
    /**
     * Creates a new instance of InformeRotacionProductoMB
     */
    public InformeRotacionProductoMB() {
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
            String ruta = servletContext.getRealPath("/informes/reportes/rotacionProductos.jasper");
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
            FacesContext context = FacesContext.getCurrentInstance();
            SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
            empresaActual = sesionMB.getEmpresaActual();
            sedeActual = sesionMB.getSedeActual();
        
        if (!validar()) {
            return;
        }
        
        try{
            FacesContext.getCurrentInstance().responseComplete();
            String baseURL = context.getExternalContext().getRequestContextPath();
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String fechaIni = sd.format(fechaInicial);
            String fechaFin = sd.format(fechaFinal);
            String url =  baseURL +"/ReportesCSV?tipo=rotacion&url="+baseURL+"&fechaInicial="+fechaIni+"&fechaFinal="+fechaFin+"&sede="+sedeActual.getIdSede();
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
