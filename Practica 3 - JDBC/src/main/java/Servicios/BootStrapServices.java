package Servicios;

import org.h2.tools.Server;

import java.sql.Connection;
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
            server = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-ifNotExists").start();
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

        /*String sql_3 = "CREATE TABLE IF NOT EXISTS USUARIOS\n" +
                "(\n" +
                "  USUARIO VARCHAR(10) PRIMARY KEY NOT NULL,\n" +
                "  NOMBRE VARCHAR(50) NOT NULL,\n" +
                "  PASSWORD VARCHAR(100) NOT NULL\n" +
                ");";
        */
        
        /*String sql = "CREATE TABLE IF NOT EXISTS PRODUCTOS\n" +
                "(\n" +
                "  MATRICULA INTEGER PRIMARY KEY NOT NULL,\n" +
                "  NOMBRE VARCHAR(100) NOT NULL,\n" +
                "  APELLIDO VARCHAR(100) NOT NULL,\n" +
                "  TELEFONO VARCHAR(25) NOT NULL,\n" +
                "  CARRERA VARCHAR(50) NOT NULL\n" +
                ");";
*/
        String sql = "CREATE TABLE IF NOT EXISTS PRODUCTOS\n" +
                "(\n" +
                "  ID INTEGER AUTOINCREMENT PRIMARY KEY NOT NULL,\n" +
                "  NOMBRE VARCHAR(100) NOT NULL,\n" +
                "  PRECIO DOUBLE NOT NULL,\n" +
                "  TELEFONO VARCHAR(25) NOT NULL,\n" +
                "  CARRERA VARCHAR(50) NOT NULL\n" +
                ");";


        Connection con = DataBaseServices.getInstancia().getConexion();
        Statement statement = con.createStatement();
      //  statement.execute(sql_1);
        statement.execute(sql_1);
        statement.execute(sql_2);
        statement.close();
        con.close();
    }

}
