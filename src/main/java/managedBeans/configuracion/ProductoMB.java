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
import entities.SegUsuario;
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
import java.util.Date;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import managedBeans.seguridad.SesionMB;
import org.primefaces.model.LazyDataModel;
import utilities.LazyProductosModel;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class ProductoMB implements Serializable {


    private String codigoProducto;
    private String codigoBarra;
    private String color;
    private String talla;
    private String nombreProducto;
    private float stockMin;
    private float costoAdq;
    private float iva;
    private float flete;
    private float costoInd;
    private float costoFinal;
    private float utilidad;
    private float precio;
    private boolean activo;
    private boolean esServicio;

    private String nombreEmpresa;
    private SesionMB sesionMB;
    private SegUsuario usuarioActual;

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
        FacesContext context = FacesContext.getCurrentInstance();
        sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
        empresaSeleccionada = sesionMB.getEmpresaActual();
        if (empresaSeleccionada != null) {
            listaCategoria = categoriaFacade.buscarPorEmpresa(empresaSeleccionada);
        } else {
            listaCategoria = new ArrayList();
        }
        setPrecio(0);
        setCostoFinal(0);
        listaReferencia = new ArrayList();
        listaMarca = new ArrayList();
        listFormsModal = new ArrayList();
        listFormsModal.add("FormModalCategoria");
        listFormsModal.add("FormModalReferencia");
        listFormsModal.add("FormModalMarca");
        listFormsModal.add("FormModalProducto");
        activo = true;
        esServicio = false;
    }

    private void limpiarFormulario() {
        codigoProducto = null;
        codigoBarra = null;
        color = null;
        talla = null;
        nombreProducto = null;
        stockMin = 0;
        costoAdq = 0;
        iva = 0;
        flete = 0;
        costoInd = 0;
        setCostoFinal(0);
        utilidad = 0;
        setPrecio(0);
        activo = true;
        esServicio = false;
    }

    public void cargarInformacionCategoria() {
        if (categoriaSeleccionada != null) {
//            setCodigoCategoria(categoriaSeleccionada.getCodigoCategoria());
            listaReferencia = referenciaFacade.buscarPorCategoria(categoriaSeleccionada);
        } else {
            listaReferencia.clear();
        }
        RequestContext.getCurrentInstance().update("FormModalReferencia");
        RequestContext.getCurrentInstance().update("IdFormProducto");
    }

    public void cargarInformacionReferencia() {
        if (referenciaSeleccionada != null) {
//            setCodigoReferencia(referenciaSeleccionada.getCodigoReferencia());
            listaMarca = marcaFacade.buscarPorReferencia(referenciaSeleccionada);
        } else {
            listaMarca.clear();
        }
        RequestContext.getCurrentInstance().update("FormModalMarca");
        RequestContext.getCurrentInstance().update("IdFormProducto");
    }

    public void cargarInformacionMarca() {
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

//    public void buscarProducto() {
//        if (empresaSeleccionada != null) {
//            if (marcaSeleccionada != null) {
//                productoSeleccionado = productoFacade.buscarPorEmpresaAndMarcaAndCodigo(empresaSeleccionada, marcaSeleccionada, codigoProducto.trim());
//            } else if (referenciaSeleccionada != null) {
//                productoSeleccionado = productoFacade.buscarPorEmpresaAndReferenciaAndCodigo(empresaSeleccionada, referenciaSeleccionada, codigoProducto.trim());
//            } else if (categoriaSeleccionada != null) {
//                productoSeleccionado = productoFacade.buscarPorEmpresaAndCategoriaAndCodigo(empresaSeleccionada, categoriaSeleccionada, codigoProducto.trim());
//            } else {
//                productoSeleccionado = productoFacade.buscarPorEmpresaAndCodigo(empresaSeleccionada, codigoProducto.trim());
//            }
//            cargarInformacionProducto();
////            if (productoSeleccionado == null && !codigoProducto.trim().isEmpty()) {
////                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No se encontro el producto"));
////            }
//        } else {
//            productoSeleccionado = null;
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No ha determinado la empresa"));
//        }
//
//    }

    public void cargarInformacionProducto() {
        if (productoSeleccionado != null) {
            setMarcaSeleccionada(productoSeleccionado.getCfgmarcaproductoidMarca());
            setReferenciaSeleccionada(marcaSeleccionada.getCfgreferenciaproductoidReferencia());
            setCategoriaSeleccionada(referenciaSeleccionada.getCfgcategoriaproductoidCategoria());
            setCodigoProducto(productoSeleccionado.getCodProducto());
            setCodigoBarra(productoSeleccionado.getCodBarProducto());
            setColor(productoSeleccionado.getColor());
            setTalla(productoSeleccionado.getTalla());
            setNombreProducto(productoSeleccionado.getNomProducto());
            setStockMin(determinarValor(productoSeleccionado.getStockMin()));
            setCostoAdq(determinarValor(productoSeleccionado.getCostoAdquisicion()));
            setIva(determinarValor(productoSeleccionado.getIva()));
            setFlete(determinarValor(productoSeleccionado.getFlete()));
            setCostoInd(determinarValor(productoSeleccionado.getCostoIndirecto()));
            setCostoFinal((float) productoSeleccionado.getCosto());
            setUtilidad(determinarValor(productoSeleccionado.getUtilidad()));
            setPrecio((float) productoSeleccionado.getPrecio());
            setActivo(productoSeleccionado.getActivo());
            setEsServicio(productoSeleccionado.getEsServicio());

        } else {
            setCodigoBarra(null);
            setColor(null);
            setTalla(null);
            setNombreProducto(null);
            setStockMin(0);
            setCostoAdq(0);
            setIva(0);
            setFlete(0);
            setCostoInd(0);
            setCostoFinal(0);
            setUtilidad(0);
            setPrecio(0);
            setActivo(true);
            setEsServicio(false);
        }
        RequestContext.getCurrentInstance().update("IdFormProducto");
        RequestContext.getCurrentInstance().update(listFormsModal);
    }

    public void determinarAccion() {
        if (productoSeleccionado != null) {
            editarProducto();
        } else {
            crearProducto();
        }
    }

    private void editarProducto() {
        if (!validacion()) {
            return;
        }
        try {
            productoSeleccionado.setCfgmarcaproductoidMarca(marcaSeleccionada);
            productoSeleccionado.setNomProducto(nombreProducto.toUpperCase());
            productoSeleccionado.setStockMin(stockMin);
            productoSeleccionado.setCostoAdquisicion(costoAdq);
            productoSeleccionado.setColor(color.toUpperCase());
            productoSeleccionado.setTalla(talla.toUpperCase());
            productoSeleccionado.setIva(iva);
            productoSeleccionado.setFlete(flete);
            productoSeleccionado.setCostoIndirecto(costoInd);
            productoSeleccionado.setCosto(getCostoFinal());
            productoSeleccionado.setUtilidad(utilidad);
            productoSeleccionado.setPrecio(getPrecio());
            productoSeleccionado.setEsServicio(esServicio);
            productoFacade.edit(productoSeleccionado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Producto modificado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Producto no Modificado"));
        }

    }

    public void determinarCostoFinalAndPrecio() {
        setCostoFinal(0);
        setPrecio(0);
        if (validarValores()) {
            setCostoFinal(costoAdq + (costoAdq * (iva / 100)));
            setCostoFinal(getCostoFinal() + (getCostoFinal() * (flete / 100)));
            setCostoFinal(getCostoFinal() + (getCostoFinal() * (costoInd / 100)));
            setPrecio(getCostoFinal() + (getCostoFinal() * (utilidad / 100)));
        }
        RequestContext.getCurrentInstance().update("IdFormProducto:IdCostoFinal");
        RequestContext.getCurrentInstance().update("IdFormProducto:IdPrecio");
    }

    private void crearProducto() {
        if (!validacion()) {
            return;
        }
        try {
            CfgProducto producto = new CfgProducto();
            producto.setCodProducto(codigoProducto.toUpperCase());
            producto.setCfgmarcaproductoidMarca(marcaSeleccionada);
            producto.setNomProducto(nombreProducto.toUpperCase());
            producto.setCodBarProducto(codigoBarra.toUpperCase());
            producto.setStockMin(stockMin);
            producto.setCostoAdquisicion(costoAdq);
            producto.setIva(iva);
            producto.setFlete(flete);
            producto.setCostoIndirecto(costoInd);
            producto.setCosto(getCostoFinal());
            producto.setUtilidad(utilidad);
            producto.setColor(color.toUpperCase());
            producto.setTalla(talla.toUpperCase());            
            producto.setPrecio(getPrecio());
            producto.setEsServicio(esServicio);
            producto.setActivo(activo);
            producto.setFecCrea(new Date());
            producto.setCfgempresaidEmpresa(empresaSeleccionada);
            producto.setSegusuarioidUsuario(usuarioActual);
            productoFacade.create(producto);
            cancelar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Producto creado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Producto no creado"));
        }
    }

    private boolean validacion() {
        if (empresaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Elija la empresa"));
            return false;
        }
        if (marcaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Determine la marca del producto"));
            return false;
        }
        if (codigoProducto == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Codigo de producto necesario"));
            return false;
        }
        if (nombreProducto == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Nombre producto obligatorio"));
            return false;
        }
        boolean ban = validarValores();
        return ban;
    }

    private boolean validarValores() {
        boolean ban = true;
        if (stockMin < 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Stock Min no valido"));
            return false;

        }
        if (costoAdq < 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Costo de adquisicion no valido"));
            return false;
        }
        if (iva < 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "%IVA no valido"));
            return false;
        }
        if (flete < 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "%flete no valido"));
            return false;
        }
        if (costoInd < 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "%Costo Ind no valido"));
            return false;
        }
        if (utilidad < 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "%utilidad incorrecto"));
            return false;
        }
        return ban;
    }

    private Float determinarValor(Float valor) {
        return valor == null ? 0 : valor;
    }

    public void cancelar() {
        limpiarFormulario();
        marcaSeleccionada = null;
        referenciaSeleccionada = null;
        categoriaSeleccionada = null;
        productoSeleccionado = null;
        listaMarca.clear();
        listaReferencia.clear();
        RequestContext.getCurrentInstance().update("IdFormProducto");
        RequestContext.getCurrentInstance().update(listFormsModal);

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

    public float getCostoAdq() {
        return costoAdq;
    }

    public void setCostoAdq(float costoAdq) {
        this.costoAdq = costoAdq;
    }

    public float getIva() {
        return iva;
    }

    public void setIva(float iva) {
        this.iva = iva;
    }

    public float getFlete() {
        return flete;
    }

    public void setFlete(float flete) {
        this.flete = flete;
    }

    public float getCostoInd() {
        return costoInd;
    }

    public void setCostoInd(float costoInd) {
        this.costoInd = costoInd;
    }

    public float getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(float utilidad) {
        this.utilidad = utilidad;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public float getStockMin() {
        return stockMin;
    }

    public void setStockMin(float stockMin) {
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

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public float getCostoFinal() {
        return costoFinal;
    }

    public void setCostoFinal(float costoFinal) {
        this.costoFinal = costoFinal;
    }

    public boolean isEsServicio() {
        return esServicio;
    }

    public void setEsServicio(boolean esServicio) {
        this.esServicio = esServicio;
    }

}
