package Servicios;

import Modelo.CarroCompra;
import Modelo.Producto;
import Modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioService {

    public static UsuarioService instancia;

    List<Usuario> usuarios;

    public UsuarioService()
    {
        usuarios = new ArrayList<>();
    }

    public static UsuarioService getInstancia(){
        if(instancia==null){
            instancia = new UsuarioService();
        }
        return instancia;
    }

    public List<Usuario> getUsuarios() {

        Connection con = null;
        List<Usuario> usuarios = null;
        
        try
        {
            usuarios = new ArrayList<>();
            String query = "select usuario,nombre,password from USUARIOS";
            con = DataBaseServices.getInstancia().getConexion();
            PreparedStatement prepareStatement = con.prepareStatement(query);
            ResultSet rs = prepareStatement.executeQuery();

            while(rs.next()){
                Usuario usuario = new Usuario();

                usuario.setNombre(rs.getString("nombre"));
                usuario.setUsuario(rs.getString("usuario"));
                usuario.setPassword(rs.getString("password"));
                usuarios.add(usuario);
            }

            this.usuarios = usuarios;

        }catch (SQLException ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public boolean crearUsuario(Usuario usuario)
    {
        Connection con = null;
        boolean ok = false;

        try {

            String query = "insert into USUARIOS(USUARIO, NOMBRE, PASSWORD) values(?,?,?)";

            con = DataBaseServices.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);


            prepareStatement.setString(1, usuario.getUsuario());
            prepareStatement.setString(2, usuario.getNombre());
            prepareStatement.setString(3, usuario.getPassword());

            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);

        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public boolean eliminarUsuario(Usuario usuario)
    {
        boolean ok =false;
        Connection con = null;

        try {

            String query = "delete from USUARIOS where usuario = ?";
            con = DataBaseServices.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);

            prepareStatement.setString(1, usuario.getUsuario());

            int fila = prepareStatement.executeUpdate();
            ok = fila > 0;

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    private boolean existeUsuario(String usuario)
    {
        boolean ok =false;
        Connection con = null;

        try {

            String query = "select from USUARIOS where usuario = ?";
            con = DataBaseServices.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);

            prepareStatement.setString(1, usuario);

            ResultSet fila = prepareStatement.executeQuery();
            ok = (fila != null) ;

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;

    }

    public Usuario getUsuarioByNombreUsuario(String usuario)
    {
        Usuario usuario1 = null;
        Connection con = null;

        try {

            String query = "select * from USUARIOS where usuario = ?";
            con = DataBaseServices.getInstancia().getConexion();
            PreparedStatement prepareStatement = con.prepareStatement(query);
            prepareStatement.setString(1,usuario);

            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                usuario1 = new Usuario();
                usuario1.setUsuario(rs.getString("usuario"));
                usuario1.setNombre(rs.getString("nombre"));
                usuario1.setPassword(rs.getString("password"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return usuario1;

    }




}