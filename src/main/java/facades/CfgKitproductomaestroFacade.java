/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgEmpresa;
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
public class CfgKitproductomaestroFacade extends AbstractFacade<CfgKitproductomaestro> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgKitproductomaestroFacade() {
        super(CfgKitproductomaestro.class);
    }

    public List<CfgKitproductomaestro> buscarPorEmpresa(CfgEmpresa empresa) {
        try {
            Query query = em.createQuery("SELECT km FROM CfgKitproductomaestro km WHERE km.cfgempresaidEmpresa = ?1");
            query.setParameter(1, empresa);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgKitproductomaestro buscarPorEmpresaAndCodigo(CfgEmpresa empresa, String codigo) {
        try {
            Query query = em.createQuery("SELECT km FROM CfgKitproductomaestro km WHERE km.cfgempresaidEmpresa = ?1 AND km.codKit = ?2");
            query.setParameter(1, empresa);
            query.setParameter(2, codigo);
            return (CfgKitproductomaestro) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }    
}
