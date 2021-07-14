package org.pucmm.jean;

import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import org.pucmm.jean.Controladores.ImagenControlador;
import org.pucmm.jean.Controladores.TiendaControlador;
import org.pucmm.jean.Controladores.WebSocketControlador;
import org.pucmm.jean.Servicios.BootStrapServices;
import org.pucmm.jean.Servicios.DataBaseServices;

import java.sql.SQLException;

public class Main {

    //indica el modo de operacion para la base de datos.
    private static String modoConexion = "";

    public static void main(String[] args) throws SQLException {

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
        new WebSocketControlador(app).aplicarRutas();


    }


    /**
     * Metodo para indicar el puerto en Heroku
     * @return
     */
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7000; //Retorna el puerto por defecto en caso de no estar en Heroku.
    }

    /**
     * Nos
     * @return
     */
    public static String getModoConexion(){
        return modoConexion;
    }

}
