package Modelo;

import java.util.ArrayList;
import java.util.List;

public class CarroCompra {

    private static int contador = 0;
    private long id = 0;
    private List<Producto> listaProductos;

    public CarroCompra()
    {
        contador = contador+1;
        id = contador;
        listaProductos = new ArrayList<>();
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

    public void addProducto(Producto producto)
    {
        this.listaProductos.add(producto);
    }

    public void deleteProducto(Producto producto)
    {
        for(int i = 0; i <  listaProductos.size(); i ++)
        {
            if(listaProductos.get(i).getId() == producto.getId())
            {
                listaProductos.remove(listaProductos.get(i));
            }
        }
    }

    public double getTotalProductos() {

        double total = 0;

        for(int i = 0; i < listaProductos.size(); i++)
        {
            total += (listaProductos.get(i).getPrecioTotal());
        }

        return total;
    }
}
