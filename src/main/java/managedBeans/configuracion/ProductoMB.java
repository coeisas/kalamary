/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.configuracion;

import entities.CfgCategoriaproducto;
import entities.CfgEmpresa;
import entities.CfgMarcaproducto;
import entities.CfgProducto;
import entities.CfgReferenciaproducto;
import facades.CfgCategoriaproductoFacade;
import facades.CfgEmpresaFacade;
import facades.CfgMarcaproductoFacade;
import facades.CfgProductoFacade;
import facades.CfgReferenciaproductoFacade;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import org.primefaces.model.LazyDataModel;
import utilities.LazyProductosModel;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class ProductoMB implements Serializable {

    private String codigoEmpresa;
    private String codigoCategoria;
    private String codigoReferencia;
    private String codigoMarca;
    private String codigoProducto;
    private String codigoBarra;
    private String color;
    private String talla;
    private String nombreProducto;
    private Float stockMin;
    private Float costoAdq;
    private Float iva;
    private Float flete;
    private Float costoInd;
    private Float costoFinal;
    private Float utilidad;
    private Float precio;
    private boolean activo;

    private String nombreEmpresa;

    private List<CfgCategoriaproducto> listaCategoria;
    private List<CfgReferenciaproducto> listaReferencia;
    private List<CfgMarcaproducto> listaMarca;
    private LazyDataModel<CfgProducto> listaProducto;
    private List<String> listFormsModal;

    private CfgEmpresa empresaSeleccionada;
    private CfgCategoriaproducto categoriaSeleccionada;
    private CfgReferenciaproducto referenciaSeleccionada;
    private CfgMarcaproducto marcaSeleccionada;
    private CfgProducto productoSeleccionado;

    @EJB
    CfgEmpresaFacade empresaFacade;

    @EJB
    CfgCategoriaproductoFacade categoriaFacade;

    @EJB
    CfgReferenciaproductoFacade referenciaFacade;

    @EJB
    CfgMarcaproductoFacade marcaFacade;

    @EJB
    CfgProductoFacade productoFacade;

    public ProductoMB() {
    }

    @PostConstruct
    private void init() {
        listaCategoria = new ArrayList();
        listaReferencia = new ArrayList();
        listaMarca = new ArrayList();
        listFormsModal = new ArrayList();
        listFormsModal.add("FormModalCategoria");
        listFormsModal.add("FormModalReferencia");
        listFormsModal.add("FormModalMarca");
        listFormsModal.add("FormModalProducto");
    }

    public void buscarEmpresa() {
        empresaSeleccionada = empresaFacade.buscarEmpresaPorCodigo(codigoEmpresa.trim());
        if (!codigoEmpresa.trim().isEmpty() && empresaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No se econtro empresa"));
        }
        cargarInformacionEmpresa();
    }

    public void cargarInformacionEmpresa() {
        listaCategoria.clear();
//        getListaProducto().clear();
        limpiarFormulario();
        if (empresaSeleccionada != null) {
            setCodigoEmpresa(empresaSeleccionada.getCodEmpresa());
            setNombreEmpresa(empresaSeleccionada.getNomEmpresa());
            listaCategoria = categoriaFacade.buscarPorEmpresa(empresaSeleccionada);
//            listaProducto = productoFacade.buscarPorEmpresa(empresaSeleccionada);
        } else {
            setCodigoEmpresa(null);
            setNombreEmpresa(null);
        }
        listaReferencia.clear();
        listaMarca.clear();
        RequestContext.getCurrentInstance().update("IdFormProducto");
        RequestContext.getCurrentInstance().update(listFormsModal);
    }

    private void limpiarFormulario() {
        codigoEmpresa = null;
        codigoCategoria = null;
        codigoReferencia = null;
        codigoMarca = null;
        codigoProducto = null;
        codigoBarra = null;
        color = null;
        talla = null;
        nombreProducto = null;
        stockMin = null;
        costoAdq = null;
        iva = null;
        flete = null;
        costoInd = null;
        costoFinal = null;
        utilidad = null;
        precio = null;
        activo = false;
    }

    public void buscarCategoria() {
        if (empresaSeleccionada != null) {
            categoriaSeleccionada = categoriaFacade.buscarPorEmpresaAndCodigo(empresaSeleccionada, codigoCategoria);
            cargarInformacionCategoria();
            if (categoriaSeleccionada == null && !codigoCategoria.trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No se econtro la categoria"));
            }
        } else {
            setCategoriaSeleccionada(null);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No ha determinado la empresa"));
        }

    }

    public void buscarReferencia() {
        if (categoriaSeleccionada != null) {
            referenciaSeleccionada = referenciaFacade.buscarPorCategoriaAndCodigo(categoriaSeleccionada, codigoReferencia);
            cargarInformacionReferencia();
            if (referenciaSeleccionada == null && !codigoReferencia.trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No se econtro la referencia"));
            }
        } else {
            setReferenciaSeleccionada(null);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No ha determinado la categoria"));
        }
    }

    public void cargarInformacionCategoria() {
        if (categoriaSeleccionada != null) {
            setCodigoCategoria(categoriaSeleccionada.getCodigoCategoria());
            listaReferencia = referenciaFacade.buscarPorCategoria(categoriaSeleccionada);
        } else {
            listaReferencia.clear();
        }
        RequestContext.getCurrentInstance().update("FormModalReferencia");
        RequestContext.getCurrentInstance().update("IdFormProducto");
    }

    public void cargarInformacionReferencia() {
        if (referenciaSeleccionada != null) {
            setCodigoReferencia(referenciaSeleccionada.getCodigoReferencia());
            listaMarca = marcaFacade.buscarPorReferencia(referenciaSeleccionada);
        } else {
            listaMarca.clear();
        }
        RequestContext.getCurrentInstance().update("FormModalMarca");
        RequestContext.getCurrentInstance().update("IdFormProducto");
    }

    public void buscarMarca() {
        if (referenciaSeleccionada != null) {
            marcaSeleccionada = marcaFacade.buscarPorReferenciaAndCodigo(referenciaSeleccionada, codigoMarca);
            cargarInformacionMarca();
            if (!codigoMarca.trim().isEmpty() && marcaSeleccionada == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No se encontro la marca"));
            }
        } else {
            setMarcaSeleccionada(null);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No ha determinado la referencia"));
        }
    }

    public void cargarInformacionMarca() {
        if (marcaSeleccionada != null) {
            setCodigoMarca(marcaSeleccionada.getCodigoMarca());
        }
        RequestContext.getCurrentInstance().update("IdFormProducto");
    }

    public void cargarProductos() {
        listaProducto = new LazyProductosModel(empresaSeleccionada, marcaSeleccionada, referenciaSeleccionada, categoriaSeleccionada, productoFacade);
        if (empresaSeleccionada != null) {                        
            RequestContext.getCurrentInstance().update("FormModalProducto");
            RequestContext.getCurrentInstance().execute("PF('dlgProducto').show()");
        } else {
//            getListaProducto().clear();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Determine la empresa"));
        }
    }

    public void buscarProducto() {
        if (empresaSeleccionada != null) {
            if (marcaSeleccionada != null) {
                productoSeleccionado = productoFacade.buscarPorEmpresaAndMarcaAndCodigo(empresaSeleccionada, marcaSeleccionada, codigoProducto.trim());
            } else if (referenciaSeleccionada != null) {
                productoSeleccionado = productoFacade.buscarPorEmpresaAndReferenciaAndCodigo(empresaSeleccionada, referenciaSeleccionada, codigoProducto.trim());
            } else if (categoriaSeleccionada != null) {
                productoSeleccionado = productoFacade.buscarPorEmpresaAndCategoriaAndCodigo(empresaSeleccionada, categoriaSeleccionada, codigoProducto.trim());
            } else {
                productoSeleccionado = productoFacade.buscarPorEmpresaAndCodigo(empresaSeleccionada, codigoProducto.trim());
            }
            cargarInformacionProducto();
            if (productoSeleccionado == null && !codigoProducto.trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No se encontro el producto"));
            }
        } else {
            productoSeleccionado = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No ha determinado la empresa"));
        }

    }

    public void cargarInformacionProducto() {
        if (productoSeleccionado != null) {
            setMarcaSeleccionada(productoSeleccionado.getCfgmarcaproductoidMarca());
            setReferenciaSeleccionada(marcaSeleccionada.getCfgreferenciaproductoidReferencia());
            setCategoriaSeleccionada(referenciaSeleccionada.getCfgcategoriaproductoidCategoria());
            setCodigoCategoria(categoriaSeleccionada.getCodigoCategoria());
            setCodigoReferencia(referenciaSeleccionada.getCodigoReferencia());
            setCodigoMarca(marcaSeleccionada.getCodigoMarca());
            setCodigoProducto(productoSeleccionado.getCodProducto());
            setCodigoBarra(productoSeleccionado.getCodBarProducto());
            setColor(productoSeleccionado.getColor());
            setTalla(productoSeleccionado.getTalla());
            setNombreProducto(productoSeleccionado.getNomProducto());
            setStockMin(productoSeleccionado.getStockMin());
            setCostoAdq(productoSeleccionado.getCostoAdquisicion());
            setIva(productoSeleccionado.getIva());
            setFlete(productoSeleccionado.getFlete());
            setCostoInd(productoSeleccionado.getCostoIndirecto());
            setCostoFinal(productoSeleccionado.getCosto());
            setUtilidad(productoSeleccionado.getUtilidad());
            setPrecio(productoSeleccionado.getPrecio());
            setActivo(productoSeleccionado.getActivo());

        } else {
            setCodigoBarra(null);
            setColor(null);
            setTalla(null);
            setNombreProducto(null);
            setStockMin(null);
            setCostoAdq(null);
            setIva(null);
            setFlete(null);
            setCostoInd(null);
            setCostoFinal(null);
            setUtilidad(null);
            setPrecio(null);
            setActivo(false);
        }
        RequestContext.getCurrentInstance().update("IdFormProducto");
        RequestContext.getCurrentInstance().update(listFormsModal);
    }

    public String getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(String codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public String getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(String codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }

    public String getCodigoReferencia() {
        return codigoReferencia;
    }

    public void setCodigoReferencia(String codigoReferencia) {
        this.codigoReferencia = codigoReferencia;
    }

    public String getCodigoMarca() {
        return codigoMarca;
    }

    public void setCodigoMarca(String codigoMarca) {
        this.codigoMarca = codigoMarca;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Float getCostoAdq() {
        return costoAdq;
    }

    public void setCostoAdq(Float costoAdq) {
        this.costoAdq = costoAdq;
    }

    public Float getIva() {
        return iva;
    }

    public void setIva(Float iva) {
        this.iva = iva;
    }

    public Float getFlete() {
        return flete;
    }

    public void setFlete(Float flete) {
        this.flete = flete;
    }

    public Float getCostoInd() {
        return costoInd;
    }

    public void setCostoInd(Float costoInd) {
        this.costoInd = costoInd;
    }

    public Float getCostoFinal() {
        return costoFinal;
    }

    public void setCostoFinal(Float costoFinal) {
        this.costoFinal = costoFinal;
    }

    public Float getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(Float utilidad) {
        this.utilidad = utilidad;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Float getStockMin() {
        return stockMin;
    }

    public void setStockMin(Float stockMin) {
        this.stockMin = stockMin;
    }

    public CfgEmpresa getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(CfgEmpresa empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public List<CfgCategoriaproducto> getListaCategoria() {
        return listaCategoria;
    }

    public void setListaCategoria(List<CfgCategoriaproducto> listaCategoria) {
        this.listaCategoria = listaCategoria;
    }

    public CfgCategoriaproducto getCategoriaSeleccionada() {
        return categoriaSeleccionada;
    }

    public void setCategoriaSeleccionada(CfgCategoriaproducto categoriaSeleccionada) {
        this.categoriaSeleccionada = categoriaSeleccionada;
    }

    public CfgReferenciaproducto getReferenciaSeleccionada() {
        return referenciaSeleccionada;
    }

    public void setReferenciaSeleccionada(CfgReferenciaproducto referenciaSeleccionada) {
        this.referenciaSeleccionada = referenciaSeleccionada;
    }

    public List<CfgReferenciaproducto> getListaReferencia() {
        return listaReferencia;
    }

    public void setListaReferencia(List<CfgReferenciaproducto> listaReferencia) {
        this.listaReferencia = listaReferencia;
    }

    public CfgMarcaproducto getMarcaSeleccionada() {
        return marcaSeleccionada;
    }

    public void setMarcaSeleccionada(CfgMarcaproducto marcaSeleccionada) {
        this.marcaSeleccionada = marcaSeleccionada;
    }

    public List<CfgMarcaproducto> getListaMarca() {
        return listaMarca;
    }

    public void setListaMarca(List<CfgMarcaproducto> listaMarca) {
        this.listaMarca = listaMarca;
    }

    public CfgProducto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(CfgProducto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public LazyDataModel<CfgProducto> getListaProducto() {
        return listaProducto;
    }

    public void setListaProducto(LazyDataModel<CfgProducto> listaProducto) {
        this.listaProducto = listaProducto;
    }

}
