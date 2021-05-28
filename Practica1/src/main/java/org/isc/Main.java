package org.isc;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;                                                                                     //JEAN CARLOS GEORGE ISC   2017-0167
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;

import java.net.URISyntaxException;

import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {

        Scanner urlScan = new Scanner(System.in);

        String url = null;

        System.out.println("Ingrese una URL valida, de lo contrario el programa terminara: ");
        url = urlScan.nextLine();
        Document doc = Jsoup.connect(url).get();


        //Variable que almacena todos los elementos que empiezan por la etiqueta p de parrafos
        Elements parrafos = doc.getElementsByTag("p");

        //1. Contando la cantidad de lineas del recurso (documento HTML)
        System.out.println("Cantidad de lineas de la pagina: " + doc.html().lines().count());

        //2. Indicar la cantidad de párrafos (p) que contiene el documento HTML
        System.out.println("Cantidad de parrafos de la pagina: " + parrafos.size());


        //3. Indicar la cantidad de imágenes (img) dentro de los párrafos que contiene el archivo HTML.

        int contadorImg = 0;
        for(Element parrafo : parrafos)
        {
            if(parrafo.getElementsByTag("img").toString().contains("img"))
            {
                contadorImg++;
            }
        }
        System.out.println("Cantidad de imagenes dentro de parrafos: " + contadorImg);


        //4. indicar la cantidad de formularios (form) que contiene el HTML por categorizando por el método implementado POST o GET.

        Elements forms = doc.getElementsByTag("form");
        int contFormGet = 0;
        int contFormPost = 0;

        for(Element form : forms) {

            if (form.attributes().get("method").equalsIgnoreCase("get"))
            {
                contFormGet++;
            }
            else if(form.attributes().get("method").equalsIgnoreCase("post"))
            {
                contFormPost++;
            }
        }

        System.out.println("Cantidad de formularios con metodo GET de la pagina: " + contFormGet);
        System.out.println("Cantidad de formularios con metodo POST de la pagina: " + contFormPost);


        //5. Para cada formulario mostrar los campos del tipo input y su respectivo tipo que contiene en el documento HTML.
        System.out.println("Todos los forms y sus sus tipos de inputs implementados: ");

        int contInput = 0;
        for(Element form : forms) {

            System.out.println("\n" + form.tag());
            for (Element child : form.children()) {

                if (child.tag().toString().equalsIgnoreCase("input"))
                {
                    contInput++;
                    System.out.println("Input #" + contInput + " Tipo: " + child.attr("type"));
                }
            }

        }

        System.out.println("\n");


        /*6. Para cada formulario parseado, identificar que el método de envío del formulario sea POST y enviar una petición al servidor con el
        parámetro llamado asignatura y valor practica1 y un header llamado
        matricula con el valor correspondiente a matrícula asignada. Debe
        mostrar la respuesta por la salida estándar.*/

        System.out.println("RESPUESTA OBTENIDA POR EL SERVIDOR AL ENVIAR PETICION A LOS FORMULARIOS POST: \n");

        HttpPost post;
        String formURL;

        //Aqui se agregan los parametros a la peticion POST
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("asignatura", "practica1"));

        for(Element form : forms) {

            //Tomando todos los forms que tengan el metodo post de la pagina
            if(form.attributes().get("method").equalsIgnoreCase("post"))
            {
                formURL = form.absUrl("action");

                post = new HttpPost(formURL);
                post.addHeader("matricula","20170167"); //Headers agregados a la peticion
                post.setEntity(new UrlEncodedFormEntity(params));

                //Imprimiendo la respuesta
                try (CloseableHttpClient cliente = HttpClients.createDefault();
                     CloseableHttpResponse response = cliente.execute(post)) {

                         System.out.println(EntityUtils.toString(response.getEntity()));
                }

                System.out.println("\n");
            }
        }

    }



}
