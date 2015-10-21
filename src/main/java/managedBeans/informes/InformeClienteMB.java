/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.informes;

import entities.CfgCliente;
import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.SegUsuario;
import facades.CfgClienteFacade;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import managedBeans.seguridad.SesionMB;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import utilities.InformeCliente;
import utilities.LazyClienteDataModel;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class InformeClienteMB implements Serializable {

    private String identificacion;
    private String nombreCliente;
    private CfgCliente clienteSeleccionado;
    private Date fechaInicial;
    private Date fechaFinal;

    private LazyDataModel<CfgCliente> listaClientes;

    private CfgEmpresa empresaActual;
    private CfgEmpresasede sedeActual;
    private SegUsuario usuarioActual;

    @EJB
    CfgClienteFacade clienteFacade;

    public InformeClienteMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        empresaActual = sesionMB.getEmpresaActual();
        sedeActual = sesionMB.getSedeActual();
        usuarioActual = sesionMB.getUsuarioActual();
    }

    public void cargarClientes() {
        if (empresaActual != null) {
            listaClientes = new LazyClienteDataModel(clienteFacade, empresaActual);
            RequestContext.getCurrentInstance().update("FormBuscarCliente");
            RequestContext.getCurrentInstance().execute("PF('dlgCliente').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Determine la empresa"));
        }
    }

    public void buscarCliente() {
        if (empresaActual != null) {
            if (identificacion != null && !identificacion.trim().isEmpty()) {
                clienteSeleccionado = clienteFacade.buscarPorIdentificacionAndIdEmpresa(identificacion, empresaActual);
                cargarInformacionCliente();
            }
        } else {
            clienteSeleccionado = null;
            cargarInformacionCliente();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Determine la empresa"));
        }
    }

    public void cargarInformacionCliente() {
        if (getClienteSeleccionado() != null) {
            identificacion = clienteSeleccionado.getNumDoc();
            nombreCliente = clienteSeleccionado.nombreCompleto();
        } else {
            nombreCliente = null;
        }
        RequestContext.getCurrentInstance().execute("PF('dlgCliente').hide()");
        RequestContext.getCurrentInstance().update("IdFormInformeCliente:IdCliente");
        RequestContext.getCurrentInstance().update("IdFormInformeCliente:IdNombreCliente");
    }

    private boolean validar() {
        boolean ban = true;
        if (sedeActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro informacion de la sede"));
            return false;
        }
        if (usuarioActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro informacion del usuario"));
            return false;
        }
        if (fechaFinal != null && fechaInicial != null) {
            if (fechaFinal.before(fechaInicial)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Verifique el orden de las fechas"));
                return false;
            }
        }
        return ban;
    }

    public void generarReporte() throws IOException, JRException {
        if (!validar()) {
            return;
        }
        List<CfgCliente> listadoCliente;
        if (clienteSeleccionado != null) {
            listadoCliente = new ArrayList();
            listadoCliente.add(clienteSeleccionado);
        } else {
            listadoCliente = clienteFacade.busquedaParaInforme(empresaActual, fechaInicial, fechaFinal);
        }
        List<InformeCliente> lista = generarLista(listadoCliente);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("/informes/reportes/informeCliente.jasper");
            Map<String, Object> parametros = new HashMap<>();
            if (sedeActual.getLogo() != null) {
                InputStream logo = new ByteArrayInputStream(sedeActual.getLogo());
                parametros.put("logo", logo);
            }
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    private List<InformeCliente> generarLista(List<CfgCliente> listadoCliente) {
        List<InformeCliente> lista = new ArrayList();
        for (CfgCliente cliente : listadoCliente) {
            InformeCliente informeCliente = new InformeCliente();
            informeCliente.setDocumento(cliente.getCfgTipoidentificacionId().getAbreviatura() + " " + cliente.getNumDoc());
            informeCliente.setNombre(cliente.nombreCompleto());
            informeCliente.setDireccion(cliente.getDirCliente());
            if (cliente.getCfgpaiscodPais() != null) {
                if (cliente.getCfgpaiscodPais().getCodPais().equals("343")) {
                    informeCliente.setUbicacion(cliente.getCfgMunicipio().getNomMunicipio() + " " + cliente.getCfgMunicipio().getCfgDepartamento().getNomDepartamento());
                } else {
                    informeCliente.setUbicacion(cliente.getCfgpaiscodPais().getNomPais());
                }
            }
            informeCliente.setTelefono(cliente.getTel1());
            informeCliente.setCorreo(cliente.getMail());
            informeCliente.setFechaNacimiento(cliente.getFecNacimiento());
            lista.add(informeCliente);
        }
        return lista;
    }

    public void limpiar() {
        clienteSeleccionado = null;
        nombreCliente = null;
        identificacion = null;
        fechaInicial = null;
        fechaFinal = null;
        RequestContext.getCurrentInstance().update("IdFormInformeCliente");
    }

    public CfgCliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(CfgCliente clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public LazyDataModel<CfgCliente> getListaClientes() {
        return listaClientes;
    }

}
