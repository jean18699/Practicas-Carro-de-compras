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

        app.get("/autenticar", ctx -> {

           if(ctx.sessionAttribute("usuario") == null)
           {
               ctx.sessionAttribute("usuario","admin");
           }

        });

        app.get("/listarProductos", ctx -> {


            Map<String, Object> modelo = new HashMap<>();
            modelo.put("productos", TiendaService.getInstancia().getListaProductos());

            if(ctx.sessionAttribute("usuario") == null)
            {
                modelo.put("totalCarrito", 0);
            }else
            {
                modelo.put("totalCarrito", TiendaService.getInstancia().getCarritoUsuario(TiendaService.getInstancia().
                        getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"))).getListaProductos().size());
                modelo.put("usuario",ctx.sessionAttribute("usuario"));
            }


            //enviando al sistema de plantilla.
            ctx.render("/templates/ListadoProductos.html", modelo);
        });

        app.post("/carrito/agregar/",ctx -> {


            if(ctx.sessionAttribute("usuario") == null)
            {
                ctx.result("Usuario no logeado");
            }else
            {
                Usuario usuario = TiendaService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));
                Producto producto = TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto")));
                System.out.println(ctx.formParam("cantidadProducto"));
                producto.setCantidad(Integer.parseInt(ctx.formParam("cantidadProducto")));

                TiendaService.getInstancia().addProductoCarritoUsuario(usuario,producto);
                //System.out.println(TiendaService.getInstancia().getCarritoUsuario(usuario).getListaProductos());

                ctx.redirect("/carrito");

            }



        });

        app.post("/carrito/eliminar/",ctx -> {

            if(ctx.sessionAttribute("usuario") == null)
            {
                ctx.result("Usuario no logeado");
            }else
            {
                Usuario usuario = TiendaService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));
                Producto producto = TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto")));
                TiendaService.getInstancia().deleteProductoCarritoUsuario(usuario,producto);
                ctx.redirect("/"+usuario.getUsuario()+"/carrito");
            }


        });

        app.get("/carrito", ctx -> {


            if(ctx.sessionAttribute("usuario") == null)
            {
                ctx.result("Usuario no logeado");
            }else
            {
                Usuario usuario = TiendaService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));
                CarroCompra carrito = TiendaService.getInstancia().getCarritoUsuario(usuario);

                Map<String, Object> modelo = new HashMap<>();
                modelo.put("productosCarrito", carrito.getListaProductos());
                modelo.put("totalCarrito", usuario.getCarrito().getListaProductos().size());

                //enviando al sistema de plantilla.
                ctx.render("/templates/Carrito.html", modelo);
            }

        });

        app.get("/controlProductos", ctx -> {


            /*if(ctx.sessionAttribute("usuario") == null)
            {
                ctx.result("Usuario no logeado");
            }else
            {*/
                Usuario usuario = TiendaService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));


                Map<String, Object> modelo = new HashMap<>();
                modelo.put("productosRegistrados", TiendaService.getInstancia().getListaProductos());
               // modelo.put("totalCarrito", usuario.getCarrito().getListaProductos().size());

                //enviando al sistema de plantilla.
                ctx.render("/templates/CRUD_Productos.html", modelo);
         /*   }*/

        });


    }

}
