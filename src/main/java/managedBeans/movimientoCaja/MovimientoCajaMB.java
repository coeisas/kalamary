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
import entities.CfgMovCta;
import entities.CntMovdetalle;
import entities.CntPuc;
import entities.FacCaja;
import entities.FacDocuementopago;
import entities.FacDocuementopagoPK;
import entities.FacDocumentoimpuesto;
import entities.FacDocumentosmaster;
import entities.FacDocumentosmasterPK;
import entities.FacMovcaja;
import entities.FacMovcajadetalle;
import entities.FacMovcajadetallePK;
import entities.SegUsuario;
import facades.CfgClienteFacade;
import facades.CfgDocumentoFacade;
import facades.CfgFormapagoFacade;
import facades.CfgMovCtaFacade;
import facades.CntMovdetalleFacade;
import facades.FacCajaFacade;
import facades.FacDocuementopagoFacade;
import facades.FacDocumentoimpuestoFacade;
import facades.FacDocumentosmasterFacade;
import facades.FacMovcajaFacade;
import facades.FacMovcajadetalleFacade;
import facades.SegUsuarioFacade;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import managedBeans.facturacion.FacturaMB;
import managedBeans.seguridad.SesionMB;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import utilities.FacturaPagoReporte;
import utilities.LazyClienteDataModel;
import utilities.ReciboCajaReporte;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class MovimientoCajaMB implements Serializable {

    private int idMovimientoCaja;
    private SegUsuario usuarioEntrega;
    private int idProtagonista;
    private String identificacionProtagonista;
    private float valor;
    private String concepto;

    private FacDocumentosmaster reciboReciente;
    private String numReciboCaja;

    private String nombreProtagonista;
    private String labelProtagonista;

    private List<SegUsuario> listaUsuarios;
    private LazyDataModel<CfgCliente> listaClientes;
    private List<SelectItem> tiposMovimientoCaja;
    private List<SelectItem> protagonistas;

    private SegUsuario usuarioActual;//usuario que genera el movimiento
    private FacCaja cajaUsuario;
    private FacMovcaja movimientoCajaMaster;//contiene informacion del maestro del movimiento. Se crea uno cada vez que se habre caja. cuando se cierra se habilita
    private SegUsuario usuarioMovimiento;//usuario protagonista del movimiento. Ingresa o recibe dinero
    private CfgCliente clienteMovimiento;//cliente protagonista del movimiento. Ingresa o recibe dinero
    private CfgEmpresa empresaActual;
    private CfgEmpresasede sedeActual;

    @EJB
    SegUsuarioFacade usuarioFacade;
    @EJB
    CfgClienteFacade clienteFacade;
    @EJB
    FacCajaFacade cajaFacade;
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
    FacDocuementopagoFacade docuementopagoFacade;
    @EJB
    CfgMovCtaFacade cfgMovCtaFacade;
    @EJB
    FacDocumentoimpuestoFacade documentoimpuestoFacade;
    @EJB
    CntMovdetalleFacade cntMovdetalleFacade;

    public MovimientoCajaMB() {
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        SesionMB sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
        empresaActual = sesionMB.getEmpresaActual();
        sedeActual = sesionMB.getSedeActual();
        tiposMovimientoCaja = new ArrayList();
        SelectItem selectItem = new SelectItem(1, "INGRESO");
        tiposMovimientoCaja.add(selectItem);
        selectItem = new SelectItem(2, "EGRESO");
        tiposMovimientoCaja.add(selectItem);
        protagonistas = new ArrayList();
        selectItem = new SelectItem(1, "USUARIO");
        protagonistas.add(selectItem);
        selectItem = new SelectItem(2, "CLIENTE");
        protagonistas.add(selectItem);
        idProtagonista = 1;
        labelProtagonista = "Usuario:";
        if (usuarioActual != null) {
            cajaUsuario = usuarioActual.getFaccajaidCaja();
        }
    }

    public void actualizarLabelProtagonista() {
        if (idProtagonista == 1) {
            labelProtagonista = "Usuario:";
            clienteMovimiento = null;
        } else {
            labelProtagonista = "Cliente:";
            usuarioMovimiento = null;
        }
        cargarInformacionProtagonista();
        RequestContext.getCurrentInstance().update("IdFormMovimientoCaja:IdProtagonista");
    }

    public void cargarProtagonistas() {
        if (empresaActual != null) {
            if (idProtagonista == 1) {
                listaUsuarios = usuarioFacade.buscarPorEmpresaActivos(empresaActual);
                RequestContext.getCurrentInstance().update("FormBuscarUsuario");
                RequestContext.getCurrentInstance().execute("PF('dlgUsuario').show()");
            } else {
                cargarClientes();
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro informacion de la empresa"));
        }
    }

    public void cargarClientes() {
        if (empresaActual != null) {
            listaClientes = new LazyClienteDataModel(clienteFacade, empresaActual);
            RequestContext.getCurrentInstance().update("FormBuscarCliente");
            RequestContext.getCurrentInstance().execute("PF('dlgCliente').show()");
        }
    }

    public void buscarProtagonista() {
        if (empresaActual != null) {
            if (!identificacionProtagonista.trim().isEmpty()) {
                if (idProtagonista == 1) {
                    usuarioMovimiento = usuarioFacade.buscarUsuarioByEmpresaAndDocumento(empresaActual, identificacionProtagonista);
                } else {
                    clienteMovimiento = clienteFacade.buscarPorIdentificacionAndIdEmpresa(identificacionProtagonista, empresaActual);
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha cargado la informacion de la empresa. Reinicie sesion"));
        }
        cargarInformacionProtagonista();

    }

    public void cargarInformacionProtagonista() {
        if (usuarioMovimiento != null) {
            nombreProtagonista = usuarioMovimiento.nombreCompleto();
            identificacionProtagonista = usuarioMovimiento.getNumDoc();
            RequestContext.getCurrentInstance().execute("PF('dlgUsuario').hide()");
        } else if (clienteMovimiento != null) {
            nombreProtagonista = clienteMovimiento.nombreCompleto();
            identificacionProtagonista = clienteMovimiento.getNumDoc();
            RequestContext.getCurrentInstance().execute("PF('dlgCliente').hide()");
        } else {
            nombreProtagonista = null;
            identificacionProtagonista = null;
        }
        RequestContext.getCurrentInstance().update("IdFormMovimientoCaja:IdNombreProtagonista");
        RequestContext.getCurrentInstance().update("IdFormMovimientoCaja:IdIdentificacionProtagonista");
    }

    public void validarConcecutivo() {
        if (idMovimientoCaja != 0 && sedeActual != null) {
            CfgDocumento documento = null;
            switch (idMovimientoCaja) {
                case 1:
                    documento = documentoFacade.buscarDocumentoDeReciboCajaBySede(sedeActual);
                    break;
                case 2:
                    documento = documentoFacade.buscarDocumentoDeEgresoCajaBySede(sedeActual);
                    break;
            }

            if (documento == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro un documento aplicado al tipo de movimiento seleccionado"));
            }
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
        if (idMovimientoCaja == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Determine el tipo de movimiento"));
            return false;
        }
        if (clienteMovimiento == null && usuarioMovimiento == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Es necesario elegir un usuario o un cliente"));
            return false;
        }
        if (valor <= 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El valor del movimiento no puede ser menor o igual a cero"));
            return false;
        }
        if (idMovimientoCaja == 2) {//cuando es un egreso se tiene en cuenta el efectivo en caja
            if (valor > calcularEfectivoEnCaja()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El valor excede el efectivo disponible"));
                return false;
            }
        }
        return ban;
    }

    private float calcularEfectivoEnCaja() {
        List<FacMovcajadetalle> listaMovDetalle = movcajadetalleFacade.buscarMovDetallePorMovCajaAndEfectivo(movimientoCajaMaster);
        float subtotal = 0;
        if (listaMovDetalle != null) {
            for (FacMovcajadetalle movcajadetalle : listaMovDetalle) {
                subtotal += movcajadetalle.getValor();
            }
        }
        return subtotal;
    }

    private boolean validarDocumento(CfgDocumento documento) {
        boolean ban = true;

        if (documento.getFinDocumento() < documento.getActDocumento()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ha llegado al limite del concecutivo. Revice la configuracion de documentos"));
            return false;
        }
        if (documentosmasterFacade.buscarBySedeAndDocumentoAndNum(sedeActual, documento.getIdDoc(), documento.getActDocumento()) != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El consecutivo duplicado. Revice la configuracion de documentos o vuelva a intentarlo"));
            return false;
        }
        return ban;
    }

    public void guardarMovimiento() {
        if (!validar()) {
            return;
        }
        CfgDocumento documento = null;
        switch (idMovimientoCaja) {
            case 1:
                documento = documentoFacade.buscarDocumentoDeReciboCajaBySede(sedeActual);
                break;
            case 2:
                documento = documentoFacade.buscarDocumentoDeEgresoCajaBySede(sedeActual);
                break;
        }
        if (documento == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro un documento aplicado al tipo de movimiento seleccionado"));
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
        try {
            FacDocumentosmaster documentoMaster = new FacDocumentosmaster();
            documentoMaster.setFacDocumentosmasterPK(new FacDocumentosmasterPK(documento.getIdDoc(), documento.getActDocumento()));
            documentoMaster.setCfgDocumento(documento);
            documentoMaster.setFaccajaidCaja(cajaUsuario);
            documentoMaster.setCfgempresasedeidSede(sedeActual);
            documentoMaster.setFecCrea(new Date());
            documentoMaster.setSegusuarioidUsuario(usuarioActual);
            //el protagonista del movimiento puede ser un usuario o un cliente
            if (usuarioMovimiento == null) {
                documentoMaster.setCfgclienteidCliente(clienteMovimiento);
            } else {
                documentoMaster.setSegusuarioidUsuario1(usuarioMovimiento);
            }
            documentoMaster.setObservaciones(concepto);
            documentoMaster.setTotal(valor);
            documentosmasterFacade.create(documentoMaster);//SE CREA EL RECIBO PARA EL TIPO DE MOVMIENTO DE CAJA SELECCIONADO

            //SE ACTUALIZA EL CONCECUTIVO
            if (documento.getActDocumento() == documento.getFinDocumento()) {
                documento.setFinalizado(true);
                documento.setActivo(false);
            }
            documentoFacade.edit(documento);

            //LA FORMA DE PAGO QUE SE APLICA ES EN EFECTIVO
            CfgFormapago formapago = formapagoFacade.buscarPorEmpresaAndCodigo(empresaActual, "1");

            //SE CREA LA FORMA DE PAGO DEL RECIBO DE CAJA. 
            List<FacDocuementopago> listaPagos = new ArrayList();
            FacDocuementopago docuementopago = new FacDocuementopago();
            docuementopago.setFacDocuementopagoPK(new FacDocuementopagoPK(formapago.getIdFormaPago(), documentoMaster.getFacDocumentosmasterPK().getCfgdocumentoidDoc(), documentoMaster.getFacDocumentosmasterPK().getNumDocumento()));
            docuementopago.setFacDocumentosmaster(documentoMaster);
            docuementopago.setCfgFormapago(formapago);
            docuementopago.setValorPago(valor);
            docuementopagoFacade.create(docuementopago);
            listaPagos.add(docuementopago);

            documentoMaster.setFacDocuementopagoList(listaPagos);
            documentosmasterFacade.edit(documentoMaster);

            //SE CREA EL MOVIMIENTO DE CAJA
            FacMovcajadetalle movcajadetalle = new FacMovcajadetalle();
            movcajadetalle.setFacMovcajadetallePK(new FacMovcajadetallePK(movimientoCajaMaster.getIdMovimiento(), documentoMaster.getFacDocumentosmasterPK().getCfgdocumentoidDoc(), documentoMaster.getFacDocumentosmasterPK().getNumDocumento(), formapago.getIdFormaPago(), new Date()));
            movcajadetalle.setFacMovcaja(movimientoCajaMaster);
            movcajadetalle.setFacDocumentosmaster(documentoMaster);
            movcajadetalle.setCfgFormapago(formapago);
            if (idMovimientoCaja == 1) {
                movcajadetalle.setValor(valor);
            } else {
                movcajadetalle.setValor(valor * (-1));
            }
            movcajadetalleFacade.create(movcajadetalle);
            limpiar();
            reciboReciente = documentoMaster;
            //CARGA DE cnt_mov_detalle CORRESPONDIENTE AL RECIBO DE CAJA (INGRESO O EGRESO)
            cargaCntMovDetalle(documentoMaster);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Movimiento de caja creado " + documentoMaster.determinarNumFactura()));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Movimiento de caja no registrado"));
        }
    }

    private void limpiar() {
        concepto = null;
        usuarioMovimiento = null;
        clienteMovimiento = null;
        valor = 0;
        nombreProtagonista = null;
        idProtagonista = 1;
        labelProtagonista = "Usuario:";
        identificacionProtagonista = null;
        RequestContext.getCurrentInstance().update("IdFormMovimientoCaja");
        RequestContext.getCurrentInstance().update("FormBuscarUsuario");
    }

    private void cargaCntMovDetalle(FacDocumentosmaster documentoMaster) {
        //EL TIPO DE MOVIMIENTO ES LA APLICACION DEL DOCUMENTO ACTUAL.
        int idTipoMovimiento = documentoMaster.getCfgDocumento().getCfgAplicaciondocumentoIdaplicacion().getIdaplicacion();
        //LA FORMA DE PAGO EN LOS RECIBO DE CAJA INGRESO O EGRESO SE REDUCEN A EFECTIVO(1)
        int idFormaPago = 1;
        //LOS VALORES: SUBTOTAL, DESCUENTO, IMPUESTO, TOTAL PUEDEN IR A DIFERENTES CUENTAS SEGUN LA CONFIGURACION EXISTENTE EN cfg_mov_cta
        List<CfgMovCta> listaCfgMovCta = cfgMovCtaFacade.buscarPorTipoMovimientoAndFormaPago(sedeActual.getIdSede(), idTipoMovimiento, idFormaPago);
        for (CfgMovCta cmc : listaCfgMovCta) {
            CntPuc cntPuc = cmc.getCntpuccodigoCuenta();
            if (cntPuc != null && cmc.getAplica() != 0) {//SI SE ASIGNO UNA CUENTA PUC AL VALOR ACTUAL DE LA ITERACION Y SI TIENE ASIGANDO UNA APLICACION DIFERENTE A 0 (DEBE, HABER)
                try {
                    CntMovdetalle cntMovdetalle = new CntMovdetalle();
                    cntMovdetalle.setCfgempresasedeidSede(sedeActual);
                    cntMovdetalle.setFecha(new Date());
                    cntMovdetalle.setFacDocumentosmaster(documentoMaster);
                    cntMovdetalle.setInvMovimiento(null);//NO ES UN MOVIMIENTO DE INVENTARIO
                    //COMO ES UN RECIBO DE CAJA INGRESO O EGRESO, EL TERCERO PUEDE SER UN USUARIO O EL CLIENTE
                    String tercero;
                    if (documentoMaster.getCfgclienteidCliente() != null) {
                        tercero = documentoMaster.getCfgclienteidCliente().nombreCompleto();
                    } else {
                        tercero = documentoMaster.getSegusuarioidUsuario1().nombreCompleto();
                    }
                    if (tercero.length() > 150) {
                        tercero = tercero.substring(0, 149);
                    }
                    cntMovdetalle.setTercero(tercero);
                    cntMovdetalle.setCntpuccodigoCuenta(cntPuc);
                    cntMovdetalle.setDetalle(documentoMaster.getObservaciones());
                    float valr = 0;
                    switch (cmc.getCntDetalle().getIdcntDetalle()) {
                        case 1://SUBTOTAL
                            valr = documentoMaster.getSubtotal();
                            break;
                        case 2://DESCUENTO
                            valr = documentoMaster.getDescuento();
                            break;
                        case 3://IMPUESTO
                            List<FacDocumentoimpuesto> listaImpuesto = documentoimpuestoFacade.buscarByDocumentoMaster(documentoMaster);
                            valr = calcularImpuestoCntMovDetalle(listaImpuesto);
                            break;
                        case 4://TOTAL
                            valr = documentoMaster.getTotal();
                            break;
                    }
                    int ban = cmc.getAplica();
                    float debito = 0;//debe
                    float credito = 0;//haber
                    if (ban == 1) {
                        debito = valr;
                    } else {
                        credito = valr;
                    }
                    cntMovdetalle.setDebito(debito);
                    cntMovdetalle.setCredito(credito);
                    float tot = debito - credito;
                    cntMovdetalle.setTotal(tot);
                    cntMovdetalleFacade.create(cntMovdetalle);
                } catch (Exception e) {
//                    System.out.println(e);
                    Logger.getLogger(FacturaMB.class.getName()).log(Level.SEVERE, null, e);
                }
            }

        }
    }

    //calcula el impuesto para el moduclo de contabilidad cnt_movdetalle
    private float calcularImpuestoCntMovDetalle(List<FacDocumentoimpuesto> lista) {
        float aux = 0;
        for (FacDocumentoimpuesto impuesto : lista) {
            aux += impuesto.getValorImpuesto();
        }
        return aux;
    }

    public void imprimirRecibo() {
        if (reciboReciente != null) {
            numReciboCaja = reciboReciente.determinarNumFactura();
            RequestContext.getCurrentInstance().update("IdFormConfirmacion");
            RequestContext.getCurrentInstance().execute("PF('dlgResult').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha encontrado un recibo de caja creado recientemente"));
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
        //el protagonista de un movimiento de caja puede ser un usuario del sistema o un cliente. No sera un valor null
        if (reciboReciente.getCfgclienteidCliente() != null) {
            reciboCajaReporte.setProtagonista(reciboReciente.getCfgclienteidCliente().nombreCompleto());
        } else {
            reciboCajaReporte.setProtagonista(reciboReciente.getSegusuarioidUsuario1().nombreCompleto());
        }
        reciboCajaReporte.setConcepto(reciboReciente.getObservaciones());
        reciboCajaReporte.setFormaPago(crearListadoPago(reciboReciente.getFacDocuementopagoList()));
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
            CfgDocumento consecutivoUsuado = reciboReciente.getCfgDocumento();
            if (consecutivoUsuado.getCfgAplicaciondocumentoIdaplicacion().getCodaplicacion().equals("2")) {
                parametros.put("accion", "RECIBÍ DE:");
            } else {
                parametros.put("accion", "ENTREGUÉ A:");
            }
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

    public SegUsuario getUsuarioEntrega() {
        return usuarioEntrega;
    }

    public void setUsuarioEntrega(SegUsuario usuarioEntrega) {
        this.usuarioEntrega = usuarioEntrega;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public List<SegUsuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public SegUsuario getUsuarioMovimiento() {
        return usuarioMovimiento;
    }

    public void setUsuarioMovimiento(SegUsuario usuarioMovimiento) {
        this.usuarioMovimiento = usuarioMovimiento;
    }

    public String getIdentificacionProtagonista() {
        return identificacionProtagonista;
    }

    public void setIdentificacionProtagonista(String identificacionProtagonista) {
        this.identificacionProtagonista = identificacionProtagonista;
    }

    public String getNombreProtagonista() {
        return nombreProtagonista;
    }

    public int getIdMovimientoCaja() {
        return idMovimientoCaja;
    }

    public void setIdMovimientoCaja(int idMovimientoCaja) {
        this.idMovimientoCaja = idMovimientoCaja;
    }

    public List<SelectItem> getTiposMovimientoCaja() {
        return tiposMovimientoCaja;
    }

    public String getNumReciboCaja() {
        return numReciboCaja;
    }

    public CfgCliente getClienteMovimiento() {
        return clienteMovimiento;
    }

    public void setClienteMovimiento(CfgCliente clienteMovimiento) {
        this.clienteMovimiento = clienteMovimiento;
    }

    public LazyDataModel<CfgCliente> getListaClientes() {
        return listaClientes;
    }

    public int getIdProtagonista() {
        return idProtagonista;
    }

    public void setIdProtagonista(int idProtagonista) {
        this.idProtagonista = idProtagonista;
    }

    public List<SelectItem> getProtagonistas() {
        return protagonistas;
    }

    public String getLabelProtagonista() {
        return labelProtagonista;
    }

}
