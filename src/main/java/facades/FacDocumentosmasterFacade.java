/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgCliente;
import entities.CfgEmpresasede;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import entities.FacDocumentosmaster;
import entities.SegUsuario;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;

/**
 *
 * @author mario
 */
@Stateless
public class FacDocumentosmasterFacade extends AbstractFacade<FacDocumentosmaster> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacDocumentosmasterFacade() {
        super(FacDocumentosmaster.class);
    }

    public FacDocumentosmaster buscarBySedeAndDocumentoAndNum(CfgEmpresasede sede, long documento, long num) {
        try {
            Query query = em.createQuery("SELECT d FROM FacDocumentosmaster d WHERE d.cfgempresasedeidSede = ?1 AND d.facDocumentosmasterPK.cfgdocumentoidDoc = ?2 AND d.facDocumentosmasterPK.numDocumento = ?3");
            query.setParameter(1, sede);
            query.setParameter(2, documento);
            query.setParameter(3, num);
            return (FacDocumentosmaster) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<FacDocumentosmaster> buscarBySede(CfgEmpresasede sede) {
        try {
            Query query = em.createQuery("SELECT d FROM FacDocumentosmaster d WHERE d.cfgempresasedeidSede = ?1");
            query.setParameter(1, sede);
            return query.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

//documentos de facturacion
    public List<FacDocumentosmaster> buscarFacturasBySedeLazy(CfgEmpresasede sede, CfgCliente cliente, int numFactura, Calendar fechaIni, Calendar fechafin, int offset, int limit, int tipoFacturacion) {
        try {
            String consulta = "SELECT d FROM FacDocumentosmaster d WHERE d.cfgempresasedeidSede = ?1 AND d.cfgDocumento.cfgAplicaciondocumentoIdaplicacion.codaplicacion =?2 ";
            
            
            if (cliente != null) {
                consulta = consulta.concat(" AND d.cfgclienteidCliente = ?3");
            }
            if (numFactura > 0) {
                consulta = consulta.concat(" AND d.facDocumentosmasterPK.numDocumento = ?4");
            }
            if (fechaIni != null) {
                consulta = consulta.concat(" AND d.fecCrea >= ?5");
            }
            if (fechafin != null) {
                consulta = consulta.concat(" AND d.fecCrea <= ?6");
            }
//            System.out.println(fechaIni +"-"+fechafin );
//            System.out.println(consulta);
            Query query = em.createQuery(consulta);
            query.setParameter(1, sede);
            query.setParameter(2, String.valueOf(tipoFacturacion));
            if (cliente != null) {
                query.setParameter(3, cliente);
            }
            if (numFactura > 0) {
                query.setParameter(4, numFactura);
            }
            if (fechaIni != null) {
                fechaIni.set(Calendar.HOUR_OF_DAY, 0);
                query.setParameter(5, fechaIni.getTime());
            }
            if (fechafin != null) {
                query.setParameter(6, fechafin.getTime());
            }
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//total documentos de facturacion
    public int totalFacturasBySede(CfgEmpresasede sede, CfgCliente cliente, int numFactura, Calendar fechaIni, Calendar fechafin) {
        try {
            String consulta = "SELECT COUNT(d.facDocumentosmasterPK) FROM FacDocumentosmaster d WHERE d.cfgempresasedeidSede = ?1 AND d.cfgDocumento.cfgAplicaciondocumentoIdaplicacion.codaplicacion LIKE '1'";
            if (cliente != null) {
                consulta = consulta.concat(" AND d.cfgclienteidCliente = ?2");
            }
            if (numFactura > 0) {

                consulta = consulta.concat(" AND d.facDocumentosmasterPK.numDocumento = ?3");
            }
            if (fechaIni != null) {
                consulta = consulta.concat(" AND d.fecCrea >= ?4");
            }
            if (fechafin != null) {
                consulta = consulta.concat(" AND d.fecCrea <= ?5");
            }
//            System.out.println(consulta);
            Query query = em.createQuery(consulta);
            query.setParameter(1, sede);
            if (cliente != null) {
                query.setParameter(2, cliente);
            }
            if (numFactura > 0) {
                query.setParameter(3, numFactura);
            }
            if (fechaIni != null) {
                query.setParameter(4, fechaIni.getTime());
            }
            if (fechafin != null) {
                query.setParameter(5, fechafin.getTime());
            }
            return Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }

//documentos de facturacion
    public List<FacDocumentosmaster> buscarFacturasEspecialBySedeLazy(CfgEmpresasede sede, CfgCliente cliente, int numFactura, Calendar fechaIni, Calendar fechafin, int offset, int limit) {
        try {
            String consulta = "SELECT d FROM FacDocumentosmaster d WHERE d.cfgempresasedeidSede = ?1 AND d.cfgDocumento.cfgAplicaciondocumentoIdaplicacion.codaplicacion LIKE '6'";
            if (cliente != null) {
                consulta = consulta.concat(" AND d.cfgclienteidCliente = ?2");
            }
            if (numFactura > 0) {
                consulta = consulta.concat(" AND d.facDocumentosmasterPK.numDocumento = ?3");
            }
            if (fechaIni != null) {
                consulta = consulta.concat(" AND d.fecCrea >= ?4");
            }
            if (fechafin != null) {
                consulta = consulta.concat(" AND d.fecCrea <= ?5");
            }
//            System.out.println(fechaIni +"-"+fechafin );
//            System.out.println(consulta);
            Query query = em.createQuery(consulta);
            query.setParameter(1, sede);
            if (cliente != null) {
                query.setParameter(2, cliente);
            }
            if (numFactura > 0) {
                query.setParameter(3, numFactura);
            }
            if (fechaIni != null) {
                query.setParameter(4, fechaIni.getTime());
            }
            if (fechafin != null) {
                query.setParameter(5, fechafin.getTime());
            }
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

//total documentos de facturacion Especial
    public int totalFacturasEspecialBySede(CfgEmpresasede sede, CfgCliente cliente, int numFactura, Calendar fechaIni, Calendar fechafin) {
        try {
            String consulta = "SELECT COUNT(d.facDocumentosmasterPK) FROM FacDocumentosmaster d WHERE d.cfgempresasedeidSede = ?1 AND d.cfgDocumento.cfgAplicaciondocumentoIdaplicacion.codaplicacion LIKE '6'";
            if (cliente != null) {
                consulta = consulta.concat(" AND d.cfgclienteidCliente = ?2");
            }
            if (numFactura > 0) {

                consulta = consulta.concat(" AND d.facDocumentosmasterPK.numDocumento = ?3");
            }
            if (fechaIni != null) {
                consulta = consulta.concat(" AND d.fecCrea >= ?4");
            }
            if (fechafin != null) {
                consulta = consulta.concat(" AND d.fecCrea <= ?5");
            }
//            System.out.println(consulta);
            Query query = em.createQuery(consulta);
            query.setParameter(1, sede);
            if (cliente != null) {
                query.setParameter(2, cliente);
            }
            if (numFactura > 0) {
                query.setParameter(3, numFactura);
            }
            if (fechaIni != null) {
                query.setParameter(4, fechaIni.getTime());
            }
            if (fechafin != null) {
                query.setParameter(5, fechafin.getTime());
            }
            return Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }
//documentos de cotizacion

    public List<FacDocumentosmaster> buscarCotizacionesBySedeLazy(CfgEmpresasede sede, CfgCliente cliente, int offset, int limit) {
        try {
            String consulta = "SELECT d FROM FacDocumentosmaster d WHERE d.cfgempresasedeidSede = ?1 AND d.cfgDocumento.cfgAplicaciondocumentoIdaplicacion.codaplicacion LIKE '5' AND d.estado LIKE 'PENDIENTE'";
            if (cliente != null) {
                consulta = consulta.concat(" AND d.cfgclienteidCliente = ?2");
            }
            Query query = em.createQuery(consulta);
            query.setParameter(1, sede);
            if (cliente != null) {
                query.setParameter(2, cliente);
            }
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
//total documentos de cotizacion

    public int totalCotizacionesBySede(CfgEmpresasede sede, CfgCliente cliente) {
        try {
            String consulta = "SELECT COUNT(d.facDocumentosmasterPK) FROM FacDocumentosmaster d WHERE d.cfgempresasedeidSede = ?1 AND d.cfgDocumento.cfgAplicaciondocumentoIdaplicacion.codaplicacion LIKE '5' AND d.estado LIKE 'PENDIENTE'";
            if (cliente != null) {
                consulta = consulta.concat(" AND d.cfgclienteidCliente = ?2");
            }
//            System.out.println(consulta);
            Query query = em.createQuery(consulta);
            query.setParameter(1, sede);
            if (cliente != null) {
                query.setParameter(2, cliente);
            }
            return Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    //recupera las facturas normales y las especiales que estan canceladas (PAGADAS)
    public List<FacDocumentosmaster> buscarFacturasToReporte(CfgEmpresasede sede, CfgCliente cliente, SegUsuario vendedor, Date fechaIni, Date fechafin) {
        try {
            String consulta = "SELECT d FROM FacDocumentosmaster d WHERE d.cfgempresasedeidSede = ?1 AND (d.cfgDocumento.cfgAplicaciondocumentoIdaplicacion.codaplicacion LIKE '1' OR d.cfgDocumento.cfgAplicaciondocumentoIdaplicacion.codaplicacion LIKE '6') AND d.estado LIKE 'CANCELADA'";
            if (cliente != null) {
                consulta = consulta.concat(" AND d.cfgclienteidCliente = ?2");
            }
            if (vendedor != null) {
                consulta = consulta.concat(" AND d.segusuarioidUsuario1 = ?3");
            }
            if (fechaIni != null) {
                consulta = consulta.concat(" AND d.fecCrea >= ?4");
            }
            if (fechafin != null) {
                consulta = consulta.concat(" AND d.fecCrea <= ?5");
            }
            consulta = consulta.concat(" ORDER BY d.fecCrea");
            Query query = em.createQuery(consulta);
            query.setParameter(1, sede);
            if (cliente != null) {
                query.setParameter(2, cliente);
            }
            if (vendedor != null) {
                query.setParameter(3, vendedor);
            }
            if (fechaIni != null) {
                query.setParameter(4, fechaIni);
            }
            if (fechafin != null) {
                query.setParameter(5, fechafin);
            }
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    //se utiliza cuando se busca un documento que referencia a otro. Ejm un recibo de caja puede contener un separado. Una factura puede contener un separado
    public FacDocumentosmaster buscarPorDocumentoMasterReflexivo(FacDocumentosmaster documentoReflexivo) {
        try {
            Query query = em.createQuery("SELECT d FROM FacDocumentosmaster d WHERE d.facDocumentosmaster = ?1");
            query.setParameter(1, documentoReflexivo);
            return (FacDocumentosmaster) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    //se busca la factura que referencia a un separado
    public FacDocumentosmaster buscarFacturaSeparado(FacDocumentosmaster separado) {
        try {
            Query query = em.createQuery("SELECT d FROM FacDocumentosmaster d WHERE d.facDocumentosmaster = ?1 AND d.cfgDocumento.cfgAplicaciondocumentoIdaplicacion.codaplicacion LIKE '1'");
            query.setParameter(1, separado);
            return (FacDocumentosmaster) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<FacDocumentosmaster> buscarSeparados(CfgEmpresasede sede, Date fechaIncial, Date fechaFinal, CfgCliente cliente, int numSeparado) {
        try {
            Query query;
            String consulta = "SELECT d FROM FacDocumentosmaster d WHERE d.cfgempresasedeidSede = ?1 AND d.cfgDocumento.cfgAplicaciondocumentoIdaplicacion.codaplicacion LIKE '7'";
//            query = em.createQuery("SELECT d FROM FacDocumentosmaster d WHERE d.cfgempresasedeidSede = ?1 AND d.cfgDocumento.cfgAplicaciondocumentoIdaplicacion.codaplicacion LIKE '7' AND d.fecCrea >= ?2 AND d.fecCrea <= ?3 AND d.cfgclienteidCliente = ?4 AND d.facDocumentosmasterPK.numDocumento = ?5 ORDER BY d.fecCrea, d.facDocumentosmasterPK");
            if (fechaIncial != null) {
                consulta = consulta.concat(" AND d.fecCrea >= ?2");
            }
            if (fechaFinal != null) {
                consulta = consulta.concat(" AND d.fecCrea <= ?3");
            }
            if (cliente != null) {
                consulta = consulta.concat(" AND d.cfgclienteidCliente = ?4");
            }
            if (numSeparado > 0) {
                consulta = consulta.concat(" AND d.facDocumentosmasterPK.numDocumento = ?5");
            }
            consulta = consulta.concat(" ORDER BY d.fecCrea, d.facDocumentosmasterPK");
            query = em.createQuery(consulta);
            query.setParameter(1, sede);
            if (fechaIncial != null) {
                query.setParameter(2, fechaIncial);
            }
            if (fechaFinal != null) {
                query.setParameter(3, fechaFinal);
            }
            if (cliente != null) {
                query.setParameter(4, cliente);
            }
            if (numSeparado > 0) {
                query.setParameter(5, numSeparado);
            }
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public int totalDocumentosPorAplicacionAndRango(int idDoc, int ini, int fin) {
        try {
            Query query = em.createQuery("SELECT COUNT(d.facDocumentosmasterPK) FROM FacDocumentosmaster d WHERE d.facDocumentosmasterPK.cfgdocumentoidDoc = ?1 AND d.facDocumentosmasterPK.numDocumento >= ?2 AND d.facDocumentosmasterPK.numDocumento <= ?3");
            query.setParameter(1, idDoc);
            query.setParameter(2, ini);
            query.setParameter(3, fin);
            return Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }
}
