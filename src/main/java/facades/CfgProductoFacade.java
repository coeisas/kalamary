/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgCategoriaproducto;
import entities.CfgEmpresa;
import entities.CfgMarcaproducto;
import entities.CfgProducto;
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
public class CfgProductoFacade extends AbstractFacade<CfgProducto> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgProductoFacade() {
        super(CfgProducto.class);
    }

    public int TotalProductosPorEmpresa(CfgEmpresa empresa) {
        try {
            Query query = em.createQuery("SELECT COUNT(p.idProducto) FROM CfgProducto p WHERE p.cfgempresaidEmpresa = ?1");
            query.setParameter(1, empresa);
            return Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public List<CfgProducto> buscarPorEmpresa(CfgEmpresa empresa, int first, int pageSize) {
        try {
            Query query = em.createQuery("SELECT p FROM CfgProducto p WHERE p.cfgempresaidEmpresa = ?1");
            query.setParameter(1, empresa);
            query.setFirstResult(first);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgProducto buscarPorEmpresaAndCodigo(CfgEmpresa empresa, String codigo) {
        try {
            Query query = em.createQuery("SELECT p FROM CfgProducto p WHERE p.cfgempresaidEmpresa = ?1 AND p.codProducto = ?2");
            query.setParameter(1, empresa);
            query.setParameter(2, codigo);
            return (CfgProducto) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgProducto buscarPorEmpresaAndMarcaAndCodigo(CfgEmpresa empresa, CfgMarcaproducto marcaproducto, String codigo) {
        try {
            Query query = em.createQuery("SELECT p FROM CfgProducto p WHERE p.cfgempresaidEmpresa = ?1 AND p.cfgmarcaproductoidMarca = ?2 AND p.codProducto = ?3");
            query.setParameter(1, empresa);
            query.setParameter(2, marcaproducto);
            query.setParameter(3, codigo);
            return (CfgProducto) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgProducto buscarPorEmpresaAndReferenciaAndCodigo(CfgEmpresa empresa, CfgReferenciaproducto referenciaproducto, String codigo) {
        try {
            Query query = em.createQuery("SELECT p FROM CfgProducto p WHERE p.cfgempresaidEmpresa = ?1 AND p.cfgmarcaproductoidMarca.cfgreferenciaproductoidReferencia = ?2 AND p.codProducto = ?3");
            query.setParameter(1, empresa);
            query.setParameter(2, referenciaproducto);
            query.setParameter(3, codigo);
            return (CfgProducto) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgProducto buscarPorEmpresaAndCategoriaAndCodigo(CfgEmpresa empresa, CfgCategoriaproducto categoriaproducto, String codigo) {
        try {
            Query query = em.createQuery("SELECT p FROM CfgProducto p WHERE p.cfgempresaidEmpresa = ?1 AND p.cfgmarcaproductoidMarca.cfgreferenciaproductoidReferencia.cfgcategoriaproductoidCategoria = ?2 AND p.codProducto = ?3");
            query.setParameter(1, empresa);
            query.setParameter(2, categoriaproducto);
            query.setParameter(3, codigo);
            return (CfgProducto) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public int totalProductosPorEmpresaAndMarca(CfgEmpresa empresa, CfgMarcaproducto marcaproducto) {
        try {
            Query query = em.createQuery("SELECT COUNT(p.idProducto) FROM CfgProducto p WHERE p.cfgempresaidEmpresa = ?1 AND p.cfgmarcaproductoidMarca = ?2");
            query.setParameter(1, empresa);
            query.setParameter(2, marcaproducto);
            return Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public List<CfgProducto> buscarPorEmpresaAndMarca(CfgEmpresa empresa, CfgMarcaproducto marcaproducto, int first, int pageSize) {
        try {
            Query query = em.createQuery("SELECT p FROM CfgProducto p WHERE p.cfgempresaidEmpresa = ?1 AND p.cfgmarcaproductoidMarca = ?2");
            query.setParameter(1, empresa);
            query.setParameter(2, marcaproducto);
            query.setFirstResult(first);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CfgProducto> buscarPorEmpresaAndReferencia(CfgEmpresa empresa, CfgReferenciaproducto referenciaproducto, int first, int pageSize) {
        try {
            Query query = em.createQuery("SELECT p FROM CfgProducto p WHERE p.cfgempresaidEmpresa = ?1 AND p.cfgmarcaproductoidMarca.cfgreferenciaproductoidReferencia = ?2");
            query.setParameter(1, empresa);
            query.setParameter(2, referenciaproducto);
            query.setFirstResult(first);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public int TotalProductosPorEmpresaAndReferencia(CfgEmpresa empresa, CfgReferenciaproducto referenciaproducto) {
        try {
            Query query = em.createQuery("SELECT COUNT(p.idProducto) FROM CfgProducto p WHERE p.cfgempresaidEmpresa = ?1 AND p.cfgmarcaproductoidMarca.cfgreferenciaproductoidReferencia = ?2");
            query.setParameter(1, empresa);
            query.setParameter(2, referenciaproducto);
            return Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public List<CfgProducto> buscarPorEmpresaAndCategoria(CfgEmpresa empresa, CfgCategoriaproducto categoriaproducto, int first, int pageSize) {
        try {
            Query query = em.createQuery("SELECT p FROM CfgProducto p WHERE p.cfgempresaidEmpresa = ?1 AND p.cfgmarcaproductoidMarca.cfgreferenciaproductoidReferencia.cfgcategoriaproductoidCategoria = ?2");
            query.setParameter(1, empresa);
            query.setParameter(2, categoriaproducto);
            query.setFirstResult(first);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public int totalProductosPorEmpresaAndCategoria(CfgEmpresa empresa, CfgCategoriaproducto categoriaproducto) {
        try {
            Query query = em.createQuery("SELECT COUNT(p.idProducto) FROM CfgProducto p WHERE p.cfgempresaidEmpresa = ?1 AND p.cfgmarcaproductoidMarca.cfgreferenciaproductoidReferencia.cfgcategoriaproductoidCategoria = ?2");
            query.setParameter(1, empresa);
            query.setParameter(2, categoriaproducto);
            return Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public List<CfgProducto> buscarPorEmpresaFilter(CfgEmpresa empresa, int first, int pageSize, String sqlFilters, String codigoInterno, String codProducto, String nomProducto) {
        try {
            String sql = "SELECT p FROM CfgProducto p WHERE p.cfgempresaidEmpresa = ?1";
            Query query = em.createQuery(sql.concat(sqlFilters));
            query.setParameter(1, empresa);
            if (!codProducto.isEmpty()) {
                query.setParameter(2, codProducto);
            }
            if (!nomProducto.isEmpty()) {
                query.setParameter(3, nomProducto);
            }
            if (!codigoInterno.isEmpty()) {
                query.setParameter(4, codigoInterno);
            }
            query.setFirstResult(first);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public int totalProductosPorEmpresaFilter(CfgEmpresa empresa, String sqlFilters, String codigoInterno, String codProducto, String nomProducto) {
        try {
            String sql = "SELECT COUNT(p.idProducto) FROM CfgProducto p WHERE p.cfgempresaidEmpresa = ?1";
            Query query = em.createQuery(sql.concat(sqlFilters));
            query.setParameter(1, empresa);
            if (!codProducto.isEmpty()) {
                query.setParameter(2, codProducto);
            }
            if (!nomProducto.isEmpty()) {
                query.setParameter(3, nomProducto);
            }
            if (!codigoInterno.isEmpty()) {
                query.setParameter(4, codigoInterno);
            }
            return Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }
}
