/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgDocumento;
import entities.CfgEmpresasede;
import entities.FacDocumentosmaster;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mario
 */
@Stateless
public class FacDocumentosmasterFacade extends AbstractFacade<FacDocumentosmaster> {
    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacDocumentosmasterFacade() {
        super(FacDocumentosmaster.class);
    }
    
    public FacDocumentosmaster buscarBySedeAndDocumentoAndNum(CfgEmpresasede sede, long documento, long num){
        try {
            Query query = em.createQuery("SELECT d FROM FacDocumentosmaster d WHERE d.cfgempresasedeidSede = ?1 AND d.facDocumentosmasterPK.cfgdocumentoidDoc = ?2 AND d.facDocumentosmasterPK.numDocumento = ?3");
            query.setParameter(1, sede);
            query.setParameter(2, documento);
            query.setParameter(3, num);
            return (FacDocumentosmaster) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
           
}
