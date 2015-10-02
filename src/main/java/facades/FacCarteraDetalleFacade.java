/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.FacCarteraCliente;
import entities.FacCarteraDetalle;
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
public class FacCarteraDetalleFacade extends AbstractFacade<FacCarteraDetalle> {
    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacCarteraDetalleFacade() {
        super(FacCarteraDetalle.class);
    }
    
    public List<FacCarteraDetalle> buscarPorCartera(FacCarteraCliente cartera) {
        try {
           Query query = em.createQuery("SELECT c FROM FacCarteraDetalle c WHERE c.facCarteraCliente = ?1 ORDER BY c.facCarteraDetallePK.fecha DESC");
           query.setParameter(1, cartera);
           return query.getResultList();
        } catch (Exception e) {
            return null;
        }        
    }
    
}
