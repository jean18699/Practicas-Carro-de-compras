package org.pucmm.jean.Controladores;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import org.pucmm.jean.Servicios.VentaService;

import java.util.*;

public class  DashboardControlador {

    private Javalin app;
    Map<String, Object> modelo = new HashMap<>();

    public DashboardControlador(Javalin app)
    {
        this.app = app;
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");

    }


    public void aplicarRutas() throws NumberFormatException {

        app.get("/dashboard",ctx -> {
            if(ctx.sessionAttribute("usuario") != null)
            {
                if(!ctx.sessionAttribute("usuario").equals("admin"))
                {
                    ctx.result("sin autorizacion");
                }else
                {
                    modelo.put("cantidadVentas", VentaService.getInstancia().getVentas());
                    modelo.put("ventas", VentaService.getInstancia().getVentas());
                    ctx.render("/vistas/templates/dashboard.html",modelo);
                }
            }else
            {
                ctx.redirect("/iniciarSesion");
            }


        });

    }
}
