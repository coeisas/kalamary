/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgEmpresa;
import entities.CfgFormapago;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.List;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mario
 */
@Stateless
public class CfgFormapagoFacade extends AbstractFacade<CfgFormapago> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgFormapagoFacade() {
        super(CfgFormapago.class);
    }

    public List<CfgFormapago> buscarPorEmpresa(CfgEmpresa empresa) {
        try {
            Query query = em.createQuery("SELECT f FROM CfgFormapago f WHERE f.cfgempresaidEmpresa = ?1");
            query.setParameter(1, empresa);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }

    }

}
