package org.pucmm.jean.Servicios;

import org.pucmm.jean.Modelo.Producto_Comprado;
import org.pucmm.jean.Modelo.VentasProductos;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;


public class VentaService {

    GestionDb gestionDb = new GestionDb(VentasProductos.class);

    public static VentaService instancia;

    public VentaService(){}

    public static VentaService getInstancia(){
        if(instancia==null){
            instancia = new VentaService();
        }
        return instancia;
    }

    public List<VentasProductos> getVentas()
    {
        EntityManager em = gestionDb.getEntityManager();
        List<VentasProductos> ventas = em.createQuery("select v from VentasProductos v", VentasProductos.class).getResultList();
        return ventas;
    }

    public void realizarVenta(VentasProductos venta)
    {
       for(int i = 0; i < venta.getListaProductos().size();i++)
        {
           Producto_Comprado producto_comprado = venta.getListaProductos().get(i);
           gestionDb.crear(producto_comprado);
        }

       gestionDb.crear(venta);
    }


}
