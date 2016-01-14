/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CntMovdetalle;
import entities.FacDocumentosmaster;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Mario
 */
@Stateless
public class CntMovdetalleFacade extends AbstractFacade<CntMovdetalle> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CntMovdetalleFacade() {
        super(CntMovdetalle.class);
    }
    
    public List<CntMovdetalle> buscarPorDocumentoMaster(FacDocumentosmaster documentomaster){
        try {
            Query query = em.createQuery("SELECT c FROM CntMovdetalle c WHERE c.facDocumentosmaster = ?1");
            query.setParameter(1, documentomaster);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CntMovdetalle> busquedaParaInformeDiario(int idSede, Date fechaIncial, Date fechaFinal, String cuenta) {
        try {
            String consulta = "SELECT c FROM CntMovdetalle c WHERE c.cfgempresasedeidSede.idSede = ?1";
//            Query query = em.createQuery("SELECT c FROM CntMovdetalle c WHERE c.cfgempresasedeidSede.idSede = ?1 AND c.fecha >= ?2 AND c.fecha <= ?3 AND c.cntpuccodigoCuenta.codigoCuenta LIKE ?4");
            if (fechaIncial != null) {
                consulta = consulta.concat(" AND c.fecha >= ?2");
            }
            if (fechaFinal != null) {
                consulta = consulta.concat(" AND c.fecha <= ?3");
            }
            if (cuenta != null && !cuenta.isEmpty()) {
                consulta = consulta.concat(" AND c.cntpuccodigoCuenta.codigoCuenta LIKE ?4");
            }
            Query query = em.createQuery(consulta);
            query.setParameter(1, idSede);
            if (fechaIncial != null) {
                query.setParameter(2, fechaIncial);
            }
            if (fechaFinal != null) {
                query.setParameter(3, fechaFinal);
            }
            if (cuenta != null && !cuenta.isEmpty()) {
                query.setParameter(4, cuenta);
            }
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Object[]> busquedaParaInformeMayor(int idSede, Date fechaIncial, Date fechaFinal, String cuenta) {
        try {
            String consulta = "SELECT SUBSTRING(c.cntpuccodigoCuenta.codigoCuenta, 1 , 4), c.cntpuccodigoCuenta.nombreCuentas, SUM(c.debito), SUM(c.credito) FROM CntMovdetalle c WHERE c.cfgempresasedeidSede.idSede = ?1";
            if (fechaIncial != null) {
                consulta = consulta.concat(" AND c.fecha >= ?2");
            }
            if (fechaFinal != null) {
                consulta = consulta.concat(" AND c.fecha <= ?3");
            }
            if (cuenta != null && !cuenta.isEmpty()) {
                consulta = consulta.concat(" AND SUBSTRING(c.cntpuccodigoCuenta.codigoCuenta, 1 , 4) LIKE ?4");
            }
            consulta = consulta.concat(" GROUP BY SUBSTRING(c.cntpuccodigoCuenta.codigoCuenta, 1 , 4)");
            Query query = em.createQuery(consulta);
            query.setParameter(1, idSede);
            if (fechaIncial != null) {
                query.setParameter(2, fechaIncial);
            }
            if (fechaFinal != null) {
                query.setParameter(3, fechaFinal);
            }
            if (cuenta != null && !cuenta.isEmpty()) {
                query.setParameter(4, cuenta);
            }
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }    

}
