/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.proceso;

import entities.CfgDocumento;
import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.CfgMovInventarioDetalle;
import entities.CfgMovInventarioMaestro;
import entities.CfgProducto;
import entities.CfgProveedor;
import entities.CfgformaPagoproveedor;
import entities.InvConsolidado;
import entities.InvConsolidadoPK;
import entities.InvMovimiento;
import entities.InvMovimientoDetalle;
import entities.InvMovimientoDetallePK;
import entities.InvMovimientoPK;
import entities.SegUsuario;
import facades.CfgDocumentoFacade;
import facades.CfgMovInventarioDetalleFacade;
import facades.CfgMovInventarioMaestroFacade;
import facades.CfgProductoFacade;
import facades.CfgProveedorFacade;
import facades.CfgformaPagoproveedorFacade;
import facades.InvConsolidadoFacade;
import facades.InvMovimientoDetalleFacade;
import facades.InvMovimientoFacade;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.ArrayList;
import managedBeans.seguridad.SesionMB;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import java.util.Date;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.LazyDataModel;
import utilities.LazyProductosModel;
import utilities.LazyProveedorDataModel;

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
    private float subtotal;
    private float totalDescuento;
    private float totalIva;
    private float totalMovimiento;

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
    InvConsolidadoFacade consolidadoFacade;

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
        if (empresaActual != null) {
            listaMovInventarioMaestro = movInventarioMaestroFacade.buscarByEmpresa(empresaActual);
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
            formaPago = proveedorSeleccionado.getCfgformaPagoproveedoridFormaPago().getIdFormaPago();
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
            InvMovimientoDetalle detalle = estaInsertado(productoSeleccionado);
            if (detalle == null) {
                detalle = new InvMovimientoDetalle();
                detalle.setInvMovimientoDetallePK(new InvMovimientoDetallePK(0, 0, productoSeleccionado.getIdProducto()));
                detalle.setCfgProducto(productoSeleccionado);
                detalle.setCantidad(1);
                detalle.setCostoAdquisicion(0);
                detalle.setDescuento(0);
                detalle.setFlete(0);
//                detalle.setIva(0);
                detalle.setIva(16);
                detalle.setCostoIndirecto(0);
                detalle.setInvMovimiento(null);//se crea cuando se guarde
                listaItemsInventarioMovimiento.add(detalle);
            } else {
                detalle.setCantidad(detalle.getCantidad() + 1);
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
        detalle.setValorDescuento(descuento);
//        costoAdq = costoAdq - (costoAdq * (descuento / 100));
        costoFinal = costoAdq + (costoAdq * (flete / 100));
        costoFinal = costoFinal + (costoFinal * (costoInd / 100));
        costoFinal = costoFinal * cantidad;//el valor mostrado en la tabla se aplica el iva
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
        totalMovimiento = subtotal - totalDescuento + totalIva;
    }

    private void determinarCostosIndividualProducto(InvMovimientoDetalle detalle) {
        float costoFinalIndividual = 0;
        float costoAdq = detalle.getCostoAdquisicion();
        costoAdq = validarValor(costoAdq);
        float iva = detalle.getIva();
        iva = validarValor(iva);
        float flete = detalle.getFlete();
        flete = validarValor(flete);
        float costoInd = detalle.getCostoIndirecto();
        costoInd = validarValor(costoInd);
        iva = costoAdq * (iva / 100);
        costoFinalIndividual = costoAdq + iva;
        costoFinalIndividual = costoFinalIndividual + (costoFinalIndividual * (flete / 100));
        costoFinalIndividual = costoFinalIndividual + (costoFinalIndividual * (costoInd / 100));
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

    public boolean validar() {
        boolean ban = true;
        if (sedeActual == null) {
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
        if (proveedorSeleccionado == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione el proveedor"));
            return false;
        }
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
        return ban;
    }

    public void guardar() {
        if (!validar()) {
            return;
        }
        CfgMovInventarioMaestro movInventarioMaestro = movInventarioMaestroFacade.find(idMovInventarioMaestro);
//        si el tipo de movimiento es de codigo = 1. El documento a aplicar es el de ENTRADA INVENTARIO. De ser el codigo = 2. Se aplicara el documento SALIDA INVENTARIO
        tipoDocumento = movInventarioMaestro.getCodMovInvetario().equals("1") ? "3" : "4";
        CfgDocumento documento = documentoFacade.buscarDocumentoDeMovInventarioBySede(sedeActual, tipoDocumento);
        if (documento == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay un documento existente ó sin finalizar aplicado a este tipo de movimiento"));
            return;
        }
        if (documento.getActDocumento() == 0) {
            documento.setActDocumento(documento.getIniDocumento());
        } else {
            documento.setActDocumento(documento.getActDocumento() + 1);
        }
        if (documento.getFinDocumento() < documento.getActDocumento()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ha llegado al limite de creacion de este tipo de movimiento. Revice la configuracion de documentos"));
            return;
        }
        CfgMovInventarioDetalle movInventarioDetalle = movInventarioDetalleFacade.find(idMovInventarioDetalle);
        try {
//            CREACION DEL MAESTRO MOVIMIENTO 
            InvMovimiento invMovimientoMaestro = new InvMovimiento();
            invMovimientoMaestro.setInvMovimientoPK(new InvMovimientoPK(documento.getIdDoc(), documento.getActDocumento()));
            invMovimientoMaestro.setCfgDocumento(documento);
            invMovimientoMaestro.setCfgempresasedeidSede(sedeActual);
            CfgformaPagoproveedor pagoproveedor = pagoproveedorFacade.find(formaPago);
            invMovimientoMaestro.setCfgformaPagoproveedoridFormaPago(pagoproveedor);
            invMovimientoMaestro.setCfgmovinventariodetalleidMovInventarioDetalle(movInventarioDetalle);
            invMovimientoMaestro.setCfgproveedoridProveedor(proveedorSeleccionado);
            invMovimientoMaestro.setDescuento(totalDescuento);
            invMovimientoMaestro.setFecha(new Date());
            invMovimientoMaestro.setIva(totalIva);
            invMovimientoMaestro.setObservacion(observacion);
            invMovimientoMaestro.setSegusuarioidUsuario(usuarioActual);
            invMovimientoMaestro.setSubtotal(subtotal);
            invMovimientoMaestro.setTotal(totalMovimiento);
            inventarioMovimientoMaestroFacade.create(invMovimientoMaestro);

//            CREACION DEL DETALLE MOVIMIENTO
            for (InvMovimientoDetalle detalleMovimiento : listaItemsInventarioMovimiento) {
                detalleMovimiento.setInvMovimientoDetallePK(new InvMovimientoDetallePK(invMovimientoMaestro.getInvMovimientoPK().getCfgdocumentoidDoc(), invMovimientoMaestro.getInvMovimientoPK().getNumDoc(), detalleMovimiento.getCfgProducto().getIdProducto()));
                detalleMovimiento.setInvMovimiento(invMovimientoMaestro);
                actualizarTablaProducto(detalleMovimiento);//actualiza la informacion del producto precio
                inventarioMovimientoDetalleFacade.create(detalleMovimiento);
            }
            cancelar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Movimiento guardado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Movimiento no guardado"));
        }

    }

    private void actualizarTablaProducto(InvMovimientoDetalle detalle) {
        try {
            CfgProducto producto = detalle.getCfgProducto();
            producto.setCostoAdquisicion(detalle.getCostoAdquisicion());
            producto.setIva(detalle.getIva());
            producto.setFlete(detalle.getFlete());
            producto.setCostoIndirecto(detalle.getCostoIndirecto());
            producto.setCosto(detalle.getCostoFinalIndividual());
            producto.setUtilidad(producto.getUtilidad());
            float utilidad = producto.getCosto() * producto.getUtilidad() / (float) 100;
            producto.setPrecio(producto.getCosto() + utilidad);
            productoFacade.edit(producto);
            actualizarTablaConsolidado(producto, detalle);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se actualizo la informacion de los productos involucrados"));
        }
    }

    private void actualizarTablaConsolidado(CfgProducto producto, InvMovimientoDetalle detalleMovimiento) {
        try {
            InvConsolidado consolidado = consolidadoFacade.buscarByEmpresaAndProducto(sedeActual, producto);
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
            } else {
                consolidado = new InvConsolidado(new InvConsolidadoPK(producto.getIdProducto(), sedeActual.getIdSede()));
                consolidado.setCfgEmpresasede(sedeActual);
                consolidado.setCfgProducto(producto);
                consolidado.setFechaUltEntrada(new Date());
                consolidado.setEntradas(detalleMovimiento.getCantidad());
                consolidado.setExistencia(detalleMovimiento.getCantidad());
                consolidado.setSalidas(0);
                consolidadoFacade.create(consolidado);
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se actualizo la informacion consolidada del inventario)"));
        }
    }

    public void cancelar() {
        numIdentificacion = null;
        limpiarFormulario();
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

}
