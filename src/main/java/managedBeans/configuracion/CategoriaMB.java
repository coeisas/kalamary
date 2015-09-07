/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.configuracion;

import entities.CfgCategoriaproducto;
import entities.CfgEmpresa;
import entities.SegUsuario;
import facades.CfgCategoriaproductoFacade;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import managedBeans.seguridad.SesionMB;
import javax.ejb.EJB;
import java.util.List;
import java.util.Date;
import org.primefaces.context.RequestContext;
import java.io.Serializable;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class CategoriaMB implements Serializable {

    private String codigoCategoria;
    private String nombreCategoria;

    private List<CfgCategoriaproducto> listaCategorias;
    private CfgCategoriaproducto categoriaSeleccionada;
    private SegUsuario usuarioActual;
    private CfgEmpresa empresaActual;

    @EJB
    CfgCategoriaproductoFacade categoriaproductoFacade;

    public CategoriaMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
        empresaActual = sesionMB.getEmpresaActual();
        actualizarTabla();
    }

    public void actualizarTabla() {
        if (empresaActual != null) {
            listaCategorias = categoriaproductoFacade.buscarPorEmpresa(empresaActual);
        }
    }

    public void buscarCategoria() {
        if (empresaActual != null) {
            if (!codigoCategoria.trim().isEmpty()) {
                categoriaSeleccionada = categoriaproductoFacade.buscarPorEmpresaAndCodigo(empresaActual, codigoCategoria);
            }
            cargarCategoria();
        }
    }

    public void cargarCategoria() {
        if (categoriaSeleccionada != null) {
            codigoCategoria = categoriaSeleccionada.getCodigoCategoria();
            nombreCategoria = categoriaSeleccionada.getNombreCategoria();
        } else {
            limpiar();
        }
        RequestContext.getCurrentInstance().update("IdFormCategoria");
    }

    private void limpiar() {
        nombreCategoria = null;
    }

    public void accion() {
        if (categoriaSeleccionada != null) {
            modificar();
        } else {
            crear();
        }       
    }

    public void cancelar() {
        categoriaSeleccionada = null;
        codigoCategoria = null;
        limpiar();
        actualizarTabla();
        RequestContext.getCurrentInstance().update("IdFormCategoria");
    }

    private boolean validar() {
        boolean ban = true;
        if (empresaActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro Informacion de la empresa"));
            return false;
        }
        if (usuarioActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro Informacion del usuario"));
            return false;
        }
        if (codigoCategoria.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese un codigo"));
            return false;
        }
        if (nombreCategoria.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Categoria necesaria"));
            return false;
        }
        return ban;
    }

    private void crear() {
        if (!validar()) {
            return;
        }
        try {
            CfgCategoriaproducto categoriaproducto = new CfgCategoriaproducto();
            categoriaproducto.setCodigoCategoria(codigoCategoria.toUpperCase());
            categoriaproducto.setNombreCategoria(nombreCategoria.toUpperCase());
            categoriaproducto.setFecCrea(new Date());
            categoriaproducto.setCfgempresaidEmpresa(empresaActual);
            categoriaproductoFacade.create(categoriaproducto);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Categoria Creada"));
            cancelar();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Categoria no Creada"));
        }

    }

    private void modificar() {
        if (!validar()) {
            return;
        }
        if (categoriaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro la categoria a modificar"));
            return;
        }
        try {
            categoriaSeleccionada.setNombreCategoria(nombreCategoria.toUpperCase());
            categoriaproductoFacade.edit(categoriaSeleccionada);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Categoria Modificada"));
            cancelar();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Categoria no Modificada"));
        }
    }

    public String getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(String codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public CfgCategoriaproducto getCategoriaSeleccionada() {
        return categoriaSeleccionada;
    }

    public void setCategoriaSeleccionada(CfgCategoriaproducto categoriaSeleccionada) {
        this.categoriaSeleccionada = categoriaSeleccionada;
    }

    public List<CfgCategoriaproducto> getListaCategorias() {
        return listaCategorias;
    }

}
