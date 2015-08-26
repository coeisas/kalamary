/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgCliente;
import entities.CfgEmpresa;
import entities.CfgProveedor;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mario
 */
@Stateless
public class CfgProveedorFacade extends AbstractFacade<CfgProveedor> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgProveedorFacade() {
        super(CfgProveedor.class);
    }

    public CfgProveedor buscarPorIdentificacionAndIdEmpresa(String numIdentificacion, CfgEmpresa empresa) {
        try {
            Query query = em.createQuery("SELECT p FROM CfgProveedor p WHERE p.numDoc = ?1 AND p.cfgempresaidEmpresa = ?2");
            query.setParameter(1, numIdentificacion);
            query.setParameter(2, empresa);
            return (CfgProveedor) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    public List<CfgProveedor> lazyCliente(CfgEmpresa empresa, int offset, int limit) {
        try {
            Query query = em.createQuery("SELECT p FROM CfgProveedor p WHERE p.cfgempresaidEmpresa = ?1");
            query.setParameter(1, empresa);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public int totaProveedoresByEmpresa(CfgEmpresa empresa) {
        try {
            Query query = em.createQuery("SELECT COUNT(p.idProveedor) FROM CfgProveedor p WHERE p.cfgempresaidEmpresa = ?1");
            query.setParameter(1, empresa);
            return Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }

}
