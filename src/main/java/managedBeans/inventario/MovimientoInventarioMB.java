/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.inventario;

import entities.CfgDocumento;
import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.CfgMovCta;
import entities.CfgMovInventarioDetalle;
import entities.CfgMovInventarioMaestro;
import entities.CfgProducto;
import entities.CfgProveedor;
import entities.CfgformaPagoproveedor;
import entities.CntMovdetalle;
import entities.CntPuc;
import entities.FacDocumentoimpuesto;
import entities.FacDocumentosmaster;
import entities.InvConsolidado;
import entities.InvConsolidadoPK;
import entities.InvMovimiento;
import entities.InvMovimientoDetalle;
import entities.InvMovimientoDetallePK;
import entities.InvMovimientoInfoadicional;
import entities.InvMovimientoInfoadicionalPK;
import entities.InvMovimientoPK;
import entities.SegUsuario;
import facades.CfgDocumentoFacade;
import facades.CfgMovCtaFacade;
import facades.CfgMovInventarioDetalleFacade;
import facades.CfgMovInventarioMaestroFacade;
import facades.CfgProductoFacade;
import facades.CfgProveedorFacade;
import facades.CfgformaPagoproveedorFacade;
import facades.CntMovdetalleFacade;
import facades.InvConsolidadoFacade;
import facades.InvMovimientoDetalleFacade;
import facades.InvMovimientoFacade;
import facades.InvMovimientoInfoadicionalFacade;
import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.faces.context.FacesContext;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.ArrayList;
import managedBeans.seguridad.SesionMB;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import managedBeans.facturacion.FacturaMB;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.LazyDataModel;
import utilities.LazyProductosModel;
import utilities.LazyProveedorDataModel;
import utilities.RemisionReporte;
import java.util.HashMap;
import java.util.Map;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import utilities.RemisionDetalleReporte;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class MovimientoInventarioMB implements Serializable {

    private int idMovInventarioMaestro;
    private int idMovInventarioDetalle;
    private int formaPago;
    private String numIdentificacion;
    private String proveedor;
    private String observacion;
    private String documentoSoporte;
    private float subtotal;
    private float totalDescuento;
    private float totalIva;
    private float totalMovimiento;

    private String ciudad;
    private String direccion;
    private String transportadora;
    private String conductor;
    private String placa;
    private String peso;

    private String concecutivo;//concecutivo del movimiento generado
    private boolean displayInfoAdicional;
    private InvMovimiento movimientoInventarioActual;

    private List<CfgMovInventarioMaestro> listaMovInventarioMaestro;
    private List<CfgMovInventarioDetalle> listaMovInventarioDetalle;
    private List<CfgformaPagoproveedor> listaFormaPago;
    private LazyDataModel<CfgProveedor> listaProveedor;
    private LazyDataModel<CfgProducto> listaProducto;
    private List<InvMovimientoDetalle> listaItemsInventarioMovimiento;

    String tipoDocumento;//determina que tipo de documento usar. El de entradas o el de salidas

    private CfgEmpresa empresaActual;
    private CfgEmpresasede sedeActual;
    private SegUsuario usuarioActual;
    private CfgProveedor proveedorSeleccionado;
    private CfgProducto productoSeleccionado;

    @EJB
    CfgMovInventarioMaestroFacade movInventarioMaestroFacade;
    @EJB
    CfgMovInventarioDetalleFacade movInventarioDetalleFacade;
    @EJB
    CfgProveedorFacade proveedorFacade;
    @EJB
    CfgformaPagoproveedorFacade pagoproveedorFacade;
    @EJB
    CfgProductoFacade productoFacade;
    @EJB
    CfgDocumentoFacade documentoFacade;
    @EJB
    InvMovimientoFacade inventarioMovimientoMaestroFacade;
    @EJB
    InvMovimientoDetalleFacade inventarioMovimientoDetalleFacade;
    @EJB
    InvMovimientoInfoadicionalFacade inventarioMovimientoInfoadicionalFacade;
    @EJB
    InvConsolidadoFacade consolidadoFacade;
    @EJB
    CfgMovCtaFacade cfgMovCtaFacade;
    @EJB
    CntMovdetalleFacade cntMovdetalleFacade;

    public MovimientoInventarioMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        empresaActual = sesionMB.getEmpresaActual();
        usuarioActual = sesionMB.getUsuarioActual();
        sedeActual = sesionMB.getSedeActual();
        listaItemsInventarioMovimiento = new ArrayList();
        listaMovInventarioMaestro = new ArrayList();
        displayInfoAdicional = false;
        if (empresaActual != null) {
            listaMovInventarioMaestro = movInventarioMaestroFacade.findAll();
            listaFormaPago = pagoproveedorFacade.buscarFormasPagoByEmpresa(empresaActual);
        }
    }

    public void cargarListaMovInventarioDetalle() {
        if (idMovInventarioMaestro != 0) {
            CfgMovInventarioMaestro maestro = movInventarioMaestroFacade.find(idMovInventarioMaestro);
            listaMovInventarioDetalle = movInventarioDetalleFacade.buscarByMaestro(maestro);
        } else {
            listaMovInventarioDetalle = new ArrayList();
        }
        RequestContext.getCurrentInstance().update("IdFormMovimientoInventario:IdListDetalleMovimiento");
    }

    public void cargarInformacionAdicional() {
        displayInfoAdicional = false;
        if (idMovInventarioMaestro != 0 && idMovInventarioDetalle != 0) {
            CfgMovInventarioMaestro maestro = movInventarioMaestroFacade.find(idMovInventarioMaestro);
            CfgMovInventarioDetalle detalle = movInventarioDetalleFacade.find(idMovInventarioDetalle);
            //si el movimiento es una salida y corresponde a un translado de bodega. se habilitar la informacion adicional
            if (maestro.getCodMovInvetario().equals("2") && detalle.getCodMovInvetarioDetalle().equals("3")) {
                displayInfoAdicional = true;
            }
        }
        RequestContext.getCurrentInstance().update("IdFormMovimientoInventario");
    }

    public void cargarProveedores() {
        if (empresaActual != null) {
            listaProveedor = new LazyProveedorDataModel(proveedorFacade, empresaActual);
            RequestContext.getCurrentInstance().update("FormBuscarProveedor");
            RequestContext.getCurrentInstance().execute("PF('dlgProveedor').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Determine la empresa"));
        }
    }

    public void buscarProveedor() {
        if (empresaActual != null) {
            if (getNumIdentificacion() != null && !numIdentificacion.trim().isEmpty()) {
                setProveedorSeleccionado(proveedorFacade.buscarPorIdentificacionAndIdEmpresa(getNumIdentificacion(), empresaActual));
                cargarInformacionProveedor();
            }
        } else {
            setProveedorSeleccionado(null);
            cargarInformacionProveedor();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Determine la empresa"));
        }
    }

    public void cargarInformacionProveedor() {
        if (proveedorSeleccionado != null) {
            proveedor = proveedorSeleccionado.getNomProveedor();
            numIdentificacion = proveedorSeleccionado.getNumDoc();
            CfgformaPagoproveedor formaPagoProveedor = proveedorSeleccionado.getCfgformaPagoproveedoridFormaPago();
            formaPago = formaPagoProveedor != null ? formaPagoProveedor.getIdFormaPago() : 0;
        } else {
            limpiarFormulario();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No se encontro Proveedor"));
        }
        RequestContext.getCurrentInstance().execute("PF('dlgProveedor').hide()");
        RequestContext.getCurrentInstance().update("IdFormMovimientoInventario");
    }

    private void limpiarFormulario() {
        numIdentificacion = null;
        idMovInventarioMaestro = 0;
        idMovInventarioDetalle = 0;
        proveedor = null;
        formaPago = 0;
        listaItemsInventarioMovimiento.clear();
        subtotal = 0;
        totalDescuento = 0;
        totalIva = 0;
        totalMovimiento = 0;
        documentoSoporte = null;
        ciudad = null;
        direccion = null;
        transportadora = null;
        conductor = null;
        placa = null;
        peso = null;
        observacion = null;
    }

    public void cargarModalProductos() {
        if (empresaActual != null) {
            if (!usuarioActual.getCfgRolIdrol().getCodrol().equals("00001") && !usuarioActual.getCfgRolIdrol().getCodrol().equals("00002")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No tiene permisos para efectuar esta accion"));
                return;
            }
            listaProducto = new LazyProductosModel(empresaActual, null, null, null, productoFacade);
            RequestContext.getCurrentInstance().update("FormModalProducto");
            RequestContext.getCurrentInstance().execute("PF('dlgProducto').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No se encontro la empresa para cargar sus productos"));
        }

    }

    public void insertarItemProducto() {
        if (productoSeleccionado != null) {
            if (productoSeleccionado.getEsServicio()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", productoSeleccionado.getNomProducto() + " es un servicio"));
                return;
            }
            if (productoSeleccionado.getEsKit()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", productoSeleccionado.getNomProducto() + " es un kit"));
                return;
            }
            //variable tenida en cuanta cuando el movimiento corresponde a una salida
            int unidadesDisponibles = 0;
            CfgMovInventarioMaestro inventarioMaestro = movInventarioMaestroFacade.find(idMovInventarioMaestro);
            if (inventarioMaestro == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Especifique el tipo de movimiento a realizar"));
                return;
            }
            if (inventarioMaestro.getCodMovInvetario().equals("2")) {//si se esta realizando una salida del inventario se tiene encuenta unidades disponibles
                InvConsolidado consolidado = consolidadoFacade.buscarBySedeAndProducto(getSedeActual(), productoSeleccionado);
                if (consolidado == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", productoSeleccionado.getNomProducto() + " no tiene unidades disponibles"));
                    return;
                } else {
                    unidadesDisponibles = consolidado.getExistencia();
                }
            }
            InvMovimientoDetalle detalle = estaInsertado(productoSeleccionado);
            if (detalle == null) {
                detalle = new InvMovimientoDetalle();
                detalle.setInvMovimientoDetallePK(new InvMovimientoDetallePK(0, 0, productoSeleccionado.getIdProducto()));
                detalle.setCfgProducto(productoSeleccionado);
                //cuando se realiza una salida(2). Se carga la informacion actual del producto.De lo contrario sera nueva
////                if (inventarioMaestro.getCodMovInvetario().equals("2")) {
                detalle.setCantidad(1);
                if (unidadesDisponibles < detalle.getCantidad() && inventarioMaestro.getCodMovInvetario().equals("2")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", productoSeleccionado.getNomProducto() + " La cantidad sobrepasa las unidades disponibles"));
                    return;
                }
                detalle.setCostoAdquisicion(productoSeleccionado.getCostoAdquisicion());
                detalle.setFlete(productoSeleccionado.getFlete());
                detalle.setIva(productoSeleccionado.getIva());
                detalle.setCostoIndirecto(productoSeleccionado.getCostoIndirecto());
//                } else {
//                    detalle.setCantidad(1);
//                    detalle.setCostoAdquisicion(0);
//                    detalle.setDescuento(0);
//                    detalle.setFlete(0);
////                detalle.setIva(0);
//                    detalle.setIva(16);
//                    detalle.setCostoIndirecto(0);
//                }
                detalle.setInvMovimiento(null);//se crea cuando se guarde
                listaItemsInventarioMovimiento.add(detalle);
            } else {
                detalle.setCantidad(detalle.getCantidad() + 1);
                //cuando se realiza una salida(2). Se valida que la cantidad no exceda a las unidades disponibles
                if (inventarioMaestro.getCodMovInvetario().equals("2")) {
                    if (unidadesDisponibles < detalle.getCantidad()) {
                        detalle.setCantidad(detalle.getCantidad() - 1);
                        updateTabla();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", productoSeleccionado.getNomProducto() + " La cantidad sobrepasa las unidades disponibles"));
                        return;
                    }
                }
            }
            determinarCostoFinal(detalle);
        }
        updateTabla();
    }

    public void quitarItem(ActionEvent actionEvent) {
        InvMovimientoDetalle item = (InvMovimientoDetalle) actionEvent.getComponent().getAttributes().get("item");
        listaItemsInventarioMovimiento.remove(item);
        subtotal -= item.getCostoFinal();
        totalDescuento -= item.getValorDescuento();
        if (listaItemsInventarioMovimiento.isEmpty()) {
            subtotal = 0;
            totalDescuento = 0;
        }
        determinarSubtotales();
        updateTabla();
    }

    private InvMovimientoDetalle estaInsertado(CfgProducto producto) {
        InvMovimientoDetalle movimientoDetalle = null;
        for (InvMovimientoDetalle detalle : listaItemsInventarioMovimiento) {
            if (detalle.getCfgProducto().equals(producto)) {
                movimientoDetalle = detalle;
                break;
            }
        }
        return movimientoDetalle;
    }

    public void onRowEdit(RowEditEvent event) {
        InvMovimientoDetalle detalle = (InvMovimientoDetalle) event.getObject();

        CfgMovInventarioMaestro inventarioMaestro = movInventarioMaestroFacade.find(idMovInventarioMaestro);
        if (inventarioMaestro.getCodMovInvetario().equals("2")) {//si se esta realizando una salida del inventario se tiene encuenta unidades disponibles
            InvConsolidado consolidado = consolidadoFacade.buscarBySedeAndProducto(sedeActual, detalle.getCfgProducto());
            if (consolidado.getExistencia() < detalle.getCantidad()) {
                detalle.setCantidad(consolidado.getExistencia());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", productoSeleccionado.getNomProducto() + " tiene " + consolidado.getExistencia() + " unidades"));
                RequestContext.getCurrentInstance().update("msg");
            }
        }
//        FacesMessage msg = new FacesMessage("Edit", detalle.getCfgProducto().getNomProducto());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
//        RequestContext.getCurrentInstance().update("msg");
        determinarCostoFinal(detalle);
    }

    public void onRowCancel(RowEditEvent event) {
        InvMovimientoDetalle detalle = (InvMovimientoDetalle) event.getObject();
//        FacesMessage msg = new FacesMessage("Edit Cancelled", detalle.getCfgProducto().getNomProducto());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
//        RequestContext.getCurrentInstance().update("msg");
        determinarCostoFinal(detalle);
    }

    public void determinarCostoFinal(InvMovimientoDetalle detalle) {
        determinarCostosIndividualProducto(detalle);
        float costoAdq = detalle.getCostoAdquisicion();
        costoAdq = validarValor(costoAdq);
        float costoFinal = 0;
        int cantidad = detalle.getCantidad();
        float flete = detalle.getFlete();
        flete = validarValor(flete);
        float descuento = detalle.getDescuento();
        descuento = validarValor(descuento);
        float costoInd = detalle.getCostoIndirecto();
        costoInd = validarValor(costoInd);
        descuento = costoAdq * (descuento / 100) * cantidad;
        descuento = Redondear(descuento, 0);
        detalle.setValorDescuento(descuento);
//        costoAdq = costoAdq - (costoAdq * (descuento / 100));
        costoFinal = costoAdq + (costoAdq * (flete / 100));
        costoFinal = costoFinal + (costoFinal * (costoInd / 100));
        costoFinal = costoFinal * cantidad;//el valor mostrado en la tabla se aplica el iva
        costoFinal = Redondear(costoFinal, 0);
        detalle.setCostoFinal(costoFinal);
        determinarSubtotales();
    }

    private void determinarSubtotales() {
        subtotal = 0;
        totalDescuento = 0;
        totalIva = 0;
        totalMovimiento = 0;
        float porcentajeIva = 0;
        if (!listaItemsInventarioMovimiento.isEmpty()) {
            porcentajeIva = listaItemsInventarioMovimiento.get(0).getIva();
        }
        for (InvMovimientoDetalle detalle : listaItemsInventarioMovimiento) {
            subtotal += detalle.getCostoFinal();
            totalDescuento += detalle.getValorDescuento();
        }
        totalIva = (subtotal - totalDescuento) * porcentajeIva / (float) 100;
        totalIva = Redondear(totalIva, 0);
        totalMovimiento = subtotal - totalDescuento + totalIva;
    }

    private void determinarCostosIndividualProducto(InvMovimientoDetalle detalle) {
        float costoFinalIndividual = 0;
        float costoAdq = detalle.getCostoAdquisicion();
        costoAdq = validarValor(costoAdq);
        float descuento = costoAdq * (detalle.getDescuento() / (float) 100);
        descuento = validarValor(descuento);
        float iva = detalle.getIva();
        iva = validarValor(iva);
        float flete = detalle.getFlete();
        flete = validarValor(flete);
        float costoInd = detalle.getCostoIndirecto();
        costoInd = validarValor(costoInd);
        costoAdq -= descuento;
        iva = costoAdq * (iva / 100);
        costoFinalIndividual = costoAdq + iva;
        costoFinalIndividual = costoFinalIndividual + (costoFinalIndividual * (flete / 100));
        costoFinalIndividual = costoFinalIndividual + (costoFinalIndividual * (costoInd / 100));
        costoFinalIndividual = Redondear(costoFinalIndividual, 0);
        detalle.setCostoFinalIndividual(costoFinalIndividual);
    }

    private float validarValor(float val) {
        if (val < 0) {
            return 0;
        }
        return val;
    }

    public void updateTabla() {
        RequestContext.getCurrentInstance().update("IdFormMovimientoInventario:IdTableMovimientoInventario");
        RequestContext.getCurrentInstance().update("IdFormMovimientoInventario:IdSubTotal");
    }

    public float Redondear(float pNumero, int pCantidadDecimales) {
        BigDecimal value = new BigDecimal(pNumero);
        value = value.setScale(pCantidadDecimales, RoundingMode.HALF_UP);
        return value.floatValue();
    }

    public boolean validar() {
        boolean ban = true;
        if (getSedeActual() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se tiene informacion de la sede"));
            return false;
        }
        if (usuarioActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No ha iniciado sesion correctamente"));
            return false;
        }
        if (!usuarioActual.getCfgRolIdrol().getCodrol().equals("00001") && !usuarioActual.getCfgRolIdrol().getCodrol().equals("00002")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No tiene permisos para efectuar esta accion"));
            return false;
        }
//        if (proveedorSeleccionado == null) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione el proveedor"));
//            return false;
//        }
        if (formaPago == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Determine la forma de pago"));
            return false;
        }
        if (idMovInventarioMaestro == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione el tipo de movimiento"));
            return false;
        }
        if (idMovInventarioDetalle == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione el tipo de movimiento"));
            return false;
        }
        if (listaItemsInventarioMovimiento.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay elementos insertados"));
            return false;
        }
        return ban;
    }

    public void guardar() {
        if (!validar()) {
            return;
        }
        CfgMovInventarioMaestro movInventarioMaestro = movInventarioMaestroFacade.find(idMovInventarioMaestro);
//        si el tipo de movimiento es de codigo = 1. El documento a aplicar es el de ENTRADA INVENTARIO. De ser el codigo = 2. Se aplicara el documento SALIDA INVENTARIO
        tipoDocumento = movInventarioMaestro.getCodMovInvetario().equals("1") ? "3" : "4";
        CfgDocumento documento = documentoFacade.buscarDocumentoDeMovInventarioBySede(getSedeActual(), tipoDocumento);
        if (documento == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay un documento existente ó sin finalizar aplicado a este tipo de movimiento"));
            return;
        }
        if (documento.getActDocumento() == 0) {
            if (documento.getIniDocumento() > 0) {
                documento.setActDocumento(documento.getIniDocumento());
            } else {
                documento.setActDocumento(documento.getIniDocumento() + 1);
            }
        } else {
            documento.setActDocumento(documento.getActDocumento() + 1);
        }
        if (documento.getFinDocumento() < documento.getActDocumento()) {
            if (!documento.getFinalizado()) {//si el documento se finaliza si aun no lo esta
                documento.setFinalizado(true);
                documentoFacade.edit(documento);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ha llegado al limite de creacion de este tipo de movimiento. Revice la configuracion de documentos"));
            return;
        }
        CfgMovInventarioDetalle movInventarioDetalle = movInventarioDetalleFacade.find(idMovInventarioDetalle);
        try {
//            CREACION DEL MAESTRO MOVIMIENTO 
            InvMovimiento invMovimientoMaestro = new InvMovimiento();
            invMovimientoMaestro.setInvMovimientoPK(new InvMovimientoPK(documento.getIdDoc(), documento.getActDocumento()));
            invMovimientoMaestro.setCfgDocumento(documento);
            invMovimientoMaestro.setCfgempresasedeidSede(getSedeActual());
            CfgformaPagoproveedor pagoproveedor = pagoproveedorFacade.find(formaPago);
            invMovimientoMaestro.setCfgformaPagoproveedoridFormaPago(pagoproveedor);
            invMovimientoMaestro.setCfgmovinventariodetalleidMovInventarioDetalle(movInventarioDetalle);
            invMovimientoMaestro.setCfgproveedoridProveedor(proveedorSeleccionado);
            invMovimientoMaestro.setDescuento(totalDescuento);
            invMovimientoMaestro.setFecha(new Date());
            invMovimientoMaestro.setIva(totalIva);
            invMovimientoMaestro.setObservacion(observacion);
            invMovimientoMaestro.setDocumentoSoporte(documentoSoporte.toUpperCase());
            invMovimientoMaestro.setSegusuarioidUsuario(usuarioActual);
            invMovimientoMaestro.setSubtotal(subtotal);
            invMovimientoMaestro.setTotal(totalMovimiento);
            inventarioMovimientoMaestroFacade.create(invMovimientoMaestro);
            if (documento.getActDocumento() >= documento.getFinDocumento()) {//dependiendo de la situacion se finaliza el documento aplicado al movimiento de salida
                documento.setFinalizado(true);
            }
            documentoFacade.edit(documento);

//            CREACION DEL DETALLE MOVIMIENTO
            List<InvMovimientoDetalle> aux = new ArrayList();
            for (InvMovimientoDetalle detalleMovimiento : listaItemsInventarioMovimiento) {
                detalleMovimiento.setInvMovimientoDetallePK(new InvMovimientoDetallePK(invMovimientoMaestro.getInvMovimientoPK().getCfgdocumentoidDoc(), invMovimientoMaestro.getInvMovimientoPK().getNumDoc(), detalleMovimiento.getCfgProducto().getIdProducto()));
                detalleMovimiento.setInvMovimiento(invMovimientoMaestro);
                actualizarTablaProducto(detalleMovimiento);//actualiza la informacion del producto precio
                inventarioMovimientoDetalleFacade.create(detalleMovimiento);
                aux.add(detalleMovimiento);
            }

//            CREACION DE LA INFORMACION ADICIONAL DEL MOVIMIENTO. APLICA A SALIDA DE INVETARIO - TRASLADO ENTRE BODEGAS
            if (displayInfoAdicional) {
                InvMovimientoInfoadicional infoadicional = new InvMovimientoInfoadicional();
                infoadicional.setInvMovimiento(invMovimientoMaestro);
                infoadicional.setInvMovimientoInfoadicionalPK(new InvMovimientoInfoadicionalPK(invMovimientoMaestro.getInvMovimientoPK().getCfgdocumentoidDoc(), invMovimientoMaestro.getInvMovimientoPK().getNumDoc()));
                infoadicional.setInvMovimiento(invMovimientoMaestro);
                infoadicional.setCiudad(ciudad.toUpperCase());
                infoadicional.setDireccion(direccion.toUpperCase());
                infoadicional.setTransportadora(transportadora.toUpperCase());
                infoadicional.setConductor(conductor.toUpperCase());
                infoadicional.setPlaca(placa.toUpperCase());
                infoadicional.setPesoTotal(peso.toUpperCase());
                inventarioMovimientoInfoadicionalFacade.create(infoadicional);
                invMovimientoMaestro.setInvMovimientoInfoadicional(infoadicional);
            }
//            SE INCLUYE EL DETALLE DEL MOVIMIENTO AL MAESTRO
            invMovimientoMaestro.setInvMovimientoDetalleList(aux);
            inventarioMovimientoMaestroFacade.edit(invMovimientoMaestro);
            movimientoInventarioActual = invMovimientoMaestro;
            //se registra el cnt_mov_detalle correspondiente al movimiento de inventario
            cargaCntMovDetalle(invMovimientoMaestro);
            cancelar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Movimiento guardado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Movimiento no guardado"));
        }

    }

    private void cargaCntMovDetalle(InvMovimiento invMovimiento) {
        //EL TIPO DE MOVIMIENTO ES LA APLICACION DEL DOCUMENTO ACTUAL.
        int idTipoMovimiento = invMovimiento.getCfgDocumento().getCfgAplicaciondocumentoIdaplicacion().getIdaplicacion();
        //LA FORMA DE PAGO EN EL MOVIMIENTO DE INVENTARIO SE REDUCE AFECTIVO(1) Y CREDITO(2)
        int idFormaPago = 1;
        String codigoFormaPago = invMovimiento.getCfgformaPagoproveedoridFormaPago().getCodigo();
        if (codigoFormaPago.equals("1") || codigoFormaPago.equals("2")) {//FORMAPAGOPROVEEDOR -> EFECTIVO Y NO APLICA
            idFormaPago = 1;
        } else {
            idFormaPago = 2;
        }
        //LOS VALORES: SUBTOTAL, DESCUENTO, IMPUESTO, TOTAL PUEDEN IR A DIFERENTES CUENTAS SEGUN LA CONFIGURACION EXISTENTE EN cfg_mov_cta
        List<CfgMovCta> listaCfgMovCta = cfgMovCtaFacade.buscarPorTipoMovimientoAndFormaPago(sedeActual.getIdSede(), idTipoMovimiento, idFormaPago);
        for (CfgMovCta cmc : listaCfgMovCta) {
            CntPuc cntPuc = cmc.getCntpuccodigoCuenta();
            if (cntPuc != null && cmc.getAplica() != 0) {//SI SE ASIGNO UNA CUENTA PUC AL VALOR ACTUAL DE LA ITERACION Y SI TIENE ASIGANDO UNA APLICACION DIFERENTE A 0 (DEBE, HABER)
                try {
                    CntMovdetalle cntMovdetalle = new CntMovdetalle();
                    cntMovdetalle.setCfgempresasedeidSede(sedeActual);
                    cntMovdetalle.setFecha(new Date());
                    cntMovdetalle.setFacDocumentosmaster(null);//NO ES UNA FACTURA
                    cntMovdetalle.setInvMovimiento(invMovimiento);
                    //COMO ES UN MOVIMIENTO DE INVENTARIO EL PROVEEDOR ES EL TERCERO
                    String tercero = null;
                    if (invMovimiento.getCfgproveedoridProveedor() != null) {
                        tercero = invMovimiento.getCfgproveedoridProveedor().getNomProveedor();
                    }

                    if (tercero != null && tercero.length() > 150) {
                        tercero = tercero.substring(0, 149);
                    }
                    cntMovdetalle.setTercero(tercero);
                    cntMovdetalle.setCntpuccodigoCuenta(cntPuc);
                    cntMovdetalle.setDetalle(invMovimiento.getObservacion());
                    float valor = 0;
                    switch (cmc.getCntDetalle().getIdcntDetalle()) {
                        case 1://SUBTOTAL
                            valor = invMovimiento.getSubtotal();
                            break;
                        case 2://DESCUENTO
                            valor = invMovimiento.getDescuento();
                            break;
                        case 3://IMPUESTO
                            valor = invMovimiento.getIva();
                            break;
                        case 4://TOTAL
                            valor = invMovimiento.getTotal();
                            break;
                    }
                    int ban = cmc.getAplica();
                    float debito = 0;//debe
                    float credito = 0;//haber
                    if (ban == 1) {
                        debito = valor;
                    } else {
                        credito = valor;
                    }
                    cntMovdetalle.setDebito(debito);
                    cntMovdetalle.setCredito(credito);
                    float tot = debito - credito;
                    cntMovdetalle.setTotal(tot);
                    cntMovdetalleFacade.create(cntMovdetalle);
                } catch (Exception e) {
//                    System.out.println(e);
                    Logger.getLogger(FacturaMB.class.getName()).log(Level.SEVERE, null, e);
                }
            }

        }
    }

    private void actualizarTablaProducto(InvMovimientoDetalle detalle) {
        try {
            CfgProducto producto = detalle.getCfgProducto();
            //se actualiza la informacion del producto cuando el tipo de movimiento es de entrada
            if (tipoDocumento.equals("3")) {
                float aux = detalle.getCostoAdquisicion() * (detalle.getDescuento() / (float) 100);
                producto.setCostoAdquisicion(detalle.getCostoAdquisicion() - aux);
                producto.setIva(detalle.getIva());
                producto.setFlete(detalle.getFlete());
                producto.setCostoIndirecto(detalle.getCostoIndirecto());
                producto.setCosto(detalle.getCostoFinalIndividual());
                producto.setUtilidad(producto.getUtilidad());
                float utilidad = producto.getCosto() * producto.getUtilidad() / (float) 100;
                producto.setPrecio(producto.getCosto() + utilidad);
                productoFacade.edit(producto);
            }
            actualizarTablaConsolidado(producto, detalle);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se actualizo la informacion de los productos involucrados"));
        }
    }

    private void actualizarTablaConsolidado(CfgProducto producto, InvMovimientoDetalle detalleMovimiento) {
        try {
            InvConsolidado consolidado = consolidadoFacade.buscarBySedeAndProducto(getSedeActual(), producto);
            //el producto si no es un servicio existira en el inventario consolidado por cada cada sede de la empresa
            List<InvConsolidado> listadoConsolidado = producto.getInvConsolidadoList();
            if (consolidado != null) {
//                se determina que modificaciones se hara en el consolidado. De salida (resta) o entrada (suma)
                if (tipoDocumento.equals("3")) {
                    consolidado.setFechaUltEntrada(new Date());
                    consolidado.setExistencia(consolidado.getExistencia() + detalleMovimiento.getCantidad());
                    consolidado.setEntradas(consolidado.getEntradas() + detalleMovimiento.getCantidad());
                } else {
                    consolidado.setFechaUltSalida(new Date());
                    consolidado.setExistencia(consolidado.getExistencia() - detalleMovimiento.getCantidad());
                    consolidado.setSalidas(consolidado.getSalidas() + detalleMovimiento.getCantidad());
                }
                consolidadoFacade.edit(consolidado);
                //se identifica el index del listadoconsolidado afectado
                int aux = 0;
                for (InvConsolidado ic : listadoConsolidado) {
                    if (ic.equals(consolidado)) {
                        break;
                    }
                    aux++;
                }
                //se actualiza la informacion del consolidado afectado del producto en cuestion
                listadoConsolidado.get(aux).setCfgEmpresasede(consolidado.getCfgEmpresasede());
                listadoConsolidado.get(aux).setCfgProducto(consolidado.getCfgProducto());
                listadoConsolidado.get(aux).setEntradas(consolidado.getEntradas());
                listadoConsolidado.get(aux).setExistencia(consolidado.getExistencia());
                listadoConsolidado.get(aux).setFechaUltEntrada(consolidado.getFechaUltEntrada());
                listadoConsolidado.get(aux).setFechaUltSalida(consolidado.getFechaUltSalida());
                listadoConsolidado.get(aux).setInvConsolidadoPK(consolidado.getInvConsolidadoPK());
                listadoConsolidado.get(aux).setSalidas(consolidado.getSalidas());
            } else {
                consolidado = new InvConsolidado(new InvConsolidadoPK(producto.getIdProducto(), getSedeActual().getIdSede()));
                consolidado.setCfgEmpresasede(getSedeActual());
                consolidado.setCfgProducto(producto);
                consolidado.setFechaUltEntrada(new Date());
                consolidado.setEntradas(detalleMovimiento.getCantidad());
                consolidado.setExistencia(detalleMovimiento.getCantidad());
                consolidado.setSalidas(0);
                consolidadoFacade.create(consolidado);
                //se inserta  la nueva informacion del consolidado en listado del producto
                listadoConsolidado.add(consolidado);
            }
            //se guarda los cambios del listado inventario en el producto
            productoFacade.edit(producto);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se actualizo la informacion consolidada del inventario)"));
        }
    }

    public void impresion() {
        if (movimientoInventarioActual != null) {
            concecutivo = movimientoInventarioActual.determinarNumConcecutivo();
            RequestContext.getCurrentInstance().update("IdFormConfirmacion");
            RequestContext.getCurrentInstance().execute("PF('dlgResult').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay un movimiento reciente"));
        }
    }

    public void generarPDF() {
        if (movimientoInventarioActual != null) {
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String ruta = servletContext.getRealPath("/inventario/reportes/remisionInventario.jasper");
            try {
                generarRemision(ruta);
            } catch (IOException ex) {
                Logger.getLogger(FacturaMB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JRException ex) {
                Logger.getLogger(FacturaMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void generarRemision(String ruta) throws IOException, JRException {
        byte[] bites = getSedeActual().getLogo();
        if (bites != null) {
            bites = getSedeActual().getCfgempresaidEmpresa().getLogo();
        }
        List<RemisionReporte> lista = new ArrayList();
        RemisionReporte remision = new RemisionReporte();
        remision.setFecha(movimientoInventarioActual.getFecha());
        remision.setConsecutivo(movimientoInventarioActual.determinarNumConcecutivo());
        InvMovimientoInfoadicional infoadicional = movimientoInventarioActual.getInvMovimientoInfoadicional();
        if (infoadicional != null) {
            remision.setCiudad(infoadicional.getCiudad());
            remision.setDireccion(infoadicional.getDireccion());
            remision.setTransportadora(infoadicional.getTransportadora());
            remision.setConductor(infoadicional.getConductor());
            remision.setPlaca(infoadicional.getPlaca());
            remision.setPeso(infoadicional.getPesoTotal());
            remision.setDespachador(movimientoInventarioActual.getSegusuarioidUsuario().nombreCompleto());
        }
        remision.setObservacion(movimientoInventarioActual.getObservacion());
        remision.setDetalle(construirDetalle(movimientoInventarioActual.getInvMovimientoDetalleList()));
        lista.add(remision);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String rutaReportes = servletContext.getRealPath("/inventario/reportes/");//ubicacion para los subreportes
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
            parametros.put("ubicacion", getSedeActual().getCfgMunicipio().getNomMunicipio() + " " + getSedeActual().getCfgMunicipio().getCfgDepartamento().getNomDepartamento());
            parametros.put("SUBREPORT_DIR", rutaReportes);
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    private List<RemisionDetalleReporte> construirDetalle(List<InvMovimientoDetalle> detalle) {
        List<RemisionDetalleReporte> lista = new ArrayList();
        for (InvMovimientoDetalle movimientoDetalle : detalle) {
            RemisionDetalleReporte detalleReporte = new RemisionDetalleReporte();
            detalleReporte.setCodigo(movimientoDetalle.getCfgProducto().getCodProducto());
            detalleReporte.setCantidad(movimientoDetalle.getCantidad());
            detalleReporte.setProducto(movimientoDetalle.getCfgProducto().getNomProducto());
            lista.add(detalleReporte);
        }
        return lista;
    }

    public void cancelar() {
        numIdentificacion = null;
        listaMovInventarioDetalle.clear();
        limpiarFormulario();
        displayInfoAdicional = false;
        RequestContext.getCurrentInstance().update("IdFormMovimientoInventario");
    }

    public List<CfgMovInventarioMaestro> getListaMovInventarioMaestro() {
        return listaMovInventarioMaestro;
    }

    public List<CfgMovInventarioDetalle> getListaMovInventarioDetalle() {
        return listaMovInventarioDetalle;
    }

    public int getIdMovInventarioMaestro() {
        return idMovInventarioMaestro;
    }

    public void setIdMovInventarioMaestro(int idMovInventarioMaestro) {
        this.idMovInventarioMaestro = idMovInventarioMaestro;
    }

    public int getIdMovInventarioDetalle() {
        return idMovInventarioDetalle;
    }

    public void setIdMovInventarioDetalle(int idMovInventarioDetalle) {
        this.idMovInventarioDetalle = idMovInventarioDetalle;
    }

    public int getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(int formaPago) {
        this.formaPago = formaPago;
    }

    public List<CfgformaPagoproveedor> getListaFormaPago() {
        return listaFormaPago;
    }

    public LazyDataModel<CfgProveedor> getListaProveedor() {
        return listaProveedor;
    }

    public CfgProveedor getProveedorSeleccionado() {
        return proveedorSeleccionado;
    }

    public void setProveedorSeleccionado(CfgProveedor proveedorSeleccionado) {
        this.proveedorSeleccionado = proveedorSeleccionado;
    }

    public String getNumIdentificacion() {
        return numIdentificacion;
    }

    public void setNumIdentificacion(String numIdentificacion) {
        this.numIdentificacion = numIdentificacion;
    }

    public String getProveedor() {
        return proveedor;
    }

    public List<InvMovimientoDetalle> getListaItemsInventarioMovimiento() {
        return listaItemsInventarioMovimiento;
    }

    public void setListaItemsInventarioMovimiento(List<InvMovimientoDetalle> listaItemsInventarioMovimiento) {
        this.listaItemsInventarioMovimiento = listaItemsInventarioMovimiento;
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

    public float getTotalIva() {
        return totalIva;
    }

    public void setTotalIva(float totalIva) {
        this.totalIva = totalIva;
    }

    public float getTotalMovimiento() {
        return totalMovimiento;
    }

    public void setTotalMovimiento(float totalMovimiento) {
        this.totalMovimiento = totalMovimiento;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getDocumentoSoporte() {
        return documentoSoporte;
    }

    public void setDocumentoSoporte(String documentoSoporte) {
        this.documentoSoporte = documentoSoporte;
    }

    public CfgEmpresasede getSedeActual() {
        return sedeActual;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(String transportadora) {
        this.transportadora = transportadora;
    }

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public boolean isDisplayInfoAdicional() {
        return displayInfoAdicional;
    }

    public String getConcecutivo() {
        return concecutivo;
    }

}
