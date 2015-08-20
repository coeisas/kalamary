/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.proceso;

import entities.CfgCliente;
import entities.CfgDocumento;
import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.FacDocuementopago;
import entities.FacDocumentodetalle;
import entities.FacDocumentoimpuesto;
import entities.FacDocumentosmaster;
import entities.SegUsuario;
import facades.CfgClienteFacade;
import facades.CfgDocumentoFacade;
import facades.FacDocuementopagoFacade;
import facades.FacDocumentodetalleFacade;
import facades.FacDocumentoimpuestoFacade;
import facades.FacDocumentosmasterFacade;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ejb.EJB;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import managedBeans.seguridad.SesionMB;
import org.primefaces.model.LazyDataModel;
import utilities.LazyFacturaDataModel;
import javax.faces.context.FacesContext;
import utilities.LazyClienteDataModel;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import org.primefaces.context.RequestContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang.StringUtils;
import utilities.FacturaDetalleReporte;
import utilities.FacturaImpuestoReporte;
import utilities.FacturaPagoReporte;
import utilities.FacturaReporte;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class ListadoFacturaMB implements Serializable {

    /**
     * Creates a new instance of ListadoFacturaMB
     */
    private LazyDataModel<FacDocumentosmaster> listadoFacturas;
    private LazyDataModel<CfgCliente> listadoClientes;
    private String factura;
    private int numFactura;
    private Date fechaIncial;
    private Date fechaFinal;
    private CfgCliente clienteSeleccionado;
    private boolean renderTablaFactura;

    private int tipoImpresion;
    private Calendar fechaIni;
    private Calendar fechaFin;

    private CfgEmpresasede sedeActual;
    private SegUsuario usuarioActual;
    private FacDocumentosmaster documentoSeleccionado;

    @EJB
    CfgClienteFacade clienteFacade;
    @EJB
    FacDocumentosmasterFacade documentosmasterFacade;
    @EJB
    FacDocumentodetalleFacade documentodetalleFacade;
    @EJB
    FacDocumentoimpuestoFacade documentoimpuestoFacade;
    @EJB
    FacDocuementopagoFacade docuementopagoFacade;
    @EJB
    CfgDocumentoFacade documentoFacade;

    public ListadoFacturaMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
        if (sesionMB.getSedeActual() != null) {
            sedeActual = sesionMB.getSedeActual();
            listadoClientes = new LazyClienteDataModel(clienteFacade, sedeActual.getCfgempresaidEmpresa());
//            listadoFacturas = new LazyFacturaDataModel(documentosmasterFacade, sedeActual);
        }
        fechaIncial = new Date();
        fechaFinal = new Date();
        tipoImpresion = 1;
    }

    public void cargarInformacionCliente() {
        RequestContext.getCurrentInstance().execute("PF('dlgCliente').hide()");
        RequestContext.getCurrentInstance().update("IdFormListadoFacturas");
    }

    public void buscarFactura() {
        if (!factura.trim().isEmpty()) {
            try {
                numFactura = Integer.parseInt(factura);
            } catch (Exception e) {
                if (factura.contains("0")) {
                    String numfact = StringUtils.substringAfter(factura, "0");
                    try {
                        numFactura = Integer.parseInt(numfact);
                    } catch (Exception ex) {
                        numFactura = 0;
                    }
//                    }
                } else {
                    numFactura = 0;
                }
                if (numFactura == 0) {
                    CfgDocumento documento = documentoFacade.buscarDocumentoDeFacturaBySede(sedeActual);
                    String aux = documento.getPrefijoDoc();
                    int init = aux.length();
                    if (init < factura.length()) {
                        aux = factura.substring(init);
                        try {
                            numFactura = Integer.parseInt(aux);
                        } catch (Exception ex) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Factura incorrecta"));
                            return;
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Factura incorrecta"));
                        return;
                    }
                }
            }
        } else {
            numFactura = 0;
        }
        if (clienteSeleccionado != null || numFactura != 0 || fechaIncial != null || fechaFinal != null) {
            cargarFechasCalendar();
            listadoFacturas = new LazyFacturaDataModel(documentosmasterFacade, sedeActual, clienteSeleccionado, fechaIni, fechaFin, numFactura);
            renderTablaFactura = true;
        } else {
            listadoFacturas = null;
            renderTablaFactura = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Ingresar al menos un parametro de busqueda"));
        }
        RequestContext.getCurrentInstance().update("IdFormListadoFacturas");
    }

    private void cargarFechasCalendar() {
        if (fechaIncial != null) {
            fechaIni = Calendar.getInstance();
            fechaIni.setTime(fechaIncial);
        } else {
            fechaIni = null;
        }
        if (fechaFinal != null) {
            fechaFin = Calendar.getInstance();
            fechaFin.setTime(fechaFinal);
        } else {
            fechaFin = null;
        }
    }

    public void limpiar() {
        clienteSeleccionado = null;
        factura = null;
        numFactura = 0;
        fechaFinal = null;
        fechaIncial = null;
        listadoFacturas = null;
        RequestContext.getCurrentInstance().update("IdFormListadoFacturas");
        RequestContext.getCurrentInstance().update("FormBuscarCliente");
    }

//--------------------------------------------------------------------------------
////////////////////METODOS PARA LA GENERACION DEL PDF
//--------------------------------------------------------------------------------
    public void abrirOpcionImpresion(ActionEvent event) {
        documentoSeleccionado = (FacDocumentosmaster) event.getComponent().getAttributes().get("factura");
        RequestContext.getCurrentInstance().execute("PF('dlgImpresion').show()");
    }

    public void generarPDF() {
//        guardarFactura();
        if (documentoSeleccionado != null) {
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String ruta = null;
            switch (tipoImpresion) {
                case 1:
                    ruta = servletContext.getRealPath("/procesos/reportes/facturaTicket.jasper");
                    break;
                case 2:
                    ruta = servletContext.getRealPath("/procesos/reportes/facturaCarta.jasper");
                    break;
            }
//            setTipoImpresion(2);
            try {
                generarFactura(ruta);
            } catch (IOException ex) {
                Logger.getLogger(FacturaMB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JRException ex) {
                Logger.getLogger(FacturaMB.class.getName()).log(Level.SEVERE, null, ex);
            }
//            documentoActual = null;
        }

    }

    private void generarFactura(String ruta) throws IOException, JRException {
        FacDocumentosmaster documento
                = documentosmasterFacade.buscarBySedeAndDocumentoAndNum(
                        sedeActual,
                        documentoSeleccionado.getFacDocumentosmasterPK().getCfgdocumentoidDoc(),
                        documentoSeleccionado.getFacDocumentosmasterPK().getNumDocumento()
                );
        byte[] bites = sedeActual.getLogo();
        if (bites != null) {
            bites = sedeActual.getCfgempresaidEmpresa().getLogo();
        }
        List<FacturaReporte> facturas = new ArrayList();
        FacturaReporte facturaReporte = new FacturaReporte();
//        facturaReporte.setNumFactura(documento.getFacDocumentosmasterPK().getNumDocumento());
        facturaReporte.setNumFac(documento.determinarNumFactura());
        facturaReporte.setDescuento(documento.getDescuento());
        facturaReporte.setSubtotal(documento.getSubtotal());
        facturaReporte.setTotalFactura(documento.getTotalFactura());
        facturaReporte.setDetalle(crearListadoDetalle(documentodetalleFacade.buscarByDocumentoMaster(documento)));
        facturaReporte.setImpuesto(crearListadoImpuesto(documentoimpuestoFacade.buscarByDocumentoMaster(documento)));
        facturaReporte.setPago(crearListadoPago(docuementopagoFacade.buscarByDocumentoMaster(documento)));
        facturas.add(facturaReporte);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(facturas);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String rutaReportes = servletContext.getRealPath("/procesos/reportes/");//ubicacion para los subreportes
            Map<String, Object> parametros = new HashMap<>();
            if (bites != null) {
                InputStream logo = new ByteArrayInputStream(bites);
                parametros.put("logo", logo);
            }
            CfgEmpresa empresa = sedeActual.getCfgempresaidEmpresa();
            parametros.put("empresa", empresa.getNomEmpresa() + " - " + sedeActual.getNomSede());
            parametros.put("direccion", sedeActual.getDireccion());
            String telefono = sedeActual.getTel1();
            if (sedeActual.getTel2() != null && !sedeActual.getTel2().isEmpty()) {
                telefono = telefono + "-".concat(sedeActual.getTel2());
            }
            parametros.put("telefono", telefono);
            parametros.put("nit", empresa.getCfgTipodocempresaId().getDocumentoempresa() + " " + sedeActual.getNumDocumento() + " " + empresa.getCfgTipoempresaId().getDescripcion());
            parametros.put("cliente", documento.getCfgclienteidCliente().nombreCompleto());
            parametros.put("identificacionCliente", documento.getCfgclienteidCliente().getCfgTipoidentificacionId().getAbreviatura() + " " + documento.getCfgclienteidCliente().getNumDoc());
            parametros.put("fecha", documento.getFecCrea());
            parametros.put("usuario", usuarioActual.nombreCompleto());
            parametros.put("ubicacion", sedeActual.getCfgMunicipio().getNomMunicipio() + " " + sedeActual.getCfgMunicipio().getCfgDepartamento().getNomDepartamento());
            parametros.put("identificacionUsuario", usuarioActual.getNumDoc());
            parametros.put("SUBREPORT_DIR", rutaReportes);
            parametros.put("observacion", documento.getObservaciones());
            parametros.put("vendedor", documento.getSegusuarioidUsuario1().nombreCompleto());
            if (documento.getFaccajaidCaja() != null) {
                parametros.put("caja", documento.getFaccajaidCaja().getNomCaja());
            } else {
                parametros.put("caja", null);
            }
            parametros.put("resdian", documento.getCfgdocumento().getResDian());
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    private List<FacturaDetalleReporte> crearListadoDetalle(List<FacDocumentodetalle> detalles) {
        List<FacturaDetalleReporte> list = new ArrayList();
        for (FacDocumentodetalle detalle : detalles) {
            FacturaDetalleReporte facturaDetalleReporte = new FacturaDetalleReporte();
            facturaDetalleReporte.setCantidad(detalle.getCantidad());
            facturaDetalleReporte.setCodProducto(detalle.getCfgProducto().getCodProducto());
            facturaDetalleReporte.setNomProducto(detalle.getCfgProducto().getNomProducto());
            facturaDetalleReporte.setValorUnitario(detalle.getValorUnitario());
            facturaDetalleReporte.setValorTotal(detalle.getValorTotal());
            facturaDetalleReporte.setDescuento(detalle.getDescuento());
            list.add(facturaDetalleReporte);
        }
        return list;
    }

    private List<FacturaPagoReporte> crearListadoPago(List<FacDocuementopago> pagos) {
        List<FacturaPagoReporte> list = new ArrayList();
        for (FacDocuementopago pago : pagos) {
            FacturaPagoReporte facturaPagoReporte = new FacturaPagoReporte();
            facturaPagoReporte.setFormaPago(pago.getCfgFormapago().getNomFormaPago());
            facturaPagoReporte.setValorPago(pago.getValorPago());
            list.add(facturaPagoReporte);
        }
        return list;
    }

    private List<FacturaImpuestoReporte> crearListadoImpuesto(List<FacDocumentoimpuesto> impuestos) {
        List<FacturaImpuestoReporte> list = new ArrayList();
        for (FacDocumentoimpuesto impuesto : impuestos) {
            FacturaImpuestoReporte impuestoReporte = new FacturaImpuestoReporte();
            impuestoReporte.setImpuesto(impuesto.getCfgImpuesto().getNomImpuesto());
            impuestoReporte.setValorImpuesto(impuesto.getValorImpuesto());
            impuestoReporte.setPorcentaje(impuesto.getPorcentajeImpuesto());
            list.add(impuestoReporte);
        }
        return list;
    }
////---------------------------------------------------------------
////////////////METODOS PARA LA ANULACION DE DOCUMENTOS
////---------------------------------------------------------------

    public void anularDocumento(ActionEvent event) {
        documentoSeleccionado = (FacDocumentosmaster) event.getComponent().getAttributes().get("factura");
        if (documentoSeleccionado != null) {
            //        solo los usuarios super y admin pueden crear y modificar
            if (usuarioActual == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay usuario"));
                return;
            }
            //solo el super y el admin pueden crear usuarios
            if (!usuarioActual.getCfgRolIdrol().getCodrol().equals("00001") && !usuarioActual.getCfgRolIdrol().getCodrol().equals("00002")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No tiene permisos para efectuar esta accion"));
                return;
            }
            try {
                documentoSeleccionado.setEstado("ANULADA");
                documentosmasterFacade.edit(documentoSeleccionado);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Factura anulada"));
                listadoFacturas = new LazyFacturaDataModel(documentosmasterFacade, sedeActual, clienteSeleccionado, fechaIni, fechaFin, numFactura);
                RequestContext.getCurrentInstance().update("IdFormListadoFacturas");
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se logro anular la factura"));
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha Seleccionada la factura"));
        }
    }

    public LazyDataModel<FacDocumentosmaster> getListadoFacturas() {
        return listadoFacturas;
    }

    public LazyDataModel<CfgCliente> getListadoClientes() {
        return listadoClientes;
    }

    public void setListadoClientes(LazyDataModel<CfgCliente> listadoClientes) {
        this.listadoClientes = listadoClientes;
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

    public boolean isRenderTablaFactura() {
        return renderTablaFactura;
    }

    public int getTipoImpresion() {
        return tipoImpresion;
    }

    public void setTipoImpresion(int tipoImpresion) {
        this.tipoImpresion = tipoImpresion;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

}
