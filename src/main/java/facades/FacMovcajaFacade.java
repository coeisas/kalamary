/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.FacCaja;
import entities.FacMovcaja;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.Date;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mario
 */
@Stateless
public class FacMovcajaFacade extends AbstractFacade<FacMovcaja> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacMovcajaFacade() {
        super(FacMovcaja.class);
    }

    public FacMovcaja buscarMovimientoCaja(FacCaja caja) {
        try {
            Query query = em.createQuery("SELECT mc FROM FacMovcaja mc WHERE mc.faccajaidCaja = ?1 ORDER BY mc.idMovimiento DESC");
            query.setParameter(1, caja);
            query.setMaxResults(1);
            return (FacMovcaja) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
