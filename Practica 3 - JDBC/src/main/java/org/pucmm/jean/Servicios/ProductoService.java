package org.pucmm.jean.Servicios;

import org.pucmm.jean.Modelo.Comentario;
import org.pucmm.jean.Modelo.Foto;
import org.pucmm.jean.Modelo.Producto;
import org.pucmm.jean.Modelo.Producto_Comprado;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static org.hibernate.id.PersistentIdentifierGenerator.PK;

public class ProductoService {

    public static ProductoService instancia;
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("MiUnidadPersistencia");
    EntityManager entityManager = emf.createEntityManager();


    public static ProductoService getInstancia(){
        if(instancia==null){
            instancia = new ProductoService();
        }
        return instancia;
    }


    public Producto getProductoById(long id)
    {
        Producto producto = entityManager.find(Producto.class,id);
        entityManager.refresh(producto);
        return producto;
    }

    public void editarProducto(long id, String nombre, Double precio){

        Producto producto = getProductoById(id);
        producto.setNombre(nombre);
        producto.setPrecio(precio);

        entityManager.getTransaction().begin();
        entityManager.merge(producto);
        entityManager.getTransaction().commit();

    }


    public void deleteProducto(Producto producto)
    {
        entityManager.getTransaction().begin();
        entityManager.remove(producto);
        entityManager.getTransaction().commit();
    }

    public void addNuevoProducto(Producto producto, List<Foto> fotos)
    {
        for(int i = 0; i < fotos.size();i++)
        {
            entityManager.getTransaction().begin();
            entityManager.persist(fotos.get(i));
            entityManager.getTransaction().commit();
        }

        producto.setFotos(fotos);

        entityManager.getTransaction().begin();
        entityManager.persist(producto);
        entityManager.getTransaction().commit();

    }

    public void enviarComentario(Producto producto, Comentario comentario){

        //Producto producto = entityManager.find(Producto.class,idProducto);

        entityManager.getTransaction().begin();
        entityManager.persist(comentario);
        entityManager.getTransaction().commit();

        producto.getComentarios().add(comentario);

        entityManager.getTransaction().begin();
        entityManager.merge(producto);
        entityManager.getTransaction().commit();
    }

    public void eliminarComentario(long idProducto, long idComentario){

        Comentario com = entityManager.find(Comentario.class,idComentario);
        Producto producto = entityManager.find(Producto.class,idProducto);
        producto.getComentarios().remove(com);

        entityManager.getTransaction().begin();
        entityManager.remove(com);
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        entityManager.merge(producto);
        entityManager.getTransaction().commit();

    }

    public List<Producto> getListaProductos()
    {
        List<Producto> productos = entityManager.createQuery("select p from Producto p",Producto.class).getResultList();

        return productos;
    }



}
