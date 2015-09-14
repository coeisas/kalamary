/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.informes;

import entities.CfgCliente;
import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.FacDocumentoimpuesto;
import entities.FacDocumentosmaster;
import entities.SegUsuario;
import facades.CfgClienteFacade;
import facades.FacDocumentosmasterFacade;
import facades.SegUsuarioFacade;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import managedBeans.seguridad.SesionMB;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import utilities.InformeFactura;
import utilities.LazyClienteDataModel;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class InformeFacturaMB implements Serializable {

    private Date fechaIncial;
    private Date fechaFinal;
    private String identificacionCliente;
    private String identificacionVendedor;
    private CfgCliente clienteSeleccionado;
    private SegUsuario vendedorSeleccionado;

    private String nombreCliente;
    private String nombreVendedor;

    private CfgEmpresa empresaSeleccionada;
    private CfgEmpresasede sedeActual;
    private SegUsuario usuarioActual;

    private LazyDataModel<CfgCliente> listaClientes;
//    private LazyDataModel<SegUsuario> listaVendedores;
    private List<SegUsuario> listaVendedores;

    @EJB
    CfgClienteFacade clienteFacade;
    @EJB
    SegUsuarioFacade usuarioFacade;
    @EJB
    FacDocumentosmasterFacade documentosmasterFacade;

    public InformeFacturaMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        empresaSeleccionada = sesionMB.getEmpresaActual();
        sedeActual = sesionMB.getSedeActual();
        usuarioActual = sesionMB.getUsuarioActual();
        fechaIncial = new Date();
        fechaFinal = new Date();
    }

    public void cargarClientes() {
        if (empresaSeleccionada != null) {
            listaClientes = new LazyClienteDataModel(clienteFacade, empresaSeleccionada);
            RequestContext.getCurrentInstance().update("FormBuscarCliente");
            RequestContext.getCurrentInstance().execute("PF('dlgCliente').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Determine la empresa"));
        }
    }

    public void buscarCliente() {
        if (empresaSeleccionada != null) {
            if (identificacionCliente != null && !identificacionCliente.trim().isEmpty()) {
                clienteSeleccionado = clienteFacade.buscarPorIdentificacionAndIdEmpresa(identificacionCliente, empresaSeleccionada);
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
            identificacionCliente = clienteSeleccionado.getNumDoc();
            nombreCliente = clienteSeleccionado.nombreCompleto();
        } else {
            nombreCliente = null;
        }
        RequestContext.getCurrentInstance().execute("PF('dlgCliente').hide()");
        RequestContext.getCurrentInstance().update("IdFormInformeFactura:IdCliente");
        RequestContext.getCurrentInstance().update("IdFormInformeFactura:IdNombreCliente");
    }

    public void cargarListaVendedores() {
        if (empresaSeleccionada != null) {
            listaVendedores = usuarioFacade.buscarVendedoresByEmpresa(empresaSeleccionada);
        } else {
            listaVendedores = new ArrayList();
        }
        RequestContext.getCurrentInstance().execute("PF('dlgVendedor').show()");
        RequestContext.getCurrentInstance().update("FormModalVendedor");
    }

    public void buscarVendedor() {
        if (empresaSeleccionada != null) {
            if (!identificacionVendedor.trim().isEmpty()) {
                vendedorSeleccionado = usuarioFacade.buscarVendedorByEmpresaAndDocumento(empresaSeleccionada, identificacionVendedor);
            }
        } else {
            vendedorSeleccionado = null;
        }
        cargarInformacionVendedor();
    }

    public void cargarInformacionVendedor() {
        if (vendedorSeleccionado != null) {
            nombreVendedor = vendedorSeleccionado.nombreCompleto();
            identificacionVendedor = vendedorSeleccionado.getNumDoc();
        } else {
            nombreVendedor = null;
            identificacionVendedor = null;
        }
        RequestContext.getCurrentInstance().execute("PF('dlgVendedor').hide()");
        RequestContext.getCurrentInstance().update("IdFormInformeFactura:IdVendedor");
        RequestContext.getCurrentInstance().update("IdFormInformeFactura:IdNombreVendedor");
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
        ///se debe ingresar al menos un parametro de busqueda
        if (fechaFinal == null && fechaIncial == null && clienteSeleccionado == null && vendedorSeleccionado == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese al menos un parametro"));
            return false;
        }
        if (fechaFinal.before(fechaIncial)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Verifique el orden de las fechas"));
            return false;
        }
        return ban;
    }

    public void generarReporte() throws IOException, JRException {
        if (!validar()) {
            return;
        }
        //como la fecha de facturacion contiene la hora es necesario aumentar un dia la fecha final ya que la fecha capurada tiene la hora 0
        Date aux = fechaFinal;
        if (fechaFinal != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fechaFinal);
            calendar.add(Calendar.DATE, 1);
            aux = calendar.getTime();
        }
        List<FacDocumentosmaster> listaFacturas = documentosmasterFacade.buscarFacturasToReporte(sedeActual, clienteSeleccionado, vendedorSeleccionado, fechaIncial, aux);
        List<InformeFactura> lista = generarListado(listaFacturas);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("/informes/reportes/informeFactura.jasper");
            Map<String, Object> parametros = new HashMap<>();
            if (sedeActual.getLogo() != null) {
                InputStream logo = new ByteArrayInputStream(sedeActual.getLogo());
                parametros.put("logo", logo);
            }
//            CfgEmpresa empresa = sedeActual.getCfgempresaidEmpresa();
//            parametros.put("empresa", empresa.getNomEmpresa() + " - " + sedeActual.getNomSede());
//            parametros.put("direccion", sedeActual.getDireccion());
//            String telefono = sedeActual.getTel1();
//            if (sedeActual.getTel2() != null && !sedeActual.getTel2().isEmpty()) {
//                telefono = telefono + "-".concat(sedeActual.getTel2());
//            }
//            parametros.put("telefono", telefono);
//            parametros.put("nit", empresa.getCfgTipodocempresaId().getDocumentoempresa() + " " + sedeActual.getNumDocumento() + " " + empresa.getCfgTipoempresaId().getDescripcion());
//            parametros.put("ubicacion", sedeActual.getCfgMunicipio().getNomMunicipio() + " " + sedeActual.getCfgMunicipio().getCfgDepartamento().getNomDepartamento());
//            parametros.put("usuario", usuarioActual.nombreCompleto());
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    private List<InformeFactura> generarListado(List<FacDocumentosmaster> listaFacturas) {
        List<InformeFactura> lista = new ArrayList();
        for (FacDocumentosmaster factura : listaFacturas) {
            InformeFactura elemento = new InformeFactura();
            elemento.setFecha(factura.getFecCrea());
            elemento.setNumFactura(factura.determinarNumFactura());
            elemento.setCliente(factura.getCfgclienteidCliente().nombreCompleto());
            elemento.setSubtotal(factura.getSubtotal());
            elemento.setDescuento(factura.getDescuento());
            float aux = 0;
            for (FacDocumentoimpuesto impuesto : factura.getFacDocumentoimpuestoList()) {
                aux += impuesto.getValorImpuesto();
            }
            elemento.setImpuesto(aux);
            elemento.setTotal(factura.getTotalFactura());
            aux = factura.getUtilidad() != null ? factura.getUtilidad() : 0;
            elemento.setUtilidad(aux);
            elemento.setTipoPago(factura.formasPagoString());
            elemento.setVendedor(factura.getSegusuarioidUsuario1().getUsuario());
            lista.add(elemento);
        }
        return lista;
    }

    public void limpiar() {
        identificacionCliente = null;
        identificacionVendedor = null;
        nombreVendedor = null;
        nombreCliente = null;
        clienteSeleccionado = null;
        vendedorSeleccionado = null;
        fechaIncial = new Date();
        fechaFinal = new Date();
        RequestContext.getCurrentInstance().update("IdFormInformeFactura");
    }

    public Date getFechaIncial() {
        return fechaIncial;
    }

    public void setFechaIncial(Date fechaIncial) {
        this.fechaIncial = fechaIncial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public CfgCliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(CfgCliente clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
    }

    public SegUsuario getVendedorSeleccionado() {
        return vendedorSeleccionado;
    }

    public void setVendedorSeleccionado(SegUsuario vendedorSeleccionado) {
        this.vendedorSeleccionado = vendedorSeleccionado;
    }

    public String getIdentificacionCliente() {
        return identificacionCliente;
    }

    public void setIdentificacionCliente(String identificacionCliente) {
        this.identificacionCliente = identificacionCliente;
    }

    public String getIdentificacionVendedor() {
        return identificacionVendedor;
    }

    public void setIdentificacionVendedor(String identificacionVendedor) {
        this.identificacionVendedor = identificacionVendedor;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public LazyDataModel<CfgCliente> getListaClientes() {
        return listaClientes;
    }

//    public LazyDataModel<SegUsuario> getListaVendedores() {
//        return listaVendedores;
//    }
    public List<SegUsuario> getListaVendedores() {
        return listaVendedores;
    }

}
