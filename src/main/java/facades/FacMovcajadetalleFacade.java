/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgCliente;
import entities.CfgEmpresasede;
import entities.FacDocumentosmaster;
import entities.FacMovcaja;
import entities.FacMovcajadetalle;
import entities.SegUsuario;
import java.util.Date;
import javax.ejb.Stateless;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author mario
 */
@Stateless
public class FacMovcajadetalleFacade extends AbstractFacade<FacMovcajadetalle> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacMovcajadetalleFacade() {
        super(FacMovcajadetalle.class);
    }

    public List<FacMovcajadetalle> buscarMovDetallePorMovCaja(FacMovcaja movcaja) {
        try {
            Query query = em.createQuery("SELECT mcd FROM FacMovcajadetalle mcd WHERE mcd.facMovcaja = ?1");
            query.setParameter(1, movcaja);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<FacMovcajadetalle> buscarMovDetallePorMovCajaAndEfectivo(FacMovcaja movcaja) {
        try {
            Query query = em.createQuery("SELECT mcd FROM FacMovcajadetalle mcd WHERE mcd.facMovcaja = ?1 AND mcd.cfgFormapago.codFormaPago LIKE '1'");
            query.setParameter(1, movcaja);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<FacMovcajadetalle> buscarMovDetalleParaInforme(CfgEmpresasede sede, Date fechaInicial, Date fechaFinal, CfgCliente cliente, SegUsuario usuario) {
        try {
            Query query;
            String consulta = "SELECT mcd FROM FacMovcajadetalle mcd WHERE mcd.facMovcaja.faccajaidCaja.cfgempresasedeidSede = ?1";
            if (fechaInicial != null) {
                consulta = consulta.concat(" AND mcd.facMovcajadetallePK.fecha >= ?2");
            }
            if (fechaFinal != null) {
                consulta = consulta.concat(" AND mcd.facMovcajadetallePK.fecha <= ?3");
            }
            if (cliente != null) {
                consulta = consulta.concat(" AND mcd.facDocumentosmaster.cfgclienteidCliente = ?4");
            }
            if (usuario != null) {
                consulta = consulta.concat(" AND (mcd.facDocumentosmaster.segusuarioidUsuario1 = ?5 AND mcd.facDocumentosmaster.cfgDocumento.cfgAplicaciondocumentoIdaplicacion.codaplicacion IN ('2', '9'))");
            }
            consulta = consulta.concat(" ORDER BY mcd.facMovcajadetallePK.fecha");
            query = em.createQuery(consulta);
//            query = em.createQuery("SELECT mcd FROM FacMovcajadetalle mcd WHERE mcd.facMovcaja.faccajaidCaja.cfgempresasedeidSede = ?1 AND mcd.fecha >= ?2 AND mcd.fecha <= ?3 AND mcd.facDocumentosmaster.cfgclienteidCliente = ?4 AND (mcd.facDocumentosmaster.segusuarioidUsuario1 = ?5 AND mcd.facDocumentosmaster.cfgDocumento.cfgAplicaciondocumentoIdaplicacion.codaplicacion IN ('2', '9')) ORDER BY mcd.fecha");
            query.setParameter(1, sede);
            if (fechaInicial != null) {
                query.setParameter(2, fechaInicial);
            }
            if (fechaFinal != null) {
                query.setParameter(3, fechaFinal);
            }
            if (cliente != null) {
                query.setParameter(4, cliente);
            }
            if (usuario != null) {
                query.setParameter(5, usuario);
            }
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    //usado en anular factura
    public FacMovcaja findByDocumentoMaestro(FacDocumentosmaster docmaestro) {
        try {
            Query query = em.createQuery("SELECT fmd.facMovcaja FROM FacMovcajadetalle fmd WHERE fmd.facDocumentosmaster = ?1", FacMovcaja.class);
            query.setParameter(1, docmaestro);
            query.setMaxResults(1);
            FacMovcaja fmc = (FacMovcaja) query.getSingleResult();
            return fmc;
        } catch (Exception e) {
            return null;
        }
    }
}
