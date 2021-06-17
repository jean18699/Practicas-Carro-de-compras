package org.pucmm.jean.Controladores;

import org.pucmm.jean.Modelo.*;
import org.pucmm.jean.Servicios.*;
import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.servlet.http.Cookie;
import java.util.*;

public class TiendaControlador {

    private Javalin app;
    String user_carrito = null;

    public TiendaControlador(Javalin app) {
        this.app = app;
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");


    }


    public void aplicarRutas() throws NumberFormatException {

        app.get("/", ctx -> {

            ctx.redirect("/iniciarSesion");

        });

        app.get("/iniciarSesion", ctx -> {


            if(ctx.cookie("usuario_recordado") != null && ctx.cookie("password_recordado") != null)
            {

                ctx.sessionAttribute("usuario", ctx.cookie("usuario_recordado"));

                user_carrito = "carrito_"+ctx.sessionAttribute("usuario");

                //Creando el carrito
                if (ctx.sessionAttribute(user_carrito) == null) {
                    System.out.println(user_carrito);
                    CarroCompra carroCompra = new CarroCompra();
                    ctx.sessionAttribute(user_carrito, carroCompra);
                    TiendaService.getInstancia().setCarroCompra(carroCompra);
                    ctx.redirect("/iniciarSesion");
                }
                else {
                    CarroCompra carroCompra = ctx.sessionAttribute(user_carrito);
                    TiendaService.getInstancia().setCarroCompra(carroCompra);
                    ctx.redirect("/iniciarSesion");
                }
            }

            //En el caso de no tener cookies se tiene que pasar por el auth
            if (ctx.sessionAttribute("usuario") == null) {
                ctx.render("/templates/Login.html");
            } else {
                ctx.redirect("/listaProductos");
            }

        });


        app.post("/autenticar", ctx -> {

          if (UsuarioService.getInstancia().getUsuarioByNombreUsuario(ctx.formParam("nombreUsuario")) != null) {
                if (!UsuarioService.getInstancia().getUsuarioByNombreUsuario(ctx.formParam("nombreUsuario")).getPassword().equals(ctx.formParam("password"))) {
                    ctx.redirect("/iniciarSesion");
                }
                else{
                    ctx.sessionAttribute("usuario", ctx.formParam("nombreUsuario"));
                }
            }


            //Guardamos el usuario en una cookie
            if(ctx.formParam("recordar") != null)
            {
                if(ctx.formParam("recordar").equals("checked"))
                {

                    StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
                    String encryptedPassword = passwordEncryptor.encryptPassword(ctx.formParam("password"));


                    Cookie cookie_usuario = new Cookie("usuario_recordado",ctx.formParam("nombreUsuario"));
                    Cookie cookie_password = new Cookie("password_recordado",encryptedPassword);
                    cookie_usuario.setMaxAge(604800); //una semana
                    cookie_password.setMaxAge(604800);

                    ctx.res.addCookie(cookie_usuario);
                    ctx.res.addCookie(cookie_password);
                }
            }


            user_carrito = "carrito_"+ctx.sessionAttribute("usuario");

            //Creando el carrito
            if (ctx.sessionAttribute(user_carrito) == null) {

                CarroCompra carroCompra = new CarroCompra();
                ctx.sessionAttribute(user_carrito, carroCompra);
                TiendaService.getInstancia().setCarroCompra(carroCompra);
                ctx.redirect("/iniciarSesion");
            }
            else {
                CarroCompra carroCompra = ctx.sessionAttribute(user_carrito);
                TiendaService.getInstancia().setCarroCompra(carroCompra);
                ctx.redirect("/iniciarSesion");
            }
        });



        app.get("/cerrarSesion", ctx ->
        {

            Cookie cookie_usuario = new Cookie("usuario_recordado",ctx.formParam(""));
            Cookie cookie_password = new Cookie("password_recordado",ctx.formParam(""));
            cookie_usuario.setMaxAge(0); //una semana
            cookie_password.setMaxAge(0);
            ctx.res.addCookie(cookie_usuario);
            ctx.res.addCookie(cookie_password);

            if(ctx.req.getSession() != null)
            {
                ctx.sessionAttribute("usuario", null);
            }

            ctx.redirect("/iniciarSesion");
        });


        app.get("/listaProductos", ctx ->
        {
            //Cargando productos para cualquier persona que entre al sistema, con o sin autenticar.
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("productos", ProductoService.getInstancia().getListaProductos());

            if(ctx.cookie("usuario_recordado") != null && ctx.cookie("password_recordado") != null)
            {
                ctx.sessionAttribute("usuario", ctx.cookie("usuario_recordado"));
                user_carrito = "carrito_"+ctx.sessionAttribute("usuario");

                if (ctx.sessionAttribute(user_carrito) == null) {

                    CarroCompra carroCompra = new CarroCompra();
                    ctx.sessionAttribute(user_carrito, carroCompra);
                    TiendaService.getInstancia().setCarroCompra(carroCompra);

                }
                else {
                    CarroCompra carroCompra = ctx.sessionAttribute(user_carrito);
                    TiendaService.getInstancia().setCarroCompra(carroCompra);

                }
            }

            if (ctx.sessionAttribute("usuario") == null) {
                modelo.put("cantidadCarrito", 0);
            } else {

                modelo.put("cantidadCarrito", TiendaService.getInstancia().getCarrito().getListaProductos().size());
                modelo.put("usuario", ctx.sessionAttribute("usuario"));
            }

            //enviando al sistema de plantilla.
            ctx.render("/templates/ListadoProductos.html", modelo);
        });

        app.get("/listaUsuarios", ctx ->

        {

            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");
            } else {

                Map<String, Object> modelo = new HashMap<>();
                modelo.put("usuarios", UsuarioService.getInstancia().getUsuarios());

                if (ctx.sessionAttribute("usuario") == null) {
                    modelo.put("cantidadCarrito", 0);
                } else {
                    modelo.put("cantidadCarrito", TiendaService.getInstancia().getCarrito().getListaProductos().size());
                    modelo.put("usuarioActual", ctx.sessionAttribute("usuario"));
                }

                //enviando al sistema de plantilla.
                ctx.render("/templates/ListaUsuarios.html", modelo);
            }
        });


        app.post("/listaProductos/verProducto/:id",ctx -> {

            Producto p = ProductoService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idVerProducto")));

            Map<String, Object> modelo = new HashMap<>();
            modelo.put("producto", p);

            ctx.render("/templates/Ver_Producto.html",modelo);



        });



        app.post("/listaUsuarios/crear", ctx ->

        {

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

                UsuarioService.getInstancia().crearUsuario(nuevoUsuario);

            }
            ctx.redirect("/listaUsuarios");
        });


        app.post("/listaUsuarios/eliminar", ctx ->
        {

            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");
            } else if (!ctx.sessionAttribute("usuario").toString().equalsIgnoreCase("admin")) {
                ctx.result("Sin autorizacion");
            } else {

                UsuarioService.getInstancia().eliminarUsuario(UsuarioService.getInstancia().getUsuarioByNombreUsuario(ctx.formParam("eliminarUsuario")));
            }

            ctx.redirect("/listaUsuarios");

        });


        app.get("/ventas", ctx ->

        {

            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");
            } else if (!ctx.sessionAttribute("usuario").toString().equalsIgnoreCase("admin")) {
                ctx.result("Sin autorizacion");
            } else {

                Map<String, Object> modelo = new HashMap<>();

                modelo.put("ventas", VentaService.getInstancia().getVentas());


                modelo.put("cantidadCarrito", TiendaService.getInstancia().getCarrito().getListaProductos().size());
                modelo.put("usuario", ctx.sessionAttribute("usuario"));

                ctx.render("/templates/Ventas.html", modelo);
            }
        });


        app.post("/carrito/agregar/", ctx ->

        {

            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");
            } else {

                Producto producto = new Producto(
                        ProductoService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getNombre(),
                        ProductoService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getPrecio()
                );
                producto.setId(ProductoService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getId());

                producto.setCantidad(
                        Integer.parseInt(ctx.formParam("cantidad"))
                );

                TiendaService.getInstancia().addProductoCarrito(producto);
                ctx.sessionAttribute(user_carrito, TiendaService.getInstancia().getCarrito());
                ctx.redirect("/carrito");
            }
        });

        app.post("/carrito/eliminar/", ctx ->

        {


            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");
            } else {

                Producto producto = new Producto(
                        ProductoService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getNombre(),
                        ProductoService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getPrecio()
                );
                producto.setId(ProductoService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getId());
                TiendaService.getInstancia().deleteProductoCarrito(producto);
                ctx.sessionAttribute(user_carrito, TiendaService.getInstancia().getCarrito());
                ctx.redirect("/carrito");
            }
        });

        app.post("/carrito/limpiar/", ctx ->

        {
            if (ctx.sessionAttribute("usuario") == null) {
                ctx.result("Usuario no logeado");
            } else {
                Usuario usuario = UsuarioService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));
                TiendaService.getInstancia().limpiarCarrito();
                ctx.sessionAttribute(user_carrito, TiendaService.getInstancia().getCarrito());
                ctx.redirect("/carrito");
            }
        });

        app.post("/carrito/comprar/", ctx ->

        {

            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");
            } else {

                Usuario usuario = UsuarioService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));
                java.sql.Date fecha = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                List<Producto_Comprado> compras = new ArrayList<>();

                //Voy a tomar las compras del carrito y entrarlas en una clase nueva para las compras, separadas de los productos para la venta
                for(int i = 0; i < TiendaService.getInstancia().getCarrito().getListaProductos().size(); i++)
                {
                    Producto_Comprado producto = new Producto_Comprado(
                            TiendaService.getInstancia().getCarrito().getListaProductos().get(i).getNombre(),
                            TiendaService.getInstancia().getCarrito().getListaProductos().get(i).getPrecio(),
                            TiendaService.getInstancia().getCarrito().getListaProductos().get(i).getCantidad()
                    );

                    compras.add(producto);
                }

                VentasProductos nuevaVenta = new VentasProductos(
                        ctx.formParam("nombreCliente"),
                        compras,
                        fecha
                );

                VentaService.getInstancia().realizarVenta(nuevaVenta); //Venta realizada
                TiendaService.getInstancia().limpiarCarrito(); //Se limpia el carrito
                ctx.sessionAttribute(user_carrito, TiendaService.getInstancia().getCarrito());
                ctx.redirect("/carrito");
            }
        });


        app.get("/carrito", ctx ->

        {


            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");
            } else {
                Usuario usuario = UsuarioService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));
                CarroCompra carrito = TiendaService.getInstancia().getCarrito();

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

        app.get("/controlProductos", ctx ->
        {

            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");
            } else if (!ctx.sessionAttribute("usuario").toString().equalsIgnoreCase("admin")) {
                ctx.result("Sin autorizacion");
            } else {
                Usuario usuario = UsuarioService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));
                CarroCompra carrito = TiendaService.getInstancia().getCarrito();
                TiendaService.getInstancia().getFotos().clear();

                Map<String, Object> modelo = new HashMap<>();

                modelo.put("usuario", usuario.getUsuario());
                modelo.put("cantidadCarrito", carrito.getListaProductos().size());
                modelo.put("productos", ProductoService.getInstancia().getListaProductos());

                //enviando al sistema de plantilla.
                ctx.render("/templates/CRUD_Productos.html", modelo);
            }

        });

        app.post("/controlProductos/eliminarProducto", ctx ->

        {

            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");
            } else if (!ctx.sessionAttribute("usuario").toString().equalsIgnoreCase("admin")) {
                ctx.result("Sin autorizacion");
            } else {
                ProductoService.getInstancia().deleteProducto(ProductoService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("eliminarProducto"))));
                ctx.redirect("/controlProductos");
            }

        });

        app.get("/agregarProducto", ctx ->
        {

            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");
            } else if (!ctx.sessionAttribute("usuario").toString().equalsIgnoreCase("admin")) {
                ctx.result("Sin autorizacion");
            } else {
                Usuario usuario = UsuarioService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));
                CarroCompra carrito = TiendaService.getInstancia().getCarrito();
                List<Foto> fotos = TiendaService.getInstancia().getFotos();


                Map<String, Object> modelo = new HashMap<>();

                modelo.put("fotos", fotos);
                modelo.put("usuario", usuario.getUsuario());
                modelo.put("cantidadCarrito", carrito.getListaProductos().size());
                modelo.put("productos", ProductoService.getInstancia().getListaProductos());
                ctx.render("/templates/Crear_Producto.html", modelo);
            }

        });

        app.post("/agregarProducto/crear", ctx ->
        {

            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");

            } else if (!ctx.sessionAttribute("usuario").toString().equalsIgnoreCase("admin")) {
                ctx.result("Sin autorizacion");
            } else {
                Producto producto = new Producto(
                        ctx.formParam("nombreProducto"),
                        Double.parseDouble(ctx.formParam("precioProducto"))
                );

                ProductoService.getInstancia().addNuevoProducto(producto, TiendaService.getInstancia().getFotos());
                TiendaService.getInstancia().getFotos().clear();
                ctx.redirect("/controlProductos");
            }

        });


        app.post("/editarProducto", ctx ->

        {

            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");

            } else if (!ctx.sessionAttribute("usuario").toString().equalsIgnoreCase("admin")) {
                ctx.result("Sin autorizacion");
            } else {

                Producto producto = new Producto(
                        ProductoService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getNombre(),
                        ProductoService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getPrecio()
                );

                producto.setId(ProductoService.getInstancia().getProductoById(Long.parseLong(ctx.formParam("idProducto"))).getId());
                CarroCompra carrito = TiendaService.getInstancia().getCarrito();

                Map<String, Object> modelo = new HashMap<>();

                modelo.put("cantidadCarrito", carrito.getListaProductos().size());
                modelo.put("producto", producto);
                ctx.render("/templates/Editar_Producto.html", modelo);
            }
        });


        app.post("/editarProducto/:idProducto", ctx ->

        {

            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/iniciarSesion");

            } else if (!ctx.sessionAttribute("usuario").toString().equalsIgnoreCase("admin")) {
                ctx.result("Sin autorizacion");
            } else {
                ProductoService.getInstancia().editarProducto(Long.parseLong(ctx.pathParam("idProducto")),
                        ctx.formParam("nombreProducto"), Double.parseDouble(ctx.formParam("precioProducto")));

                ctx.redirect("/controlProductos");
            }


        });

    }

}