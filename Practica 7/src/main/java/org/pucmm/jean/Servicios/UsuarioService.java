package org.pucmm.jean.Servicios;

import org.pucmm.jean.Modelo.Producto;
import org.pucmm.jean.Modelo.Usuario;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    public static UsuarioService instancia;
    private GestionDb gestionDb;

    public UsuarioService()
    {
        gestionDb = new GestionDb(Usuario.class);

        //Ingresando el admin
        Usuario admin = (Usuario) gestionDb.find("admin");
        if(admin == null)
        {
            Usuario userAdmin = new Usuario("admin","Administrador","admin");
            gestionDb.crear(userAdmin);

        }

    }

    public static UsuarioService getInstancia(){
        if(instancia==null){
            instancia = new UsuarioService();
        }
        return instancia;
    }

    public List<Usuario> getUsuarios() {
        List<Usuario> usuarios = gestionDb.findAll();
        return usuarios;
    }


    public void crearUsuario(Usuario usuario)
    {
        gestionDb.crear(usuario);
    }

    public void eliminarUsuario(Usuario usuario)
    {
        gestionDb.eliminar(usuario);
    }


    public Usuario getUsuarioByNombreUsuario(String usuario)
    {
       return (Usuario) gestionDb.find(usuario);
    }

}