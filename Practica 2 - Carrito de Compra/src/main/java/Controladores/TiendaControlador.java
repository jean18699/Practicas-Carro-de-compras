package Controladores;

import Modelo.CarroCompra;
import Modelo.Producto;
import Modelo.Usuario;
import Servicios.TiendaService;
import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;

import java.time.Year;
import java.util.HashMap;
import java.util.Map;

public class TiendaControlador {

    private Javalin app;


    public TiendaControlador(Javalin app){
        this.app = app;
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
    }


    public void aplicarRutas(){

        app.get("/listarProductos", ctx -> {

            Map<String, Object> modelo = new HashMap<>();
            modelo.put("productos", TiendaService.getInstancia().getListaProductos());

            //enviando al sistema de plantilla.
            ctx.render("/templates/ListadoProductos.html", modelo);
        });

        app.post("/:usuario/carrito/agregar/",ctx -> {



            Usuario usuario = TiendaService.getInstancia().getUsuarioByNombreUsuario(ctx.pathParam("usuario"));
            Producto producto = TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto")));

         //   producto.setCantidad(Integer.parseInt(ctx.formParam("cantidadProducto")));
            System.out.println(ctx.formParam("cantidadProducto"));
            producto.setCantidad(Integer.parseInt(ctx.formParam("cantidadProducto")));

            TiendaService.getInstancia().addProductoCarritoUsuario(usuario,producto);
            //System.out.println(TiendaService.getInstancia().getCarritoUsuario(usuario).getListaProductos());

            ctx.redirect("/"+usuario.getUsuario()+"/carrito");

        });

        app.get("/:usuario/carrito", ctx -> {



            Usuario usuario = TiendaService.getInstancia().getUsuarioByNombreUsuario(ctx.pathParam("usuario"));
            CarroCompra carrito = TiendaService.getInstancia().getCarritoUsuario(usuario);

            for(int i = 0;i<carrito.getListaProductos().size(); i++)
            {

               // System.out.println(carrito.getListaProductos().get(i).getId());
            }

            Map<String, Object> modelo = new HashMap<>();
            modelo.put("productosCarrito", carrito.getListaProductos());
            modelo.put("totalCarrito", carrito.getTotalProductos());

            //enviando al sistema de plantilla.
            ctx.render("/templates/Carrito.html", modelo);
        });



    }

}
