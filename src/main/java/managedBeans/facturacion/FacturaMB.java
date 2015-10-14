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
import entities.CfgFormapago;
import entities.CfgImpuesto;
import entities.CfgKitproductodetalle;
import entities.CfgKitproductomaestro;
import entities.CfgMovInventarioDetalle;
import entities.CfgMovInventarioMaestro;
import entities.CfgMunicipio;
import entities.CfgMunicipioPK;
import entities.CfgProducto;
import entities.CfgTipoempresa;
import entities.InvConsolidado;
import entities.FacCaja;
import entities.FacCarteraCliente;
import entities.FacCarteraClientePK;
import entities.FacCarteraDetalle;
import entities.FacCarteraDetallePK;
import entities.FacDocuementopago;
import entities.FacDocuementopagoPK;
import entities.FacDocumentodetalle;
import entities.FacDocumentodetallePK;
import entities.FacDocumentoimpuesto;
import entities.FacDocumentoimpuestoPK;
import entities.FacDocumentosmaster;
import entities.FacDocumentosmasterPK;
import entities.FacMovcaja;
import entities.FacMovcajadetalle;
import entities.FacMovcajadetallePK;
import entities.InvMovimiento;
import entities.InvMovimientoDetalle;
import entities.InvMovimientoDetallePK;
import entities.InvMovimientoPK;
import entities.SegUsuario;
import facades.CfgClienteFacade;
import facades.CfgDocumentoFacade;
import facades.CfgFormapagoFacade;
import facades.CfgImpuestoFacade;
import facades.CfgKitproductodetalleFacade;
import facades.CfgMovInventarioDetalleFacade;
import facades.CfgMovInventarioMaestroFacade;
import facades.CfgMunicipioFacade;
import facades.CfgProductoFacade;
import facades.CfgTipoempresaFacade;
import facades.CfgTipoidentificacionFacade;
import facades.FacCarteraClienteFacade;
import facades.FacCarteraDetalleFacade;
import facades.FacDocuementopagoFacade;
import facades.FacDocumentodetalleFacade;
import facades.FacDocumentoimpuestoFacade;
import facades.FacDocumentosmasterFacade;
import facades.FacMovcajaFacade;
import facades.FacMovcajadetalleFacade;
import facades.InvConsolidadoFacade;
import facades.InvMovimientoDetalleFacade;
import facades.InvMovimientoFacade;
import facades.SegUsuarioFacade;
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
import javax.el.ELException;
import javax.faces.event.PhaseId;
import javax.faces.model.SelectItem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import utilities.FacturaCuotasReporte;
import utilities.FacturaDetalleReporte;
import utilities.FacturaImpuestoReporte;
import utilities.FacturaPagoReporte;
import utilities.LazyClienteDataModel;
import utilities.LazyCotizacionDataModel;

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
    private float totalUSD;
    private float utilidad;
    private float cuotaInicial;
//    private int numCuotas;
    private Date fechaLimite;
    private String observacion;
    private SegUsuario vendedorSeleccionado;
    private LazyDataModel<CfgProducto> listaProducto;
    private List<FacDocumentodetalle> listaDetalle;
    private LazyDataModel<CfgCliente> listaClientes;
    private LazyDataModel<FacDocumentosmaster> listadoCotizacion;
    private List<CfgImpuesto> listaImpuestos;
    private List<CfgFormapago> listaFormapagos;
    private FacCaja cajaUsuario;//caja asignada al usuario
    private FacMovcaja movimientoCajaMaster;//contiene informacion del maestro del movimiento. Se crea uno cada vez que se habre caja. cuando se cierra se habilita
    private List<SegUsuario> listaVendedor;
    private String documentoVendedor;
    private String nombreVendedor;

    private String display;//style para ocultar o no la informacion adicional de separados: cuota inicial y numero de cuotas

    private List<SelectItem> listaTipoFactura;//los tipos de factura son: normal y remision.
    private int tipoFactura;

    private int tipoImpresion;
    private CfgEmpresa empresaActual;
    private CfgEmpresasede sedeActual;
    private CfgCliente clienteSeleccionado;
    private CfgProducto productoSeleccionado;
    private FacDocumentosmaster cotizacionSeleccionada;
    private SegUsuario usuarioActual;
    private FacDocumentosmaster documentoActual;
    private boolean enableBtnPrint;

    private SesionMB sesionMB;

//--------------------------------------------------------
//-------------PROPIEDADES CLIENTE NUEVO
//--------------------------------------------------------
    private String idDepartamento;
    private String idMunicipio;
    private int idIdentificacion;
    private String numIdentificacion;
    private int idTipoCliente;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private UploadedFile file;
    private String direccion;
    private String telefono;
    private String mail;
    private Date fechaNacimiento;
    private String tarjetaMembresia;
    private float cupoCredito;
    private StreamedContent image;

    private CfgCliente clienteSeleccionadoModal;

    private List<CfgMunicipio> listaMunicipios;

    @EJB
    SegUsuarioFacade usuarioFacade;
    @EJB
    CfgClienteFacade clienteFacade;
    @EJB
    CfgImpuestoFacade impuestoFacade;
    @EJB
    CfgDocumentoFacade documentoFacade;
    @EJB
    CfgProductoFacade productoFacade;
    @EJB
    CfgKitproductodetalleFacade kitproductodetalleFacade;
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
    @EJB
    FacMovcajaFacade movcajamaestroFacade;
    @EJB
    FacMovcajadetalleFacade movcajadetalleFacade;
    @EJB
    CfgMunicipioFacade municipioFacade;
    @EJB
    CfgTipoidentificacionFacade tipoidentificacionFacade;
    @EJB
    CfgTipoempresaFacade tipoClienteFacade;
    @EJB
    InvConsolidadoFacade invConsolidadoFacade;
    @EJB
    InvMovimientoFacade inventarioMovimientoMaestroFacade;
    @EJB
    InvMovimientoDetalleFacade inventarioMovimientoDetalleFacade;
    @EJB
    InvConsolidadoFacade consolidadoFacade;
    @EJB
    CfgMovInventarioDetalleFacade movInventarioDetalleFacade;
    @EJB
    CfgMovInventarioMaestroFacade movInventarioMaestroFacade;
    @EJB
    FacCarteraClienteFacade carteraClienteFacade;
    @EJB
    FacCarteraDetalleFacade carteraDetalleFacade;

    public FacturaMB() {

    }

    @PostConstruct
    private void init() {
        //si es super usuario o admin permitir escoger la empresa y la sede
        tipoImpresion = 1;
        setEnableBtnPrint(false);
        listaMunicipios = new ArrayList();
        setListaDetalle((List<FacDocumentodetalle>) new ArrayList());
        setListaImpuestos((List<CfgImpuesto>) new ArrayList());
        FacesContext context = FacesContext.getCurrentInstance();
        sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
        sedeActual = sesionMB.getSedeActual();
        empresaActual = sesionMB.getEmpresaActual();
//        numCuotas = 2;
        fechaLimite = new Date();
        display = "none";
        if (empresaActual != null) {
            actualizarListadoClientes();
            listaProducto = new LazyProductosModel(empresaActual, null, null, null, productoFacade);
            RequestContext.getCurrentInstance().update("FormModalProducto");
            clienteSeleccionado = clienteFacade.buscarClienteDefault(empresaActual);
            setListaImpuestos(impuestoFacade.buscarImpuestosPorEmpresa(empresaActual));
            listaFormapagos = formapagoFacade.buscarPorEmpresa(empresaActual);
            cargarInformacionCliente();
            listaTipoFactura = new ArrayList();
            SelectItem aux = new SelectItem(1, "NORMAL");
            listaTipoFactura.add(aux);
            aux = new SelectItem(2, "ESPECIAL");
            listaTipoFactura.add(aux);
            aux = new SelectItem(3, "SEPARADO");
            listaTipoFactura.add(aux);
            tipoFactura = 1;
        } else {
            listaImpuestos = new ArrayList();
        }
        if (usuarioActual != null) {
            cajaUsuario = usuarioActual.getFaccajaidCaja();
            if (cajaUsuario != null) {
                movimientoCajaMaster = movcajamaestroFacade.buscarMovimientoCaja(cajaUsuario);
            }
        }
    }

    public void buscarCliente() {
        if (!identificacionCliente.trim().isEmpty()) {
            clienteSeleccionado = clienteFacade.buscarPorIdentificacionAndIdEmpresa(identificacionCliente, empresaActual);
            if (clienteSeleccionado == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No se encontro cliente con esa identificacion"));
            }
        } else {
            clienteSeleccionado = null;
        }
        cargarInformacionCliente();
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
        RequestContext.getCurrentInstance().update("IdFormFactura");
    }

    public void validarTipoFacturacion() {
        if (getSedeActual() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro informacion de la sede"));
            return;
        }
        display = "none";
        CfgDocumento documento = null;
        if (tipoFactura == 1) {//se valida la existencia del documento aplicado a facturacion
            documento = documentoFacade.buscarDocumentoDeFacturaBySede(getSedeActual());
        } else if (tipoFactura == 2) {//se valida la existencia del documento aplicado a remision especial
            documento = documentoFacade.buscarDocumentoDeRemisionEspecialBySede(getSedeActual());
        } else if (tipoFactura == 3) {//se valida la existencia del documento aplicado a remision especial
            documento = documentoFacade.buscarDocumentoDeSeparadoBySede(getSedeActual());
            display = documento != null ? "block" : "none";
        }
        if (tipoFactura != 0 && documento == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay un documento que aplique al tipo de factura seleccionada"));
        }
        RequestContext.getCurrentInstance().update("FormModalFactura");
    }

    public void cargarModalProductos() {
        movimientoCajaMaster = movcajamaestroFacade.buscarMovimientoCaja(cajaUsuario);
        if (movimientoCajaMaster != null) {
            if (movimientoCajaMaster.getAbierta()) {
                RequestContext.getCurrentInstance().execute("PF('dlgProducto').show()");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La caja no esta abierta"));
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Abra la caja por primera vez"));
        }
    }

    public void cargarCotizaciones() {
        if (empresaActual != null) {
            listadoCotizacion = new LazyCotizacionDataModel(documentosmasterFacade, getSedeActual(), clienteSeleccionado);
            RequestContext.getCurrentInstance().update("FormBuscarCotizacion");
            RequestContext.getCurrentInstance().execute("PF('dlgCotizacion').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro informacion de la empresa"));
        }
    }

    public void cargarInformacionCotizacion() {
        listaDetalle.clear();
        subtotal = 0;
        totalDescuento = 0;
        if (cotizacionSeleccionada != null) {
            clienteSeleccionado = cotizacionSeleccionada.getCfgclienteidCliente();
            List<FacDocumentodetalle> lista = documentodetalleFacade.buscarByDocumentoMaster(cotizacionSeleccionada);
            cargarInformacionCliente();
            vendedorSeleccionado = cotizacionSeleccionada.getSegusuarioidUsuario1();
            cargarInformacionVendedor();
            for (FacDocumentodetalle detalle : lista) {
                CfgProducto producto = detalle.getCfgProducto();
                if (!producto.getEsServicio()) {
                    InvConsolidado consolidado = invConsolidadoFacade.buscarBySedeAndProducto(getSedeActual(), producto);
                    if (consolidado == null) {//el producto no esta en el inventario
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se tiene registro de " + producto.getNomProducto() + " en el inventario"));
                        return;
                    }
                    if (consolidado.getExistencia() == 0) {//no hay unidades disponibles
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay existencias de " + producto.getNomProducto()));
                        return;
                    }
                    //se determina como cantidad posible el total de las existencias en el inventario
                    detalle.setCantidadPosible(consolidado.getExistencia());
                }
                //el valor del documento master no es el definitivo
                detalle.setFacDocumentodetallePK(new FacDocumentodetallePK(producto.getIdProducto(), 1, 1));
                detalle.setPrecioOriginal(detalle.getValorUnitario());
                float descuento = detalle.getDescuento() * detalle.getValorTotal() / (float) 100;
                descuento = Redondear(descuento, 0);
                detalle.setValorDescuento(descuento);
                listaDetalle.add(detalle);
            }
            calcularSubtotal();
            calcularTotalDescuento();
            calcularImpuesto();
            calcularTotalFactura();
            calcularTotalUSD();
        }
        RequestContext.getCurrentInstance().update("IdFormFactura");
        RequestContext.getCurrentInstance().execute("PF('dlgCotizacion').hide()");
    }

    public void cargarInformacionCliente() {
        if (clienteSeleccionado != null) {
            setNombreCliente(clienteSeleccionado.nombreCompleto());
            setIdentificacionCliente(clienteSeleccionado.getNumDoc());
        } else {
            setNombreCliente(null);
        }
        listaDetalle.clear();
        listaFormapagos = formapagoFacade.buscarPorEmpresa(empresaActual);
        setSubtotal(0);
        setTotalDescuento(0);
        setTotalFactura(0);
        totalUSD = 0;
        setEnableBtnPrint(false);
        RequestContext.getCurrentInstance().execute("PF('dlgCliente').hide()");
        RequestContext.getCurrentInstance().update("IdFormFactura:IdNomCliente");
        RequestContext.getCurrentInstance().update("IdFormFactura:IdSubTotal");
        RequestContext.getCurrentInstance().update("IdFormFactura");
    }

    private void actualizarListadoClientes() {
        if (getSedeActual() != null) {
            listaClientes = new LazyClienteDataModel(clienteFacade, empresaActual);
        }
        RequestContext.getCurrentInstance().update("FormBuscarCliente");
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
            InvConsolidado consolidado = null;
            if (!productoSeleccionado.getEsServicio() && !productoSeleccionado.getEsKit()) {//si el producto seleccionado no es un servicio y tampoco un kit se tiene encuenta la existencia en el inventario
                consolidado = invConsolidadoFacade.buscarBySedeAndProducto(getSedeActual(), productoSeleccionado);
                if (consolidado == null) {//el producto no esta en el inventario
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se tiene registro de este producto en el inventario"));
                    return;
                }
                if (consolidado.getExistencia() == 0) {//no hay unidades disponibles
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay existencias de este producto"));
                    return;
                }
            }
            int cantidadPosibleKit = 0;
            if (productoSeleccionado.getEsKit()) {//si el producto es un kit. se busca existencia de cada elemento en el inventario
                CfgKitproductomaestro kitproductomaestro = productoSeleccionado.getCfgkitproductomaestroidKit();
                List<CfgKitproductodetalle> itemsKit = kitproductomaestro.getCfgKitproductodetalleList();
                if (itemsKit.isEmpty()) {
                    itemsKit = kitproductodetalleFacade.buscarByMaestro(kitproductomaestro);
                }
                boolean ban = true;
                int aux;
                int totalCantidadItemKit = 0;
                //se recorre cada item del kit buscando su existencia en inventario
                for (CfgKitproductodetalle detallekit : itemsKit) {
                    CfgProducto productokit = detallekit.getCfgProducto();
                    if (!productokit.getEsServicio()) {//si el item actual no es un servicio se tiene encuenta la existencia en el inventario                    
                        consolidado = invConsolidadoFacade.buscarBySedeAndProducto(getSedeActual(), productokit);
                        if (consolidado == null) {//el producto no esta en el inventario
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se tiene registro de " + productokit.getNomProducto() + " en el inventario"));
                            ban = false;
                            break;
                        }
                        if (consolidado.getExistencia() == 0) {//no hay unidades disponibles
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay existencias de " + productokit.getNomProducto()));
                            ban = false;
                            break;
                        }
                        totalCantidadItemKit = consolidado.getExistencia();
                        aux = totalCantidadItemKit / (int) detallekit.getCant();
                        if (aux == 0) {//se debe satisfacer completamente la cantidad requerida de un elemento del kit
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay existencias suficientes de " + productokit.getNomProducto()));
                            ban = false;
                            break;
                        }
                        if (cantidadPosibleKit == 0) {
                            cantidadPosibleKit = aux;
                        } else if (cantidadPosibleKit > aux) {
                            cantidadPosibleKit = aux;
                        }
                    }
                }
                if (!ban) {
                    return;
                }
            }
            FacDocumentodetalle facdetalle = obtenerItemEnLista(productoSeleccionado);
            if (facdetalle == null) {
                facdetalle = new FacDocumentodetalle();
                facdetalle.setCfgProducto(productoSeleccionado);
                if (!productoSeleccionado.getEsKit() && consolidado != null) {//se permite un consolidad nulo cuando el producto seleccionado corresponde a un servicio
                    //se determina como cantidad posible el total de las existencias en el inventario
                    facdetalle.setCantidadPosible(consolidado.getExistencia());
                } else if (productoSeleccionado.getEsKit()) {
                    facdetalle.setCantidadPosible(cantidadPosibleKit);
                }
                //el valor del documento master no es el definitivo
                facdetalle.setFacDocumentodetallePK(new FacDocumentodetallePK(productoSeleccionado.getIdProducto(), 1, 1));
                facdetalle.setCantidad(1);
                facdetalle.setDescuento(0);
                float aux = productoSeleccionado.getPrecio();
                aux = Redondear(aux, 0);
                facdetalle.setValorUnitario(aux);
                facdetalle.setPrecioOriginal(aux);
                facdetalle.setValorTotal(facdetalle.getCantidad() * facdetalle.getValorUnitario());
                listaDetalle.add(facdetalle);
            } else {
                int aux = facdetalle.getCantidad() + 1;
                if (aux > facdetalle.getCantidadPosible()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La cantidad insertada excede las existencias del producto"));
                    return;
                }
                subtotal -= facdetalle.getValorTotal();
                facdetalle.setCantidad(facdetalle.getCantidad() + 1);
                facdetalle.setValorTotal(facdetalle.getCantidad() * facdetalle.getValorUnitario());
            }
            subtotal += facdetalle.getValorTotal();
            calcularTotalDescuento();
            calcularImpuesto();
            calcularTotalFactura();
            calcularTotalUSD();
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
        calcularTotalUSD();
        RequestContext.getCurrentInstance().update("IdFormFactura:IdTableItemFactura");
        RequestContext.getCurrentInstance().update("IdFormFactura:IdSubTotal");
    }

    public void onCellEdit(CellEditEvent event) {
        int index = event.getRowIndex();
        if (listaDetalle.get(index).getValorUnitario() < listaDetalle.get(index).getPrecioOriginal()) {
            listaDetalle.get(index).setValorUnitario(listaDetalle.get(index).getPrecioOriginal());
        }
        subtotal -= listaDetalle.get(index).getValorTotal();
        if (!listaDetalle.get(index).getCfgProducto().getEsServicio()) {//si el producto seleccionado no es un servicio. Se valida la cantidad maxima permitida a ingresar
            //validacion donde se impide que la cantidad sea mayor a las existencias
            if (listaDetalle.get(index).getCantidad() > listaDetalle.get(index).getCantidadPosible()) {
                listaDetalle.get(index).setCantidad(listaDetalle.get(index).getCantidadPosible());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "Hay unicamente " + listaDetalle.get(index).getCantidadPosible() + " unidades disponibles"));
                RequestContext.getCurrentInstance().update("msg");
            }
        }
        listaDetalle.get(index).setValorTotal(listaDetalle.get(index).getCantidad() * listaDetalle.get(index).getValorUnitario());
        float porcentaje = listaDetalle.get(index).getDescuento() / (float) 100;
        float descuento = porcentaje * listaDetalle.get(index).getValorTotal();
        descuento = Redondear(descuento, 0);
        listaDetalle.get(index).setValorDescuento(descuento);
        subtotal += listaDetalle.get(index).getValorTotal();
        calcularTotalDescuento();
        calcularImpuesto();
        calcularTotalFactura();
        calcularTotalUSD();
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

    //se usa unicamente cuando se cargar una autorizacion
    private void calcularSubtotal() {
        subtotal = 0;
        for (FacDocumentodetalle documentodetalle : listaDetalle) {
            subtotal += documentodetalle.getValorTotal();
        }
    }

    private void calcularImpuesto() {
        float aux;
        for (CfgImpuesto impuesto : listaImpuestos) {
            aux = impuesto.getPorcentaje() * (subtotal - totalDescuento) / (float) 100;
            aux = Redondear(aux, 0);
            impuesto.setTotalImpuesto(aux);
        }
    }

    private void calcularTotalUtilidad() {
        utilidad = 0;
        for (FacDocumentodetalle documentodetalle : listaDetalle) {
            float costoProductoOriginal = documentodetalle.getCfgProducto().getCosto() != null ? documentodetalle.getCfgProducto().getCosto() : 0;
            costoProductoOriginal = Redondear(costoProductoOriginal, 0);
            float utilidadProducto = documentodetalle.getCantidad() * (documentodetalle.getValorUnitario() - costoProductoOriginal);
            utilidad += utilidadProducto;
        }
    }

    private void calcularTotalFactura() {
        totalFactura = subtotal - totalDescuento;
        for (CfgImpuesto impuesto : listaImpuestos) {
            totalFactura += impuesto.getTotalImpuesto();
        }
    }

    private void calcularTotalUSD() {
        if (movimientoCajaMaster != null) {
            if (movimientoCajaMaster.getAbierta()) {
                if (movimientoCajaMaster.getTrm() > 0) {
                    totalUSD = totalFactura / movimientoCajaMaster.getTrm();
                    totalUSD = Redondear(totalUSD, 0);
                } else {
                    totalUSD = 0;
                }
            } else {
                totalUSD = 0;
            }
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
        if (vendedorSeleccionado == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay vendedor seleccionado"));
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
        if (documentosmasterFacade.buscarBySedeAndDocumentoAndNum(getSedeActual(), documento.getIdDoc(), documento.getActDocumento()) != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Consecutivo de factura duplicado. Revice la configuracion de documentos"));
            return false;
        }
        return ban;
    }

    public void guardarFactura() {
        movimientoCajaMaster = movcajamaestroFacade.buscarMovimientoCaja(cajaUsuario);
        if (!movimientoCajaMaster.getAbierta()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La caja no esta abierta"));
            return;
        }
        if (tipoFactura == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione el tipo de Factura"));
            return;
        }
        CfgDocumento documento = null;
        if (tipoFactura == 1) {//se valida la existencia del documento aplicado a facturacion
            documento = documentoFacade.buscarDocumentoDeFacturaBySede(getSedeActual());
        } else if (tipoFactura == 2) {//se valida la existencia del documento aplicado a remision especial
            documento = documentoFacade.buscarDocumentoDeRemisionEspecialBySede(getSedeActual());
        } else if (tipoFactura == 3) {//se valida la existencia del documento aplicado a separado
            documento = documentoFacade.buscarDocumentoDeSeparadoBySede(getSedeActual());
        }
        if (documento == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay un documento existente ó sin finalizar aplicado al tipo de factura seleccionado"));
            return;
        }
        if (documento.getActDocumento() == 0) {
            documento.setActDocumento(documento.getIniDocumento());
        } else {
            documento.setActDocumento(documento.getActDocumento() + 1);
        }
        if (!validarCampos(documento)) {
            return;
        }
        if (tipoFactura == 3) {//en separados se valida la fecha limite
            if (fechaLimite == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Fecha limite necesaria"));
                return;
            }
            if (fechaLimite.before(new Date())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Revice la fecha limite"));
                return;
            }
        }
        CfgDocumento documentoMovInventario = documentoFacade.buscarDocumentoInventarioSalidaBySede(getSedeActual());
        if (documentoMovInventario == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay un documento existente ó sin finalizar aplicado a movimiento de inventario de salida"));
            return;
        }
        try {
//            CREANDO EL DOCUEMENTO MASTER DE: FACTURA ESPECIAL, NORMAL, SEPARADO
            FacDocumentosmaster documentosmaster = new FacDocumentosmaster();
            documentosmaster.setFacDocumentosmasterPK(new FacDocumentosmasterPK(documento.getIdDoc(), documento.getActDocumento()));
            documentosmaster.setCfgDocumento(documento);
            documentosmaster.setCfgclienteidCliente(clienteSeleccionado);
            documentosmaster.setCfgempresasedeidSede(getSedeActual());
            documentosmaster.setDescuento(totalDescuento);
            documentosmaster.setFecCrea(new Date());
            documentosmaster.setSegusuarioidUsuario(usuarioActual);
            documentosmaster.setSubtotal(subtotal);
            documentosmaster.setTotal(totalFactura);
            documentosmaster.setSegusuarioidUsuario1(vendedorSeleccionado);
            documentosmaster.setTotalUSD(totalUSD);
            documentosmaster.setObservaciones(observacion);
            documentosmaster.setFaccajaidCaja(cajaUsuario);
            calcularTotalUtilidad();
            documentosmaster.setUtilidad(utilidad);
            documentosmaster.setEstado("PENDIENTE");
            documentosmasterFacade.create(documentosmaster);
            if (documento.getActDocumento() == documento.getFinDocumento()) {
                documento.setFinalizado(true);
                documento.setActivo(false);
            }
            documentoFacade.edit(documento);

//            MODIFICANDO EL DETALLE 
            for (FacDocumentodetalle documentodetalle : listaDetalle) {
                documentodetalle.setFacDocumentodetallePK(new FacDocumentodetallePK(documentodetalle.getCfgProducto().getIdProducto(), documentosmaster.getFacDocumentosmasterPK().getCfgdocumentoidDoc(), documentosmaster.getFacDocumentosmasterPK().getNumDocumento()));
                documentodetalle.setFacDocumentosmaster(documentosmaster);
                if (tipoFactura == 2) {//para la factura especial. los impuestos no estan discriminados. Se incluyen en el producto
                    float precionUnitario = documentodetalle.getValorUnitario();
                    for (CfgImpuesto impuesto : listaImpuestos) {//recorre los impuestos aplicados al cliente y su valor se adiciona al precioUnitario del producto
                        float valorImpuesto = documentodetalle.getValorUnitario() * impuesto.getPorcentaje() / (float) 100;
                        valorImpuesto = Redondear(valorImpuesto, 0);
                        precionUnitario += valorImpuesto;
                    }
                    //se realiza  los respectivos cambios en precio unitario, total y descuento
                    documentodetalle.setValorUnitario(precionUnitario);
                    documentodetalle.setValorTotal(precionUnitario * documentodetalle.getCantidad());
                    float valorDescuento = documentodetalle.getValorTotal() * documentodetalle.getDescuento() / (float) 100;
                    valorDescuento = Redondear(valorDescuento, 0);
                    documentodetalle.setValorDescuento(valorDescuento);
                }
                documentodetalleFacade.create(documentodetalle);
                //UN SEPARADO NO HACE MOVIMIENTOS DE IVENTARIO. SOLO CUANDO SE CUMPLE EL TOTAL DE LAS CUOTAS
                if (!documentodetalle.getCfgProducto().getEsServicio() && !documentodetalle.getCfgProducto().getEsKit() && tipoFactura != 3) {//si el producto no es un servicio tampoco kit y el tipo de factura no es un separado, se descontara del inventario de forma simple
                    actualizarTablaConsolidado(documentodetalle);
                } else if (documentodetalle.getCfgProducto().getEsKit() && tipoFactura != 3) {//si el producto es un kit y no se esta realizandoo un separado, se actualizara el inventario consolidado de cada producto que conforma el kit
                    CfgKitproductomaestro kitproductomaestro = documentodetalle.getCfgProducto().getCfgkitproductomaestroidKit();
                    List<CfgKitproductodetalle> itemsKit = kitproductomaestro.getCfgKitproductodetalleList();
                    if (itemsKit.isEmpty()) {
                        itemsKit = kitproductodetalleFacade.buscarByMaestro(kitproductomaestro);
                    }
                    for (CfgKitproductodetalle detallekit : itemsKit) {
                        if (!detallekit.getCfgProducto().getEsServicio()) {
                            actualizarTablaConsolidadoKit(documentodetalle, detallekit);
                        }
                    }
                }
            }
            if (tipoFactura == 2) {//si el tipo de factura es especial. Se actualiza el subtotal y el totaldescuento en el documentomaster
                calcularSubtotal();
                calcularTotalDescuento();
                documentosmaster.setDescuento(totalDescuento);
                documentosmaster.setSubtotal(subtotal);
            }

            //GUARDANDO LOS IMPUESTOS APLICADOS A LA FACTURA
            List<FacDocumentoimpuesto> listaDocumentoImpuesto = new ArrayList();
            if (tipoFactura == 1 || tipoFactura == 3) {//para la factura normal o separado. los impuestos estan discriminados
                for (CfgImpuesto impuesto : listaImpuestos) {
                    FacDocumentoimpuesto documentoimpuesto = new FacDocumentoimpuesto();
                    documentoimpuesto.setFacDocumentoimpuestoPK(new FacDocumentoimpuestoPK(impuesto.getIdImpuesto(), documentosmaster.getFacDocumentosmasterPK().getCfgdocumentoidDoc(), documentosmaster.getFacDocumentosmasterPK().getNumDocumento()));
                    documentoimpuesto.setCfgImpuesto(impuesto);
                    documentoimpuesto.setFacDocumentosmaster(documentosmaster);
                    documentoimpuesto.setValorImpuesto(impuesto.getTotalImpuesto());
                    documentoimpuesto.setPorcentajeImpuesto(impuesto.getPorcentaje());
                    documentoimpuestoFacade.create(documentoimpuesto);
                    listaDocumentoImpuesto.add(documentoimpuesto);
                }
            }

            //GUARDANDO LAS FORMAS DE PAGOS DE LA FACTURA
            List<FacDocuementopago> listaDocumentoPago = new ArrayList();
            for (CfgFormapago formapago : listaFormapagos) {
                if (formapago.getSubtotal() > 0) {
                    FacDocuementopago docuementopago = new FacDocuementopago();
                    docuementopago.setFacDocuementopagoPK(new FacDocuementopagoPK(formapago.getIdFormaPago(), documentosmaster.getFacDocumentosmasterPK().getCfgdocumentoidDoc(), documentosmaster.getFacDocumentosmasterPK().getNumDocumento()));
                    docuementopago.setCfgFormapago(formapago);
                    docuementopago.setFacDocumentosmaster(documentosmaster);
                    docuementopago.setValorPago(formapago.getSubtotal());
                    docuementopagoFacade.create(docuementopago);
                    listaDocumentoPago.add(docuementopago);
                }
            }

//            ASOCIANDO LAS LISTAS: DETALLE, IMPUESTOS Y PAGOS AL DOCUMENTOMASTER
            documentosmaster.setFacDocumentodetalleList(listaDetalle);
            if (!listaDocumentoImpuesto.isEmpty()) {
                documentosmaster.setFacDocumentoimpuestoList(listaDocumentoImpuesto);
            }
            if (!listaDocumentoPago.isEmpty()) {
                documentosmaster.setFacDocuementopagoList(listaDocumentoPago);
            }
            documentosmasterFacade.edit(documentosmaster);

//            SI LA FACTURA PROVIENE DE UNA COTIZACION. SE CAMBIARA EL ESTADO DE LA COTIZACION A FACTURADA
            if (cotizacionSeleccionada != null) {
                cotizacionSeleccionada.setEstado("FACTURADA");
                documentosmasterFacade.edit(cotizacionSeleccionada);
            }

            documentoActual = documentosmaster;
            //ban sera true cuando el detalle incluya al menos un kit y un producto que no sea un servicio
            boolean ban = false;
            for (FacDocumentodetalle fd : listaDetalle) {
                if (!fd.getCfgProducto().getEsServicio() || fd.getCfgProducto().getEsKit()) {
                    ban = true;
                    break;
                }
            }
            //si ban = true. se creara un movimiento de salida en el inventario.
            if (ban) {
                crearMovimientoInventario(listaDetalle, documentosmaster);
            }
            if (tipoFactura != 3) {//el movimiento de caja se comporta diferente para los separados
//                se registra el total de la factura o remision
                generarMovimientoCaja(documentosmaster);
            } else {
//                se crea cartera del cliente por el separado, en movimiento caja se registra el pago de la cuota inicial
                generarMovimientoAlternativo(documentosmaster);
            }
            limpiarFormulario();
            RequestContext.getCurrentInstance().execute("PF('dlgFormaPago').hide()");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion", "Factura creada"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Factura no creada"));
        }
    }

    private void generarMovimientoCaja(FacDocumentosmaster documentosmaster) {
        List<FacDocuementopago> listaformapago = docuementopagoFacade.buscarByDocumentoMaster(documentosmaster);
        try {
            for (FacDocuementopago formapago : listaformapago) {
                FacMovcajadetalle movcajadetalle = new FacMovcajadetalle();
                movcajadetalle.setFacMovcajadetallePK(new FacMovcajadetallePK(movimientoCajaMaster.getIdMovimiento(), documentosmaster.getFacDocumentosmasterPK().getCfgdocumentoidDoc(), documentosmaster.getFacDocumentosmasterPK().getNumDocumento(), formapago.getFacDocuementopagoPK().getCfgformapagoidFormaPago()));
                movcajadetalle.setFacMovcaja(movimientoCajaMaster);
                movcajadetalle.setFacDocumentosmaster(documentosmaster);
                movcajadetalle.setCfgFormapago(formapago.getCfgFormapago());
                movcajadetalle.setFecha(new Date());
                movcajadetalle.setValor(formapago.getValorPago());
                movcajadetalleFacade.create(movcajadetalle);
            }
            documentosmaster.setEstado("CANCELADA");
            documentosmasterFacade.edit(documentosmaster);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se logro registrar el pago. Cancele la factura " + documentosmaster.getFacDocumentosmasterPK().getNumDocumento()));
        }
    }

    private void generarMovimientoAlternativo(FacDocumentosmaster documento) {
        try {
            //SE ABRE CARTERA POR CONCEPTO DEL SEPARADO
            FacCarteraCliente carteraCliente = new FacCarteraCliente();
            carteraCliente.setFacCarteraClientePK(new FacCarteraClientePK(clienteSeleccionado.getIdCliente(), documento.getFacDocumentosmasterPK().getCfgdocumentoidDoc(), documento.getFacDocumentosmasterPK().getNumDocumento()));
            carteraCliente.setCfgCliente(documento.getCfgclienteidCliente());
            carteraCliente.setFacDocumentosmaster(documento);
//            carteraCliente.setTotalcuotas(numCuotas);
            carteraCliente.setFechaLimite(fechaLimite);
            carteraCliente.setCuotaactual(1);//corresponde a la inicial
            carteraCliente.setEstado("PENDIENTE");//cuando se termine de pagar todas la cuotas pasara A FINALIZADA
            float totalSeparado = documento.getTotal();
            carteraCliente.setValor(totalSeparado);
            carteraCliente.setSaldo(totalSeparado - cuotaInicial);
            carteraClienteFacade.create(carteraCliente);

            //SE REGISTRA LA CUOTA INICIAL COMO EL PRIMER DETALLE DE LA CARTERA
            List<FacCarteraDetalle> listaDetalleCartera = new ArrayList();
            FacCarteraDetalle carteraDetalle = new FacCarteraDetalle();
            carteraDetalle.setFacCarteraDetallePK(new FacCarteraDetallePK(carteraCliente.getFacCarteraClientePK().getCfgclienteidCliente(), carteraCliente.getFacCarteraClientePK().getFacdocumentosmastercfgdocumentoidDoc(), carteraCliente.getFacCarteraClientePK().getFacdocumentosmasternumDocumento(), new Date()));
            carteraDetalle.setFacCarteraCliente(carteraCliente);
            carteraDetalle.setAbono(cuotaInicial);
            //PARA LA CUOTA INICIAL FacDocumentosmaster HARA REFERENCIA AL DOCUMENTO SEPERADO. PERO PARA PROXIMOS ABONADOS HARA REFERENCIA AL RECIBO DE CAJA
            carteraDetalle.setFacDocumentosmaster(documento);
            carteraDetalle.setSaldo(carteraCliente.getSaldo());
            carteraDetalleFacade.create(carteraDetalle);
            listaDetalleCartera.add(carteraDetalle);

            //SE INCLUYE LA INFORMACION DEL DETALLE EN LA CARTERA
            carteraCliente.setFacCarteraDetalleList(listaDetalleCartera);
            carteraClienteFacade.edit(carteraCliente);

            //creamos el detalle del movimiento de caja por concepto de la cuota inicial de la factura
            //la cuota inicial puede ser pagada mediante una o varias formas de pago (EFE, TDB, TCR, TRG, CHE)
            List<FacDocuementopago> listaformapago = documento.getFacDocuementopagoList();
            if (listaformapago.isEmpty()) {
                listaformapago = docuementopagoFacade.buscarByDocumentoMaster(documento);
            }
            for (FacDocuementopago formapago : listaformapago) {
                FacMovcajadetalle movcajadetalle = new FacMovcajadetalle();
                movcajadetalle.setFacMovcajadetallePK(new FacMovcajadetallePK(movimientoCajaMaster.getIdMovimiento(), documento.getFacDocumentosmasterPK().getCfgdocumentoidDoc(), documento.getFacDocumentosmasterPK().getNumDocumento(), formapago.getFacDocuementopagoPK().getCfgformapagoidFormaPago()));
                movcajadetalle.setFacMovcaja(movimientoCajaMaster);
                movcajadetalle.setFacDocumentosmaster(documento);
                movcajadetalle.setCfgFormapago(formapago.getCfgFormapago());
                movcajadetalle.setFecha(new Date());
                movcajadetalle.setValor(formapago.getValorPago());
                movcajadetalleFacade.create(movcajadetalle);
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se logro abrir cartera para " + documento.determinarNumFactura()));
        }

    }

    private void actualizarTablaConsolidado(FacDocumentodetalle documentodetalle) {
        try {
            InvConsolidado consolidado = consolidadoFacade.buscarBySedeAndProducto(getSedeActual(), documentodetalle.getCfgProducto());
            //el producto si no es un servicio existira en el inventario consolidado por cada cada sede de la empresa
            List<InvConsolidado> listadoConsolidado = documentodetalle.getCfgProducto().getInvConsolidadoList();
            if (consolidado != null) {
                consolidado.setFechaUltSalida(new Date());
                consolidado.setExistencia(consolidado.getExistencia() - documentodetalle.getCantidad());
                consolidado.setSalidas(consolidado.getSalidas() + documentodetalle.getCantidad());
                consolidadoFacade.edit(consolidado);
                //se actualiza la informacion del inventario consolidado en el producto
                //el producto en el inventario consolidado esta una sola vez por cada sede de la empresa.  el producto no debe ser un servicio
                //se identifica el index del listadoconsolidado afectado
                int aux = 0;
                for (InvConsolidado ic : listadoConsolidado) {
                    if (ic.equals(consolidado)) {
                        break;
                    }
                    aux++;
                }
                //se actualiza la informacion la nueva informacion del consolidado
                listadoConsolidado.get(aux).setCfgEmpresasede(consolidado.getCfgEmpresasede());
                listadoConsolidado.get(aux).setCfgProducto(consolidado.getCfgProducto());
                listadoConsolidado.get(aux).setEntradas(consolidado.getEntradas());
                listadoConsolidado.get(aux).setExistencia(consolidado.getExistencia());
                listadoConsolidado.get(aux).setFechaUltEntrada(consolidado.getFechaUltEntrada());
                listadoConsolidado.get(aux).setFechaUltSalida(consolidado.getFechaUltSalida());
                listadoConsolidado.get(aux).setInvConsolidadoPK(consolidado.getInvConsolidadoPK());
                listadoConsolidado.get(aux).setSalidas(consolidado.getSalidas());

                //se guarda los cambios del listado inventario en el producto
                productoFacade.edit(documentodetalle.getCfgProducto());
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se actualizo la informacion consolidada del inventario para el producto " + documentodetalle.getCfgProducto().getNomProducto()));
        }
    }

    private void actualizarTablaConsolidadoKit(FacDocumentodetalle documentodetalle, CfgKitproductodetalle detalleKit) {
        try {
            InvConsolidado consolidado = consolidadoFacade.buscarBySedeAndProducto(getSedeActual(), detalleKit.getCfgProducto());
            //el producto si no es un servicio existira en el inventario consolidado por cada cada sede de la empresa
            List<InvConsolidado> listadoConsolidado = detalleKit.getCfgProducto().getInvConsolidadoList();
            if (consolidado != null) {
                consolidado.setFechaUltSalida(new Date());
                consolidado.setExistencia(consolidado.getExistencia() - (documentodetalle.getCantidad() * (int) detalleKit.getCant()));
                consolidado.setSalidas(consolidado.getSalidas() + (documentodetalle.getCantidad() * (int) detalleKit.getCant()));
                consolidadoFacade.edit(consolidado);
                //se actualiza la informacion del inventario consolidado en el producto
                //el producto en el inventario consolidado esta una sola vez por cada sede de la empresa.  el producto no debe ser un servicio
                //se identifica el index del listadoconsolidado afectado
                int aux = 0;
                for (InvConsolidado ic : listadoConsolidado) {
                    if (ic.equals(consolidado)) {
                        break;
                    }
                    aux++;
                }
                //se actualiza la informacion la nueva informacion del consolidado
                listadoConsolidado.get(aux).setCfgEmpresasede(consolidado.getCfgEmpresasede());
                listadoConsolidado.get(aux).setCfgProducto(consolidado.getCfgProducto());
                listadoConsolidado.get(aux).setEntradas(consolidado.getEntradas());
                listadoConsolidado.get(aux).setExistencia(consolidado.getExistencia());
                listadoConsolidado.get(aux).setFechaUltEntrada(consolidado.getFechaUltEntrada());
                listadoConsolidado.get(aux).setFechaUltSalida(consolidado.getFechaUltSalida());
                listadoConsolidado.get(aux).setInvConsolidadoPK(consolidado.getInvConsolidadoPK());
                listadoConsolidado.get(aux).setSalidas(consolidado.getSalidas());

                //se guarda los cambios del listado inventario en el producto
                productoFacade.edit(detalleKit.getCfgProducto());
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se actualizo la informacion consolidada del inventario para el producto " + documentodetalle.getCfgProducto().getNomProducto()));
        }
    }

    private void crearMovimientoInventario(List<FacDocumentodetalle> listaDetalle, FacDocumentosmaster documentosmaster) {
//        BUSCA EL DOCUMENTO APLICADO AL MOVIMIENTO DE INVENTARIO DE SALIDA
        CfgDocumento documento = documentoFacade.buscarDocumentoInventarioSalidaBySede(getSedeActual());
        if (documento.getActDocumento() == 0) {
            documento.setActDocumento(documento.getIniDocumento());
        } else {
            documento.setActDocumento(documento.getActDocumento() + 1);
        }
        CfgMovInventarioMaestro movInventarioMaestro = movInventarioMaestroFacade.buscarMovimientoSalida();
        CfgMovInventarioDetalle movInventarioDetalle = movInventarioDetalleFacade.buscarSalidaVentaByMaestro(movInventarioMaestro);
        try {
//            CREACION DEL MAESTRO MOVIMIENTO 
            InvMovimiento invMovimientoMaestro = new InvMovimiento();
            invMovimientoMaestro.setInvMovimientoPK(new InvMovimientoPK(documento.getIdDoc(), documento.getActDocumento()));
            invMovimientoMaestro.setCfgDocumento(documento);
            invMovimientoMaestro.setCfgempresasedeidSede(getSedeActual());
            invMovimientoMaestro.setCfgmovinventariodetalleidMovInventarioDetalle(movInventarioDetalle);
            invMovimientoMaestro.setDescuento(totalDescuento);
            invMovimientoMaestro.setFacDocumentosmaster(documentosmaster);
            invMovimientoMaestro.setFecha(new Date());
//            invMovimientoMaestro.setIva(totalIva);
            invMovimientoMaestro.setSegusuarioidUsuario(usuarioActual);
            invMovimientoMaestro.setSubtotal(subtotal);
            invMovimientoMaestro.setTotal(totalFactura);
            inventarioMovimientoMaestroFacade.create(invMovimientoMaestro);
            if (documento.getActDocumento() >= documento.getFinDocumento()) {//dependiendo de la situacion se finaliza el documento aplicado al movimiento de salida
                documento.setFinalizado(true);
            }
            documentoFacade.edit(documento);
            List<InvMovimientoDetalle> listaItemsInventarioMovimiento = new ArrayList();
//            CREACION DEL DETALLE MOVIMIENTO
            for (FacDocumentodetalle detalleFactura : listaDetalle) {
                if (!detalleFactura.getCfgProducto().getEsServicio()) {//si el producto no es un servicio se incluye en el detalle del movimiento de salida
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se actualizo el inventario en el inventario"));
        }
    }

    public void facturacion() {
        if (cajaUsuario == null) {
            //se vuelve a buscar la caja del usario. Por si el usuario la creo en el transcurso de la sesion
            usuarioActual = usuarioFacade.find(usuarioActual.getIdUsuario());
            cajaUsuario = usuarioActual.getFaccajaidCaja();
            if (cajaUsuario == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El usuario no tiene caja asignada"));
                return;
            } else {
                sesionMB.getUsuarioActual().setFaccajaidCaja(cajaUsuario);
            }
        }
        movimientoCajaMaster = movcajamaestroFacade.buscarMovimientoCaja(cajaUsuario);
        if (movimientoCajaMaster == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Abra la caja por primera vez"));
            return;
        }
        if (!movimientoCajaMaster.getAbierta()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La caja no esta abierta"));
            return;
        }
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
        //cuando es un separado se valida con respecto a la cuota inicial y no con la totalidad de la compra
        float pago;
        if (tipoFactura != 3) {
            pago = totalFactura;
        } else {
            if (cuotaInicial <= 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Valor de la cuota inicial incorrecto"));
                RequestContext.getCurrentInstance().update("msg");
                setEnableBtnPrint(false);
                return;
            }
            pago = cuotaInicial;
        }
        float aux = totalFormaPago();
        if (formapago.getSubtotal() < 0 || !validarFormaPago(aux)) {
            formapago.setSubtotal(0);
        }
        setEnableBtnPrint(pago == aux);
    }

    public void onRowCancel(RowEditEvent event) {
//        FacesMessage msg = new FacesMessage("Edit Cancelled", ((CfgFormapago) event.getObject()).getNomFormaPago());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
        CfgFormapago formapago = (CfgFormapago) event.getObject();
        //cuando es un separado se valida con respecto a la cuota inicial y no con la totalidad de la compra
        float pago;
        if (tipoFactura != 3) {
            pago = totalFactura;
        } else {
            if (cuotaInicial <= 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Valor de la cuota inicial incorrecto"));
                RequestContext.getCurrentInstance().update("msg");
                setEnableBtnPrint(false);
                return;
            }
            pago = cuotaInicial;
        }
        formapago.setSubtotal(0);
        float aux = totalFormaPago();
        setEnableBtnPrint(pago == aux);
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

    public void determinarValorFormaPago() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        int idFormaPago = Integer.valueOf(params.get("idPago"));
        //si es un separado se valida con respecto a la cuota inicial de lo contrario del total de la compra
        float pago;
        if (tipoFactura != 3) {
            pago = totalFactura;
        } else {
            if (cuotaInicial <= 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Valor de la cuota inicial incorrecto"));
                RequestContext.getCurrentInstance().update("msg");
                setEnableBtnPrint(false);
                return;
            }
            pago = cuotaInicial;
        }
        float aux = totalFormaPago();
        for (CfgFormapago fpago : listaFormapagos) {
            if (fpago.getIdFormaPago() == idFormaPago && fpago.getSubtotal() == 0) {
                fpago.setSubtotal(pago - aux);
                break;
            }
        }
        aux = totalFormaPago();
        setEnableBtnPrint(pago == aux);
        RequestContext.getCurrentInstance().update("FormModalFactura:IdTableFormaPago");
        RequestContext.getCurrentInstance().update("FormModalFactura:IdBtnPrint");
    }

    public void generarPDF() {
//        guardarFactura();
        if (documentoActual != null) {
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String ruta = null;
            if (!documentoActual.getCfgDocumento().getCfgAplicaciondocumentoIdaplicacion().getCodaplicacion().equals("7")) {//todo tipo de facturacion diferente a separado aplica el formato ticket y carta
                switch (tipoImpresion) {
                    case 1:
                        ruta = servletContext.getRealPath("/facturacion/reportes/facturaTicket.jasper");
                        break;
                    case 2:
                        ruta = servletContext.getRealPath("/facturacion/reportes/facturaCarta.jasper");
                        break;
                }
            } else {//solo se aplica el formato carta
                ruta = servletContext.getRealPath("/facturacion/reportes/separadoCarta.jasper");
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
                = documentosmasterFacade.buscarBySedeAndDocumentoAndNum(getSedeActual(),
                        documentoActual.getFacDocumentosmasterPK().getCfgdocumentoidDoc(),
                        documentoActual.getFacDocumentosmasterPK().getNumDocumento()
                );
        byte[] bites = getSedeActual().getLogo();
        if (bites != null) {
            bites = getSedeActual().getCfgempresaidEmpresa().getLogo();
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
        FacCarteraCliente cartera = carteraClienteFacade.buscarPorDocumentoMaestro(documento);
        facturaReporte.setCuotas(crearListadoCuotas(carteraDetalleFacade.buscarPorCartera(cartera)));
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
            CfgEmpresa empresa = getSedeActual().getCfgempresaidEmpresa();
            switch (documento.getCfgDocumento().getCfgAplicaciondocumentoIdaplicacion().getCodaplicacion()) {
                case "1":
                    parametros.put("title", "VENTA No");
                    parametros.put("nit", empresa.getCfgTipodocempresaId().getDocumentoempresa() + " " + getSedeActual().getNumDocumento() + " " + empresa.getCfgTipoempresaId().getDescripcion());
                    parametros.put("resdian", documento.getCfgDocumento().getResDian());
                    break;
                case "6":
                    parametros.put("title", "VENTA No");
                    break;
                case "7":
                    parametros.put("title", "SEPARADO");
            }
            parametros.put("empresa", empresa.getNomEmpresa() + " - " + getSedeActual().getNomSede());
            parametros.put("direccion", getSedeActual().getDireccion());
            String telefono = getSedeActual().getTel1();
            if (getSedeActual().getTel2() != null && !sedeActual.getTel2().isEmpty()) {
                telefono = telefono + "-".concat(getSedeActual().getTel2());
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
            parametros.put("ubicacion", getSedeActual().getCfgMunicipio().getNomMunicipio() + " " + getSedeActual().getCfgMunicipio().getCfgDepartamento().getNomDepartamento());
            parametros.put("usuario", usuarioActual.nombreCompleto());
            parametros.put("identificacionUsuario", usuarioActual.getNumDoc());
            parametros.put("SUBREPORT_DIR", rutaReportes);
            parametros.put("observacion", documento.getObservaciones());
            parametros.put("caja", documento.getFaccajaidCaja().getNomCaja());
            parametros.put("vendedor", documento.getSegusuarioidUsuario1().nombreCompleto());
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

    private List<FacturaCuotasReporte> crearListadoCuotas(List<FacCarteraDetalle> cuotas) {
        List<FacturaCuotasReporte> list = new ArrayList();
        int i = 1;
        for (FacCarteraDetalle cuota : cuotas) {
            FacturaCuotasReporte cuotaReporte = new FacturaCuotasReporte();
            cuotaReporte.setNumCuota(i);
            cuotaReporte.setValor(cuota.getAbono());
            cuotaReporte.setFecha(cuota.getFacCarteraDetallePK().getFecha());
            list.add(cuotaReporte);
            i++;
        }
        return list;
    }

    private void limpiarFormulario() {
        listaDetalle.clear();
        clienteSeleccionado = clienteFacade.buscarClienteDefault(getSedeActual().getCfgempresaidEmpresa());
        setListaImpuestos(impuestoFacade.buscarImpuestosPorEmpresa(empresaActual));
        listaFormapagos = formapagoFacade.buscarPorEmpresa(getSedeActual().getCfgempresaidEmpresa());
        cargarInformacionCliente();
        setSubtotal(0);
        setTotalDescuento(0);
        setTotalFactura(0);
//        numCuotas = 2;
        observacion = "";
        fechaLimite = new Date();
        totalUSD = 0;
        tipoFactura = 1;
        display = "none";
        cotizacionSeleccionada = null;
        setEnableBtnPrint(false);
        RequestContext.getCurrentInstance().update("IdFormFactura");
        RequestContext.getCurrentInstance().update("FormModalFactura");

    }
//------------------------------------    
//    METODOS PARA CREAR CLIENTE
//------------------------------------    

    public void buscarClienteModal() {
        if (getSedeActual() != null) {
            if (numIdentificacion != null && !numIdentificacion.trim().isEmpty()) {
                clienteSeleccionadoModal = clienteFacade.buscarPorIdentificacionAndIdEmpresa(numIdentificacion, getSedeActual().getCfgempresaidEmpresa());
                cargarInformacionClienteModal();
            }
        } else {
            clienteSeleccionadoModal = null;
            cargarInformacionClienteModal();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Determine la empresa"));
        }
    }

    public void actualizarMunicipios() {
        listaMunicipios.clear();
        if (idDepartamento != null) {
            listaMunicipios = municipioFacade.buscarPorDepartamento(idDepartamento);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        if (file != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Foto cargada"));
            RequestContext.getCurrentInstance().execute("PF('dlgFoto').hide()");
        }
    }

    public void cargarInformacionClienteModal() {
        if (getClienteSeleccionadoModal() != null) {
            setIdDepartamento(clienteSeleccionadoModal.getCfgMunicipio().getCfgDepartamento().getIdDepartamento());
            setListaMunicipios(municipioFacade.buscarPorDepartamento(idDepartamento));
            setIdMunicipio(clienteSeleccionadoModal.getCfgMunicipio().getCfgMunicipioPK().getIdMunicipio());
            setIdIdentificacion(clienteSeleccionadoModal.getCfgTipoidentificacionId().getId());
            setIdTipoCliente(clienteSeleccionadoModal.getCfgTipoempresaId().getId());
            setNumIdentificacion(clienteSeleccionadoModal.getNumDoc());
            setPrimerNombre(clienteSeleccionadoModal.getNom1Cliente());
            setSegundoNombre(clienteSeleccionadoModal.getNom2Cliente());
            setPrimerApellido(clienteSeleccionadoModal.getApellido1());
            setSegundoApellido(clienteSeleccionadoModal.getApellido2());
            setDireccion(clienteSeleccionadoModal.getDirCliente());
            if (clienteSeleccionadoModal.getCupoCredito() != null) {
                setCupoCredito(clienteSeleccionadoModal.getCupoCredito());
            } else {
                setCupoCredito(0);
            }
            setTarjetaMembresia(clienteSeleccionadoModal.getTarjetaMembresia());
            setTelefono(clienteSeleccionadoModal.getTel1());
            setMail(clienteSeleccionadoModal.getMail());
            setFechaNacimiento(clienteSeleccionadoModal.getFecNacimiento());
            if (clienteSeleccionadoModal.getFoto() != null) {
                setImage(new DefaultStreamedContent(new ByteArrayInputStream(clienteSeleccionado.getFoto())));
            } else {
                setImage(null);
            }
        } else {
            listaMunicipios.clear();
            limpiarFormularioModal();
        }
        RequestContext.getCurrentInstance().update("FormModalCliente");
    }

    private void limpiarFormularioModal() {
        setIdIdentificacion(0);
//        setNumIdentificacion(null);
//        setNombreEmpresa(null);       
        setPrimerNombre(null);
        setIdDepartamento(null);
        setIdTipoCliente(0);
        setSegundoNombre(null);
        setPrimerApellido(null);
        setSegundoApellido(null);
        setDireccion(null);
        setCupoCredito(0);
        setTarjetaMembresia(null);
        setTelefono(null);
        setMail(null);
        setIdMunicipio(null);
        setIdIdentificacion(0);
        setFechaNacimiento(null);
//        opcion = "creacion";
    }

    public void accion() {
        if (clienteSeleccionadoModal != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No es posible modificar al cliente."));
        } else {
            crearCliente();
        }
    }

    private boolean validarCamposFormulario() {
        if (getSedeActual() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Determine la empresa"));
            return false;
        }
//        if (codigoCliente.isEmpty()) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Asigne un codigo al cliente"));
//            return false;
//        }
        if (idIdentificacion == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Determine el tipo de identificacion"));
            return false;
        }
        if (numIdentificacion.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Numero identificacion requerido"));
            return false;
        }
        if (primerNombre.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Primer Nombre necesario"));
            return false;
        }
        if (primerApellido.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Primer apellido necesario"));
            return false;
        }
        if (direccion.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Direccion vacia"));
            return false;

        }
        if (telefono.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese un numero telefonico"));
            return false;
        }
        return true;
    }

    private void crearCliente() {
        if (!validarCamposFormulario()) {
            return;
        }
        try {
            CfgMunicipioPK cfgMunicipioPK = new CfgMunicipioPK(idMunicipio, idDepartamento);
            CfgMunicipio municipio = municipioFacade.buscarPorMunicipioPK(cfgMunicipioPK);
            CfgTipoempresa tipocliente = tipoClienteFacade.find(idTipoCliente);
            CfgCliente cliente = new CfgCliente();
//            cliente.setCodigoCliente(codigoCliente);
            cliente.setCfgMunicipio(municipio);
            cliente.setApellido1(primerApellido.trim().toUpperCase());
            cliente.setApellido2(segundoApellido.trim().toUpperCase());
            cliente.setCfgTipoidentificacionId(tipoidentificacionFacade.find(idIdentificacion));
            cliente.setCfgempresaidEmpresa(getSedeActual().getCfgempresaidEmpresa());
            cliente.setCfgTipoempresaId(tipocliente);
            cliente.setDirCliente(direccion.trim().toUpperCase());
            cliente.setFecCrea(new Date());
            if (file != null) {
                cliente.setFoto(file.getContents());
            }
            cliente.setMail(mail.trim().toUpperCase());
            cliente.setCupoCredito(cupoCredito);
            cliente.setTarjetaMembresia(tarjetaMembresia);
            cliente.setNom1Cliente(primerNombre.trim().toUpperCase());
            cliente.setNom2Cliente(segundoNombre.trim().toUpperCase());
            cliente.setNumDoc(numIdentificacion.trim());
            cliente.setTel1(telefono.trim());
            cliente.setFecNacimiento(fechaNacimiento);
            cliente.setSegusuarioidUsuario(usuarioActual);
            clienteFacade.create(cliente);
            clienteSeleccionado = cliente;
            cargarInformacionCliente();
            listaMunicipios.clear();
            limpiarFormularioModal();
            setNumIdentificacion(null);
            actualizarListadoClientes();
            RequestContext.getCurrentInstance().update("FormModalCliente");
            RequestContext.getCurrentInstance().execute("PF('dlgCrearCliente').hide()");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Cliente creado"));
        } catch (NumberFormatException | ELException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cliente no creado"));
        }
    }

    public void cancelar() {
        clienteSeleccionadoModal = null;
//        empresaSeleccionada = null;
        listaMunicipios.clear();
//        setCodEmpresa(null);
//        setCodigoCliente(null);
        limpiarFormularioModal();
        RequestContext.getCurrentInstance().update("FormModalCliente");
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

    public LazyDataModel<CfgCliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(LazyDataModel<CfgCliente> listaClientes) {
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

    public float getTotalUSD() {
        return totalUSD;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(String idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(String idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public int getIdIdentificacion() {
        return idIdentificacion;
    }

    public void setIdIdentificacion(int idIdentificacion) {
        this.idIdentificacion = idIdentificacion;
    }

    public String getNumIdentificacion() {
        return numIdentificacion;
    }

    public void setNumIdentificacion(String numIdentificacion) {
        this.numIdentificacion = numIdentificacion;
    }

    public int getIdTipoCliente() {
        return idTipoCliente;
    }

    public void setIdTipoCliente(int idTipoCliente) {
        this.idTipoCliente = idTipoCliente;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTarjetaMembresia() {
        return tarjetaMembresia;
    }

    public void setTarjetaMembresia(String tarjetaMembresia) {
        this.tarjetaMembresia = tarjetaMembresia;
    }

    public float getCupoCredito() {
        return cupoCredito;
    }

    public void setCupoCredito(float cupoCredito) {
        this.cupoCredito = cupoCredito;
    }

    public StreamedContent getImage() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {//fase de jsf
            image = new DefaultStreamedContent();
            return image;
        } else {
            if (clienteSeleccionado != null) {
                String imageId = context.getExternalContext().getRequestParameterMap().get("id");
                CfgCliente cliente = clienteFacade.find(Integer.parseInt(imageId));
                image = new DefaultStreamedContent(new ByteArrayInputStream(cliente.getFoto()));
                return image;
            } else {
                return null;
            }
        }
    }

    public void setImage(StreamedContent image) {
        this.image = image;
    }

    public List<CfgMunicipio> getListaMunicipios() {
        return listaMunicipios;
    }

    public void setListaMunicipios(List<CfgMunicipio> listaMunicipios) {
        this.listaMunicipios = listaMunicipios;
    }

    public CfgCliente getClienteSeleccionadoModal() {
        return clienteSeleccionadoModal;
    }

    public void setClienteSeleccionadoModal(CfgCliente clienteSeleccionadoModal) {
        this.clienteSeleccionadoModal = clienteSeleccionadoModal;
    }

    public SegUsuario getVendedorSeleccionado() {
        return vendedorSeleccionado;
    }

    public void setVendedorSeleccionado(SegUsuario vendedorSeleccionado) {
        this.vendedorSeleccionado = vendedorSeleccionado;
    }

    public List<SegUsuario> getListaVendedor() {
        return listaVendedor;
    }

    public void setListaVendedor(List<SegUsuario> listaVendedor) {
        this.listaVendedor = listaVendedor;
    }

    public String getDocumentoVendedor() {
        return documentoVendedor;
    }

    public void setDocumentoVendedor(String documentoVendedor) {
        this.documentoVendedor = documentoVendedor;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    public LazyDataModel<FacDocumentosmaster> getListadoCotizacion() {
        return listadoCotizacion;
    }

    public FacDocumentosmaster getCotizacionSeleccionada() {
        return cotizacionSeleccionada;
    }

    public void setCotizacionSeleccionada(FacDocumentosmaster cotizacionSeleccionada) {
        this.cotizacionSeleccionada = cotizacionSeleccionada;
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

    public String getDisplay() {
        return display;
    }

    public CfgEmpresasede getSedeActual() {
        return sedeActual;
    }

    public float getCuotaInicial() {
        return cuotaInicial;
    }

    public void setCuotaInicial(float cuotaInicial) {
        this.cuotaInicial = cuotaInicial;
    }

//    public int getNumCuotas() {
//        return numCuotas;
//    }
//
//    public void setNumCuotas(int numCuotas) {
//        this.numCuotas = numCuotas;
//    }
    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }
}
