/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.configuracion;

import entities.CfgCliente;
import entities.CfgEmpresa;
import entities.CfgMunicipio;
import entities.CfgMunicipioPK;
import entities.CfgTipodocempresa;
import entities.CfgTipoempresa;
import entities.CfgTipoidentificacion;
import entities.SegUsuario;
import facades.CfgClienteFacade;
import facades.CfgDepartamentoFacade;
import facades.CfgEmpresaFacade;
import facades.CfgMunicipioFacade;
import facades.CfgTipodocempresaFacade;
import facades.CfgTipoempresaFacade;
import facades.CfgTipoidentificacionFacade;
import facades.SegUsuarioFacade;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.event.PhaseId;
import managedBeans.seguridad.AplicacionMB;
import managedBeans.seguridad.SesionMB;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class EmpresaMB implements Serializable {

    private String codigo;
    private String idDepartamento;
    private String idMunicipio;
    private String nombre;
    private String numDocumento;
    private String direccion;
    private String telefono1;
    private String telefono2;
    private String website;
    private String mail;
    private UploadedFile file;
    private String nombreArchivo;
    private int tipoDocEmpresa;
    private int tipoEmpresa;

    private CfgEmpresa empresaSeleccionada;
    private List<CfgMunicipio> listaMuncipios;
    private StreamedContent image;
    private String opcion;//opcion que contiene la accion del boton guardar: creacion, modificacion

    private SesionMB sesionMB;
    private SegUsuario usuarioActual;

    @EJB
    CfgDepartamentoFacade departamentoFacade;

    @EJB
    CfgMunicipioFacade municipioFacade;

    @EJB
    CfgEmpresaFacade empresaFacade;

    @EJB
    CfgTipoempresaFacade tipoempresaFacade;

    @EJB
    CfgTipodocempresaFacade tipodocempresaFacade;

    @EJB
    CfgTipoidentificacionFacade tipoidentificacionFacade;

    @EJB
    CfgClienteFacade clienteFacade;
    
    @EJB
    SegUsuarioFacade usuarioFacade;

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        opcion = "creacion";
        listaMuncipios = new ArrayList();
        usuarioActual = sesionMB.getUsuarioActual();
    }

    /**
     * Creates a new instance of EmpresaMB
     */
    public EmpresaMB() {
    }

    public void accion() {
        switch (opcion) {
            case "creacion":
                crearEmpresa();
                break;
            case "modificacion":
                modificarEmpresa();
                break;
        }
    }

    public void buscarEmpresa() {
        empresaSeleccionada = empresaFacade.buscarEmpresaPorCodigo(codigo);
        if (empresaSeleccionada != null) {
            cargarInformacionEmpresa();
            opcion = "modificacion";
        } else {
            limpiarFormulario();
            opcion = "creacion";
            RequestContext.getCurrentInstance().update("IdFormEmpresa");
        }

    }

    public void cargarInformacionEmpresa() {
        opcion = "modificacion";
        setNombreArchivo(null);
        setCodigo(empresaSeleccionada.getCodEmpresa());
        setIdDepartamento(empresaSeleccionada.getCfgMunicipio().getCfgDepartamento().getIdDepartamento());
        listaMuncipios = municipioFacade.buscarPorDepartamento(idDepartamento);
        setIdMunicipio(empresaSeleccionada.getCfgMunicipio().getCfgMunicipioPK().getIdMunicipio());
        setTipoDocEmpresa(empresaSeleccionada.getCfgTipodocempresaId().getId());
        setTipoEmpresa(empresaSeleccionada.getCfgTipoempresaId().getId());
        setNumDocumento(empresaSeleccionada.getNumDoc());
        setNombre(empresaSeleccionada.getNomEmpresa());
        setDireccion(empresaSeleccionada.getDirEmpresa());
        setTelefono1(empresaSeleccionada.getTel1());
        setTelefono2(empresaSeleccionada.getTel2());
        setWebsite(empresaSeleccionada.getWebsite());
        setMail(empresaSeleccionada.getMail());
        if (empresaSeleccionada.getLogo() != null) {
            setImage(new DefaultStreamedContent(new ByteArrayInputStream(empresaSeleccionada.getLogo())));
        } else {
            setImage(null);
        }
        RequestContext.getCurrentInstance().update("IdFormEmpresa");
    }

    public void limpiarFormulario() {
        setNombreArchivo(null);
        setIdDepartamento(null);
        setIdMunicipio(null);
        setNumDocumento(null);
        setNombre(null);
        setTipoDocEmpresa(0);
        setTipoEmpresa(0);
        setDireccion(null);
        setTelefono1(null);
        setTelefono2(null);
        setWebsite(null);
        setMail(null);
        setImage(null);
    }

    private void crearEmpresa() {
        try {
            CfgEmpresa empresa = new CfgEmpresa();
            CfgMunicipioPK cfgMunicipioPK = new CfgMunicipioPK(idMunicipio, idDepartamento);
            CfgMunicipio municipio = municipioFacade.buscarPorMunicipioPK(cfgMunicipioPK);
            empresa.setCodEmpresa(codigo);
            empresa.setCfgMunicipio(municipio);
            CfgTipoempresa tipoempresa = tipoempresaFacade.find(tipoEmpresa);
            empresa.setCfgTipoempresaId(tipoempresa);
            CfgTipodocempresa tipodocempresa = tipodocempresaFacade.find(tipoDocEmpresa);
            empresa.setCfgTipodocempresaId(tipodocempresa);
            empresa.setDirEmpresa(direccion.toUpperCase());
            empresa.setNomEmpresa(nombre.toUpperCase());
            empresa.setMail(mail.toUpperCase());
            empresa.setTel1(telefono1);
            empresa.setTel2(telefono2);
            empresa.setNumDoc(numDocumento);
            empresa.setFecCrea(new Date());
            empresa.setWebsite(website.toUpperCase());
            if (file != null) {
                empresa.setLogo(getFile().getContents());
            }
            empresa.setSegusuarioidUsuario(usuarioActual);
            empresaFacade.create(empresa);
            crearClienteDefault(empresa);
            setEmpresaSeleccionada(null);
            setCodigo(null);
            limpiarFormulario();
            FacesContext context = FacesContext.getCurrentInstance();
            AplicacionMB aplicacionMB = context.getApplication().evaluateExpressionGet(context, "#{aplicacionMB}", AplicacionMB.class);
            aplicacionMB.actualizarListaEmpresas();
            RequestContext.getCurrentInstance().update("IdFormEmpresa");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Empresa creada"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Empresa no creada"));
        }

    }

    private void modificarEmpresa() {
        try {
            CfgMunicipioPK cfgMunicipioPK = new CfgMunicipioPK(idMunicipio, idDepartamento);
            CfgMunicipio municipio = municipioFacade.buscarPorMunicipioPK(cfgMunicipioPK);
            empresaSeleccionada.setCfgMunicipio(municipio);
            CfgTipoempresa tipoempresa = tipoempresaFacade.find(tipoEmpresa);
            empresaSeleccionada.setCfgTipoempresaId(tipoempresa);
            CfgTipodocempresa tipodocempresa = tipodocempresaFacade.find(tipoDocEmpresa);
            empresaSeleccionada.setCfgTipodocempresaId(tipodocempresa);
            empresaSeleccionada.setDirEmpresa(direccion.toUpperCase());
            empresaSeleccionada.setNomEmpresa(nombre.toUpperCase());
            empresaSeleccionada.setMail(mail.toUpperCase());
            empresaSeleccionada.setTel1(telefono1);
            empresaSeleccionada.setTel2(telefono2);
            empresaSeleccionada.setNumDoc(numDocumento);
            empresaSeleccionada.setWebsite(website.toUpperCase());
            if (file != null) {
                empresaSeleccionada.setLogo(getFile().getContents());
            }
            empresaFacade.edit(empresaSeleccionada);
            setEmpresaSeleccionada(null);
            setCodigo(null);
            limpiarFormulario();
            RequestContext.getCurrentInstance().update("IdFormEmpresa");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Empresa modificada"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Empresa no modificada"));
        }
    }

    public void actualizarMunicipios() {
        listaMuncipios.clear();
        if (idDepartamento != null) {
            listaMuncipios = municipioFacade.buscarPorDepartamento(idDepartamento);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        if (file != null) {
            setNombreArchivo(getFile().getFileName());
            RequestContext.getCurrentInstance().update("IdFormEmpresa:nombreImagen");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Logo cargado"));
            RequestContext.getCurrentInstance().execute("PF('dlgLogo').hide()");
        }
    }

    private void crearClienteDefault(CfgEmpresa  empresa) {
        try {
            CfgCliente cliente = new CfgCliente();
            cliente.setNom1Cliente("CLIENTE");
            cliente.setApellido1("DEFAULT");
            cliente.setNumDoc("1");
            cliente.setCodigoCliente("1");
            cliente.setTel1("1");
            cliente.setDirCliente("DIRECCION CLIENTE DEFAULT");
            cliente.setFecCrea(new Date());
            cliente.setSegusuarioidUsuario(usuarioActual);
            cliente.setCfgempresaidEmpresa(empresa);
            CfgTipoidentificacion tipoidentificacion = tipoidentificacionFacade.buscarByCodigo("00009");//SIN DETERMINAR
            cliente.setCfgTipoidentificacionId(tipoidentificacion);
            CfgTipoempresa tipoempresa = tipoempresaFacade.buscarByCodigo("00011");//PERSONA NATURAL REGIMEN SIMPLIFICADO
            cliente.setCfgTipoempresaId(tipoempresa);
            CfgMunicipioPK cfgMunicipioPK = new CfgMunicipioPK(idMunicipio, idDepartamento);
            CfgMunicipio municipio = municipioFacade.buscarPorMunicipioPK(cfgMunicipioPK);
            cliente.setCfgMunicipio(municipio);
            clienteFacade.create(cliente);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cliente Defautl no creado"));
        }
    }

    public String getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(String idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public List<CfgMunicipio> getListaMuncipios() {
        return listaMuncipios;
    }

    public void setListaMuncipios(List<CfgMunicipio> listaMuncipios) {
        this.listaMuncipios = listaMuncipios;
    }

    public String getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(String idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
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

    public CfgEmpresa getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(CfgEmpresa empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
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
            if (empresaSeleccionada != null) {
                String imageId = context.getExternalContext().getRequestParameterMap().get("id");
                CfgEmpresa empresa = empresaFacade.find(Integer.parseInt(imageId));
                image = new DefaultStreamedContent(new ByteArrayInputStream(empresa.getLogo()));
                return image;
            } else {
                return null;
            }
        }
    }

    public void setImage(StreamedContent image) {
        this.image = image;
    }

    public int getTipoDocEmpresa() {
        return tipoDocEmpresa;
    }

    public void setTipoDocEmpresa(int tipoDocEmpresa) {
        this.tipoDocEmpresa = tipoDocEmpresa;
    }

    public int getTipoEmpresa() {
        return tipoEmpresa;
    }

    public void setTipoEmpresa(int tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

}
