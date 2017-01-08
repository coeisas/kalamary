/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.informes;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.CfgProducto;
import entities.SegUsuario;
import facades.CfgProductoFacade;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
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
import utilities.LazyProductosModel;
/**
 *
 * @author ArcoSoft-Pc1
 */
@Named(value = "informeProductoStockMB")
@SessionScoped
public class InformeProductoStockMB implements Serializable {

    private String codigoProducto;
    private String nombreProducto;
    private LazyDataModel<CfgProducto> listaProducto;
    private CfgProducto productoSeleccionado;
    private CfgEmpresa empresaActual;
    private CfgEmpresasede sedeActual;
    private SegUsuario usuarioActual;
    private SesionMB sesionMB;
    @EJB
    CfgProductoFacade productoFacade;
    /**
     * Creates a new instance of InformeProductoStockMB
     */
    public InformeProductoStockMB() {
    }
    
        @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        empresaActual = sesionMB.getEmpresaActual();
        sedeActual = sesionMB.getSedeActual();
        usuarioActual = sesionMB.getUsuarioActual();
    }
    
    public void cargarModalProductos() {
        if (empresaActual != null) {
            listaProducto = new LazyProductosModel(empresaActual, null, null, null, productoFacade);
            RequestContext.getCurrentInstance().update("FormModalProducto");
            RequestContext.getCurrentInstance().execute("PF('dlgProducto').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No se encontro la empresa para cargar sus productos"));
        }

    }

    public void cargarInformacionProducto() {
        if (productoSeleccionado != null) {
            nombreProducto = productoSeleccionado.getNomProducto();
            codigoProducto = productoSeleccionado.getCodigoInterno();
        } else {
            nombreProducto = null;
            codigoProducto = null;
        }
        RequestContext.getCurrentInstance().execute("PF('dlgProducto').hide()");
        RequestContext.getCurrentInstance().update("FormProductosA");
    }
    
    public void buscarProducto() {
        if (empresaActual != null) {
            if (!codigoProducto.trim().isEmpty()) {
                productoSeleccionado = productoFacade.buscarPorCodigoInterno(empresaActual.getIdEmpresa(), codigoProducto);
            }
        } else {
            productoSeleccionado = null;
        }
        cargarInformacionProducto();
    }
    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public LazyDataModel<CfgProducto> getListaProducto() {
        return listaProducto;
    }

    public void setListaProducto(LazyDataModel<CfgProducto> listaProducto) {
        this.listaProducto = listaProducto;
    }

    public CfgProducto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(CfgProducto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
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
                ruta = servletContext.getRealPath("/informes/reportes/productosStock.jasper");
            Map<String, Object> parametros = new HashMap<>();
            if(productoSeleccionado!=null)parametros.put("CODIGO_PRODUCTO", productoSeleccionado.getCodigoInterno());
            else parametros.put("CODIGO_PRODUCTO", "0");
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
            String url =  baseURL +"/ReportesCSV?tipo=stock&producto="+(codigoProducto!=null?codigoProducto:"")+"&sede="+sedeActual.getIdSede();
            String encodeURL = context.getExternalContext().encodeResourceURL(url);
        
            context.getExternalContext().redirect(encodeURL);
        }  catch(Exception e)    {
            e.printStackTrace();
        }
    }
    public void limpiar(){
        productoSeleccionado = null;
        nombreProducto = null;
        codigoProducto = null;
        RequestContext.getCurrentInstance().update("FormProductosA");
    }
    
}
