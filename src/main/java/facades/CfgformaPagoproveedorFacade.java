/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgEmpresa;
import java.util.List;
import entities.CfgformaPagoproveedor;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mario
 */
@Stateless
public class CfgformaPagoproveedorFacade extends AbstractFacade<CfgformaPagoproveedor> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgformaPagoproveedorFacade() {
        super(CfgformaPagoproveedor.class);
    }

    public List<CfgformaPagoproveedor> buscarFormasPagoByEmpresa(CfgEmpresa empresa) {
        try {
            Query query = em.createQuery("SELECT fp FROM CfgformaPagoproveedor fp WHERE fp.cfgempresaidEmpresa = ?1");
            query.setParameter(1, empresa);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
        
    }
}
