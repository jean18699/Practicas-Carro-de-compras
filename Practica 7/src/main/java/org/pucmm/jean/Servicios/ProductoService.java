package org.pucmm.jean.Servicios;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.annotations.QueryHints;
import org.pucmm.jean.Main;
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

    private GestionDb gestionDb = new GestionDb(Producto.class);

    public static ProductoService getInstancia(){
        if(instancia==null){
            instancia = new ProductoService();
        }
        return instancia;
    }


    public Producto getProductoById(long id)
    {
        Producto producto = (Producto) gestionDb.find(id);
        return producto;
    }

    public void editarProducto(long id, String nombre, Double precio, String descripcion){

        Producto producto = getProductoById(id);
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setDescripcion(descripcion);
        gestionDb.editar(producto);
    }


    public void deleteProducto(Producto producto)
    {
        gestionDb.eliminar(producto);
    }

    public void addNuevoProducto(Producto producto)
    {

        EntityManager em = gestionDb.getEntityManager();

        try {
            for (int i = 0; i < TiendaService.getInstancia().getFotos().size(); i++) {
                em.getTransaction().begin();
                em.persist(TiendaService.getInstancia().getFotos().get(i));
                em.getTransaction().commit();
            }
        }
        finally {
            producto.setFotos(TiendaService.getInstancia().getFotos());
            gestionDb.crear(producto);
            em.close();
        }
    }

    public void agregarFoto(long idProducto, Foto foto)
    {
        EntityManager em = gestionDb.getEntityManager();

        try{
            Producto producto = (Producto) gestionDb.find(idProducto);
            em.getTransaction().begin();
            em.persist(foto);
            em.getTransaction().commit();
            producto.getFotos().add(foto);
            gestionDb.editar(producto);
        }finally {
            em.close();
        }


    }

    public void enviarComentario(Producto producto, Comentario comentario)
    {
        EntityManager em = gestionDb.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(comentario);
            em.getTransaction().commit();
            producto.getComentarios().add(comentario);
            gestionDb.editar(producto);
        }finally {
            em.close();
        }
    }

    public void eliminarComentario(long idProducto, long idComentario)
    {
        EntityManager em = gestionDb.getEntityManager();
        Comentario com = em.find(Comentario.class, idComentario);
        Producto producto = em.find(Producto.class, idProducto);
        try {

            producto.getComentarios().remove(com);
            gestionDb.editar(producto);
            em.getTransaction().begin();
            em.remove(com);
            em.getTransaction().commit();

        }finally {
            em.close();
        }
    }

    public void eliminarFoto(long idProducto, long idFoto)
    {
        EntityManager em = gestionDb.getEntityManager();
        Foto foto = em.find(Foto.class, idFoto);
        Producto producto = em.find(Producto.class, idProducto);
        try
        {
            producto.getFotos().remove(foto);
            gestionDb.editar(producto);

            em.getTransaction().begin();
            em.remove(foto);
            em.getTransaction().commit();

        }finally
        {
            em.close();
        }
    }


    public List<Producto> getListaProductos(int pagina)
    {
        EntityManager entityManager = gestionDb.getEntityManager();

        try{
            //Implementaremos paginacion
            int paginaSize = 10;

            Query query = entityManager.createQuery("Select distinct p from Producto p")
                    .setFirstResult(calcularOffset(pagina))
                    .setMaxResults(paginaSize);


            return query.getResultList();
        }finally
        {
            entityManager.close();
        }

    }

    public int getTotalPaginas()
    {
        EntityManager entityManager = gestionDb.getEntityManager();

        try{
            Query query =  entityManager.createQuery("Select Count(*) from Producto");
            if((int) Math.ceil(Double.parseDouble(query.getSingleResult().toString()) / 10) == 0)
            {
                return 1;
            }else
            {
                return (int) Math.ceil(Double.parseDouble(query.getSingleResult().toString()) / 10);
            }
        }finally {
            entityManager.close();
        }

    }

    private int calcularOffset(int pagina)
    {
        return ((10*pagina)-10);
    }


}