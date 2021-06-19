package org.pucmm.jean.Servicios;

import org.pucmm.jean.Modelo.Foto;
import org.pucmm.jean.Modelo.Producto;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class FotoServices {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("MiUnidadPersistencia");
    EntityManager entityManager = emf.createEntityManager();

    private static FotoServices instancia;

    private FotoServices(){

    }

    public static FotoServices getInstancia(){
        if(instancia==null){
            instancia = new FotoServices();
        }
        return instancia;
    }


    public List<Foto> getFotos()
    {
        return entityManager.createQuery("Select f from Foto f",Foto.class).getResultList();
    }

    public Foto getFotoById(long id)
    {
        Foto foto = entityManager.find(Foto.class,id);
        entityManager.refresh(foto);
        return foto;
    }

}
