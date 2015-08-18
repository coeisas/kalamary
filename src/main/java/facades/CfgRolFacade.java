/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgRol;
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
public class CfgRolFacade extends AbstractFacade<CfgRol> {
    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgRolFacade() {
        super(CfgRol.class);
    }
    
    public List<CfgRol> buscarRolVisibles(){
        try {
            Query query = em.createQuery("SELECT r FROM CfgRol r WHERE r.codrol NOT LIKE '00001'");
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    
}
