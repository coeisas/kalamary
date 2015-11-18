/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.informes;

import entities.CfgCliente;
import entities.CfgEmpresasede;
import utilities.InformeDetalladoCaja;
import entities.FacMovcajadetalle;
import entities.SegUsuario;
import facades.CfgClienteFacade;
import facades.FacMovcajadetalleFacade;
import facades.SegUsuarioFacade;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
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
import utilities.LazyClienteDataModel;

/**
 *
 * @author Mario
 */
@ManagedBean
@SessionScoped
public class InformeDetalladoCajaMB implements Serializable {

    private Date fechaInicial;
    private Date fechaFinal;
    private int idProtagonista;
    private String identificacionProtagonista;
    private String nombreProtagonista;
    private String labelProtagonista;

    private CfgCliente clienteSeleccionado;
    private SegUsuario usuarioSeleccionado;

    private List<SegUsuario> listaUsuario;
    private List<SelectItem> protagonistas;
    private LazyDataModel<CfgCliente> listaCliente;

    private CfgEmpresasede sedeActual;

    @EJB
    FacMovcajadetalleFacade movcajadetalleFacade;
    @EJB
    CfgClienteFacade clienteFacade;
    @EJB
    SegUsuarioFacade usuarioFacade;

    public InformeDetalladoCajaMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        listaUsuario = new ArrayList();
        sedeActual = sesionMB.getSedeActual();
        protagonistas = new ArrayList();
        SelectItem selectItem = new SelectItem(1, "USUARIO");
        protagonistas.add(selectItem);
        selectItem = new SelectItem(2, "CLIENTE");
        protagonistas.add(selectItem);
        idProtagonista = 1;
        actualizarLabelProtagonista();
    }

    public void actualizarLabelProtagonista() {
        if (idProtagonista == 1) {
            labelProtagonista = "Usuario:";
            clienteSeleccionado = null;
        } else {
            labelProtagonista = "Cliente:";
            setUsuarioSeleccionado(null);
        }
        RequestContext.getCurrentInstance().update("IdFormInformeDetalladoCaja:IdProtagonista");
        cargarInformacionProtagonista();
    }

    public void cargarProtagonistas() {
        if (sedeActual != null) {
            if (idProtagonista == 1) {
                listaUsuario = usuarioFacade.buscarPorEmpresaActivos(sedeActual.getCfgempresaidEmpresa());
                RequestContext.getCurrentInstance().update("FormBuscarUsuario");
                RequestContext.getCurrentInstance().execute("PF('dlgUsuario').show()");
            } else {
                cargarClientes();
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro informacion de la empresa"));
        }
    }

    public void buscarProtagonista() {
        if (sedeActual != null) {
            if (!identificacionProtagonista.trim().isEmpty()) {
                if (idProtagonista == 1) {
                    setUsuarioSeleccionado(usuarioFacade.buscarUsuarioByEmpresaAndDocumento(sedeActual.getCfgempresaidEmpresa(), identificacionProtagonista));
                } else {
                    clienteSeleccionado = clienteFacade.buscarPorIdentificacionAndIdEmpresa(identificacionProtagonista, sedeActual.getCfgempresaidEmpresa());
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha cargado la informacion de la empresa. Reinicie sesion"));
        }
        cargarInformacionProtagonista();

    }

    public void cargarInformacionProtagonista() {
        if (getUsuarioSeleccionado() != null) {
            nombreProtagonista = getUsuarioSeleccionado().nombreCompleto();
            identificacionProtagonista = getUsuarioSeleccionado().getNumDoc();
            RequestContext.getCurrentInstance().execute("PF('dlgUsuario').hide()");
        } else if (clienteSeleccionado != null) {
            nombreProtagonista = clienteSeleccionado.nombreCompleto();
            identificacionProtagonista = clienteSeleccionado.getNumDoc();
            RequestContext.getCurrentInstance().execute("PF('dlgCliente').hide()");
        } else {
            nombreProtagonista = null;
            identificacionProtagonista = null;
        }
        RequestContext.getCurrentInstance().update("IdFormInformeDetalladoCaja:IdNombreProtagonista");
        RequestContext.getCurrentInstance().update("IdFormInformeDetalladoCaja:IdIdentificacionProtagonista");
    }

    public void cargarClientes() {
        if (sedeActual != null) {
            listaCliente = new LazyClienteDataModel(clienteFacade, sedeActual.getCfgempresaidEmpresa());
            RequestContext.getCurrentInstance().update("FormBuscarCliente");
            RequestContext.getCurrentInstance().execute("PF('dlgCliente').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha cargado la sede de la sesion"));
        }
    }

    public void deseleccionarCliente() {
        setClienteSeleccionado(null);
        cargarInformacionProtagonista();
    }

    public void deseleccionarUsuario() {
        setUsuarioSeleccionado(null);
        cargarInformacionProtagonista();
    }

    public void generarReporte() throws IOException, JRException {
        Date aux = null;
        if (fechaFinal != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(fechaFinal);
            c.add(Calendar.DATE, 1);
            aux = c.getTime();
        }
        List<FacMovcajadetalle> movcajadetalles = movcajadetalleFacade.buscarMovDetalleParaInforme(sedeActual, fechaInicial, aux, clienteSeleccionado, usuarioSeleccionado);
        List<InformeDetalladoCaja> lista = generarLista(movcajadetalles);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("/informes/reportes/informeDetalladoCaja.jasper");
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

    private List<InformeDetalladoCaja> generarLista(List<FacMovcajadetalle> movcajadetalles) {
        List<InformeDetalladoCaja> lista = new ArrayList();
        for (FacMovcajadetalle fm : movcajadetalles) {
            InformeDetalladoCaja informeDetalladoCaja = new InformeDetalladoCaja();
            informeDetalladoCaja.setFechaRegistro(fm.getFecha());
            informeDetalladoCaja.setNumDocumento(fm.getFacDocumentosmaster().determinarNumFactura());
            CfgCliente cliente = fm.getFacDocumentosmaster().getCfgclienteidCliente();
            if (cliente != null) {
                informeDetalladoCaja.setProtagonista(cliente.nombreCompleto());
            } else {
                SegUsuario usuario = fm.getFacDocumentosmaster().getSegusuarioidUsuario1();
                informeDetalladoCaja.setProtagonista(usuario.nombreCompleto());
            }
            informeDetalladoCaja.setFormaPago(fm.getCfgFormapago().getAbreviatura());
            informeDetalladoCaja.setValor(fm.getValor());
            lista.add(informeDetalladoCaja);
        }
        return lista;
    }

    public void limpiar() {
        idProtagonista = 1;
        fechaInicial = null;
        fechaFinal = null;
        clienteSeleccionado = null;
        usuarioSeleccionado = null;
        actualizarLabelProtagonista();
        cargarInformacionProtagonista();
        RequestContext.getCurrentInstance().update("IdFormInformeDetalladoCaja");
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

    public List<SegUsuario> getListaUsuario() {
        return listaUsuario;
    }

    public void setListaUsuario(List<SegUsuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }

    public String getIdentificacionProtagonista() {
        return identificacionProtagonista;
    }

    public void setIdentificacionProtagonista(String identificacionProtagonista) {
        this.identificacionProtagonista = identificacionProtagonista;
    }

    public String getNombreProtagonista() {
        return nombreProtagonista;
    }

    public LazyDataModel<CfgCliente> getListaCliente() {
        return listaCliente;
    }

    public CfgCliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(CfgCliente clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
    }

    public int getIdProtagonista() {
        return idProtagonista;
    }

    public void setIdProtagonista(int idProtagonista) {
        this.idProtagonista = idProtagonista;
    }

    public List<SelectItem> getProtagonistas() {
        return protagonistas;
    }

    public String getLabelProtagonista() {
        return labelProtagonista;
    }

    public SegUsuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(SegUsuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

}
