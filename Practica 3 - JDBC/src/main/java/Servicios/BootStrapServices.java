package Servicios;

import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by vacax on 27/05/16.
 */
public class BootStrapServices {

    private static Server server;

    /**
     *
     * @throws SQLException
     */
    public static void startDb()  {
        try {
            server = Server.createTcpServer("-tcpPort", "9093", "-tcpAllowOthers", "-ifNotExists").start();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /**
     *
     * @throws SQLException
     */
    public static void stopDb() throws SQLException {
        if(server!=null) {
            server.stop();
        }
    }


    /**
     * Metodo para recrear las tablas necesarios
     * @throws SQLException
     */
    public static void crearTablas() throws SQLException {

        String sql_1 = "CREATE TABLE IF NOT EXISTS PRODUCTOS\n" +
                "(\n" +
                "ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                "  NOMBRE VARCHAR(100) NOT NULL,\n" +
                "  PRECIO VARCHAR(100) NOT NULL\n" +
                ");";


        String sql_2 = "CREATE TABLE IF NOT EXISTS USUARIOS\n" +
                "(\n" +
                "  USUARIO VARCHAR(10) PRIMARY KEY NOT NULL,\n" +
                "  NOMBRE VARCHAR(50) NOT NULL,\n" +
                "  PASSWORD VARCHAR(100) NOT NULL\n" +
                ");";


        String sql_3 = "CREATE TABLE IF NOT EXISTS VENTAS\n" +
                "(\n" +
                "  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                "  NOMBRE_CLIENTE VARCHAR(50) NOT NULL,\n" +
                " ID_PRODUCTO LONG, \n" +
                " USUARIO VARCHAR(25) NOT NULL,\n" +
                " CANTIDAD INTEGER NOT NULL,\n" +
                " FECHA_COMPRA DATE NOT NULL,\n" +
                " ID_VENTA LONG NOT NULL,\n" +
                "CONSTRAINT fk_venta_producto\n" +
                "FOREIGN KEY(ID_PRODUCTO) REFERENCES PRODUCTOS(ID)\n" +
                "ON DELETE CASCADE" +
                ");";


        String sql_admin = "INSERT INTO USUARIOS(USUARIO,NOMBRE,PASSWORD) VALUES('admin','Administrador','admin');";

        Connection con = DataBaseServices.getInstancia().getConexion();
        Statement statement = con.createStatement();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select USUARIO from USUARIOS where USUARIO = 'admin'"); //se toma el ultimo ID que haya registrado

        if(!rs.next())
        {
            statement.execute(sql_admin);
        }


        statement.execute(sql_1);
        statement.execute(sql_2);
        statement.execute(sql_3);
        statement.close();
        con.close();
    }

}
