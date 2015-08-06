/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgTipoempresa;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mario
 */
@Stateless
public class CfgTipoempresaFacade extends AbstractFacade<CfgTipoempresa> {
    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgTipoempresaFacade() {
        super(CfgTipoempresa.class);
    }
    
    public CfgTipoempresa buscarByCodigo(String codigo){
        try {
            Query query = em.createQuery("SELECT t FROM CfgTipoempresa t WHERE t.codigo = ?1");
            query.setParameter(1, codigo);
            return (CfgTipoempresa) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
}
