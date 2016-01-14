/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.contabilidad;

import entities.CfgEmpresasede;
import entities.CfgMovCta;
import entities.CfgMovCtaPK;
import facades.CfgAplicaciondocumentoFacade;
import facades.CfgMovCtaFacade;
import facades.CntPucFacade;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import managedBeans.seguridad.SesionMB;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Mario
 */
@ManagedBean
@SessionScoped
public class ConfiguracionContabilidadMB implements Serializable {

    private CfgEmpresasede sedeActual;

    private int idTipoMovimiento;
    private int idFormaPago;
    private String codCuentaSubtotal;
    private int idAfectaSubutotal;//DEBE(1) - HABER(2)
    private String codCuentaDescuento;
    private int idAfectaDescuento;//DEBE(1) - HABER(2)
    private String codCuentaImpuesto;
    private int idAfectaImpuesto;//DEBE(1) - HABER(2)
    private String codCuentaTotal;
    private int idAfectaTotal;//DEBE(1) - HABER(2)

    private boolean esNuevoRegistro;

    private List<CfgMovCta> listaCfgMovCta;

    @EJB
    CntPucFacade cntPucFacade;
    @EJB
    CfgAplicaciondocumentoFacade aplicaciondocumentoFacade;
    @EJB
    CfgMovCtaFacade cfgMovCtaFacade;

    public ConfiguracionContabilidadMB() {
    }

    @PostConstruct
    private void init() {
        esNuevoRegistro = true;
        FacesContext context = FacesContext.getCurrentInstance();

        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        sedeActual = sesionMB.getSedeActual();
    }

    public void accion() {
        if (esNuevoRegistro) {
            guardarConfiguracionContabilidad();
        } else {
            actualizarConfiguracionContabilidad();
        }
        limpiarFormulario();
    }

    private void guardarConfiguracionContabilidad() {
        try {
            //SUBTOTAL
            CfgMovCta cfgMovCtaSubtotal = new CfgMovCta();
            cfgMovCtaSubtotal.setCfgMovCtaPK(new CfgMovCtaPK(sedeActual.getIdSede(), idTipoMovimiento, 1, idFormaPago));
            cfgMovCtaSubtotal.setAplica(idAfectaSubutotal);
            cfgMovCtaSubtotal.setCfgAplicaciondocumento(aplicaciondocumentoFacade.find(idTipoMovimiento));
            cfgMovCtaSubtotal.setCntpuccodigoCuenta(cntPucFacade.find(codCuentaSubtotal));
            cfgMovCtaFacade.create(cfgMovCtaSubtotal);

            //DESCUENTO
            CfgMovCta cfgMovCtaDescuento = new CfgMovCta();
            cfgMovCtaDescuento.setCfgMovCtaPK(new CfgMovCtaPK(sedeActual.getIdSede(), idTipoMovimiento, 2, idFormaPago));
            cfgMovCtaDescuento.setAplica(idAfectaDescuento);
            cfgMovCtaDescuento.setCfgAplicaciondocumento(aplicaciondocumentoFacade.find(idTipoMovimiento));
            cfgMovCtaDescuento.setCntpuccodigoCuenta(cntPucFacade.find(codCuentaDescuento));
            cfgMovCtaFacade.create(cfgMovCtaDescuento);

            //IMPUESTO
            CfgMovCta cfgMovCtaImpuesto = new CfgMovCta();
            cfgMovCtaImpuesto.setCfgMovCtaPK(new CfgMovCtaPK(sedeActual.getIdSede(), idTipoMovimiento, 3, idFormaPago));
            cfgMovCtaImpuesto.setAplica(idAfectaImpuesto);
            cfgMovCtaImpuesto.setCfgAplicaciondocumento(aplicaciondocumentoFacade.find(idTipoMovimiento));
            cfgMovCtaImpuesto.setCntpuccodigoCuenta(cntPucFacade.find(codCuentaImpuesto));
            cfgMovCtaFacade.create(cfgMovCtaImpuesto);

            //TOTAL
            CfgMovCta cfgMovCtaTotal = new CfgMovCta();
            cfgMovCtaTotal.setCfgMovCtaPK(new CfgMovCtaPK(sedeActual.getIdSede(), idTipoMovimiento, 4, idFormaPago));
            cfgMovCtaTotal.setAplica(idAfectaTotal);
            cfgMovCtaTotal.setCfgAplicaciondocumento(aplicaciondocumentoFacade.find(idTipoMovimiento));
            cfgMovCtaTotal.setCntpuccodigoCuenta(cntPucFacade.find(codCuentaTotal));
            cfgMovCtaFacade.create(cfgMovCtaTotal);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Registro Guardado"));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Registro no Guardado"));
        }
    }

    public void buscarConfiguracionContable() {
        listaCfgMovCta = null;
        if (idTipoMovimiento != 0 && idFormaPago != 0) {
            listaCfgMovCta = cfgMovCtaFacade.buscarPorTipoMovimientoAndFormaPago(sedeActual.getIdSede(), idTipoMovimiento, idFormaPago);
        }
        if (listaCfgMovCta != null && !listaCfgMovCta.isEmpty()) {
            esNuevoRegistro = false;
            for (CfgMovCta cmc : listaCfgMovCta) {
                switch (cmc.getCfgMovCtaPK().getCntDetalleIdcntDetalle()) {
                    case 1:
                        codCuentaSubtotal = cmc.getCntpuccodigoCuenta() != null ? cmc.getCntpuccodigoCuenta().getCodigoCuenta() : null;
                        idAfectaSubutotal = cmc.getAplica();
                        break;
                    case 2:
                        codCuentaDescuento = cmc.getCntpuccodigoCuenta() != null ? cmc.getCntpuccodigoCuenta().getCodigoCuenta() : null;
                        idAfectaDescuento = cmc.getAplica();
                        break;
                    case 3:
                        codCuentaImpuesto = cmc.getCntpuccodigoCuenta() != null ? cmc.getCntpuccodigoCuenta().getCodigoCuenta() : null;
                        idAfectaImpuesto = cmc.getAplica();
                        break;
                    case 4:
                        codCuentaTotal = cmc.getCntpuccodigoCuenta() != null ? cmc.getCntpuccodigoCuenta().getCodigoCuenta() : null;
                        idAfectaTotal = cmc.getAplica();
                        break;
                }
            }
        } else {
            esNuevoRegistro = true;
            codCuentaDescuento = null;
            codCuentaSubtotal = null;
            codCuentaImpuesto = null;
            codCuentaTotal = null;
            idAfectaDescuento = 0;
            idAfectaSubutotal = 0;
            idAfectaImpuesto = 0;
            idAfectaTotal = 0;
        }
        RequestContext.getCurrentInstance().update("IdFormConfiguracionContable");
    }

    private void limpiarFormulario() {
        idTipoMovimiento = 0;
        idFormaPago = 0;
        codCuentaDescuento = null;
        codCuentaSubtotal = null;
        codCuentaImpuesto = null;
        codCuentaTotal = null;
        idAfectaDescuento = 0;
        idAfectaSubutotal = 0;
        idAfectaImpuesto = 0;
        idAfectaTotal = 0;
        esNuevoRegistro = true;
        RequestContext.getCurrentInstance().update("IdFormConfiguracionContable");
    }

    private void actualizarConfiguracionContabilidad() {
        try {
            for (CfgMovCta cmc : listaCfgMovCta) {
                switch (cmc.getCfgMovCtaPK().getCntDetalleIdcntDetalle()) {
                    case 1:
                        cmc.setCntpuccodigoCuenta(cntPucFacade.find(codCuentaSubtotal));
                        cmc.setAplica(idAfectaSubutotal);
                        break;
                    case 2:
                        cmc.setCntpuccodigoCuenta(cntPucFacade.find(codCuentaDescuento));
                        cmc.setAplica(idAfectaDescuento);
                        break;
                    case 3:
                        cmc.setCntpuccodigoCuenta(cntPucFacade.find(codCuentaImpuesto));
                        cmc.setAplica(idAfectaImpuesto);
                        break;
                    case 4:
                        cmc.setCntpuccodigoCuenta(cntPucFacade.find(codCuentaTotal));
                        cmc.setAplica(idAfectaTotal);
                        break;
                }
                cfgMovCtaFacade.edit(cmc);
            }
            esNuevoRegistro = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Registro Actualizado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Registro no Actualizado"));
        }

    }

    public int getIdTipoMovimiento() {
        return idTipoMovimiento;
    }

    public void setIdTipoMovimiento(int idTipoMovimiento) {
        this.idTipoMovimiento = idTipoMovimiento;
    }

    public int getIdFormaPago() {
        return idFormaPago;
    }

    public void setIdFormaPago(int idFormaPago) {
        this.idFormaPago = idFormaPago;
    }

    public String getCodCuentaSubtotal() {
        return codCuentaSubtotal;
    }

    public void setCodCuentaSubtotal(String codCuentaSubtotal) {
        this.codCuentaSubtotal = codCuentaSubtotal;
    }

    public int getIdAfectaSubutotal() {
        return idAfectaSubutotal;
    }

    public void setIdAfectaSubutotal(int idAfectaSubutotal) {
        this.idAfectaSubutotal = idAfectaSubutotal;
    }

    public String getCodCuentaDescuento() {
        return codCuentaDescuento;
    }

    public void setCodCuentaDescuento(String codCuentaDescuento) {
        this.codCuentaDescuento = codCuentaDescuento;
    }

    public int getIdAfectaDescuento() {
        return idAfectaDescuento;
    }

    public void setIdAfectaDescuento(int idAfectaDescuento) {
        this.idAfectaDescuento = idAfectaDescuento;
    }

    public String getCodCuentaImpuesto() {
        return codCuentaImpuesto;
    }

    public void setCodCuentaImpuesto(String codCuentaImpuesto) {
        this.codCuentaImpuesto = codCuentaImpuesto;
    }

    public int getIdAfectaImpuesto() {
        return idAfectaImpuesto;
    }

    public void setIdAfectaImpuesto(int idAfectaImpuesto) {
        this.idAfectaImpuesto = idAfectaImpuesto;
    }

    public String getCodCuentaTotal() {
        return codCuentaTotal;
    }

    public void setCodCuentaTotal(String codCuentaTotal) {
        this.codCuentaTotal = codCuentaTotal;
    }

    public int getIdAfectaTotal() {
        return idAfectaTotal;
    }

    public void setIdAfectaTotal(int idAfectaTotal) {
        this.idAfectaTotal = idAfectaTotal;
    }

}
