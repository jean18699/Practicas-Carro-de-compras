package org.pucmm.jean.Servicios;

import org.h2.tools.Server;

import java.sql.SQLException;

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

    /*    String sql_1 = "CREATE TABLE IF NOT EXISTS PRODUCTOS\n" +
                "(\n" +
                "ID LONG AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                "  NOMBRE VARCHAR(100) NOT NULL,\n" +
                "  PRECIO DOUBLE NOT NULL\n" +
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
                "CONSTRAINT fk_venta_usuario\n" +
                "FOREIGN KEY(USUARIO) REFERENCES USUARIOS(USUARIO)\n" +
                "ON DELETE CASCADE," +
                "CONSTRAINT fk_venta_producto\n" +
                "FOREIGN KEY(ID_PRODUCTO) REFERENCES PRODUCTOS(ID)\n" +
                "ON DELETE CASCADE" +
                ");";


        //Query para guardar el admin por defecto
        String sql_admin = "INSERT INTO USUARIOS(USUARIO,NOMBRE,PASSWORD) VALUES('admin','Administrador','admin');";

        //Creando la conexion
        Connection con = DataBaseServices.getInstancia().getConexion();
        Statement statement = con.createStatement();
        Statement stmt = con.createStatement();



        //Ejecutando los queries

        //Creacion de la tabla productos
        statement.execute(sql_1);


        //Creacion de la tabla usuarios
        statement.execute(sql_2);


        ResultSet rs = stmt.executeQuery("select USUARIO from USUARIOS where USUARIO = 'admin'");

        if(!rs.next())
        {
            statement.execute(sql_admin);
        }

        //Creacion de la tabla ventas
        statement.execute(sql_3);
        statement.close();

        con.close();*/
    }

}
