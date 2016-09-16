/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.informes;

import entities.CfgCliente;
import entities.CfgDocumento;
import entities.CfgEmpresasede;
import entities.FacCarteraCliente;
import entities.FacCarteraDetalle;
import entities.FacDocumentodetalle;
import entities.FacDocumentosmaster;
import facades.CfgClienteFacade;
import facades.CfgDocumentoFacade;
import facades.FacCarteraClienteFacade;
import facades.FacCarteraDetalleFacade;
import facades.FacDocumentodetalleFacade;
import facades.FacDocumentosmasterFacade;
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
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import utilities.FacturaDetalleReporte;
import utilities.InformeAbonoDetalle;
import utilities.InformeSeparado;
import utilities.LazyClienteDataModel;

/**
 *
 * @author Mario
 */
@ManagedBean
@SessionScoped
public class InformeSeparadoMB implements Serializable {

    private Date fechaInicial;
    private Date fechaFinal;
    private String nombreCliente;
    private String identificacionCliente;
    private String numSeparado;

    private CfgCliente clienteSeleccionado;

    private LazyDataModel<CfgCliente> listaClientes;

    private CfgEmpresasede sedeActual;

    @EJB
    CfgDocumentoFacade documentoFacade;
    @EJB
    CfgClienteFacade clienteFacade;
    @EJB
    FacDocumentosmasterFacade separadoFacade;
    @EJB
    FacDocumentodetalleFacade detalleSeparadoFacade;
    @EJB
    FacCarteraClienteFacade carteraFacade;
    @EJB
    FacCarteraDetalleFacade carteraDetalleFacade;

    public InformeSeparadoMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        sedeActual = sesionMB.getSedeActual();
    }

    public void buscarCliente() {
        if (sedeActual != null) {
            if (identificacionCliente != null && !identificacionCliente.isEmpty()) {
                clienteSeleccionado = clienteFacade.buscarPorIdentificacionAndIdEmpresa(identificacionCliente, sedeActual.getCfgempresaidEmpresa());
                if (clienteSeleccionado != null) {
                    cargarInformacionCliente();
                } else {
                    nombreCliente = null;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro cliente con esa identificacion"));
                }
            } else {
                clienteSeleccionado = null;
                cargarInformacionCliente();
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha cargado la sede de la sesion"));
        }

    }

    public void cargarClientes() {
        if (sedeActual != null) {
            listaClientes = new LazyClienteDataModel(clienteFacade, sedeActual.getCfgempresaidEmpresa());
            RequestContext.getCurrentInstance().update("FormBuscarCliente");
            RequestContext.getCurrentInstance().execute("PF('dlgCliente').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha cargado la sede de la sesion"));
        }
    }

    public void cargarInformacionCliente() {
        if (clienteSeleccionado != null) {
            identificacionCliente = clienteSeleccionado.getNumDoc();
            nombreCliente = clienteSeleccionado.nombreCompleto();
        } else {
            identificacionCliente = null;
            nombreCliente = null;
        }
        RequestContext.getCurrentInstance().execute("PF('dlgCliente').hide()");
        RequestContext.getCurrentInstance().update("IdFormInformeSeparado");
    }

    public void deseleccionarCliente() {
        clienteSeleccionado = null;
        cargarInformacionCliente();
    }

    public void generarReporte() throws IOException, JRException {
        int separado = determinarNumSeparado();
        List<FacDocumentosmaster> separados = separadoFacade.buscarSeparados(sedeActual, fechaInicial, fechaFinal, clienteSeleccionado, separado);
        List<InformeSeparado> lista = generarLista(separados);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        String rutaReportes = servletContext.getRealPath("/informes/reportes/");//ubicacion para los subreportes
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("/informes/reportes/informeSeparado.jasper");
            Map<String, Object> parametros = new HashMap<>();
            if (sedeActual.getLogo() != null) {
                InputStream logo = new ByteArrayInputStream(sedeActual.getLogo());
                parametros.put("logo", logo);
            }
            parametros.put("SUBREPORT_DIR", rutaReportes);
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    private List<InformeSeparado> generarLista(List<FacDocumentosmaster> separados) {
        List<InformeSeparado> lista = new ArrayList();
        for (FacDocumentosmaster separado : separados) {
            InformeSeparado informeSeparado = new InformeSeparado();
            informeSeparado.setNumDocumento(separado.determinarNumFactura());
            informeSeparado.setFecha(separado.getFecCrea());
            CfgCliente cliente = separado.getCfgclienteidCliente();
            informeSeparado.setCliente(cliente.nombreCompleto());
            informeSeparado.setTelefono(cliente.getTel1());
            informeSeparado.setValor(separado.getTotal());
            //SE BUSCA LA CARTERA DEL SEPARADO
            FacCarteraCliente carteraCliente = carteraFacade.buscarPorDocumentoMaestro(separado);
            if (carteraCliente != null) {
                informeSeparado.setFechaVencimiento(carteraCliente.getFechaLimite());
                float abonado = 0;
                informeSeparado.setSaldo(carteraCliente.getSaldo());
                //SE CARGA EL DETALLE DE LA CARTERA (ABONOS)
                List<FacCarteraDetalle> abonos;
//                abonos = carteraCliente.getFacCarteraDetalleList();
//                if (abonos == null || abonos.isEmpty()) {
                abonos = carteraDetalleFacade.buscarPorCartera(carteraCliente);
//                }
                List<InformeAbonoDetalle> listaDetalleAbonado = new ArrayList();
                for (FacCarteraDetalle abono : abonos) {
                    FacDocumentosmaster documento = abono.getFacDocumentosmaster();//recibo de caja o documento separado
                    InformeAbonoDetalle informeAbonoDetalle = new InformeAbonoDetalle();
                    informeAbonoDetalle.setFechaAbono(documento.getFecCrea());
                    informeAbonoDetalle.setNumDocumento(documento.determinarNumFactura());
                    informeAbonoDetalle.setValor(abono.getAbono());
                    abonado += abono.getAbono();
                    listaDetalleAbonado.add(informeAbonoDetalle);
                }
                informeSeparado.setAbonado(abonado);
                informeSeparado.setDetalleAbono(listaDetalleAbonado);
            }
            //SE CARGA EL DETALLE DEL SEPARADO (PRODUCTOS)
            List<FacDocumentodetalle> productos = separado.getFacDocumentodetalleList();
            if (productos == null || productos.isEmpty()) {
                productos = detalleSeparadoFacade.buscarByDocumentoMaster(separado);
            }
            List<FacturaDetalleReporte> listaDetalleSeparado = new ArrayList();
            for (FacDocumentodetalle producto : productos) {
                FacturaDetalleReporte facturaDetalleReporte = new FacturaDetalleReporte();
                facturaDetalleReporte.setCodProducto(producto.getCfgProducto().getCodProducto());
                facturaDetalleReporte.setNomProducto(producto.getCfgProducto().getNomProducto());
                facturaDetalleReporte.setValorUnitario(producto.getValorUnitario());
                facturaDetalleReporte.setCantidad(producto.getCantidad());
                facturaDetalleReporte.setValorTotal(producto.getValorTotal());
                facturaDetalleReporte.setDescuento(producto.getDescuento());
                if (producto.getTipoDescuento() != null) {
                    facturaDetalleReporte.setPresentacionDescuento(producto.getTipoDescuento() == 1 ? "" : "%");
                } else {
                    facturaDetalleReporte.setPresentacionDescuento("%");
                }
                listaDetalleSeparado.add(facturaDetalleReporte);
            }
            informeSeparado.setDetalleSeparado(listaDetalleSeparado);
            lista.add(informeSeparado);
        }
        return lista;
    }

    //DETERMINA EL NUMERO DEL SEPARADO SIN EL PREFIJO (VALOR NUMERICO)
    private int determinarNumSeparado() {
        int separado;
        if (!numSeparado.trim().isEmpty()) {
            try {
                separado = Integer.parseInt(numSeparado);
            } catch (Exception e) {
                if (numSeparado.contains("0")) {
                    String aux = StringUtils.substringAfter(numSeparado, "0");
                    try {
                        separado = Integer.parseInt(aux);
                    } catch (Exception ex) {
                        separado = 0;
                    }
//                    }
                } else {
                    separado = 0;
                }
                if (separado == 0) {
                    CfgDocumento documento = documentoFacade.buscarDocumentoDeSeparadoBySede(sedeActual);
                    String aux = documento.getPrefijoDoc();
                    int init = aux.length();
                    if (init < numSeparado.length()) {
                        aux = numSeparado.substring(init);
                        try {
                            separado = Integer.parseInt(aux);
                        } catch (Exception ex) {
                            separado = 0;
                        }
                    } else {
                        separado = 0;
                    }
                }
            }
        } else {
            separado = 0;
        }
        return separado;
    }

    public void limpiar() {
        clienteSeleccionado = null;
        cargarInformacionCliente();
        fechaInicial = null;
        fechaFinal = null;
        numSeparado = null;
        RequestContext.getCurrentInstance().update("IdFormInformeSeparado");
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

    public String getIdentificacionCliente() {
        return identificacionCliente;
    }

    public void setIdentificacionCliente(String identificacionCliente) {
        this.identificacionCliente = identificacionCliente;
    }

    public String getNumSeparado() {
        return numSeparado;
    }

    public void setNumSeparado(String numSeparado) {
        this.numSeparado = numSeparado;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public LazyDataModel<CfgCliente> getListaClientes() {
        return listaClientes;
    }

    public CfgCliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(CfgCliente clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
    }

}
