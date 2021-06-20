package org.pucmm.jean.Servicios;

import org.pucmm.jean.Modelo.Producto_Comprado;
import org.pucmm.jean.Modelo.VentasProductos;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;


public class VentaService {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("MiUnidadPersistencia");
    EntityManager em = emf.createEntityManager();

    public static VentaService instancia;

    public VentaService()
    {

    }

    public static VentaService getInstancia(){
        if(instancia==null){
            instancia = new VentaService();
        }
        return instancia;
    }

    public List<VentasProductos> getVentas()
    {
        List<VentasProductos> ventas = em.createQuery("select v from VentasProductos v", VentasProductos.class).getResultList();
        return ventas;
    }

    public void realizarVenta(VentasProductos venta)
    {
       for(int i = 0; i < venta.getListaProductos().size();i++)
        {
           Producto_Comprado producto_comprado = venta.getListaProductos().get(i);
            em.getTransaction().begin();
            em.persist(producto_comprado);
            em.getTransaction().commit();
        }

        em.getTransaction().begin();
        em.persist(venta);
        em.getTransaction().commit();



    }


}
