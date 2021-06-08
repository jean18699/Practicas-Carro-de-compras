package Servicios;

import Modelo.CarroCompra;
import Modelo.Producto;
import Modelo.Usuario;
import Modelo.VentasProductos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TiendaService {

    public static TiendaService instancia;
   // private List<CarroCompra> carritos;


    private List<VentasProductos> ventas;

    private TiendaService() {
        //carritos = new ArrayList<>();

        ventas = new ArrayList<>();


        //Agregando productos
       /* productos.add(new Producto("Computadora",  1000));
        productos.add(new Producto("Martillo",  512));
        productos.add(new Producto("Gasolina",  1500));
        productos.add(new Producto("Motor",  2000));
        productos.add(new Producto("Tornillo",  1300));
        productos.add(new Producto("Anillo",  400));*/
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

    public Producto getProductoById(long id)
    {
       /* for(int i = 0; i < productos.size();i++){
            if(productos.get(i).getId() == id)
            {
                return productos.get(i);
            }
        }*/
        return null;
    }




    public void deleteProducto(Producto producto)
    {
/*        getListaProductos().remove(producto);

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
        }*/
    }

    public void addNuevoProducto(Producto producto)
    {
        getListaProductos().add(producto);
    }

    public void realizarVenta(VentasProductos venta)
    {
        this.ventas.add(venta);

    }


    public List<Producto> getListaProductos()
    {
        Connection con = null;
        List<Producto> productos = new ArrayList<>();

        try
        {
            String query = "select * from PRODUCTOS";
            con = DataBaseServices.getInstancia().getConexion();
            PreparedStatement prepareStatement = con.prepareStatement(query);
            ResultSet rs = prepareStatement.executeQuery();

            while(rs.next()){
                Producto producto = new Producto();

                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getDouble("precio"));
               //
                productos.add(producto);
            }

        }catch (SQLException ex) {
            Logger.getLogger(TiendaService.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(TiendaService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return productos;
    }


    public void limpiarCarrito(Usuario usuario) {

        for(int i = 0; i < usuario.getCarrito().getListaProductos().size();i++)
        {
            usuario.getCarrito().getListaProductos().clear();
        }
    }
}
