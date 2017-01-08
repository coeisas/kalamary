/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.informes;

import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.SegUsuario;
import facades.SegUsuarioFacade;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import managedBeans.seguridad.SesionMB;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.context.RequestContext;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.primefaces.context.RequestContext;
import utilities.DBConnector;

/**
 *
 * @author ArcoSoft-Pc1
 */
@Named(value = "informeVentaVendedoresMB")
@SessionScoped
public class InformeVentaVendedoresMB implements Serializable {

    private SegUsuario vendedorSeleccionado;
    private CfgEmpresa empresaActual;
    private CfgEmpresasede sedeActual;
    private Date fechaInicial;
    private Date fechaFinal;
    private SegUsuario usuarioActual;
    private SesionMB sesionMB;
    private String documentoVendedor;
    private List<SegUsuario> listaVendedor;
    private String nombreVendedor;
    
    @EJB
    SegUsuarioFacade usuarioFacade;
    /**
     * Creates a new instance of InformeVentaVendedoresMB
     */
    public InformeVentaVendedoresMB() {
    }
    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        empresaActual = sesionMB.getEmpresaActual();
        sedeActual = sesionMB.getSedeActual();
        usuarioActual = sesionMB.getUsuarioActual();
    }


    
    public void cargarListaVendedores() {
        if (empresaActual != null) {
            listaVendedor = usuarioFacade.buscarPorEmpresaActivos(empresaActual);
        } else {
            listaVendedor = new ArrayList();
        }
        RequestContext.getCurrentInstance().execute("PF('dlgVendedor').show()");
        RequestContext.getCurrentInstance().update("FormModalVendedor");
    }

    public void buscarVendedor() {
        if (empresaActual != null) {
            if (!documentoVendedor.trim().isEmpty()) {
                vendedorSeleccionado = usuarioFacade.buscarVendedorByEmpresaAndDocumento(empresaActual, documentoVendedor);
            }
        } else {
            vendedorSeleccionado = null;
        }
        cargarInformacionVendedor();
    }
    
    public void cargarInformacionVendedor() {
        if (vendedorSeleccionado != null) {
            nombreVendedor = vendedorSeleccionado.nombreCompleto();
            documentoVendedor = vendedorSeleccionado.getNumDoc();
        } else {
            nombreVendedor = null;
            documentoVendedor = null;
        }
        RequestContext.getCurrentInstance().execute("PF('dlgVendedor').hide()");
        RequestContext.getCurrentInstance().update("IdFormInformeVentaVendedores");
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
        if (fechaFinal != null && fechaInicial != null) {
            if (fechaFinal.before(fechaInicial)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Verifique el orden de las fechas"));
                return false;
            }
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
            if(vendedorSeleccionado!=null)
                ruta = servletContext.getRealPath("/informes/reportes/informeVentaVendedores.jasper");
            else 
                ruta = servletContext.getRealPath("/informes/reportes/informeVentaVendedoresAll.jasper");
            Map<String, Object> parametros = new HashMap<>();
            if(vendedorSeleccionado!=null)parametros.put("P_IDENTIFICACION", vendedorSeleccionado.getNumDoc());
            parametros.put("P_EMPRESA", sedeActual.getIdSede());
            if(fechaInicial==null && fechaFinal==null){
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                try{
                    fechaInicial = sd.parse("2010-01-01");
                    String fechaAct = sd.format(new Date());
                    fechaFinal = sd.parse(fechaAct);
                }catch(Exception ex){
                    
                }
            }
            parametros.put("P_FECHA_INICIAL", fechaInicial);
            parametros.put("P_FECHA_FINAL", fechaFinal);

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
            String fechaIni = null;
            String fechaFin = null;
            if(fechaInicial==null && fechaFinal==null){
                    fechaIni  ="2010-01-01";
                    fechaFin = sd.format(new Date());
            }else{
                fechaIni  =sd.format(fechaInicial);
                fechaFin  =sd.format(fechaFinal);
            }
            String url =  baseURL +"/ReportesCSV?tipo=ventasVendedores&url="+baseURL+"&identificacion="+(documentoVendedor!=null?documentoVendedor:"0")+"&sede="+sedeActual.getIdSede()+"&fechaInicial="+fechaIni+"&fechaFinal="+fechaFin;
            String encodeURL = context.getExternalContext().encodeResourceURL(url);
        
            context.getExternalContext().redirect(encodeURL);
        }  catch(Exception e)    {
            e.printStackTrace();
        }
    }

    public void limpiar() {
        vendedorSeleccionado = null;
        nombreVendedor = null;
        documentoVendedor = null;
        fechaInicial = null;
        fechaFinal = null;
        RequestContext.getCurrentInstance().update("IdFormInformeVentaVendedores");
    }
    public SegUsuario getVendedorSeleccionado() {
        return vendedorSeleccionado;
    }

    public void setVendedorSeleccionado(SegUsuario vendedorSeleccionado) {
        this.vendedorSeleccionado = vendedorSeleccionado;
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

    public String getDocumentoVendedor() {
        return documentoVendedor;
    }

    public void setDocumentoVendedor(String documentoVendedor) {
        this.documentoVendedor = documentoVendedor;
    }

    public List<SegUsuario> getListaVendedor() {
        return listaVendedor;
    }

    public void setListaVendedor(List<SegUsuario> listaVendedor) {
        this.listaVendedor = listaVendedor;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
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

    
    
}
