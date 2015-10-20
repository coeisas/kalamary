/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.movimientoCaja;

import entities.CfgCliente;
import entities.CfgDocumento;
import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.CfgFormapago;
import entities.CfgKitproductodetalle;
import entities.CfgKitproductomaestro;
import entities.CfgMovInventarioDetalle;
import entities.CfgMovInventarioMaestro;
import entities.CfgProducto;
import entities.FacCaja;
import entities.FacCarteraCliente;
import entities.FacCarteraDetalle;
import entities.FacCarteraDetallePK;
import entities.FacDocuementopago;
import entities.FacDocuementopagoPK;
import entities.FacDocumentodetalle;
import entities.FacDocumentodetallePK;
import entities.FacDocumentoimpuesto;
import entities.FacDocumentoimpuestoPK;
import entities.FacDocumentosmaster;
import entities.FacDocumentosmasterPK;
import entities.FacMovcaja;
import entities.FacMovcajadetalle;
import entities.FacMovcajadetallePK;
import entities.InvConsolidado;
import entities.InvMovimiento;
import entities.InvMovimientoDetalle;
import entities.InvMovimientoDetallePK;
import entities.InvMovimientoPK;
import entities.SegUsuario;
import facades.CfgClienteFacade;
import facades.CfgDocumentoFacade;
import facades.CfgFormapagoFacade;
import facades.CfgKitproductodetalleFacade;
import facades.CfgMovInventarioDetalleFacade;
import facades.CfgMovInventarioMaestroFacade;
import facades.FacCajaFacade;
import facades.FacCarteraClienteFacade;
import facades.FacCarteraDetalleFacade;
import facades.FacDocuementopagoFacade;
import facades.FacDocumentodetalleFacade;
import facades.FacDocumentoimpuestoFacade;
import facades.FacDocumentosmasterFacade;
import facades.FacMovcajaFacade;
import facades.FacMovcajadetalleFacade;
import facades.InvConsolidadoFacade;
import facades.InvMovimientoDetalleFacade;
import facades.InvMovimientoFacade;
import facades.SegUsuarioFacade;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.ejb.EJB;
import java.util.List;
import javax.faces.application.FacesMessage;
import java.util.Date;
import java.util.Map;
import javax.servlet.ServletContext;
import managedBeans.seguridad.SesionMB;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.LazyDataModel;
import utilities.LazyClienteDataModel;
import utilities.ReciboCajaReporte;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import java.util.Calendar;
import utilities.AuxilarMovInventario;
import utilities.FacturaPagoReporte;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class AbonoMB implements Serializable {

    private String identificacionCliente;
    private String nombreCliente;
//    private int cuotas;
    private Date fechaLimite;
    private float total;
    private float saldo;
    private float abono;

    private boolean enableBtnGuardar;

    private CfgCliente clienteSeleccionado;
    private FacCarteraCliente carteraSeleccionada;
    private FacMovcaja movimientoCajaMaster;//contiene informacion del maestro del movimiento. Se crea uno cada vez que se habre caja. cuando se cierra se habilita

    private List<CfgFormapago> listaFormapagos;
    private LazyDataModel<CfgCliente> listaClientes;
    private List<FacCarteraCliente> listaCarteras;
    private List<FacCarteraDetalle> listaDetalleCartera;
    private List<AuxilarMovInventario> listaAuxiliarInventario;

    private CfgEmpresa empresaActual;
    private CfgEmpresasede sedeActual;
    private SegUsuario usuarioActual;
    private FacCaja cajaUsuario;
    private FacDocumentosmaster reciboReciente;

    @EJB
    CfgClienteFacade clienteFacade;
    @EJB
    FacCarteraClienteFacade carteraClienteFacade;
    @EJB
    FacCarteraDetalleFacade carteraDetalleFacade;
    @EJB
    FacCajaFacade cajaFacade;
    @EJB
    SegUsuarioFacade usuarioFacade;
    @EJB
    FacMovcajaFacade movcajamaestroFacade;
    @EJB
    FacMovcajadetalleFacade movcajadetalleFacade;
    @EJB
    CfgDocumentoFacade documentoFacade;//buscar el tipo de docuemnto a usar(recibo de caja)
    @EJB
    FacDocumentosmasterFacade documentosmasterFacade;
    @EJB
    CfgFormapagoFacade formapagoFacade;
    @EJB
    FacDocumentodetalleFacade documentodetalleFacade;
    @EJB
    FacDocuementopagoFacade docuementopagoFacade;
    @EJB
    FacDocumentoimpuestoFacade documentoimpuestoFacade;
    @EJB
    InvConsolidadoFacade consolidadoInventarioFacade;
    @EJB
    CfgKitproductodetalleFacade kitDetalleFacade;
    @EJB
    CfgMovInventarioMaestroFacade movInventarioMaestroFacade;
    @EJB
    CfgMovInventarioDetalleFacade movInventarioDetalleFacade;
    @EJB
    InvMovimientoFacade inventarioMovimientoMaestroFacade;
    @EJB
    InvMovimientoDetalleFacade inventarioMovimientoDetalleFacade;

    public AbonoMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        empresaActual = sesionMB.getEmpresaActual();
        sedeActual = sesionMB.getSedeActual();
        usuarioActual = sesionMB.getUsuarioActual();
        if (usuarioActual != null) {
            cajaUsuario = usuarioActual.getFaccajaidCaja();
        }
    }

    public void cargarClientes() {
        if (empresaActual != null) {
            listaClientes = new LazyClienteDataModel(clienteFacade, empresaActual);
            RequestContext.getCurrentInstance().update("FormBuscarCliente");
            RequestContext.getCurrentInstance().execute("PF('dlgCliente').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro informacion de la empresa"));
        }
    }

    public void buscarCliente() {
        if (!identificacionCliente.trim().isEmpty()) {
            clienteSeleccionado = clienteFacade.buscarPorIdentificacionAndIdEmpresa(identificacionCliente, empresaActual);
            if (clienteSeleccionado == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "No se encontro cliente con esa identificacion"));
            }
        } else {
            clienteSeleccionado = null;
        }
        cargarInformacionCliente();
    }

    public void cargarInformacionCliente() {
        limpiar();
        if (clienteSeleccionado != null) {
            nombreCliente = clienteSeleccionado.nombreCompleto();
            setIdentificacionCliente(clienteSeleccionado.getNumDoc());
        } else {
            nombreCliente = null;
        }
        RequestContext.getCurrentInstance().execute("PF('dlgCliente').hide()");
        RequestContext.getCurrentInstance().update("IdFormAbono");
    }

    public void cargarSeparardos() {
        if (clienteSeleccionado != null) {
            CfgDocumento documento = documentoFacade.buscarDocumentoDeReciboCajaBySede(sedeActual);
            if (documento != null) {
                listaCarteras = carteraClienteFacade.buscarPorCliente(clienteSeleccionado);
                RequestContext.getCurrentInstance().update("FormBuscarSeparado");
                RequestContext.getCurrentInstance().execute("PF('dlgSeparados').show()");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro un documento vigente que aplique a recibo de caja"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No ha seleccionado un cliente"));
        }
    }

    public void cargarCartera() {
        if (carteraSeleccionada != null) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", carteraSeleccionada.getFacDocumentosmaster().determinarNumFactura()));
            total = carteraSeleccionada.getValor();
            saldo = carteraSeleccionada.getSaldo();
            fechaLimite = carteraSeleccionada.getFechaLimite();
            RequestContext.getCurrentInstance().execute("PF('dlgSeparados').hide()");
            listaDetalleCartera = carteraDetalleFacade.buscarPorCartera(carteraSeleccionada);
            RequestContext.getCurrentInstance().update("IdFormAbono");
        }

    }

    public void limpiar() {
        listaFormapagos = formapagoFacade.buscarPorEmpresa(empresaActual);
        listaDetalleCartera = null;
        carteraSeleccionada = null;
        total = 0;
        saldo = 0;
        fechaLimite = null;
//        cuotas = 0;
    }

    public void actualizarTablaFormaPago() {
        RequestContext.getCurrentInstance().update("FormModalPago:IdTableFormaPago");
        RequestContext.getCurrentInstance().update("FormModalPago:IdBtnPrint");
    }

    public void determinarValorFormaPago() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        int idFormaPago = Integer.valueOf(params.get("idPago"));
        float aux = totalFormaPago();
        //A LA CELDA SELECCINADA  Y SI EL SUBTOTAL ES 0 SE ENVIA EL VALOR DEL SALDO
        for (CfgFormapago fpago : listaFormapagos) {
            if (fpago.getIdFormaPago() == idFormaPago && fpago.getSubtotal() == 0) {
                fpago.setSubtotal(saldo - aux);
                break;
            }
        }
        aux = totalFormaPago();
        enableBtnGuardar = (aux <= saldo && aux != 0);
        RequestContext.getCurrentInstance().update("FormModalPago:IdTableFormaPago");
        RequestContext.getCurrentInstance().update("FormModalPago:IdBtnPrint");
    }

    public void onRowEdit(RowEditEvent event) {
        CfgFormapago formapago = (CfgFormapago) event.getObject();
        float aux = totalFormaPago();//total de las formas de pago
        if (formapago.getSubtotal() < 0 || !(aux <= saldo)) {
            formapago.setSubtotal(0);
        }
        //EL VALOR DEL ABONO DEBE SER INFERIOR O IGUAL AL SALDO Y DIFERENTE DE CERO
        enableBtnGuardar = (aux <= saldo && aux != 0);
    }

    public void onRowCancel(RowEditEvent event) {
//        FacesMessage msg = new FacesMessage("Edit Cancelled", ((CfgFormapago) event.getObject()).getNomFormaPago());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
        CfgFormapago formapago = (CfgFormapago) event.getObject();
        formapago.setSubtotal(0);
        float aux = totalFormaPago();
        //EL VALOR DEL ABONO DEBE SER INFERIOR O IGUAL AL SALDO Y DIFERENTE DE CERO
        enableBtnGuardar = (aux <= saldo && aux != 0);
    }

    private float totalFormaPago() {
        float aux = 0;
        for (CfgFormapago formapago : listaFormapagos) {
            aux += formapago.getSubtotal();
        }
        return aux;
    }

    public void cargarFormularioPago() {
        if (carteraSeleccionada != null) {
            if (carteraSeleccionada.getEstado().equals("PENDIENTE")) {
                RequestContext.getCurrentInstance().execute("PF('dlgFormaPago').show()");
                RequestContext.getCurrentInstance().update("FormModalPago");
            } else {
                if (carteraSeleccionada.getEstado().equals("FINALIZADA")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "El separado seleccionado no tiene cuotas pendientes"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "El separado seleccionado ha caducado"));
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione el separado a abonar"));
        }

    }

    private boolean validar() {
        boolean ban = true;
        if (usuarioActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro informacion del usuario actual"));
            return false;
        }
        if (cajaUsuario == null) {
            //se vuelve a buscar la caja del usario. Por si el usuario la creo en el transcurso de la sesion
            usuarioActual = usuarioFacade.find(usuarioActual.getIdUsuario());
            cajaUsuario = usuarioActual.getFaccajaidCaja();
            if (cajaUsuario == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El usuario no tiene caja asignada"));
                return false;
            } else {
                usuarioActual.setFaccajaidCaja(cajaUsuario);
            }

        }
        if (sedeActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro informacion de la sede"));
            return false;
        }
        movimientoCajaMaster = movcajamaestroFacade.buscarMovimientoCaja(cajaUsuario);
        if (movimientoCajaMaster == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Abra la caja por primera vez"));
            return false;
        }
        if (!movimientoCajaMaster.getAbierta()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La caja no esta abierta"));
            return false;
        }
        if (clienteSeleccionado == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione un cliente"));
            return false;
        }
        if (carteraSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione el separado a abonar"));
            return false;
        }
        abono = totalFormaPago();
        if (abono <= 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El valor del abono no puede ser menor que cero"));
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        if (carteraSeleccionada.getFechaLimite() != null) {
            calendar.setTime(carteraSeleccionada.getFechaLimite());
        }
        calendar.add(Calendar.DATE, 1);
        Date fechaActual = new Date();
        if (fechaActual.after(calendar.getTime())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ha excedido la fecha limite"));
            carteraSeleccionada.setEstado("CADUCADA");
            carteraClienteFacade.edit(carteraSeleccionada);
            return false;
        }
        carteraSeleccionada.setCuotaactual(carteraSeleccionada.getCuotaactual() + 1);
        return ban;
    }

    private boolean validarDocumento(CfgDocumento documento) {
        boolean ban = true;

        if (documento.getFinDocumento() < documento.getActDocumento()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ha llegado al limite de la creacion de factura. Revice la configuracion de documentos"));
            return false;
        }
        if (documentosmasterFacade.buscarBySedeAndDocumentoAndNum(sedeActual, documento.getIdDoc(), documento.getActDocumento()) != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Consecutivo de Recibo de caja duplicado. Revice la configuracion de documentos"));
            return false;
        }
        return ban;
    }

    public void guardar() {
        if (!validar()) {
            return;
        }
        try {
            //SE CREA UN RECIBO DE CAJA PARA EL ABONADO. DEBE EXISTIR UN DOCUMENTO APLICADO A RECIBO DE CAJA
            CfgDocumento documento = documentoFacade.buscarDocumentoDeReciboCajaBySede(sedeActual);
            if (documento == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay un documento existente ó sin finalizar aplicado a recibo de caja"));
                return;
            }
            if (documento.getActDocumento() == 0) {
                documento.setActDocumento(documento.getIniDocumento());
            } else {
                documento.setActDocumento(documento.getActDocumento() + 1);
            }
            if (!validarDocumento(documento)) {
                return;
            }
            //CREACION DEL RECIBO DE CAJA CUYO VALOR ES EL ABONO Y HARA REFERENCIA AL SEPARADO SELECCIONADO
            FacDocumentosmaster documentosmaster = new FacDocumentosmaster();
            documentosmaster.setFacDocumentosmasterPK(new FacDocumentosmasterPK(documento.getIdDoc(), documento.getActDocumento()));
            documentosmaster.setCfgDocumento(documento);
            documentosmaster.setCfgclienteidCliente(clienteSeleccionado);
            documentosmaster.setFaccajaidCaja(cajaUsuario);
            documentosmaster.setCfgempresasedeidSede(sedeActual);
            documentosmaster.setFecCrea(new Date());
            documentosmaster.setSegusuarioidUsuario(usuarioActual);
            documentosmaster.setTotal(abono);
            documentosmaster.setObservaciones("ABONO A " + carteraSeleccionada.getFacDocumentosmaster().determinarNumFactura());
            //AQUI SE RELACIONA EL RECIBO DE CAJA CON EL SEPARADO QUE ESTA EMBEBIDO EN LA CARTERA SELECCIONADA
            documentosmaster.setFacDocumentosmaster(carteraSeleccionada.getFacDocumentosmaster());
            documentosmasterFacade.create(documentosmaster);//SE CREA EL RECIBO DE CAJA

            //SE ACTUALIZA EL CONCECUTIVO DE RECIBO DE CAJA
            if (documento.getActDocumento() == documento.getFinDocumento()) {
                documento.setFinalizado(true);
                documento.setActivo(false);
            }
            documentoFacade.edit(documento);

            //SE CREA EL NUEVO DETALLE DE LA FACTURA CORRESPONDIENTE AL ABONADO Y REFERENCIA AL RECIBO DE CAJA CREADO
            FacCarteraDetalle carteraDetalle = new FacCarteraDetalle();
            carteraDetalle.setFacCarteraDetallePK(new FacCarteraDetallePK(clienteSeleccionado.getIdCliente(), carteraSeleccionada.getFacCarteraClientePK().getFacdocumentosmastercfgdocumentoidDoc(), carteraSeleccionada.getFacCarteraClientePK().getFacdocumentosmasternumDocumento(), new Date()));
            carteraDetalle.setFacDocumentosmaster(documentosmaster);
            carteraDetalle.setAbono(abono);
            carteraDetalle.setFacCarteraCliente(carteraSeleccionada);
            carteraDetalle.setSaldo(carteraSeleccionada.getSaldo() - abono);
            carteraDetalleFacade.create(carteraDetalle);

            //SE ACTUALIZA: EL NUEVO SALDO DE LA CARTERA, LA CUOTA ACTUAL Y SE ENLAZA EL NUEVO DETALLE DE LA CARTERA 
            carteraSeleccionada.setSaldo(carteraDetalle.getSaldo());

            //SE CREA EL RESPECTIVO MOVIMIENTO DE CAJA POR CADA FORMA DE PAGO APLICADA
            List<FacDocuementopago> formasPagoRC = new ArrayList();
            for (CfgFormapago formapago : listaFormapagos) {
                if (formapago.getSubtotal() != 0) {
                    //ASOCIAMOS AL RECIBO DE CAJA CADA FORMA DE PAGO QUE SE UTILIZO PARA PAGAR EL ABONO
                    FacDocuementopago fpagoRC = new FacDocuementopago();
                    fpagoRC.setFacDocuementopagoPK(new FacDocuementopagoPK(formapago.getIdFormaPago(), documentosmaster.getFacDocumentosmasterPK().getCfgdocumentoidDoc(), documentosmaster.getFacDocumentosmasterPK().getNumDocumento()));
                    fpagoRC.setCfgFormapago(formapago);
                    fpagoRC.setValorPago(formapago.getSubtotal());
                    fpagoRC.setFacDocumentosmaster(documentosmaster);
                    docuementopagoFacade.create(fpagoRC);
                    formasPagoRC.add(fpagoRC);

                    //REGISTRAMOS EL MOVIMIENTO DE CADA FORMA DE PAGO EN LOS MOVIMIENTOS DE LA CAJA
                    FacMovcajadetalle movcajadetalle = new FacMovcajadetalle();
                    movcajadetalle.setFacMovcajadetallePK(new FacMovcajadetallePK(movimientoCajaMaster.getIdMovimiento(), documentosmaster.getFacDocumentosmasterPK().getCfgdocumentoidDoc(), documentosmaster.getFacDocumentosmasterPK().getNumDocumento(), formapago.getIdFormaPago()));
                    movcajadetalle.setFacMovcaja(movimientoCajaMaster);
                    movcajadetalle.setFacDocumentosmaster(documentosmaster);
                    movcajadetalle.setCfgFormapago(formapago);
                    movcajadetalle.setFecha(new Date());
                    movcajadetalle.setValor(formapago.getSubtotal());
                    movcajadetalleFacade.create(movcajadetalle);
                }
            }

            //SI EL SALDO DE LA CARTERA ES CERO SE DA POR FINALIZADA
            if (carteraSeleccionada.getSaldo() == 0) {
                carteraSeleccionada.setEstado("FINALIZADA");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "Separado finalizado. Cree la factura"));
                RequestContext.getCurrentInstance().update("msg");
            }

            //SE ACTUALIZA LA CARTERA IMPLICADA
            carteraClienteFacade.edit(carteraSeleccionada);

            //ACTUALIZAOMO EL RECIBO DE CAJA CON SUS FORMAS DE PAGO
            documentosmaster.setFacDocuementopagoList(formasPagoRC);
            documentosmasterFacade.edit(documentosmaster);

            //se carga la cartera nuevamente. se debe reflejar el pago del abononado creado
            cargarCartera();
            RequestContext.getCurrentInstance().execute("PF('dlgFormaPago').hide()");
            listaFormapagos = formapagoFacade.buscarPorEmpresa(empresaActual);
            reciboReciente = documentosmaster;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Abono registrado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se registro el abono"));
        }

    }

    public void cargarModalRC() {
        if (reciboReciente != null) {
            RequestContext.getCurrentInstance().update("FormModalRC");
            RequestContext.getCurrentInstance().execute("PF('dlgResult').show()");

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se econtro un recibo de caja creado recientemente"));
        }

    }

    public void generarRC() throws IOException, JRException {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String ruta = servletContext.getRealPath("/movimientoCaja/reportes/reciboCaja.jasper");
        byte[] bites = sedeActual.getLogo();
        if (bites == null) {
            bites = sedeActual.getCfgempresaidEmpresa().getLogo();
        }
        List<ReciboCajaReporte> listaReciboCaja = new ArrayList();
        ReciboCajaReporte reciboCajaReporte = new ReciboCajaReporte();
        reciboCajaReporte.setConcecutivo(reciboReciente.determinarNumFactura());
        reciboCajaReporte.setCiudad(sedeActual.getCfgMunicipio().getNomMunicipio());
        reciboCajaReporte.setFecha(reciboReciente.getFecCrea());
        reciboCajaReporte.setValor(reciboReciente.getTotal());
        reciboCajaReporte.setProtagonista(reciboReciente.getCfgclienteidCliente().nombreCompleto());
        String separado = reciboReciente.getFacDocumentosmaster().determinarNumFactura();
        FacCarteraCliente facCarteraCliente = carteraClienteFacade.buscarPorDocumentoMaestro(reciboReciente.getFacDocumentosmaster());
        String pendiente = String.valueOf(facCarteraCliente.getSaldo());
        reciboCajaReporte.setConcepto("ABONO A " + separado + " SALDO $" + pendiente);
        reciboCajaReporte.setFormaPago(crearListadoPago(docuementopagoFacade.buscarByDocumentoMaster(reciboReciente)));
        listaReciboCaja.add(reciboCajaReporte);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listaReciboCaja);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String rutaReportes = servletContext.getRealPath("/movimientoCaja/reportes/");//ubicacion para los subreportes
            Map<String, Object> parametros = new HashMap<>();
            if (bites != null) {
                InputStream logo = new ByteArrayInputStream(bites);
                parametros.put("logo", logo);
            }
            parametros.put("empresa", empresaActual.getNomEmpresa() + " - " + sedeActual.getNomSede());
            parametros.put("direccion", sedeActual.getDireccion());
            String telefono = sedeActual.getTel1();
            if (sedeActual.getTel2() != null && !sedeActual.getTel2().isEmpty()) {
                telefono = telefono + "-".concat(sedeActual.getTel2());
            }
            parametros.put("telefono", telefono);
            parametros.put("ubicacion", sedeActual.getCfgMunicipio().getNomMunicipio() + " " + sedeActual.getCfgMunicipio().getCfgDepartamento().getNomDepartamento());
            parametros.put("accion", "RECIBÍ DE:");
            parametros.put("SUBREPORT_DIR", rutaReportes);
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    private List<FacturaPagoReporte> crearListadoPago(List<FacDocuementopago> pagos) {
        List<FacturaPagoReporte> list = new ArrayList();
        for (FacDocuementopago pago : pagos) {
            FacturaPagoReporte facturaPagoReporte = new FacturaPagoReporte();
            facturaPagoReporte.setFormaPago(pago.getCfgFormapago().getNomFormaPago());
            facturaPagoReporte.setValorPago(pago.getValorPago());
            list.add(facturaPagoReporte);
        }
        return list;
    }

    public void cargarFactura() {
        if (carteraSeleccionada != null) {
            if (!carteraSeleccionada.getEstado().equals("PENDIENTE")) {//SOLO LAS FACTURAS FINALIZADAS SE PUEDEN FACTURAR
                //se valida que el separado no tenga una factura
                FacDocumentosmaster separado = carteraSeleccionada.getFacDocumentosmaster();
                FacDocumentosmaster factura = documentosmasterFacade.buscarFacturaSeparado(separado);
                if (factura == null) {//se crea una factura asociada al separado. (esta finalizado) saldo = 0
                    crearFactura(separado);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "Ya esta existe una factura para este separado. " + factura.determinarNumFactura()));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El separado no esta pagado en su totalidad"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione el separado a facturar"));
        }

    }

    private void crearFactura(FacDocumentosmaster separado) {
        CfgDocumento documentoConcecutivoFactura = documentoFacade.buscarDocumentoDeFacturaBySede(sedeActual);
        CfgDocumento documentoConcecutivoInventarioSalida = documentoFacade.buscarDocumentoInventarioSalidaBySede(sedeActual);
        //los documentos aplicados a factura y salida inventario deben estar vigentes
        if (documentoConcecutivoFactura == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "No hay un documento vigente aplicado a factura"));
            return;
        }
        if (documentoConcecutivoInventarioSalida == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "No hay un documento vigente aplicado a salida de inventario"));
            return;
        }
        if (documentoConcecutivoFactura.getActDocumento() == 0) {
            documentoConcecutivoFactura.setActDocumento(documentoConcecutivoFactura.getIniDocumento());
        } else {
            documentoConcecutivoFactura.setActDocumento(documentoConcecutivoFactura.getActDocumento() + 1);
        }
        if (!validarDocumentoAplicacion(documentoConcecutivoFactura, "Factura")) {
            return;
        }
        listaAuxiliarInventario = new ArrayList();//lista auxilar donde se guardan el producto y la cantidad a descontar del inventario
        if (!validarExistenciaEnInventarioDelSeparado(separado)) {
            return;
        }
        try {
            //creando el documento master de factura
            FacDocumentosmaster factura = new FacDocumentosmaster();
            factura.setFacDocumentosmasterPK(new FacDocumentosmasterPK(documentoConcecutivoFactura.getIdDoc(), documentoConcecutivoFactura.getActDocumento()));
            factura.setCfgDocumento(documentoConcecutivoFactura);
            factura.setCfgclienteidCliente(separado.getCfgclienteidCliente());
            factura.setCfgempresasedeidSede(sedeActual);
            factura.setFecCrea(new Date());
            factura.setTotal(separado.getTotal());
            factura.setTotalUSD(separado.getTotalUSD());
            factura.setSubtotal(separado.getSubtotal());
            factura.setDescuento(separado.getDescuento());
            factura.setUtilidad(separado.getUtilidad());
            factura.setSegusuarioidUsuario1(separado.getSegusuarioidUsuario1());
            factura.setEstado("CANCELADA");
            factura.setSegusuarioidUsuario(usuarioActual);
            factura.setFaccajaidCaja(cajaUsuario);
            factura.setFacDocumentosmaster(separado);
            documentosmasterFacade.create(factura);

            //el detalle del separado corresponde a la de la factura
            List<FacDocumentodetalle> listaDetalleFactura = new ArrayList();
            List<FacDocumentodetalle> listaDetalleSeparado = documentodetalleFacade.buscarByDocumentoMaster(separado);
            for (FacDocumentodetalle detalleSeparado : listaDetalleSeparado) {
                FacDocumentodetalle facturaDetalle = new FacDocumentodetalle();
                facturaDetalle.setFacDocumentodetallePK(new FacDocumentodetallePK(detalleSeparado.getCfgProducto().getIdProducto(), factura.getFacDocumentosmasterPK().getCfgdocumentoidDoc(), factura.getFacDocumentosmasterPK().getNumDocumento()));
                facturaDetalle.setFacDocumentosmaster(factura);
                facturaDetalle.setCfgProducto(detalleSeparado.getCfgProducto());
                facturaDetalle.setCantidad(detalleSeparado.getCantidad());
                facturaDetalle.setValorUnitario(detalleSeparado.getValorUnitario());
                facturaDetalle.setValorTotal(detalleSeparado.getValorTotal());
                facturaDetalle.setDescuento(detalleSeparado.getDescuento());
                documentodetalleFacade.create(facturaDetalle);
                listaDetalleFactura.add(facturaDetalle);
            }

            //los impuestos de la separado corresponderan a los de la factura
            List<FacDocumentoimpuesto> listaImpuestosFactura = new ArrayList();
            List<FacDocumentoimpuesto> listaImpuestosSeparado = documentoimpuestoFacade.buscarByDocumentoMaster(separado);
            for (FacDocumentoimpuesto impuestoSeparado : listaImpuestosSeparado) {
                FacDocumentoimpuesto impuestoFactura = new FacDocumentoimpuesto();
                impuestoFactura.setFacDocumentoimpuestoPK(new FacDocumentoimpuestoPK(impuestoSeparado.getCfgImpuesto().getIdImpuesto(), factura.getFacDocumentosmasterPK().getCfgdocumentoidDoc(), factura.getFacDocumentosmasterPK().getNumDocumento()));
                impuestoFactura.setFacDocumentosmaster(factura);
                impuestoFactura.setCfgImpuesto(impuestoSeparado.getCfgImpuesto());
                impuestoFactura.setValorImpuesto(impuestoSeparado.getValorImpuesto());
                impuestoFactura.setPorcentajeImpuesto(impuestoSeparado.getPorcentajeImpuesto());
                documentoimpuestoFacade.create(impuestoFactura);
                listaImpuestosFactura.add(impuestoFactura);
            }

            factura.setFacDocumentodetalleList(listaDetalleFactura);
            factura.setFacDocumentoimpuestoList(listaImpuestosFactura);
            documentosmasterFacade.edit(factura);

            documentoFacade.edit(documentoConcecutivoFactura);

            //ACTULIZAR INVENTARIO CONSOLIDADO
            for (AuxilarMovInventario auxilarMovInventario : listaAuxiliarInventario) {
                InvConsolidado inventarioConsolidado = consolidadoInventarioFacade.buscarBySedeAndProducto(sedeActual, auxilarMovInventario.getProducto());
                inventarioConsolidado.setExistencia(inventarioConsolidado.getExistencia() - auxilarMovInventario.getCantidad());
                inventarioConsolidado.setFechaUltSalida(new Date());
                inventarioConsolidado.setSalidas(inventarioConsolidado.getSalidas() + auxilarMovInventario.getCantidad());
                consolidadoInventarioFacade.edit(inventarioConsolidado);
            }
            
            //SE CREA UN MOVIMIENTO DE INVENTARIO PARA LA FACTURA
            //ban sera true cuando el detalle incluya al menos un kit O un producto que no sea un servicio
            boolean ban = false;
            for (FacDocumentodetalle fd : listaDetalleFactura) {
                if (!fd.getCfgProducto().getEsServicio() || fd.getCfgProducto().getEsKit()) {
                    ban = true;
                    break;
                }
            }
            //si ban = true. se creara un movimiento de salida en el inventario.
            if (ban) {
                crearMovimientoInventario(listaDetalleFactura, factura);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Factura " + factura.determinarNumFactura() + " creada"));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Factura no creada"));
        }
    }

    private void crearMovimientoInventario(List<FacDocumentodetalle> listaDetalle, FacDocumentosmaster documentosmaster) {
//        BUSCA EL DOCUMENTO APLICADO AL MOVIMIENTO DE INVENTARIO DE SALIDA
        CfgDocumento documento = documentoFacade.buscarDocumentoInventarioSalidaBySede(sedeActual);
        if (documento.getActDocumento() == 0) {
            documento.setActDocumento(documento.getIniDocumento());
        } else {
            documento.setActDocumento(documento.getActDocumento() + 1);
        }
        CfgMovInventarioMaestro movInventarioMaestro = movInventarioMaestroFacade.buscarMovimientoSalida();
        CfgMovInventarioDetalle movInventarioDetalle = movInventarioDetalleFacade.buscarSalidaVentaByMaestro(movInventarioMaestro);
        try {
//            CREACION DEL MAESTRO MOVIMIENTO 
            InvMovimiento invMovimientoMaestro = new InvMovimiento();
            invMovimientoMaestro.setInvMovimientoPK(new InvMovimientoPK(documento.getIdDoc(), documento.getActDocumento()));
            invMovimientoMaestro.setCfgDocumento(documento);
            invMovimientoMaestro.setCfgempresasedeidSede(sedeActual);
            invMovimientoMaestro.setCfgmovinventariodetalleidMovInventarioDetalle(movInventarioDetalle);
            invMovimientoMaestro.setDescuento(documentosmaster.getDescuento());
            invMovimientoMaestro.setFacDocumentosmaster(documentosmaster);
            invMovimientoMaestro.setFecha(new Date());
//            invMovimientoMaestro.setIva(totalIva);
            invMovimientoMaestro.setSegusuarioidUsuario(usuarioActual);
            invMovimientoMaestro.setSubtotal(documentosmaster.getSubtotal());
            invMovimientoMaestro.setTotal(documentosmaster.getTotal());
            inventarioMovimientoMaestroFacade.create(invMovimientoMaestro);
            if (documento.getActDocumento() >= documento.getFinDocumento()) {//dependiendo de la situacion se finaliza el documento aplicado al movimiento de salida
                documento.setFinalizado(true);
            }
            documentoFacade.edit(documento);
            List<InvMovimientoDetalle> listaItemsInventarioMovimiento = new ArrayList();
//            CREACION DEL DETALLE MOVIMIENTO
            for (FacDocumentodetalle detalleFactura : listaDetalle) {
                if (!detalleFactura.getCfgProducto().getEsServicio()) {//si el producto no es un servicio se incluye en el detalle del movimiento de salida
                    InvMovimientoDetalle detalle = new InvMovimientoDetalle();
                    detalle.setInvMovimientoDetallePK(new InvMovimientoDetallePK(invMovimientoMaestro.getInvMovimientoPK().getCfgdocumentoidDoc(), invMovimientoMaestro.getInvMovimientoPK().getNumDoc(), detalleFactura.getCfgProducto().getIdProducto()));
                    detalle.setInvMovimiento(invMovimientoMaestro);
                    detalle.setCantidad(detalleFactura.getCantidad());
                    detalle.setCfgProducto(detalleFactura.getCfgProducto());
                    detalle.setCostoAdquisicion(detalleFactura.getValorUnitario());
                    detalle.setDescuento(detalleFactura.getDescuento());
                    detalle.setCostoFinal(detalleFactura.getValorTotal());
                    inventarioMovimientoDetalleFacade.create(detalle);
                    listaItemsInventarioMovimiento.add(detalle);
                }
            }

//            SE INCLUYE EL DETALLE DEL MOVIMIENTO AL MAESTRO
            invMovimientoMaestro.setInvMovimientoDetalleList(listaItemsInventarioMovimiento);
            inventarioMovimientoMaestroFacade.edit(invMovimientoMaestro);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha registrado el movimiento de salida para esta factura solo se desconto las unidades en inventario. Compruebe que exista un documento vigente para salida de inventario"));
        }
    }

    private boolean validarDocumentoAplicacion(CfgDocumento documento, String tipoDocumento) {
        boolean ban = true;
        if (clienteSeleccionado == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay cliente seleccionado"));
            return false;
        }
        if (documento.getFinDocumento() < documento.getActDocumento()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ha llegado al limite de la creacion de " + tipoDocumento + " . Revice la configuracion de documentos"));
            return false;
        }
        if (documentosmasterFacade.buscarBySedeAndDocumentoAndNum(sedeActual, documento.getIdDoc(), documento.getActDocumento()) != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Consecutivo de " + tipoDocumento + " duplicado. Revice la configuracion de documentos"));
            return false;
        }
        return ban;
    }

    //se valida que existan existencia para cada item del separado
    private boolean validarExistenciaEnInventarioDelSeparado(FacDocumentosmaster separado) {
        boolean ban = true;
        List<FacDocumentodetalle> listaDetalle = separado.getFacDocumentodetalleList();
        if (separado.getFacCarteraDetalleList().isEmpty()) {
            listaDetalle = documentodetalleFacade.buscarByDocumentoMaster(separado);
        }
        //iteracion de la lista detalle del separado. Para validar existencia
        for (FacDocumentodetalle detalleSeparado : listaDetalle) {
            CfgProducto producto = detalleSeparado.getCfgProducto();
            if (!producto.getEsServicio() && !producto.getEsKit()) {//si el producto no es un servicio y tampoco un kit. se busca existencia en inventario directamente
                if (!validarExistenciaRequeridaProducto(producto, detalleSeparado)) {
                    ban = false;
                    break;
                }
            } else if (producto.getEsKit()) {//si el producto es un kit se comprueba existencia de cada elemento que lo conforma
                CfgKitproductomaestro kit = producto.getCfgkitproductomaestroidKit();
                List<CfgKitproductodetalle> listadetallekit = kitDetalleFacade.buscarByMaestro(kit);
                boolean band2 = true;//bandera para salir del ciclo detalle kit
                for (CfgKitproductodetalle detalleKit : listadetallekit) {
                    producto = detalleKit.getCfgProducto();
                    if (!validarExistenciaRequeridaProductoKit(producto, detalleSeparado, detalleKit)) {
                        band2 = false;
                        break;
                    }
                }
                if (band2 == false) {
                    ban = false;
                    break;
                }
            }
        }
        return ban;
    }

    //para un producto individual. No incluido en un kit
    private boolean validarExistenciaRequeridaProducto(CfgProducto producto, FacDocumentodetalle detalleSeparado) {
        boolean ban = true;
        InvConsolidado consolidadoInventario = consolidadoInventarioFacade.buscarBySedeAndProducto(sedeActual, producto);
        if (consolidadoInventario == null) {//si el producto no esta en el inventario retorna false
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Existencias insuficientes para " + producto.getNomProducto()));
            return false;
        } else {//validacion entre unidades requeridas en el separado y unidades existentes en el inventario
            if (consolidadoInventario.getExistencia() == 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Existencias insuficientes para " + producto.getNomProducto()));
                return false;
            } else {//las existencias deben suplir la cantidad del detalle(separado)
                if (consolidadoInventario.getExistencia() < detalleSeparado.getCantidad()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Existencias insuficientes para " + producto.getNomProducto()));
                    return false;
                }
                int indx = buscarProductoEnListaAuxiliar(producto);//se recupera el index del producto en la lista. si es que ya esta insertado
                if (indx != -1) {//si el producto ya esta insertado se actualizara la cantidad a descontar en el inventario
                    int cantidadPrevia = listaAuxiliarInventario.get(indx).getCantidad();
                    int nuevaCantidad = cantidadPrevia + detalleSeparado.getCantidad();
                    if (consolidadoInventario.getExistencia() < nuevaCantidad) {//no hay existencia para suplir el separado
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Existencias insuficientes para " + producto.getNomProducto()));
                        return false;
                    }
                    listaAuxiliarInventario.get(indx).setCantidad(nuevaCantidad);
                } else {//se registra por primera vez el producto en la lista auxiliar
                    AuxilarMovInventario auxilarMovInventario = new AuxilarMovInventario();
                    auxilarMovInventario.setProducto(producto);
                    auxilarMovInventario.setCantidad(detalleSeparado.getCantidad());
                    listaAuxiliarInventario.add(auxilarMovInventario);
                }
            }
        }
        return ban;
    }

    //para un producto incluido en un kit. se tiene en cuenta la cantidad  determinada en el separado como la especificada en el kit
    private boolean validarExistenciaRequeridaProductoKit(CfgProducto producto, FacDocumentodetalle detalleSeparado, CfgKitproductodetalle detalleKit) {
        boolean ban = true;
        InvConsolidado consolidadoInventario = consolidadoInventarioFacade.buscarBySedeAndProducto(sedeActual, producto);
        if (consolidadoInventario == null) {//si el producto no esta en el inventario retorna false
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Existencias insuficientes para " + producto.getNomProducto()));
            return false;
        } else {//validacion entre unidades requeridas en el separado y unidades existentes en el inventario
            if (consolidadoInventario.getExistencia() == 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Existencias insuficientes para " + producto.getNomProducto()));
                return false;
            } else {//las existencias deben suplir la cantidad del detalle(separado) y la del kit
                if (consolidadoInventario.getExistencia() < (detalleSeparado.getCantidad() * (int) detalleKit.getCant())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Existencias insuficientes para " + producto.getNomProducto()));
                    return false;
                }
                int indx = buscarProductoEnListaAuxiliar(producto);//se recupera el index del producto en la lista. si es que ya esta insertado
                if (indx != -1) {//si el producto ya esta insertado se actualizara la cantidad a descontar en el inventario
                    int cantidadPrevia = listaAuxiliarInventario.get(indx).getCantidad();
                    //la nueva cantidad se ve comprometida por lo requerido en el separado como la cantidad establecida en el kit
                    int nuevaCantidad = cantidadPrevia + (detalleSeparado.getCantidad() * (int) detalleKit.getCant());
                    if (consolidadoInventario.getExistencia() < nuevaCantidad) {//no hay existencia para suplir el separado
                        return false;
                    }
                    listaAuxiliarInventario.get(indx).setCantidad(nuevaCantidad);
                } else {//se registra por primera vez el producto en la lista auxiliar
                    AuxilarMovInventario auxilarMovInventario = new AuxilarMovInventario();
                    auxilarMovInventario.setProducto(producto);
                    auxilarMovInventario.setCantidad(detalleSeparado.getCantidad() * (int) detalleKit.getCant());
                    listaAuxiliarInventario.add(auxilarMovInventario);
                }
            }
        }
        return ban;
    }

    //devuelve el index del item del producto si esta se encontraba insertado. de lo contrario devuelve un -1
    private int buscarProductoEnListaAuxiliar(CfgProducto producto) {
        int indx = -1;
        if (listaAuxiliarInventario.isEmpty()) {
            return indx;
        }
        int i = 0;
        for (AuxilarMovInventario auxilarMovInventario : listaAuxiliarInventario) {
            if (auxilarMovInventario.getProducto().equals(producto)) {
                indx = i;
                break;
            }
            i++;
        }
        return indx;
    }

    public CfgCliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(CfgCliente clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
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

    public LazyDataModel<CfgCliente> getListaClientes() {
        return listaClientes;
    }

    public List<FacCarteraCliente> getListaCarteras() {
        return listaCarteras;
    }

    public FacCarteraCliente getCarteraSeleccionada() {
        return carteraSeleccionada;
    }

    public void setCarteraSeleccionada(FacCarteraCliente carteraSeleccionada) {
        this.carteraSeleccionada = carteraSeleccionada;
    }

    public List<FacCarteraDetalle> getListaDetalleCartera() {
        return listaDetalleCartera;
    }

    public float getTotal() {
        return total;
    }

    public float getSaldo() {
        return saldo;
    }

    public List<CfgFormapago> getListaFormapagos() {
        return listaFormapagos;
    }

    public boolean isEnableBtnGuardar() {
        return enableBtnGuardar;
    }

    public FacDocumentosmaster getReciboReciente() {
        return reciboReciente;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

}
