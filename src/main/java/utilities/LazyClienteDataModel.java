/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import entities.CfgCliente;
import entities.CfgEmpresa;
import java.util.List;
import java.util.ArrayList;
import facades.CfgClienteFacade;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author mario
 */
public class LazyClienteDataModel extends LazyDataModel<CfgCliente> {

    private final CfgClienteFacade clienteFacade;
    private final CfgEmpresa empresa;

    public LazyClienteDataModel(CfgClienteFacade clienteFacade, CfgEmpresa empresa) {
        this.clienteFacade = clienteFacade;
        this.empresa = empresa;
    }

    @Override
    public Object getRowKey(CfgCliente cfgCliente) {
        return cfgCliente.getIdCliente();
    }

    @Override
    public CfgCliente getRowData(String rowKey) {
        return clienteFacade.find(Integer.parseInt(rowKey));
    }

    @Override
    public List<CfgCliente> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        //filter
        List<CfgCliente> data;
        data = loadClientes(first, pageSize, filters);
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

    private List<CfgCliente> loadClientes(int first, int pageSize, Map<String, Object> filters) {
        List<CfgCliente> data = new ArrayList();
        if (filters.isEmpty()) {
            data = clienteFacade.lazyCliente(empresa, first, pageSize);
        }
        this.setRowCount(clienteFacade.totaClientesByEmpresa(empresa));
        return data;
    }

}
