package org.pucmm.jean.Servicios;

import org.pucmm.jean.Modelo.Producto_Comprado;
import org.pucmm.jean.Modelo.VentasProductos;
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
        return gestionDb.findAll();
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