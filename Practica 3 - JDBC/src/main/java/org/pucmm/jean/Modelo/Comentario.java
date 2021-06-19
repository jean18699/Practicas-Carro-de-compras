package org.pucmm.jean.Modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Comentario {

    @Id @GeneratedValue
    private long id;
    private String mensaje;

    @ManyToOne
    private Usuario usuario;

    public Comentario(Usuario usuario, String mensaje){
        this.usuario = usuario;
        this.mensaje = mensaje;
    }

    public Comentario(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}