/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgCliente;
import entities.CfgEmpresasede;
import entities.FacCarteraCliente;
import entities.FacDocumentosmaster;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 *
 * @author mario
 */
@Stateless
public class FacCarteraClienteFacade extends AbstractFacade<FacCarteraCliente> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacCarteraClienteFacade() {
        super(FacCarteraCliente.class);
    }

    public List<FacCarteraCliente> buscarPorCliente(CfgCliente cliente) {
        try {
            Query query = em.createQuery("SELECT c FROM FacCarteraCliente c WHERE c.facCarteraClientePK.cfgclienteidCliente = ?1");
            query.setParameter(1, cliente.getIdCliente());
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public FacCarteraCliente buscarPorDocumentoMaestro(FacDocumentosmaster maestro) {
        try {
            Query query = em.createQuery("SELECT c FROM FacCarteraCliente c WHERE c.facDocumentosmaster =?1");
            query.setParameter(1, maestro);
            return (FacCarteraCliente) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
