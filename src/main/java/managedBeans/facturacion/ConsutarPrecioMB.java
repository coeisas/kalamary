/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.CfgProducto;
import facades.CfgProductoFacade;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import managedBeans.seguridad.SesionMB;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import utilities.LazyProductosModel;

/**
 *
 * @author Mario
 */
@ManagedBean
@SessionScoped
public class ConsutarPrecioMB implements Serializable {

    private String codigoProducto;
    private String nombreProducto;
    private float precioProducto;

    private LazyDataModel<CfgProducto> listaProducto;
    private CfgProducto productoSeleccionado;

    private CfgEmpresa empresaActual;
    private CfgEmpresasede sedeActual;

    @EJB
    CfgProductoFacade productoFacade;

    public ConsutarPrecioMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        empresaActual = sesionMB.getEmpresaActual();
        sedeActual = sesionMB.getSedeActual();
    }

    public void cargarModalProductos() {
        if (empresaActual != null && sedeActual != null) {
            listaProducto = new LazyProductosModel(empresaActual, null, null, null, productoFacade);
            RequestContext.getCurrentInstance().update("FormModalProducto");
            RequestContext.getCurrentInstance().execute("PF('dlgProducto').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Empresa o Sede no cargadas correctamente. Reinicie sesion"));
        }
    }

    public void buscarProducto() {
        productoSeleccionado = null;
        if (empresaActual != null) {
            if (!codigoProducto.trim().isEmpty()) {
                productoSeleccionado = productoFacade.buscarPorEmpresaAndCodigo(empresaActual, codigoProducto);
            }
        }
        cargarInformacionProducto();
    }

    public void cargarInformacionProducto() {
        if (productoSeleccionado != null) {
            nombreProducto = productoSeleccionado.getNomProducto();
            codigoProducto = productoSeleccionado.getCodProducto();
            if (productoSeleccionado.getPrecio() != null) {
                precioProducto = productoSeleccionado.getPrecio();
            } else {
                precioProducto = 0;
            }
        } else {
            nombreProducto = "";
            codigoProducto = "";
            precioProducto = 0;
        }
        RequestContext.getCurrentInstance().update("IdFormPrecios");
        RequestContext.getCurrentInstance().execute("PF('dlgProducto').hide()");
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public float getPrecioProducto() {
        return precioProducto;
    }

    public LazyDataModel<CfgProducto> getListaProducto() {
        return listaProducto;
    }

    public CfgProducto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(CfgProducto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public CfgEmpresasede getSedeActual() {
        return sedeActual;
    }

}
