package org.pucmm.jean.Modelo;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Set;

@Entity
public class Venta implements Serializable {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;
    private String nombreCliente;

    @ManyToMany()
    private List<Producto> listaProductos;


    private Date fechaCompra;

    @OneToOne
    private int cantidad;


    public Venta(String nombreCliente, List<Producto> listaProductos, Date fechaCompra)
    {
        this.nombreCliente = nombreCliente;
        this.listaProductos = listaProductos;
        this.fechaCompra = fechaCompra;
       // this.usuario = usuario;
    }

    public Venta() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    /*public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }*/

   /*public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }*/

   /* public long getId_venta() {
        return id_venta;
    }

    public void setId_venta(long id_venta) {
        this.id_venta = id_venta;
    }*/

   /* public double getTotalVenta()
    {
        double total = 0;

        for(int i = 0; i < listaProductos.size(); i++)
        {
            total += (listaProductos.get(i).getPrecioTotal());
        }

        return total;
    }*/


}
