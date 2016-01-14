/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.contabilidad;

import entities.CfgCliente;
import entities.CfgEmpresasede;
import entities.CfgProveedor;
import entities.CntMovdetalle;
import entities.SegUsuario;
import facades.CntMovdetalleFacade;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
import utilities.InformeLibroDiario;
import utilities.InformeLibroMayor;

/**
 *
 * @author Mario
 */
@ManagedBean
@SessionScoped
public class InformeContabilidadMB implements Serializable {

    private Date fechaInicial;
    private Date fechaFinal;
    private String cuenta;

    private CfgEmpresasede sedeActual;

    @EJB
    CntMovdetalleFacade cntMovdetalleFacade;

    public InformeContabilidadMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        sedeActual = sesionMB.getSedeActual();
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void generarLibroDiarioPDF() throws IOException, JRException {
        List<CntMovdetalle> detallesMovimiento = cntMovdetalleFacade.busquedaParaInformeDiario(sedeActual.getIdSede(), fechaInicial, fechaFinal, cuenta);
        List<InformeLibroDiario> lista = crearListaDiarioAuxiliar(detallesMovimiento);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("/contabilidad/reportes/infLibroDiario.jasper");
            Map<String, Object> parametros = new HashMap<>();
            if (sedeActual.getLogo() != null) {
                InputStream logo = new ByteArrayInputStream(sedeActual.getLogo());
                parametros.put("logo", logo);
            }
            parametros.put("title", "INFORME LIBRO DIARIO");
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public void generarLibroMayorPDF() throws IOException, JRException {
        List<Object[]> detallesconsolidado = cntMovdetalleFacade.busquedaParaInformeMayor(sedeActual.getIdSede(), fechaInicial, fechaFinal, cuenta);
        List<InformeLibroMayor> lista = crearListaMayorAuxiliar(detallesconsolidado);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("/contabilidad/reportes/infLibroMayor.jasper");
            String header = "<b>Periodo:</b>";
            Map<String, Object> parametros = new HashMap<>();
            if (fechaInicial == null && fechaFinal == null) {
                header = header.concat(" Todos");
            } else {
                String periodo = null;
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                if (fechaInicial != null && fechaFinal != null) {
                    periodo = df.format(fechaInicial);
                    periodo = periodo.concat(" - ");
                    periodo = periodo.concat(df.format(fechaFinal));
                } else {
                    if (fechaInicial != null) {
                        periodo = "Desde ";
                        periodo = periodo.concat(df.format(fechaInicial));
                    }
                    if (fechaFinal != null) {
                        periodo = "Hasta ";
                        periodo = periodo.concat(df.format(fechaFinal));
                    }
                }
                header = header.concat(periodo);
            }
            header = header.concat(" <b>Cuenta:</b>");
            if (cuenta != null && !cuenta.isEmpty()) {
                header = header.concat(" " + cuenta);
            } else {
                header = header.concat(" Todas");
            }
            if (sedeActual.getLogo() != null) {
                InputStream logo = new ByteArrayInputStream(sedeActual.getLogo());
                parametros.put("logo", logo);
            }
            parametros.put("header", header);
            parametros.put("title", "INFORME LIBRO MAYOR");
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public void generarCSV() {

    }

    private List<InformeLibroDiario> crearListaDiarioAuxiliar(List<CntMovdetalle> detallesMovimiento) {
        List<InformeLibroDiario> lista = new ArrayList();
        String aux = null;
        for (CntMovdetalle movdetalle : detallesMovimiento) {
            InformeLibroDiario ic = new InformeLibroDiario();
            ic.setNoMovimiento(movdetalle.getIdcntmovDetalle());
            ic.setFechaMovimiento(movdetalle.getFecha());
            //SI NO SE TRATA DE UNA FACTURA, RC ... ENTONCES ES UN MOVIMIENTO DE INVENTARIO
            if (movdetalle.getFacDocumentosmaster() != null) {
                aux = movdetalle.getFacDocumentosmaster().determinarNumFactura();
                ic.setDocumento(aux);
                CfgCliente cliente = movdetalle.getFacDocumentosmaster().getCfgclienteidCliente();
                //DETERMINA SI EL TERCERO ES UN CLIENTE O UN USUARIO
                if (cliente != null) {
                    aux = cliente.getCfgTipoidentificacionId().getAbreviatura() + " ";
                    aux = aux.concat(cliente.getNumDoc());
                    ic.setIdentificacionTercero(aux);
                    ic.setTercero(cliente.nombreCompleto());
                } else {
                    SegUsuario usuario = movdetalle.getFacDocumentosmaster().getSegusuarioidUsuario1();
                    aux = usuario.getCfgTipoidentificacionId().getAbreviatura() + " ";
                    aux = aux.concat(usuario.getNumDoc());
                    ic.setIdentificacionTercero(aux);
                    ic.setTercero(usuario.nombreCompleto());
                }
                ic.setDetalle(movdetalle.getFacDocumentosmaster().getObservaciones());
            } else {
                aux = movdetalle.getInvMovimiento().determinarNumConcecutivo();
                ic.setDocumento(aux);
                CfgProveedor proveedor = movdetalle.getInvMovimiento().getCfgproveedoridProveedor();
                if (proveedor != null) {
                    ic.setIdentificacionTercero(proveedor.getNumDoc());
                    ic.setTercero(proveedor.getNomProveedor());
                }
                ic.setDetalle(movdetalle.getInvMovimiento().getObservacion());
            }
            ic.setCuenta(movdetalle.getCntpuccodigoCuenta().getCodigoCuenta());
            ic.setNombreCuenta(movdetalle.getCntpuccodigoCuenta().getNombreCuentas());
            ic.setDebito(movdetalle.getDebito());
            ic.setCredito(movdetalle.getCredito());
            ic.setTotal(movdetalle.getTotal());
            lista.add(ic);
        }
        return lista;
    }

    private List<InformeLibroMayor> crearListaMayorAuxiliar(List<Object[]> detallesMovimiento) {
        List<InformeLibroMayor> lista = new ArrayList();
        for (Object[] object : detallesMovimiento) {
            InformeLibroMayor ic = new InformeLibroMayor();
            ic.setCuenta(object[0].toString());
            ic.setNombreCuenta(object[1].toString());
            ic.setDebitos(Float.parseFloat(object[2].toString()));
            ic.setCreditos(Float.parseFloat(object[3].toString()));
            lista.add(ic);
        }
        return lista;
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

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

}
