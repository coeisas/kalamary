/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.FacMovcaja;
import entities.FacMovcajadetalle;
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
public class FacMovcajadetalleFacade extends AbstractFacade<FacMovcajadetalle> {
    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacMovcajadetalleFacade() {
        super(FacMovcajadetalle.class);
    }
    
    public List<FacMovcajadetalle> buscarMovDetallePorMovCaja(FacMovcaja movcaja){
        try {
            Query query = em.createQuery("SELECT mcd FROM FacMovcajadetalle mcd WHERE mcd.facmovcajaidMovimiento = ?1");
            query.setParameter(1, movcaja);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
}
