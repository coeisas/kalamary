/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.proceso;

import entities.CfgEmpresa;
import entities.CfgMovInventarioDetalle;
import entities.CfgMovInventarioMaestro;
import entities.CfgProducto;
import entities.CfgProveedor;
import entities.CfgformaPagoproveedor;
import entities.InvMovimientoDetalle;
import entities.SegUsuario;
import facades.CfgMovInventarioDetalleFacade;
import facades.CfgMovInventarioMaestroFacade;
import facades.CfgProductoFacade;
import facades.CfgProveedorFacade;
import facades.CfgformaPagoproveedorFacade;
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
import org.primefaces.context.RequestContext;
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

    private List<CfgMovInventarioMaestro> listaMovInventarioMaestro;
    private List<CfgMovInventarioDetalle> listaMovInventarioDetalle;
    private List<CfgformaPagoproveedor> listaFormaPago;
    private LazyDataModel<CfgProveedor> listaProveedor;
    private LazyDataModel<CfgProducto> listaProducto;
    private List<InvMovimientoDetalle> listaItemsInventarioMovimiento;

    private CfgEmpresa empresaActual;
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

    public MovimientoInventarioMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        empresaActual = sesionMB.getEmpresaActual();
        usuarioActual = sesionMB.getUsuarioActual();
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
        }
        RequestContext.getCurrentInstance().execute("PF('dlgProveedor').hide()");
        RequestContext.getCurrentInstance().update("IdFormMovimientoInventario");
    }

    private void limpiarFormulario() {
        idMovInventarioMaestro = 0;
        idMovInventarioDetalle = 0;
        proveedor = null;
        formaPago = 0;
        listaItemsInventarioMovimiento.clear();

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

    public void insertarItemProducto() {
        if (productoSeleccionado != null) {
        }
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

}
