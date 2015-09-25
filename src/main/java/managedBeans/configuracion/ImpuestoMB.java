/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.configuracion;

import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.CfgImpuesto;
import entities.CfgTipoempresa;
import entities.SegUsuario;
import facades.CfgEmpresaFacade;
import facades.CfgEmpresasedeFacade;
import facades.CfgImpuestoFacade;
import facades.CfgTipoempresaFacade;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import org.primefaces.context.RequestContext;
import javax.faces.context.FacesContext;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import managedBeans.seguridad.SesionMB;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class ImpuestoMB implements Serializable {

    private String codigoImpuesto;
    private String nombreImpuesto;
    private float porcentajeImpuesto;
    private int tipoEmpresa;

    private CfgEmpresa empresaSeleccionada;
    private CfgEmpresasede sedeSeleccionada;
    private CfgImpuesto impuestoSeleccionado;

    private List<CfgEmpresasede> listaSede;
    private List<CfgImpuesto> listaImpuestos;

    private SesionMB sesionMB;
    private SegUsuario usuarioActual;

    @EJB
    CfgEmpresaFacade empresaFacade;

    @EJB
    CfgEmpresasedeFacade sedeFacade;

    @EJB
    CfgImpuestoFacade impuestoFacade;

    @EJB
    CfgTipoempresaFacade tipoempresaFacade;

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
        sedeSeleccionada = sesionMB.getSedeActual();
        empresaSeleccionada = sesionMB.getEmpresaActual();
        if (sedeSeleccionada != null) {
            listaImpuestos = impuestoFacade.buscarImpuestoPorSede(sedeSeleccionada);
        } else {
            listaImpuestos = new ArrayList();
        }
    }

    public void cargarListaSedes() {
        if (empresaSeleccionada != null) {
            listaSede = sedeFacade.buscarSedesPorEmpresa(empresaSeleccionada.getIdEmpresa());
            RequestContext.getCurrentInstance().update("FormModalSede");
            RequestContext.getCurrentInstance().execute("PF('dlgSede').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Determine la empresa"));
        }
    }

    public void buscarImpuesto() {
        impuestoSeleccionado = null;
        if (sedeSeleccionada != null) {
            if (!codigoImpuesto.trim().isEmpty()) {
                impuestoSeleccionado = impuestoFacade.buscarImpuestoPorSedeAndCodigo(sedeSeleccionada, codigoImpuesto);
                if (impuestoSeleccionado != null) {
                    cargarInformacionImpuesto();
                }
            }

        }
    }

    private void cargarInformacionImpuesto() {
        if (impuestoSeleccionado != null) {
            setCodigoImpuesto(impuestoSeleccionado.getCodImpuesto());
            setNombreImpuesto(impuestoSeleccionado.getNomImpuesto());
            setPorcentajeImpuesto(impuestoSeleccionado.getPorcentaje());
            setTipoEmpresa(impuestoSeleccionado.getCfgTipoempresaId().getId());
        }
        RequestContext.getCurrentInstance().update("IdFormImpuesto");
    }

    private void limpiarFormulario() {
        setImpuestoSeleccionado(null);
        setCodigoImpuesto(null);
        setNombreImpuesto(null);
        setPorcentajeImpuesto(0);
        setTipoEmpresa(0);
        listaImpuestos = impuestoFacade.buscarImpuestoPorSede(sedeSeleccionada);
    }

    public void accion() {
        if (impuestoSeleccionado == null) {
            crearInpuesto();
        } else {
            editarImpuesto();
        }

    }

    private boolean validacion() {
        boolean ban = true;
        if (empresaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Empresa no seleccionada"));
            return false;
        }
        if (sedeSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Sede no seleccionada"));
            return false;
        }
        //        solo los usuarios super y admin pueden creary  modificar
        if (usuarioActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay usuario"));
            return false;
        }
        if (!usuarioActual.getCfgRolIdrol().getCodrol().equals("00001") && !usuarioActual.getCfgRolIdrol().getCodrol().equals("00002")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No tiene permisos para efectuar esta accion"));
            return false;
        }
        if (codigoImpuesto.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Codigo impuesto requerido"));
            return false;
        }
        if (nombreImpuesto.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Impuesto requerido"));
            return false;
        }
        if (porcentajeImpuesto == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Porcentaje impuesto requerido"));
            return false;
        }
        if (tipoEmpresa == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Tipo Empresa requerido"));
            return false;
        }
        return ban;
    }

    private void crearInpuesto() {
        if (!validacion()) {
            return;
        }
        if (impuestoSeleccionado != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ya existe un impuesto con ese codigo"));
            return;
        }
        try {
            CfgImpuesto impuesto = new CfgImpuesto();
            impuesto.setCfgempresaidEmpresa(empresaSeleccionada);
            impuesto.setFecCrea(new Date());
            impuesto.setCodImpuesto(codigoImpuesto);
            impuesto.setNomImpuesto(nombreImpuesto.trim().toUpperCase());
            impuesto.setPorcentaje(porcentajeImpuesto);
            CfgTipoempresa cfgTipoempresa = tipoempresaFacade.find(tipoEmpresa);
            impuesto.setCfgTipoempresaId(cfgTipoempresa);
            impuesto.setSegusuarioidUsuario(sesionMB.getUsuarioActual());
            impuestoFacade.create(impuesto);
            limpiarFormulario();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Impuesto creado"));
            RequestContext.getCurrentInstance().update("IdFormImpuesto");
            RequestContext.getCurrentInstance().update("IdTableImpuestos");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Impuesto no creado"));
        }
    }

    private void editarImpuesto() {
        if (!validacion()) {
            return;
        }
        if (impuestoSeleccionado != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Impuesto no seleccionado"));
            return;
        }
        try {
            impuestoSeleccionado.setNomImpuesto(nombreImpuesto.toUpperCase());
            impuestoSeleccionado.setPorcentaje(porcentajeImpuesto);
            CfgTipoempresa cfgTipoempresa = tipoempresaFacade.find(tipoEmpresa);
            impuestoSeleccionado.setCfgTipoempresaId(cfgTipoempresa);
            impuestoFacade.edit(impuestoSeleccionado);
            limpiarFormulario();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Impuesto actualizado"));
            RequestContext.getCurrentInstance().update("IdFormImpuesto");
            RequestContext.getCurrentInstance().update("IdTableImpuestos");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Impuesto no actualizado"));
        }

    }

    public void cancelar() {
        limpiarFormulario();
        RequestContext.getCurrentInstance().update("IdFormImpuesto");
        RequestContext.getCurrentInstance().update("FormModalSede");
        RequestContext.getCurrentInstance().update("FormModalEmpresa");
        RequestContext.getCurrentInstance().update("IdTableImpuestos");
    }

    public ImpuestoMB() {
    }

    public String getCodigoImpuesto() {
        return codigoImpuesto;
    }

    public void setCodigoImpuesto(String codigoImpuesto) {
        this.codigoImpuesto = codigoImpuesto;
    }

    public String getNombreImpuesto() {
        return nombreImpuesto;
    }

    public void setNombreImpuesto(String nombreImpuesto) {
        this.nombreImpuesto = nombreImpuesto;
    }

    public float getPorcentajeImpuesto() {
        return porcentajeImpuesto;
    }

    public void setPorcentajeImpuesto(float porcentajeImpuesto) {
        this.porcentajeImpuesto = porcentajeImpuesto;
    }

    public CfgEmpresa getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(CfgEmpresa empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public CfgEmpresasede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(CfgEmpresasede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public List<CfgEmpresasede> getListaSede() {
        return listaSede;
    }

    public void setListaSede(List<CfgEmpresasede> listaSede) {
        this.listaSede = listaSede;
    }

    public List<CfgImpuesto> getListaImpuestos() {
        return listaImpuestos;
    }

    public void setListaImpuestos(List<CfgImpuesto> listaImpuestos) {
        this.listaImpuestos = listaImpuestos;
    }

    public int getTipoEmpresa() {
        return tipoEmpresa;
    }

    public void setTipoEmpresa(int tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
    }

    public CfgImpuesto getImpuestoSeleccionado() {
        return impuestoSeleccionado;
    }

    public void setImpuestoSeleccionado(CfgImpuesto impuestoSeleccionado) {
        this.impuestoSeleccionado = impuestoSeleccionado;
    }

}
