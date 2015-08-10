/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.seguridad;

import entities.CfgDepartamento;
import entities.CfgEmpresa;
import entities.CfgRol;
import entities.CfgTipodocempresa;
import entities.CfgTipoempresa;
import entities.CfgTipoidentificacion;
import entities.SegUsuario;
import facades.CfgDepartamentoFacade;
import facades.CfgEmpresaFacade;
import facades.CfgRolFacade;
import facades.CfgTipodocempresaFacade;
import facades.CfgTipoempresaFacade;
import facades.CfgTipoidentificacionFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.servlet.http.HttpSession;

/**
 *
 * @author mario
 */
@ManagedBean
@ApplicationScoped
public class AplicacionMB implements Serializable {

    private List<String> imagenes;//contiene las imagenes que aparecen en el index
    private List<SegUsuario> listaUsuariosActivos;
    private List<HttpSession> listaSesiones;

    private List<CfgDepartamento> listaDepartementos;
    private List<CfgTipodocempresa> listaTipodocempresa;
    private List<CfgTipoempresa> listaTipoEmpresa;
    private List<CfgTipoidentificacion> listaTipoIdentificacion;
    private List<CfgRol> listaRoles;
    private List<CfgEmpresa> listaEmpresas;

    @EJB
    CfgDepartamentoFacade departamentoFacade;

    @EJB
    CfgTipoidentificacionFacade tipoidentificacionFacade;

    @EJB
    CfgTipodocempresaFacade tipodocempresaFacade;

    @EJB
    CfgTipoempresaFacade tipoempresaFacade;

    @EJB
    CfgEmpresaFacade empresaFacade;

    @EJB
    CfgRolFacade rolFacade;

    @PostConstruct
    private void init() {
        imagenes = new ArrayList();
        listaSesiones = new ArrayList();
        setListaDepartementos(departamentoFacade.buscarDepartamentosOrdenado());
        setListaTipoIdentificacion(tipoidentificacionFacade.findAll());
        setListaTipodocempresa(tipodocempresaFacade.findAll());
        setListaTipoEmpresa(tipoempresaFacade.findAll());
        setListaEmpresas(empresaFacade.findAll());
        setListaRoles(rolFacade.findAll());
        setListaUsuariosActivos((List<SegUsuario>) new ArrayList());
        for (int i = 1; i <= 6; i++) {
            String img = "idx" + i + ".jpg";
            imagenes.add(img);
        }

    }

    public void actualizarListaEmpresas() {
        listaEmpresas = empresaFacade.findAll();
    }

    public AplicacionMB() {

    }

    public void insertarUsuario(SegUsuario usuario) {
        getListaUsuariosActivos().add(usuario);
//        System.out.println("inicio sesion => " + usuario.getUsuario() + " idSession => " + usuario.getRememberToken());
    }

    public void descartarUsuario(SegUsuario usuario) {
        getListaUsuariosActivos().remove(usuario);
//        System.out.println("cerro sesion => " + usuario.getUsuario() + " idSession => " + usuario.getRememberToken());
    }

    public String descartarSession(SegUsuario usuario) {
        SegUsuario user = buscarUsuario(usuario);
        if (user == null) {
            return "";
        }
        user.getSession().invalidate();
        return "procesos/main?faces-redirect=true";
    }

    public void insertarHttpSession(HttpSession session) {
        listaSesiones.add(session);
//        System.out.println("sesiones activas =>" + listaSesiones.size());
    }

    public void descartarHttpSession(HttpSession session) {
        listaSesiones.remove(session);
//        System.out.println("sesiones activas =>" + listaSesiones.size());
    }

    private SegUsuario buscarUsuario(SegUsuario usuario) {
        SegUsuario user = null;
        for (SegUsuario u : listaUsuariosActivos) {
            if (u.equals(usuario)) {
                user = u;
                break;
            }
        }
        return user;
    }

    public List<String> getImagenes() {
        return imagenes;
    }

    public List<CfgDepartamento> getListaDepartementos() {
        return listaDepartementos;
    }

    public void setListaDepartementos(List<CfgDepartamento> listaDepartementos) {
        this.listaDepartementos = listaDepartementos;
    }

    public List<CfgTipodocempresa> getListaTipodocempresa() {
        return listaTipodocempresa;
    }

    public void setListaTipodocempresa(List<CfgTipodocempresa> listaTipodocempresa) {
        this.listaTipodocempresa = listaTipodocempresa;
    }

    public List<CfgTipoidentificacion> getListaTipoIdentificacion() {
        return listaTipoIdentificacion;
    }

    public void setListaTipoIdentificacion(List<CfgTipoidentificacion> listaTipoIdentificacion) {
        this.listaTipoIdentificacion = listaTipoIdentificacion;
    }

    public List<CfgTipoempresa> getListaTipoEmpresa() {
        return listaTipoEmpresa;
    }

    public void setListaTipoEmpresa(List<CfgTipoempresa> listaTipoEmpresa) {
        this.listaTipoEmpresa = listaTipoEmpresa;
    }

    public List<CfgEmpresa> getListaEmpresas() {
        return listaEmpresas;
    }

    public void setListaEmpresas(List<CfgEmpresa> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }

    public List<SegUsuario> getListaUsuariosActivos() {
        return listaUsuariosActivos;
    }

    public void setListaUsuariosActivos(List<SegUsuario> listaUsuariosActivos) {
        this.listaUsuariosActivos = listaUsuariosActivos;
    }

    public List<CfgRol> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(List<CfgRol> listaRoles) {
        this.listaRoles = listaRoles;
    }

    public List<HttpSession> getListaSesiones() {
        return listaSesiones;
    }

    public void setListaSesiones(List<HttpSession> listaSesiones) {
        this.listaSesiones = listaSesiones;
    }

}
