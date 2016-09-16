/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import com.google.common.io.Files;
import entities.CfgCliente;
import entities.CfgDocumento;
import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.CfgImpuesto;
import entities.CfgKitproductodetalle;
import entities.CfgKitproductomaestro;
import entities.CfgMunicipio;
import entities.CfgMunicipioPK;
import entities.CfgProducto;
import entities.CfgTipoempresa;
import entities.FacDocumentodetalle;
import entities.FacDocumentodetallePK;
import entities.FacDocumentoimpuesto;
import entities.FacDocumentoimpuestoPK;
import entities.FacDocumentosmaster;
import entities.FacDocumentosmasterPK;
import entities.FacMovcaja;
import entities.InvConsolidado;
import entities.SegUsuario;
import facades.CfgClienteFacade;
import facades.CfgDocumentoFacade;
import facades.CfgFormapagoFacade;
import facades.CfgImpuestoFacade;
import facades.CfgKitproductodetalleFacade;
import facades.CfgMovInventarioDetalleFacade;
import facades.CfgMovInventarioMaestroFacade;
import facades.CfgMunicipioFacade;
import facades.CfgPaisFacade;
import facades.CfgProductoFacade;
import facades.CfgTipoempresaFacade;
import facades.CfgTipoidentificacionFacade;
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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import managedBeans.seguridad.SesionMB;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import utilities.FacturaDetalleReporte;
import utilities.FacturaImpuestoReporte;
import utilities.FacturaReporte;
import utilities.LazyClienteDataModel;
import utilities.LazyProductosModel;
import java.util.Calendar;
import javax.faces.model.SelectItem;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class CotizacionMB implements Serializable {

    private String identificacionCliente;
    private String nombreCliente;
    private float subtotal;
    private float totalDescuento;
    private float descuentoTotal;
    private float descuentoTotalValor;
    private int   tipoDescuentoFactura;
    private float totalFactura;
    private float totalUSD;
    private Date fechaSeleccionada;
    private Date fechaVencimiento;
    private String observacion;
    private String pathRoot;
    private SegUsuario vendedorSeleccionado;
    private LazyDataModel<CfgProducto> listaProducto;
    private List<FacDocumentodetalle> listaDetalle;
    private LazyDataModel<CfgCliente> listaClientes;
    private List<CfgImpuesto> listaImpuestos;
//    private FacCaja cajaUsuario;//caja asignada al usuario
    private FacMovcaja movimientoCajaMaster;//contiene informacion del maestro del movimiento. Se crea uno cada vez que se habre caja. cuando se cierra se habilita
    private List<SegUsuario> listaVendedor;
    private String documentoVendedor;
    private String nombreVendedor;

    private List<SelectItem> listaTipoDescuento;

    private String numCotizacion;
    private int tipoImpresion;
    private CfgEmpresa empresaActual;
    private CfgEmpresasede sedeActual;
    private CfgCliente clienteSeleccionado;
    private CfgProducto productoSeleccionado;
    private SegUsuario usuarioActual;
    private FacDocumentosmaster documentoActual;
    private boolean enableBtnPrint;
    private StreamedContent productImage;
    private SesionMB sesionMB;

//--------------------------------------------------------
//-------------PROPIEDADES CLIENTE NUEVO
//--------------------------------------------------------
    private String idPais;
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
    private boolean mostrarDepartamentoMunicipio;

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
    CfgKitproductodetalleFacade kitproductodetalleFacade;    
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
    CfgPaisFacade paisFacade;

    public CotizacionMB() {

    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        //si es super usuario o admin permitir escoger la empresa y la sede
        pathRoot = context.getExternalContext().getInitParameter("directory.images");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 8);
        fechaVencimiento = calendar.getTime();
        fechaSeleccionada = fechaVencimiento;
        descuentoTotal = 0;
        tipoImpresion = 1;
        setEnableBtnPrint(false);
        listaMunicipios = new ArrayList();
        setListaDetalle((List<FacDocumentodetalle>) new ArrayList());
        setListaImpuestos((List<CfgImpuesto>) new ArrayList());
        sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
        sedeActual = sesionMB.getSedeActual();
        empresaActual = sesionMB.getEmpresaActual();
        if (empresaActual != null) {
            actualizarListadoClientes();
            listaProducto = new LazyProductosModel(empresaActual, null, null, null, productoFacade);
            RequestContext.getCurrentInstance().update("FormModalProducto");
            clienteSeleccionado = clienteFacade.buscarClienteDefault(empresaActual);
            setListaImpuestos(impuestoFacade.buscarImpuestosPorEmpresa(empresaActual));
            setListaImpuestos(impuestoFacade.buscarImpuestosPorEmpresa(empresaActual));
            cargarInformacionCliente();
            listaTipoDescuento = new ArrayList();
            SelectItem aux = new SelectItem(1, "PORCENTAJE");
            listaTipoDescuento.add(aux);
            aux = new SelectItem(2, "VALOR");
            listaTipoDescuento.add(aux);
        } else {
            listaImpuestos = new ArrayList();
        }
        if (usuarioActual != null) {
            if (usuarioActual.getCfgRolIdrol() != null) {
                vendedorSeleccionado = usuarioActual.getCfgRolIdrol().getCodrol().equals("00003") ? usuarioActual : null;
                cargarInformacionVendedor();
            }
        }
        idPais = "343";
        mostrarDepartamentoMunicipio = true;
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
        RequestContext.getCurrentInstance().update("IdFormCotizacion");
    }

    public void cargarModalProductos() {
        if (empresaActual != null) {
            RequestContext.getCurrentInstance().execute("PF('dlgProducto').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro informacion de la empresa"));
        }
    }

    public void cargarInformacionCliente() {
        if (clienteSeleccionado != null) {
            setNombreCliente(clienteSeleccionado.nombreCompleto());
            setIdentificacionCliente(clienteSeleccionado.getNumDoc());
        } else {
            setNombreCliente(null);
        }
        listaDetalle.clear();
        setSubtotal(0);
        setTotalDescuento(0);
        setTotalFactura(0);
        totalUSD = 0;
        setEnableBtnPrint(false);
        RequestContext.getCurrentInstance().execute("PF('dlgCliente').hide()");
        RequestContext.getCurrentInstance().update("IdFormCotizacion:IdNomCliente");
        RequestContext.getCurrentInstance().update("IdFormCotizacion:IdSubTotal");
        RequestContext.getCurrentInstance().update("IdFormCotizacion");
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
            if (!productoSeleccionado.getEsServicio() && !productoSeleccionado.getEsKit()) {//si el producto seleccionado no es un servicio se tiene encuenta la existencia en el inventario ni un kit
                consolidado = invConsolidadoFacade.buscarBySedeAndProducto(getSedeActual(), productoSeleccionado);
                if (consolidado == null) {//el producto no esta en el inventario
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se tiene registro de este producto en el inventario"));
                    return;
                }
                /*if (consolidado.getExistencia() == 0) {//no hay unidades disponibles
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay existencias de este producto"));
                    return;
                }*/
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
                /*if (aux > facdetalle.getCantidadPosible()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La cantidad insertada excede las existencias del producto"));
                    return;
                }*/
                subtotal -= facdetalle.getValorTotal();
                facdetalle.setCantidad(facdetalle.getCantidad() + 1);
                facdetalle.setValorTotal(facdetalle.getCantidad() * facdetalle.getValorUnitario());
                //se determina el nuevo descuento para el producto actual si es un porcentaje
                if (facdetalle.getTipoDescuento() != null && facdetalle.getTipoDescuento() == 1) {
                    float porcentaje = facdetalle.getDescuento() / (float) 100;
                    float descuento = porcentaje * facdetalle.getValorTotal();
                    descuento = Redondear(descuento, 0);
                    facdetalle.setValorDescuento(descuento);
                }
            }
            subtotal += facdetalle.getValorTotal();
            calcularTotalDescuento();//total descuento de la cotizacion
            calcularImpuesto();
            calcularTotalFactura();
            calcularTotalUSD();
            RequestContext.getCurrentInstance().update("IdFormCotizacion:IdTableItemCotizacion");
            RequestContext.getCurrentInstance().update("IdFormCotizacion:IdSubTotal");
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
        RequestContext.getCurrentInstance().update("IdFormCotizacion:IdTableItemCotizacion");
        RequestContext.getCurrentInstance().update("IdFormCotizacion:IdSubTotal");
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
        float descuento = 0;
        //se determina el valor del descuento
        if (listaDetalle.get(index).getTipoDescuento() != null) {
            if (listaDetalle.get(index).getTipoDescuento() == 1) {//si es porcentaje se determina acorde al valorTotal del item (valorUnitario * cantidad) 
                float porcentaje = listaDetalle.get(index).getDescuento() / (float) 100;
                descuento = porcentaje * listaDetalle.get(index).getValorTotal();
            } else {
                descuento = listaDetalle.get(index).getDescuento();
            }
        } else {
            listaDetalle.get(index).setDescuento(0);
        }
        descuento = Redondear(descuento, 0);
        listaDetalle.get(index).setValorDescuento(descuento);
        subtotal += listaDetalle.get(index).getValorTotal();
        calcularTotalDescuento();
        calcularImpuesto();
        calcularTotalFactura();
        calcularTotalUSD();
        RequestContext.getCurrentInstance().update("IdFormCotizacion:IdSubTotal");
    }

    public void updateTabla() {
        RequestContext.getCurrentInstance().update("IdFormCotizacion:IdTableItemCotizacion");
    }
    
    public void cambio(){
        descuentoTotal = 0;
        descuentoTotalValor = 0;
        RequestContext.getCurrentInstance().update("IdFormCotizacion");
    }
    
    public void aplicarDescuento(){
        try {
            descuentoTotal = 0;
            calcularTotalDescuento();
            calcularImpuesto();
            calcularTotalFactura();
            calcularTotalUSD();
            if(tipoDescuentoFactura==1){
                descuentoTotalValor = descuentoTotalValor/10;
                descuentoTotal = totalFactura * descuentoTotalValor;
                descuentoTotal  =Redondear(descuentoTotal,0);
                
                
            }else{
                descuentoTotal = descuentoTotalValor;
            }
            calcularTotalDescuento();
            calcularImpuesto();
            calcularTotalFactura();
            calcularTotalUSD();
            RequestContext.getCurrentInstance().update("IdFormCotizacion");
        } catch (Exception e) {
        }
    }

    private void calcularTotalDescuento() {
        totalDescuento = 0;
        for (FacDocumentodetalle documentodetalle : listaDetalle) {
            totalDescuento += documentodetalle.getValorDescuento();
        }
        totalDescuento = totalDescuento + descuentoTotal;
    }

    private void calcularImpuesto() {
        float aux;
        for (CfgImpuesto impuesto : listaImpuestos) {
            aux = impuesto.getPorcentaje() * (subtotal - totalDescuento) / (float) 100;
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
        if (usuarioActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro informacion del usuario"));
            return false;
        }
        if (clienteSeleccionado == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay cliente seleccionado"));
            return false;
        }
        if (vendedorSeleccionado == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay vendedor seleccionado"));
            return false;
        }
        if (listaDetalle.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay items de cotizacion"));
            return false;
        }
        if (documento.getFinDocumento() < documento.getActDocumento()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ha llegado al limite de la creacion de cotizacion. Revice la configuracion de documentos"));
            return false;
        }
        if (documentosmasterFacade.buscarBySedeAndDocumentoAndNum(getSedeActual(), documento.getIdDoc(), documento.getActDocumento()) != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Consecutivo de cotizacion duplicado. Revice la configuracion de documentos"));
            return false;
        }
        return ban;
    }

    public void guardarCotizacion() {
        CfgDocumento documento = documentoFacade.buscarDocumentoDeCotizacionBySede(getSedeActual());
        if (documento == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay un documento existente ó sin finalizar aplicado a cotizacion"));
            return;
        }
        if (fechaVencimiento == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione fecha de vencimiento"));
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
        try {
            FacDocumentosmaster documentosmaster = new FacDocumentosmaster();
            documentosmaster.setFacDocumentosmasterPK(new FacDocumentosmasterPK(documento.getIdDoc(), documento.getActDocumento()));
            documentosmaster.setCfgDocumento(documento);
            documentosmaster.setCfgclienteidCliente(clienteSeleccionado);
            documentosmaster.setCfgempresasedeidSede(getSedeActual());
            documentosmaster.setDescuento(totalDescuento);
            documentosmaster.setFecCrea(new Date());
            documentosmaster.setSubtotal(subtotal);
            documentosmaster.setTotal(totalFactura);
            documentosmaster.setSegusuarioidUsuario(usuarioActual);
            documentosmaster.setSegusuarioidUsuario1(vendedorSeleccionado);//vendedor
            documentosmaster.setTotalUSD(totalUSD);
            documentosmaster.setObservaciones(observacion);
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
            documentoActual = documentosmaster;
            fechaSeleccionada = fechaVencimiento;
            limpiarFormulario();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion", "Cotizacion " + documentosmaster.determinarNumFactura() + " creada"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cotizacion no creada"));
        }
    }

    public void impresion() {
        if (documentoActual != null) {
            setNumCotizacion(documentoActual.determinarNumFactura());
            RequestContext.getCurrentInstance().update("IdFormConfirmacion");
            RequestContext.getCurrentInstance().execute("PF('dlgResult').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay una cotizacion reciente"));
        }
    }

    public void generarPDF() {
        if (documentoActual != null) {
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String ruta = servletContext.getRealPath("/facturacion/reportes/cotizacion.jasper");
            try {
                generarCotizacion(ruta);
            } catch (IOException ex) {
                Logger.getLogger(FacturaMB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JRException ex) {
                Logger.getLogger(FacturaMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void generarCotizacion(String ruta) throws IOException, JRException {
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
            parametros.put("empresa", empresa.getNomEmpresa() + " - " + getSedeActual().getNomSede());
            parametros.put("direccion", getSedeActual().getDireccion());
            String telefono = getSedeActual().getTel1();
            if (getSedeActual().getTel2() != null && !sedeActual.getTel2().isEmpty()) {
                telefono = telefono + "-".concat(getSedeActual().getTel2());
            }
            parametros.put("telefono", telefono);
            parametros.put("nit", empresa.getCfgTipodocempresaId().getDocumentoempresa() + " " + getSedeActual().getNumDocumento() + " " + empresa.getCfgTipoempresaId().getDescripcion());
            parametros.put("cliente", documento.getCfgclienteidCliente().nombreCompleto());
            parametros.put("tipoDoc", documento.getCfgclienteidCliente().getCfgTipoidentificacionId().getAbreviatura());
            parametros.put("identificacionCliente", documento.getCfgclienteidCliente().getNumDoc());
            parametros.put("fecha", documento.getFecCrea());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(documento.getFecCrea());
            calendar.add(Calendar.DATE, 8);
            parametros.put("vencimiento", fechaSeleccionada);
            CfgCliente cliente = documento.getCfgclienteidCliente();
            parametros.put("dircli", cliente.getDirCliente());
            parametros.put("telcli", cliente.getTel1());
            if (cliente.getCfgpaiscodPais().getCodPais().equals("343")) {
                parametros.put("ciudadcli", cliente.getCfgMunicipio().getNomMunicipio());
            } else {
                parametros.put("ciudadcli", cliente.getCfgpaiscodPais().getNomPais());
            }
            parametros.put("emailcli", cliente.getMail());
            parametros.put("ubicacion", getSedeActual().getCfgMunicipio().getNomMunicipio() + " " + getSedeActual().getCfgMunicipio().getCfgDepartamento().getNomDepartamento());
            parametros.put("usuario", usuarioActual.nombreCompleto());
            parametros.put("identificacionUsuario", usuarioActual.getNumDoc());
            parametros.put("SUBREPORT_DIR", rutaReportes);
            parametros.put("observacion", documento.getObservaciones());
            parametros.put("vendedor", documento.getSegusuarioidUsuario1().nombreCompleto());
            parametros.put("resdian", documento.getCfgDocumento().getResDian());
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
            facturaDetalleReporte.setImagen(pathRoot+detalle.getCfgProducto().getImgProducto());
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
        clienteSeleccionado = clienteFacade.buscarClienteDefault(getSedeActual().getCfgempresaidEmpresa());
//        setListaImpuestos(impuestoFacade.buscarImpuestosPorEmpresa(empresaActual));
        cargarInformacionCliente();
        setSubtotal(0);
        setTotalDescuento(0);
        setTotalFactura(0);
        setDescuentoTotal(0);
        setDescuentoTotalValor(0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 8);
        fechaVencimiento = calendar.getTime();
        totalUSD = 0;
        setEnableBtnPrint(false);
        RequestContext.getCurrentInstance().update("IdFormCotizacion");
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

    //cuando se selecciona Colombia se muestraran los departamentos y municipios
    public void validacionPais() {
        if (idPais.equals("343")) {
            mostrarDepartamentoMunicipio = true;
        } else {
            mostrarDepartamentoMunicipio = false;
        }
        RequestContext.getCurrentInstance().update("FormModalCliente");
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
            setIdPais(clienteSeleccionadoModal.getCfgpaiscodPais().getCodPais());
            if (idPais.equals("343")) {
                mostrarDepartamentoMunicipio = true;
                setIdDepartamento(clienteSeleccionadoModal.getCfgMunicipio().getCfgDepartamento().getIdDepartamento());
                setListaMunicipios(municipioFacade.buscarPorDepartamento(idDepartamento));
                setIdMunicipio(clienteSeleccionadoModal.getCfgMunicipio().getCfgMunicipioPK().getIdMunicipio());
            } else {
                mostrarDepartamentoMunicipio = false;
            }
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
        setIdPais("343");
        mostrarDepartamentoMunicipio = true;
//        opcion = "creacion";
    }

    public void accion() {
        if (clienteSeleccionadoModal != null) {
            modificarCliente();
        } else {
            crearCliente();
        }
    }

    private boolean validarCamposFormulario() {
        if (getSedeActual() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Determine la empresa"));
            return false;
        }
        if (usuarioActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se tiene informacion del usuario"));
            return false;
        }
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
            CfgMunicipio municipio = null;
            if (idPais.equals("343")) {
                CfgMunicipioPK cfgMunicipioPK = new CfgMunicipioPK(idMunicipio, idDepartamento);
                municipio = municipioFacade.buscarPorMunicipioPK(cfgMunicipioPK);
            }
            CfgTipoempresa tipocliente = tipoClienteFacade.find(idTipoCliente);
            CfgCliente cliente = new CfgCliente();
//            cliente.setCodigoCliente(codigoCliente);
            cliente.setCfgMunicipio(municipio);
            cliente.setCfgpaiscodPais(paisFacade.find(idPais));
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

    private void modificarCliente() {
        if (!validarCamposFormulario()) {
            return;
        }
        try {
            CfgMunicipio municipio = null;
            if (idPais.equals("343")) {
                CfgMunicipioPK cfgMunicipioPK = new CfgMunicipioPK(idMunicipio, idDepartamento);
                municipio = municipioFacade.buscarPorMunicipioPK(cfgMunicipioPK);
            }
            CfgTipoempresa tipocliente = tipoClienteFacade.find(idTipoCliente);
            clienteSeleccionadoModal.setApellido1(primerApellido.trim().toUpperCase());
            clienteSeleccionadoModal.setApellido2(segundoApellido.trim().toUpperCase());
            clienteSeleccionadoModal.setCfgMunicipio(municipio);
            clienteSeleccionadoModal.setCfgpaiscodPais(paisFacade.find(idPais));
            clienteSeleccionadoModal.setCfgTipoidentificacionId(tipoidentificacionFacade.find(idIdentificacion));
            clienteSeleccionadoModal.setCfgTipoempresaId(tipocliente);
            clienteSeleccionadoModal.setCfgempresaidEmpresa(empresaActual);
            clienteSeleccionadoModal.setDirCliente(direccion.trim().toUpperCase());
            if (file != null) {
                clienteSeleccionadoModal.setFoto(file.getContents());
            }
            clienteSeleccionadoModal.setMail(mail.trim().toUpperCase());
            clienteSeleccionadoModal.setNom1Cliente(primerNombre.trim().toUpperCase());
            clienteSeleccionadoModal.setNom2Cliente(segundoNombre.trim().toUpperCase());
            clienteSeleccionadoModal.setCupoCredito(cupoCredito);
            clienteSeleccionadoModal.setTarjetaMembresia(tarjetaMembresia);
            clienteSeleccionadoModal.setNumDoc(numIdentificacion.trim());
            clienteSeleccionadoModal.setFecNacimiento(fechaNacimiento);
            clienteSeleccionadoModal.setTel1(telefono.trim());
            clienteFacade.edit(clienteSeleccionadoModal);
            limpiarFormularioModal();
            setNumIdentificacion(null);
            listaMunicipios.clear();
            setClienteSeleccionadoModal(null);
            RequestContext.getCurrentInstance().update("FormBuscarCliente");
            RequestContext.getCurrentInstance().execute("PF('dlgCrearCliente').hide()");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Cliente modificado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cliente no modificado"));
        }
    }

    public void cancelar() {
        clienteSeleccionadoModal = null;
        listaMunicipios.clear();
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

    public String getNumCotizacion() {
        return numCotizacion;
    }

    public void setNumCotizacion(String numCotizacion) {
        this.numCotizacion = numCotizacion;
    }

    public CfgEmpresasede getSedeActual() {
        return sedeActual;
    }

    public String getIdPais() {
        return idPais;
    }

    public void setIdPais(String idPais) {
        this.idPais = idPais;
    }

    public boolean isMostrarDepartamentoMunicipio() {
        return mostrarDepartamentoMunicipio;
    }

    public List<SelectItem> getListaTipoDescuento() {
        return listaTipoDescuento;
    }

    public float getDescuentoTotal() {
        return descuentoTotal;
    }

    public void setDescuentoTotal(float descuentoTotal) {
        this.descuentoTotal = descuentoTotal;
    }

    public Date getFechaSeleccionada() {
        return fechaSeleccionada;
    }

    public void setFechaSeleccionada(Date fechaSeleccionada) {
        this.fechaSeleccionada = fechaSeleccionada;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public float getDescuentoTotalValor() {
        return descuentoTotalValor;
    }

    public void setDescuentoTotalValor(float descuentoTotalValor) {
        this.descuentoTotalValor = descuentoTotalValor;
    }

    public int getTipoDescuentoFactura() {
        return tipoDescuentoFactura;
    }

    public void setTipoDescuentoFactura(int tipoDescuentoFactura) {
        this.tipoDescuentoFactura = tipoDescuentoFactura;
    }

    public StreamedContent getProductImage() throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {//fase de jsf
            image = new DefaultStreamedContent();
            return image;
        } else {
        	String id = context.getExternalContext().getRequestParameterMap()
					.get("pid");
                String path;
                if(id!=null){
                    if(!id.equals(""))
                        path = pathRoot.concat(id);
                    else{
                        path = pathRoot.concat("0.jpg");
                    }
                }else{
                    return null;
                }
            File f = new File(path);
            if(f.isFile()){
                byte[] data = Files.toByteArray(f);
                image = new DefaultStreamedContent(new ByteArrayInputStream(data));
                return image;
            }else return null;
        } 
        
    }

    public void setProductImage(StreamedContent productImage) {
        this.productImage = productImage;
    }
    

}
