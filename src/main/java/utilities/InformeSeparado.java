/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Mario
 */
public class InformeSeparado {
    private String numDocumento;
    private String cliente;
    private String telefono;
    private Date fecha;
    private float valor;
    private float abonado;
    private float saldo;
    private List<FacturaDetalleReporte> detalleSeparado;
    private List<InformeAbonoDetalle> detalleAbono;

    public InformeSeparado() {
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public float getAbonado() {
        return abonado;
    }

    public void setAbonado(float abonado) {
        this.abonado = abonado;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public List<FacturaDetalleReporte> getDetalleSeparado() {
        return detalleSeparado;
    }

    public void setDetalleSeparado(List<FacturaDetalleReporte> detalleSeparado) {
        this.detalleSeparado = detalleSeparado;
    }

    public List<InformeAbonoDetalle> getDetalleAbono() {
        return detalleAbono;
    }

    public void setDetalleAbono(List<InformeAbonoDetalle> detalleAbono) {
        this.detalleAbono = detalleAbono;
    }   
    
}
