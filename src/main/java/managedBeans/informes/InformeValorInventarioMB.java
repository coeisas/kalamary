/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.informes;

import entities.CfgCategoriaproducto;
import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.CfgMarcaproducto;
import entities.CfgProducto;
import entities.CfgReferenciaproducto;
import entities.InvConsolidado;
import facades.CfgProductoFacade;
import facades.InvConsolidadoFacade;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
import utilities.InformeProductos;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class InformeValorInventarioMB implements Serializable {

    private CfgEmpresasede sedeActual;

    @EJB
    CfgProductoFacade productoFacade;
    @EJB
    InvConsolidadoFacade invConsolidadoFacade;

    public InformeValorInventarioMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        sedeActual = sesionMB.getSedeActual();
    }

    public void generarReporte() throws IOException, JRException {
        List<CfgProducto> productos = invConsolidadoFacade.findIdProductosBySedeWithExistencia(sedeActual);
        List<InformeProductos> lista = crearListaAuxiliar(productos);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("/informes/reportes/informeValorInventario.jasper");
            Map<String, Object> parametros = new HashMap<>();
            if (sedeActual.getLogo() != null) {
                InputStream logo = new ByteArrayInputStream(sedeActual.getLogo());
                parametros.put("logo", logo);
            }
            parametros.put("title", "INFORME VALOR INVENTARIO");
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public List<InformeProductos> crearListaAuxiliar(List<CfgProducto> productos) {
        List<InformeProductos> lista = new ArrayList();
        for (CfgProducto producto : productos) {
            InformeProductos informeProducto = new InformeProductos();
            informeProducto.setCodigo(producto.getCodProducto());
            informeProducto.setDescripcion(producto.getNomProducto());
            informeProducto.setColor(producto.getCfgColorIdColor().getColor());
            informeProducto.setTalla(producto.getCfgTallaIdTalla().getTalla());
            float costo = producto.getCosto() != null ? producto.getCosto() : 0;
            InvConsolidado invConsolidado = invConsolidadoFacade.buscarBySedeAndProducto(sedeActual, producto);
            int existencia = invConsolidado.getExistencia();
            informeProducto.setCosto(costo);
            informeProducto.setExistencia(existencia);
            informeProducto.setValor(costo * existencia);
            lista.add(informeProducto);
        }
        return lista;
    }
}
