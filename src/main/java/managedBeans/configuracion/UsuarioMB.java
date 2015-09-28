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

    private SesionMB sesionMB;
    private SegUsuario usuarioActual;

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
        FacesContext context = FacesContext.getCurrentInstance();
        sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
        empresaSeleccionada = sesionMB.getEmpresaActual();
        if (empresaSeleccionada != null) {
            listaSedes = sedeFacade.buscarSedesPorEmpresa(empresaSeleccionada.getIdEmpresa());
        } else {
            listaSedes = new ArrayList();
        }
        listaCajas = new ArrayList();

        opcion = "creacion";
        background = "#e0e0e0";

    }

    public UsuarioMB() {

    }

    public void buscarUsuario() {
        if (numIdentificacion != null && !numIdentificacion.trim().isEmpty()) {
            usuarioSeleccionado = usuarioFacade.buscarPorEmpresaAndDocumento(empresaSeleccionada, numIdentificacion);
        } else {
            setIdIdentificacion(0);
            setSedeSeleccionada(null);
            usuarioSeleccionado = null;
        }
        cargarInformacionUsuario();
    }

    public void limpiarFormulario() {
        setFile(null);
        setUsuario(null);
        setPrimerNombre(null);
        setSegundoNombre(null);
        setPrimerApellido(null);
        setSegundoApellido(null);
        setContrasenia(null);
        setContraseniaR(null);
        setUsuarioActivo(true);
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

    public void cargarInformacionSede() {
        if (getSedeSeleccionada() != null) {
            listaCajas = cajaFacade.buscarCajasPorSede(sedeSeleccionada);
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
        contraseniaValidada = false;
        background = "#e0e0e0";
        if (contraseniaR != null && contrasenia != null) {
            if (!contraseniaR.trim().isEmpty() && !contrasenia.trim().isEmpty()) {
                if (contraseniaR.equals(contrasenia)) {
                    contraseniaValidada = true;
                    background = "#bbeebb";
                } else {
                    contraseniaValidada = false;
                    background = "#FFAAAA";
                }
            }
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

    public void cargarSedes() {
        if (empresaSeleccionada != null) {
            listaSedes = sedeFacade.buscarSedesPorEmpresa(empresaSeleccionada.getIdEmpresa());
            RequestContext.getCurrentInstance().update("FormModalSede");
            RequestContext.getCurrentInstance().execute("PF('dlgSede').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No se ha cargado la empresa. Reinicie sesion"));
        }
    }

    public void cargarInformacionUsuario() {
        listaCajas.clear();
        if (usuarioSeleccionado != null) {
            opcion = "modificacion";
            displayActivoControl = "block";
            if (usuarioSeleccionado.getCfgempresasedeidSede() != null) {
                setSedeSeleccionada(usuarioSeleccionado.getCfgempresasedeidSede());
                setListaCajas(cajaFacade.buscarCajasPorSede(usuarioSeleccionado.getCfgempresasedeidSede()));
            } else {
                setSedeSeleccionada(null);
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
        setUsuarioSeleccionado(null);
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
        //        solo los usuarios super y admin pueden crear y modificar
        if (usuarioActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay usuario"));
            return;
        }
        //solo el super y el admin pueden crear usuarios
        if (!usuarioActual.getCfgRolIdrol().getCodrol().equals("00001") && !usuarioActual.getCfgRolIdrol().getCodrol().equals("00002")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No tiene permisos para efectuar esta accion"));
            return;
        }
        if (!validarCamposFormulario()) {
            return;
        }
        if (contrasenia.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta la contraseña"));
            return;
        }
        if (contraseniaR.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Repita la contraseña"));
            return;
        }
        validarContrasenia();
        if (!contraseniaValidada) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Las contraseñas no coinciden"));
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
            user.setCfgempresaidEmpresa(empresaSeleccionada);
            user.setUsrCrea(usuarioActual.getIdUsuario());
            usuarioFacade.create(user);
            limpiarFormulario();
            setIdIdentificacion(0);
            setNumIdentificacion(null);
            setSedeSeleccionada(null);
            displayActivoControl = "none";
            RequestContext.getCurrentInstance().update("IdFormUsuario");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Usuario creado"));
        } catch (NumberFormatException | ELException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario no creado"));
        }
    }

    private void modificarUsuario() {
        //solo el super y el admin pueden modificar usuarios sin restriccion
        if (!usuarioActual.getCfgRolIdrol().getCodrol().equals("00001") && !usuarioActual.getCfgRolIdrol().getCodrol().equals("00002")) {
//            if (!usuarioActual.equals(usuarioSeleccionado)) {//los otros usuarios unicamente puede modificar su perfil
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No pude modificar este usuario"));
//                return;
//            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No tiene permisos para crear o modificar usuarios"));
            return;

        }
        if (!validarCamposFormulario()) {
            return;
        }
        //        solo los usuarios super y admin pueden crear y modificar
        if (usuarioActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay usuario"));
            return;
        }
        //si la contraseña no esta vacia significa que se va a cambiar.
        if (!contrasenia.trim().isEmpty()) {
            //se debe repetir la nueva contraseña
            if (contraseniaR.trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Repita la contraseña"));
                return;
            }
            validarContrasenia();
            if (!contraseniaValidada) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Las contraseñas no coinciden"));
                return;
            }
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
            if (!contrasenia.isEmpty()) {
                usuarioSeleccionado.setPassword(contrasenia.trim());
            }
            usuarioSeleccionado.setTel1(telefono.trim());
            usuarioSeleccionado.setUsuario(usuario.trim().toLowerCase());
            usuarioFacade.edit(usuarioSeleccionado);
            limpiarFormulario();
            setUsuarioSeleccionado(null);
            setIdIdentificacion(0);
            setNumIdentificacion(null);
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
        if (rol.getCodrol().equals("00003") || rol.getCodrol().equals("00004")) {//cajero y vendedor necesitan tener especificado la empresa - sede
            if (empresaSeleccionada == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No ha Determinado la empresa a la que pertenecera el usuario"));
                return false;
            }
            if (sedeSeleccionada == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Especifique la sede"));
                return false;
            }
        }
        if (rol.getCodrol().equals("00002")) {//administradores necesitan estar asociados a una empresa
            if (empresaSeleccionada == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No ha Determinado la empresa a la que pertenecera el usuario"));
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
//        if (contrasenia.trim().isEmpty()) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta la contraseña"));
//            return false;
//        }
//        if (contraseniaR.trim().isEmpty()) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Repita la contraseña"));
//            return false;
//        }
//        validarContrasenia();
//        if (!contraseniaValidada) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Las contraseñas no coinciden"));
//            return false;
//        }
        return true;
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
