/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgCategoriaproducto;
import entities.CfgEmpresa;
import entities.CfgMarcaproducto;
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
            Query query = em.createQuery("SELECT r FROM CfgReferenciaproducto r WHERE r.cfgcategoriaproductoidCategoria = ?1 AND r.codigoReferencia = ?2");
            query.setParameter(1, categoriaproducto);
            query.setParameter(2, codigo);
            return (CfgReferenciaproducto) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CfgReferenciaproducto> buscarPorEmpresa(CfgEmpresa empresa) {
        try {
            Query query = em.createQuery("SELECT r FROM CfgReferenciaproducto r WHERE r.cfgempresaidEmpresa = ?1");
            query.setParameter(1, empresa);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgReferenciaproducto buscarPorEmpresaAndCodigo(CfgEmpresa empresa, String codigo) {
        try {
            Query query = em.createQuery("SELECT r FROM CfgReferenciaproducto r WHERE r.cfgempresaidEmpresa = ?1 AND r.codigoReferencia = ?2");
            query.setParameter(1, empresa);
            query.setParameter(2, codigo);
            return (CfgReferenciaproducto) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public CfgReferenciaproducto buscarPorEmpresaCategoriaAndCodigo(CfgEmpresa empresa, CfgCategoriaproducto categoriaproducto, String codigo) {
        try {
            Query query = em.createQuery("SELECT r FROM CfgReferenciaproducto r WHERE r.cfgempresaidEmpresa = ?1 AND r.cfgcategoriaproductoidCategoria =?2 AND r.codigoReferencia = ?3");
            query.setParameter(1, empresa);
            query.setParameter(2, categoriaproducto);
            query.setParameter(3, codigo);
            return (CfgReferenciaproducto) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
