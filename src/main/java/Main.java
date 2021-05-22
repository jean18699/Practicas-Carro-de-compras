import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {



        Document doc = Jsoup.connect("http://help.websiteos.com/websiteos/example_of_a_simple_html_page.htm").get();

        //Variable que almacena todos los elementos que empiezan por la etiqueta p de parrafos
        Elements parrafos = doc.getElementsByTag("p");

        //Contando la cantidad de lineas del recurso (documento HTML)
        System.out.println("Cantidad de lineas de la pagina: " + doc.html().lines().count());

        //Indicar la cantidad de párrafos (p) que contiene el documento HTML
        System.out.println("Cantidad de parrafos de la pagina: " + parrafos.size());


    }

}
