/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.movimientoCaja;

import entities.CfgDocumento;
import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.CfgFormapago;
import entities.FacCaja;
import entities.FacDocuementopago;
import entities.FacDocuementopagoPK;
import entities.FacDocumentosmaster;
import entities.FacDocumentosmasterPK;
import entities.FacMovcaja;
import entities.FacMovcajadetalle;
import entities.FacMovcajadetallePK;
import entities.SegUsuario;
import facades.CfgDocumentoFacade;
import facades.CfgFormapagoFacade;
import facades.FacCajaFacade;
import facades.FacDocuementopagoFacade;
import facades.FacDocumentosmasterFacade;
import facades.FacMovcajaFacade;
import facades.FacMovcajadetalleFacade;
import facades.SegUsuarioFacade;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import managedBeans.seguridad.SesionMB;
import org.primefaces.context.RequestContext;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class MovimientoCajaMB implements Serializable {

    private int idMovimientoCaja;
    private SegUsuario usuarioEntrega;
    private String identificacionUsuario;
    private float valor;
    private String concepto;

    private String nombreUsuario;

    private List<SegUsuario> listaUsuarios;
    private List<SelectItem> tiposMovimientoCaja;

    private SegUsuario usuarioActual;//usuario que genera el movimiento
    private FacCaja cajaUsuario;
    private FacMovcaja movimientoCajaMaster;//contiene informacion del maestro del movimiento. Se crea uno cada vez que se habre caja. cuando se cierra se habilita
    private SegUsuario usuarioMovimiento;//usuario protagonista del movimiento. Ingresa o recibe dinero
    private CfgEmpresa empresaActual;
    private CfgEmpresasede sedeActual;

    @EJB
    SegUsuarioFacade usuarioFacade;
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
        if (usuarioActual != null) {
            cajaUsuario = usuarioActual.getFaccajaidCaja();
        }
    }

    public void cargarUsuarios() {
        if (empresaActual != null) {
            listaUsuarios = usuarioFacade.buscarPorEmpresaActivos(empresaActual);
            RequestContext.getCurrentInstance().update("FormBuscarUsuario");
            RequestContext.getCurrentInstance().execute("PF('dlgUsuario').show()");
        }
    }

    public void buscarUsuario() {
        if (empresaActual != null) {
            if (!identificacionUsuario.isEmpty()) {
                usuarioMovimiento = usuarioFacade.buscarUsuarioByEmpresaAndDocumento(empresaActual, identificacionUsuario);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha cargado la informacion de la empresa. Reinicie sesion"));
        }
        cargarInformacionUsuario();

    }

    public void cargarInformacionUsuario() {
        if (usuarioMovimiento != null) {
            nombreUsuario = usuarioMovimiento.nombreCompleto();
            identificacionUsuario = usuarioMovimiento.getNumDoc();
            RequestContext.getCurrentInstance().execute("PF('dlgUsuario').hide()");
        } else {
            nombreUsuario = null;
        }
        RequestContext.getCurrentInstance().update("IdFormMovimientoCaja:IdNombreCliente");
        RequestContext.getCurrentInstance().update("IdFormMovimientoCaja:IdIdentificacionUsuario");
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
            documentoMaster.setSegusuarioidUsuario1(usuarioMovimiento);
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
            movcajadetalle.setFacMovcajadetallePK(new FacMovcajadetallePK(movimientoCajaMaster.getIdMovimiento(), documentoMaster.getFacDocumentosmasterPK().getCfgdocumentoidDoc(), documentoMaster.getFacDocumentosmasterPK().getNumDocumento(), formapago.getIdFormaPago()));
            movcajadetalle.setFacMovcaja(movimientoCajaMaster);
            movcajadetalle.setFacDocumentosmaster(documentoMaster);
            movcajadetalle.setCfgFormapago(formapago);
            movcajadetalle.setFecha(new Date());
            if (idMovimientoCaja == 1) {
                movcajadetalle.setValor(valor);
            } else {
                movcajadetalle.setValor(valor * (-1));
            }
            movcajadetalleFacade.create(movcajadetalle);
            limpiar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Movimiento de caja creado " + documentoMaster.determinarNumFactura()));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Movimiento de caja no registrado"));
        }
    }

    private void limpiar() {
        concepto = null;
        usuarioMovimiento = null;
        valor = 0;
        nombreUsuario = null;
        identificacionUsuario = null;
        RequestContext.getCurrentInstance().update("IdFormMovimientoCaja");
        RequestContext.getCurrentInstance().update("FormBuscarUsuario");
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

    public String getIdentificacionUsuario() {
        return identificacionUsuario;
    }

    public void setIdentificacionUsuario(String identificacionUsuario) {
        this.identificacionUsuario = identificacionUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
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

}
