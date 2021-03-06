/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgEmpresa;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mario
 */
@Stateless
public class CfgEmpresaFacade extends AbstractFacade<CfgEmpresa> {
    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgEmpresaFacade() {
        super(CfgEmpresa.class);
    }

    public CfgEmpresa buscarEmpresaPorCodigo(String codigo) {
        try {
            Query query = em.createQuery("SELECT e FROM CfgEmpresa e WHERE e.codEmpresa = ?1");
            query.setParameter(1, codigo);
            return (CfgEmpresa) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }    
}
