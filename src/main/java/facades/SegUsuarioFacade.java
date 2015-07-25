/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgEmpresa;
import java.util.List;
import entities.SegUsuario;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mario
 */
@Stateless
public class SegUsuarioFacade extends AbstractFacade<SegUsuario> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SegUsuarioFacade() {
        super(SegUsuario.class);
    }

//    usado para login como super usuario
    public SegUsuario buscarUsuarioSuper(String usuario, String pasword) {
        try {
            Query query = em.createQuery("SELECT u FROM SegUsuario u WHERE u.cfgRolIdrol.codrol = '00001' AND u.usuario = ?1 AND u.password = ?2  AND u.activo = TRUE");
            query.setParameter(1, usuario);
            query.setParameter(2, pasword);
            return (SegUsuario) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
//    usado para comprabar existencia de un usuario super. Valor unico
    public SegUsuario buscarUsuarioSuper(String usuario) {
        try {
            Query query = em.createQuery("SELECT u FROM SegUsuario u WHERE u.cfgRolIdrol.codrol = '00001' AND u.usuario = ?1");
            query.setParameter(1, usuario);
            return (SegUsuario) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public SegUsuario buscarUsuario(String usuario, String pasword, Integer idSede) {
        try {
            Query query = em.createQuery("SELECT u FROM SegUsuario u WHERE u.cfgempresasedeidSede.idSede = ?3 AND u.usuario = ?1 AND u.password = ?2 AND u.activo = TRUE");
            query.setParameter(1, usuario);
            query.setParameter(2, pasword);
            query.setParameter(3, idSede);
            return (SegUsuario) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public SegUsuario buscarPorEmpresaAndNomUsuario(Integer idEmpresa, String nomUsuario){
        try{
            Query query = em.createQuery("SELECT u FROM SegUsuario u WHERE u.cfgempresasedeidSede.cfgempresaidEmpresa.idEmpresa = ?1 AND u.usuario LIKE ?2");
            query.setParameter(1, idEmpresa);
            query.setParameter(2, nomUsuario);
            return (SegUsuario) query.getSingleResult();
        }catch(Exception e){
            return null;
        }
    }
    
    public List<SegUsuario> buscarPorEmpresa(CfgEmpresa empresa){
        try{
            Query query = em.createQuery("SELECT u FROM SegUsuario u WHERE u.cfgempresasedeidSede.cfgempresaidEmpresa = ?1");
            query.setParameter(1, empresa);
            return query.getResultList();
        }catch(Exception e){
            return null;
        }        
    }
}
