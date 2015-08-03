/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import entities.CfgCategoriaproducto;
import entities.CfgEmpresa;
import entities.CfgMarcaproducto;
import entities.CfgProducto;
import entities.CfgReferenciaproducto;
import facades.CfgProductoFacade;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author mario
 */
public class LazyProductosModel extends LazyDataModel<CfgProducto> {

    private final CfgEmpresa empresa;
    private final CfgMarcaproducto marcaproducto;
    private final CfgReferenciaproducto referenciaproducto;
    private final CfgCategoriaproducto categoriaproducto;
    private final CfgProductoFacade productoFacade;

    public LazyProductosModel(CfgEmpresa empresa, CfgMarcaproducto marcaproducto, CfgReferenciaproducto referenciaproducto, CfgCategoriaproducto categoriaproducto, CfgProductoFacade productoFacade) {
        this.empresa = empresa;
        this.marcaproducto = marcaproducto;
        this.referenciaproducto = referenciaproducto;
        this.categoriaproducto = categoriaproducto;
        this.productoFacade = productoFacade;
    }

    @Override
    public CfgProducto getRowData(String rowKey) {
        return productoFacade.find(Integer.parseInt(rowKey));
    }

    @Override
    public Object getRowKey(CfgProducto producto) {
        return producto.getIdProducto();
    }

    @Override
    public List<CfgProducto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        //filter
        List<CfgProducto> data;
        if (filters.isEmpty()) {
            data = loadProductos(first, pageSize);
        } else {
            data = loadProductos(first, pageSize, filters);
        }
        //rowCount
        int dataSize = data.size();

        //paginate
        if (dataSize > pageSize) {
            try {
                return data.subList(first, first + pageSize);
            } catch (IndexOutOfBoundsException e) {
                return data.subList(first, first + (dataSize % pageSize));
            }
        } else {
            return data;
        }
    }

    private List<CfgProducto> loadProductos(int first, int pageSize) {
        List<CfgProducto> data = new ArrayList();
        if (empresa != null) {
            if (marcaproducto != null) {
                this.setRowCount(productoFacade.totalProductosPorEmpresaAndMarca(empresa, marcaproducto));
                data = productoFacade.buscarPorEmpresaAndMarca(empresa, marcaproducto, first, pageSize);
            } else if (referenciaproducto != null) {
                this.setRowCount(productoFacade.TotalProductosPorEmpresaAndReferencia(empresa, referenciaproducto));
                data = productoFacade.buscarPorEmpresaAndReferencia(empresa, referenciaproducto, first, pageSize);
            } else if (categoriaproducto != null) {
                this.setRowCount(productoFacade.totalProductosPorEmpresaAndCategoria(empresa, categoriaproducto));
                data = productoFacade.buscarPorEmpresaAndCategoria(empresa, categoriaproducto, first, pageSize);
            } else {
                this.setRowCount(productoFacade.TotalProductosPorEmpresa(empresa));
                data = productoFacade.buscarPorEmpresa(empresa, first, pageSize);
            }
            return data;
        } else {
            return data;
        }
    }

    private List<CfgProducto> loadProductos(int first, int pageSize, Map<String, Object> filters) {
        List<CfgProducto> data = new ArrayList();
        if (empresa != null) {
            String sqlFilters = "";
            String codProducto = "";
            String nomProducto = "";
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String filterProperty = entry.getKey();                
                switch (filterProperty) {
                    case "codProducto":                        
                        codProducto = entry.getValue().toString().trim();
                        if(!codProducto.isEmpty()){
                            sqlFilters = sqlFilters.concat(" AND p.codProducto LIKE CONCAT(?2, '%')");
                        }
                        break;
                    case "nomProducto":
                        nomProducto = entry.getValue().toString().trim();
                        if(!nomProducto.isEmpty()){
                            sqlFilters = sqlFilters.concat(" AND p.nomProducto LIKE CONCAT('%', ?3, '%')");
                        }
                        break;
                }
            }
            data = productoFacade.buscarPorEmpresaFilter(empresa, first, pageSize, sqlFilters, codProducto, nomProducto);
            this.setRowCount(productoFacade.totalProductosPorEmpresaFilter(empresa, sqlFilters, codProducto, nomProducto));
        }
        return data;
    }

}
