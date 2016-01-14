/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.Date;

/**
 *
 * @author Mario
 */
public class InformeLibroDiario {
    private long noMovimiento;
    private Date fechaMovimiento;
    private String documento;
    private String cuenta;
    private String nombreCuenta;
    private String tercero;
    private String identificacionTercero;
    private String detalle;
    private float debito;
    private float credito;
    private float total;

    public InformeLibroDiario() {
    }

    public long getNoMovimiento() {
        return noMovimiento;
    }

    public void setNoMovimiento(long noMovimiento) {
        this.noMovimiento = noMovimiento;
    }

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public String getTercero() {
        return tercero;
    }

    public void setTercero(String tercero) {
        this.tercero = tercero;
    }

    public String getIdentificacionTercero() {
        return identificacionTercero;
    }

    public void setIdentificacionTercero(String identificacionTercero) {
        this.identificacionTercero = identificacionTercero;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public float getDebito() {
        return debito;
    }

    public void setDebito(float debito) {
        this.debito = debito;
    }

    public float getCredito() {
        return credito;
    }

    public void setCredito(float credito) {
        this.credito = credito;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
    
}
