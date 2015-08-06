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
import entities.CfgTipoempresa;
import facades.CfgClienteFacade;
import facades.CfgEmpresaFacade;
import facades.CfgMunicipioFacade;
import facades.CfgTipoempresaFacade;
import facades.CfgTipoidentificacionFacade;
import java.io.ByteArrayInputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;
import javax.ejb.EJB;
import java.util.List;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.el.ELException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import managedBeans.seguridad.SesionMB;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class ClienteMB implements Serializable {
    
    private String codEmpresa;
    private String nombreEmpresa;
    private String codigoCliente;
    private String idDepartamento;
    private String idMunicipio;
    private int idIdentificacion;
    private String numIdentificacion;
    private int idTipoCliente;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private UploadedFile file;
    private String direccion;
    private String telefono;
    private String mail;
    private Date fechaNacimiento;
    private String tarjetaMembresia;
    private float cupoCredito;
    private StreamedContent image;
    
    private String opcion;
    private CfgCliente clienteSeleccionado;
    private CfgEmpresa empresaSeleccionada;
    private List<CfgCliente> listaClientes;
    private List<CfgMunicipio> listaMunicipios;
    
    @EJB
    CfgClienteFacade clienteFacade;
    
    @EJB
    CfgEmpresaFacade empresaFacade;
    
    @EJB
    CfgMunicipioFacade municipioFacade;
    
    @EJB
    CfgTipoidentificacionFacade tipoidentificacionFacade;
    
    @EJB
    CfgTipoempresaFacade tipoClienteFacade;
    
    public ClienteMB() {
    }
    
    @PostConstruct
    private void init() {
        opcion = "creacion";
        listaMunicipios = new ArrayList();
        listaClientes = new ArrayList();
        
    }
    
    public void cargarClientes() {
        if (empresaSeleccionada != null) {
            listaClientes = clienteFacade.buscarPorEmpresa(empresaSeleccionada);
            RequestContext.getCurrentInstance().update("FormBuscarCliente");
            RequestContext.getCurrentInstance().execute("PF('dlgCliente').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Determine la empresa"));
        }
    }
    
    public void cargarInformacionCliente() {
        if (getClienteSeleccionado() != null) {
            opcion = "modificacion";
            setNombreEmpresa(clienteSeleccionado.getCfgempresaidEmpresa().getNomEmpresa());
            setCodigoCliente(clienteSeleccionado.getCodigoCliente());
            setEmpresaSeleccionada(clienteSeleccionado.getCfgempresaidEmpresa());
            setCodEmpresa(empresaSeleccionada.getCodEmpresa());
            setIdDepartamento(empresaSeleccionada.getCfgMunicipio().getCfgDepartamento().getIdDepartamento());
            setListaMunicipios(municipioFacade.buscarPorDepartamento(idDepartamento));
            setIdMunicipio(clienteSeleccionado.getCfgMunicipio().getCfgMunicipioPK().getIdMunicipio());
            setIdIdentificacion(clienteSeleccionado.getCfgTipoidentificacionId().getId());
            setIdTipoCliente(clienteSeleccionado.getCfgTipoempresaId().getId());
            setNumIdentificacion(clienteSeleccionado.getNumDoc());
            setPrimerNombre(clienteSeleccionado.getNom1Cliente());
            setSegundoNombre(clienteSeleccionado.getNom2Cliente());
            setPrimerApellido(clienteSeleccionado.getApellido1());
            setSegundoApellido(clienteSeleccionado.getApellido2());
            setDireccion(clienteSeleccionado.getDirCliente());
            if (clienteSeleccionado.getCupoCredito() != null) {
                setCupoCredito(clienteSeleccionado.getCupoCredito());
            } else {
                setCupoCredito(0);
            }
            setTarjetaMembresia(clienteSeleccionado.getTarjetaMembresia());
            setTelefono(clienteSeleccionado.getTel1());
            setMail(clienteSeleccionado.getMail());
            setFechaNacimiento(clienteSeleccionado.getFecNacimiento());
            if (clienteSeleccionado.getFoto() != null) {
                setImage(new DefaultStreamedContent(new ByteArrayInputStream(clienteSeleccionado.getFoto())));
            } else {
                setImage(null);
            }
        } else {
            opcion = "creacion";
            listaMunicipios.clear();
            limpiarFormulario();
        }
        RequestContext.getCurrentInstance().execute("PF('dlgCliente').hide()");
        RequestContext.getCurrentInstance().update("IdFormCliente");
    }
    
    private void limpiarFormulario() {
        setIdIdentificacion(0);
//        setNombreEmpresa(null);
        setNumIdentificacion(null);
        setPrimerNombre(null);
        setIdDepartamento(null);
        setIdTipoCliente(0);
        setSegundoNombre(null);
        setPrimerApellido(null);
        setSegundoApellido(null);
        setDireccion(null);
        setCupoCredito(0);
        setTarjetaMembresia(null);
        setTelefono(null);
        setMail(null);
        setFechaNacimiento(null);
        opcion = "creacion";
    }
    
    public void buscarCliente() {
        if (empresaSeleccionada != null) {
            if (codigoCliente != null && !codigoCliente.trim().isEmpty()) {
                clienteSeleccionado = clienteFacade.buscarPorCodigoAndIdEmpresa(codigoCliente, empresaSeleccionada.getIdEmpresa());
                cargarInformacionCliente();
            }
        } else {
            clienteSeleccionado = null;
            cargarInformacionCliente();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Determine la empresa"));
        }
    }
    
    public void buscarEmpresa() {
        empresaSeleccionada = null;
        if (!codEmpresa.trim().isEmpty()) {
            setEmpresaSeleccionada(empresaFacade.buscarEmpresaPorCodigo(codEmpresa));
            if (getEmpresaSeleccionada() != null) {
                if (clienteSeleccionado == null) {
                    limpiarFormulario();
                }
                actualizarEmpresa();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro empresa con el codigo " + codEmpresa));
            }
        }
        if (getEmpresaSeleccionada() == null) {
            if (clienteSeleccionado == null) {
                limpiarFormulario();
            }
        }
        RequestContext.getCurrentInstance().update("IdFormCliente");
        
    }
    
    public void actualizarEmpresa() {
        if (getEmpresaSeleccionada() != null) {
            setCodEmpresa(getEmpresaSeleccionada().getCodEmpresa());
            setNombreEmpresa(empresaSeleccionada.getNomEmpresa());
            if (codigoCliente != null) {
                buscarCliente();
            }
        } else {
            if (clienteSeleccionado == null) {
                limpiarFormulario();
            }
        }
        RequestContext.getCurrentInstance().update("FormModalSede");
        RequestContext.getCurrentInstance().update("IdFormCliente");
    }
    
    public void actualizarMunicipios() {
        listaMunicipios.clear();
        if (idDepartamento != null) {
            listaMunicipios = municipioFacade.buscarPorDepartamento(idDepartamento);
        }
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        if (file != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Foto cargada"));
            RequestContext.getCurrentInstance().execute("PF('dlgFoto').hide()");
        }
    }
    
    public void accion() {
        switch (opcion) {
            case "modificacion":
                modificarCliente();
                break;
            case "creacion":
                crearCliente();
                break;
        }
    }
    
    private void crearCliente() {
        if (!validarCamposFormulario()) {
            return;
        }
        try {
            CfgMunicipioPK cfgMunicipioPK = new CfgMunicipioPK(idMunicipio, idDepartamento);
            CfgMunicipio municipio = municipioFacade.buscarPorMunicipioPK(cfgMunicipioPK);
            CfgTipoempresa tipocliente = tipoClienteFacade.find(idTipoCliente);
            CfgCliente cliente = new CfgCliente();
            cliente.setCodigoCliente(codigoCliente);
            cliente.setCfgMunicipio(municipio);
            cliente.setApellido1(primerApellido.trim().toUpperCase());
            cliente.setApellido2(segundoApellido.trim().toUpperCase());
            cliente.setCfgTipoidentificacionId(tipoidentificacionFacade.find(idIdentificacion));
            cliente.setCfgempresaidEmpresa(empresaSeleccionada);
            cliente.setCfgTipoempresaId(tipocliente);
            cliente.setDirCliente(direccion.trim().toUpperCase());
            cliente.setFecCrea(new Date());
            if (file != null) {
                cliente.setFoto(file.getContents());
            }
            cliente.setMail(mail.trim().toUpperCase());
            cliente.setCupoCredito(cupoCredito);
            cliente.setTarjetaMembresia(tarjetaMembresia);
            cliente.setNom1Cliente(primerNombre.trim().toUpperCase());
            cliente.setNom2Cliente(segundoNombre.trim().toUpperCase());
            cliente.setNumDoc(numIdentificacion.trim());
            cliente.setTel1(telefono.trim());
            cliente.setFecNacimiento(fechaNacimiento);
            FacesContext context = FacesContext.getCurrentInstance();
            SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
            cliente.setSegusuarioidUsuario(sesionMB.getUsuarioActual());
            clienteFacade.create(cliente);
            listaMunicipios.clear();
            limpiarFormulario();
            setCodEmpresa(null);
            setCodigoCliente(null);
            setEmpresaSeleccionada(null);
            RequestContext.getCurrentInstance().update("IdFormCliente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Cliente creado"));
        } catch (NumberFormatException | ELException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cliente no creado"));
        }
    }
    
    private void modificarCliente() {
        if (!validarCamposFormulario()) {
            return;
        }
        try {
            CfgMunicipioPK cfgMunicipioPK = new CfgMunicipioPK(idMunicipio, idDepartamento);
            CfgMunicipio municipio = municipioFacade.buscarPorMunicipioPK(cfgMunicipioPK);
            CfgTipoempresa tipocliente = tipoClienteFacade.find(idTipoCliente);
            clienteSeleccionado.setApellido1(primerApellido.trim().toUpperCase());
            clienteSeleccionado.setApellido2(segundoApellido.trim().toUpperCase());
            clienteSeleccionado.setCfgMunicipio(municipio);
            clienteSeleccionado.setCfgTipoidentificacionId(tipoidentificacionFacade.find(idIdentificacion));
            clienteSeleccionado.setCfgTipoempresaId(tipocliente);
            clienteSeleccionado.setCfgempresaidEmpresa(empresaSeleccionada);
            clienteSeleccionado.setDirCliente(direccion.trim().toUpperCase());
            if (file != null) {
                clienteSeleccionado.setFoto(file.getContents());
            }
            clienteSeleccionado.setMail(mail.trim().toUpperCase());
            clienteSeleccionado.setNom1Cliente(primerNombre.trim().toUpperCase());
            clienteSeleccionado.setNom2Cliente(segundoNombre.trim().toUpperCase());
            clienteSeleccionado.setCupoCredito(cupoCredito);
            clienteSeleccionado.setTarjetaMembresia(tarjetaMembresia);
            clienteSeleccionado.setNumDoc(numIdentificacion.trim());
            clienteSeleccionado.setFecNacimiento(fechaNacimiento);
            clienteSeleccionado.setTel1(telefono.trim());
            clienteFacade.edit(clienteSeleccionado);
            limpiarFormulario();
            listaMunicipios.clear();
            setCodEmpresa(null);
            setCodigoCliente(null);
            setClienteSeleccionado(null);
            setEmpresaSeleccionada(null);
            opcion = "creacion";
            RequestContext.getCurrentInstance().update("FormBuscarCliente");
            RequestContext.getCurrentInstance().update("IdFormCliente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Cliente modificado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cliente no modificado"));
        }
    }
    
    private boolean validarCamposFormulario() {
        if (empresaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Determine la empresa"));
            return false;
        }
        if (codigoCliente.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Asigne un codigo al cliente"));
            return false;
        }
        if (idIdentificacion == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Determine el tipo de identificacion"));
            return false;
        }
        if (numIdentificacion.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Numero identificacion requerido"));
            return false;
        }
        if (primerNombre.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Primer Nombre necesario"));
            return false;
        }
        if (primerApellido.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Primer apellido necesario"));
            return false;
        }
        if (direccion.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Direccion vacia"));
            return false;
            
        }
        if (telefono.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese un numero telefonico"));
            return false;
        }
        return true;
    }
    
    public void cancelar() {
        clienteSeleccionado = null;
        empresaSeleccionada = null;
        listaMunicipios.clear();
        setCodEmpresa(null);
        setCodigoCliente(null);
        limpiarFormulario();
        RequestContext.getCurrentInstance().update("IdFormCliente");
    }
    
    public String getCodEmpresa() {
        return codEmpresa;
    }
    
    public void setCodEmpresa(String codEmpresa) {
        this.codEmpresa = codEmpresa;
    }
    
    public int getIdIdentificacion() {
        return idIdentificacion;
    }
    
    public void setIdIdentificacion(int idIdentificacion) {
        this.idIdentificacion = idIdentificacion;
    }
    
    public String getNumIdentificacion() {
        return numIdentificacion;
    }
    
    public void setNumIdentificacion(String numIdentificacion) {
        this.numIdentificacion = numIdentificacion;
    }
    
    public String getPrimerNombre() {
        return primerNombre;
    }
    
    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }
    
    public String getSegundoNombre() {
        return segundoNombre;
    }
    
    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }
    
    public String getPrimerApellido() {
        return primerApellido;
    }
    
    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }
    
    public String getSegundoApellido() {
        return segundoApellido;
    }
    
    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
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
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getMail() {
        return mail;
    }
    
    public void setMail(String mail) {
        this.mail = mail;
    }
    
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public String getTarjetaMembresia() {
        return tarjetaMembresia;
    }
    
    public void setTarjetaMembresia(String tarjetaMembresia) {
        this.tarjetaMembresia = tarjetaMembresia;
    }
    
    public float getCupoCredito() {
        return cupoCredito;
    }
    
    public void setCupoCredito(float cupoCredito) {
        this.cupoCredito = cupoCredito;
    }
    
    public List<CfgCliente> getListaClientes() {
        return listaClientes;
    }
    
    public void setListaClientes(List<CfgCliente> listaClientes) {
        this.listaClientes = listaClientes;
    }
    
    public CfgCliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }
    
    public void setClienteSeleccionado(CfgCliente clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
    }
    
    public CfgEmpresa getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }
    
    public void setEmpresaSeleccionada(CfgEmpresa empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }
    
    public StreamedContent getImage() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {//fase de jsf
            image = new DefaultStreamedContent();
            return image;
        } else {
            if (clienteSeleccionado != null) {
                String imageId = context.getExternalContext().getRequestParameterMap().get("id");
                CfgCliente cliente = clienteFacade.find(Integer.parseInt(imageId));
                image = new DefaultStreamedContent(new ByteArrayInputStream(cliente.getFoto()));
                return image;
            } else {
                return null;
            }
        }
    }
    
    public void setImage(StreamedContent image) {
        this.image = image;
    }
    
    public String getIdDepartamento() {
        return idDepartamento;
    }
    
    public void setIdDepartamento(String idDepartamento) {
        this.idDepartamento = idDepartamento;
    }
    
    public String getIdMunicipio() {
        return idMunicipio;
    }
    
    public void setIdMunicipio(String idMunicipio) {
        this.idMunicipio = idMunicipio;
    }
    
    public List<CfgMunicipio> getListaMunicipios() {
        return listaMunicipios;
    }
    
    public void setListaMunicipios(List<CfgMunicipio> listaMunicipios) {
        this.listaMunicipios = listaMunicipios;
    }
    
    public String getNombreEmpresa() {
        return nombreEmpresa;
    }
    
    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }
    
    public String getCodigoCliente() {
        return codigoCliente;
    }
    
    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }
    
    public int getIdTipoCliente() {
        return idTipoCliente;
    }
    
    public void setIdTipoCliente(int idTipoCliente) {
        this.idTipoCliente = idTipoCliente;
    }
    
}
