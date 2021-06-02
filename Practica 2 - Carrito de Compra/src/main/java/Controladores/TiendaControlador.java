package Controladores;

import Modelo.CarroCompra;
import Modelo.Producto;
import Modelo.Usuario;
import Servicios.TiendaService;
import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;

import java.util.HashMap;
import java.util.Map;

public class TiendaControlador {

    private Javalin app;


    public TiendaControlador(Javalin app){
        this.app = app;
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");

    }


    public void aplicarRutas() throws NumberFormatException{

        app.get("/autenticar", ctx -> {

           if(ctx.sessionAttribute("usuario") == null)
           {
               ctx.sessionAttribute("usuario","admin");
               ctx.redirect("/listaProductos");
           }

        });

        app.get("/listaProductos", ctx -> {


            Map<String, Object> modelo = new HashMap<>();
            modelo.put("productos", TiendaService.getInstancia().getListaProductos());

            if(ctx.sessionAttribute("usuario") == null)
            {
                modelo.put("cantidadCarrito", 0);
            }else
            {
                modelo.put("cantidadCarrito", TiendaService.getInstancia().getCarritoUsuario(TiendaService.getInstancia().
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

                ctx.formParamMap().forEach((key, lista) -> {
                    System.out.println(String.format("[%s] = [%s]", key, String.join(",", lista)));
                });
                System.out.println(ctx.formParam("cantidad"));

               Producto producto = new Producto(
                       TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getNombre(),
                       TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getPrecio()
               );
               producto.setId(TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getId());

               producto.setCantidad(
                       Integer.parseInt(ctx.formParam("cantidad"))
               );

                TiendaService.getInstancia().addProductoCarritoUsuario(usuario,producto);
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

                Producto producto = new Producto(
                        TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getNombre(),
                        TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getPrecio()
                );
                producto.setId(TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getId());

                TiendaService.getInstancia().deleteProductoCarritoUsuario(usuario,producto);
                ctx.redirect("/carrito");
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

                int totalCarrito = 0;
                for(int i = 0; i < carrito.getListaProductos().size();i++)
                {
                    totalCarrito += carrito.getListaProductos().get(i).getPrecioTotal();
                }

                Map<String, Object> modelo = new HashMap<>();
                modelo.put("cantidadCarrito", carrito.getListaProductos().size());
                modelo.put("productosCarrito", carrito.getListaProductos());
                modelo.put("totalCarrito", totalCarrito);

                //enviando al sistema de plantilla.
                ctx.render("/templates/Carrito.html", modelo);
            }

        });

        app.get("/controlProductos", ctx -> {


            if(ctx.sessionAttribute("usuario") == null)
            {
                ctx.result("Usuario no logeado");
            }else
            {
                Usuario usuario = TiendaService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));
                CarroCompra carrito = TiendaService.getInstancia().getCarritoUsuario(usuario);
                Map<String, Object> modelo = new HashMap<>();

                modelo.put("cantidadCarrito", carrito.getListaProductos().size());
                modelo.put("productos", TiendaService.getInstancia().getListaProductos());

                //enviando al sistema de plantilla.
                ctx.render("/templates/CRUD_Productos.html", modelo);
            }

        });

        app.post("/controlProductos/eliminarProducto", ctx -> {

            if(ctx.sessionAttribute("usuario") == null)
            {
                ctx.result("Usuario no logeado");
            }else
            {
                TiendaService.getInstancia().deleteProducto(TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("eliminarProducto"))));
                ctx.redirect("/controlProductos");
            }

        });

        app.get("/agregarProducto", ctx -> {

            if(ctx.sessionAttribute("usuario") == null)
            {
                ctx.result("Usuario no logeado");
            }else
            {
               // TiendaService.getInstancia().deleteProducto(TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("eliminarProducto"))));
                ctx.render("/templates/Crear_Producto.html");
            }

        });

        app.post("/agregarProducto/crear", ctx -> {

            if(ctx.sessionAttribute("usuario") == null)
            {
                ctx.result("Usuario no logeado");
            }else
            {
                Producto producto = new Producto(
                        ctx.formParam("nombreProducto"),
                        Double.parseDouble(ctx.formParam("precioProducto"))
                );

                TiendaService.getInstancia().addNuevoProducto(producto);
                ctx.redirect("/controlProductos");
            }

        });


        app.post("/editarProducto", ctx -> {

         //   System.out.println(ctx.formParam("idProducto"));


            Usuario usuario = TiendaService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));
            Producto producto = new Producto(
                    TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getNombre(),
                    TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getPrecio()
            );
            producto.setId(TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getId());

            CarroCompra carrito = TiendaService.getInstancia().getCarritoUsuario(usuario);


            Map<String, Object> modelo = new HashMap<>();

            modelo.put("cantidadCarrito", carrito.getListaProductos().size());
            modelo.put("producto", producto);
            ctx.render("/templates/Editar_Producto.html", modelo);

        });


           app.post("/editarProducto/:idProducto", ctx -> {

               System.out.println(ctx.formParam("nombreProducto"));
               System.out.println(ctx.formParam("precioProducto"));


               TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.pathParam("idProducto")
               )).setNombre(ctx.formParam("nombreProducto"));

               TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.pathParam("idProducto")
               )).setPrecio(Double.parseDouble(ctx.formParam("precioProducto")));

               ctx.redirect("/controlProductos");

           /* Producto producto = TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.pathParam("producto")));


            Map<String, Object> modelo = new HashMap<>();
            modelo.put("producto", producto);
            ctx.render("/templates/CRUD_Productos.html", modelo);
*/
        });

    }

}
