/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgTalla;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 *
 * @author mario
 */
@Stateless
public class CfgTallaFacade extends AbstractFacade<CfgTalla> {
    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgTallaFacade() {
        super(CfgTalla.class);
    }
    public CfgTalla getTallaXid(int id){
        try {
            Query query = em.createNamedQuery("CfgTalla.findByIdTalla");
            query.setParameter("idTalla", id);
            return (CfgTalla) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }
}
