/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgEmpresa;
import entities.CfgMovInventarioMaestro;
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
public class CfgMovInventarioMaestroFacade extends AbstractFacade<CfgMovInventarioMaestro> {
    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgMovInventarioMaestroFacade() {
        super(CfgMovInventarioMaestro.class);
    }
    
    public List<CfgMovInventarioMaestro> buscarByEmpresa(CfgEmpresa empresa){
        try {
            Query query = em.createQuery("SELECT m FROM CfgMovInventarioMaestro m WHERE m.cfgempresaidEmpresa = ?1");
            query.setParameter(1, empresa);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
}
