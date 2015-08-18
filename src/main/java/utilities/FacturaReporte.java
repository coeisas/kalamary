/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.List;

/**
 *
 * @author mario
 */
public class FacturaReporte extends NumberToLetterConverter {

//    private int numFactura;
    private String numFac;
    private float subtotal;
    private float descuento;
    private float totalFactura;
    private String totalLetras;
    private List<FacturaDetalleReporte> detalle;
    private List<FacturaPagoReporte> pago;
    private List<FacturaImpuestoReporte> impuesto;

    public FacturaReporte() {

    }

//    private String determinarNumFac(int num) {
//        String aux = String.valueOf(num);
//        if (num < 10) {
//            aux = "0000".concat(aux);
//        } else if (num >= 10 && num < 100) {
//            aux = "000".concat(aux);
//        } else if (num >= 100 && num < 1000) {
//            aux = "00".concat(aux);
//        }
//        return aux;
//    }

    public List<FacturaDetalleReporte> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<FacturaDetalleReporte> detalle) {
        this.detalle = detalle;
    }

    public List<FacturaPagoReporte> getPago() {
        return pago;
    }

    public void setPago(List<FacturaPagoReporte> pago) {
        this.pago = pago;
    }

    public List<FacturaImpuestoReporte> getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(List<FacturaImpuestoReporte> impuesto) {
        this.impuesto = impuesto;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

    public float getTotalFactura() {
        return totalFactura;
    }

    public void setTotalFactura(float totalFactura) {
        String aux = String.valueOf(totalFactura);
        totalLetras = convertNumberToLetter(aux);
        this.totalFactura = totalFactura;
    }

    public String getTotalLetras() {
        return totalLetras;
    }

    public String getNumFac() {
        return numFac;
    }

//    public int getNumFactura() {
//        return numFactura;
//    }

//    public void setNumFactura(int numFactura) {
//        this.numFac = determinarNumFac(numFactura);
//        this.numFactura = numFactura;
//    }

    public void setNumFac(String numFac) {
        this.numFac = numFac;
    }

}
