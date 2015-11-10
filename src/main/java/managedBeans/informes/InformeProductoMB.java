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
import facades.CfgProductoFacade;
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
import utilities.InformeProductos;

/**
 *
 * @author Mario
 */
@ManagedBean
@SessionScoped
public class InformeProductoMB implements Serializable {

    private CfgEmpresa empresaActual;
    private CfgEmpresasede sedeActual;
//    private SegUsuario usuarioActual;

    public InformeProductoMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        empresaActual = sesionMB.getEmpresaActual();
        sedeActual = sesionMB.getSedeActual();
//        usuarioActual = sesionMB.getUsuarioActual();
    }

    @EJB
    CfgProductoFacade productoFacade;

    public void generarReporte() throws IOException, JRException {
        List<CfgProducto> productos = productoFacade.buscarPorEmpresaTodos(empresaActual);
        List<InformeProductos> lista = crearListaAuxiliar(productos);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("/informes/reportes/informeProducto.jasper");
            Map<String, Object> parametros = new HashMap<>();
            if (sedeActual.getLogo() != null) {
                InputStream logo = new ByteArrayInputStream(sedeActual.getLogo());
                parametros.put("logo", logo);
            }
            parametros.put("title", "INFORME PRODUCTOS");
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }
    
    public List<InformeProductos> crearListaAuxiliar(List<CfgProducto> productos){
        List<InformeProductos> lista = new ArrayList();
        for(CfgProducto producto : productos){
            InformeProductos informeProducto = new InformeProductos();
            informeProducto.setCodigo(producto.getCodProducto());
            informeProducto.setDescripcion(producto.getNomProducto());
            CfgMarcaproducto marca = producto.getCfgmarcaproductoidMarca();
            informeProducto.setMarca(marca.getNombreMarca());
            CfgReferenciaproducto referencia = marca.getCfgreferenciaproductoidReferencia();
            informeProducto.setReferencia(referencia.getNombreReferencia());
            CfgCategoriaproducto categoria = referencia.getCfgcategoriaproductoidCategoria();
            informeProducto.setCategoria(categoria.getNombreCategoria());
            informeProducto.setColor(producto.getCfgColorIdColor().getColor());
            informeProducto.setTalla(producto.getCfgTallaIdTalla().getTalla());
            informeProducto.setPrecio(producto.getPrecio());
            informeProducto.setEsServicio(producto.getEsServicio());
            informeProducto.setEsKit(producto.getEsKit());
            informeProducto.setActivo(producto.getActivo());
            lista.add(informeProducto);
        }
        return lista;
    }

}
