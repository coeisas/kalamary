/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import entities.CfgProducto;

/**
 *
 * @author mario
 */
public class AuxilarMovInventario {

    private CfgProducto producto;
    private int cantidad;

    public AuxilarMovInventario() {
    }

    public AuxilarMovInventario(CfgProducto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public CfgProducto getProducto() {
        return producto;
    }

    public void setProducto(CfgProducto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

}
