/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgEmpresasede;
import entities.CfgProducto;
import entities.InvConsolidado;
import javax.ejb.Stateless;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mario
 */
@Stateless
public class InvConsolidadoFacade extends AbstractFacade<InvConsolidado> {
    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InvConsolidadoFacade() {
        super(InvConsolidado.class);
    }
    
    public InvConsolidado buscarByEmpresaAndProducto(CfgEmpresasede sede, CfgProducto producto){
        try {
            Query query = em.createQuery("SELECT c FROM InvConsolidado c WHERE c.cfgEmpresasede = ?1 AND c.cfgProducto = ?2");
            query.setParameter(1, sede);
            query.setParameter(2, producto);
            return (InvConsolidado) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
           
    }
}
