/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgMovCta;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Mario
 */
@Stateless
public class CfgMovCtaFacade extends AbstractFacade<CfgMovCta> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgMovCtaFacade() {
        super(CfgMovCta.class);
    }

    public List<CfgMovCta> buscarPorTipoMovimientoAndFormaPago(int idSede, int idTipoMovimiento, int idFormaPago) {
        try {
            Query query = em.createQuery("SELECT c FROM CfgMovCta c WHERE c.cfgMovCtaPK.cfgempresasedeidSede = ?1 AND c.cfgMovCtaPK.cfgAplicaciondocumentoIdaplicacion = ?2 AND c.cfgMovCtaPK.formaPago = ?3" );
            query.setParameter(1, idSede);
            query.setParameter(2, idTipoMovimiento);
            query.setParameter(3, idFormaPago);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}


