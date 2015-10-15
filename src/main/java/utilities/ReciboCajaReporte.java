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
 * @author mario
 */
public class ReciboCajaReporte extends NumberToLetterConverter {

    private String concecutivo;
    private String ciudad;
    private Date fecha;
    private float valor;
    private String protagonista;
    private String concepto;
    private String valorNominal;
    private List<FacturaPagoReporte> formaPago;

    public ReciboCajaReporte() {
    }

    public String getConcecutivo() {
        return concecutivo;
    }

    public void setConcecutivo(String concecutivo) {
        this.concecutivo = concecutivo;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
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
        String aux = String.valueOf(valor);
        valorNominal = convertNumberToLetter(aux);
        this.valor = valor;
    }

    public String getProtagonista() {
        return protagonista;
    }

    public void setProtagonista(String protagonista) {
        this.protagonista = protagonista;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getValorNominal() {
        return valorNominal;
    }

    public List<FacturaPagoReporte> getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(List<FacturaPagoReporte> formaPago) {
        this.formaPago = formaPago;
    }

}
