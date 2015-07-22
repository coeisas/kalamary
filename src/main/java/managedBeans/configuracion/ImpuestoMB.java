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

    private String codigoEmpresa;
    private String codigoSede;
    private String codigoImpuesto;
    private String nombreImpuesto;
    private float porcentajeImpuesto;
    private int tipoEmpresa;

    private String nombreEmpresa;
    private String nombreSede;

    private CfgEmpresa empresaSeleccionada;
    private CfgEmpresasede sedeSeleccionada;
    private CfgImpuesto impuestoSeleccionado;

    private List<CfgEmpresasede> listaSede;
    private List<CfgImpuesto> listaImpuestos;

    private SesionMB sesionMB;

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
        listaImpuestos = new ArrayList();
    }

    public void buscarEmpresa() {
        if (!codigoEmpresa.trim().isEmpty()) {
            empresaSeleccionada = empresaFacade.buscarEmpresaPorCodigo(codigoEmpresa);
            if (empresaSeleccionada == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No se encontro empresa"));
            }
            cargarInformacionEmpresa();
        }
    }

    public void cargarInformacionEmpresa() {
        if (empresaSeleccionada != null) {
            setCodigoEmpresa(empresaSeleccionada.getCodEmpresa());
            setNombreEmpresa(empresaSeleccionada.getNomEmpresa());
        } else {
            limpiarFormulario();
        }
        RequestContext.getCurrentInstance().update("IdFormImpuesto");
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

    public void buscarSede() {
        if (!codigoSede.trim().isEmpty() && empresaSeleccionada != null) {
            sedeSeleccionada = sedeFacade.buscarSedePorEmpresa(empresaSeleccionada, codigoSede);
            cargarInformacionSede();
            if (sedeSeleccionada == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No se encontro sede"));
            }
        }
    }

    public void cargarInformacionSede() {
        if (sedeSeleccionada != null) {
            setCodigoSede(sedeSeleccionada.getCodSede());
            setNombreSede(sedeSeleccionada.getNomSede());
            listaImpuestos = impuestoFacade.buscarImpuestoPorSede(sedeSeleccionada);
            RequestContext.getCurrentInstance().update("IdTableImpuestos");
        } else {
            setCodigoSede(null);
            setNombreSede(null);
            setSedeSeleccionada(null);
        }
        RequestContext.getCurrentInstance().update("IdFormImpuesto");
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
        setEmpresaSeleccionada(null);
        setSedeSeleccionada(null);
        setImpuestoSeleccionado(null);
        setCodigoEmpresa(null);
        setNombreEmpresa(null);
        setCodigoSede(null);
        setNombreSede(null);
        setCodigoImpuesto(null);
        setNombreImpuesto(null);
        setPorcentajeImpuesto(0);
        setTipoEmpresa(0);
        listaImpuestos.clear();
    }

    public void accion() {
        if (impuestoSeleccionado == null) {
            crearInpuesto();
        } else {
            editarImpuesto();
        }

    }

    private void validacion() {
        if (empresaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Empresa no seleccionada"));
            return;
        }
        if (sedeSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Sede no seleccionada"));
            return;
        }
        if (codigoImpuesto.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Codigo impuesto requerido"));
            return;
        }
        if (nombreImpuesto.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Impuesto requerido"));
            return;
        }
        if (porcentajeImpuesto == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Porcentaje impuesto requerido"));
            return;
        }
        if (tipoEmpresa == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Tipo Empresa requerido"));
        }
    }

    private void crearInpuesto() {
        validacion();
        if (impuestoSeleccionado != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ya existe un impuesto con ese codigo"));
            return;
        }
        try {
            CfgImpuesto impuesto = new CfgImpuesto();
            impuesto.setCfgempresasedeidSede(sedeSeleccionada);
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
        validacion();
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

    public String getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(String codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public String getCodigoSede() {
        return codigoSede;
    }

    public void setCodigoSede(String codigoSede) {
        this.codigoSede = codigoSede;
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

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getNombreSede() {
        return nombreSede;
    }

    public void setNombreSede(String nombreSede) {
        this.nombreSede = nombreSede;
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
