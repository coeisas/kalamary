/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.FacDocumentodetalle;
import entities.FacDocumentosmaster;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mario
 */
@Stateless
public class FacDocumentodetalleFacade extends AbstractFacade<FacDocumentodetalle> {
    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacDocumentodetalleFacade() {
        super(FacDocumentodetalle.class);
    }
    
    public List<FacDocumentodetalle> buscarByDocumentoMaster(FacDocumentosmaster documentosmaster){
        try {
            Query query = em.createQuery("SELECT d FROM FacDocumentodetalle d WHERE d.facDocumentosmaster = ?1");
            query.setParameter(1, documentosmaster);
            return query.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
}
