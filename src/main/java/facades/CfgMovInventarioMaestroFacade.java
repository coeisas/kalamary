/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgEmpresa;
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
public class CfgMovInventarioMaestroFacade extends AbstractFacade<CfgMovInventarioMaestro> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgMovInventarioMaestroFacade() {
        super(CfgMovInventarioMaestro.class);
    }

    public CfgMovInventarioMaestro buscarMovimientoSalida() {
        try {
            Query query = em.createQuery("SELECT m FROM CfgMovInventarioMaestro m WHERE m.codMovInvetario LIKE '2'");
            return (CfgMovInventarioMaestro) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public CfgMovInventarioMaestro buscarMovimientoEntrada() {
        try {
            Query query = em.createQuery("SELECT m FROM CfgMovInventarioMaestro m WHERE m.codMovInvetario LIKE '1'");
            return (CfgMovInventarioMaestro) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }    
}
