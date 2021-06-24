package org.pucmm.jean;

import org.pucmm.jean.Controladores.ImagenControlador;
import org.pucmm.jean.Controladores.TiendaControlador;
import org.pucmm.jean.Servicios.BootStrapServices;
import org.pucmm.jean.Servicios.DataBaseServices;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;

import java.sql.SQLException;

public class Main {

    //indica el modo de operacion para la base de datos.
    private static String modoConexion = "";

    public static void main(String[] args) throws SQLException {

        if(args.length >= 1){
            modoConexion = args[0];
            System.out.println("Modo de Operacion: "+modoConexion);
        }

        //Iniciando la base de datos.
        if(modoConexion.isEmpty()) {
            BootStrapServices.getInstancia().init();
        }

        //Iniciando el servicio
        BootStrapServices.startDb();

        //Prueba de Conexión.
        DataBaseServices.getInstancia().testConexion();



        //Creando la instancia del servidor.
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/bootstrap-5.0.1-dist"); //desde la carpeta de resources
            config.registerPlugin(new RouteOverviewPlugin("/rutas")); //aplicando plugins de las rutas
            config.enableCorsForAllOrigins();
        }).start(getHerokuAssignedPort());


        //BootStrapServices.crearTablas();
        //BootStrapServices.stopDb();
        new TiendaControlador(app).aplicarRutas();
        new ImagenControlador(app).aplicarRutas();


    }


    /**
     * Metodo para indicar el puerto en Heroku
     * @return
     */
    static int getHerokuAssignedPort() {
        String herokuPort = System.getenv("PORT");
        if (herokuPort != null) {
            return Integer.parseInt(herokuPort);
        }
        return 7000;
    }

    /**
     * Nos
     * @return
     */
    public static String getModoConexion(){
        return modoConexion;
    }

}
