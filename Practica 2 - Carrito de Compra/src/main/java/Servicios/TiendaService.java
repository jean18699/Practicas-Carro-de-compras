package Servicios;

import Modelo.CarroCompra;
import Modelo.Producto;
import Modelo.Usuario;
import Modelo.VentasProductos;

import java.util.ArrayList;
import java.util.List;

public class TiendaService {

    public static TiendaService instancia;
   // private List<CarroCompra> carritos;
    private List<Producto> productos;
    private List<Usuario> usuarios;
    private List<VentasProductos> ventas;

    private TiendaService() {
        //carritos = new ArrayList<>();
        productos = new ArrayList<>();
        usuarios = new ArrayList<>();
        ventas = new ArrayList<>();

        //Agregando el admin
        usuarios.add(new Usuario("admin","Usuario Admin","admin"));

        //Agregando productos
        productos.add(new Producto("Computadora",  1000));
        productos.add(new Producto("Martillo",  512));
        productos.add(new Producto("Gasolina",  1500));
        productos.add(new Producto("Motor",  2000));
        productos.add(new Producto("Tornillo",  1300));
        productos.add(new Producto("Anillo",  400));
    }


    public static TiendaService getInstancia(){
        if(instancia==null){
            instancia = new TiendaService();
        }
        return instancia;
    }

    public Usuario crearUsuario(Usuario usuario)
    {
        if(existeUsuario(usuario.getUsuario()))
        {
            return null;
        }else
        {
            usuarios.add(usuario);
            return usuario;
        }
    }

    private boolean existeUsuario(String usuario)
    {
        for(int i = 0; i < usuarios.size(); i++)
        {
            if(usuarios.get(i).getUsuario().equalsIgnoreCase(usuario))
            {
                return true;
            }
        }

        return false;
    }

    public Usuario getUsuarioByNombreUsuario(String usuario)
    {
        for(int i = 0; i < usuarios.size();i++){
            if(usuarios.get(i).getUsuario().equalsIgnoreCase(usuario))
            {
                return usuarios.get(i);
            }
        }
        return null;
    }

    public Producto getProductoById(long id)
    {
        for(int i = 0; i < productos.size();i++){
            if(productos.get(i).getId() == id)
            {
                return productos.get(i);
            }
        }
        return null;
    }


    public void addProductoCarritoUsuario(Usuario usuario, Producto producto)
    {
        if(existeUsuario(usuario.getUsuario()) && !usuario.existeProductoCarrito(producto.getId()))
        {
            usuario.getCarrito().addProducto(producto);
        }
    }

    public void deleteProductoCarritoUsuario(Usuario usuario, Producto producto)
    {
        if(existeUsuario(usuario.getUsuario()) && usuario.existeProductoCarrito(producto.getId()))
        {
            usuario.getCarrito().deleteProducto(producto);
        }
    }



    public CarroCompra getCarritoUsuario(Usuario usuario)
    {
        if(existeUsuario(usuario.getUsuario()))
        {
            return usuario.getCarrito();
        }else
        {
            return null;
        }
    }

    public List<Producto> getListaProductos()
    {
        return productos;
    }


}
