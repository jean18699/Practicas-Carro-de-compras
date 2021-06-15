package org.pucmm.jean.Servicios;

import org.pucmm.jean.Modelo.Producto;
import org.pucmm.jean.Modelo.Usuario;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    public static UsuarioService instancia;

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("MiUnidadPersistencia");
    EntityManager em = emf.createEntityManager();

    public UsuarioService()
    {
        //Ingresando el admin
        Usuario admin = em.find(Usuario.class,"admin");
        if(admin == null)
        {
            Usuario userAdmin = new Usuario("admin","Administrador","admin");
            em.getTransaction().begin();
            em.persist(userAdmin);
            em.getTransaction().commit();
        }

    }

    public static UsuarioService getInstancia(){
        if(instancia==null){
            instancia = new UsuarioService();
        }
        return instancia;
    }

    public List<Usuario> getUsuarios() {

        List<Usuario> usuarios = em.createQuery("select u from Usuario u",Usuario.class).getResultList();
        return usuarios;
    }


    public void crearUsuario(Usuario usuario)
    {
       em.getTransaction().begin();
       em.persist(usuario);
       em.getTransaction().commit();
    }

    public void eliminarUsuario(Usuario usuario)
    {
        em.getTransaction().begin();
        em.remove(usuario);
        em.getTransaction().commit();
    }


    public Usuario getUsuarioByNombreUsuario(String usuario)
    {
        Usuario usuario1 = em.find(Usuario.class,usuario);
        return usuario1;
    }




}
