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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.hibernate.id.PersistentIdentifierGenerator.PK;

public class ProductoService {

    public static ProductoService instancia;

    private GestionDb gestionDb = new GestionDb(Producto.class);;

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

    public void addNuevoProducto(Producto producto, Set<Foto> fotos)
    {
        for(Iterator<Foto> it = fotos.iterator(); it.hasNext();)
        {
            gestionDb.crear(it.next());
        }
        producto.setFotos(fotos);
        gestionDb.crear(producto);
    }

    public void agregarFoto(long idProducto, Foto foto)
    {
        Producto producto = (Producto) gestionDb.find(idProducto);
        gestionDb.crear(foto);
        producto.getFotos().add(foto);
        gestionDb.editar(producto);
    }

    public void enviarComentario(Producto producto, Comentario comentario)
    {
        gestionDb.crear(comentario);
        producto.getComentarios().add(comentario);
        gestionDb.editar(producto);
    }

    public void eliminarComentario(long idProducto, long idComentario)
    {
        Comentario com = (Comentario) gestionDb.find(idComentario);
        Producto producto = (Producto) gestionDb.find(idProducto);
        producto.getComentarios().remove(com);
        gestionDb.eliminar(com);
        gestionDb.editar(producto);
    }

    public void eliminarFoto(long idProducto, long idFoto)
    {
        Foto foto = (Foto) gestionDb.find(idFoto);
        Producto producto = (Producto) gestionDb.find(idProducto);
        producto.getFotos().remove(foto);
        gestionDb.eliminar(foto);
        gestionDb.editar(producto);
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
