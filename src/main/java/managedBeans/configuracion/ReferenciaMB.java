/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.configuracion;

import entities.CfgCategoriaproducto;
import entities.CfgEmpresa;
import entities.CfgReferenciaproducto;
import entities.SegUsuario;
import facades.CfgCategoriaproductoFacade;
import facades.CfgReferenciaproductoFacade;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import managedBeans.seguridad.SesionMB;
import org.primefaces.context.RequestContext;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class ReferenciaMB implements Serializable {

    private String categoria;
    private String codigoReferencia;
    private String nombreReferencia;

    private List<CfgCategoriaproducto> listaCategorias;
    private List<CfgReferenciaproducto> listaReferencias;

    private CfgCategoriaproducto categoriaSeleccionada;
    private CfgReferenciaproducto referenciaSeleccionada;

    private SegUsuario usuarioActual;
    private CfgEmpresa empresaActual;

    @EJB
    CfgCategoriaproductoFacade categoriaproductoFacade;
    @EJB
    CfgReferenciaproductoFacade referenciaproductoFacade;

    public ReferenciaMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
        empresaActual = sesionMB.getEmpresaActual();
        listaCategorias = new ArrayList();
        actualizarTabla();
    }

    public void actualizarTabla() {
        if (empresaActual != null) {
            listaReferencias = referenciaproductoFacade.buscarPorEmpresa(empresaActual);
        }
    }

    public void cargarCategoria() {
        if (empresaActual != null) {
            listaCategorias = categoriaproductoFacade.buscarPorEmpresa(empresaActual);
        } else {
            listaCategorias.clear();
        }
        RequestContext.getCurrentInstance().update("FormModalCategoria");
        RequestContext.getCurrentInstance().execute("PF('dlgCategoria').show()");
    }

    public void cargarInformacionCategoria() {
        if (categoriaSeleccionada != null) {
            categoria = categoriaSeleccionada.getCodigoCategoria() + "-" + categoriaSeleccionada.getNombreCategoria();
            RequestContext.getCurrentInstance().update("IdFormReferencia");
        }
        RequestContext.getCurrentInstance().execute("PF('dlgCategoria').hide()");
    }

    public void buscarReferencia() {
        if (empresaActual != null) {
            if (!codigoReferencia.trim().isEmpty()) {
                if (categoriaSeleccionada == null) {
                    referenciaSeleccionada = referenciaproductoFacade.buscarPorEmpresaAndCodigo(empresaActual, codigoReferencia);
                } else {
                    referenciaSeleccionada = referenciaproductoFacade.buscarPorEmpresaCategoriaAndCodigo(empresaActual, categoriaSeleccionada, codigoReferencia);
                }
            }
            cargarReferencia();
        }

    }

    private void cargarReferencia() {
        if (referenciaSeleccionada != null) {
            categoriaSeleccionada = referenciaSeleccionada.getCfgcategoriaproductoidCategoria();
            codigoReferencia = referenciaSeleccionada.getCodigoReferencia();
            nombreReferencia = referenciaSeleccionada.getNombreReferencia();
            cargarInformacionCategoria();
        } else {
            limpiar();
        }
        RequestContext.getCurrentInstance().update("IdFormReferencia");
        RequestContext.getCurrentInstance().update("FormModalCategoria");
    }

    private void limpiar() {
        nombreReferencia = null;
    }

    public boolean validar() {
        boolean ban = true;
        if (empresaActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro Informacion de la empresa"));
            return false;
        }
        if (usuarioActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro Informacion del usuario"));
            return false;
        }
        if (categoriaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Determine la categoria"));
            return false;
        }
        if (codigoReferencia.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese un codigo"));
            return false;
        }
        if (nombreReferencia.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Referencia necesaria"));
            return false;
        }
        return ban;
    }

    public void accion() {
        if (referenciaSeleccionada != null) {
            modificar();
        } else {
            crear();
        }

    }

    private void crear() {
        if (!validar()) {
            return;
        }
        try {
            CfgReferenciaproducto referenciaproducto = new CfgReferenciaproducto();
            referenciaproducto.setCfgcategoriaproductoidCategoria(categoriaSeleccionada);
            referenciaproducto.setCodigoReferencia(codigoReferencia.toUpperCase());
            referenciaproducto.setNombreReferencia(nombreReferencia.toUpperCase());
            referenciaproducto.setCfgempresaidEmpresa(empresaActual);
            referenciaproducto.setFecCrea(new Date());
            referenciaproductoFacade.create(referenciaproducto);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Referencia Creada"));
            cancelar();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Referencia no Creada"));
        }
    }

    private void modificar() {
        if (!validar()) {
            return;
        }
        if (referenciaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Referencia no seleccionada"));
            return;
        }
        try {
            referenciaSeleccionada.setNombreReferencia(nombreReferencia.toUpperCase());
            referenciaSeleccionada.setCfgcategoriaproductoidCategoria(categoriaSeleccionada);
            referenciaproductoFacade.edit(referenciaSeleccionada);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Referencia Modificada"));
            cancelar();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Referencia no Modificada"));
        }
    }

    public void cancelar() {
        categoriaSeleccionada = null;
        referenciaSeleccionada = null;
        categoria = null;
        codigoReferencia = null;
        limpiar();
        actualizarTabla();
        RequestContext.getCurrentInstance().update("IdFormReferencia");
    }

    public String getCodigoReferencia() {
        return codigoReferencia;
    }

    public void setCodigoReferencia(String codigoReferencia) {
        this.codigoReferencia = codigoReferencia;
    }

    public String getNombreReferencia() {
        return nombreReferencia;
    }

    public void setNombreReferencia(String nombreReferencia) {
        this.nombreReferencia = nombreReferencia;
    }

    public List<CfgCategoriaproducto> getListaCategorias() {
        return listaCategorias;
    }

    public List<CfgReferenciaproducto> getListaReferencias() {
        return listaReferencias;
    }

    public CfgCategoriaproducto getCategoriaSeleccionada() {
        return categoriaSeleccionada;
    }

    public void setCategoriaSeleccionada(CfgCategoriaproducto categoriaSeleccionada) {
        this.categoriaSeleccionada = categoriaSeleccionada;
    }

    public String getCategoria() {
        return categoria;
    }

}
