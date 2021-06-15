package org.pucmm.jean.Servicios;

import org.pucmm.jean.Modelo.Producto;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

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

    public void addNuevoProducto(Producto producto)
    {
        entityManager.getTransaction().begin();
        entityManager.persist(producto);
        entityManager.getTransaction().commit();

    }


    public List<Producto> getListaProductos()
    {
        List<Producto> productos = entityManager.createQuery("select p from Producto p",Producto.class).getResultList();

        return productos;
    }



}
