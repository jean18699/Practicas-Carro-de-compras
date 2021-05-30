package Modelo;

import java.util.List;

public class CarroCompra {

    private static int contador = 0;
    private long id = 0;
    private List<Producto> listaProductos;

    public CarroCompra()
    {
        contador = contador+1;
        id = contador;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }
}
