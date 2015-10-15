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
import java.text.SimpleDateFormat;

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
    public List<ConsolidadoMovcaja> buscarPorSedeAndCaja(CfgEmpresasede sede, FacCaja caja, Date fechaInicial, Date fechaFinal) {
        try {
            String auxDate;
//            SELECT * FROM `consolidado_movcaja` WHERE cfg_empresasede_idSede = 3 AND fac_caja_idCaja = 1 AND fecha >= '2015-10-14' AND fecha <= '2015-10-14'
            String consulta = "SELECT * FROM consolidado_movcaja WHERE cfg_empresasede_idSede = " + sede.getIdSede();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (caja != null) {
                consulta = consulta.concat(" AND fac_caja_idCaja = " + caja.getIdCaja());

            }
            if (fechaInicial != null) {
                auxDate = sdf.format(fechaInicial);
                consulta = consulta.concat(" AND fecha >= '" + auxDate + "'");
            }
            if (fechaFinal != null) {
                auxDate = sdf.format(fechaFinal);
                consulta = consulta.concat(" AND fecha <= '" + auxDate + "'");
            }
            consulta = consulta.concat(" GROUP BY cfg_empresasede_idSede, fac_caja_idCaja, fecha, cfg_formapago_idFormaPago ORDER BY cfg_empresasede_idSede, fac_caja_idCaja, fecha, cfg_formapago_idFormaPago");
//            System.out.println(consulta);
            Query query = em.createNativeQuery(consulta, ConsolidadoMovcaja.class);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
