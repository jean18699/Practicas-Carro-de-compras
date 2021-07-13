package org.pucmm.jean.Servicios;

import org.pucmm.jean.Modelo.CarroCompra;
import org.pucmm.jean.Modelo.Foto;
import org.pucmm.jean.Modelo.Producto;

import java.util.ArrayList;
import java.util.List;

public class TiendaService {

    public static TiendaService instancia;
    private CarroCompra carrito;
    private List<Foto> fotos;
    private Foto ultimaFoto;

    private TiendaService() {
        fotos = new ArrayList<>();
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


    public void setCarrito(CarroCompra carrito) {
        this.carrito = carrito;
    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }

    public void addFoto(Foto foto)
    {
        fotos.add(foto);
    }

    public void deleteFoto(Foto foto)
    {
        for(int i = 0; i < fotos.size();i++)
        {
            if(fotos.get(i) == foto){
                fotos.remove(foto);
            }
        }
    }

    public Foto getFotoByNombre(String nombre)
    {
        for(int i = 0; i < fotos.size();i++)
        {
            if(fotos.get(i).getNombre().equals(nombre)){
                return fotos.get(i);
            }
        }
        return null;
    }

    public Foto getUltimaFoto() {
        return ultimaFoto;
    }

    public void setUltimaFoto(Foto ultimaFoto) {
        this.ultimaFoto = ultimaFoto;
    }
}