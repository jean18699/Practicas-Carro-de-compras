package Servicios;

import Modelo.Producto;
import Modelo.Usuario;
import Modelo.VentasProductos;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VentaService {

    public static VentaService instancia;
    private ArrayList<VentasProductos> ventas;
    private static long contador = 0;

    public VentaService()
    {
        ventas = new ArrayList<>();
    }

    public static VentaService getInstancia(){
        if(instancia==null){
            instancia = new VentaService();
        }
        return instancia;
    }

    public ArrayList<VentasProductos> getVentas()
    {
        Connection con = null;
        ventas = new ArrayList<>();
        String nombre_cliente = null;
        Date fechaCompra = null;

        try {
            String query = "SELECT nombre_cliente,FECHA_COMPRA, P.NOMBRE,PRECIO,CANTIDAD\n" +
                    "FROM VENTAS\n" +
                    "INNER JOIN PRODUCTOS P on VENTAS.ID_PRODUCTO = P.ID\n" +
                    "Where id_venta = ?;";

            String query2 = "select distinct id_venta from VENTAS;";


            con = DataBaseServices.getInstancia().getConexion();


            PreparedStatement prepareStatementCantidad = con.prepareStatement(query2);
            ResultSet rs = prepareStatementCantidad.executeQuery();


            while(rs.next()){

                PreparedStatement prepareStatement = con.prepareStatement(query);
                prepareStatement.setInt(1, rs.getInt("id_venta"));
                ResultSet rs2 = prepareStatement.executeQuery();

                ArrayList<Producto> productos = new ArrayList<>();

                while(rs2.next()) {
                    nombre_cliente = rs2.getString("nombre_cliente");
                    fechaCompra = rs2.getDate("fecha_compra");
                    Producto producto = new Producto();
                    producto.setPrecio(rs2.getDouble("precio"));
                    producto.setNombre(rs2.getString("nombre"));
                    producto.setCantidad(rs2.getInt("cantidad"));
                    productos.add(producto);
                }
                VentasProductos venta = new VentasProductos(
                       nombre_cliente,productos,fechaCompra
                );

                ventas.add(venta);

            }

        }catch (SQLException ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ventas;
    }

    public boolean realizarVenta(Usuario usuario, VentasProductos venta)
    {

        long id_venta = 0;
        boolean ok =false;

        Connection con = null;
        try {

            String query = "insert into VENTAS(id_venta,nombre_cliente, fecha_compra,id_producto,usuario,cantidad) values(?,?,?,?,?,?)";

            con = DataBaseServices.getInstancia().getConexion();

            Statement stmt = con.createStatement();

            //Logica para identificar la el id de la venta, el cual servira para determinar que productos pertenecen a esa venta
            ResultSet rs = stmt.executeQuery("select max(ID_VENTA) as ID_VENTA from VENTAS"); //se toma el ultimo ID que haya registrado

            int fila = 0;


            while(rs.next())
            {
                id_venta = rs.getLong("ID_VENTA");
            }

            if(id_venta == 0)
            {
                id_venta = 1;
            }else
            {
                id_venta = id_venta + 1;
            }


            for(Producto producto : TiendaService.getInstancia().getCarrito().getListaProductos())
            {
                PreparedStatement prepareStatement = con.prepareStatement(query);
                //System.out.println(venta.getFechaCompra());
                prepareStatement.setLong(1, id_venta);
                prepareStatement.setString(2, venta.getNombreCliente());
                prepareStatement.setDate(3, venta.getFechaCompra());
                prepareStatement.setLong(4, producto.getId());
                prepareStatement.setString(5, usuario.getUsuario());
                prepareStatement.setInt(6, producto.getCantidad());
                fila = prepareStatement.executeUpdate();

            }

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

        //this.ventas.add(venta);

    }


}
