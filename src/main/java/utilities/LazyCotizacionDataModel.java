/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import entities.CfgCliente;
import entities.CfgEmpresasede;
import entities.FacDocumentosmaster;
import facades.FacDocumentosmasterFacade;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author mario
 */
public class LazyCotizacionDataModel extends LazyDataModel<FacDocumentosmaster> {

    private final FacDocumentosmasterFacade documentosmasterFacade;
    private final CfgEmpresasede sede;
    private CfgCliente cliente;

    public LazyCotizacionDataModel(FacDocumentosmasterFacade documentosmasterFacade, CfgEmpresasede sede) {
        this.documentosmasterFacade = documentosmasterFacade;
        this.sede = sede;
    }

    public LazyCotizacionDataModel(FacDocumentosmasterFacade documentosmasterFacade, CfgEmpresasede sede, CfgCliente cliente) {
        this.documentosmasterFacade = documentosmasterFacade;
        this.sede = sede;
        this.cliente = cliente;
    }

    @Override
    public Object getRowKey(FacDocumentosmaster documentosmaster) {
        return documentosmaster.getFacDocumentosmasterPK();
    }

    @Override
    public FacDocumentosmaster getRowData(String key) {
        String[] aux = key.split(",");
        Long documento = Long.parseLong(aux[0]);
        Long numDoc = Long.parseLong(aux[1]);
        return documentosmasterFacade.buscarBySedeAndDocumentoAndNum(sede, documento, numDoc);
    }

    @Override
    public List<FacDocumentosmaster> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        //filter
        List<FacDocumentosmaster> data;
        data = loadDocuementoMaster(first, pageSize, filters);
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

    private List<FacDocumentosmaster> loadDocuementoMaster(int first, int pageSize, Map<String, Object> filters) {
        List<FacDocumentosmaster> data;
        data = documentosmasterFacade.buscarCotizacionesBySedeLazy(sede, cliente, first, pageSize);
        this.setRowCount(documentosmasterFacade.totalCotizacionesBySede(sede, cliente));
        return data;
    }

}
