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
        usuarios.add(new Usuario("admin","Administrador","admin"));
        usuarios.add(new Usuario("jean18699","Administrador","abc"));

        //Agregando productos
        productos.add(new Producto("Computadora",  1000));
        productos.add(new Producto("Martillo",  512));
        productos.add(new Producto("Gasolina",  1500));
        productos.add(new Producto("Motor",  2000));
        productos.add(new Producto("Tornillo",  1300));
        productos.add(new Producto("Anillo",  400));
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public List<Usuario> getUsuarios() {
         return usuarios;
      /*  ArrayList<Usuario> listaUsuarios = new ArrayList<>(); //Creo una nueva lista para almacenar todos los usuarios menos el Admin.

        for(Usuario usuario : usuarios)
        {
            if(!usuario.getUsuario().equalsIgnoreCase("admin"))
            {
                listaUsuarios.add(usuario);
            }
        }*/

    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<VentasProductos> getVentas() {
        return ventas;
    }

    public void setVentas(List<VentasProductos> ventas) {
        this.ventas = ventas;
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

            System.out.println(usuarios);
            return usuario;
        }
    }

    public Usuario eliminarUsuario(Usuario usuario)
    {
        if(!existeUsuario(usuario.getUsuario()))
        {
            return null;
        }else
        {
            for(int i = 0; i < usuarios.size();i++)
            {
                if(usuarios.get(i).getUsuario().equalsIgnoreCase(usuario.getUsuario()))
                {
                    usuarios.remove(i);
                }
            }
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


    public boolean addProductoCarritoUsuario(Usuario usuario, Producto producto)
    {
        //Si ya tiene el producto en el carrito, se le agrega la nueva cantidad
        if(existeUsuario(usuario.getUsuario()) && usuario.existeProductoCarrito(producto.getId()))
        {
            for(int i = 0; i < usuario.getCarrito().getListaProductos().size();i++)
            {
                if(usuario.getCarrito().getListaProductos().get(i).getId() == producto.getId())
                {
                    usuario.getCarrito().getListaProductos().get(i).addCantidad(producto.getCantidad());

                    return true;
                }
            }
        }
        //Si no lo tiene, se le agrega
        else if(existeUsuario(usuario.getUsuario()) && !usuario.existeProductoCarrito(producto.getId()))
        {
            usuario.getCarrito().addProducto(producto);

            return true;
        }
        return false;
    }

    public void deleteProductoCarritoUsuario(Usuario usuario, Producto producto)
    {
        if(existeUsuario(usuario.getUsuario()) && usuario.existeProductoCarrito(producto.getId()))
        {


            usuario.getCarrito().deleteProducto(producto);
        }
    }

    public void deleteProducto(Producto producto)
    {
        getListaProductos().remove(producto);

        //Eliminando el producto quitado de los carritos de los usuarios
        for(int i = 0; i < usuarios.size();i++)
        {
            for(int j = 0; j < usuarios.get(i).getCarrito().getListaProductos().size();j++)
            {
               if(usuarios.get(i).getCarrito().getListaProductos().get(j).getId() == producto.getId())
               {
                   usuarios.get(i).getCarrito().getListaProductos().remove(j);
               }
            }
        }
    }

    public void addNuevoProducto(Producto producto)
    {
        getListaProductos().add(producto);
    }

    public void realizarVenta(VentasProductos venta)
    {
        this.ventas.add(venta);

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


    public void limpiarCarrito(Usuario usuario) {

        for(int i = 0; i < usuario.getCarrito().getListaProductos().size();i++)
        {
            usuario.getCarrito().getListaProductos().clear();
        }
    }
}
