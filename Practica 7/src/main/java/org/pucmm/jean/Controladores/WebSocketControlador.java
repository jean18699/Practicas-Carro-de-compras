package org.pucmm.jean.Controladores;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.Javalin;
import org.eclipse.jetty.websocket.api.Session;
import org.pucmm.jean.Modelo.Comentario;
import org.pucmm.jean.Modelo.Producto;
import org.pucmm.jean.Modelo.Usuario;
import org.pucmm.jean.Servicios.ComentarioService;
import org.pucmm.jean.Servicios.ProductoService;
import org.pucmm.jean.Servicios.UsuarioService;
import org.pucmm.jean.Servicios.VentaService;

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

        app.ws("/sesion",ws->{

            ws.onConnect(ctx->{
                if(ctx.sessionAttribute("usuario")!= null)
                {
                    TiendaControlador.usuariosConectados.add(ctx.session);
                    enviarCantidadConectados();
                }
            });

            ws.onClose(ctx -> {
                if(ctx.sessionAttribute("usuario")!= null)
                {
                    TiendaControlador.usuariosConectados.remove(ctx.session);
                    enviarCantidadConectados();
                }

            });

            ws.onMessage(ctx -> {
                enviarCantidadConectados();
            });

        });

        app.ws("/ventas",ws->{
            ws.onConnect(ctx -> {
                ctx.session.getRemote().sendString(String.valueOf(VentaService.getInstancia().getVentas().size()));
            });

            ws.onClose(ctx -> {

            });

            ws.onMessage(ctx -> {
            });
        });

        app.ws("/cantidadVendida",ws->{
            ws.onConnect(ctx -> {
                ObjectMapper mapper = new ObjectMapper();
                ArrayNode arrayNode = mapper.createArrayNode();

                for(int i = 0; i < VentaService.getInstancia().getVentas().size();i++)
                {
                    ObjectNode venta = mapper.createObjectNode();
                    venta.put("id",VentaService.getInstancia().getVentas().get(i).getId());
                    venta.put("cantidadVendida",VentaService.getInstancia().getVentas().get(i).getCantidadVendida());
                    arrayNode.add(venta);
                }
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);

                 ctx.session.getRemote().sendString(json);
            });

            ws.onClose(ctx -> {

            });

            ws.onMessage(ctx -> {
            });
        });

        app.ws("/comentarios/:id",ws->{

            ws.onConnect(ctx->{

                TiendaControlador.usuariosVistaProducto.add(ctx.session);
                for(Comentario com : ProductoService.getInstancia().getProductoById(Long.parseLong(ctx.pathParam("id"))).getComentarios())
                {
                    try {
                        if(!ctx.sessionAttribute("usuario").equals("admin") && !ctx.sessionAttribute("usuario").equals(com.getUsuario().getUsuario()))
                        {
                            ctx.session.getRemote().sendString(
                                    tr(
                                            td(com.getUsuario().getUsuario()),
                                            td(com.getMensaje()),
                                            td(button("Eliminar").withType("button").withClass("btn btn-danger").withValue(String.valueOf(com.getId())).attr("hidden"))
                                    ).render());
                        }else if(ctx.sessionAttribute("usuario").equals("admin") || ctx.sessionAttribute("usuario").equals(com.getUsuario().getUsuario()))
                        {
                            ctx.session.getRemote().sendString(
                                    tr(
                                            td(com.getUsuario().getUsuario()),
                                            td(com.getMensaje()),
                                            td(button("Eliminar").withType("button").withClass("btn btn-danger").withValue(String.valueOf(com.getId())))
                                    ).render());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            ws.onClose(ctx -> {
                TiendaControlador.usuariosVistaProducto.remove(ctx.session);
            });

            //Enviar o eliminar mensaje
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
                    enviarComentario(comentario,ctx.sessionAttribute("usuario"));
                }
            });

        });
    }

    private  static void enviarComentario(Comentario comentario, String sesion) {
        for(Session sesionConectada : TiendaControlador.usuariosVistaProducto){
            try {
                if(!sesion.equals("admin") && !sesion.equals(comentario.getUsuario().getUsuario()))
                {
                    sesionConectada.getRemote().sendString(
                            tr(
                                    td(comentario.getUsuario().getUsuario()),
                                    td(comentario.getMensaje()),
                                    td(button("Eliminar").withType("button").withClass("btn btn-danger").withValue(String.valueOf(comentario.getId())).attr("hidden"))
                            ).render());
                }else
                {
                    sesionConectada.getRemote().sendString(
                            tr(
                                    td(comentario.getUsuario().getUsuario()),
                                    td(comentario.getMensaje()),
                                    td(button("Eliminar").withType("button").withClass("btn btn-danger").withValue(String.valueOf(comentario.getId())))
                            ).render());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void enviarCantidadConectados() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();
        for(int i = 0; i < TiendaControlador.nombreUsuariosConectados.size();i++)
        {
            ObjectNode usuario = mapper.createObjectNode();
            usuario.put("usuario",TiendaControlador.nombreUsuariosConectados.get(i));
            arrayNode.add(usuario);
        }
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);

        for(Session sesionConectada : TiendaControlador.usuariosConectados){
            try {
                if(sesionConectada.isOpen())
                {
                    sesionConectada.getRemote().sendString(String.valueOf(json));
                }

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