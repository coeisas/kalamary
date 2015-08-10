/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.proceso;

import entities.CfgCliente;
import entities.CfgDocumento;
import entities.CfgEmpresasede;
import entities.CfgFormapago;
import entities.CfgImpuesto;
import entities.CfgProducto;
import entities.FacDocuementopago;
import entities.FacDocuementopagoPK;
import entities.FacDocumentodetalle;
import entities.FacDocumentodetallePK;
import entities.FacDocumentoimpuesto;
import entities.FacDocumentoimpuestoPK;
import entities.FacDocumentosmaster;
import entities.FacDocumentosmasterPK;
import entities.SegUsuario;
import facades.CfgClienteFacade;
import facades.CfgDocumentoFacade;
import facades.CfgFormapagoFacade;
import facades.CfgImpuestoFacade;
import facades.CfgProductoFacade;
import facades.FacDocuementopagoFacade;
import facades.FacDocumentodetalleFacade;
import facades.FacDocumentoimpuestoFacade;
import facades.FacDocumentosmasterFacade;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.primefaces.context.RequestContext;
import javax.faces.context.FacesContext;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import managedBeans.seguridad.SesionMB;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.LazyDataModel;
import utilities.LazyProductosModel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import utilities.FacturaReporte;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilities.FacturaDetalleReporte;
import utilities.FacturaImpuestoReporte;
import utilities.FacturaPagoReporte;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class FacturaMB implements Serializable {

    private String identificacionCliente;
    private String nombreCliente;
    private float subtotal;
    private float totalDescuento;
    private float totalFactura;
    private LazyDataModel<CfgProducto> listaProducto;
    private List<FacDocumentodetalle> listaDetalle;
    private List<CfgCliente> listaClientes;
    private List<CfgImpuesto> listaImpuestos;
    private List<CfgFormapago> listaFormapagos;

    private int tipoImpresion;
//    private CfgEmpresa empresaActual;
    private CfgEmpresasede sedeActual;
    private CfgCliente clienteSeleccionado;
    private CfgProducto productoSeleccionado;
    private SegUsuario usuarioActual;
    private FacDocumentosmaster documentoActual;
    private boolean enableBtnPrint;

    private SesionMB sesionMB;
    @EJB
    CfgClienteFacade clienteFacade;
    @EJB
    CfgImpuestoFacade impuestoFacade;
    @EJB
    CfgDocumentoFacade documentoFacade;
    @EJB
    CfgProductoFacade productoFacade;
    @EJB
    CfgFormapagoFacade formapagoFacade;
    @EJB
    FacDocumentosmasterFacade documentosmasterFacade;
    @EJB
    FacDocumentodetalleFacade documentodetalleFacade;
    @EJB
    FacDocumentoimpuestoFacade documentoimpuestoFacade;
    @EJB
    FacDocuementopagoFacade docuementopagoFacade;

    public FacturaMB() {

    }

    @PostConstruct
    private void init() {
        //si es super usuario o admin permitir escoger la empresa y la sede
        tipoImpresion = 2;
        setEnableBtnPrint(false);
        setListaDetalle((List<FacDocumentodetalle>) new ArrayList());
        setListaImpuestos((List<CfgImpuesto>) new ArrayList());
        FacesContext context = FacesContext.getCurrentInstance();
        sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
        sedeActual = sesionMB.getSedeActual();
        if (sedeActual != null) {
            actualizarListadoClientes();
            listaProducto = new LazyProductosModel(sedeActual.getCfgempresaidEmpresa(), null, null, null, productoFacade);
            RequestContext.getCurrentInstance().update("FormModalProducto");
            clienteSeleccionado = clienteFacade.buscarClienteDefault(sedeActual.getCfgempresaidEmpresa());
            setListaImpuestos(impuestoFacade.buscarImpuestosPorTipoEmpresaAndSede(clienteSeleccionado.getCfgTipoempresaId(), sedeActual));
            listaFormapagos = formapagoFacade.buscarPorEmpresa(sedeActual.getCfgempresaidEmpresa());
            cargarInformacionCliente();
        }
    }

    public void buscarCliente() {
        if (!identificacionCliente.trim().isEmpty()) {
            clienteSeleccionado = clienteFacade.buscarPorIdentificacionAndIdEmpresa(identificacionCliente, sedeActual.getCfgempresaidEmpresa());
            if (clienteSeleccionado == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No se encontro cliente con esa identificacion"));
            }
        } else {
            clienteSeleccionado = null;
        }
        cargarInformacionCliente();
    }

    public void cargarInformacionCliente() {
        if (clienteSeleccionado != null) {
            setNombreCliente(clienteSeleccionado.nombreCompleto());
            setIdentificacionCliente(clienteSeleccionado.getNumDoc());
            setListaImpuestos(impuestoFacade.buscarImpuestosPorTipoEmpresaAndSede(clienteSeleccionado.getCfgTipoempresaId(), sedeActual));
            listaFormapagos = formapagoFacade.buscarPorEmpresa(sedeActual.getCfgempresaidEmpresa());
        } else {
            setNombreCliente(null);
            listaDetalle.clear();
            listaImpuestos.clear();
            listaFormapagos.clear();
        }
        RequestContext.getCurrentInstance().execute("PF('dlgCliente').hide()");
        RequestContext.getCurrentInstance().update("IdFormFactura:IdNomCliente");
        RequestContext.getCurrentInstance().update("IdFormFactura:IdSubTotal");
    }

    private void actualizarListadoClientes() {
        listaClientes = clienteFacade.buscarPorEmpresa(sedeActual.getCfgempresaidEmpresa());
    }

    public void deseleccionarCliente() {
        clienteSeleccionado = null;
        RequestContext.getCurrentInstance().update("FormBuscarCliente");
    }

    public void insertarItemProducto() {
        if (listaDetalle.isEmpty()) {
            subtotal = 0;
            totalDescuento = 0;
        }
        if (productoSeleccionado != null) {
            FacDocumentodetalle facdetalle = obtenerItemEnLista(productoSeleccionado);
            if (facdetalle == null) {
                facdetalle = new FacDocumentodetalle();
                facdetalle.setCfgProducto(productoSeleccionado);
                //el valor del documento master no es el definitivo
                facdetalle.setFacDocumentodetallePK(new FacDocumentodetallePK(productoSeleccionado.getIdProducto(), 1, 1));
                facdetalle.setCantidad(1);
                facdetalle.setDescuento(0);
                float aux = productoSeleccionado.getPrecio();
                aux = Redondear(aux, 0);
                facdetalle.setValorUnitario(aux);
                facdetalle.setValorTotal(facdetalle.getCantidad() * facdetalle.getValorUnitario());
                listaDetalle.add(facdetalle);
            } else {
                subtotal -= facdetalle.getValorTotal();
                facdetalle.setCantidad(facdetalle.getCantidad() + 1);
                facdetalle.setValorTotal(facdetalle.getCantidad() * facdetalle.getValorUnitario());
            }
            subtotal += facdetalle.getValorTotal();
            calcularImpuesto();
            calcularTotalFactura();
            RequestContext.getCurrentInstance().update("IdFormFactura:IdTableItemFactura");
            RequestContext.getCurrentInstance().update("IdFormFactura:IdSubTotal");
        }
    }

    private FacDocumentodetalle obtenerItemEnLista(CfgProducto cfgProducto) {
        for (FacDocumentodetalle documentodetalle : listaDetalle) {
            if (documentodetalle.getCfgProducto().equals(cfgProducto)) {
                return documentodetalle;
            }
        }
        return null;
    }

    public void quitarItem(ActionEvent actionEvent) {
        FacDocumentodetalle item = (FacDocumentodetalle) actionEvent.getComponent().getAttributes().get("item");
        listaDetalle.remove(item);
        subtotal -= item.getValorTotal();
        if (listaDetalle.isEmpty()) {
            subtotal = 0;
            totalDescuento = 0;
        }
        calcularTotalDescuento();
        calcularImpuesto();
        calcularTotalFactura();
        RequestContext.getCurrentInstance().update("IdFormFactura:IdTableItemFactura");
        RequestContext.getCurrentInstance().update("IdFormFactura:IdSubTotal");
    }

    public void onCellEdit(CellEditEvent event) {
        int index = event.getRowIndex();
        subtotal -= listaDetalle.get(index).getValorTotal();
        listaDetalle.get(index).setValorTotal(listaDetalle.get(index).getCantidad() * listaDetalle.get(index).getValorUnitario());
        float porcentaje = listaDetalle.get(index).getDescuento() / (float) 100;
        float descuento = porcentaje * listaDetalle.get(index).getValorTotal();
        descuento = Redondear(descuento, 0);
        listaDetalle.get(index).setValorDescuento(descuento);
        subtotal += listaDetalle.get(index).getValorTotal();
        calcularImpuesto();
        calcularTotalDescuento();
        calcularTotalFactura();
        RequestContext.getCurrentInstance().update("IdFormFactura:IdSubTotal");
    }

    public void updateTabla() {
        RequestContext.getCurrentInstance().update("IdFormFactura:IdTableItemFactura");
    }

    private void calcularTotalDescuento() {
        totalDescuento = 0;
        for (FacDocumentodetalle documentodetalle : listaDetalle) {
            totalDescuento += documentodetalle.getValorDescuento();
        }
    }

    private void calcularImpuesto() {
        float aux;
        for (CfgImpuesto impuesto : listaImpuestos) {
            aux = impuesto.getPorcentaje() * subtotal / (float) 100;
            aux = Redondear(aux, 0);
            impuesto.setTotalImpuesto(aux);
        }
    }

    private void calcularTotalFactura() {
        totalFactura = subtotal - totalDescuento;
        for (CfgImpuesto impuesto : listaImpuestos) {
            totalFactura += impuesto.getTotalImpuesto();
        }
    }

    public float Redondear(float pNumero, int pCantidadDecimales) {
        BigDecimal value = new BigDecimal(pNumero);
        value = value.setScale(pCantidadDecimales, RoundingMode.HALF_UP);
        return value.floatValue();
    }

    private boolean validarCampos(CfgDocumento documento) {
        boolean ban = true;
        if (clienteSeleccionado == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay cliente seleccionado"));
            return false;
        }
        if (listaDetalle.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay items de factura"));
            return false;
        }
        if (documento.getFinDocumento() < documento.getActDocumento()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ha llegado al limite de la creacion de factura. Revice la configuracion de documentos"));
            return false;
        }
        if (documentosmasterFacade.buscarBySedeAndDocumentoAndNum(sedeActual, documento.getIdDoc(), documento.getActDocumento()) != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Consecutivo de factura duplicado. Revice la configuracion de documentos"));
            return false;
        }
        return ban;
    }

    public void guardarFactura() {
        CfgDocumento documento = documentoFacade.buscarDocumentoDeFacturaBySede(sedeActual);
        if (documento == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay un documento existente ó sin finalizar aplicado a factura"));
            return;
        }
        documento.setActDocumento(documento.getActDocumento() + 1);
        if (!validarCampos(documento)) {
            return;
        }
        try {
            FacDocumentosmaster documentosmaster = new FacDocumentosmaster();
            documentosmaster.setFacDocumentosmasterPK(new FacDocumentosmasterPK(documento.getIdDoc(), documento.getActDocumento()));
            documentosmaster.setCfgdocumento(documento);
            documentosmaster.setCfgclienteidCliente(clienteSeleccionado);
            documentosmaster.setCfgempresasedeidSede(sedeActual);
            documentosmaster.setDescuento(totalDescuento);
            documentosmaster.setFecCrea(new Date());
            documentosmaster.setSegusuarioidUsuario(usuarioActual);
            documentosmaster.setSubtotal(subtotal);
            documentosmaster.setTotalFactura(totalFactura);
            documentosmaster.setEstado("PENDIENTE");
            documentosmasterFacade.create(documentosmaster);
            if (documento.getActDocumento() == documento.getFinDocumento()) {
                documento.setFinalizado(true);
                documento.setActivo(false);
            }
            documentoFacade.edit(documento);
            for (FacDocumentodetalle documentodetalle : listaDetalle) {
                documentodetalle.setFacDocumentodetallePK(new FacDocumentodetallePK(documentodetalle.getCfgProducto().getIdProducto(), documentosmaster.getFacDocumentosmasterPK().getCfgdocumentoidDoc(), documentosmaster.getFacDocumentosmasterPK().getNumDocumento()));
                documentodetalle.setFacDocumentosmaster(documentosmaster);
                documentodetalleFacade.create(documentodetalle);
            }
            for (CfgImpuesto impuesto : listaImpuestos) {
                FacDocumentoimpuesto documentoimpuesto = new FacDocumentoimpuesto();
                documentoimpuesto.setFacDocumentoimpuestoPK(new FacDocumentoimpuestoPK(impuesto.getIdImpuesto(), documentosmaster.getFacDocumentosmasterPK().getCfgdocumentoidDoc(), documentosmaster.getFacDocumentosmasterPK().getNumDocumento()));
                documentoimpuesto.setCfgImpuesto(impuesto);
                documentoimpuesto.setFacDocumentosmaster(documentosmaster);
                documentoimpuesto.setValorImpuesto(impuesto.getTotalImpuesto());
                documentoimpuesto.setPorcentajeImpuesto(impuesto.getPorcentaje());
                documentoimpuestoFacade.create(documentoimpuesto);
            }
            for (CfgFormapago formapago : listaFormapagos) {
                if (formapago.getSubtotal() > 0) {
                    FacDocuementopago docuementopago = new FacDocuementopago();
                    docuementopago.setFacDocuementopagoPK(new FacDocuementopagoPK(formapago.getIdFormaPago(), documentosmaster.getFacDocumentosmasterPK().getCfgdocumentoidDoc(), documentosmaster.getFacDocumentosmasterPK().getNumDocumento()));
                    docuementopago.setCfgFormapago(formapago);
                    docuementopago.setFacDocumentosmaster(documentosmaster);
                    docuementopago.setValorPago(formapago.getSubtotal());
                    docuementopagoFacade.create(docuementopago);
                }
            }
            documentoActual = documentosmaster;
            limpiarFormulario();
            RequestContext.getCurrentInstance().execute("PF('dlgFormaPago').hide()");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion", "Factura creada"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Factura no creada"));
        }
    }

    public void facturacion() {
        if (!listaDetalle.isEmpty()) {
            RequestContext.getCurrentInstance().execute("PF('dlgFormaPago').show()");
            RequestContext.getCurrentInstance().update("FormModalFactura");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay items de factura"));
        }
    }

    public void impresion() {
        if (documentoActual != null) {
            RequestContext.getCurrentInstance().execute("PF('dlgResult').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay una factura reciente"));
        }
    }

    public void onRowEdit(RowEditEvent event) {
        CfgFormapago formapago = (CfgFormapago) event.getObject();
        float aux = totalFormaPago();
        if (formapago.getSubtotal() < 0 || !validarFormaPago(aux)) {
            formapago.setSubtotal(0);
        }
        setEnableBtnPrint(totalFactura == aux);
    }

    public void onRowCancel(RowEditEvent event) {
//        FacesMessage msg = new FacesMessage("Edit Cancelled", ((CfgFormapago) event.getObject()).getNomFormaPago());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
        CfgFormapago formapago = (CfgFormapago) event.getObject();
        formapago.setSubtotal(0);
        float aux = totalFormaPago();
        setEnableBtnPrint(totalFactura == aux);
    }

    private float totalFormaPago() {
        float aux = 0;
        for (CfgFormapago formapago : listaFormapagos) {
            aux += formapago.getSubtotal();
        }
        return aux;
    }

    private boolean validarFormaPago(float aux) {
        return aux <= totalFactura;
    }

    public void actualizarTablaFormaPago() {
        RequestContext.getCurrentInstance().update("FormModalFactura:IdTableFormaPago");
        RequestContext.getCurrentInstance().update("FormModalFactura:IdBtnPrint");
    }

    public void generarPDF() {
//        guardarFactura();
        if (documentoActual != null) {
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
                        documentoActual.getFacDocumentosmasterPK().getCfgdocumentoidDoc(),
                        documentoActual.getFacDocumentosmasterPK().getNumDocumento()
                );
        byte[] bites = sedeActual.getLogo();
        if (bites == null) {
            bites = sedeActual.getCfgempresaidEmpresa().getLogo();
        }
        List<FacturaReporte> facturas = new ArrayList();
        FacturaReporte facturaReporte = new FacturaReporte();
        facturaReporte.setNumFactura(documento.getFacDocumentosmasterPK().getNumDocumento());
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
            InputStream logo = new ByteArrayInputStream(bites);
            parametros.put("logo", logo);
            parametros.put("empresa", sedeActual.getCfgempresaidEmpresa().getNomEmpresa());
            parametros.put("nit", sedeActual.getNumDocumento());
            parametros.put("cliente", documento.getCfgclienteidCliente().nombreCompleto());
            parametros.put("identificacionCliente", documento.getCfgclienteidCliente().getNumDoc());
            parametros.put("fecha", documento.getFecCrea());
            parametros.put("usuario", usuarioActual.nombreCompleto());
            parametros.put("identificacionUsuario", usuarioActual.getNumDoc());
            parametros.put("SUBREPORT_DIR", rutaReportes);
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

    private void limpiarFormulario() {
        listaDetalle.clear();
        clienteSeleccionado = clienteFacade.buscarClienteDefault(sedeActual.getCfgempresaidEmpresa());
        setListaImpuestos(impuestoFacade.buscarImpuestosPorTipoEmpresaAndSede(clienteSeleccionado.getCfgTipoempresaId(), sedeActual));
        listaFormapagos = formapagoFacade.buscarPorEmpresa(sedeActual.getCfgempresaidEmpresa());
        cargarInformacionCliente();
        setSubtotal(0);
        setTotalDescuento(0);
        setTotalFactura(0);
        setEnableBtnPrint(false);
        RequestContext.getCurrentInstance().update("IdFormFactura");
        RequestContext.getCurrentInstance().update("FormModalFactura");

    }

    public List<FacDocumentodetalle> getListaDetalle() {
        return listaDetalle;
    }

    public void setListaDetalle(List<FacDocumentodetalle> listaDetalle) {
        this.listaDetalle = listaDetalle;
    }

    public CfgCliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(CfgCliente clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
    }

    public List<CfgCliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<CfgCliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public String getIdentificacionCliente() {
        return identificacionCliente;
    }

    public void setIdentificacionCliente(String identificacionCliente) {
        this.identificacionCliente = identificacionCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
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

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getTotalDescuento() {
        return totalDescuento;
    }

    public void setTotalDescuento(float totalDescuento) {
        this.totalDescuento = totalDescuento;
    }

    public List<CfgImpuesto> getListaImpuestos() {
        return listaImpuestos;
    }

    public void setListaImpuestos(List<CfgImpuesto> listaImpuestos) {
        this.listaImpuestos = listaImpuestos;
    }

    public float getTotalFactura() {
        return totalFactura;
    }

    public void setTotalFactura(float totalFactura) {
        this.totalFactura = totalFactura;
    }

    public List<CfgFormapago> getListaFormapagos() {
        return listaFormapagos;
    }

    public void setListaFormapagos(List<CfgFormapago> listaFormapagos) {
        this.listaFormapagos = listaFormapagos;
    }

    public int getTipoImpresion() {
        return tipoImpresion;
    }

    public void setTipoImpresion(int tipoImpresion) {
        this.tipoImpresion = tipoImpresion;
    }

    public boolean isEnableBtnPrint() {
        return enableBtnPrint;
    }

    public void setEnableBtnPrint(boolean enableBtnPrint) {
        this.enableBtnPrint = enableBtnPrint;
    }
}
