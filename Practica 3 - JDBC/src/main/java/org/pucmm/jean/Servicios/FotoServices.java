package org.pucmm.jean.Servicios;

import org.pucmm.jean.Modelo.Foto;
import org.pucmm.jean.Modelo.Producto;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class FotoServices {

    EntityManager entityManager;
    private GestionDb gestionDb;

    private static FotoServices instancia;

    private FotoServices(){
        gestionDb = new GestionDb(Foto.class);
        entityManager = gestionDb.getEntityManager();
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
