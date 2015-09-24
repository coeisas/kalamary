/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.informes;

import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.InvConsolidado;
import facades.CfgEmpresasedeFacade;
import facades.InvConsolidadoFacade;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
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
import org.primefaces.context.RequestContext;
import utilities.InformeInventario;

/**
 *
 * @author Mario
 */
@ManagedBean
@SessionScoped
public class InformeInventarioMB implements Serializable {

    private int idSede;
    private List<CfgEmpresasede> listaSedes;

    private CfgEmpresa empresaActual;
    private SesionMB sesionMB;

    @EJB
    CfgEmpresasedeFacade sedeFacade;
    @EJB
    InvConsolidadoFacade consolidadoFacade;

    public InformeInventarioMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        empresaActual = sesionMB.getEmpresaActual();
        if (empresaActual != null) {
            listaSedes = sedeFacade.buscarSedesPorEmpresa(empresaActual.getIdEmpresa());
        }
    }

    public void generarReporte() throws IOException, JRException {
        CfgEmpresasede sedeSeleccionada = null;
        if (idSede != 0) {
            sedeSeleccionada = sedeFacade.find(idSede);
        }
        List<InvConsolidado> listadoInventario = consolidadoFacade.reporteConsolidadoBySede(sedeSeleccionada);
        List<InformeInventario> lista = generarLista(listadoInventario);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("/informes/reportes/informeInventario.jasper");
            Map<String, Object> parametros = new HashMap<>();
            if (empresaActual.getLogo() != null) {
                InputStream logo = new ByteArrayInputStream(empresaActual.getLogo());
                parametros.put("logo", logo);
            }
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    private List<InformeInventario> generarLista(List<InvConsolidado> listadoInventario) {
        List<InformeInventario> lista = new ArrayList();
        for (InvConsolidado consolidado : listadoInventario) {
            InformeInventario informeInventario = new InformeInventario();
            informeInventario.setSede(consolidado.getCfgEmpresasede().getNomSede());
            informeInventario.setCodigoInterno(consolidado.getCfgProducto().getCodigoInterno());
            informeInventario.setCodigoProducto(consolidado.getCfgProducto().getCodProducto());
            informeInventario.setProducto(consolidado.getCfgProducto().getNomProducto());
            informeInventario.setExistencias(consolidado.getExistencia());
            informeInventario.setEntradas(consolidado.getEntradas());
            informeInventario.setSalidas(consolidado.getSalidas());
            informeInventario.setFechaEntrada(consolidado.getFechaUltEntrada());
            informeInventario.setFechaSalida(consolidado.getFechaUltSalida());
            lista.add(informeInventario);
        }
        return lista;
    }

    public void limpiar() {
        idSede = 0;
        RequestContext.getCurrentInstance().update("IdFormInformeInventario");
    }

    public List<CfgEmpresasede> getListaSedes() {
        return listaSedes;
    }

    public int getIdSede() {
        return idSede;
    }

    public void setIdSede(int idSede) {
        this.idSede = idSede;
    }

}
