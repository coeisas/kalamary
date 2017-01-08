/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.informes;

import entities.CfgCliente;
import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.SegUsuario;
import facades.CfgClienteFacade;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import utilities.DBConnector;
import utilities.LazyClienteDataModel;

/**
 *
 * @author ArcoSoft-Pc1
 */
@Named(value = "informeVentasClienteMB")
@SessionScoped
public class InformeVentasClienteMB implements Serializable {

    private String identificacion;
    private String nombreCliente;
    private CfgCliente clienteSeleccionado;
    private Date fechaInicial;
    private Date fechaFinal;

    private LazyDataModel<CfgCliente> listaClientes;

    private CfgEmpresa empresaActual;
    private CfgEmpresasede sedeActual;
    private SegUsuario usuarioActual;

    @EJB
    CfgClienteFacade clienteFacade;

    public InformeVentasClienteMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        empresaActual = sesionMB.getEmpresaActual();
        sedeActual = sesionMB.getSedeActual();
        usuarioActual = sesionMB.getUsuarioActual();
    }

    public void cargarClientes() {
        if (empresaActual != null) {
            listaClientes = new LazyClienteDataModel(clienteFacade, empresaActual);
            RequestContext.getCurrentInstance().update("FormBuscarCliente");
            RequestContext.getCurrentInstance().execute("PF('dlgCliente').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Determine la empresa"));
        }
    }

    public void buscarCliente() {
        if (empresaActual != null) {
            if (identificacion != null && !identificacion.trim().isEmpty()) {
                clienteSeleccionado = clienteFacade.buscarPorIdentificacionAndIdEmpresa(identificacion, empresaActual);
                cargarInformacionCliente();
            }
        } else {
            clienteSeleccionado = null;
            cargarInformacionCliente();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Determine la empresa"));
        }
    }

    public void cargarInformacionCliente() {
        if (getClienteSeleccionado() != null) {
            identificacion = clienteSeleccionado.getNumDoc();
            nombreCliente = clienteSeleccionado.nombreCompleto();
        } else {
            nombreCliente = null;
        }
        RequestContext.getCurrentInstance().execute("PF('dlgCliente').hide()");
        RequestContext.getCurrentInstance().update("IdFormInformeCliente:IdCliente");
        RequestContext.getCurrentInstance().update("IdFormInformeCliente:IdNombreCliente");
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
            if(clienteSeleccionado!=null)
                ruta = servletContext.getRealPath("/informes/reportes/informeVentaCliente.jasper");
            else 
                ruta = servletContext.getRealPath("/informes/reportes/informeVentaClienteAll.jasper");
            Map<String, Object> parametros = new HashMap<>();
            if(clienteSeleccionado!=null)parametros.put("P_IDENTIFICACION", clienteSeleccionado.getNumDoc());
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
            String url =  baseURL +"/ReportesCSV?tipo=vc&identificacion="+(clienteSeleccionado!=null?clienteSeleccionado.getNumDoc():"0")+"&sede="+sedeActual.getIdSede();
            String encodeURL = context.getExternalContext().encodeResourceURL(url);
        
            context.getExternalContext().redirect(encodeURL);
        }  catch(Exception e)    {
            e.printStackTrace();
        }
    }

    public void limpiar() {
        clienteSeleccionado = null;
        nombreCliente = null;
        identificacion = null;
        fechaInicial = null;
        fechaFinal = null;
        RequestContext.getCurrentInstance().update("IdFormInformeCliente");
    }

    public CfgCliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(CfgCliente clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
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

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public LazyDataModel<CfgCliente> getListaClientes() {
        return listaClientes;
    }
    
}
