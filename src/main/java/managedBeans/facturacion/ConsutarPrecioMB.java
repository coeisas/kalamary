/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import com.google.common.io.Files;
import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.CfgProducto;
import facades.CfgProductoFacade;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import managedBeans.seguridad.SesionMB;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
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

    private StreamedContent image;
    private String pathRoot;

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
        pathRoot = context.getExternalContext().getInitParameter("directory.images");
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

    public void buscarProducto() throws IOException {
        productoSeleccionado = null;
        if (empresaActual != null) {
            if (!codigoProducto.trim().isEmpty()) {
                productoSeleccionado = productoFacade.buscarPorEmpresaAndCodigo(empresaActual, codigoProducto);
            }
        }
        cargarInformacionProducto();
    }

    public void cargarInformacionProducto() throws IOException {
        if (productoSeleccionado != null) {
            nombreProducto = productoSeleccionado.getNomProducto();
            codigoProducto = productoSeleccionado.getCodProducto();
            if (productoSeleccionado.getPrecio() != null) {
                precioProducto = productoSeleccionado.getPrecio();
            } else {
                precioProducto = 0;
            }
            String path;
            if (productoSeleccionado.getImgProducto() == null) {//si el producto no tiene una imagen asociada se muestra la imagen por default
                path = pathRoot.concat("0.jpg");
            } else {
                path = pathRoot.concat(productoSeleccionado.getImgProducto());
            }
            File f = new File(path);
            byte[] data = Files.toByteArray(f);
            image = new DefaultStreamedContent(new ByteArrayInputStream(data));
            setImage(image);
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

    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {//fase de jsf
            image = new DefaultStreamedContent();
            return image;
        } else if (productoSeleccionado != null) {
            String codInterno = context.getExternalContext().getRequestParameterMap().get("id");
            CfgProducto producto = productoFacade.buscarPorCodigoInterno(empresaActual.getIdEmpresa(), codInterno);
            String path;
            if (producto.getImgProducto() == null) {//si el producto no tiene una imagen asociada se muestra la imagen por default
                path = pathRoot.concat("0.jpg");
            } else {
                path = pathRoot.concat(producto.getImgProducto());
            }
            File f = new File(path);
            byte[] data = Files.toByteArray(f);
            image = new DefaultStreamedContent(new ByteArrayInputStream(data));
            return image;
        } else {
            return null;
        }
    }

    public void setImage(StreamedContent image) {
        this.image = image;
    }

}
