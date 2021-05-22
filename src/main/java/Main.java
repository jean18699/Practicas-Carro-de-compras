import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {


        Document doc = Jsoup.connect("http://help.websiteos.com/websiteos/example_of_a_simple_html_page.htm").get();

        //Contando la cantidad de lineas del recurso (documento HTML)
        System.out.println("Cantidad de lineas de la pagina: " + doc.html().lines().count());
      

    }

}
