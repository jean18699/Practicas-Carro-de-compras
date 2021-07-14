package org.pucmm.jean.Servicios;

import org.h2.tools.Server;

import java.sql.SQLException;

/**
 * Created by vacax on 27/05/16.
 */
public class BootStrapServices {

    private static Server server;
    private static BootStrapServices instancia;

    private BootStrapServices() {

    }

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

    public void init() {
        startDb();
    }



}
