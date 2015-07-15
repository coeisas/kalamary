/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgCategoriaproducto;
import entities.CfgReferenciaproducto;
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
public class CfgReferenciaproductoFacade extends AbstractFacade<CfgReferenciaproducto> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgReferenciaproductoFacade() {
        super(CfgReferenciaproducto.class);
    }

    public List<CfgReferenciaproducto> buscarPorCategoria(CfgCategoriaproducto categoriaproducto) {
        try {
            Query query = em.createQuery("SELECT r FROM CfgReferenciaproducto r WHERE r.cfgcategoriaproductoidCategoria = ?1");
            query.setParameter(1, categoriaproducto);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgReferenciaproducto buscarPorCategoriaAndCodigo(CfgCategoriaproducto categoriaproducto, String codigo) {
        try {
            Query query = em.createQuery(codigo);
            query.setParameter(1, categoriaproducto);
            query.setParameter(2, codigo);
            return (CfgReferenciaproducto) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
