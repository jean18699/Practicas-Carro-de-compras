package org.pucmm.jean.Modelo;

import javax.persistence.*;
import java.io.Serializable;


@Entity
public class Producto implements Serializable {


    @Id
    @GeneratedValue
    private long id;
    private String nombre;
    private double precio;

    @Transient
    private int cantidad;



    public Producto(String nombre, double precio)
    {
        this.nombre = nombre;
        this.precio = precio;

    }



    public Producto() {

    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrecioTotal() {
        return precio * cantidad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public void addCantidad(int cantidad) {
        this.cantidad += cantidad;
    }


}
