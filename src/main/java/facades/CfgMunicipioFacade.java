/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgMunicipio;
import entities.CfgMunicipioPK;
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
public class CfgMunicipioFacade extends AbstractFacade<CfgMunicipio> {
    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgMunicipioFacade() {
        super(CfgMunicipio.class);
    }
    
    public List<CfgMunicipio> buscarPorDepartamento(String idDepartamento) {
        try {
            Query query = em.createQuery("SELECT m FROM CfgMunicipio m WHERE m.cfgMunicipioPK.cfgdepartamentoidDepartamento = ?1 ORDER BY m.nomMunicipio");
            query.setParameter(1, idDepartamento);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public CfgMunicipio buscarPorMunicipioPK(CfgMunicipioPK cfgMunicipioPK) {
        try {
            Query query = em.createQuery("SELECT m FROM CfgMunicipio m WHERE m.cfgMunicipioPK = ?1");
            query.setParameter(1, cfgMunicipioPK);
            return (CfgMunicipio) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }        
}
