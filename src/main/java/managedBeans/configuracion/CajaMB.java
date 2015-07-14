/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.configuracion;

import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.FacCaja;
import facades.CfgEmpresaFacade;
import facades.CfgEmpresasedeFacade;
import facades.FacCajaFacade;
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
public class CajaMB implements Serializable {

    private String codigoEmpresa;
    private String nombreEmpresa;
    private String codigoSede;
    private String nombreSede;
    private String codigoCaja;
    private String nombreCaja;

    private boolean codigoCajaValidado = false;

    private List<CfgEmpresasede> listaSedes;
    private List<FacCaja> listaCajas;

    private FacCaja cajaSeleccionada;
    private CfgEmpresa empresaSeleccionada;
    private CfgEmpresasede sedeSeleccionada;
    
    private SesionMB sesionMB;

    @EJB
    CfgEmpresasedeFacade sedeFacade;

    @EJB
    CfgEmpresaFacade empresaFacade;

    @EJB
    FacCajaFacade cajaFacade;

    public CajaMB() {
    }
    
    @PostConstruct
    private void init(){
        FacesContext context = FacesContext.getCurrentInstance();
        sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
    }

    public void buscarEmpresaPorCodigo() {
        empresaSeleccionada = empresaFacade.buscarEmpresaPorCodigo(codigoEmpresa);
        if (empresaSeleccionada == null && !codigoEmpresa.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No se encontro empresa"));
        }
        cargarInformacionEmpresa();
    }

    public void buscarSedePorCodigo() {
        if (empresaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione una Empresa"));
        } else {
            sedeSeleccionada = sedeFacade.buscarSedePorEmpresa(empresaSeleccionada, codigoSede.trim());
            if (sedeSeleccionada == null && !codigoSede.trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No se encontro sede"));
            }
            cargarInformacionSede();
        }
    }

    public void cargarSedes() {
        if (empresaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione una Empresa"));
        } else {
            RequestContext.getCurrentInstance().update("FormModalSede");
            RequestContext.getCurrentInstance().execute("PF('dlgSede').show()");
        }
    }

    public void cargarInformacionEmpresa() {
        if (empresaSeleccionada != null) {
            listaSedes = sedeFacade.buscarSedesPorEmpresa(empresaSeleccionada.getIdEmpresa());
            setNombreEmpresa(empresaSeleccionada.getNomEmpresa());
            setCodigoEmpresa(empresaSeleccionada.getCodEmpresa());
            RequestContext.getCurrentInstance().execute("PF('dlgEmpresa').hide()");
            if (codigoSede != null) {
                if (!codigoSede.trim().isEmpty()) {
                    buscarSedePorCodigo();
                }
            }
        } else {
            limpiarFormulario();
            RequestContext.getCurrentInstance().update("IdDataCaja");
        }
        RequestContext.getCurrentInstance().update("IdFormCaja");

    }

    public void cargarInformacionSede() {
        if (sedeSeleccionada != null) {
            setNombreSede(sedeSeleccionada.getNomSede());
            setCodigoSede(sedeSeleccionada.getCodSede());
            RequestContext.getCurrentInstance().execute("PF('dlgSede').hide()");
            listaCajas = cajaFacade.buscarCajasPorSede(sedeSeleccionada);
            RequestContext.getCurrentInstance().update("IdDataCaja");
        } else {
            setCodigoSede("");
            setNombreSede("");
        }
        RequestContext.getCurrentInstance().update("IdFormCaja");

    }

    public void limpiarFormulario() {
        setCodigoEmpresa("");
        setNombreEmpresa("");
        setCodigoSede("");
        setNombreSede("");
        setCodigoCaja("");
        setNombreCaja("");
        setListaSedes(null);
        setListaCajas(null);
    }

    public void buscarCajaPorCodigo() {
        if (sedeSeleccionada == null) {
            codigoCajaValidado = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione una sede"));
        } else {
            cajaSeleccionada = cajaFacade.buscarCajasPorSedeAndCodigo(sedeSeleccionada, codigoCaja);
            if (cajaSeleccionada == null) {
                codigoCajaValidado = true;
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Ya existe una caja con ese codigo"));
            }

        }

    }

    public void cancelar() {
        limpiarFormulario();
        setSedeSeleccionada(null);
        setEmpresaSeleccionada(null);
        RequestContext.getCurrentInstance().update("IdFormCaja");
        RequestContext.getCurrentInstance().update("IdDataCaja");
    }

    public void crearCaja() {
        if (empresaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione la empresa"));
            return;
        }
        if (sedeSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione la sede"));
            return;
        }
        if (nombreCaja.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese el nombre de la caja"));
            return;
        }
        if (codigoCaja.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese el codigo de la caja"));
            return;
        }
        if (cajaSeleccionada != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Codigo de caja existente para la sede"));
            return;
        }
        try {
            FacCaja caja = new FacCaja();
            caja.setCfgempresasedeidSede(sedeSeleccionada);
            caja.setCodigoCaja(codigoCaja.toUpperCase());
            caja.setNomCaja(nombreCaja.toUpperCase());
            caja.setCerrada(true);
            caja.setFeccrea(new Date());
            caja.setUsrcrea(sesionMB.getUsuarioActual().getIdUsuario());
            cajaFacade.create(caja);
            cancelar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion", "Caja creada"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Caja no creada"));
        }
    }

    public String getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(String codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getCodigoSede() {
        return codigoSede;
    }

    public void setCodigoSede(String codigoSede) {
        this.codigoSede = codigoSede;
    }

    public String getNombreSede() {
        return nombreSede;
    }

    public void setNombreSede(String nombreSede) {
        this.nombreSede = nombreSede;
    }

    public String getCodigoCaja() {
        return codigoCaja;
    }

    public void setCodigoCaja(String codigoCaja) {
        this.codigoCaja = codigoCaja;
    }

    public String getNombreCaja() {
        return nombreCaja;
    }

    public void setNombreCaja(String nombreCaja) {
        this.nombreCaja = nombreCaja;
    }

    public List<CfgEmpresasede> getListaSedes() {
        return listaSedes;
    }

    public void setListaSedes(List<CfgEmpresasede> listaSedes) {
        this.listaSedes = listaSedes;
    }

    public List<FacCaja> getListaCajas() {
        return listaCajas;
    }

    public void setListaCajas(List<FacCaja> listaCajas) {
        this.listaCajas = listaCajas;
    }

    public FacCaja getCajaSeleccionada() {
        return cajaSeleccionada;
    }

    public void setCajaSeleccionada(FacCaja cajaSeleccionada) {
        this.cajaSeleccionada = cajaSeleccionada;
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

}
