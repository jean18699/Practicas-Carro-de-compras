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

public class WebSocketControlador {

    private Javalin app;
    private Session sesionActual;

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

                    if(TiendaControlador.usuariosConectados.get(i).equals(sesionActual))
                    {
                        TiendaControlador.usuariosConectados.remove(i);
                    }
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
                enviarComentarios(Long.parseLong(ctx.pathParam("id")));
            });

            ws.onClose(ctx -> {

            });

            ws.onMessage(ctx -> {
                System.out.println(ctx.message());
                Usuario usuario = UsuarioService.getInstancia().getUsuarioByNombreUsuario(ctx.sessionAttribute("usuario"));
                Comentario comentario = new Comentario(usuario,ctx.message());
                ProductoService.getInstancia().enviarComentario(ProductoService.getInstancia().getProductoById(Long.parseLong(ctx.pathParam("id"))),comentario);
                enviarComentarios(Long.parseLong(ctx.pathParam("id")));
            });

        });




    }

    private  static void enviarComentarios(long idProducto) {
        for(Session sesionConectada : TiendaControlador.usuariosConectados){
            for(int i = 0 ; i < ProductoService.getInstancia().getProductoById(idProducto).getComentarios().size(); i++)
            {
                try {
                    sesionConectada.getRemote().sendString(ProductoService.getInstancia().getProductoById(idProducto).getComentarios().get(i).getMensaje());
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

}
