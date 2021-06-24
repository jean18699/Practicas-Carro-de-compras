package org.pucmm.jean.Servicios;

import org.pucmm.jean.Modelo.Comentario;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ComentarioService {

    public static ComentarioService instancia;
    GestionDb gestionDb = new GestionDb(Comentario.class);


    public static ComentarioService getInstancia(){
        if(instancia==null){
            instancia = new ComentarioService();
        }
        return instancia;
    }

    public ComentarioService(){}

    public Comentario getComentarioById(long id)
    {
        Comentario com = (Comentario) gestionDb.find(id);
        return com;
    }

}
