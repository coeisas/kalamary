/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgCategoriaproducto;
import entities.CfgEmpresa;
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
public class CfgCategoriaproductoFacade extends AbstractFacade<CfgCategoriaproducto> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgCategoriaproductoFacade() {
        super(CfgCategoriaproducto.class);
    }

    public List<CfgCategoriaproducto> buscarPorEmpresa(CfgEmpresa empresa) {
        try {
            Query query = em.createQuery("SELECT c FROM CfgCategoriaproducto c WHERE c.cfgempresaidEmpresa = ?1");
            query.setParameter(1, empresa);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgCategoriaproducto buscarPorEmpresaAndCodigo(CfgEmpresa empresa, String codigoCategoria) {
        try {
            Query query = em.createQuery("SELECT c FROM CfgCategoriaproducto c WHERE c.cfgempresaidEmpresa = ?1 AND c.codigoCategoria = ?2");
            query.setParameter(1, empresa);
            query.setParameter(2, codigoCategoria);
            return (CfgCategoriaproducto) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

}
