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
import facades.CfgMarcaproductoFacade;
import facades.CfgProductoFacade;
import facades.CfgReferenciaproductoFacade;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import managedBeans.seguridad.SesionMB;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Mario
 */
@ManagedBean
@SessionScoped
public class ListaPrecioMB implements Serializable {

    private int idCategoria;
    private int idReferencia;
    private int idMarca;
    private List<CfgProducto> listaProducto;
    private List<CfgCategoriaproducto> listaCategoria;
    private List<CfgReferenciaproducto> listaReferencia;
    private List<CfgMarcaproducto> listaMarca;

    private CfgCategoriaproducto categoriaSeleccionada;
    private CfgReferenciaproducto referenciaSeleccionada;
    private CfgMarcaproducto marcaSeleccionada;
    private SegUsuario usuarioActual;
    private CfgEmpresa empresaActual;
    private boolean mostrarColumnas;
    @EJB
    CfgCategoriaproductoFacade categoriaFacade;
    @EJB
    CfgReferenciaproductoFacade referenciaFacade;
    @EJB
    CfgMarcaproductoFacade marcaFacade;
    @EJB
    CfgProductoFacade productoFacade;

    public ListaPrecioMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
        mostrarColumnas = !usuarioActual.getCfgRolIdrol().getCodrol().equals("00003");
        empresaActual = sesionMB.getEmpresaActual();
        limpiarFormulario();
    }

    public void updateTabla() {
        listaProducto = productoFacade.buscarPorEmpresaCategoriaReferenciaAndMarca(empresaActual, categoriaSeleccionada, referenciaSeleccionada, marcaSeleccionada);
        RequestContext.getCurrentInstance().update("IdFormListaPrecios:IdTableProducto");
    }

    public void onRowEdit(RowEditEvent event) {
        if (validar()) {
//            SOLO PARA CellEditEvent
//            String clientId = event.getColumn().getClientId();
//            se obtiene el id de la columna modificada. empenzando desde el form.
            CfgProducto producto = (CfgProducto) event.getObject();
            float vlrs[];
            vlrs = determinarCostoFinalAndPrecio(producto);
            if (vlrs.length > 0) {
                producto.setCosto(vlrs[0]);
                producto.setPrecio(vlrs[1]);
            }
            try {
                productoFacade.edit(producto);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Producto Actualizado"));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Producto No Actualizado"));
            }
        }
        RequestContext.getCurrentInstance().update("msg");
    }

    public float[] determinarCostoFinalAndPrecio(CfgProducto producto) {
        float[] valores = new float[2];
        valores[0] = 0;
        valores[0] = producto.getCostoAdquisicion() + (producto.getCostoAdquisicion() * (producto.getIva() / 100));
        valores[0] = valores[0] + (valores[0] * (producto.getFlete() / 100));
        valores[0] = valores[0] + (valores[0] * (producto.getCostoIndirecto() / 100));
        valores[1] = (valores[0] + (valores[0] * (producto.getUtilidad() / 100)));
        return valores;
    }

    public float determinarUtilidadPorPrecioAndCostoFinal(float costoFinal, float precio) {
        float utilidad, aux;
        aux = ((precio / costoFinal) - 1) * 100;
        utilidad = aux;
        return utilidad;
    }

    public float determinarPrecioPorUtilidad(float costoFinal, float utilidad) {
        float precio;
        precio = (costoFinal + (costoFinal * (utilidad / 100)));
        return precio;
    }

    private boolean validar() {
        boolean ban = true;
        //solo los usuarios super y admin pueden crear y modificar productos
        if (usuarioActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay usuario"));
            ban = false;
        }
        if (!usuarioActual.getCfgRolIdrol().getCodrol().equals("00001") && !usuarioActual.getCfgRolIdrol().getCodrol().equals("00002")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No tiene permisos para efectuar esta accion"));
            ban = false;
        }
        return ban;
    }

    public void limpiarFormulario() {
        categoriaSeleccionada = null;
        referenciaSeleccionada = null;
        marcaSeleccionada = null;
        if (empresaActual != null) {
            listaProducto = productoFacade.buscarPorEmpresaCategoriaReferenciaAndMarca(empresaActual, categoriaSeleccionada, referenciaSeleccionada, marcaSeleccionada);
            listaCategoria = categoriaFacade.buscarPorEmpresa(empresaActual);
        } else {
            listaCategoria = new ArrayList();
        }
        idCategoria = 0;
        idReferencia = 0;
        idMarca = 0;
        listaReferencia = new ArrayList();
        listaMarca = new ArrayList();
        RequestContext.getCurrentInstance().update("IdFormListaPrecios");
    }

    public void cargarReferencia() {
        listaReferencia.clear();
        listaMarca.clear();
        if (idCategoria != 0) {
            categoriaSeleccionada = categoriaFacade.find(idCategoria);
            listaReferencia = referenciaFacade.buscarPorCategoria(categoriaSeleccionada);
        } else {
            categoriaSeleccionada = null;
        }
        idMarca = 0;
        idReferencia = 0;
        referenciaSeleccionada = null;
        marcaSeleccionada = null;
        listaProducto = productoFacade.buscarPorEmpresaCategoriaReferenciaAndMarca(empresaActual, categoriaSeleccionada, referenciaSeleccionada, marcaSeleccionada);
        RequestContext.getCurrentInstance().update("IdFormListaPrecios:IdReferencia");
        RequestContext.getCurrentInstance().update("IdFormListaPrecios:IdMarca");
        RequestContext.getCurrentInstance().update("IdFormListaPrecios:IdTableProducto");
    }

    public void cargarMarca() {
        listaMarca.clear();
        if (idReferencia != 0) {
            referenciaSeleccionada = referenciaFacade.find(idReferencia);
            listaMarca = marcaFacade.buscarPorReferencia(referenciaSeleccionada);
        } else {
            referenciaSeleccionada = null;
        }
        listaProducto = productoFacade.buscarPorEmpresaCategoriaReferenciaAndMarca(empresaActual, categoriaSeleccionada, referenciaSeleccionada, marcaSeleccionada);
        RequestContext.getCurrentInstance().update("IdFormListaPrecios:IdMarca");
        RequestContext.getCurrentInstance().update("IdFormListaPrecios:IdTableProducto");

    }

    public void cargarProducto() {
        if (idMarca != 0) {
            marcaSeleccionada = marcaFacade.find(idMarca);
        } else {
            marcaSeleccionada = null;
        }
        listaProducto = productoFacade.buscarPorEmpresaCategoriaReferenciaAndMarca(empresaActual, categoriaSeleccionada, referenciaSeleccionada, marcaSeleccionada);
        RequestContext.getCurrentInstance().update("IdFormListaPrecios:IdTableProducto");

    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(int idReferencia) {
        this.idReferencia = idReferencia;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public List<CfgProducto> getListaProducto() {
        return listaProducto;
    }

    public List<CfgCategoriaproducto> getListaCategoria() {
        return listaCategoria;
    }

    public List<CfgReferenciaproducto> getListaReferencia() {
        return listaReferencia;
    }

    public List<CfgMarcaproducto> getListaMarca() {
        return listaMarca;
    }

    public boolean isMostrarColumnas() {
        return mostrarColumnas;
    }

    public void setMostrarColumnas(boolean mostrarColumnas) {
        this.mostrarColumnas = mostrarColumnas;
    }
}
