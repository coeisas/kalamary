/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgUnidad;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 *
 * @author mario
 */
@Stateless
public class CfgUnidadFacade extends AbstractFacade<CfgUnidad> {
    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgUnidadFacade() {
        super(CfgUnidad.class);
    }
    
    public CfgUnidad getUnidadXid(int id){
        try {
            Query query = em.createNamedQuery("CfgUnidad.findByIdUnidad");
            query.setParameter("idUnidad", id);
            return (CfgUnidad) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }
    
}
