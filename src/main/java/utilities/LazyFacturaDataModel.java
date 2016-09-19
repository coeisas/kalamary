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
import org.primefaces.model.LazyDataModel;
import java.util.List;
import java.util.Calendar;
import java.util.Map;
import org.primefaces.model.SortOrder;

/**
 *
 * @author mario
 */
public class LazyFacturaDataModel extends LazyDataModel<FacDocumentosmaster> {

    private final FacDocumentosmasterFacade documentosmasterFacade;
    private final CfgEmpresasede sede;
    private CfgCliente cliente;
    private Calendar fechaIncial;
    private Calendar fechaFinal;
    private int numFactura;
    private int tipoFacturacion;

    public LazyFacturaDataModel(FacDocumentosmasterFacade documentosmasterFacade, CfgEmpresasede sede) {
        this.documentosmasterFacade = documentosmasterFacade;
        this.sede = sede;
    }

    public LazyFacturaDataModel(FacDocumentosmasterFacade documentosmasterFacade, CfgEmpresasede sede, CfgCliente cliente, Calendar fechaIncial, Calendar fechaFinal, int numFactura,int tipoFacturacion) {
        this.documentosmasterFacade = documentosmasterFacade;
        this.sede = sede;
        this.cliente = cliente;
        this.fechaIncial = fechaIncial;
        this.fechaFinal = fechaFinal;
        if (fechaIncial != null) {
            this.fechaIncial.add(Calendar.HOUR, 0);//se incremente un dia por en la base de datos se guarda con hora y el valor enviado es a las 0 horas
        }
        if (fechaFinal != null) {
            this.fechaFinal.add(Calendar.DATE, 1);//se incremente un dia por en la base de datos se guarda con hora y el valor enviado es a las 0 horas
        }
        this.numFactura = numFactura;
        this.tipoFacturacion = tipoFacturacion;
    }

    @Override
    public Object getRowKey(FacDocumentosmaster documentosmaster) {
        return documentosmaster.getFacDocumentosmasterPK();
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
        data = documentosmasterFacade.buscarFacturasBySedeLazy(sede, cliente, numFactura, fechaIncial, fechaFinal, first, pageSize,tipoFacturacion);
        this.setRowCount(documentosmasterFacade.totalFacturasBySede(sede, cliente, numFactura, fechaIncial, fechaFinal));
        return data;
    }

}
