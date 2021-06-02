package Modelo;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class VentasProductos {

    private static long contador = 0;
    private long id;
    private String nombreCliente;
    private List<Producto> listaProductos;
    private LocalDate fechaCompra;

    public VentasProductos(String nombreCliente, List<Producto> listaProductos, LocalDate fechaCompra)
    {
        contador = contador + 1;
        this.id = contador;
        this.nombreCliente = nombreCliente;
        this.listaProductos = listaProductos;
        this.fechaCompra = fechaCompra;
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

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
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
