package org.pucmm.jean.Modelo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
public class Producto implements Serializable {


    @Id
    @GeneratedValue
    private long id;
    private String nombre;
    private double precio;
    private String descripcion;

    @Transient
    private int cantidad;

    @OneToMany(orphanRemoval = true)
    private List<Foto> fotos;

   @OneToMany(orphanRemoval = true)
   private List<Comentario> comentarios;

    public Producto(String nombre, double precio, String descripcion)
    {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
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

    public List<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
