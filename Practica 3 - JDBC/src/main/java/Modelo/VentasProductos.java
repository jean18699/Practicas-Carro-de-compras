package Modelo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class VentasProductos {

    private static long contador = 0;
    private long id;
    private String nombreCliente;
    private List<Producto> listaProductos;
    private Date fechaCompra;

    public VentasProductos(String nombreCliente, List<Producto> listaProductos, Date fechaCompra)
    {
        contador = contador + 1;
        this.id = contador;
        this.nombreCliente = nombreCliente;
        this.listaProductos = listaProductos;
        this.fechaCompra = fechaCompra;
    }

    public VentasProductos() {

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

    public double getTotalVenta()
    {
        double total = 0;

        for(int i = 0; i < listaProductos.size(); i++)
        {
            total += (listaProductos.get(i).getPrecioTotal());
        }

        return total;
    }

}
