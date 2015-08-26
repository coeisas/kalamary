/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.configuracion;

import entities.CfgEmpresa;
import entities.CfgMunicipio;
import entities.CfgMunicipioPK;
import entities.CfgProveedor;
import entities.CfgTipoidentificacion;
import entities.CfgformaPagoproveedor;
import entities.SegUsuario;
import facades.CfgMunicipioFacade;
import facades.CfgProveedorFacade;
import facades.CfgTipoidentificacionFacade;
import facades.CfgformaPagoproveedorFacade;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import managedBeans.seguridad.SesionMB;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import utilities.LazyProveedorDataModel;
import java.util.Date;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class ProveedorMB implements Serializable {

    private String idDepartamento;
    private String idMunicipio;
    private int idIdentificacion;
    private String numIdentificacion;
    private String proveedor;
    private String contacto;
    private int formaPago;
    private String direccion;
    private String telefono1;
    private String telefono2;
    private String website;
    private String mail1;
    private String mail2;
    private float cupoCredito;

    private CfgEmpresa empresaSeleccionada;
    private SegUsuario usuarioActual;
    private CfgProveedor proveedorSeleccionado;

    private List<CfgMunicipio> listaMunicipios;
    private LazyDataModel<CfgProveedor> listaProveedor;
    private List<CfgformaPagoproveedor> listaFormaPago;

    @EJB
    CfgMunicipioFacade municipioFacade;
    @EJB
    CfgProveedorFacade proveedorFacade;
    @EJB
    CfgformaPagoproveedorFacade pagoproveedorFacade;
    @EJB
    CfgTipoidentificacionFacade tipoidentificacionFacade;

    public ProveedorMB() {
    }

    @PostConstruct
    public void init() {
        listaMunicipios = new ArrayList();
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        empresaSeleccionada = sesionMB.getEmpresaActual();
        usuarioActual = sesionMB.getUsuarioActual();
        listaFormaPago = new ArrayList();
        if (empresaSeleccionada != null) {
            listaFormaPago = pagoproveedorFacade.buscarFormasPagoByEmpresa(empresaSeleccionada);
        }
    }

    public void actualizarMunicipios() {
        getListaMunicipios().clear();
        if (idDepartamento != null) {
            setListaMunicipios(municipioFacade.buscarPorDepartamento(idDepartamento));
        }
    }

    public void cargarProveedores() {
        if (empresaSeleccionada != null) {
            listaProveedor = new LazyProveedorDataModel(proveedorFacade, empresaSeleccionada);
            RequestContext.getCurrentInstance().update("FormBuscarProveedor");
            RequestContext.getCurrentInstance().execute("PF('dlgProveedor').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Determine la empresa"));
        }
    }

    public void buscarProveedor() {
        if (empresaSeleccionada != null) {
            if (numIdentificacion != null && !numIdentificacion.trim().isEmpty()) {
                setProveedorSeleccionado(proveedorFacade.buscarPorIdentificacionAndIdEmpresa(numIdentificacion, empresaSeleccionada));
                cargarInformacionProveedor();
            }
        } else {
            setProveedorSeleccionado(null);
            cargarInformacionProveedor();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Determine la empresa"));
        }
    }

    public void cargarInformacionProveedor() {
        if (proveedorSeleccionado != null) {
            setIdDepartamento(proveedorSeleccionado.getCfgMunicipio().getCfgDepartamento().getIdDepartamento());
            setListaMunicipios(municipioFacade.buscarPorDepartamento(idDepartamento));
            setIdMunicipio(proveedorSeleccionado.getCfgMunicipio().getCfgMunicipioPK().getIdMunicipio());
            setIdIdentificacion(proveedorSeleccionado.getCfgTipoidentificacionId().getId());
            setNumIdentificacion(proveedorSeleccionado.getNumDoc());
            setProveedor(proveedorSeleccionado.getNomProveedor());
            setContacto(proveedorSeleccionado.getContactoProveedor());
            formaPago = proveedorSeleccionado.getCfgformaPagoproveedoridFormaPago().getIdFormaPago();
            setDireccion(proveedorSeleccionado.getDirProveedor());
            setTelefono1(proveedorSeleccionado.getTel1Proveedor());
            setTelefono2(proveedorSeleccionado.getTel2Proveedor());
            setWebsite(proveedorSeleccionado.getWebsiteProveedor());
            setMail1(proveedorSeleccionado.getMailProveedor());
            setMail2(proveedorSeleccionado.getMail2Proveedor());
            if (proveedorSeleccionado.getCupoCredito() != null) {
                setCupoCredito(proveedorSeleccionado.getCupoCredito());
            } else {
                setCupoCredito(0);
            }
        } else {
            listaMunicipios.clear();
            limpiarFormulario();
        }
        RequestContext.getCurrentInstance().execute("PF('dlgProveedor').hide()");
        RequestContext.getCurrentInstance().update("IdFormProveedor");
    }

    private void limpiarFormulario() {
        setIdDepartamento(null);
        listaMunicipios.clear();
        setIdMunicipio(null);
        setIdIdentificacion(0);
        setProveedor(null);
        setContacto(null);
        formaPago = 0;
        setDireccion(null);
        setTelefono1(null);
        setTelefono2(null);
        setWebsite(null);
        setMail1(null);
        setMail2(null);
        setCupoCredito(0);
    }

    private boolean validar() {
        boolean ban = true;
        if (empresaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro informacion de la empresa"));
            return false;
        }
        if (usuarioActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay usuario"));
            return false;
        }
        if (!usuarioActual.getCfgRolIdrol().getCodrol().equals("00001") && !usuarioActual.getCfgRolIdrol().getCodrol().equals("00002")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No tiene permisos para efectuar esta accion"));
            return false;
        }
        if (numIdentificacion.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El numero de identificacion es necesario"));
            return false;
        }
        if (idIdentificacion == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Especifique el tipo de identificacion"));
            return false;
        }
        if (proveedor.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta el proveedor"));
            return false;
        }
        if (formaPago == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Elija una forma de pago"));
            return false;
        }
        if (direccion.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Direccion obligatoria"));
            return false;
        }
        if (telefono1.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Telefono 1 necesario"));
            return false;
        }
        if (mail1.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Mail 1 obligatorio"));
            return false;
        }
        return ban;
    }

    public void accion() {
        if (proveedorSeleccionado != null) {
            modificarProveedor();
        } else {
            crearProveedor();
        }
    }

    private void crearProveedor() {
        if (!validar()) {
            return;
        }
        try {
            CfgProveedor cfgProveedor = new CfgProveedor();
            cfgProveedor.setCfgempresaidEmpresa(empresaSeleccionada);
            CfgMunicipio municipio = municipioFacade.buscarPorMunicipioPK(new CfgMunicipioPK(idMunicipio, idDepartamento));
            cfgProveedor.setCfgMunicipio(municipio);
            cfgProveedor.setNumDoc(numIdentificacion);
            CfgTipoidentificacion tipoidentificacion = tipoidentificacionFacade.find(idIdentificacion);
            cfgProveedor.setCfgTipoidentificacionId(tipoidentificacion);
            CfgformaPagoproveedor pagoproveedor = pagoproveedorFacade.find(formaPago);
            cfgProveedor.setNomProveedor(proveedor.toUpperCase());
            cfgProveedor.setContactoProveedor(contacto.toUpperCase());
            cfgProveedor.setCfgformaPagoproveedoridFormaPago(pagoproveedor);
            cfgProveedor.setDirProveedor(direccion.toUpperCase());
            cfgProveedor.setTel1Proveedor(telefono1);
            cfgProveedor.setTel2Proveedor(telefono2);
            cfgProveedor.setWebsiteProveedor(website.toUpperCase());
            cfgProveedor.setMailProveedor(mail1.toUpperCase());
            cfgProveedor.setMail2Proveedor(mail2.toUpperCase());
            cfgProveedor.setCupoCredito(cupoCredito);
            cfgProveedor.setFecCrea(new Date());
            cfgProveedor.setSegusuarioidUsuario(usuarioActual);
            proveedorFacade.create(cfgProveedor);
            cancelar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Proveedor creado"));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Proveedor no creado"));
        }
    }

    private void modificarProveedor() {
        if (!validar()) {
            return;
        }
        if (proveedorSeleccionado == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No ha especificado el proveedor a modificar"));
            return;
        }
        try {
            proveedorSeleccionado.setCfgempresaidEmpresa(empresaSeleccionada);
            CfgMunicipio municipio = municipioFacade.buscarPorMunicipioPK(new CfgMunicipioPK(idMunicipio, idDepartamento));
            proveedorSeleccionado.setCfgMunicipio(municipio);
            proveedorSeleccionado.setNumDoc(numIdentificacion);
            CfgTipoidentificacion tipoidentificacion = tipoidentificacionFacade.find(idIdentificacion);
            proveedorSeleccionado.setCfgTipoidentificacionId(tipoidentificacion);
            CfgformaPagoproveedor pagoproveedor = pagoproveedorFacade.find(formaPago);
            proveedorSeleccionado.setNomProveedor(proveedor.toUpperCase());
            proveedorSeleccionado.setContactoProveedor(contacto.toUpperCase());
            proveedorSeleccionado.setCfgformaPagoproveedoridFormaPago(pagoproveedor);
            proveedorSeleccionado.setDirProveedor(direccion.toUpperCase());
            proveedorSeleccionado.setTel1Proveedor(telefono1);
            proveedorSeleccionado.setTel2Proveedor(telefono2);
            proveedorSeleccionado.setWebsiteProveedor(website.toUpperCase());
            proveedorSeleccionado.setMailProveedor(mail1.toUpperCase());
            proveedorSeleccionado.setMail2Proveedor(mail2.toUpperCase());
            proveedorSeleccionado.setCupoCredito(cupoCredito);
            proveedorFacade.edit(proveedorSeleccionado);
            cancelar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Proveedor actualizado"));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Proveedor no actualizado"));
        }
    }

    public void cancelar() {
        setNumIdentificacion(null);
        limpiarFormulario();
        RequestContext.getCurrentInstance().update("IdFormProveedor");
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

    public CfgProveedor getProveedorSeleccionado() {
        return proveedorSeleccionado;
    }

    public void setProveedorSeleccionado(CfgProveedor proveedorSeleccionado) {
        this.proveedorSeleccionado = proveedorSeleccionado;
    }

    public LazyDataModel<CfgProveedor> getListaProveedor() {
        return listaProveedor;
    }

    public void setListaProveedor(LazyDataModel<CfgProveedor> listaProveedor) {
        this.listaProveedor = listaProveedor;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
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

    public String getMail1() {
        return mail1;
    }

    public void setMail1(String mail1) {
        this.mail1 = mail1;
    }

    public String getMail2() {
        return mail2;
    }

    public void setMail2(String mail2) {
        this.mail2 = mail2;
    }

    public float getCupoCredito() {
        return cupoCredito;
    }

    public void setCupoCredito(float cupoCredito) {
        this.cupoCredito = cupoCredito;
    }

    public List<CfgformaPagoproveedor> getListaFormaPago() {
        return listaFormaPago;
    }

    public int getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(int formaPago) {
        this.formaPago = formaPago;
    }

}
