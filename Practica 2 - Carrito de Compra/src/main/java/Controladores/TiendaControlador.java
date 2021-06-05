package Controladores;

import Modelo.CarroCompra;
import Modelo.Producto;
import Modelo.Usuario;
import Modelo.VentasProductos;
import Servicios.TiendaService;
import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TiendaControlador {

    private Javalin app;


    public TiendaControlador(Javalin app) {
        this.app = app;
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");

    }


    public void aplicarRutas() throws NumberFormatException {

        app.get("/iniciarSesion", ctx -> {

            if (ctx.sessionAttribute("usuario") == null) {
                ctx.render("/templates/Login.html");
            } else {
                ctx.redirect("/listaProductos");
            }

        });

        app.post("/autenticar", ctx -> {

            if (TiendaService.getInstancia().getUsuarioByNombreUsuario(ctx.formParam("nombreUsuario")) != null) {

                if (!TiendaService.getInstancia().getUsuarioByNombreUsuario(ctx.formParam("nombreUsuario")).getPassword().equals(ctx.formParam("password"))) {
                    ctx.redirect("/iniciarSesion");
                } else {
                    if (ctx.sessionAttribute("usuario") == null) {
                        ctx.sessionAttribute("usuario", ctx.formParam("nombreUsuario"));
                        ctx.redirect("/iniciarSesion");
                    }
                }
            }else
            {
                ctx.redirect("/iniciarSesion");
            }
        });

        app.get("/cerrarSesion", ctx -> {
                ctx.req.getSession().invalidate();
                ctx.redirect("/iniciarSesion");
        });


        app.get("/listaProductos", ctx -> {

            //Cargando productos para cualquier persona que entre al sistema, con o sin autenticar.
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("productos", TiendaService.getInstancia().getListaProductos());

            if (ctx.sessionAttribute("usuario") == null) {
                modelo.put("cantidadCarrito", 0);
            } else {
                modelo.put("cantidadCarrito", TiendaService.getInstancia().getCarritoUsuario(TiendaService.getInstancia().
                        getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"))).getListaProductos().size());
                modelo.put("usuario", ctx.sessionAttribute("usuario"));
            }

            //enviando al sistema de plantilla.
            ctx.render("/templates/ListadoProductos.html", modelo);
        });

        app.get("/listaUsuarios", ctx -> {

            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");
            }
            else{

                Map<String, Object> modelo = new HashMap<>();
                modelo.put("usuarios", TiendaService.getInstancia().getUsuarios());

                if (ctx.sessionAttribute("usuario") == null) {
                    modelo.put("cantidadCarrito", 0);
                } else {
                    modelo.put("cantidadCarrito", TiendaService.getInstancia().getCarritoUsuario(TiendaService.getInstancia().
                            getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"))).getListaProductos().size());
                    modelo.put("usuarioActual", ctx.sessionAttribute("usuario"));
                }

                //enviando al sistema de plantilla.
                ctx.render("/templates/ListaUsuarios.html", modelo);
            }
        });

        app.post("/listaUsuarios/crear", ctx -> {

            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");

            } else if (!ctx.sessionAttribute("usuario").toString().equalsIgnoreCase("admin")) {
                ctx.result("Sin autorizacion");
            } else {
                Usuario nuevoUsuario = new Usuario(
                        ctx.formParam("nombreUsuario"),
                        ctx.formParam("nombreCliente"),
                        ctx.formParam("password")
                );
                if (TiendaService.getInstancia().crearUsuario(nuevoUsuario) == null) {
                    ctx.result("Este usuario ya esta registrado...");
                }
                ;
            }
            ctx.redirect("/listaUsuarios");
        });


        app.post("/listaUsuarios/eliminar", ctx -> {


            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");
            } else if (!ctx.sessionAttribute("usuario").toString().equalsIgnoreCase("admin")) {
                ctx.result("Sin autorizacion");
            } else {


                if (TiendaService.getInstancia().eliminarUsuario(TiendaService.getInstancia().getUsuarioByNombreUsuario(ctx.formParam("eliminarUsuario"))) == null) {
                    ctx.result("Este usuario no se encuentra registrado...");
                }
                ;
            }

            ctx.redirect("/listaUsuarios");

        });


        app.get("/ventas", ctx -> {


            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");
            } else if (!ctx.sessionAttribute("usuario").toString().equalsIgnoreCase("admin")) {
                ctx.result("Sin autorizacion");
            } else {
                Map<String, Object> modelo = new HashMap<>();
                modelo.put("ventas", TiendaService.getInstancia().getVentas());


                modelo.put("cantidadCarrito", TiendaService.getInstancia().getCarritoUsuario(TiendaService.getInstancia().
                        getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"))).getListaProductos().size());
                modelo.put("usuario", ctx.sessionAttribute("usuario"));

                ctx.render("/templates/Ventas.html", modelo);
            }
        });


        app.post("/carrito/agregar/", ctx -> {

            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");
            } else {
                Usuario usuario = TiendaService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));


                Producto producto = new Producto(
                        TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getNombre(),
                        TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getPrecio()
                );
                producto.setId(TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getId());

                producto.setCantidad(
                        Integer.parseInt(ctx.formParam("cantidad"))
                );

                TiendaService.getInstancia().addProductoCarritoUsuario(usuario, producto);
                ctx.redirect("/carrito");
            }
        });

        app.post("/carrito/eliminar/", ctx -> {


            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");
            } else {

                Usuario usuario = TiendaService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));

                Producto producto = new Producto(
                        TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getNombre(),
                        TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getPrecio()
                );
                producto.setId(TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getId());

                TiendaService.getInstancia().deleteProductoCarritoUsuario(usuario, producto);
                ctx.redirect("/carrito");
            }
        });

        app.post("/carrito/limpiar/", ctx -> {


            if (ctx.sessionAttribute("usuario") == null) {
                ctx.result("Usuario no logeado");
            } else {
                Usuario usuario = TiendaService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));
                TiendaService.getInstancia().limpiarCarrito(usuario);
                ctx.redirect("/carrito");
            }
        });

        app.post("/carrito/comprar/", ctx -> {


            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");
            } else {

                Usuario usuario = TiendaService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));
                LocalDate fechaActual = LocalDate.now();
                CarroCompra comprasUsuario = new CarroCompra();

                //Agregando las compras del usuario a la variable comprasUsuario
                for (int i = 0; i < usuario.getCarrito().getListaProductos().size(); i++) {
                    comprasUsuario.getListaProductos().add(
                            usuario.getCarrito().getListaProductos().get(i));
                }

                VentasProductos nuevaVenta = new VentasProductos(
                        ctx.formParam("nombreCliente"),
                        comprasUsuario.getListaProductos(),
                        fechaActual
                );

                TiendaService.getInstancia().realizarVenta(nuevaVenta); //Venta realizada
                TiendaService.getInstancia().limpiarCarrito(usuario); //Se limpia el carrito

                ctx.redirect("/carrito");
            }
        });


        app.get("/carrito", ctx -> {


            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");
            } else {
                Usuario usuario = TiendaService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));
                CarroCompra carrito = TiendaService.getInstancia().getCarritoUsuario(usuario);

                int totalCarrito = 0;
                for (int i = 0; i < carrito.getListaProductos().size(); i++) {
                    totalCarrito += carrito.getListaProductos().get(i).getPrecioTotal();
                }


                Map<String, Object> modelo = new HashMap<>();
                modelo.put("usuario", usuario);
                modelo.put("cantidadCarrito", carrito.getListaProductos().size());
                modelo.put("productosCarrito", carrito.getListaProductos());
                modelo.put("totalCarrito", totalCarrito);

                //enviando al sistema de plantilla.
                ctx.render("/templates/Carrito.html", modelo);
            }

        });

        app.get("/controlProductos", ctx -> {


            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");
            }
            else if (!ctx.sessionAttribute("usuario").toString().equalsIgnoreCase("admin")) {
                ctx.result("Sin autorizacion");
            } else {
                Usuario usuario = TiendaService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));
                CarroCompra carrito = TiendaService.getInstancia().getCarritoUsuario(usuario);
                Map<String, Object> modelo = new HashMap<>();

                modelo.put("usuario", usuario.getUsuario());
                modelo.put("cantidadCarrito", carrito.getListaProductos().size());
                modelo.put("productos", TiendaService.getInstancia().getListaProductos());

                //enviando al sistema de plantilla.
                ctx.render("/templates/CRUD_Productos.html", modelo);
            }

        });

        app.post("/controlProductos/eliminarProducto", ctx -> {

            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");
            } else if (!ctx.sessionAttribute("usuario").toString().equalsIgnoreCase("admin")) {
                ctx.result("Sin autorizacion");
            } else {
                TiendaService.getInstancia().deleteProducto(TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("eliminarProducto"))));
                ctx.redirect("/controlProductos");
            }

        });

        app.get("/agregarProducto", ctx -> {

            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");
            } else if (!ctx.sessionAttribute("usuario").toString().equalsIgnoreCase("admin")) {
                ctx.result("Sin autorizacion");
            } else {
                Usuario usuario = TiendaService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));
                CarroCompra carrito = TiendaService.getInstancia().getCarritoUsuario(usuario);
                Map<String, Object> modelo = new HashMap<>();

                modelo.put("usuario", usuario.getUsuario());
                modelo.put("cantidadCarrito", carrito.getListaProductos().size());
                modelo.put("productos", TiendaService.getInstancia().getListaProductos());
                ctx.render("/templates/Crear_Producto.html", modelo);
            }

        });

        app.post("/agregarProducto/crear", ctx -> {

            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");

            } else if (!ctx.sessionAttribute("usuario").toString().equalsIgnoreCase("admin")) {
                ctx.result("Sin autorizacion");
            } else {
                Producto producto = new Producto(
                        ctx.formParam("nombreProducto"),
                        Double.parseDouble(ctx.formParam("precioProducto"))
                );

                TiendaService.getInstancia().addNuevoProducto(producto);
                ctx.redirect("/controlProductos");
            }

        });


        app.post("/editarProducto", ctx -> {

            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");

            } else if (!ctx.sessionAttribute("usuario").toString().equalsIgnoreCase("admin")) {
                ctx.result("Sin autorizacion");
            } else {

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
            }
        });


        app.post("/editarProducto/:idProducto", ctx -> {

            if(ctx.sessionAttribute("usuario") == null)
            {
                ctx.redirect("/iniciarSesion");

            }else if(!ctx.sessionAttribute("usuario").toString().equalsIgnoreCase("admin"))
            {
                ctx.result("Sin autorizacion");
            }else
            {
                TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.pathParam("idProducto")
                )).setNombre(ctx.formParam("nombreProducto"));

                TiendaService.getInstancia().getProductoById(Long.parseLong(ctx.pathParam("idProducto")
                )).setPrecio(Double.parseDouble(ctx.formParam("precioProducto")));

                ctx.redirect("/controlProductos");
            }


        });

    }

}
