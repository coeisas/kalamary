/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.configuracion;

import entities.CfgEmpresa;
import entities.CfgEmpresasede;
import entities.CfgKitproductodetalle;
import entities.CfgKitproductodetallePK;
import entities.CfgKitproductomaestro;
import entities.CfgProducto;
import entities.SegUsuario;
import facades.CfgEmpresaFacade;
import facades.CfgKitproductodetalleFacade;
import facades.CfgKitproductomaestroFacade;
import facades.CfgProductoFacade;
import javax.faces.bean.ManagedBean;
import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import managedBeans.seguridad.SesionMB;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import javax.faces.event.ActionEvent;
import org.primefaces.model.LazyDataModel;
import utilities.LazyProductosModel;
import java.util.Date;

/**
 *
 * @author mario
 */
@ManagedBean
@SessionScoped
public class KitMB implements Serializable {

    private String codigoKit;
    private String nombreKit;
    private float costo;
    private float utilidad;
    private float precio;
    private boolean activo;

    private SesionMB sesionMB;
    private SegUsuario usuarioActual;
    private CfgEmpresasede sedeActual;

    private CfgEmpresa empresaSeleccionada;
    private CfgProducto productoSeleccionado;
    private CfgKitproductomaestro kitMaestroSeleccionado;
    private CfgKitproductodetalle kitDetalleSeleccionado;

    private LazyDataModel<CfgProducto> listaProducto;
    private List<CfgKitproductomaestro> listaKitMaestro;
    private List<CfgKitproductodetalle> listaKitDetalle;
    private List<CfgKitproductodetalle> listaAuxiliarDetalle;//lista auxiliar se usa para modificar

    @EJB
    CfgEmpresaFacade empresaFacade;
    @EJB
    CfgProductoFacade productoFacade;
    @EJB
    CfgKitproductomaestroFacade kitMaestroFacade;
    @EJB
    CfgKitproductodetalleFacade kitDetalleFacade;

    public KitMB() {
    }

    @PostConstruct
    private void init() {
        activo = true;
        listaKitMaestro = new ArrayList();
        FacesContext context = FacesContext.getCurrentInstance();
        sesionMB = context.getApplication().evaluateExpressionGet(context, "#{sesionMB}", SesionMB.class);
        usuarioActual = sesionMB.getUsuarioActual();
        empresaSeleccionada = sesionMB.getEmpresaActual();
        sedeActual = sesionMB.getSedeActual();
        if (empresaSeleccionada != null) {
            listaProducto = new LazyProductosModel(empresaSeleccionada, null, null, null, productoFacade);
        }
        setListaKitDetalle((List<CfgKitproductodetalle>) new ArrayList());
        listaAuxiliarDetalle = new ArrayList();

    }

    public void cargarProductos() {
        listaProducto = new LazyProductosModel(empresaSeleccionada, null, null, null, productoFacade);
        if (empresaSeleccionada != null) {
            RequestContext.getCurrentInstance().update("FormModalProducto");
            RequestContext.getCurrentInstance().execute("PF('dlgProducto').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "Determine la empresa"));
        }

    }

    public void cargarKits() {
        if (empresaSeleccionada != null) {
            listaKitMaestro = kitMaestroFacade.buscarPorEmpresa(empresaSeleccionada);
        } else {
            listaKitMaestro.clear();
        }
        RequestContext.getCurrentInstance().update("FormModalKit");
        RequestContext.getCurrentInstance().execute("PF('dlgKit').show()");       
    }

    public void buscarKit() {
        if (empresaSeleccionada != null && !codigoKit.trim().isEmpty()) {
            kitMaestroSeleccionado = kitMaestroFacade.buscarPorEmpresaAndCodigo(empresaSeleccionada, codigoKit);
        } else {
            kitMaestroSeleccionado = null;
        }
        cargarInformacionKit();
    }

    public void cargarInformacionKit() {
        if (kitMaestroSeleccionado != null) {
//            setListaKitDetalle(kitMaestroSeleccionado.getCfgKitproductodetalleList());
            listaKitDetalle = kitDetalleFacade.buscarByMaestro(kitMaestroSeleccionado);
            //se marca como elementos no nuevos a los elementos que estan incluidos en el paquete. (Antes de modificar)
            for (CfgKitproductodetalle kitproductodetalle : listaKitDetalle) {
                kitproductodetalle.setNuevo(false);
            }
            setCodigoKit(kitMaestroSeleccionado.getCodKit());
            setNombreKit(kitMaestroSeleccionado.getNomKit());
            setCosto(kitMaestroSeleccionado.getCosto());
            setUtilidad(kitMaestroSeleccionado.getUtilidad());
            setPrecio(kitMaestroSeleccionado.getPrecio());
            setActivo(kitMaestroSeleccionado.getActivo());
        } else {
            limpiarInformacionKit();
        }
        RequestContext.getCurrentInstance().update("IdFormKit");
    }

    public void insertarItemKit() {
        if (productoSeleccionado != null) {
            CfgKitproductodetalle kitproductodetalle = comprobarItemInsertado(productoSeleccionado);
            if (kitproductodetalle == null) {//el producto no esta en el item
                kitproductodetalle = new CfgKitproductodetalle();
                kitproductodetalle.setCfgKitproductodetallePK(new CfgKitproductodetallePK(0, productoSeleccionado.getIdProducto()));
                kitproductodetalle.setCant(1);
                kitproductodetalle.setCfgProducto(productoSeleccionado);
                kitproductodetalle.setNuevo(true);//variable tenidad en cuenta para editar el paquete. Cuando es igual a true se creara un nuevo elemento de lo contrario se modifica
                float aux = Redondear(productoSeleccionado.getPrecio(), 0);
                kitproductodetalle.setPrecioUnitario(aux);
                listaKitDetalle.add(kitproductodetalle);
            } else {
                kitproductodetalle.setCant(kitproductodetalle.getCant() + 1);
            }
            kitproductodetalle.setPrecioTotal(kitproductodetalle.getPrecioUnitario() * kitproductodetalle.getCant());
            calcularCostoKit();
            calcularPrecioKit();
        }
        RequestContext.getCurrentInstance().update("IdFormKit");
        RequestContext.getCurrentInstance().execute("PF('dlgProducto').hide()");

    }

    public void quitarItem(ActionEvent event) {
        CfgKitproductodetalle detalle = (CfgKitproductodetalle) event.getComponent().getAttributes().get("itemKit");
        listaKitDetalle.remove(detalle);
        calcularCostoKit();
        calcularPrecioKit();
        if (kitMaestroSeleccionado != null) {
            //aquellos elementos que no son nuevos se elminaran definitivamente del paquete cuando se guarden los cambios
            if (!detalle.isNuevo()) {
                listaAuxiliarDetalle.add(detalle);
            }
        }
        RequestContext.getCurrentInstance().update("IdFormKit");
    }

    public CfgKitproductodetalle comprobarItemInsertado(CfgProducto producto) {
        CfgKitproductodetalle aux = null;
        for (CfgKitproductodetalle kitproductodetalle : listaKitDetalle) {
            if (kitproductodetalle.getCfgProducto().equals(producto)) {
                aux = kitproductodetalle;
                break;
            }
        }
        return aux;
    }

    private void limpiarInformacionKit() {
        getListaKitDetalle().clear();
        listaAuxiliarDetalle.clear();
        kitMaestroSeleccionado = null;
        setNombreKit(null);
        setCosto(0);
        setUtilidad(0);
        setPrecio(0);
        setActivo(true);
    }

    private float Redondear(float pNumero, int pCantidadDecimales) {
        BigDecimal value = new BigDecimal(pNumero);
        value = value.setScale(pCantidadDecimales, RoundingMode.HALF_UP);
        return value.floatValue();
    }

    public void updateTabla() {
        RequestContext.getCurrentInstance().update("IdFormKit");
    }

    public void onCellEdit(CellEditEvent event) {
        int index = event.getRowIndex();
        listaKitDetalle.get(index).setPrecioTotal(listaKitDetalle.get(index).getCant() * listaKitDetalle.get(index).getPrecioUnitario());
        calcularCostoKit();
        calcularPrecioKit();
    }

    private void calcularCostoKit() {
        costo = 0;
        for (CfgKitproductodetalle kitproductodetalle : listaKitDetalle) {
            costo += kitproductodetalle.getPrecioTotal();
        }
    }

    public void calcularPrecioKit() {
        precio = costo + (costo * utilidad / (float) 100);
        precio = Redondear(precio, 0);
    }

    public void accion() {
        if (kitMaestroSeleccionado != null) {
            actualizarKit();
        } else {
            crearKit();
        }
        RequestContext.getCurrentInstance().update("IdFormKit");
    }

    private boolean validar() {
        boolean ban = true;
        if (empresaSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro la empresa"));
            return false;
        }
        if (usuarioActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No ha iniciado sesion correctamente"));
            return false;
        }
        if (!usuarioActual.getCfgRolIdrol().getCodrol().equals("00001") && !usuarioActual.getCfgRolIdrol().getCodrol().equals("00002")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No tiene permisos para crear kits"));
            return false;
        }
        if (listaKitDetalle.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay Items insertados"));
            return false;
        }
        return ban;
    }

    private void crearKit() {
        if (!validar()) {
            return;
        }
        if (codigoKit.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Codigo obligatorio"));
            return;
        }
        try {
            CfgKitproductomaestro kitmaestro = new CfgKitproductomaestro();
            kitmaestro.setActivo(activo);
            kitmaestro.setCfgempresaidEmpresa(empresaSeleccionada);
            kitmaestro.setCodKit(codigoKit.toUpperCase());
            kitmaestro.setCosto(costo);
            kitmaestro.setFecCrea(new Date());
            kitmaestro.setNomKit(nombreKit.toUpperCase());
            kitmaestro.setPrecio(precio);
            kitmaestro.setSegusuarioidUsuario(usuarioActual);
            kitmaestro.setUtilidad(utilidad);
            kitMaestroFacade.create(kitmaestro);
            for (CfgKitproductodetalle kitproductodetalle : listaKitDetalle) {
                kitproductodetalle.getCfgKitproductodetallePK().setCfgkitproductomaestroidKit(kitmaestro.getIdKit());
                kitproductodetalle.setCfgKitproductomaestro(kitmaestro);
                kitDetalleFacade.create(kitproductodetalle);
            }
            setCodigoKit(null);
            limpiarInformacionKit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Kit creado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Kit no creado"));
        }

    }

    private void actualizarKit() {
        if (kitMaestroSeleccionado == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro el kit a modificar"));
            return;
        }
        if (!validar()) {
            return;
        }
        try {
            //se elimina los items que ya no forman parte del kit.
            for (CfgKitproductodetalle kitproductodetalle : listaAuxiliarDetalle) {
                kitDetalleFacade.remove(kitproductodetalle);
            }
//            se crea los nuevos items o se modifica. Dependiendo del caso
            for (CfgKitproductodetalle kitproductodetalle : listaKitDetalle) {
                if (kitproductodetalle.isNuevo()) {
                    kitproductodetalle.getCfgKitproductodetallePK().setCfgkitproductomaestroidKit(kitMaestroSeleccionado.getIdKit());
                    kitproductodetalle.setCfgKitproductomaestro(kitMaestroSeleccionado);
                    kitDetalleFacade.create(kitproductodetalle);
                } else {
                    kitDetalleFacade.edit(kitproductodetalle);
                }
            }
//          se actuliza los campos del kitmaestro
            kitMaestroSeleccionado.setActivo(activo);
            kitMaestroSeleccionado.setCosto(costo);
            kitMaestroSeleccionado.setNomKit(nombreKit.toUpperCase());
            kitMaestroSeleccionado.setPrecio(precio);
            kitMaestroSeleccionado.setUtilidad(utilidad);
            kitMaestroFacade.edit(kitMaestroSeleccionado);
            setCodigoKit(null);
            limpiarInformacionKit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Kit modificado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Kit no modificado"));
        }

    }

    public void cancelar() {
        limpiarInformacionKit();
        RequestContext.getCurrentInstance().update("IdFormKit");
    }

    public String getCodigoKit() {
        return codigoKit;
    }

    public void setCodigoKit(String codigoKit) {
        this.codigoKit = codigoKit;
    }

    public String getNombreKit() {
        return nombreKit;
    }

    public void setNombreKit(String nombreKit) {
        this.nombreKit = nombreKit;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public float getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(float utilidad) {
        this.utilidad = utilidad;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public CfgEmpresa getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(CfgEmpresa empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
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

    public CfgKitproductomaestro getKitMaestroSeleccionado() {
        return kitMaestroSeleccionado;
    }

    public void setKitMaestroSeleccionado(CfgKitproductomaestro kitMaestroSeleccionado) {
        this.kitMaestroSeleccionado = kitMaestroSeleccionado;
    }

    public List<CfgKitproductomaestro> getListaKitMaestro() {
        return listaKitMaestro;
    }

    public void setListaKitMaestro(List<CfgKitproductomaestro> listaKitMaestro) {
        this.listaKitMaestro = listaKitMaestro;
    }

    public List<CfgKitproductodetalle> getListaKitDetalle() {
        return listaKitDetalle;
    }

    public void setListaKitDetalle(List<CfgKitproductodetalle> listaKitDetalle) {
        this.listaKitDetalle = listaKitDetalle;
    }

}
