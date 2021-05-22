import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Document doc = Jsoup.connect("https://www.formsite.com/templates/industry/").get();

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







    }



}
