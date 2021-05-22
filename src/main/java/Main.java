import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Document doc = Jsoup.connect("https://developer.mozilla.org/en-US/docs/Learn/HTML/Multimedia_and_embedding/Images_in_HTML").get();

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
    }

}
