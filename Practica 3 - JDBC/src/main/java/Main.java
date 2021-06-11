import Controladores.TiendaControlador;
import Servicios.BootStrapServices;
import Servicios.DataBaseServices;
import Servicios.TiendaService;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.plugin.openapi.OpenApiPlugin;

import java.sql.SQLException;

public class Main {

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


        });

        app.start(7000);
        BootStrapServices.crearTablas();
        BootStrapServices.stopDb();
        new TiendaControlador(app).aplicarRutas();

    }

}
