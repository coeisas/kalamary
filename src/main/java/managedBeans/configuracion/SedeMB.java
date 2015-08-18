/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.configuracion;

import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.CfgMunicipio;
import entities.CfgMunicipioPK;
import entities.SegUsuario;
import facades.CfgEmpresaFacade;
import facades.CfgEmpresasedeFacade;
import facades.CfgMunicipioFacade;
import java.io.ByteArrayInputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import java.util.List;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import managedBeans.seguridad.SesionMB;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class SedeMB implements Serializable {

    private String codSede;
    private String idDepartamento;
    private String idMuncipio;
    private String numDocumento;//puede ser el mismo numero de la empresa o correspondera a uno diferente
    private String nombreSede;
    private UploadedFile file;
    private String direccion;
    private String telefono1;
    private String telefono2;
    private String website;
    private String mail;
    
    private String opcion;
    private CfgEmpresa empresaSeleccionada;
    private CfgEmpresasede sedeSeleccionada;
    private StreamedContent image;
    private String nombreArchivo;
    private List<CfgMunicipio> listaMunicipios;
    private List<CfgEmpresasede> listaSedes;
    
    private SegUsuario usuarioActual;
    private SesionMB sesionMB;
    
    @EJB
    CfgEmpresaFacade empresaFacade;
    
    @EJB
    CfgEmpresasedeFacade sedeFacade;
    
    @EJB
    CfgMunicipioFacade municipioFacade;
    
    public SedeMB() {
    }
    
    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
        empresaSeleccionada = sesionMB.getEmpresaActual();
        if (empresaSeleccionada != null) {
            listaSedes = sedeFacade.buscarSedesPorEmpresa(empresaSeleccionada.getIdEmpresa());
//        } else {
//            listaSedes = new ArrayList();
        }else{
            listaSedes = new ArrayList();
        }
        setListaMunicipios((List<CfgMunicipio>) new ArrayList());
    }
    
    public void buscarSede() {
        if (!codSede.trim().isEmpty() && empresaSeleccionada != null) {
            setSedeSeleccionada(sedeFacade.buscarSedePorEmpresa(empresaSeleccionada, codSede));
            if (sedeSeleccionada != null) {
                cargarInformacionSede();
            } else {
                limpiarFormulario();
                setNumDocumento(empresaSeleccionada.getNumDoc());
                opcion = "creacion";
            }
        } else {
            setSedeSeleccionada(null);
            limpiarFormulario();
        }
        RequestContext.getCurrentInstance().update("IdFormSede");
    }
    
    public void limpiarFormulario() {
        listaSedes = sedeFacade.buscarSedesPorEmpresa(empresaSeleccionada.getIdEmpresa());
        setNombreArchivo(null);
        setIdDepartamento(null);
        setIdMuncipio(null);
        setNumDocumento(null);
        setNombreSede(null);
        setDireccion(null);
        setTelefono1(null);
        setTelefono2(null);
        setWebsite(null);
        setMail(null);
        setImage(null);
    }
    
    public void actualizarMunicipios() {
        getListaMunicipios().clear();
        if (getIdDepartamento() != null) {
            setListaMunicipios(municipioFacade.buscarPorDepartamento(getIdDepartamento()));
        }
    }
    
    public void cargarInformacionSede() {
        if (sedeSeleccionada != null) {
            opcion = "modificacion";
            setNombreArchivo(null);
//            setCodEmpresa(getEmpresaSeleccionada().getCodEmpresa());
            setCodSede(sedeSeleccionada.getCodSede());
            setNumDocumento(empresaSeleccionada.getNumDoc());
            setIdDepartamento(sedeSeleccionada.getCfgMunicipio().getCfgDepartamento().getIdDepartamento());
            listaMunicipios = municipioFacade.buscarPorDepartamento(idDepartamento);
            setIdMuncipio(sedeSeleccionada.getCfgMunicipio().getCfgMunicipioPK().getIdMunicipio());
            setNumDocumento(sedeSeleccionada.getNumDocumento());
            setNombreSede(sedeSeleccionada.getNomSede());
            setDireccion(sedeSeleccionada.getDireccion());
            setTelefono1(sedeSeleccionada.getTel1());
            setTelefono2(sedeSeleccionada.getTel2());
            setWebsite(sedeSeleccionada.getWebsite());
            setMail(sedeSeleccionada.getMail());
            if (sedeSeleccionada.getLogo() != null) {
                setImage(new DefaultStreamedContent(new ByteArrayInputStream(sedeSeleccionada.getLogo())));
            } else {
                setImage(null);
            }
            RequestContext.getCurrentInstance().update("IdFormSede");
        }
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        if (file != null) {
            setNombreArchivo(getFile().getFileName());
            RequestContext.getCurrentInstance().update("IdFormSede:nombreImagen");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Logo cargado"));
            RequestContext.getCurrentInstance().execute("PF('dlgLogo').hide()");
        }
    }
    
    public void accion() {
        switch (opcion) {
            case "creacion":
                crearSede();
                break;
            case "modificacion":
                modificarSede();
                break;
        }
    }
    
    private boolean validar() {
        boolean ban = true;
        if (empresaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Asigne un departamento"));
            return false;
        }
//        solo los usuarios super y admin pueden crear y modificar sedes
        if (!usuarioActual.getCfgRolIdrol().getCodrol().equals("00001") && !usuarioActual.getCfgRolIdrol().getCodrol().equals("00002")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No tiene permisos para efectuar esta accion"));
            return false;
        }        
        if (idDepartamento == null || idDepartamento.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Asigne un departamento"));
            return false;
        }
        if (idMuncipio == null || idMuncipio.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Elija un municipio"));
            return false;
        }
        if (codSede == null || codSede.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Codigo de sede obligatorio"));
            return false;
        }
        if (numDocumento == null || numDocumento.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese un numero de documento"));
            return false;
        }
        if (nombreSede == null || nombreSede.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Determine el nombre de la sede"));
            return false;
        }
        if (direccion == null || direccion.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Se requiere la direccion"));
            return false;
        }
        if (telefono1 == null || telefono1.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta telefono 1"));
            return false;
        }
        return ban;
    }
    
    private void crearSede() {
        if (!validar()) {
            return;
        }
        try {
            CfgEmpresasede sede = new CfgEmpresasede();
            sede.setCodSede(codSede);
            CfgMunicipioPK cfgMunicipioPK = new CfgMunicipioPK(idMuncipio, idDepartamento);
            CfgMunicipio municipio = municipioFacade.buscarPorMunicipioPK(cfgMunicipioPK);
            sede.setCfgempresaidEmpresa(empresaSeleccionada);
            sede.setCfgMunicipio(municipio);
            sede.setDireccion(direccion.toUpperCase());
            sede.setNomSede(nombreSede.toUpperCase());
            sede.setMail(mail.toUpperCase());
            sede.setTel1(telefono1);
            sede.setTel2(telefono2);
            sede.setNumDocumento(numDocumento);
            sede.setWebsite(website.toUpperCase());
            sede.setActivo(true);
            if (file != null) {
                sede.setLogo(getFile().getContents());
            } else {
                sede.setLogo(empresaSeleccionada.getLogo());
            }
            sedeFacade.create(sede);
//            setEmpresaSeleccionada(null);
            setSedeSeleccionada(null);
//            setCodEmpresa(null);
            setCodSede(null);
            limpiarFormulario();
            RequestContext.getCurrentInstance().update("IdFormSede");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Sede creada"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Sede no creada"));
        }
    }
    
    private void modificarSede() {
        if (!validar()) {
            return;
        }
        if (sedeSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione la sede a modificar"));
            return;
        }
        try {
            CfgMunicipioPK cfgMunicipioPK = new CfgMunicipioPK(idMuncipio, idDepartamento);
            CfgMunicipio municipio = municipioFacade.buscarPorMunicipioPK(cfgMunicipioPK);
            sedeSeleccionada.setCfgMunicipio(municipio);
            sedeSeleccionada.setDireccion(direccion.toUpperCase());
            sedeSeleccionada.setNomSede(nombreSede.toUpperCase());
            sedeSeleccionada.setMail(mail.toUpperCase());
            sedeSeleccionada.setTel1(telefono1);
            sedeSeleccionada.setTel2(telefono2);
            sedeSeleccionada.setNumDocumento(numDocumento);
            sedeSeleccionada.setWebsite(website.toUpperCase());
            if (file != null) {
                sedeSeleccionada.setLogo(getFile().getContents());
            }
            sedeFacade.edit(sedeSeleccionada);
//            setEmpresaSeleccionada(null);
            setSedeSeleccionada(null);
//            setCodEmpresa(null);
            setCodSede(null);
            limpiarFormulario();
            RequestContext.getCurrentInstance().update("IdFormSede");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Sede modificada"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Sede no modificada"));
        }
    }

    public String getCodSede() {
        return codSede;
    }
    
    public void setCodSede(String codSede) {
        this.codSede = codSede;
    }
    
    public String getIdMuncipio() {
        return idMuncipio;
    }
    
    public void setIdMuncipio(String idMuncipio) {
        this.idMuncipio = idMuncipio;
    }
    
    public String getNumDocumento() {
        return numDocumento;
    }
    
    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }
    
    public String getNombreSede() {
        return nombreSede;
    }
    
    public void setNombreSede(String nombreSede) {
        this.nombreSede = nombreSede;
    }
    
    public UploadedFile getFile() {
        return file;
    }
    
    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getTelefono1() {
        return telefono1;
    }
    
    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }
    
    public String getTelefono2() {
        return telefono2;
    }
    
    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public String getMail() {
        return mail;
    }
    
    public void setMail(String mail) {
        this.mail = mail;
    }
    
    public String getIdDepartamento() {
        return idDepartamento;
    }
    
    public void setIdDepartamento(String idDepartamento) {
        this.idDepartamento = idDepartamento;
    }
    
    public List<CfgMunicipio> getListaMunicipios() {
        return listaMunicipios;
    }
    
    public void setListaMunicipios(List<CfgMunicipio> listaMunicipios) {
        this.listaMunicipios = listaMunicipios;
    }
    
    public String getNombreArchivo() {
        return nombreArchivo;
    }
    
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
    
    public StreamedContent getImage() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {//fase de jsf
            image = new DefaultStreamedContent();
            return image;
        } else {
            if (sedeSeleccionada != null) {
                String imageId = context.getExternalContext().getRequestParameterMap().get("id");
                CfgEmpresasede sede = sedeFacade.find(Integer.parseInt(imageId));
                image = new DefaultStreamedContent(new ByteArrayInputStream(sede.getLogo()));
                return image;
            } else {
                return null;
            }
        }
    }
    
    public void setImage(StreamedContent image) {
        this.image = image;
    }
    
    public List<CfgEmpresasede> getListaSedes() {
        return listaSedes;
    }
    
    public void setListaSedes(List<CfgEmpresasede> listaSedes) {
        this.listaSedes = listaSedes;
    }
    
    public CfgEmpresasede getSedeSeleccionada() {
        return sedeSeleccionada;
    }
    
    public void setSedeSeleccionada(CfgEmpresasede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

}
