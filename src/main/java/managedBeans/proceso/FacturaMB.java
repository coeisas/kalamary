/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.proceso;

import entities.CfgCliente;
import entities.CfgEmpresasede;
import entities.CfgImpuesto;
import entities.CfgProducto;
import entities.FacDocumentodetalle;
import entities.FacDocumentodetallePK;
import entities.SegUsuario;
import facades.CfgClienteFacade;
import facades.CfgImpuestoFacade;
import facades.CfgProductoFacade;
import java.util.List;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.primefaces.context.RequestContext;
import javax.faces.context.FacesContext;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import managedBeans.seguridad.SesionMB;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.LazyDataModel;
import utilities.LazyProductosModel;

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
//    private CfgEmpresa empresaActual;
    private CfgEmpresasede sedeActual;
    private CfgCliente clienteSeleccionado;
    private CfgProducto productoSeleccionado;
    private SegUsuario usuarioActual;

    private SesionMB sesionMB;
    @EJB
    CfgClienteFacade clienteFacade;
    @EJB
    CfgImpuestoFacade impuestoFacade;
    @EJB
    CfgProductoFacade productoFacade;

    public FacturaMB() {

    }

    @PostConstruct
    private void init() {
        //si es super usuario o admin permitir escoger la empresa y la sede
        setListaDetalle((List<FacDocumentodetalle>) new ArrayList());
        setListaImpuestos((List<CfgImpuesto>) new ArrayList());
        FacesContext context = FacesContext.getCurrentInstance();
        sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
//        empresaActual = sesionMB.getSedeActual().getCfgempresaidEmpresa();
        sedeActual = sesionMB.getSedeActual();
        if (sedeActual != null) {
//            if (usuarioActual.getCfgempresasedeidSede() != null) {
//                empresaActual = usuarioActual.getCfgempresasedeidSede().getCfgempresaidEmpresa();
//            } else {
//                empresaActual = null;
//            }
            actualizarListadoClientes();
            listaProducto = new LazyProductosModel(sedeActual.getCfgempresaidEmpresa(), null, null, null, productoFacade);
            RequestContext.getCurrentInstance().update("FormModalProducto");
            clienteSeleccionado = clienteFacade.buscarClienteDefault(sedeActual.getCfgempresaidEmpresa());
            setListaImpuestos(impuestoFacade.buscarImpuestosPorTipoEmpresaAndSede(clienteSeleccionado.getCfgTipoempresaId(), sedeActual));
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
            setListaImpuestos(impuestoFacade.buscarImpuestosPorTipoEmpresaAndSede(clienteSeleccionado.getCfgTipoempresaId(), sedeActual));
        } else {
            setNombreCliente(null);
            getListaImpuestos().clear();
        }
        RequestContext.getCurrentInstance().execute("PF('dlgCliente').hide()");
        RequestContext.getCurrentInstance().update("IdFormFactura:IdNomCliente");
    }

    private void actualizarListadoClientes() {
        listaClientes = clienteFacade.buscarPorEmpresaMenosClienteDefault(sedeActual.getCfgempresaidEmpresa());
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
                facdetalle.setFacDocumentodetallePK(new FacDocumentodetallePK(productoSeleccionado.getIdProducto(), 1));
                facdetalle.setCantidad(1);
                facdetalle.setDescuento(0);
                facdetalle.setValorUnitario(productoSeleccionado.getPrecio());
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
        listaDetalle.get(index).setValorDescuento(descuento);
        subtotal += listaDetalle.get(index).getValorTotal();
        calcularImpuesto();
        calcularTotalDescuento();
        calcularTotalFactura();
        RequestContext.getCurrentInstance().update("IdFormFactura:IdSubTotal");
    }

    private void calcularTotalDescuento() {
        totalDescuento = 0;
        for (FacDocumentodetalle documentodetalle : listaDetalle) {
            totalDescuento += documentodetalle.getValorDescuento();
        }
    }

    private void calcularImpuesto() {
        for (CfgImpuesto impuesto : listaImpuestos) {
            impuesto.setTotalImpuesto(impuesto.getPorcentaje() * subtotal / (float) 100);
        }
    }

    private void calcularTotalFactura() {
        totalFactura = subtotal - totalDescuento;
        for (CfgImpuesto impuesto : listaImpuestos) {
            totalFactura +=impuesto.getTotalImpuesto();
        }
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

}
