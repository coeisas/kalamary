/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.informes;

import entities.CfgDocumento;
import entities.CfgEmpresasede;
import entities.CfgMovInventarioMaestro;
import entities.CfgProducto;
import entities.FacDocumentosmaster;
import entities.InvMovimientoDetalle;
import facades.CfgDocumentoFacade;
import facades.CfgMovInventarioMaestroFacade;
import facades.CfgProductoFacade;
import facades.InvMovimientoDetalleFacade;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javax.faces.bean.ManagedBean;
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
import utilities.InformeMovimientoInventario;
import utilities.LazyProductosModel;

/**
 *
 * @author Mario
 */
@ManagedBean
@SessionScoped
public class InformeMovimientoInventarioMB implements Serializable {

    private Date fechaIncial;
    private Date fechaFinal;
    private String codProducto;
    private String nombreProducto;
    private String numMovimiento;
    private String documentoSoporte;
    private int idMovimiento;

    private List<CfgMovInventarioMaestro> listaMovimiento;
    private LazyDataModel<CfgProducto> listaProducto;

    private CfgMovInventarioMaestro movimientoSeleccionado;
    private CfgProducto productoSeleccionado;
    private CfgEmpresasede sedeActual;

    @EJB
    CfgProductoFacade productoFacade;
    @EJB
    CfgMovInventarioMaestroFacade movimientoMaestroFacade;
    @EJB
    InvMovimientoDetalleFacade movimientoDetalleFacade;
    @EJB
    CfgMovInventarioMaestroFacade inventarioMaestroFacade;
    @EJB
    CfgDocumentoFacade documentoFacade;

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        sedeActual = sesionMB.getSedeActual();
        listaMovimiento = movimientoMaestroFacade.findAll();
    }

    public InformeMovimientoInventarioMB() {
    }

    public void cargarProductos() {
        if (sedeActual != null) {
            listaProducto = listaProducto = new LazyProductosModel(sedeActual.getCfgempresaidEmpresa(), null, null, null, productoFacade);
            RequestContext.getCurrentInstance().update("FormModalProducto");
            RequestContext.getCurrentInstance().execute("PF('dlgProducto').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro informacion de la sede. Reinicie la sesion"));
        }
    }

    private int determinarNumMovimiento() {
        int num;
        if (!numMovimiento.trim().isEmpty()) {
            try {
                num = Integer.parseInt(numMovimiento);
            } catch (Exception e) {
                if (numMovimiento.contains("0")) {
                    String aux = StringUtils.substringAfter(numMovimiento, "0");
                    try {
                        num = Integer.parseInt(aux);
                    } catch (Exception ex) {
                        num = 0;
                    }
//                    }
                } else {
                    num = 0;
                }
                if (num == 0) {
                    CfgDocumento documento;
                    if (movimientoSeleccionado != null) {
                        if (movimientoSeleccionado.getCodMovInvetario().equals("1")) {
                            documento = documentoFacade.buscarDocumentoInventarioEntradaBySede(sedeActual);
                        } else {
                            documento = documentoFacade.buscarDocumentoInventarioSalidaBySede(sedeActual);
                        }
                    } else {//si no se escoge el tipo de movimiento. No se tendra en cuenta el numMovimiento entrado
                        return 0;
                    }
                    String aux = documento.getPrefijoDoc();
                    int init = aux.length();
                    if (init < numMovimiento.length()) {
                        aux = numMovimiento.substring(init);
                        try {
                            num = Integer.parseInt(aux);
                        } catch (Exception ex) {
                            num = 0;
                        }
                    } else {
                        num = 0;
                    }
                }
            }
        } else {
            num = 0;
        }
        return num;
    }

    public void buscarProducto() {
        if (sedeActual != null) {
            if (!codProducto.isEmpty()) {
                setProductoSeleccionado(productoFacade.buscarPorEmpresaAndCodigo(sedeActual.getCfgempresaidEmpresa(), codProducto));
                if (productoSeleccionado == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro el producto"));
                }
            } else {
                setProductoSeleccionado(null);
            }
            cargarInformacionProducto();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro informacion de la sede. Reinicie la sesion"));
        }
    }

    public void cargarInformacionProducto() {
        if (productoSeleccionado != null) {
            nombreProducto = productoSeleccionado.getNomProducto();
            codProducto = productoSeleccionado.getCodProducto();
        } else {
            nombreProducto = null;
            codProducto = null;
        }
        RequestContext.getCurrentInstance().update("IdFormInformeMovimientoInventario:IdCodigoProducto");
        RequestContext.getCurrentInstance().update("IdFormInformeMovimientoInventario:IdNombreProducto");
        RequestContext.getCurrentInstance().execute("PF('dlgProducto').hide()");
    }

    public void buscarMovimiento() {
        movimientoSeleccionado = inventarioMaestroFacade.find(idMovimiento);
    }

    public void generarReporte() throws IOException, JRException {
        int numero = determinarNumMovimiento();
        List<InvMovimientoDetalle> detalleMovimiento = movimientoDetalleFacade.detalleMovimientoInforme(sedeActual, fechaIncial, fechaFinal, productoSeleccionado, movimientoSeleccionado, numero, documentoSoporte);
        List<InformeMovimientoInventario> lista = generarLista(detalleMovimiento);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        String rutaReportes = servletContext.getRealPath("/informes/reportes/");//ubicacion para los subreportes
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("/informes/reportes/informeMovimientoInventario.jasper");
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

    private List<InformeMovimientoInventario> generarLista(List<InvMovimientoDetalle> listaDetalle) {
        List<InformeMovimientoInventario> lista = new ArrayList();
        for (InvMovimientoDetalle movimientoDetalle : listaDetalle) {
            InformeMovimientoInventario informeMovimientoInventario = new InformeMovimientoInventario();
            informeMovimientoInventario.setFechaMovimiento(movimientoDetalle.getInvMovimiento().getFecha());
            informeMovimientoInventario.setCodigoProducto(movimientoDetalle.getCfgProducto().getCodProducto());
            informeMovimientoInventario.setDescripcion(movimientoDetalle.getCfgProducto().getNomProducto());
            informeMovimientoInventario.setCantidad(movimientoDetalle.getCantidad());
            informeMovimientoInventario.setMovimiento(movimientoDetalle.getInvMovimiento().getCfgmovinventariodetalleidMovInventarioDetalle().getCfgmovinventariomaestroidMovInventarioMaestro().getNombreMovimiento());
            informeMovimientoInventario.setTipoMovimiento(movimientoDetalle.getInvMovimiento().getCfgmovinventariodetalleidMovInventarioDetalle().getNomMovimientoDetalle());
            informeMovimientoInventario.setNumDocumento(movimientoDetalle.getInvMovimiento().determinarNumConcecutivo());
            informeMovimientoInventario.setSoporte(movimientoDetalle.getInvMovimiento().getDocumentoSoporte());
            FacDocumentosmaster factura = movimientoDetalle.getInvMovimiento().getFacDocumentosmaster();
            if (factura != null) {
                informeMovimientoInventario.setFactura(factura.determinarNumFactura());
            } else {
                informeMovimientoInventario.setFactura(null);
            }
            lista.add(informeMovimientoInventario);
        }
        return lista;
    }

    public void limpiar() {
        idMovimiento = 0;
        numMovimiento = null;
        fechaIncial = null;
        fechaFinal = null;
        movimientoSeleccionado = null;
        productoSeleccionado = null;
        documentoSoporte = null;
        cargarInformacionProducto();
        RequestContext.getCurrentInstance().update("IdFormInformeMovimientoInventario");
    }

    public Date getFechaIncial() {
        return fechaIncial;
    }

    public void setFechaIncial(Date fechaIncial) {
        this.fechaIncial = fechaIncial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(String codProducto) {
        this.codProducto = codProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public LazyDataModel<CfgProducto> getListaProducto() {
        return listaProducto;
    }

    public CfgProducto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(CfgProducto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public List<CfgMovInventarioMaestro> getListaMovimiento() {
        return listaMovimiento;
    }

    public String getNumMovimiento() {
        return numMovimiento;
    }

    public void setNumMovimiento(String numMovimiento) {
        this.numMovimiento = numMovimiento;
    }

    public String getDocumentoSoporte() {
        return documentoSoporte;
    }

    public void setDocumentoSoporte(String documentoSoporte) {
        this.documentoSoporte = documentoSoporte;
    }
}
