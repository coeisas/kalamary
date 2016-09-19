/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import entities.CfgCliente;
import entities.CfgDocumento;
import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.CfgKitproductodetalle;
import entities.CfgKitproductomaestro;
import entities.CfgMovInventarioDetalle;
import entities.CfgMovInventarioMaestro;
import entities.CfgProducto;
import entities.CntMovdetalle;
import entities.FacDocuementopago;
import entities.FacDocumentodetalle;
import entities.FacDocumentoimpuesto;
import entities.FacDocumentosmaster;
import entities.InvConsolidado;
import entities.InvMovimiento;
import entities.InvMovimientoDetalle;
import entities.InvMovimientoDetallePK;
import entities.InvMovimientoPK;
import entities.SegUsuario;
import facades.CfgClienteFacade;
import facades.CfgDocumentoFacade;
import facades.CfgKitproductodetalleFacade;
import facades.CfgMovInventarioDetalleFacade;
import facades.CfgMovInventarioMaestroFacade;
import facades.CntMovdetalleFacade;
import facades.FacDocuementopagoFacade;
import facades.FacDocumentodetalleFacade;
import facades.FacDocumentoimpuestoFacade;
import facades.FacDocumentosmasterFacade;
import facades.InvConsolidadoFacade;
import facades.InvMovimientoDetalleFacade;
import facades.InvMovimientoFacade;
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
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang.StringUtils;
import utilities.AuxilarMovInventario;
import utilities.FacturaDetalleReporte;
import utilities.FacturaImpuestoReporte;
import utilities.FacturaPagoReporte;
import utilities.FacturaReporte;
import utilities.LazyFacturaEspecialDataModel;

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

    private List<SelectItem> listaTipoFactura;//los tipos de factura son: normal y remision.
    private int tipoFactura;

    private List<AuxilarMovInventario> listaAuxiliarInventario;

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
    @EJB
    CfgKitproductodetalleFacade kitDetalleFacade;
    @EJB
    InvConsolidadoFacade consolidadoInventarioFacade;
    @EJB
    CfgMovInventarioMaestroFacade movInventarioMaestroFacade;
    @EJB
    CfgMovInventarioDetalleFacade movInventarioDetalleFacade;
    @EJB
    InvMovimientoFacade inventarioMovimientoMaestroFacade;
    @EJB
    InvMovimientoDetalleFacade inventarioMovimientoDetalleFacade;
    @EJB
    CntMovdetalleFacade cntMovdetalleFacade;

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
            listaTipoFactura = new ArrayList();
            SelectItem aux = new SelectItem(1, "NORMAL");
            listaTipoFactura.add(aux);
            aux = new SelectItem(2, "ESPECIAL");
            listaTipoFactura.add(aux);
            aux = new SelectItem(3, "SEPARADO");
            listaTipoFactura.add(aux);
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
        if (sedeActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha encontrado informacion de la sede"));
            return;
        }
        if (tipoFactura == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha Seleccionado el tipo de factura"));
            return;
        }
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
                    CfgDocumento documento;
                    if (tipoFactura == 1) {
                        documento = documentoFacade.buscarDocumentoDeFacturaBySede(sedeActual);
                    } else if (tipoFactura==2){
                        documento = documentoFacade.buscarDocumentoDeRemisionEspecialBySede(sedeActual);
                    }else{
                       documento = documentoFacade.buscarDocumentoDeSeparadoBySede(sedeActual);
                     }
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
            if (tipoFactura == 1) {//muestra las facturas normales
                listadoFacturas = new LazyFacturaDataModel(documentosmasterFacade, sedeActual, clienteSeleccionado, fechaIni, fechaFin, numFactura,1);
            } else if (tipoFactura==2){
                listadoFacturas = new LazyFacturaEspecialDataModel(documentosmasterFacade, sedeActual, clienteSeleccionado, fechaIni, fechaFin, numFactura);
            }else if (tipoFactura==3){
                listadoFacturas = new LazyFacturaDataModel(documentosmasterFacade, sedeActual, clienteSeleccionado, fechaIni, fechaFin, numFactura,7);
            }
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
                    ruta = servletContext.getRealPath("/facturacion/reportes/facturaTicket.jasper");
                    break;
                case 2:
                    ruta = servletContext.getRealPath("/facturacion/reportes/facturaCarta.jasper");
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
        facturaReporte.setTotalFactura(documento.getTotal());
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
            String rutaReportes = servletContext.getRealPath("/facturacion/reportes/");//ubicacion para los subreportes
            Map<String, Object> parametros = new HashMap<>();
            if (bites != null) {
                InputStream logo = new ByteArrayInputStream(bites);
                parametros.put("logo", logo);
            }
            CfgEmpresa empresa = sedeActual.getCfgempresaidEmpresa();
            switch (documento.getCfgDocumento().getCfgAplicaciondocumentoIdaplicacion().getCodaplicacion()) {
                case "1":
                    parametros.put("title", "VENTA No");
                    parametros.put("nit", empresa.getCfgTipodocempresaId().getDocumentoempresa() + " " + sedeActual.getNumDocumento() + " " + empresa.getCfgTipoempresaId().getDescripcion());
                    parametros.put("resdian", documento.getCfgDocumento().getResDian());
                    break;
                case "6":
                    parametros.put("title", "VENTA No");
                    break;
            }
            parametros.put("empresa", empresa.getNomEmpresa() + " - " + sedeActual.getNomSede());
            parametros.put("direccion", sedeActual.getDireccion());
            String telefono = sedeActual.getTel1();
            if (sedeActual.getTel2() != null && !sedeActual.getTel2().isEmpty()) {
                telefono = telefono + "-".concat(sedeActual.getTel2());
            }
            parametros.put("telefono", telefono);
            parametros.put("cliente", documento.getCfgclienteidCliente().nombreCompleto());
            if (tipoImpresion != 2) { //no es impresion carta
                parametros.put("identificacionCliente", documento.getCfgclienteidCliente().getCfgTipoidentificacionId().getAbreviatura() + " " + documento.getCfgclienteidCliente().getNumDoc());
            } else {
                parametros.put("identificacionCliente", documento.getCfgclienteidCliente().getNumDoc());
                parametros.put("tipoDoc", documento.getCfgclienteidCliente().getCfgTipoidentificacionId().getAbreviatura());
            }
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
            String presentacion = "";
            if (detalle.getTipoDescuento() != null && detalle.getTipoDescuento() == 1) {
                presentacion = "%";
            }
            facturaDetalleReporte.setPresentacionDescuento(presentacion);
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
            CfgDocumento documentoEntradaInventario = documentoFacade.buscarDocumentoInventarioEntradaBySede(sedeActual);
            if (documentoEntradaInventario == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro un documento de entrada de inventario vigente"));
                return;
            }
            try {
                documentoSeleccionado.setEstado("ANULADA");
                documentosmasterFacade.edit(documentoSeleccionado);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Factura anulada"));
                listadoFacturas = new LazyFacturaDataModel(documentosmasterFacade, sedeActual, clienteSeleccionado, fechaIni, fechaFin, numFactura,tipoFactura==1?1:7);
                RequestContext.getCurrentInstance().update("IdFormListadoFacturas");

                //SE CREA UN MOVIMIENTO DE INVENTARIO (ENTRADA) PARA LA FACTURA ANULADA
                //ban sera true cuando el detalle incluya al menos un kit O un producto que no sea un servicio
                boolean ban = false;
                List<FacDocumentodetalle> listaDetalleDocumento = documentoSeleccionado.getFacDocumentodetalleList();
                if (listaDetalleDocumento.isEmpty()) {
                    listaDetalleDocumento = documentodetalleFacade.buscarByDocumentoMaster(documentoSeleccionado);
                }
                for (FacDocumentodetalle fd : listaDetalleDocumento) {
                    if (!fd.getCfgProducto().getEsServicio() || fd.getCfgProducto().getEsKit()) {
                        ban = true;
                        break;
                    }
                }
                //si ban = true. se creara un movimiento de entrada en el inventario.
                if (ban) {
                    crearMovimientoInventario(listaDetalleDocumento, documentoSeleccionado);
                }

                //ACTULIZAR INVENTARIO CONSOLIDADO
                llenarListaAuxiliarInventario(listaDetalleDocumento);
                for (AuxilarMovInventario auxilarMovInventario : listaAuxiliarInventario) {
                    InvConsolidado inventarioConsolidado = consolidadoInventarioFacade.buscarBySedeAndProducto(sedeActual, auxilarMovInventario.getProducto());
                    inventarioConsolidado.setExistencia(inventarioConsolidado.getExistencia() + auxilarMovInventario.getCantidad());
                    inventarioConsolidado.setFechaUltEntrada(new Date());
                    inventarioConsolidado.setEntradas(inventarioConsolidado.getEntradas() + auxilarMovInventario.getCantidad());
                    consolidadoInventarioFacade.edit(inventarioConsolidado);
                }
                //MODIFICAR LA INFORMACION DE CONTABILIDAD DE FACTURA ANULADA
                List<CntMovdetalle> listaCntMovdetalle = cntMovdetalleFacade.buscarPorDocumentoMaster(documentoSeleccionado);
                float vlr = 0;
                boolean debito;
                float tot;
                for (CntMovdetalle cm : listaCntMovdetalle) {
                    vlr = cm.getDebito() == null ? 0 : cm.getDebito();
                    if (vlr != 0) {
                        debito = true;
                        cm.setCredito(vlr);
                        cm.setDebito(0f);
                        tot = cm.getDebito() - cm.getCredito();
                        cm.setTotal(tot);
                        cntMovdetalleFacade.edit(cm);
                    } else {
                        debito = false;
                        vlr = cm.getCredito() == null ? 0 : cm.getCredito();
                    }
                    if (!debito && vlr != 0) {
                        cm.setCredito(0f);
                        cm.setDebito(vlr);
                        tot = cm.getDebito() - cm.getCredito();
                        cm.setTotal(tot);                        
                        cntMovdetalleFacade.edit(cm);
                    }
                }
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se logro anular la factura"));
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha Seleccionada la factura"));
        }
    }

    public void llenarListaAuxiliarInventario(List<FacDocumentodetalle> listaDetalleDocumento) {
        listaAuxiliarInventario = new ArrayList();
        //iteracion de la lista detalle del documento. factura norma o especial
        for (FacDocumentodetalle detalle : listaDetalleDocumento) {
            CfgProducto producto = detalle.getCfgProducto();
            if (!producto.getEsServicio() && !producto.getEsKit()) {//si el producto no es un servicio y tampoco un kit. se inserta el producto a la listaAuxiliar
                insertarItemsListaAuxiliarInventario(detalle.getCfgProducto(), detalle.getCantidad());
            } else if (producto.getEsKit()) {//si el producto es un kit se comprueba existencia de cada elemento que lo conforma
                CfgKitproductomaestro kit = producto.getCfgkitproductomaestroidKit();
                List<CfgKitproductodetalle> listadetallekit = kitDetalleFacade.buscarByMaestro(kit);
                for (CfgKitproductodetalle detalleKit : listadetallekit) {
                    producto = detalleKit.getCfgProducto();
                    if (!producto.getEsServicio()) {
                        insertarItemsListaAuxiliarInventario(producto, detalle.getCantidad() * (int) detalleKit.getCant());
                    }
                }
            }
        }
    }

    public void insertarItemsListaAuxiliarInventario(CfgProducto producto, int cantidad) {
        int indx = buscarProductoEnListaAuxiliar(producto);//se recupera el index del producto en la lista. si es que ya esta insertado
        if (indx != -1) {//si el producto ya esta insertado se actualizara la cantidad en la lista auxiliar
            int cantidadPrevia = listaAuxiliarInventario.get(indx).getCantidad();
            //la nueva cantidad se ve comprometida por lo requerido en el separado como la cantidad establecida en el kit
            int nuevaCantidad = cantidadPrevia + cantidad;
            listaAuxiliarInventario.get(indx).setCantidad(nuevaCantidad);
        } else {//se registra por primera vez el producto en la lista auxiliar
            AuxilarMovInventario auxilarMovInventario = new AuxilarMovInventario();
            auxilarMovInventario.setProducto(producto);
            auxilarMovInventario.setCantidad(cantidad);
            listaAuxiliarInventario.add(auxilarMovInventario);
        }
    }

    //devuelve el index del item del producto si esta se encontraba insertado. de lo contrario devuelve un -1
    private int buscarProductoEnListaAuxiliar(CfgProducto producto) {
        int indx = -1;
        if (listaAuxiliarInventario.isEmpty()) {
            return indx;
        }
        int i = 0;
        for (AuxilarMovInventario auxilarMovInventario : listaAuxiliarInventario) {
            if (auxilarMovInventario.getProducto().equals(producto)) {
                indx = i;
                break;
            }
            i++;
        }
        return indx;
    }

    private void crearMovimientoInventario(List<FacDocumentodetalle> listaDetalle, FacDocumentosmaster documentosmaster) {
//        BUSCA EL DOCUMENTO APLICADO AL MOVIMIENTO DE INVENTARIO DE ENTRADA
        CfgDocumento documento = documentoFacade.buscarDocumentoInventarioEntradaBySede(sedeActual);
        if (documento.getActDocumento() == 0) {
            documento.setActDocumento(documento.getIniDocumento());
        } else {
            documento.setActDocumento(documento.getActDocumento() + 1);
        }
        CfgMovInventarioMaestro movInventarioMaestro = movInventarioMaestroFacade.buscarMovimientoEntrada();
        CfgMovInventarioDetalle movInventarioDetalle = movInventarioDetalleFacade.buscarAjustePositivoByMaestro(movInventarioMaestro);
        try {
//            CREACION DEL MAESTRO MOVIMIENTO 
            InvMovimiento invMovimientoMaestro = new InvMovimiento();
            invMovimientoMaestro.setInvMovimientoPK(new InvMovimientoPK(documento.getIdDoc(), documento.getActDocumento()));
            invMovimientoMaestro.setCfgDocumento(documento);
            invMovimientoMaestro.setCfgempresasedeidSede(sedeActual);
            invMovimientoMaestro.setCfgmovinventariodetalleidMovInventarioDetalle(movInventarioDetalle);
            invMovimientoMaestro.setDescuento(documentosmaster.getDescuento());
            invMovimientoMaestro.setFacDocumentosmaster(documentosmaster);
            invMovimientoMaestro.setFecha(new Date());
//            invMovimientoMaestro.setIva(totalIva);
            invMovimientoMaestro.setSegusuarioidUsuario(usuarioActual);
            invMovimientoMaestro.setSubtotal(documentosmaster.getSubtotal());
            invMovimientoMaestro.setTotal(documentosmaster.getTotal());
            inventarioMovimientoMaestroFacade.create(invMovimientoMaestro);
            if (documento.getActDocumento() >= documento.getFinDocumento()) {//dependiendo de la situacion se finaliza el documento aplicado al movimiento de salida
                documento.setFinalizado(true);
            }
            documentoFacade.edit(documento);
            List<InvMovimientoDetalle> listaItemsInventarioMovimiento = new ArrayList();
//            CREACION DEL DETALLE MOVIMIENTO
            for (FacDocumentodetalle detalleFactura : listaDetalle) {
                if (!detalleFactura.getCfgProducto().getEsServicio()) {//si el producto no es un servicio se incluye en el detalle del movimiento de entrada
                    InvMovimientoDetalle detalle = new InvMovimientoDetalle();
                    detalle.setInvMovimientoDetallePK(new InvMovimientoDetallePK(invMovimientoMaestro.getInvMovimientoPK().getCfgdocumentoidDoc(), invMovimientoMaestro.getInvMovimientoPK().getNumDoc(), detalleFactura.getCfgProducto().getIdProducto()));
                    detalle.setInvMovimiento(invMovimientoMaestro);
                    detalle.setCantidad(detalleFactura.getCantidad());
                    detalle.setCfgProducto(detalleFactura.getCfgProducto());
                    detalle.setCostoAdquisicion(detalleFactura.getValorUnitario());
                    detalle.setDescuento(detalleFactura.getDescuento());
                    detalle.setCostoFinal(detalleFactura.getValorTotal());
                    inventarioMovimientoDetalleFacade.create(detalle);
                    listaItemsInventarioMovimiento.add(detalle);
                }
            }
//            SE INCLUYE EL DETALLE DEL MOVIMIENTO AL MAESTRO
            invMovimientoMaestro.setInvMovimientoDetalleList(listaItemsInventarioMovimiento);
            inventarioMovimientoMaestroFacade.edit(invMovimientoMaestro);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha registrado el movimiento de entrada para esta factura solo se incremento las unidades en inventario. Compruebe que exista un documento vigente para entrada de inventario"));
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

    public List<SelectItem> getListaTipoFactura() {
        return listaTipoFactura;
    }

    public int getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(int tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

}
