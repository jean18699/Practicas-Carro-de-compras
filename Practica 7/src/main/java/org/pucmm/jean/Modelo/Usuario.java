package org.pucmm.jean.Modelo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Usuario implements Serializable {

    @Id
    private String usuario;
    private String nombre;
    private String password;


    public Usuario(String usuario, String nombre, String password) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.password = password;
    }

    public Usuario() {
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}