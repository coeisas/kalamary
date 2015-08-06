/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgTipoidentificacion;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mario
 */
@Stateless
public class CfgTipoidentificacionFacade extends AbstractFacade<CfgTipoidentificacion> {
    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CfgTipoidentificacionFacade() {
        super(CfgTipoidentificacion.class);
    }
    
    public CfgTipoidentificacion buscarByCodigo(String codigo){
        try {
            Query query = em.createQuery("SELECT i FROM CfgTipoidentificacion i WHERE i.codigo = ?1");
            query.setParameter(1, codigo);
            return (CfgTipoidentificacion) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
}
