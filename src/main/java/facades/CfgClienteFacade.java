/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgCliente;
import entities.CfgEmpresa;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author mario
 */
@Stateless
public class CfgClienteFacade extends AbstractFacade<CfgCliente> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgClienteFacade() {
        super(CfgCliente.class);
    }

    public CfgCliente buscarPorCodigoAndIdEmpresa(String codigo, Integer idEmpresa) {
        try {
            Query query = em.createQuery("SELECT c FROM CfgCliente c WHERE c.cfgempresaidEmpresa.idEmpresa = ?1 AND c.codigoCliente = ?2");
            query.setParameter(1, idEmpresa);
            query.setParameter(2, codigo);
            return (CfgCliente) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgCliente buscarPorIdentificacionAndIdEmpresa(String identificacion, CfgEmpresa empresa) {
        try {
            Query query = em.createQuery("SELECT c FROM CfgCliente c WHERE c.cfgempresaidEmpresa = ?1 AND c.numDoc = ?2");
            query.setParameter(1, empresa);
            query.setParameter(2, identificacion);
            return (CfgCliente) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CfgCliente> buscarPorEmpresa(CfgEmpresa empresa) {
        try {
            Query query = em.createQuery("SELECT c FROM CfgCliente c WHERE c.cfgempresaidEmpresa = ?1");
            query.setParameter(1, empresa);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CfgCliente> buscarPorEmpresaMenosClienteDefault(CfgEmpresa empresa) {
        try {
            Query query = em.createQuery("SELECT c FROM CfgCliente c WHERE c.cfgempresaidEmpresa = ?1 AND c.codigoCliente <> '1'");
            query.setParameter(1, empresa);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgCliente buscarClienteDefault(CfgEmpresa empresa) {
        try {
            Query query = em.createQuery("SELECT c FROM CfgCliente c WHERE c.cfgempresaidEmpresa = ?1 AND c.codigoCliente = '1'");
            query.setParameter(1, empresa);
            return (CfgCliente) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public int totaClientesByEmpresa(CfgEmpresa empresa) {
        try {
            Query query = em.createQuery("SELECT COUNT(c.idCliente) FROM CfgCliente c WHERE c.cfgempresaidEmpresa = ?1");
            query.setParameter(1, empresa);
            return Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public List<CfgCliente> lazyCliente(CfgEmpresa empresa, int offset, int limit) {
        try {
            Query query = em.createQuery("SELECT c FROM CfgCliente c WHERE c.cfgempresaidEmpresa = ?1");
            query.setParameter(1, empresa);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
}
