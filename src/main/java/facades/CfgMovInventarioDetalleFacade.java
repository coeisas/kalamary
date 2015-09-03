/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgMovInventarioDetalle;
import entities.CfgMovInventarioMaestro;
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
public class CfgMovInventarioDetalleFacade extends AbstractFacade<CfgMovInventarioDetalle> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgMovInventarioDetalleFacade() {
        super(CfgMovInventarioDetalle.class);
    }

    public List<CfgMovInventarioDetalle> buscarByMaestro(CfgMovInventarioMaestro maestro) {
        try {
            Query query = em.createQuery("SELECT d FROM CfgMovInventarioDetalle d WHERE d.cfgmovinventariomaestroidMovInventarioMaestro = ?1 AND d.visible = TRUE");
            query.setParameter(1, maestro);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgMovInventarioDetalle buscarSalidaVentaByMaestro(CfgMovInventarioMaestro movInventarioMaestro) {
        try {
            Query query = em.createQuery("SELECT d FROM CfgMovInventarioDetalle d WHERE d.cfgmovinventariomaestroidMovInventarioMaestro = ?1 AND d.codMovInvetarioDetalle LIKE '4'");
            query.setParameter(1, movInventarioMaestro);
            return (CfgMovInventarioDetalle) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
