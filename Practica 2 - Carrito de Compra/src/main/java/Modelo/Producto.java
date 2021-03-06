package Modelo;

import java.math.BigDecimal;
import java.util.UUID;

public class Producto {

    private static long contador = 0;
    private long id;
    private String nombre;
    private int cantidad;
    private double precio;


    public Producto(String nombre, double precio)
    {
        contador = contador+=1;
        this.id = contador; //generador de ID
        this.nombre = nombre;
        this.precio = precio;
        cantidad = 1;

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

    public double getPrecioTotal() {
        return precio * cantidad;
    }

    public void setId(long id) {
        this.id = id;
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
