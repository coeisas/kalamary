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
public class InformeLibroMayor {

    private String cuenta;
    private String nombreCuenta;
    private float debitos;
    private float creditos;
    private Date fecha;

    public InformeLibroMayor() {
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

    public float getDebitos() {
        return debitos;
    }

    public void setDebitos(float debitos) {
        this.debitos = debitos;
    }

    public float getCreditos() {
        return creditos;
    }

    public void setCreditos(float creditos) {
        this.creditos = creditos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }  

}
