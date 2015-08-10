/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

/**
 *
 * @author mario
 */
public class FacturaImpuestoReporte {
    private String impuesto;
    private float porcentaje;
    private float valorImpuesto;

    public FacturaImpuestoReporte() {
    }

    public String getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(String impuesto) {
        this.impuesto = impuesto;
    }

    public float getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(float porcentaje) {
        this.porcentaje = porcentaje;
    }

    public float getValorImpuesto() {
        return valorImpuesto;
    }

    public void setValorImpuesto(float valorImpuesto) {
        this.valorImpuesto = valorImpuesto;
    }
    
    
    
}
