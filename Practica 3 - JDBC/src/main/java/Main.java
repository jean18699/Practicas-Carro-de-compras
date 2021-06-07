import Controladores.TiendaControlador;
import Servicios.TiendaService;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.plugin.openapi.OpenApiPlugin;

public class Main {

    public static void main(String[] args) {

        System.out.println(TiendaService.getInstancia().getUsuarioByNombreUsuario("admin").getNombre());

        //Creando la instancia del servidor.
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/bootstrap-5.0.1-dist"); //desde la carpeta de resources
            config.registerPlugin(new RouteOverviewPlugin("/rutas")); //aplicando plugins de las rutas
            config.enableCorsForAllOrigins();

        });

        app.start(7000);

        new TiendaControlador(app).aplicarRutas();

    }

}
