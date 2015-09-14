/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.informes;

import entities.CfgEmpresasede;
import entities.ConsolidadoMovcaja;
import entities.FacCaja;
import entities.SegUsuario;
import facades.ConsolidadoMovcajaFacade;
import facades.FacCajaFacade;
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
import utilities.InformeMovCaja;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class InformeMovimientoCajaMB implements Serializable {

    private int idCaja;
    private Date fechaInicial;
    private Date fechaFinal;
    private List<FacCaja> listaCajas;

    private CfgEmpresasede sedeActual;
    private SegUsuario usuarioActual;

    @EJB
    FacCajaFacade cajaFacade;
    @EJB
    ConsolidadoMovcajaFacade consolidadoMovcajaFacade;

    public InformeMovimientoCajaMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        sedeActual = sesionMB.getSedeActual();
        usuarioActual = sesionMB.getUsuarioActual();
    }

    public void cargarCajas() {
        if (sedeActual != null) {
            listaCajas = cajaFacade.buscarCajasPorSede(sedeActual);
            RequestContext.getCurrentInstance().update("IdFormInformeMovCaja");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro informacion de la sede"));
        }
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
        ///se debe ingresar al menos un parametro de busqueda
        if (fechaFinal == null && fechaInicial == null && idCaja == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese al menos un parametro"));
            return false;
        }
        if (fechaFinal.before(fechaInicial)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Verifique el orden de las fechas"));
            return false;
        }
        return ban;
    }

    public void generarReporte() throws IOException, JRException {
        if (!validar()) {
            return;
        }
        FacCaja caja = cajaFacade.find(idCaja);
        List<ConsolidadoMovcaja> listadoMovCaja = consolidadoMovcajaFacade.buscarPorSedeAndCaja(sedeActual, caja, fechaInicial, fechaFinal);
        List<InformeMovCaja> lista = generarLista(listadoMovCaja);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("/informes/reportes/informeMovCaja.jasper");
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

    private List<InformeMovCaja> generarLista(List<ConsolidadoMovcaja> listaConsolidado) {
        List<InformeMovCaja> lista = new ArrayList();
        for (ConsolidadoMovcaja movcaja : listaConsolidado) {
            InformeMovCaja informeMovCaja = new InformeMovCaja();
            informeMovCaja.setSede(movcaja.getCfgEmpresasede().getNomSede());
            informeMovCaja.setCaja(movcaja.getFacCaja().getNomCaja());
            informeMovCaja.setFecha(movcaja.getConsolidadoMovcajaPK().getFecha());
            informeMovCaja.setFormaPago(movcaja.getCfgFormapago().getNomFormaPago());
            informeMovCaja.setValor(movcaja.getAcumulado());
            lista.add(informeMovCaja);
        }
        return lista;
    }

    public void limpiar() {
        idCaja = 0;
        fechaInicial = new Date();
        fechaFinal = new Date();
        RequestContext.getCurrentInstance().update("IdFormInformeMovCaja");
    }

    public List<FacCaja> getListaCajas() {
        return listaCajas;
    }

    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
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
}
