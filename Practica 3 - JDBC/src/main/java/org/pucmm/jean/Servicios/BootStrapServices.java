package org.pucmm.jean.Servicios;

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

    private static BootStrapServices instancia;

    private BootStrapServices() {

    }

    public static BootStrapServices getInstancia() {
        if (instancia == null) {
            instancia = new BootStrapServices();
        }
        return instancia;
    }

    /**
     * @throws SQLException
     */
    public static void startDb() {
        try {
            server = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-ifNotExists").start();
            String status = Server.createWebServer("-trace", "-webPort", "0").start().getStatus();
            System.out.println("Status Web: " + status);
        } catch (SQLException ex) {
            System.out.println("Problema con la base de datos: " + ex.getMessage());
            // ex.printStackTrace();
        }
    }

    /**
     * @throws SQLException
     */
    public static void stopDb() throws SQLException {
        if (server != null) {
            server.stop();
        }
    }

    public void init() {
        startDb();
    }


}