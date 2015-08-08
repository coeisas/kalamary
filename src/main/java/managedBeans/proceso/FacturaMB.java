/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.proceso;

import entities.CfgCliente;
import entities.CfgDocumento;
import entities.CfgEmpresasede;
import entities.CfgFormapago;
import entities.CfgImpuesto;
import entities.CfgProducto;
import entities.FacDocuementopago;
import entities.FacDocuementopagoPK;
import entities.FacDocumentodetalle;
import entities.FacDocumentodetallePK;
import entities.FacDocumentoimpuesto;
import entities.FacDocumentoimpuestoPK;
import entities.FacDocumentosmaster;
import entities.SegUsuario;
import facades.CfgClienteFacade;
import facades.CfgDocumentoFacade;
import facades.CfgFormapagoFacade;
import facades.CfgImpuestoFacade;
import facades.CfgProductoFacade;
import facades.FacDocuementopagoFacade;
import facades.FacDocumentodetalleFacade;
import facades.FacDocumentoimpuestoFacade;
import facades.FacDocumentosmasterFacade;
import java.util.List;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.primefaces.context.RequestContext;
import javax.faces.context.FacesContext;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import managedBeans.seguridad.SesionMB;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.LazyDataModel;
import utilities.LazyProductosModel;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class FacturaMB implements Serializable {

    private String identificacionCliente;
    private String nombreCliente;
    private float subtotal;
    private float totalDescuento;
    private float totalFactura;
    private LazyDataModel<CfgProducto> listaProducto;
    private List<FacDocumentodetalle> listaDetalle;
    private List<CfgCliente> listaClientes;
    private List<CfgImpuesto> listaImpuestos;
    private List<CfgFormapago> listaFormapagos;
//    private CfgEmpresa empresaActual;
    private CfgEmpresasede sedeActual;
    private CfgCliente clienteSeleccionado;
    private CfgProducto productoSeleccionado;
    private SegUsuario usuarioActual;
    private FacDocumentosmaster documentoActual;

    private SesionMB sesionMB;
    @EJB
    CfgClienteFacade clienteFacade;
    @EJB
    CfgImpuestoFacade impuestoFacade;
    @EJB
    CfgDocumentoFacade documentoFacade;
    @EJB
    CfgProductoFacade productoFacade;
    @EJB
    CfgFormapagoFacade formapagoFacade;
    @EJB
    FacDocumentosmasterFacade documentosmasterFacade;
    @EJB
    FacDocumentodetalleFacade documentodetalleFacade;
    @EJB
    FacDocumentoimpuestoFacade documentoimpuestoFacade;
    @EJB
    FacDocuementopagoFacade docuementopagoFacade;

    public FacturaMB() {

    }

    @PostConstruct
    private void init() {
        //si es super usuario o admin permitir escoger la empresa y la sede
        setListaDetalle((List<FacDocumentodetalle>) new ArrayList());
        setListaImpuestos((List<CfgImpuesto>) new ArrayList());
        FacesContext context = FacesContext.getCurrentInstance();
        sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
//        empresaActual = sesionMB.getSedeActual().getCfgempresaidEmpresa();
        sedeActual = sesionMB.getSedeActual();
        if (sedeActual != null) {
//            if (usuarioActual.getCfgempresasedeidSede() != null) {
//                empresaActual = usuarioActual.getCfgempresasedeidSede().getCfgempresaidEmpresa();
//            } else {
//                empresaActual = null;
//            }
            actualizarListadoClientes();
            listaProducto = new LazyProductosModel(sedeActual.getCfgempresaidEmpresa(), null, null, null, productoFacade);
            RequestContext.getCurrentInstance().update("FormModalProducto");
            clienteSeleccionado = clienteFacade.buscarClienteDefault(sedeActual.getCfgempresaidEmpresa());
            setListaImpuestos(impuestoFacade.buscarImpuestosPorTipoEmpresaAndSede(clienteSeleccionado.getCfgTipoempresaId(), sedeActual));
            listaFormapagos = formapagoFacade.buscarPorEmpresa(sedeActual.getCfgempresaidEmpresa());
            cargarInformacionCliente();
        }
    }

    public void buscarCliente() {
        if (!identificacionCliente.trim().isEmpty()) {
            clienteSeleccionado = clienteFacade.buscarPorIdentificacionAndIdEmpresa(identificacionCliente, sedeActual.getCfgempresaidEmpresa());
            if (clienteSeleccionado == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No se encontro cliente con esa identificacion"));
            }
        } else {
            clienteSeleccionado = null;
        }
        cargarInformacionCliente();
    }

    public void cargarInformacionCliente() {
        if (clienteSeleccionado != null) {
            setNombreCliente(clienteSeleccionado.nombreCompleto());
            setIdentificacionCliente(clienteSeleccionado.getNumDoc());
            setListaImpuestos(impuestoFacade.buscarImpuestosPorTipoEmpresaAndSede(clienteSeleccionado.getCfgTipoempresaId(), sedeActual));
        } else {
            setNombreCliente(null);
            listaDetalle.clear();
            listaImpuestos.clear();
            getListaImpuestos().clear();
        }
        RequestContext.getCurrentInstance().execute("PF('dlgCliente').hide()");
        RequestContext.getCurrentInstance().update("IdFormFactura:IdNomCliente");
        RequestContext.getCurrentInstance().update("IdFormFactura:IdSubTotal");
    }

    private void actualizarListadoClientes() {
        listaClientes = clienteFacade.buscarPorEmpresa(sedeActual.getCfgempresaidEmpresa());
    }

    public void deseleccionarCliente() {
        clienteSeleccionado = null;
        RequestContext.getCurrentInstance().update("FormBuscarCliente");
    }

    public void insertarItemProducto() {
        if (listaDetalle.isEmpty()) {
            subtotal = 0;
            totalDescuento = 0;
        }
        if (productoSeleccionado != null) {
            FacDocumentodetalle facdetalle = obtenerItemEnLista(productoSeleccionado);
            if (facdetalle == null) {
                facdetalle = new FacDocumentodetalle();
                facdetalle.setCfgProducto(productoSeleccionado);
                //el valor del documento master no es el definitivo
                facdetalle.setFacDocumentodetallePK(new FacDocumentodetallePK(productoSeleccionado.getIdProducto(), 1));
                facdetalle.setCantidad(1);
                facdetalle.setDescuento(0);
                float aux = productoSeleccionado.getPrecio();
                aux = Redondear(aux, 0);
                facdetalle.setValorUnitario(aux);
                facdetalle.setValorTotal(facdetalle.getCantidad() * facdetalle.getValorUnitario());
                listaDetalle.add(facdetalle);
            } else {
                subtotal -= facdetalle.getValorTotal();
                facdetalle.setCantidad(facdetalle.getCantidad() + 1);
                facdetalle.setValorTotal(facdetalle.getCantidad() * facdetalle.getValorUnitario());
            }
            subtotal += facdetalle.getValorTotal();
            calcularImpuesto();
            calcularTotalFactura();
            RequestContext.getCurrentInstance().update("IdFormFactura:IdTableItemFactura");
            RequestContext.getCurrentInstance().update("IdFormFactura:IdSubTotal");
        }
    }

    private FacDocumentodetalle obtenerItemEnLista(CfgProducto cfgProducto) {
        for (FacDocumentodetalle documentodetalle : listaDetalle) {
            if (documentodetalle.getCfgProducto().equals(cfgProducto)) {
                return documentodetalle;
            }
        }
        return null;
    }

    public void quitarItem(ActionEvent actionEvent) {
        FacDocumentodetalle item = (FacDocumentodetalle) actionEvent.getComponent().getAttributes().get("item");
        listaDetalle.remove(item);
        subtotal -= item.getValorTotal();
        if (listaDetalle.isEmpty()) {
            subtotal = 0;
            totalDescuento = 0;
        }
        calcularTotalDescuento();
        calcularImpuesto();
        calcularTotalFactura();
        RequestContext.getCurrentInstance().update("IdFormFactura:IdTableItemFactura");
        RequestContext.getCurrentInstance().update("IdFormFactura:IdSubTotal");
    }

    public void onCellEdit(CellEditEvent event) {
        int index = event.getRowIndex();
        subtotal -= listaDetalle.get(index).getValorTotal();
        listaDetalle.get(index).setValorTotal(listaDetalle.get(index).getCantidad() * listaDetalle.get(index).getValorUnitario());
        float porcentaje = listaDetalle.get(index).getDescuento() / (float) 100;
        float descuento = porcentaje * listaDetalle.get(index).getValorTotal();
        descuento = Redondear(descuento, 0);
        listaDetalle.get(index).setValorDescuento(descuento);
        subtotal += listaDetalle.get(index).getValorTotal();
        calcularImpuesto();
        calcularTotalDescuento();
        calcularTotalFactura();
        RequestContext.getCurrentInstance().update("IdFormFactura:IdSubTotal");
    }

    public void updateTabla() {
        RequestContext.getCurrentInstance().update("IdFormFactura:IdTableItemFactura");
    }

    private void calcularTotalDescuento() {
        totalDescuento = 0;
        for (FacDocumentodetalle documentodetalle : listaDetalle) {
            totalDescuento += documentodetalle.getValorDescuento();
        }
    }

    private void calcularImpuesto() {
        float aux;
        for (CfgImpuesto impuesto : listaImpuestos) {
            aux = impuesto.getPorcentaje() * subtotal / (float) 100;
            aux = Redondear(aux, 0);
            impuesto.setTotalImpuesto(aux);
        }
    }

    private void calcularTotalFactura() {
        totalFactura = subtotal - totalDescuento;
        for (CfgImpuesto impuesto : listaImpuestos) {
            totalFactura += impuesto.getTotalImpuesto();
        }
    }

    public float Redondear(float pNumero, int pCantidadDecimales) {
        BigDecimal value = new BigDecimal(pNumero);
        value = value.setScale(pCantidadDecimales, RoundingMode.HALF_UP);
        return value.floatValue();
    }

    private boolean validarCampos(CfgDocumento documento) {
        boolean ban = true;
        if (clienteSeleccionado == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay cliente seleccionado"));
            return false;
        }
        if (listaDetalle.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay items de factura"));
            return false;
        }
        if (documento == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay un documento que aplicado a factura"));
            return false;
        }
        if (documento.getFinDocumento() == documento.getActDocumento()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ha llegado al limite de la creacion de factura. Revice la configuracion de documentos"));
            return false;
        }
        if (documentosmasterFacade.buscarBySedeAndDocumentoAndNum(sedeActual, documento, documento.getActDocumento()) != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Consecutivo de factura duplicado. Revice la configuracion de documentos"));
            return false;
        }
        return ban;
    }

    public void guardarFactura() {
        CfgDocumento documento = documentoFacade.buscarDocumentoDeFacturaBySede(sedeActual);
        documento.setActDocumento(documento.getActDocumento() + 1);
        if (!validarCampos(documento)) {
            return;
        }
        try {
            FacDocumentosmaster documentosmaster = new FacDocumentosmaster();
            documentosmaster.setCfgclienteidCliente(clienteSeleccionado);
            documentosmaster.setCfgdocumentoidDoc(documento);
            documentosmaster.setNumDocumento(documento.getActDocumento());
            documentosmaster.setCfgempresasedeidSede(sedeActual);
            documentosmaster.setDescuento(totalDescuento);
            documentosmaster.setFecCrea(new Date());
            documentosmaster.setSegusuarioidUsuario(usuarioActual);
            documentosmaster.setSubtotal(subtotal);
            documentosmaster.setTotalFactura(totalFactura);
            documentosmaster.setEstado("PENDIENTE");
            documentosmasterFacade.create(documentosmaster);
            documentoFacade.edit(documento);
            for (FacDocumentodetalle documentodetalle : listaDetalle) {
                documentodetalle.setFacDocumentodetallePK(new FacDocumentodetallePK(documentodetalle.getCfgProducto().getIdProducto(), documentosmaster.getIddocumentomaster()));
                documentodetalle.setFacDocumentosmaster(documentosmaster);
                documentodetalleFacade.create(documentodetalle);
            }
            for (CfgImpuesto impuesto : listaImpuestos) {
                FacDocumentoimpuesto documentoimpuesto = new FacDocumentoimpuesto();
                documentoimpuesto.setFacDocumentoimpuestoPK(new FacDocumentoimpuestoPK(documentosmaster.getIddocumentomaster(), impuesto.getIdImpuesto()));
                documentoimpuesto.setCfgImpuesto(impuesto);
                documentoimpuesto.setFacDocumentosmaster(documentosmaster);
                documentoimpuesto.setValorImpuesto(impuesto.getTotalImpuesto());
                documentoimpuesto.setPorcentajeImpuesto(impuesto.getPorcentaje());
                documentoimpuestoFacade.create(documentoimpuesto);
            }
            for (CfgFormapago formapago : listaFormapagos) {
                if (formapago.getSubtotal() > 0) {
                    FacDocuementopago docuementopago = new FacDocuementopago();
                    docuementopago.setFacDocuementopagoPK(new FacDocuementopagoPK(documentosmaster.getIddocumentomaster(), formapago.getIdFormaPago()));
                    docuementopago.setCfgFormapago(formapago);
                    docuementopago.setFacDocumentosmaster(documentosmaster);
                    docuementopago.setValorPago(formapago.getSubtotal());
                    docuementopagoFacade.create(docuementopago);
                }
            }
            documentoActual = documentosmaster;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion", "Factura creada"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Factura no creada"));
        }
    }

    public void facturacion() {
        if (!listaDetalle.isEmpty()) {
            RequestContext.getCurrentInstance().execute("PF('dlgFormaPago').show()");
            RequestContext.getCurrentInstance().update("FormModalFactura");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay items de factura"));
        }
    }

    public void onRowEdit(RowEditEvent event) {
        CfgFormapago formapago = (CfgFormapago) event.getObject();
        float aux = totalFormaPago();
        if (formapago.getSubtotal() < 0 || !validarFormaPago(aux)) {
            formapago.setSubtotal(0);
        }
    }

    public void onRowCancel(RowEditEvent event) {
//        FacesMessage msg = new FacesMessage("Edit Cancelled", ((CfgFormapago) event.getObject()).getNomFormaPago());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
        CfgFormapago formapago = (CfgFormapago) event.getObject();
        formapago.setSubtotal(0);
    }

    private float totalFormaPago() {
        float aux = 0;
        for (CfgFormapago formapago : listaFormapagos) {
            aux += formapago.getSubtotal();
        }
        return aux;
    }

    private boolean validarFormaPago(float aux) {
        return aux <= totalFactura;
    }

    public void actualizarTablaFormaPago() {
        RequestContext.getCurrentInstance().update("FormModalFactura:IdTableFormaPago");
    }

    public void imprimir() {
        float aux = totalFormaPago();
        if (aux < totalFactura) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No coincide el valor total de la factura con los valores ingresados en las formas de pago"));
            return;
        }
        guardarFactura();
    }

    private void limpiarFormulario() {
        listaDetalle.clear();
        clienteSeleccionado = clienteFacade.buscarClienteDefault(sedeActual.getCfgempresaidEmpresa());
    }

    public List<FacDocumentodetalle> getListaDetalle() {
        return listaDetalle;
    }

    public void setListaDetalle(List<FacDocumentodetalle> listaDetalle) {
        this.listaDetalle = listaDetalle;
    }

    public CfgCliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(CfgCliente clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
    }

    public List<CfgCliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<CfgCliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public String getIdentificacionCliente() {
        return identificacionCliente;
    }

    public void setIdentificacionCliente(String identificacionCliente) {
        this.identificacionCliente = identificacionCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public LazyDataModel<CfgProducto> getListaProducto() {
        return listaProducto;
    }

    public void setListaProducto(LazyDataModel<CfgProducto> listaProducto) {
        this.listaProducto = listaProducto;
    }

    public CfgProducto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(CfgProducto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getTotalDescuento() {
        return totalDescuento;
    }

    public void setTotalDescuento(float totalDescuento) {
        this.totalDescuento = totalDescuento;
    }

    public List<CfgImpuesto> getListaImpuestos() {
        return listaImpuestos;
    }

    public void setListaImpuestos(List<CfgImpuesto> listaImpuestos) {
        this.listaImpuestos = listaImpuestos;
    }

    public float getTotalFactura() {
        return totalFactura;
    }

    public void setTotalFactura(float totalFactura) {
        this.totalFactura = totalFactura;
    }

    public List<CfgFormapago> getListaFormapagos() {
        return listaFormapagos;
    }

    public void setListaFormapagos(List<CfgFormapago> listaFormapagos) {
        this.listaFormapagos = listaFormapagos;
    }

}
