/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.configuracion;

import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.CfgRol;
import entities.FacCaja;
import entities.SegUsuario;
import facades.CfgEmpresaFacade;
import facades.CfgEmpresasedeFacade;
import facades.CfgRolFacade;
import facades.CfgTipoidentificacionFacade;
import facades.FacCajaFacade;
import facades.SegUsuarioFacade;
import java.io.ByteArrayInputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.event.PhaseId;
import managedBeans.seguridad.SesionMB;
import org.primefaces.model.DefaultStreamedContent;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class UsuarioMB implements Serializable {

    private String codEmpresa;
    private String nomEmpresa;
    private String codSede;
    private String nomSede;
    private int idIdentificacion;
    private String numIdentificacion;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private UploadedFile file;
    private float comision;
    private String direccion;
    private String telefono;
    private String mail;
    private Date fechaNacimiento;
    private String usuario;
    private String contrasenia;
    private String contraseniaR;
    private int idRole;
    private int idCaja;
    private boolean usuarioActivo;

    private List<CfgEmpresasede> listaSedes;
    private List<SegUsuario> listaUsuarios;
    private List<FacCaja> listaCajas;

    private String opcion;
    private boolean nombreUsuarioValidado;
    private boolean contraseniaValidada;
    private String background;
    private StreamedContent image;
    private CfgEmpresa empresaSeleccionada;
    private CfgEmpresasede sedeSeleccionada;
    private SegUsuario usuarioSeleccionado;
    private String displayActivoControl = "none";

    @EJB
    CfgEmpresaFacade empresaFacade;

    @EJB
    CfgEmpresasedeFacade sedeFacade;

    @EJB
    SegUsuarioFacade usuarioFacade;

    @EJB
    CfgRolFacade rolFacade;

    @EJB
    CfgTipoidentificacionFacade tipoidentificacionFacade;

    @EJB
    FacCajaFacade cajaFacade;

    @PostConstruct
    private void init() {
        listaSedes = new ArrayList();
        listaCajas = new ArrayList();
        opcion = "creacion";
        background = "#e0e0e0";

    }

    public UsuarioMB() {
    }

    public void buscarEmpresa() {
        empresaSeleccionada = null;
        setSedeSeleccionada(null);
        if (!codEmpresa.trim().isEmpty()) {
            setEmpresaSeleccionada(empresaFacade.buscarEmpresaPorCodigo(codEmpresa));
            if (getEmpresaSeleccionada() != null) {
                setNomSede(null);
                setCodSede(null);
                if (usuarioSeleccionado == null) {
                    limpiarFormulario();
                }
                actualizarEmpresa();

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro empresa con el codigo " + codEmpresa));
            }
        }
        if (getEmpresaSeleccionada() == null) {
            getListaSedes().clear();
            setNomSede(null);
            setCodSede(null);
            setNomSede(null);
            setNomEmpresa(null);
            if (usuarioSeleccionado == null) {
                limpiarFormulario();
            }
        }
        RequestContext.getCurrentInstance().update("IdFormUsuario");

    }

    public void buscarSede() {
        if (codSede != null && getEmpresaSeleccionada() != null) {
            setSedeSeleccionada(sedeFacade.buscarSedePorEmpresa(getEmpresaSeleccionada(), codSede.trim()));
            if (getSedeSeleccionada() != null) {
                cargarInformacionSede();
            } else {
                setNomSede(null);
                if (usuarioSeleccionado == null) {
                    limpiarFormulario();
                }
                if (!codSede.trim().isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro sede con el codigo " + codSede));
                }
            }
        } else {
            setSedeSeleccionada(null);
            setNomSede(null);
            if (usuarioSeleccionado == null) {
                limpiarFormulario();
            }
        }
        RequestContext.getCurrentInstance().update("IdFormUsuario");
    }

    public void limpiarFormulario() {
        setNumIdentificacion(null);
        setFile(null);
        setIdIdentificacion(0);
        setUsuario(null);
        setPrimerNombre(null);
        setSegundoNombre(null);
        setPrimerApellido(null);
        setSegundoApellido(null);
        setContrasenia(null);
        setContraseniaR(null);
        setFechaNacimiento(null);
        setComision(0);
        setIdCaja(0);
        setIdRole(0);
        setDireccion(null);
        setTelefono(null);
        setMail(null);
        setImage(null);
        setBackground("#e0e0e0");
    }

    public void actualizarEmpresa() {
        if (getEmpresaSeleccionada() != null) {
            setCodEmpresa(getEmpresaSeleccionada().getCodEmpresa());
            setNomEmpresa(getEmpresaSeleccionada().getNomEmpresa());
            listaSedes = sedeFacade.buscarSedesPorEmpresa(getEmpresaSeleccionada().getIdEmpresa());
            buscarSede();
        } else {
            listaSedes.clear();
            if (usuarioSeleccionado == null) {
                limpiarFormulario();
            }
        }
        RequestContext.getCurrentInstance().update("FormModalSede");
        RequestContext.getCurrentInstance().update("IdFormUsuario");
    }

    public void cargarInformacionSede() {
        if (getSedeSeleccionada() != null) {
            setCodSede(sedeSeleccionada.getCodSede());
            setNomSede(getSedeSeleccionada().getNomSede());
            listaCajas = cajaFacade.buscarCajasPorSede(sedeSeleccionada);
        } else {
            setNomSede(null);
        }
        RequestContext.getCurrentInstance().update("IdFormUsuario");
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        if (file != null) {
//            setNombreArchivo(getFile().getFileName());
//            RequestContext.getCurrentInstance().update("IdFormSede:nombreImagen");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Foto cargada"));
            RequestContext.getCurrentInstance().execute("PF('dlgFoto').hide()");
        }
    }

    public void validarUsuario() {
        nombreUsuarioValidado = false;
        if (empresaSeleccionada != null) {
            if (!usuario.trim().isEmpty()) {
                SegUsuario user = usuarioFacade.buscarPorEmpresaAndNomUsuario(empresaSeleccionada.getIdEmpresa(), usuario);
                if (user == null) {//si user es null significa que el nombre de usuario esta disponible
                    nombreUsuarioValidado = true;
                } else {
                    if (opcion.equals("creacion")) {//validacion cuando se esta creando un nuevo usuario
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Nombre de usuario no disponible"));
                    } else {//validacion cuando se esta creando un nuevo usuario
                        if (user.equals(usuarioSeleccionado)) {
                            nombreUsuarioValidado = true;
                        }
                    }
                }
            }

        } else {
            CfgRol rol = rolFacade.find(idRole);
            if (rol.getCodrol().equals("00001")) {//se esta validando un usuario con rol super
                SegUsuario user = usuarioFacade.buscarUsuarioSuper(usuario);
                if (user == null) {//si user es null significa que el nombre de usuario esta disponible
                    nombreUsuarioValidado = true;
                } else {
                    if (opcion.equals("creacion")) {//validacion cuando se esta creando un nuevo usuario
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Nombre de usuario no disponible"));
                    } else {//validacion cuando se esta creando un nuevo usuario
                        if (user.equals(usuarioSeleccionado)) {
                            nombreUsuarioValidado = true;
                        }
                    }
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Especifique la empresa"));
            }
        }
    }

    public void validarContrasenia() {
        if (!contraseniaR.trim().isEmpty() && !contrasenia.trim().isEmpty()) {
            if (contraseniaR.equals(contrasenia)) {
                contraseniaValidada = true;
                background = "#bbeebb";
            } else {
                contraseniaValidada = false;
                background = "#FFAAAA";
            }
        } else {
            contraseniaValidada = false;
            background = "#e0e0e0";
        }
    }

    public void cargarUsuarios() {
        if (empresaSeleccionada != null) {
            listaUsuarios = usuarioFacade.buscarPorEmpresa(empresaSeleccionada);
            RequestContext.getCurrentInstance().update("FormBuscarUsuario");
            RequestContext.getCurrentInstance().execute("PF('dlgUsuario').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Determine la empresa"));
        }
    }

    public void cargarInformacionUsuario() {
        if (usuarioSeleccionado != null) {
            opcion = "modificacion";
            displayActivoControl = "block";
            if (usuarioSeleccionado.getCfgempresasedeidSede() != null) {
                setEmpresaSeleccionada(usuarioSeleccionado.getCfgempresasedeidSede().getCfgempresaidEmpresa());
                setSedeSeleccionada(usuarioSeleccionado.getCfgempresasedeidSede());
                setCodEmpresa(usuarioSeleccionado.getCfgempresasedeidSede().getCfgempresaidEmpresa().getCodEmpresa());
                setNomEmpresa(usuarioSeleccionado.getCfgempresasedeidSede().getCfgempresaidEmpresa().getNomEmpresa());
                setCodSede(usuarioSeleccionado.getCfgempresasedeidSede().getCodSede());
                setNomSede(usuarioSeleccionado.getCfgempresasedeidSede().getNomSede());
                listaCajas = cajaFacade.buscarCajasPorSede(sedeSeleccionada);
            } else {
                setEmpresaSeleccionada(null);
                setSedeSeleccionada(null);
                setCodEmpresa(null);
                listaCajas.clear();
                setNomEmpresa(null);
                setCodSede(null);
                setNomSede(null);
            }
            setIdIdentificacion(usuarioSeleccionado.getCfgTipoidentificacionId().getId());
            setNumIdentificacion(usuarioSeleccionado.getNumDoc());
            setPrimerNombre(usuarioSeleccionado.getNom1Usuario());
            setSegundoNombre(usuarioSeleccionado.getNom2Usuario());
            setPrimerApellido(usuarioSeleccionado.getApellido1());
            setSegundoApellido(usuarioSeleccionado.getApellido2());
            setDireccion(usuarioSeleccionado.getDirUsuario());
            setTelefono(usuarioSeleccionado.getTel1());
            setComision(determinarValor(usuarioSeleccionado.getComisionVenta()));
            setMail(usuarioSeleccionado.getMail());
            setFechaNacimiento(usuarioSeleccionado.getFecNacimiento());
            setUsuario(usuarioSeleccionado.getUsuario());
            if (usuarioSeleccionado.getFaccajaidCaja() != null) {
                setIdCaja(usuarioSeleccionado.getFaccajaidCaja().getIdCaja());
            }
            if (usuarioSeleccionado.getFoto() != null) {
                setImage(new DefaultStreamedContent(new ByteArrayInputStream(usuarioSeleccionado.getFoto())));
            } else {
                setImage(null);
            }
            setIdRole(usuarioSeleccionado.getCfgRolIdrol().getIdrol());
            setContrasenia(usuarioSeleccionado.getPassword());
            setContraseniaR(usuarioSeleccionado.getPassword());
            setUsuarioActivo(usuarioSeleccionado.getActivo());
        } else {
            displayActivoControl = "none";
            opcion = "creacion";
            limpiarFormulario();
        }
        RequestContext.getCurrentInstance().execute("PF('dlgUsuario').hide()");
        RequestContext.getCurrentInstance().update("IdFormUsuario");
    }

    private Float determinarValor(Float valor) {
        return valor == null ? 0 : valor;
    }

    private Integer determinarValor(Integer valor) {
        return valor == null ? 0 : valor;
    }

    public void cancelar() {
        limpiarFormulario();
        setNomEmpresa(null);
        setCodEmpresa(null);
        setNomSede(null);
        setCodSede(null);
        setUsuarioSeleccionado(null);
        setEmpresaSeleccionada(null);
        setSedeSeleccionada(null);
        displayActivoControl = "none";
        background = "#e0e0e0";
        RequestContext.getCurrentInstance().update("IdFormUsuario");
    }

    public void accion() {
        switch (opcion) {
            case "modificacion":
                modificarUsuario();
                break;
            case "creacion":
                crearUsuario();
                break;
        }
    }

    private void crearUsuario() {
        if (!validarCamposFormulario()) {
            return;
        }
        try {
            SegUsuario user = new SegUsuario();
            user.setActivo(true);
            user.setApellido1(primerApellido.trim().toUpperCase());
            user.setApellido2(segundoApellido.trim().toUpperCase());
            CfgRol rol = rolFacade.find(idRole);
            user.setCfgRolIdrol(rol);
            user.setCfgTipoidentificacionId(tipoidentificacionFacade.find(idIdentificacion));
            user.setCfgempresasedeidSede(sedeSeleccionada);
            user.setComisionVenta(comision);
            user.setDirUsuario(direccion.trim().toUpperCase());
            user.setFecCrea(new Date());
            if (file != null) {
                user.setFoto(file.getContents());
            }
            if (idCaja != 0) {
                FacCaja caja = cajaFacade.find(idCaja);
                user.setFaccajaidCaja(caja);
            }
            user.setMail(mail.trim().toUpperCase());
            user.setNom1Usuario(primerNombre.trim().toUpperCase());
            user.setNom2Usuario(segundoNombre.trim().toUpperCase());
            user.setNumDoc(numIdentificacion.trim());
            user.setPassword(contrasenia.trim());
            user.setTel1(telefono.trim());
            user.setUsuario(usuario.trim().toLowerCase());
            user.setFecNacimiento(fechaNacimiento);
            FacesContext context = FacesContext.getCurrentInstance();
            SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
            user.setUsrCrea(sesionMB.getUsuarioActual().getIdUsuario());
            usuarioFacade.create(user);
            limpiarFormulario();
            setNomEmpresa(null);
            setCodEmpresa(null);
            setNomSede(null);
            setCodSede(null);
            setEmpresaSeleccionada(null);
            setSedeSeleccionada(null);
            displayActivoControl = "none";
            RequestContext.getCurrentInstance().update("IdFormUsuario");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Usuario creado"));
        } catch (NumberFormatException | ELException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario no creado"));
        }
    }

    private void modificarUsuario() {
        if (!validarCamposFormulario()) {
            return;
        }
        try {
            usuarioSeleccionado.setActivo(usuarioActivo);
            usuarioSeleccionado.setApellido1(primerApellido.trim().toUpperCase());
            usuarioSeleccionado.setApellido2(segundoApellido.trim().toUpperCase());
            CfgRol rol = rolFacade.find(idRole);
            usuarioSeleccionado.setCfgRolIdrol(rol);
            usuarioSeleccionado.setCfgTipoidentificacionId(tipoidentificacionFacade.find(idIdentificacion));
            usuarioSeleccionado.setCfgempresasedeidSede(sedeSeleccionada);
            usuarioSeleccionado.setComisionVenta(comision);
            usuarioSeleccionado.setDirUsuario(direccion.trim().toUpperCase());
            if (file != null) {
                usuarioSeleccionado.setFoto(file.getContents());
            }
            if (idCaja != 0) {
                FacCaja caja = cajaFacade.find(idCaja);
                usuarioSeleccionado.setFaccajaidCaja(caja);
            } else {
                usuarioSeleccionado.setFaccajaidCaja(null);
            }
            usuarioSeleccionado.setMail(mail.trim().toUpperCase());
            usuarioSeleccionado.setNom1Usuario(primerNombre.trim().toUpperCase());
            usuarioSeleccionado.setNom2Usuario(segundoNombre.trim().toUpperCase());
            usuarioSeleccionado.setNumDoc(numIdentificacion.trim());
            usuarioSeleccionado.setFecNacimiento(fechaNacimiento);
            usuarioSeleccionado.setPassword(contrasenia.trim());
            usuarioSeleccionado.setTel1(telefono.trim());
            usuarioSeleccionado.setUsuario(usuario.trim().toLowerCase());
            usuarioFacade.edit(usuarioSeleccionado);
            limpiarFormulario();
            setNomEmpresa(null);
            setCodEmpresa(null);
            setNomSede(null);
            setCodSede(null);
            setUsuarioSeleccionado(null);
            setEmpresaSeleccionada(null);
            setSedeSeleccionada(null);
            opcion = "creacion";
            RequestContext.getCurrentInstance().update("FormBuscarUsuario");
            RequestContext.getCurrentInstance().update("IdFormUsuario");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Usuario modificado"));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario no modificado"));
        }
    }

    private boolean validarCamposFormulario() {
        if (idRole == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Asignacion de rol necesaria"));
            return false;
        }
        CfgRol rol = rolFacade.find(idRole);
        if (rol == null) {//un usuario debe obligatoriamente tener asignado un rol
            return false;
        }
        if (!rol.getCodrol().equals("00001")) {//el rol con codigo 00001 es el SUPER. Que es el unico rol que puede estar o no asignado a empresa - sede
            if (empresaSeleccionada == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Determine la empresa"));
                return false;
            }
            if (sedeSeleccionada == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Especifique la sede"));
                return false;
            }
        }
        if (idIdentificacion == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Determine el tipo de identificacion"));
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

        if (usuario.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Especifique nombre usuario"));
            return false;
        }
        validarUsuario();
        if (!nombreUsuarioValidado) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario no validado"));
            return false;
        }
        if (contrasenia.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta la contraseña"));
            return false;
        }
        if (contraseniaR.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Repita la contraseña"));
            return false;
        }
        validarContrasenia();
        if (!contraseniaValidada) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Las contraseñas no coinciden"));
            return false;
        }
        return true;
    }

    public String getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(String codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public String getNomEmpresa() {
        return nomEmpresa;
    }

    public void setNomEmpresa(String nomEmpresa) {
        this.nomEmpresa = nomEmpresa;
    }

    public String getCodSede() {
        return codSede;
    }

    public void setCodSede(String codSede) {
        this.codSede = codSede;
    }

    public String getNomSede() {
        return nomSede;
    }

    public void setNomSede(String nomSede) {
        this.nomSede = nomSede;
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

    public float getComision() {
        return comision;
    }

    public void setComision(float comision) {
        this.comision = comision;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getContraseniaR() {
        return contraseniaR;
    }

    public void setContraseniaR(String contraseniaR) {
        this.contraseniaR = contraseniaR;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    public StreamedContent getImage() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {//fase de jsf
            image = new DefaultStreamedContent();
            return image;
        } else {
            if (usuarioSeleccionado != null) {
                String imageId = context.getExternalContext().getRequestParameterMap().get("id");
                SegUsuario user = usuarioFacade.find(Integer.parseInt(imageId));
                image = new DefaultStreamedContent(new ByteArrayInputStream(user.getFoto()));
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

    public List<SegUsuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<SegUsuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public SegUsuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(SegUsuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public boolean isUsuarioActivo() {
        return usuarioActivo;
    }

    public void setUsuarioActivo(boolean usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
    }

    public String getDisplayActivoControl() {
        return displayActivoControl;
    }

    public void setDisplayActivoControl(String displayActivoControl) {
        this.displayActivoControl = displayActivoControl;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String bakground) {
        this.background = bakground;
    }

    public List<FacCaja> getListaCajas() {
        return listaCajas;
    }

    public void setListaCajas(List<FacCaja> listaCajas) {
        this.listaCajas = listaCajas;
    }

}
