/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.configuracion;

import entities.CfgEmpresa;
import entities.CfgMarcaproducto;
import entities.SegUsuario;
import facades.CfgMarcaproductoFacade;
import facades.CfgReferenciaproductoFacade;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
public class MarcaMB implements Serializable {

    private String codigoMarca;
    private String nombreMarca;

    private SegUsuario usuarioActual;
    private CfgEmpresa empresaActual;

    private List<CfgMarcaproducto> listaMarca;

    private CfgMarcaproducto marcaSeleccionada;

    @EJB
    CfgReferenciaproductoFacade referenciaproductoFacade;
    @EJB
    CfgMarcaproductoFacade marcaproductoFacade;

    public MarcaMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
        empresaActual = sesionMB.getEmpresaActual();
        actualizarTabla();
    }

    private void limpiar() {
        nombreMarca = null;
    }

    public void actualizarTabla() {
        if (empresaActual != null) {
            listaMarca = marcaproductoFacade.buscarPorEmpresa(empresaActual);
        }
    }

    public void buscarMarca() {
        if (empresaActual != null) {
            if (!codigoMarca.trim().isEmpty()) {
                    marcaSeleccionada = marcaproductoFacade.buscarPorEmpresaAndCodigo(empresaActual, codigoMarca);
            }
            cargarMarca();
        }
    }

    private void cargarMarca() {
        if (marcaSeleccionada != null) {
            codigoMarca = marcaSeleccionada.getCodigoMarca();
            nombreMarca = marcaSeleccionada.getNombreMarca();
        } else {
            limpiar();
        }
        RequestContext.getCurrentInstance().update("IdFormMarca");
        RequestContext.getCurrentInstance().update("FormModalReferencia");
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
        if (codigoMarca.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese un codigo"));
            return false;
        }
        if (nombreMarca.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese la marca"));
            return false;
        }
        return ban;
    }

    public void accion() {
        if (marcaSeleccionada != null) {
            editar();
        } else {
            crear();
        }
    }

    private void crear() {
        if (!validar()) {
            return;
        }
        try {
            CfgMarcaproducto marcaproducto = new CfgMarcaproducto();
            marcaproducto.setCodigoMarca(codigoMarca.toUpperCase());
            marcaproducto.setNombreMarca(nombreMarca.toUpperCase());
            marcaproducto.setCfgempresaidEmpresa(empresaActual);
            marcaproducto.setFecCrea(new Date());
            marcaproductoFacade.create(marcaproducto);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Marca Creada"));
            cancelar();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Marca no Creada"));
        }
    }

    private void editar() {
        if (!validar()) {
            return;
        }
        if (marcaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Marca no seleccionada"));
            return;
        }
        try {
            marcaSeleccionada.setNombreMarca(nombreMarca.toUpperCase());
            marcaproductoFacade.edit(marcaSeleccionada);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Marca Modificada"));
            cancelar();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Marca no Modificada"));
        }
    }

    public void cancelar() {
        marcaSeleccionada = null;
        codigoMarca = null;
        limpiar();
        actualizarTabla();
        RequestContext.getCurrentInstance().update("FormModalReferencia");
        RequestContext.getCurrentInstance().update("IdFormMarca");
    }

    public String getCodigoMarca() {
        return codigoMarca;
    }

    public void setCodigoMarca(String codigoMarca) {
        this.codigoMarca = codigoMarca;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public List<CfgMarcaproducto> getListaMarca() {
        return listaMarca;
    }

    public CfgMarcaproducto getMarcaSeleccionada() {
        return marcaSeleccionada;
    }

    public void setMarcaSeleccionada(CfgMarcaproducto marcaSeleccionada) {
        this.marcaSeleccionada = marcaSeleccionada;
    }

}
