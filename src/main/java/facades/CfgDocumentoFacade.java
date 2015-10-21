/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgDocumento;
import entities.CfgEmpresasede;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author mario
 */
@Stateless
public class CfgDocumentoFacade extends AbstractFacade<CfgDocumento> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgDocumentoFacade() {
        super(CfgDocumento.class);
    }

    public CfgDocumento buscarDocumentoPorSedeAndCodigo(CfgEmpresasede sede, String codigo) {
        try {
            Query query = em.createQuery("SELECT d FROM CfgDocumento d WHERE d.cfgempresasedeidSede = ?1 AND d.codDocumento = ?2");
            query.setParameter(1, sede);
            query.setParameter(2, codigo);
            return (CfgDocumento) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CfgDocumento> buscarDocumentoPorSede(CfgEmpresasede sede) {
        try {
            Query query = em.createQuery("SELECT d FROM CfgDocumento d WHERE d.cfgempresasedeidSede = ?1");
            query.setParameter(1, sede);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgDocumento buscarAplicacionDocumentoPorSede(CfgEmpresasede sede, int idaplicacion) {
        try {
            Query query = em.createQuery("SELECT d FROM CfgDocumento d WHERE d.cfgempresasedeidSede = ?1 AND d.cfgAplicaciondocumentoIdaplicacion.idaplicacion = ?2 AND d.finalizado =  FALSE");
            query.setParameter(1, sede);
            query.setParameter(2, idaplicacion);
            return (CfgDocumento) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgDocumento buscarDocumentoDeFacturaBySede(CfgEmpresasede sede) {
        try {
            Query query = em.createQuery("SELECT d FROM CfgDocumento d WHERE d.cfgempresasedeidSede = ?1 AND d.cfgAplicaciondocumentoIdaplicacion.codaplicacion LIKE '1' AND d.finalizado = FALSE AND d.activo = TRUE");
            query.setParameter(1, sede);
            return (CfgDocumento) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgDocumento buscarDocumentoDeRemisionEspecialBySede(CfgEmpresasede sede) {
        try {
            Query query = em.createQuery("SELECT d FROM CfgDocumento d WHERE d.cfgempresasedeidSede = ?1 AND d.cfgAplicaciondocumentoIdaplicacion.codaplicacion LIKE '6' AND d.finalizado = FALSE AND d.activo = TRUE");
            query.setParameter(1, sede);
            return (CfgDocumento) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgDocumento buscarDocumentoDeSeparadoBySede(CfgEmpresasede sede) {
        try {
            Query query = em.createQuery("SELECT d FROM CfgDocumento d WHERE d.cfgempresasedeidSede = ?1 AND d.cfgAplicaciondocumentoIdaplicacion.codaplicacion LIKE '7' AND d.finalizado = FALSE AND d.activo = TRUE");
            query.setParameter(1, sede);
            return (CfgDocumento) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgDocumento buscarDocumentoDeCotizacionBySede(CfgEmpresasede sede) {
        try {
            Query query = em.createQuery("SELECT d FROM CfgDocumento d WHERE d.cfgempresasedeidSede = ?1 AND d.cfgAplicaciondocumentoIdaplicacion.codaplicacion LIKE '5' AND d.finalizado = FALSE AND d.activo = TRUE");
            query.setParameter(1, sede);
            return (CfgDocumento) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    //usado para ingresos y Abonos
    public CfgDocumento buscarDocumentoDeReciboCajaBySede(CfgEmpresasede sede) {
        try {
            Query query = em.createQuery("SELECT d FROM CfgDocumento d WHERE d.cfgempresasedeidSede = ?1 AND d.cfgAplicaciondocumentoIdaplicacion.codaplicacion LIKE '2' AND d.finalizado = FALSE AND d.activo = TRUE");
            query.setParameter(1, sede);
            return (CfgDocumento) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgDocumento buscarDocumentoDeEgresoCajaBySede(CfgEmpresasede sede) {
        try {
            Query query = em.createQuery("SELECT d FROM CfgDocumento d WHERE d.cfgempresasedeidSede = ?1 AND d.cfgAplicaciondocumentoIdaplicacion.codaplicacion LIKE '9' AND d.finalizado = FALSE AND d.activo = TRUE");
            query.setParameter(1, sede);
            return (CfgDocumento) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgDocumento buscarDocumentoInventarioSalidaBySede(CfgEmpresasede sede) {
        try {
            Query query = em.createQuery("SELECT d FROM CfgDocumento d WHERE d.cfgempresasedeidSede = ?1 AND d.cfgAplicaciondocumentoIdaplicacion.codaplicacion LIKE '4' AND d.finalizado = FALSE AND d.activo = TRUE");
            query.setParameter(1, sede);
            return (CfgDocumento) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public CfgDocumento buscarDocumentoInventarioEntradaBySede(CfgEmpresasede sede) {
        try {
            Query query = em.createQuery("SELECT d FROM CfgDocumento d WHERE d.cfgempresasedeidSede = ?1 AND d.cfgAplicaciondocumentoIdaplicacion.codaplicacion LIKE '3' AND d.finalizado = FALSE AND d.activo = TRUE");
            query.setParameter(1, sede);
            return (CfgDocumento) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }    

    public CfgDocumento buscarDocumentoDeMovInventarioBySede(CfgEmpresasede sede, String codMov) {
        try {
            Query query = em.createQuery("SELECT d FROM CfgDocumento d WHERE d.cfgempresasedeidSede = ?1 AND d.cfgAplicaciondocumentoIdaplicacion.codaplicacion LIKE ?2 AND d.finalizado = FALSE AND d.activo = TRUE");
            query.setParameter(1, sede);
            query.setParameter(2, codMov);
            return (CfgDocumento) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
