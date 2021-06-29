package Servicios;

import Modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductoService {

    public static ProductoService instancia;

    public static ProductoService getInstancia(){
        if(instancia==null){
            instancia = new ProductoService();
        }
        return instancia;
    }


    public Producto getProductoById(long id)
    {
        Producto producto = null;
        Connection con = null;

        try {

            String query = "select * from PRODUCTOS where id = ?";
            con = DataBaseServices.getInstancia().getConexion();
            PreparedStatement prepareStatement = con.prepareStatement(query);
            prepareStatement.setLong(1,id);

            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                producto = new Producto();
                producto.setId(rs.getLong("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getDouble("precio"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return producto;

    }

    public boolean editarProducto(long id, String nombre, Double precio){

        boolean ok =false;
        Connection con = null;

        try {

            String query = "update PRODUCTOS set nombre=?, precio=? where id = ?";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, nombre);
            prepareStatement.setDouble(2, precio);
            prepareStatement.setLong(3, id);

            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(ProductoService.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProductoService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }


    public boolean deleteProducto(Producto producto)
    {
        boolean ok =false;
        Connection con = null;

        try {

            String query = "delete from PRODUCTOS where id = ?";
            con = DataBaseServices.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);

            prepareStatement.setLong(1, producto.getId());

            int fila = prepareStatement.executeUpdate();
            ok = fila > 0;

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public boolean addNuevoProducto(Producto producto)
    {
        boolean ok =false;

        Connection con = null;
        try {

            String query = "insert into PRODUCTOS(nombre, precio) values(?,?)";
            con = DataBaseServices.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);

            prepareStatement.setString(1, producto.getNombre());
            prepareStatement.setDouble(2, producto.getPrecio());

            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(TiendaService.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(TiendaService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
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



}
