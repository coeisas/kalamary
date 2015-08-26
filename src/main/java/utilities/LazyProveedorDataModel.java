/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import entities.CfgCliente;
import entities.CfgEmpresa;
import entities.CfgProveedor;
import facades.CfgClienteFacade;
import facades.CfgProveedorFacade;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author mario
 */
public class LazyProveedorDataModel extends LazyDataModel<CfgProveedor> {

    private final CfgProveedorFacade proveedorFacade;
    private final CfgEmpresa empresa;

    public LazyProveedorDataModel(CfgProveedorFacade proveedorFacade, CfgEmpresa empresa) {
        this.proveedorFacade = proveedorFacade;
        this.empresa = empresa;
    }

    @Override
    public Object getRowKey(CfgProveedor cfgProveedor) {
        return cfgProveedor.getIdProveedor();
    }

    @Override
    public CfgProveedor getRowData(String rowKey) {
        return proveedorFacade.find(Long.parseLong(rowKey));
    }

    @Override
    public List<CfgProveedor> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        //filter
        List<CfgProveedor> data;
        data = loadProveedor(first, pageSize, filters);
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

    private List<CfgProveedor> loadProveedor(int first, int pageSize, Map<String, Object> filters) {
        List<CfgProveedor> data = new ArrayList();
        if (filters.isEmpty()) {
            data = proveedorFacade.lazyCliente(empresa, first, pageSize);
        }
        this.setRowCount(proveedorFacade.totaProveedoresByEmpresa(empresa));
        return data;
    }
}
