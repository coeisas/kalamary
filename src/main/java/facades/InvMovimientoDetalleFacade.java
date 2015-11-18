/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CfgEmpresasede;
import entities.CfgMarcaproducto;
import entities.CfgMovInventarioMaestro;
import entities.CfgProducto;
import entities.InvMovimientoDetalle;
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
public class InvMovimientoDetalleFacade extends AbstractFacade<InvMovimientoDetalle> {

    @PersistenceContext(unitName = "com.mycompany_kalamary_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InvMovimientoDetalleFacade() {
        super(InvMovimientoDetalle.class);
    }

    public List<InvMovimientoDetalle> detalleMovimientoInforme(CfgEmpresasede sede, Date fechaIncial, Date fechaFinal, CfgProducto producto, CfgMovInventarioMaestro movimientoMaestro, int concecutivo) {
        try {
            Query query;
            String consulta = "SELECT m FROM InvMovimientoDetalle m WHERE m.invMovimiento.cfgempresasedeidSede = ?1";
            if (fechaIncial != null) {
                consulta = consulta.concat(" AND m.invMovimiento.fecha >= ?2");
            }
            if (fechaFinal != null) {
                consulta = consulta.concat(" AND m.invMovimiento.fecha <= ?3");
            }
            if (producto != null) {
                consulta = consulta.concat(" AND m.cfgProducto = ?4");
            }
            if (movimientoMaestro != null) {
                consulta = consulta.concat(" AND m.invMovimiento.cfgmovinventariodetalleidMovInventarioDetalle.cfgmovinventariomaestroidMovInventarioMaestro = ?5");
            }
            if (concecutivo > 0) {
                consulta = consulta.concat(" AND m.invMovimiento.invMovimientoPK.numDoc = ?6");
            }
            consulta = consulta.concat(" ORDER BY m.invMovimiento.fecha, m.invMovimiento.invMovimientoPK");
//            query = em.createQuery("SELECT m FROM InvMovimientoDetalle m WHERE m.invMovimiento.cfgempresasedeidSede = ?1 AND m.invMovimiento.fecha >= ?2 AND m.invMovimiento.fecha <= ?3 AND m.cfgProducto = ?4 AND m.invMovimiento.cfgmovinventariodetalleidMovInventarioDetalle.cfgmovinventariomaestroidMovInventarioMaestro = ?5 AND m.invMovimiento.invMovimientoPK.numDoc = ?6 ORDER BY m.invMovimiento.fecha, m.invMovimiento.invMovimientoPK");
            query = em.createQuery(consulta);
            query.setParameter(1, sede);
            if (fechaIncial != null) {
                query.setParameter(2, fechaIncial);
            }
            if (fechaFinal != null) {
                query.setParameter(3, fechaFinal);
            }
            if (producto != null) {
                query.setParameter(4, producto);
            }
            if (movimientoMaestro != null) {
                query.setParameter(5, movimientoMaestro);
            }
            if (concecutivo > 0) {
                query.setParameter(6, concecutivo);
            }
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
