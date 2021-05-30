package Modelo;

import java.util.List;

public class VentasProductos {

    private static long contador = 0;
    private long id;
    private String nombreCliente;
    private List<Producto> listaProductos;

    public VentasProductos(String nombreCliente, List<Producto> listaProductos)
    {
        contador = contador + 1;
        this.id = contador;
        this.nombreCliente = nombreCliente;
        this.listaProductos = listaProductos;
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
}
