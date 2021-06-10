package Servicios;

import Modelo.CarroCompra;
import Modelo.Producto;
import Modelo.Usuario;
import Modelo.VentasProductos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TiendaService {

    public static TiendaService instancia;
   // private List<CarroCompra> carritos;
    private CarroCompra carrito;


    private TiendaService() {


    }

    public static TiendaService getInstancia(){
        if(instancia==null){
            instancia = new TiendaService();
        }
        return instancia;
    }

    public void setCarroCompra(CarroCompra carroCompra)
    {
        carrito = carroCompra;
    }

    public void limpiarCarrito() {
        carrito.getListaProductos().clear();
    }

    public boolean existeProductoCarrito(long idProducto)
    {
        for(int i = 0; i < carrito.getListaProductos().size(); i++)
        {
            if(carrito.getListaProductos().get(i).getId() == idProducto)
            {
                return true;
            }
        }
        return false;
    }

    public void deleteProductoCarrito(Producto producto)
    {
        for(int i = 0; i < carrito.getListaProductos().size();i++)
        {
            if(carrito.getListaProductos().get(i).getId() == producto.getId())
            {
                carrito.getListaProductos().remove(i);
            }
        }
    }

    public CarroCompra getCarrito()
    {
        return carrito;
    }

    public boolean addProductoCarrito(Producto producto)
    {
        //Si ya tiene el producto en el carrito, se le agrega la nueva cantidad
        if(existeProductoCarrito(producto.getId()))
        {

            for(int i = 0; i < carrito.getListaProductos().size();i++)
            {

                if(carrito.getListaProductos().get(i).getId() == producto.getId())
                {
                    carrito.getListaProductos().get(i).addCantidad(producto.getCantidad());

                    return true;
                }
            }
        }
        //Si no lo tiene, se le agrega
        else
        {
            carrito.addProducto(producto);
            return true;
        }
        return false;
    }

}
