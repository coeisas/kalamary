/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.configuracion;

import entities.CfgEmpresa;
import entities.CfgKitproductodetalle;
import entities.CfgKitproductomaestro;
import entities.CfgProducto;
import facades.CfgEmpresaFacade;
import facades.CfgKitproductodetalleFacade;
import facades.CfgKitproductomaestroFacade;
import facades.CfgProductoFacade;
import javax.faces.bean.ManagedBean;
import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import utilities.LazyProductosModel;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class KitMB implements Serializable {

    private String codigoEmpresa;
    private String codigoKit;
    private String nombreKit;
    private float costo;
    private float utilidad;
    private float precio;
    private boolean activo;

    private String nombreEmpresa;

    private CfgEmpresa empresaSeleccionada;
    private CfgProducto productoSeleccionado;
    private CfgKitproductomaestro kitMaestroSeleccionado;
    private CfgKitproductodetalle kitDetalleSeleccionado;

    private LazyDataModel<CfgProducto> listaProducto;
    private List<CfgKitproductomaestro> listaKitMaestro;
    private List<CfgKitproductodetalle> listaKitDetalle;

    @EJB
    CfgEmpresaFacade empresaFacade;
    @EJB
    CfgProductoFacade productoFacade;
    @EJB
    CfgKitproductomaestroFacade kitMaestroFacade;
    @EJB
    CfgKitproductodetalleFacade kitDetalleFacade;

    public KitMB() {
    }

    @PostConstruct
    private void init() {
        activo = true;
        listaKitMaestro = new ArrayList();
        setListaKitDetalle((List<CfgKitproductodetalle>) new ArrayList());
    }

    public void buscarEmpresa() {
        empresaSeleccionada = empresaFacade.buscarEmpresaPorCodigo(codigoEmpresa.trim());
        if (!codigoEmpresa.trim().isEmpty() && empresaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No se econtro empresa"));
        }
        cargarInformacionEmpresa();
    }

    public void cargarInformacionEmpresa() {
        listaKitMaestro.clear();
//        limpiarInformacionEmpresa();
        if (empresaSeleccionada != null) {
            setCodigoEmpresa(empresaSeleccionada.getCodEmpresa());
            setNombreEmpresa(empresaSeleccionada.getNomEmpresa());
            listaKitMaestro = kitMaestroFacade.buscarPorEmpresa(empresaSeleccionada);
//            listaProducto = productoFacade.buscarPorEmpresa(empresaSeleccionada);
        } else {
            limpiarInformacionEmpresa();
            limpiarInformacionKit();
        }
        RequestContext.getCurrentInstance().update("IdFormKit");
    }

    public void cargarProductos() {
        listaProducto = new LazyProductosModel(empresaSeleccionada, null, null, null, productoFacade);
        if (empresaSeleccionada != null) {                        
            RequestContext.getCurrentInstance().update("FormModalProducto");
            RequestContext.getCurrentInstance().execute("PF('dlgProducto').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Determine la empresa"));
        }

    }

    public void buscarKit() {
        if (empresaSeleccionada != null && !codigoKit.trim().isEmpty()) {
            kitMaestroSeleccionado = kitMaestroFacade.buscarPorEmpresaAndCodigo(empresaSeleccionada, codigoKit);
        } else {
            kitMaestroSeleccionado = null;
        }
        cargarInformacionKit();
    }

    public void cargarInformacionKit() {
        if (kitMaestroSeleccionado != null) {
            setListaKitDetalle(kitDetalleFacade.findAll());
            setCodigoKit(kitMaestroSeleccionado.getCodKit());
            setNombreKit(kitMaestroSeleccionado.getNomKit());
            setCosto(kitMaestroSeleccionado.getCosto());
            setUtilidad(kitMaestroSeleccionado.getUtilidad());
            setPrecio(kitMaestroSeleccionado.getPrecio());
            setActivo(kitMaestroSeleccionado.getActivo());
        } else {
            limpiarInformacionKit();
        }
        RequestContext.getCurrentInstance().update("IdFormKit");
    }

    public void cargarItemKit() {

    }

    public void limpiarInformacionEmpresa() {
        setCodigoEmpresa(null);
        setNombreEmpresa(null);
        listaKitMaestro.clear();
        setEmpresaSeleccionada(null);
    }

    public void limpiarInformacionKit() {
        getListaKitDetalle().clear();
        kitMaestroSeleccionado = null;
        setNombreKit(null);
        setCosto(0);
        setUtilidad(0);
        setPrecio(0);
        setActivo(true);
    }

    public String getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(String codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public String getCodigoKit() {
        return codigoKit;
    }

    public void setCodigoKit(String codigoKit) {
        this.codigoKit = codigoKit;
    }

    public String getNombreKit() {
        return nombreKit;
    }

    public void setNombreKit(String nombreKit) {
        this.nombreKit = nombreKit;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public float getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(float utilidad) {
        this.utilidad = utilidad;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public CfgEmpresa getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(CfgEmpresa empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
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

    public CfgKitproductomaestro getKitMaestroSeleccionado() {
        return kitMaestroSeleccionado;
    }

    public void setKitMaestroSeleccionado(CfgKitproductomaestro kitMaestroSeleccionado) {
        this.kitMaestroSeleccionado = kitMaestroSeleccionado;
    }

    public List<CfgKitproductomaestro> getListaKitMaestro() {
        return listaKitMaestro;
    }

    public void setListaKitMaestro(List<CfgKitproductomaestro> listaKitMaestro) {
        this.listaKitMaestro = listaKitMaestro;
    }

    public List<CfgKitproductodetalle> getListaKitDetalle() {
        return listaKitDetalle;
    }

    public void setListaKitDetalle(List<CfgKitproductodetalle> listaKitDetalle) {
        this.listaKitDetalle = listaKitDetalle;
    }

}
