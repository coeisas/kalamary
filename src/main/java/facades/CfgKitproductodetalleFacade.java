/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgKitproductodetalle;
import entities.CfgKitproductodetallePK;
import entities.CfgKitproductomaestro;
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
public class CfgKitproductodetalleFacade extends AbstractFacade<CfgKitproductodetalle> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgKitproductodetalleFacade() {
        super(CfgKitproductodetalle.class);
    }

    public List<CfgKitproductodetalle> buscarByMaestro(CfgKitproductomaestro maestro) {
        try {
            Query query = em.createQuery("SELECT kd FROM CfgKitproductodetalle kd WHERE kd.cfgKitproductomaestro = ?1");
            query.setParameter(1, maestro);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
