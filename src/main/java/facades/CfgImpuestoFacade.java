/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.CfgImpuesto;
import entities.CfgTipoempresa;
import java.util.ArrayList;
import javax.ejb.Stateless;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mario
 */
@Stateless
public class CfgImpuestoFacade extends AbstractFacade<CfgImpuesto> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgImpuestoFacade() {
        super(CfgImpuesto.class);
    }
   
    public CfgImpuesto buscarImpuestoPorEmpresaAndCodigo(CfgEmpresa empresa, String codigo) {
        try {
            Query query = em.createQuery("SELECT i FROM CfgImpuesto i WHERE i.cfgempresaidEmpresa = ?1 AND i.codImpuesto = ?2");
            query.setParameter(1, empresa);
            query.setParameter(2, codigo);
            return (CfgImpuesto) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<CfgImpuesto> buscarImpuestosPorEmpresa(CfgEmpresa empresa){
        try {
            Query query = em.createQuery("SELECT i FROM CfgImpuesto i WHERE i.cfgTipoempresaId = ?1 AND i.cfgempresaidEmpresa = ?2");
            query.setParameter(1, empresa.getCfgTipoempresaId());
            query.setParameter(2, empresa);
            return query.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
}
