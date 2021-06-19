package org.pucmm.jean.Servicios;

import org.hibernate.Criteria;
import org.hibernate.Session;
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

        entityManager.refresh(producto);

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

        entityManager.refresh(producto);
    }

    public void agregarFoto(long idProducto, Foto foto)
    {
        Producto producto = entityManager.find(Producto.class,idProducto);

        entityManager.getTransaction().begin();
        entityManager.persist(foto);
        entityManager.getTransaction().commit();

        producto.getFotos().add(foto);

        entityManager.getTransaction().begin();
        entityManager.merge(producto);
        entityManager.getTransaction().commit();

        entityManager.refresh(producto);
    }

    public void enviarComentario(Producto producto, Comentario comentario){

        entityManager.getTransaction().begin();
        entityManager.persist(comentario);
        entityManager.getTransaction().commit();

        producto.getComentarios().add(comentario);

        entityManager.getTransaction().begin();
        entityManager.merge(producto);
        entityManager.getTransaction().commit();

        entityManager.refresh(producto);
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

        entityManager.refresh(producto);

    }

    public void eliminarFoto(long idProducto, long idFoto){

        Foto foto = entityManager.find(Foto.class, idFoto);
        Producto producto = entityManager.find(Producto.class,idProducto);
        producto.getFotos().remove(foto);

        entityManager.getTransaction().begin();
        entityManager.remove(foto);
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        entityManager.merge(producto);
        entityManager.getTransaction().commit();

        entityManager.refresh(producto);

    }


    public List<Producto> getListaProductos(int pagina)
    {
        //Implementaremos paginacion
        int paginaSize = 10;

        Query query = entityManager.createQuery("Select p from Producto  p")
                .setFirstResult(calcularOffset(pagina))
                .setMaxResults(paginaSize);

        return query.getResultList();
    }

    public int getTotalPaginas()
    {
        Query query = entityManager.createQuery("Select Count(*) from Producto");

        if((int) Math.ceil(Double.parseDouble(query.getSingleResult().toString()) / 10) == 0)
        {
            return 1;
        }else
        {
            return (int) Math.ceil(Double.parseDouble(query.getSingleResult().toString()) / 10);
        }

    }

    private int calcularOffset(int pagina)
    {
        return ((10*pagina)-10);
    }



}
