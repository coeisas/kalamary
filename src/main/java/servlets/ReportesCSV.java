/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import utilities.DBConnector;

/**
 *
 * @author ArcoSoft-Pc1
 */
public class ReportesCSV extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String fechaInicial = request.getParameter("fechaInicial");
        String fechaFinal = request.getParameter("fechaFinal");
        String url = getServletContext().getRealPath("/");
        String identificacion = request.getParameter("identificacion");
        String producto = request.getParameter("producto");
        String cotizacion = request.getParameter("cotizacion");
        int sede = Integer.parseInt(request.getParameter("sede"));
        String tipo = request.getParameter("tipo");
        String query = null;
        try {
            con = DBConnector.getInstance().getConnection();
            switch (tipo) {
                case "rotacion":
                    query = "SELECT p.codigoInterno,p.nomProducto,sum(cantidad),mim.nombreMovimiento,p.costo "
                            + "FROM kalamarypos.inv_movimiento m "
                            + "inner join inv_movimiento_detalle md on md.inv_movimiento_numDoc = m.numDoc "
                            + "inner join cfg_mov_inventario_detalle mi on mi.idMovInventarioDetalle = m.cfg_mov_inventario_detalle_idMovInventarioDetalle "
                            + "inner join cfg_mov_inventario_maestro mim on mim.idMovInventarioMaestro = mi.cfg_mov_inventario_maestro_idMovInventarioMaestro "
                            + "inner join cfg_producto p on p.idProducto = md.cfg_producto_IdProducto "
                            + "where fecha between ? and ADDDATE(?, INTERVAL 13 DAY) "
                            + "and cfg_empresasede_idSede = ? "
                            + "group by p.codProducto,p.nomProducto,mim.nombreMovimiento "
                            + "order by 3 desc";
                    break;
                case "vc":
                    query = "SELECT f.fecCrea,d.prefijoDoc, fc.fac_documentosmaster_numDocumento,ifnull(di.valorImpuesto,0) iva,f.subtotal-f.descuento as subtotal,f.total, "
                            + "concat(ti.abreviatura,' ',c.numDoc,' ',c.nom1Cliente,' ',c.nom2Cliente,' ',c.apellido1,' ',c.apellido2) as nombre "
                            + "FROM kalamarypos.fac_cartera_cliente fc "
                            + "inner join fac_documentosmaster f on fc.fac_documentosmaster_numDocumento = f.numDocumento "
                            + "inner join cfg_cliente c on c.idCliente = fc.cfg_cliente_idCliente "
                            + "inner join cfg_documento d on d.cfg_empresasede_idSede = f.cfg_empresasede_idSede "
                            + "inner join cfg_tipoidentificacion ti on ti.id = c.cfg_tipoidentificacion_id "
                            + "left join fac_documentoimpuesto di on di.fac_documentosmaster_numDocumento = fc.fac_documentosmaster_numDocumento and di.porcentajeImpuesto=16 and di.fac_documentosmaster_cfg_documento_idDoc=f.cfg_documento_idDoc "
                            + "where f.estado!='CANCELADA' and f.estado!='ANULADA' "
                            + "and f.cfg_empresasede_idSede=? "
                            + "and d.codDocumento  !=6 "
                            + "and f.cfg_documento_idDoc =d.idDoc ";
                    if (!identificacion.equals("0")) {
                    query = query + " and c.numDoc = ? ";
                    }   query = query + " order by 1 desc";
                    break;
                case "productosVendidos":
                    query  ="SELECT c.nombreCategoria, " +
                        "       r.nombreReferencia, " +
                        "	   m.nombreMarca, " +
                        "	   p.codigoInterno, " +
                        "		p.nomProducto, " +
                        "		sum(fd.cantidad) as total, " +
                        "		max(f.fecCrea) as maxFeCrea " +
                        "FROM kalamarypos.fac_cartera_cliente fc " +
                        "inner join fac_documentosmaster f on fc.fac_documentosmaster_numDocumento = f.numDocumento " +
                        "inner join cfg_documento d on d.cfg_empresasede_idSede = f.cfg_empresasede_idSede " +
                        "inner join fac_documentodetalle  fd on fd.fac_documentosmaster_cfg_documento_idDoc=f.cfg_documento_idDoc " +
                        "inner join cfg_producto p on p.idProducto = fd.cfg_producto_idProducto " +
                        "inner join cfg_categoriaproducto c on c.idCategoria = p.cfg_categoriaproducto_idCategoria " +
                        "inner join cfg_referenciaproducto r on r.idReferencia = p.cfg_referenciaproducto_idReferencia " +
                        "inner join cfg_marcaproducto m on m.idMarca = p.cfg_marcaproducto_idMarca " +
                        "where f.estado!='CANCELADA' and f.estado!='ANULADA' " +
                        "and f.cfg_empresasede_idSede=? " +
                        "and fd.fac_documentosmaster_numDocumento=f.numDocumento " +
                        "and f.cfg_documento_idDoc =d.idDoc  " +
                        "and f.fecCrea between ? and ADDDATE(?, INTERVAL 1 DAY) " +
                        "and fd.fac_documentosmaster_numDocumento = f.numDocumento " +
                        "group by fd.cfg_producto_idProducto " +
                        "order by 6 desc ";
                    break;//productosVendidos
                case "ventasVendedores":
                    query = "SELECT f.fecCrea,d.prefijoDoc, fc.fac_documentosmaster_numDocumento,ifnull(di.valorImpuesto,0) iva,f.subtotal-f.descuento as subtotal,f.total, " +
                            "concat('CC ',s.numDoc,' ',s.nom1Usuario,' ',s.nom2Usuario,' ',s.apellido1,' ',s.apellido2) as nombre " +
                            "FROM kalamarypos.fac_cartera_cliente fc " +
                            "inner join fac_documentosmaster f on fc.fac_documentosmaster_numDocumento = f.numDocumento " +
                            "inner join cfg_documento d on d.cfg_empresasede_idSede = f.cfg_empresasede_idSede " +
                            "inner join seg_usuario s on s.idUsuario = f.seg_usuario_idUsuario1 " +
                            " left join fac_documentoimpuesto di on di.fac_documentosmaster_numDocumento = fc.fac_documentosmaster_numDocumento and di.porcentajeImpuesto=16 and di.fac_documentosmaster_cfg_documento_idDoc=f.cfg_documento_idDoc "+
                            "where f.estado!='CANCELADA' and f.estado!='ANULADA' " +
                            "and f.cfg_empresasede_idSede=? "
                            + "and f.fecCrea between ? and  ADDDATE(?, INTERVAL 13 DAY) "
                            + "and f.cfg_documento_idDoc =d.idDoc " +
                            "and d.codDocumento  !=6 " +
                            "and s.cfg_rol_idrol=3 " ;
                            if (!identificacion.equals("0")) {
                                if(!identificacion.equals("")){
                                query = query + "and s.numDoc = ? " ;
                                }
                            }   
                            query = query + " order by 1 desc";
                        break;//Ventas vendedores
                    case "productosA":
                        query  ="select c.nombreCategoria, " +
                        "       r.nombreReferencia, " +
                        "	   m.nombreMarca, " +
                        "	   p.codigoInterno, " +
                        "       p.nomProducto, " +
                        "		p.precio " +
                        "from cfg_producto p " +
                        "inner join cfg_empresasede s on s.cfg_empresa_idEmpresa = p.cfg_empresa_idEmpresa " +
                        "inner join cfg_categoriaproducto c on c.idCategoria = p.cfg_categoriaproducto_idCategoria " +
                        "inner join cfg_referenciaproducto r on r.idReferencia = p.cfg_referenciaproducto_idReferencia " +
                        "inner join cfg_marcaproducto m on m.idMarca = p.cfg_marcaproducto_idMarca " +
                        "where s.idSede =? " ;
                        if(producto!=null){
                            if(!producto.equals("")){
                                query = query+"and p.codigoInterno= ? ";
                            }
                        }
                        
                        break;//Productos
                    case "servicios":
                        query  ="SELECT c.nombreCategoria, " +
                            "       r.nombreReferencia, " +
                            "	   m.nombreMarca, " +
                            "	   p.codigoInterno, " +
                            "       p.codBarProducto , " +
                            "		p.nomProducto, " +
                            "		p.costo, " +
                            "        p.utilidad, " +
                            "        p.precio, " +
                            "		p.fecCrea " +
                            "FROM " +
                            " cfg_producto p " +
                            "inner join cfg_empresasede s on s.cfg_empresa_idEmpresa = p.cfg_empresa_idEmpresa " +
                            "inner join cfg_categoriaproducto c on c.idCategoria = p.cfg_categoriaproducto_idCategoria " +
                            "inner join cfg_referenciaproducto r on r.idReferencia = p.cfg_referenciaproducto_idReferencia " +
                            "inner join cfg_marcaproducto m on m.idMarca = p.cfg_marcaproducto_idMarca " +
                            "where p.esServicio=1 " +
                            "and s.idSede =? " ;
                            if(producto!=null){
                                if(!producto.equals("")){
                                query = query+"and p.codigoInterno= ? ";
                                }
                            }
                            
                            query = query+"order by 1 desc";
                        break;//Servicios
                    case "stock":
                        query  ="SELECT c.nombreCategoria, " +
                                "       r.nombreReferencia, " +
                                "	   m.nombreMarca, " +
                                "	   p.codigoInterno, " +
                                "		p.nomProducto, " +
                                "		co.existencia " +
                                "FROM kalamarypos.cfg_producto p " +
                                "inner join inv_consolidado co on co.cfg_producto_idProducto = p.idProducto " +
                                "inner join cfg_categoriaproducto c on c.idCategoria = p.cfg_categoriaproducto_idCategoria " +
                                "inner join cfg_referenciaproducto r on r.idReferencia = p.cfg_referenciaproducto_idReferencia " +
                                "inner join cfg_marcaproducto m on m.idMarca = p.cfg_marcaproducto_idMarca " +
                                "where co.cfg_empresasede_idSede=? " ;
                                if(producto!=null){
                                    if(!producto.equals("")){
                                    query = query+"and p.codigoInterno= ? ";
                                    }
                                }
                              
                                query  =query+" order by co.existencia desc";
                        break;//stock
                    case "productoClientes":
                        query  ="SELECT concat(ti.abreviatura,' ',c.numDoc,' ',c.nom1Cliente,' ',c.nom2Cliente,' ',c.apellido1,' ',c.apellido2) as nombre, " +
                                "fc.cfg_cliente_idCliente,fc.fac_documentosmaster_numDocumento,sum(cantidad) as total,fd.cfg_producto_idProducto, " +
                                "max(f.fecCrea),concat(ca.nombreCategoria,' ',r.nombreReferencia,' ',m.nombreMarca,' ',p.codigoInterno,' ',p.nomProducto) as producto " +
                                "FROM kalamarypos.fac_cartera_cliente fc " +
                                "inner join fac_documentosmaster f on fc.fac_documentosmaster_numDocumento = f.numDocumento " +
                                "inner join fac_documentodetalle  fd on fd.fac_documentosmaster_cfg_documento_idDoc=f.cfg_documento_idDoc " +
                                "inner join cfg_cliente c on c.idCliente = fc.cfg_cliente_idCliente " +
                                "inner join cfg_documento d on d.cfg_empresasede_idSede = f.cfg_empresasede_idSede " +
                                "inner join cfg_tipoidentificacion ti on ti.id = c.cfg_tipoidentificacion_id " +
                                "inner join cfg_producto p on p.idProducto = fd.cfg_producto_idProducto " +
                                "inner join cfg_categoriaproducto ca on ca.idCategoria = p.cfg_categoriaproducto_idCategoria " +
                                "inner join cfg_referenciaproducto r on r.idReferencia = p.cfg_referenciaproducto_idReferencia " +
                                "inner join cfg_marcaproducto m on m.idMarca = p.cfg_marcaproducto_idMarca " +
                                "where f.estado!='CANCELADA' and f.estado!='ANULADA' " +
                                "and f.cfg_empresasede_idSede=? " +
                                "and fd.fac_documentosmaster_numDocumento = f.numDocumento " +
                                "and d.codDocumento  =1 " ;
                                    if(producto!=null){
                                        if(!producto.equals("")){
                                        query = query+"and p.codigoInterno= ? ";
                                        }
                                    }

                                
                                query = query +" group by fd.cfg_producto_idProducto " +
                                "order by 4 desc";
                         break;//productosClientes
                    case "cotizacion":
                        query  ="SELECT p.codigoInterno, " +
                            "p.nomProducto, " +
                            "p.precio, " +
                            "ifnull(di.valorImpuesto,0) as iva, " +
                            "f.total, " +
                            "concat(ti.abreviatura,' ',cl.numDoc,' ',cl.nom1Cliente,' ',cl.nom2Cliente,' ',cl.apellido1,' TELEFONO ',cl.tel1) as nombre, " +
                            "concat('No Cotización:',' ',d.prefijoDoc,' ', fc.fac_documentosmaster_numDocumento) cotizacion, " +
                            "f.fecCrea, concat('Vendedor:',s.nom1Usuario,' ',s.nom2Usuario,' ',s.apellido1,' ',s.apellido2) as vendedor " +
                            " " +
                            "FROM kalamarypos.fac_documentosmaster fc " +
                            "inner join fac_documentosmaster f on fc.fac_documentosmaster_numDocumento = f.numDocumento " +
                            "inner join fac_documentodetalle  fd on fd.fac_documentosmaster_cfg_documento_idDoc=f.cfg_documento_idDoc " +
                            "inner join cfg_cliente cl on cl.idCliente = fc.cfg_cliente_idCliente " +
                            "inner join cfg_documento d on d.cfg_empresasede_idSede = f.cfg_empresasede_idSede " +
                            "inner join cfg_producto p on p.idProducto = fd.cfg_producto_idProducto " +
                            "inner join cfg_categoriaproducto c on c.idCategoria = p.cfg_categoriaproducto_idCategoria " +
                            "inner join cfg_referenciaproducto r on r.idReferencia = p.cfg_referenciaproducto_idReferencia " +
                            "inner join cfg_marcaproducto m on m.idMarca = p.cfg_marcaproducto_idMarca " +
                            "inner join cfg_tipoidentificacion ti on ti.id = cl.cfg_tipoidentificacion_id " +
                            "inner join seg_usuario s on s.idUsuario = f.seg_usuario_idUsuario1 " +
                            "left join fac_documentoimpuesto di on di.fac_documentosmaster_numDocumento = fc.fac_documentosmaster_numDocumento and di.porcentajeImpuesto=16 and di.fac_documentosmaster_cfg_documento_idDoc=f.cfg_documento_idDoc " +
                            "where f.estado!='CANCELADA' and f.estado!='ANULADA' " +
                            "and f.cfg_empresasede_idSede=? " ;
                        if(identificacion!=null){
                            if(!identificacion.equals(""))
                                query = query + "and cl.numDoc =  ? " ;
                        }
                        if(cotizacion!=null){
                            if(!cotizacion.equals(""))
                                query = query + "and fc.fac_documentosmaster_numDocumento =  ? " ;
                        }
                        query = query+    " and d.codDocumento  =6 " +
                            "and fd.fac_documentosmaster_numDocumento=f.numDocumento " +
                            "and f.cfg_documento_idDoc = d.idDoc " +
                            "and s.cfg_rol_idrol=3";
                        break;//cotizacion
                    case "vencimientosFacturas":
                        query  ="SELECT cc.numDoc, " +
                            "      concat(cc.nom1Cliente,' ',cc.nom2Cliente,' ',cc.apellido1,' ',cc.apellido2) cliente, " +
                            "       cc.tel1, " +
                            "       fc.valor, " +
                            "       fc.saldo, " +
                            "       fc.fecha_limite, " +
                            "       concat(d.prefijoDoc,' ',fd.numDocumento) documento " +
                            "FROM kalamarypos.fac_cartera_cliente fc " +
                            "inner join fac_documentosmaster fd on fc.fac_documentosmaster_numDocumento = fd.numDocumento " +
                            "inner join cfg_cliente cc on cc.idCliente = fc.cfg_cliente_idCliente " +
                            "inner join cfg_documento d on d.cfg_empresasede_idSede = fd.cfg_empresasede_idSede " +
                            " where fc.estado='PENDIENTE' " +
                            "and fecha_limite<=ADDDATE(now(), INTERVAL 15 DAY) " +
                            "and fd.cfg_documento_idDoc = fc.fac_documentosmaster_cfg_documento_idDoc " +
                            "and d.codDocumento  =1 "
                          + "and fd.cfg_documento_idDoc =d.idDoc " +
                            "and fd.cfg_empresasede_idSede=? " +
                            "and fd.cfg_documento_idDoc = fc.fac_documentosmaster_cfg_documento_idDoc " +
                            "order by fecha_limite";
                        break;//vencimiento factruas
                    case "vencimientosSeparados":
                        query  ="SELECT cc.numDoc, " +
                            "      concat(cc.nom1Cliente,' ',cc.nom2Cliente,' ',cc.apellido1,' ',cc.apellido2) cliente, " +
                            "       cc.tel1, " +
                            "       fc.valor, " +
                            "       fc.saldo, " +
                            "       fc.fecha_limite, " +
                            "       concat(d.prefijoDoc,' ',fd.numDocumento) documento " +
                            "FROM kalamarypos.fac_cartera_cliente fc " +
                            "inner join fac_documentosmaster fd on fc.fac_documentosmaster_numDocumento = fd.numDocumento " +
                            "inner join cfg_cliente cc on cc.idCliente = fc.cfg_cliente_idCliente " +
                            "inner join cfg_documento d on d.cfg_empresasede_idSede = fd.cfg_empresasede_idSede " +
                            " where fc.estado='PENDIENTE' " +
                            "and fecha_limite<=ADDDATE(now(), INTERVAL 15 DAY) " +
                            "and fd.cfg_documento_idDoc = fc.fac_documentosmaster_cfg_documento_idDoc " +
                            "and d.codDocumento  =7 " +
                            "and fd.cfg_empresasede_idSede=? "
                            + "and fd.cfg_documento_idDoc =d.idDoc " +
                            "and fd.cfg_documento_idDoc = fc.fac_documentosmaster_cfg_documento_idDoc " +
                            "order by fecha_limite";
                        
                        break;//vencimientoSeparados
                    case "productosSinRotacion":
                        query  ="SELECT c.nombreCategoria, " +
                            "       r.nombreReferencia, " +
                            "	   ma.nombreMarca, " +
                            "	   p.codigoInterno, " +
                            "		p.nomProducto, " +
                            "       max(m.fecha) fechaultimo " +
                            " " +
                            "FROM kalamarypos.inv_movimiento_detalle  im " +
                            "inner join inv_movimiento m on im.inv_movimiento_numDoc = m.numDoc " +
                            "left join cfg_producto p on p.idProducto = im.cfg_producto_idProducto " +
                            "inner join cfg_categoriaproducto c on c.idCategoria = p.cfg_categoriaproducto_idCategoria " +
                            "inner join cfg_referenciaproducto r on r.idReferencia = p.cfg_referenciaproducto_idReferencia " +
                            "inner join cfg_marcaproducto ma on ma.idMarca = p.cfg_marcaproducto_idMarca " +
                            "where cfg_empresasede_idSede=? " +
                            "group by im.cfg_producto_idProducto " +
                            "having DATEDIFF(now(),max(m.fecha))>=90 " +
                            "order by 6";
                        break;//productosSinRotacion
            }
            ps = con.prepareStatement(query);
            ps.clearParameters();
            int indice = 1;
            switch (tipo) {
                case "rotacion":
                    ps.setString(indice++, fechaInicial);
                    ps.setString(indice++, fechaFinal);
                    ps.setInt(indice++, sede);
                    break;
                case "vc":
                    ps.setInt(indice++, sede);
                    if (!identificacion.equals("0")) {
                        ps.setString(indice++, identificacion);
                    }
                    break;
                case "productosVendidos":
                    ps.setInt(indice++, sede);
                    ps.setString(indice++, fechaInicial);
                    ps.setString(indice++, fechaFinal);
                    
                    break;
                    case "ventasVendedores":
                        ps.setInt(indice++, sede);
                        ps.setString(indice++, fechaInicial);
                    ps.setString(indice++, fechaFinal);
                    if (!identificacion.equals("0")) {
                        if(!identificacion.equals(""))
                            ps.setString(indice++, identificacion);
                    }
                        break;
                        case "productosA":
                            ps.setInt(indice++, sede);
                            if(producto!=null){
                            if(!producto.equals("")){
                                ps.setString(indice++, producto);
                            }
                        }
                        break;//Producros
                    case "servicios":
                            ps.setInt(indice++, sede);
                            if(producto!=null){
                            if(!producto.equals("")){
                                ps.setString(indice++, producto);
                            }
                            }
                        break;//servicios
                    case "stock":
                            ps.setInt(indice++, sede);
                                if(producto!=null){
                                    if(!producto.equals("")){
                                    ps.setString(indice++, producto);
                                    }
                                }
                        break;//stock
                    case "productoClientes":
                            ps.setInt(indice++, sede);
                            if(producto!=null){
                               if(!producto.equals("")){
                                  ps.setString(indice++, producto);
                                }
                            }
                        break;//productoClientes
                     case "cotizacion":
                         ps.setInt(indice++, sede);
                         if(identificacion!=null){
                            if(!identificacion.equals(""))
                                ps.setString(indice++, identificacion);
                        }
                        if(cotizacion!=null){
                            if(!cotizacion.equals(""))
                                ps.setString(indice++, cotizacion);
                        }
                        break;
                    case "vencimientosFacturas":
                         ps.setInt(indice++, sede);
                    break;
                        case "vencimientosSeparados":
                         ps.setInt(indice++, sede);
                    break;
                    case "productosSinRotacion":
                         ps.setInt(indice++, sede);
                    break;
                           
            }
            rs = ps.executeQuery();
            Date fechaActual = new Date();
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sd2 = new SimpleDateFormat("dd-MMM-yyyy");
            String rutaArchivo = url + "/informes/reportes/";
            String nombreArchivo = null;
            switch (tipo) {
                case "rotacion":
                    nombreArchivo = "rotacionProducto_" + sd.format(fechaActual) + "_" + sede + ".xls";
                    break;
                case "vc":
                    nombreArchivo = "ventasPorCliente_" + sd.format(fechaActual) + "_" + sede + ".xls";
                    break;
                case "productosVendidos":
                    nombreArchivo = "productosVendidos" + sd.format(fechaActual) + "_" + sede + ".xls";
                    break;
                case "ventasVendedores":
                        nombreArchivo = "ventasVendedores" + sd.format(fechaActual) + "_" + sede + ".xls";
                    break;
                case "productosA":
                        nombreArchivo = "productos" + sd.format(fechaActual) + "_" + sede + ".xls";
                    break;
                case "servicios":
                        nombreArchivo = "servicios" + sd.format(fechaActual) + "_" + sede + ".xls";
                    break;
                case "stock":
                        nombreArchivo = "stock" + sd.format(fechaActual) + "_" + sede + ".xls";
                    break;
                case "productoClientes":
                    nombreArchivo = "ventasProductosClientes" + sd.format(fechaActual) + "_" + sede + ".xls";
                    break;
                case "cotizacion":
                    nombreArchivo = "cotizacion" + sd.format(fechaActual) + "_" + sede + ".xls";
                    break;
                case "vencimientosFacturas":
                         nombreArchivo = "vencimientoFacturas" + sd.format(fechaActual) + "_" + sede + ".xls";
                    break;
                case "vencimientosSeparados":
                         nombreArchivo = "vencimientoSeparados" + sd.format(fechaActual) + "_" + sede + ".xls";
                    break;
                case "productosSinRotacion":
                         nombreArchivo = "productosSinRotacion" + sd.format(fechaActual) + "_" + sede + ".xls";
                    break;
            }

            rutaArchivo = rutaArchivo + nombreArchivo;
            File archivo1 = new File(rutaArchivo);
            if (archivo1.exists()) {
                archivo1.delete();
            }
            archivo1.createNewFile();
            HSSFWorkbook libro = new HSSFWorkbook();

            HSSFCellStyle cellStyle = libro.createCellStyle();
            HSSFFont font = libro.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 10);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setColor(HSSFColor.BLACK.index);
            font.setCharSet(HSSFFont.ANSI_CHARSET);
            cellStyle.setFont(font);
            /*Se inicializa el flujo de datos con el archivo xls*/
            FileOutputStream archi = new FileOutputStream(rutaArchivo);
            Sheet hoja;
            Cell celda;
            Row fila ;
            int i = 0;
            switch (tipo) {
                case "rotacion":
                    hoja = libro.createSheet("Rotación de Producto");
                    fila = hoja.createRow(i);
                    celda = fila.createCell(0);
                    celda.setCellValue("CODIGO PRODUCTO");
                    celda.setCellStyle(cellStyle);

                    celda = fila.createCell(1);
                    celda.setCellValue("PRODUCTO");
                    celda.setCellStyle(cellStyle);

                    celda = fila.createCell(2);
                    celda.setCellValue("CANTIDAD MOVIMIENTO");
                    celda.setCellStyle(cellStyle);

                    celda = fila.createCell(3);
                    celda.setCellValue("TIPO MOVIMIENTO");
                    celda.setCellStyle(cellStyle);

                    celda = fila.createCell(4);
                    celda.setCellValue("COSTO UNITARIO");
                    celda.setCellStyle(cellStyle);

                    celda = fila.createCell(5);
                    celda.setCellValue("COSTO TOTAL");
                    celda.setCellStyle(cellStyle);

                    i = i + 1;
                    for (int j = 0; j <= 12; j++) {
                        hoja.autoSizeColumn(j);
                    }
                    while (rs.next()) {
                        fila = hoja.createRow(i);
                        celda = fila.createCell(0);
                        celda.setCellValue(rs.getString(1));
                        celda = fila.createCell(1);
                        celda.setCellValue(rs.getString(2));
                        celda = fila.createCell(2);
                        celda.setCellValue(rs.getInt(3));
                        celda = fila.createCell(3);
                        celda.setCellValue(rs.getString(4));
                        celda = fila.createCell(4);
                        celda.setCellValue(rs.getDouble(5));
                        celda = fila.createCell(5);
                        celda.setCellValue((rs.getDouble(5) * rs.getInt(3)));
                        ////celda.getStringCellValue().getBytes(Charset.forName("UTF-8"));
                        i = i + 1;
                    }
                    break;
                    //En rotación
                    case "vc":
                        hoja = libro.createSheet("Ventas por clientes");
                        fila = hoja.createRow(i);
                        celda = fila.createCell(0);
                        celda.setCellValue("FECHA");
                        celda.setCellStyle(cellStyle);

                        celda = fila.createCell(1);
                        celda.setCellValue("DOCUMENTO");
                        celda.setCellStyle(cellStyle);

                        celda = fila.createCell(2);
                        celda.setCellValue("CLIENTE");
                        celda.setCellStyle(cellStyle);

                        celda = fila.createCell(3);
                        celda.setCellValue("SUBTOTAL");
                        celda.setCellStyle(cellStyle);

                        celda = fila.createCell(4);
                        celda.setCellValue("IVA");
                        celda.setCellStyle(cellStyle);
                        
                        celda = fila.createCell(5);
                        celda.setCellValue("COSTO TOTAL");
                        celda.setCellStyle(cellStyle);


                        i = i + 1;
                        for (int j = 0; j <= 12; j++) {
                            hoja.autoSizeColumn(j);
                        }
                        while (rs.next()) {
                            fila = hoja.createRow(i);
                            celda = fila.createCell(0);
                            celda.setCellValue(sd2.format(rs.getDate(1)));
                            celda = fila.createCell(1);
                            celda.setCellValue(rs.getString(2)+" "+rs.getString(3));
                            celda = fila.createCell(2);
                            celda.setCellValue(rs.getString(7));
                            celda = fila.createCell(3);
                            celda.setCellValue(rs.getDouble(5));
                            celda = fila.createCell(4);
                            celda.setCellValue(rs.getDouble(4));
                            celda = fila.createCell(5);
                            celda.setCellValue(rs.getDouble(6));
                            i = i + 1;
                        }
                    break;//Ventas por cliente
                        
                    case "productosVendidos":
                        hoja = libro.createSheet("Productos Vendidos");
                        fila = hoja.createRow(i);
                        celda = fila.createCell(0);
                        celda.setCellValue("CATEGORIA");
                        celda.setCellStyle(cellStyle);

                        celda = fila.createCell(1);
                        celda.setCellValue("REFERENCIA");
                        celda.setCellStyle(cellStyle);

                        celda = fila.createCell(2);
                        celda.setCellValue("MARCA");
                        celda.setCellStyle(cellStyle);

                        celda = fila.createCell(3);
                        celda.setCellValue("CODIGO INTERNO");
                        celda.setCellStyle(cellStyle);

                        celda = fila.createCell(4);
                        celda.setCellValue("PRODUCTO");
                        celda.setCellStyle(cellStyle);
                        
                        celda = fila.createCell(5);
                        celda.setCellValue("CANTIDAD VENDIDA");
                        celda.setCellStyle(cellStyle);
                        
                        celda = fila.createCell(6);
                        celda.setCellValue("FECHA ULTIMA VENTA");
                        celda.setCellStyle(cellStyle);


                        i = i + 1;
                        for (int j = 0; j <= 12; j++) {
                            hoja.autoSizeColumn(j);
                        }
                        while (rs.next()) {
                            fila = hoja.createRow(i);
                            celda = fila.createCell(0);
                            celda.setCellValue(rs.getString(1));
                            
                            celda = fila.createCell(1);
                            celda.setCellValue(rs.getString(2));
                            
                            celda = fila.createCell(2);
                            celda.setCellValue(rs.getString(3));
                            
                            celda = fila.createCell(3);
                            celda.setCellValue(rs.getString(4));
                            
                            celda = fila.createCell(4);
                            celda.setCellValue(rs.getString(5));
                            
                            celda = fila.createCell(5);
                            celda.setCellValue(rs.getDouble(6));
                            
                            celda = fila.createCell(6);
                            celda.setCellValue(sd2.format(rs.getDate(7)));
                            i = i + 1;
                        }
                    break;//Productos Vendidos
                        case "ventasVendedores":
                         hoja = libro.createSheet("Ventas Vendedores");
                        fila = hoja.createRow(i);
                        celda = fila.createCell(0);
                        celda.setCellValue("FECHA");
                        celda.setCellStyle(cellStyle);

                        celda = fila.createCell(1);
                        celda.setCellValue("VENDEDOR");
                        celda.setCellStyle(cellStyle);

                        celda = fila.createCell(2);
                        celda.setCellValue("DOCUMENTO");
                        celda.setCellStyle(cellStyle);

                        celda = fila.createCell(3);
                        celda.setCellValue("SUBTOTAL");
                        celda.setCellStyle(cellStyle);

                        celda = fila.createCell(4);
                        celda.setCellValue("IVA");
                        celda.setCellStyle(cellStyle);
                        
                        celda = fila.createCell(5);
                        celda.setCellValue("TOTAL");
                        celda.setCellStyle(cellStyle);
                        
                        i = i + 1;
                        for (int j = 0; j <= 12; j++) {
                            hoja.autoSizeColumn(j);
                        }
                        while (rs.next()) {
                              fila = hoja.createRow(i);
                            celda = fila.createCell(0);
                            celda.setCellValue(sd2.format(rs.getDate(1)));
                            
                            celda = fila.createCell(1);
                            celda.setCellValue(rs.getString("nombre"));
                            
                            celda = fila.createCell(2);
                            celda.setCellValue(rs.getString(2)+" "+rs.getString(3));
                            
                            celda = fila.createCell(3);
                            celda.setCellValue(rs.getDouble("subtotal"));
                            
                            celda = fila.createCell(4);
                            celda.setCellValue(rs.getDouble("IVA"));
                            
                            celda = fila.createCell(5);
                            celda.setCellValue(rs.getDouble("TOTAL"));
                            
                            i = i + 1;
                        }
                            break;//ventas vendedores
                        case "productosA":
                             hoja = libro.createSheet("Informe Productos");
                            fila = hoja.createRow(i);
                            celda = fila.createCell(0);
                            celda.setCellValue("CATEGORIA");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(1);
                            celda.setCellValue("REFERENCIA");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(2);
                            celda.setCellValue("MARCA");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(3);
                            celda.setCellValue("CODIGO INTERNO");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(4);
                            celda.setCellValue("PRODUCTO");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(5);
                            celda.setCellValue("VALOR");
                            celda.setCellStyle(cellStyle);

                            i = i + 1;
                            for (int j = 0; j <= 12; j++) {
                                hoja.autoSizeColumn(j);
                            }
                            while (rs.next()) {
                                  fila = hoja.createRow(i);
                            celda = fila.createCell(0);
                            celda.setCellValue(rs.getString("nombreCategoria"));
                            
                            celda = fila.createCell(1);
                            celda.setCellValue(rs.getString("nombreReferencia"));
                            
                            celda = fila.createCell(2);
                            celda.setCellValue(rs.getString("nombreMarca"));
                            
                            celda = fila.createCell(3);
                            celda.setCellValue(rs.getString("codigoInterno"));
                            
                            celda = fila.createCell(4);
                            celda.setCellValue(rs.getString("nomProducto"));
                            
                            celda = fila.createCell(5);
                            celda.setCellValue(rs.getDouble("PRECIO"));
                            
                            i = i + 1;
                            }
                        break;//productos
                    case "servicios":
                            hoja = libro.createSheet("Servicios");
                            fila = hoja.createRow(i);
                            celda = fila.createCell(0);
                            celda.setCellValue("CATEGORIA");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(1);
                            celda.setCellValue("REFERENCIA");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(2);
                            celda.setCellValue("MARCA");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(3);
                            celda.setCellValue("CODIGO INTERNO");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(4);
                            celda.setCellValue("CODIGO DE BARRAS");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(5);
                            celda.setCellValue("PRODUCTO");
                            celda.setCellStyle(cellStyle);
                            
                            celda = fila.createCell(6);
                            celda.setCellValue("COSTO");
                            celda.setCellStyle(cellStyle);
                            
                            celda = fila.createCell(7);
                            celda.setCellValue("UTILIDAD");
                            celda.setCellStyle(cellStyle);
                            
                            celda = fila.createCell(8);
                            celda.setCellValue("PRECIO");
                            celda.setCellStyle(cellStyle);
                            
                            celda = fila.createCell(9);
                            celda.setCellValue("FECHA CREACION");
                            celda.setCellStyle(cellStyle);

                            i = i + 1;
                            for (int j = 0; j <= 12; j++) {
                                hoja.autoSizeColumn(j);
                            }
                            while (rs.next()) {
                                  fila = hoja.createRow(i);
                            celda = fila.createCell(0);
                            celda.setCellValue(rs.getString("nombreCategoria"));
                            
                            celda = fila.createCell(1);
                            celda.setCellValue(rs.getString("nombreReferencia"));
                            
                            celda = fila.createCell(2);
                            celda.setCellValue(rs.getString("nombreMarca"));
                            
                            celda = fila.createCell(3);
                            celda.setCellValue(rs.getString("codigoInterno"));
                            
                            celda = fila.createCell(4);
                            celda.setCellValue(rs.getString("codBarProducto"));
                            
                            celda = fila.createCell(5);
                            celda.setCellValue(rs.getString("nomProducto"));
                            
                            celda = fila.createCell(6);
                            celda.setCellValue(rs.getDouble("costo"));
                            
                            celda = fila.createCell(7);
                            celda.setCellValue(rs.getDouble("utilidad"));
                            
                            celda = fila.createCell(8);
                            celda.setCellValue(rs.getDouble("precio"));
                            
                            celda = fila.createCell(9);
                            celda.setCellValue(sd2.format(rs.getDate("fecCrea")));
                            
                            
                            i = i + 1;
                            }

                        break;//Servicios
                    case "stock":
                            hoja = libro.createSheet("Stock");
                            fila = hoja.createRow(i);
                            celda = fila.createCell(0);
                            celda.setCellValue("CATEGORIA");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(1);
                            celda.setCellValue("REFERENCIA");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(2);
                            celda.setCellValue("MARCA");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(3);
                            celda.setCellValue("CODIGO INTERNO");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(4);
                            celda.setCellValue("PRODUCTO");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(5);
                            celda.setCellValue("EXISTENCIA");
                            celda.setCellStyle(cellStyle);
                            
                            i = i + 1;
                            for (int j = 0; j <= 12; j++) {
                                hoja.autoSizeColumn(j);
                            }
                            while (rs.next()) {
                                  fila = hoja.createRow(i);
                            celda = fila.createCell(0);
                            celda.setCellValue(rs.getString("nombreCategoria"));
                            
                            celda = fila.createCell(1);
                            celda.setCellValue(rs.getString("nombreReferencia"));
                            
                            celda = fila.createCell(2);
                            celda.setCellValue(rs.getString("nombreMarca"));
                            
                            celda = fila.createCell(3);
                            celda.setCellValue(rs.getString("codigoInterno"));
                            
                            celda = fila.createCell(4);
                            celda.setCellValue(rs.getString("nomProducto"));
                            
                            celda = fila.createCell(5);
                            celda.setCellValue(rs.getLong("existencia"));
                            
                            
                            i = i + 1;
                            }

                        break;//stock
                        
                    case "productoClientes":
                            hoja = libro.createSheet("Ventas productos Clientes");
                            fila = hoja.createRow(i);
                            celda = fila.createCell(0);
                            celda.setCellValue("CLIENTE");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(1);
                            celda.setCellValue("PRODUCTO");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(2);
                            celda.setCellValue("CANTIDAD VENDIDA");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(3);
                            celda.setCellValue("FECHA ULTIMA VENTA");
                            celda.setCellStyle(cellStyle);

                            i = i + 1;
                            for (int j = 0; j <= 12; j++) {
                                hoja.autoSizeColumn(j);
                            }
                            while (rs.next()) {
                                  fila = hoja.createRow(i);
                            celda = fila.createCell(0);
                            celda.setCellValue(rs.getString("nombre"));
                            
                            celda = fila.createCell(1);
                            celda.setCellValue(rs.getString("producto"));
                            
                            celda = fila.createCell(2);
                            celda.setCellValue(rs.getDouble("total"));
                            
                            celda = fila.createCell(3);
                            celda.setCellValue(sd2.format(rs.getDate(6)));
                            
                            i = i + 1;
                            }

                        break;//productosClientes
                        case "cotizacion":
                            hoja = libro.createSheet("Ventas Cotizaciones");
                            fila = hoja.createRow(i);
                            celda = fila.createCell(0);
                            celda.setCellValue("CLIENTE");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(1);
                            celda.setCellValue("COTIZACION");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(2);
                            celda.setCellValue("FECHA");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(3);
                            celda.setCellValue("CODIGO PRODUCTO");
                            celda.setCellStyle(cellStyle);
                            
                            celda = fila.createCell(4);
                            celda.setCellValue("PRODUCTO");
                            celda.setCellStyle(cellStyle);
                            
                            celda = fila.createCell(5);
                            celda.setCellValue("PRECIO");
                            celda.setCellStyle(cellStyle);
                            
                            celda = fila.createCell(6);
                            celda.setCellValue("IVA");
                            celda.setCellStyle(cellStyle);
                            
                            celda = fila.createCell(7);
                            celda.setCellValue("TOTAL");
                            celda.setCellStyle(cellStyle);
                            
                            i = i + 1;
                            for (int j = 0; j <= 12; j++) {
                                hoja.autoSizeColumn(j);
                            }
                            while (rs.next()) {
                                  fila = hoja.createRow(i);
                                celda = fila.createCell(0);
                                celda.setCellValue(rs.getString("nombre"));
                            
                                celda = fila.createCell(1);
                                celda.setCellValue(rs.getString("cotizacion"));
                            
                                celda = fila.createCell(2);
                                celda.setCellValue(sd2.format(rs.getDate("fecCrea")));
                            
                                celda = fila.createCell(3);
                                celda.setCellValue(rs.getString("codigoInterno"));
                                
                                celda = fila.createCell(4);
                                celda.setCellValue(rs.getString("nomProducto"));
                                
                                celda = fila.createCell(5);
                                celda.setCellValue(rs.getDouble("precio"));
                                
                                celda = fila.createCell(6);
                                celda.setCellValue(rs.getString("iva"));
                                
                                celda = fila.createCell(7);
                                celda.setCellValue(rs.getString("total"));
                            
                                i = i + 1;
                            }

                        break;//cotizacion
                            
                case "vencimientosFacturas":
                        
                    hoja = libro.createSheet("Vencimiento Facturas");
                            fila = hoja.createRow(i);
                            celda = fila.createCell(0);
                            celda.setCellValue("DOCUMENTO");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(1);
                            celda.setCellValue("NUMERO IDENTIFICACION CLIENTE");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(2);
                            celda.setCellValue("CLIENTE");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(3);
                            celda.setCellValue("TELEFONO");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(4);
                            celda.setCellValue("VALOR");
                            celda.setCellStyle(cellStyle);
                            
                            celda = fila.createCell(5);
                            celda.setCellValue("SALDO");
                            celda.setCellStyle(cellStyle);
                            
                            celda = fila.createCell(6);
                            celda.setCellValue("FECHA VENCIMIENTO");
                            celda.setCellStyle(cellStyle);
                            i = i + 1;
                            for (int j = 0; j <= 12; j++) {
                                hoja.autoSizeColumn(j);
                            }
                            while (rs.next()) {
                                  fila = hoja.createRow(i);
                            celda = fila.createCell(0);
                            celda.setCellValue(rs.getString("documento"));
                            
                            celda = fila.createCell(1);
                            celda.setCellValue(rs.getString("numDoc"));
                            
                            celda = fila.createCell(2);
                            celda.setCellValue(rs.getString("cliente"));
                            
                            celda = fila.createCell(3);
                            celda.setCellValue(rs.getString("tel1"));
                            
                            celda = fila.createCell(4);
                            celda.setCellValue(rs.getDouble("valor"));
                            
                            celda = fila.createCell(5);
                            celda.setCellValue(rs.getDouble("saldo"));
                            
                            celda = fila.createCell(6);
                            celda.setCellValue(sd2.format(rs.getDate("fecha_limite")));
                            
                            i = i + 1;
                            }
                        break;//vencimientofactuas
                case "vencimientosSeparados":
                         hoja = libro.createSheet("Vencimiento Separados");
                            fila = hoja.createRow(i);
                            celda = fila.createCell(0);
                            celda.setCellValue("DOCUMENTO");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(1);
                            celda.setCellValue("NUMERO IDENTIFICACION CLIENTE");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(2);
                            celda.setCellValue("CLIENTE");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(3);
                            celda.setCellValue("TELEFONO");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(4);
                            celda.setCellValue("VALOR");
                            celda.setCellStyle(cellStyle);
                            
                            celda = fila.createCell(5);
                            celda.setCellValue("SALDO");
                            celda.setCellStyle(cellStyle);
                            
                            celda = fila.createCell(6);
                            celda.setCellValue("FECHA VENCIMIENTO");
                            celda.setCellStyle(cellStyle);
                            i = i + 1;
                            for (int j = 0; j <= 12; j++) {
                                hoja.autoSizeColumn(j);
                            }
                            while (rs.next()) {
                                  fila = hoja.createRow(i);
                            celda = fila.createCell(0);
                            celda.setCellValue(rs.getString("documento"));
                            
                            celda = fila.createCell(1);
                            celda.setCellValue(rs.getString("numDoc"));
                            
                            celda = fila.createCell(2);
                            celda.setCellValue(rs.getString("cliente"));
                            
                            celda = fila.createCell(3);
                            celda.setCellValue(rs.getString("tel1"));
                            
                            celda = fila.createCell(4);
                            celda.setCellValue(rs.getDouble("valor"));
                            
                            celda = fila.createCell(5);
                            celda.setCellValue(rs.getDouble("saldo"));
                            
                            celda = fila.createCell(6);
                            celda.setCellValue(sd2.format(rs.getDate("fecha_limite")));
                            
                            i = i + 1;
                            }
                        break;//vencimientosSeprados
                case "productosSinRotacion":
                         hoja = libro.createSheet("productos sin rotación");
                            fila = hoja.createRow(i);
                            celda = fila.createCell(0);
                            celda.setCellValue("CATEGORIA");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(1);
                            celda.setCellValue("REFERENCIA");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(2);
                            celda.setCellValue("MARCA");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(3);
                            celda.setCellValue("CODIGO INTERNO");
                            celda.setCellStyle(cellStyle);

                            celda = fila.createCell(4);
                            celda.setCellValue("PRODUCTO");
                            celda.setCellStyle(cellStyle);
                            
                            celda = fila.createCell(5);
                            celda.setCellValue("FECHA ULTIMO MOVIMIENTO");
                            celda.setCellStyle(cellStyle);
                            
                            
                            i = i + 1;
                            for (int j = 0; j <= 12; j++) {
                                hoja.autoSizeColumn(j);
                            }
                            while (rs.next()) {
                                  fila = hoja.createRow(i);
                            celda = fila.createCell(0);
                            celda.setCellValue(rs.getString("nombreCategoria"));
                            
                            celda = fila.createCell(1);
                            celda.setCellValue(rs.getString("nombreReferencia"));
                            
                            celda = fila.createCell(2);
                            celda.setCellValue(rs.getString("nombreMarca"));
                            
                            celda = fila.createCell(3);
                            celda.setCellValue(rs.getString("codigoInterno"));
                            
                            celda = fila.createCell(4);
                            celda.setCellValue(rs.getString("nomProducto"));
                            
                            celda = fila.createCell(5);
                            celda.setCellValue(sd2.format(rs.getDate("fechaultimo")));
                            
                            i = i + 1;
                            }
                        break;//ProductosSinRotacion
            }

            /*Escribimos en el libro*/
            libro.write(archi);
            /*Cerramos el flujo de datos*/
            archi.close();
            /*DESCARGAMOS EL ARCHIVO */
            File f;
            f = new File(rutaArchivo);
            int bit;
            InputStream in;
            ServletOutputStream out;
            response.setContentType("application/vnd.ms-excel"); //Tipo de fichero.
            response.setHeader("Content-Disposition", "attachment;filename=\"" + nombreArchivo + "\""); //Configurar cabecera http

            in = new FileInputStream(f);
            out = response.getOutputStream();

            bit = 256;
            while ((bit) >= 0) {
                bit = in.read();
                out.write(bit);
            }

            out.flush();
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
                DBConnector.getInstance().closeConnection();
            } catch (Exception e) {
            }
        }

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doGet(request, response);
    }
}
