package Servicios;

import Modelo.CarroCompra;
import Modelo.Producto;
import Modelo.Usuario;
import Modelo.VentasProductos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TiendaService {

    public static TiendaService instancia;
    private List<CarroCompra> carritos;
    private List<Producto> productos;
    private List<Usuario> usuarios;
    private List<VentasProductos> ventas;

    private TiendaService() {
        carritos = new ArrayList<>();
        productos = new ArrayList<>();
        usuarios = new ArrayList<>();
        ventas = new ArrayList<>();

        //Agregando el admin
        usuarios.add(new Usuario("admin","Usuario Admin","admin"));

        //Agregando productos
        productos.add(new Producto("Computadora",  1000));
        productos.add(new Producto("Martillo",  512));

    }


    public static TiendaService getInstancia(){
        if(instancia==null){
            instancia = new TiendaService();
        }
        return instancia;
    }

    public void crearUsuario(String usuario, String nombre, String password)
    {
        if(existeUsuario(usuario))
        {
            return;
        }else
        {
            Usuario nuevoUsuario = new Usuario(usuario,nombre,password);
            usuarios.add(nuevoUsuario);
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

    public List<Producto> getListaProductos()
    {
        return productos;
    }


}
