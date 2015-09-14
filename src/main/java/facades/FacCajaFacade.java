/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.FacCaja;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mario
 */
@Stateless
public class FacCajaFacade extends AbstractFacade<FacCaja> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacCajaFacade() {
        super(FacCaja.class);
    }
    
    public List<FacCaja> buscarCajasPorEmpresa(CfgEmpresa empresa) {
        try {
            Query query = em.createQuery("SELECT c FROM FacCaja c WHERE c.cfgempresasedeidSede.cfgempresaidEmpresa = ?1");
            query.setParameter(1, empresa);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }    

    public List<FacCaja> buscarCajasPorSede(CfgEmpresasede sede) {
        try {
            Query query = em.createQuery("SELECT c FROM FacCaja c WHERE c.cfgempresasedeidSede = ?1");
            query.setParameter(1, sede);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public FacCaja buscarCajasPorSedeAndCodigo(CfgEmpresasede sede, String codigo) {
        try {
            Query query = em.createQuery("SELECT c FROM FacCaja c WHERE c.cfgempresasedeidSede = ?1 AND c.codigoCaja = ?2");
            query.setParameter(1, sede);
            query.setParameter(2, codigo);
            return (FacCaja) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
