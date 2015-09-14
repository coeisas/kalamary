/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgEmpresasede;
import entities.ConsolidadoMovcaja;
import entities.FacCaja;
import java.util.Date;
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
public class ConsolidadoMovcajaFacade extends AbstractFacade<ConsolidadoMovcaja> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConsolidadoMovcajaFacade() {
        super(ConsolidadoMovcaja.class);
    }

//    USADO PARA EL INFORME DE MOVIMIENTO DE CAJAS
    public List<ConsolidadoMovcaja> buscarPorSedeAndCaja(CfgEmpresasede sede, FacCaja caja, Date fechaInicial, Date FechaFinal) {
        try {
//            SELECT * FROM consolidado_movcaja  GROUP BY cfg_empresasede_idSede, fac_caja_idCaja, fecha, cfg_formapago_idFormaPago ORDER BY cfg_empresasede_idSede, fac_caja_idCaja, fecha, cfg_formapago_idFormaPago
            String sql = "SELECT m FROM ConsolidadoMovcaja m WHERE m.cfgEmpresasede = ?1";
            if (caja != null) {
                sql = sql.concat(" AND m.facCaja = ?2");
            }
            if (fechaInicial != null) {
                sql = sql.concat(" AND m.consolidadoMovcajaPK.fecha >= ?3");
            }
            if (FechaFinal != null) {
                sql = sql.concat(" AND m.consolidadoMovcajaPK.fecha <= ?4");
            }
            sql = sql.concat(" GROUP BY m.cfgEmpresasede, m.facCaja, m.consolidadoMovcajaPK.fecha, m.cfgFormapago ORDER BY m.cfgEmpresasede, m.facCaja, m.consolidadoMovcajaPK.fecha, m.cfgFormapago");
//            Query query = em.createQuery("SELECT m FROM ConsolidadoMovcaja m WHERE m.cfgEmpresasede = ?1 AND m.facCaja = ?2 AND m.consolidadoMovcajaPK.fecha >= ?3 AND m.consolidadoMovcajaPK.fecha <= ?4 GROUP BY m.cfgEmpresasede, m.facCaja, m.consolidadoMovcajaPK.fecha, m.cfgFormapago ORDER BY m.cfgEmpresasede, m.facCaja, m.consolidadoMovcajaPK.fecha, m.cfgFormapago");
            Query query = em.createQuery(sql);
            query.setParameter(1, sede);
            if (caja != null) {
                query.setParameter(2, caja);
            }
            if (fechaInicial != null) {
                query.setParameter(3, fechaInicial);
            }
            if (FechaFinal != null) {
                query.setParameter(4, FechaFinal);
            }
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
