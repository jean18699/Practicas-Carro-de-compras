package org.pucmm.jean.Controladores;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import org.h2.util.IOUtils;
import org.pucmm.jean.Modelo.Foto;
import org.pucmm.jean.Servicios.FotoServices;
import org.pucmm.jean.Servicios.TiendaService;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;


public class ImagenControlador {

    private Javalin app;

    public ImagenControlador(Javalin app) {
        this.app = app;
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
    }

    public void aplicarRutas() {

        app.routes(()->{
            path("/fotos",()->{

                post("/procesarFoto", ctx -> {

                    ctx.uploadedFiles("foto").forEach(uploadedFile -> {
                        try{

                            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                            int nRead;
                            byte[] data = new byte[16384];

                            while ((nRead = uploadedFile.getContent().read(data, 0, data.length)) != -1) {
                                buffer.write(data, 0, nRead);
                            }

                            byte[] bytes = buffer.toByteArray();

                            String encodedString = Base64.getEncoder().encodeToString(bytes);
                            Foto foto = new Foto(uploadedFile.getFilename(),uploadedFile.getContentType(),encodedString);
                            TiendaService.getInstancia().addFoto(foto);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    ctx.redirect("/agregarProducto/");
                });


            });

        });




    }




}
