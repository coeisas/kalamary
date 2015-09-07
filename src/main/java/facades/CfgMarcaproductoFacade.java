/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

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
public class CfgMarcaproductoFacade extends AbstractFacade<CfgMarcaproducto> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgMarcaproductoFacade() {
        super(CfgMarcaproducto.class);
    }

    public List<CfgMarcaproducto> buscarPorReferencia(CfgReferenciaproducto referenciaproducto) {
        try {
            Query query = em.createQuery("SELECT m FROM CfgMarcaproducto m WHERE m.cfgreferenciaproductoidReferencia = ?1");
            query.setParameter(1, referenciaproducto);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgMarcaproducto buscarPorReferenciaAndCodigo(CfgReferenciaproducto referenciaproducto, String codigo) {
        try {
            Query query = em.createQuery("SELECT m FROM CfgMarcaproducto m WHERE m.cfgreferenciaproductoidReferencia = ?1 AND m.codigoMarca = ?2");
            query.setParameter(1, referenciaproducto);
            query.setParameter(2, codigo);
            return (CfgMarcaproducto) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CfgMarcaproducto> buscarPorEmpresa(CfgEmpresa empresa) {
        try {
            Query query = em.createQuery("SELECT m FROM CfgMarcaproducto m WHERE m.cfgempresaidEmpresa = ?1");
            query.setParameter(1, empresa);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgMarcaproducto buscarPorEmpresaAndCodigo(CfgEmpresa empresa, String codigo) {
        try {
            Query query = em.createQuery("SELECT m FROM CfgMarcaproducto m WHERE m.cfgempresaidEmpresa = ?1 AND m.codigoMarca LIKE ?2");
            query.setParameter(1, empresa);
            query.setParameter(2, codigo);
            return (CfgMarcaproducto) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
