/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.configuracion;

import com.google.common.io.Files;
import entities.CfgCategoriaproducto;
import entities.CfgColor;
import entities.CfgEmpresa;
import entities.CfgMarcaproducto;
import entities.CfgProducto;
import entities.CfgReferenciaproducto;
import entities.CfgTalla;
import entities.CfgUnidad;
import entities.SegUsuario;
import facades.CfgCategoriaproductoFacade;
import facades.CfgColorFacade;
import facades.CfgEmpresaFacade;
import facades.CfgMarcaproductoFacade;
import facades.CfgProductoFacade;
import facades.CfgReferenciaproductoFacade;
import facades.CfgTallaFacade;
import facades.CfgUnidadFacade;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javax.faces.event.PhaseId;
import managedBeans.seguridad.SesionMB;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.ByteArrayContent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
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
    private int idcolor;
    private int idtalla;
    private int idunidad;
    private String nombreProducto;
    private int stockMin;
    private float costoAdq;
    private float iva;
    private float flete;
    private float costoInd;
    private float costoFinal;
    private float utilidad;
    private float precio;
    private boolean activo;
    private boolean esServicio;
    private StreamedContent image;
    private UploadedFile file;

    private String pathRoot;
    private String nombreArchivo;

    private String nombreEmpresa;
    private SesionMB sesionMB;
    private SegUsuario usuarioActual;

    private List<CfgCategoriaproducto> listaCategoria;
    private List<CfgReferenciaproducto> listaReferencia;
    private List<CfgMarcaproducto> listaMarca;
    private List<CfgColor> listaColor;
    private List<CfgTalla> listaTalla;
    private List<CfgUnidad> listaUnidad;
    private LazyDataModel<CfgProducto> listaProducto;
    private List<String> listFormsModal;

    private CfgEmpresa empresaSeleccionada;
    private CfgCategoriaproducto categoriaSeleccionada;
    private CfgReferenciaproducto referenciaSeleccionada;
    private CfgMarcaproducto marcaSeleccionada;
    private CfgProducto productoSeleccionado;

    private boolean renderBoton;
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

    @EJB
    CfgColorFacade colorFacade;

    @EJB
    CfgTallaFacade tallaFacade;
    
    @EJB
    CfgUnidadFacade unidadFacade;

    public ProductoMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        pathRoot = context.getExternalContext().getInitParameter("directory.images");
        renderBoton = false;
        sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
        empresaSeleccionada = sesionMB.getEmpresaActual();
        listaColor = colorFacade.findAll();
        listaTalla = tallaFacade.findAll();
        listaUnidad = unidadFacade.findAll();
        if (empresaSeleccionada != null) {
            listaCategoria = categoriaFacade.buscarPorEmpresa(empresaSeleccionada);
            listaReferencia = referenciaFacade.buscarPorEmpresa(empresaSeleccionada);
            listaMarca = marcaFacade.buscarPorEmpresa(empresaSeleccionada);
        } else {
            listaCategoria = new ArrayList();
            listaReferencia = new ArrayList();
            listaMarca = new ArrayList();
        }
        setPrecio(0);
        setCostoFinal(0);
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
        setIdcolor(1);
        setIdtalla(1);
        setIdunidad(1);
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
        nombreArchivo = null;
    }

    public void reloadCategoriaReferenciaAndMarca() {
        int count = categoriaFacade.totalCategoriaPorEmpresa(empresaSeleccionada);
        if (count > listaCategoria.size()) {
            listaCategoria = categoriaFacade.buscarPorEmpresa(empresaSeleccionada);
            RequestContext.getCurrentInstance().update("FormModalCategoria");
        }
        count = referenciaFacade.totalReferenciaPorEmpresa(empresaSeleccionada);
        if (count > listaReferencia.size()) {
            listaReferencia = referenciaFacade.buscarPorEmpresa(empresaSeleccionada);
            RequestContext.getCurrentInstance().update("FormModalReferencia");
        }
        count = marcaFacade.totalMarcaPorEmpresa(empresaSeleccionada);
        if (count > listaMarca.size()) {
            listaMarca = marcaFacade.buscarPorEmpresa(empresaSeleccionada);
            RequestContext.getCurrentInstance().update("FormModalMarca");
        }
    }

    public void cargarInformacionCategoria() {
        RequestContext.getCurrentInstance().update("FormModalReferencia");
        RequestContext.getCurrentInstance().update("IdFormProducto");
    }

    public void cargarInformacionReferencia() {
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

    //Controla la integridad de Categoria - Referencia - Marca
    public void deseleccionar(Object o) {
        String opcion = o != null ? o.getClass().toString() : "";
        List<String> updates = new ArrayList();
        updates.add("IdFormProducto");
        switch (opcion) {
            case "class entities.CfgCategoriaproducto":
                categoriaSeleccionada = null;
                updates.add("FormModalCategoria");
                break;
            case "class entities.CfgReferenciaproducto":
                referenciaSeleccionada = null;
                updates.add("FormModalReferencia");
                break;
            case "class entities.CfgMarcaproducto":
                marcaSeleccionada = null;
                updates.add("FormModalMarca");
                break;
        }
        RequestContext.getCurrentInstance().update(updates);
    }

    public void cargarInformacionProducto() {
        if (productoSeleccionado != null) {
            setMarcaSeleccionada(productoSeleccionado.getCfgmarcaproductoidMarca());
            setReferenciaSeleccionada(productoSeleccionado.getCfgreferenciaproductoidReferencia());
            setCategoriaSeleccionada(productoSeleccionado.getCfgcategoriaproductoidCategoria());
            setCodigoProducto(productoSeleccionado.getCodProducto());
            setCodigoBarra(productoSeleccionado.getCodBarProducto());
            setIdcolor(productoSeleccionado.getCfgColorIdColor().getIdColor());
            setIdtalla(productoSeleccionado.getCfgTallaIdTalla().getIdTalla());
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
            if(usuarioActual.getCfgRolIdrol().getCodrol().equals("00001") || usuarioActual.getCfgRolIdrol().getCodrol().equals("00002"))
            renderBoton = true;

        } else {
            setCodigoBarra(null);
            setIdcolor(1);
            setIdtalla(1);
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

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        if (file != null) {
            if (productoSeleccionado == null) {// SI NO SE HA CREADO EL PRODUCTO SIMPLEMENTE SE GUARDA LA IMAGEN EN LA SESION
                nombreArchivo = file.getFileName();
                RequestContext.getCurrentInstance().update("IdFormProducto:nombreImagen");
            } else {//SI YA EXISTE EL PRODUCTO. SE GUARDA INMEDIATAMENTE LA IMAGEN EN EL SERVIDOR
                String nombreOriginal = file.getFileName();
                String[] id = nombreOriginal.split("\\.");
                String extension = ".".concat(id[1]);
                String filename = productoSeleccionado.getCodigoInterno().concat(extension);
                productoSeleccionado.setImgProducto(filename);
                uploadArchivo(file, filename);
                image = new ByteArrayContent(file.getContents());
                setImage(image);
                productoFacade.edit(productoSeleccionado);
                RequestContext.getCurrentInstance().update("IdFormProducto");
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Imagen cargada"));
            RequestContext.getCurrentInstance().execute("PF('dlgImagen').hide()");
        }
    }

    public void determinarAccion() {
        if (productoSeleccionado != null) {
            editarProducto();
        } else {
            crearProducto();
        }
    }

    public void eliminar(){
        if(productoSeleccionado!=null){
            productoSeleccionado.setActivo(false);
            productoFacade.edit(productoSeleccionado);
            cancelar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Producto modificado"));
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
            CfgColor color = colorFacade.find(idcolor);
            productoSeleccionado.setCfgColorIdColor(color);
            CfgTalla talla = tallaFacade.find(idtalla);
            productoSeleccionado.setCfgTallaIdTalla(talla);
            String codigoInterno = generarCodigoInterno(productoSeleccionado);
            productoSeleccionado.setCodigoInterno(codigoInterno);
//            productoSeleccionado.setCodigoInterno(codigoProducto + color.getIdColor() + talla.getIdTalla());
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
            producto.setCfgcategoriaproductoidCategoria(categoriaSeleccionada);
            producto.setCfgreferenciaproductoidReferencia(referenciaSeleccionada);
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
            CfgColor color = colorFacade.find(idcolor);
            producto.setCfgColorIdColor(color);
            CfgTalla talla = tallaFacade.find(idtalla);
            producto.setCfgTallaIdTalla(talla);
//            producto.setCodigoInterno(codigoProducto + color.getIdColor() + talla.getIdTalla());
            //la unidad de medida del producto por defecto es UND. Cambiar esto cuando la unidad sea dinamico.
            CfgUnidad unidad = unidadFacade.find(idunidad);
            producto.setCfgUnidadIdUnidad(unidad);
            producto.setPrecio(getPrecio());
            producto.setEsServicio(esServicio);
            producto.setActivo(activo);
            producto.setEsKit(false);
            producto.setFecCrea(new Date());
            producto.setCfgempresaidEmpresa(empresaSeleccionada);
            producto.setSegusuarioidUsuario(usuarioActual);
            productoFacade.create(producto);
            //una vez creado el producto se asigna el codigoInterno. Ademas el codigo de barras lo genera automaticamente codCategoria+codReferencia+codMarca+idProducto
            String codigoInterno = generarCodigoInterno(producto);
            producto.setCodigoInterno(codigoInterno);
            producto.setCodBarProducto(codigoInterno);
            if (file != null) {
                String nombreOriginal = file.getFileName();
                String[] id = nombreOriginal.split("\\.");
                String extension = ".".concat(id[1]);
                String filename = producto.getCodigoInterno().concat(extension);
                producto.setImgProducto(filename);
                uploadArchivo(file, filename);
            }
            productoFacade.edit(producto);
            cancelar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Producto creado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Producto no creado"));
        }
    }

    public boolean uploadArchivo(UploadedFile archivoCargado, String nombreArchivo) {
        String path = pathRoot.concat(nombreArchivo);
        File fichero = new java.io.File(path);
        if (fichero.exists()) {//si existe se borra
            fichero.delete();
        }
        fichero = new java.io.File(path);
        try (FileOutputStream fileOutput = new FileOutputStream(fichero)) {
            InputStream inputStream = archivoCargado.getInputstream();
            byte[] buffer = new byte[1024];
            int bufferLength;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
            }
        } catch (Exception e) {
            System.out.println("Error 01: " + e.getMessage());
            return false;
        }
        return true;
    }

    private String generarCodigoInterno(CfgProducto producto) {
        CfgMarcaproducto marca = producto.getCfgmarcaproductoidMarca();
        CfgReferenciaproducto referencia = producto.getCfgreferenciaproductoidReferencia();
        CfgCategoriaproducto categoria = producto.getCfgcategoriaproductoidCategoria();
        String aux;
        if (marca != null && referencia != null && categoria != null) {
            aux = categoria.getCodigoCategoria() + referencia.getCodigoReferencia() + marca.getCodigoMarca() + producto.getIdProducto();
        } else {
            aux = marca.getCodigoMarca() + producto.getIdProducto();
        }
        return aux;
    }

    private boolean validacion() {
        if (empresaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Elija la empresa"));
            return false;
        }
        //solo los usuarios super y admin pueden crear y modificar productos
        if (usuarioActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay usuario"));
            return false;
        }
        if (!usuarioActual.getCfgRolIdrol().getCodrol().equals("00001") && !usuarioActual.getCfgRolIdrol().getCodrol().equals("00002")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No tiene permisos para efectuar esta accion"));
            return false;
        }
        if (categoriaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Determine la categoria del producto"));
            return false;
        }
        if (referenciaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Determine la referencia del producto"));
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

    private Integer determinarValor(Integer valor) {
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
        renderBoton = false;
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

    public int getStockMin() {
        return stockMin;
    }

    public void setStockMin(int stockMin) {
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

    public int getIdcolor() {
        return idcolor;
    }

    public void setIdcolor(int idcolor) {
        this.idcolor = idcolor;
    }

    public int getIdtalla() {
        return idtalla;
    }

    public void setIdtalla(int idtalla) {
        this.idtalla = idtalla;
    }

    public int getIdunidad() {
        return idunidad;
    }

    public void setIdunidad(int idunidad) {
        this.idunidad = idunidad;
    }

    public List<CfgColor> getListaColor() {
        return listaColor;
    }

    public void setListaColor(List<CfgColor> listaColor) {
        this.listaColor = listaColor;
    }

    public List<CfgTalla> getListaTalla() {
        return listaTalla;
    }

    public List<CfgUnidad> getListaUnidad() {
        return listaUnidad;
    }

    public void setListaUnidad(List<CfgUnidad> listaUnidad) {
        this.listaUnidad = listaUnidad;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {//fase de jsf
            image = new DefaultStreamedContent();
            return image;
        } else if (productoSeleccionado != null) {
            String codInterno = context.getExternalContext().getRequestParameterMap().get("id");
            CfgProducto producto = productoFacade.buscarPorCodigoInterno(empresaSeleccionada.getIdEmpresa(), codInterno);
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

    /**
     * @return the nombreArchivo
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public boolean isRenderBoton() {
        return renderBoton;
    }

    public void setRenderBoton(boolean renderBoton) {
        this.renderBoton = renderBoton;
    }

}
