package org.pucmm.jean.Servicios;
import org.pucmm.jean.Modelo.Foto;
import java.util.List;

public class FotoServices {

    private GestionDb gestionDb;

    private static FotoServices instancia;

    private FotoServices(){
        gestionDb = new GestionDb(Foto.class);

    }

    public static FotoServices getInstancia(){
        if(instancia==null){
            instancia = new FotoServices();
        }
        return instancia;
    }


    public List<Foto> getFotos()
    {
        return gestionDb.findAll();
    }

    public Foto getFotoById(long id)
    {
        Foto foto = (Foto) gestionDb.find(id);
        return foto;
    }

}