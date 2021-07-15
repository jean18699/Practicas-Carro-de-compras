package org.pucmm.jean.Controladores;

import io.javalin.Javalin;
import org.eclipse.jetty.websocket.api.Session;
import org.pucmm.jean.Modelo.Comentario;
import org.pucmm.jean.Modelo.Producto;
import org.pucmm.jean.Modelo.Usuario;
import org.pucmm.jean.Servicios.ComentarioService;
import org.pucmm.jean.Servicios.ProductoService;
import org.pucmm.jean.Servicios.UsuarioService;

import java.io.IOException;
import java.net.http.WebSocket;
import java.util.List;

import static j2html.TagCreator.*;

public class WebSocketControlador {

    private Javalin app;

    public WebSocketControlador(Javalin app)
    {
        this.app = app;

    }

    public void aplicarRutas() throws NumberFormatException {

        app.ws("/iniciarSesion",ws->{
            ws.onConnect(ctx->{
                ctx.sessionAttributeMap().put("sesion_",ctx.session);
                TiendaControlador.usuariosConectados.add(ctx.session);
                System.out.println(TiendaControlador.usuariosConectados.size());
            });

            ws.onMessage(ctx -> {

            });

        });

        app.ws("/cerrarSesion",ws->{
            ws.onClose(ctx->{

                for(int i = 0; i < TiendaControlador.usuariosConectados.size();i++)
                {
                    TiendaControlador.usuariosConectados.remove(i);
                }

                System.out.println(TiendaControlador.usuariosConectados.size());
            });
        });

        app.ws("/sesion",ws->{

            ws.onConnect(ctx->{
                TiendaControlador.usuariosConectados.add(ctx.session);
            });

            ws.onClose(ctx -> {
                for(int i = 0; i < TiendaControlador.usuariosConectados.size();i++)
                {
                    if(TiendaControlador.usuariosConectados.get(i).equals(ctx.session))
                    {
                        TiendaControlador.usuariosConectados.remove(i);
                    }
                }

            });

            ws.onMessage(ctx -> {
                enviarCantidadConectados();
            });

        });


        app.ws("/comentarios/:id",ws->{

            ws.onConnect(ctx->{
                TiendaControlador.usuariosVistaProducto.add(ctx.session);
                for(Comentario com : ProductoService.getInstancia().getProductoById(Long.parseLong(ctx.pathParam("id"))).getComentarios())
                {
                    try {
                        ctx.session.getRemote().sendString(
                                tr(
                                        td(ctx.sessionAttribute("usuario").toString()),
                                        td(com.getMensaje()),
                                        td(button("Eliminar").withType("button").withClass("btn btn-danger").withValue(String.valueOf(com.getId())))
                                ).render()
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            ws.onClose(ctx -> {
                TiendaControlador.usuariosVistaProducto.remove(ctx.session);
            });

            ws.onMessage(ctx -> {

                //Si el mensaje que llega es un numero, es que intentamos eliminar un comentario
                if(isNumeric(ctx.message()))
                {
                    ProductoService.getInstancia().eliminarComentario(Long.parseLong(ctx.pathParam("id")),Long.parseLong(ctx.message()));
                    for(Session sesionConectada : TiendaControlador.usuariosVistaProducto) {

                        sesionConectada.getRemote().sendString(ctx.message());
                    }

                }
                else { //De lo contrario intentamos enviar un comentario
                    Usuario usuario = UsuarioService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));
                    Comentario comentario = new Comentario(usuario,ctx.message());
                    ProductoService.getInstancia().enviarComentario(ProductoService.getInstancia().getProductoById(Long.parseLong(ctx.pathParam("id"))),comentario);
                    enviarComentario(usuario.getUsuario(),comentario);
                }
               // System.out.println(ctx.message());
              /*  Usuario usuario = UsuarioService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));
                Comentario comentario = new Comentario(usuario,ctx.message());
                ProductoService.getInstancia().enviarComentario(ProductoService.getInstancia().getProductoById(Long.parseLong(ctx.pathParam("id"))),comentario);
                enviarComentario(usuario.getUsuario(),comentario);*/
            });

        });
    }

    private  static void enviarComentario(String nombreUsuario, Comentario comentario) {
        for(Session sesionConectada : TiendaControlador.usuariosVistaProducto){
            try {
                sesionConectada.getRemote().sendString(
                        tr(
                                td(nombreUsuario),
                                td(comentario.getMensaje()),
                                td(button("Eliminar").withType("button").withClass("btn btn-danger").withValue(String.valueOf(comentario.getId())))
                        ).render());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void enviarCantidadConectados(){
        for(Session sesionConectada : TiendaControlador.usuariosConectados){
            try {
                sesionConectada.getRemote().sendString(String.valueOf(TiendaControlador.usuariosConectados.size()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}