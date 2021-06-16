package org.pucmm.jean.Modelo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Producto_Comprado implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    private String nombre;
    private double precio;
    private int cantidad;


    public Producto_Comprado(String nombre, double precio, int cantidad)
    {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public Producto_Comprado()
    {

    }


    public double getPrecioTotal() {
        return precio * cantidad;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
