/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgEmpresa;
import entities.CfgEmpresasede;
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
public class CfgEmpresasedeFacade extends AbstractFacade<CfgEmpresasede> {
    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgEmpresasedeFacade() {
        super(CfgEmpresasede.class);
    }
    
    public List<CfgEmpresasede> buscarSedesPorEmpresa(Integer idEmpresa) {
        try {
            Query query = em.createQuery("SELECT s FROM CfgEmpresasede s WHERE s.activo = TRUE AND s.cfgempresaidEmpresa.idEmpresa = ?1");
            query.setParameter(1, idEmpresa);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }    
    
    public CfgEmpresasede buscarSedePorEmpresa(CfgEmpresa empresa, String codSede){
        try{
            Query query = em.createQuery("SELECT s FROM CfgEmpresasede s WHERE s.activo = TRUE AND s.cfgempresaidEmpresa = ?1 AND s.codSede LIKE ?2");
            query.setParameter(1, empresa);
            query.setParameter(2, codSede);
            return (CfgEmpresasede) query.getSingleResult();
        }catch(Exception e){
            return null;
        }
    }
}
