package org.pucmm.jean.Servicios;

import org.pucmm.jean.Modelo.Venta;

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

    public List<Venta> getVentas()
    {
        List<Venta> ventas = em.createQuery("select v from Venta v", Venta.class).getResultList();
        return ventas;
    }

    public void realizarVenta(Venta venta)
    {


       // Query query =  em.createQuery("Insert into VentasProductos_Productos()")
        em.getTransaction().begin();
        em.persist(venta);
        em.getTransaction().commit();


       /* Connection con = null;
        try {

            String query = "insert into VENTAS(id_venta,nombre_cliente, fecha_compra,id_producto,usuario,cantidad) values(?,?,?,?,?,?)";

            con = DataBaseServices.getInstancia().getConexion();

            Statement stmt = con.createStatement();

            //Logica para identificar la el id de la venta, el cual servira para determinar que productos pertenecen a esa venta
            ResultSet rs = stmt.executeQuery("select max(ID_VENTA) as ID_VENTA from VENTAS"); //se toma el ultimo ID que haya registrado

            int fila = 0;


            while(rs.next())
            {
                id_venta = rs.getLong("ID_VENTA");
            }

            if(id_venta == 0)
            {
                id_venta = 1;
            }else
            {
                id_venta = id_venta + 1;
            }


            for(Producto producto : TiendaService.getInstancia().getCarrito().getListaProductos())
            {
                PreparedStatement prepareStatement = con.prepareStatement(query);
                //System.out.println(venta.getFechaCompra());
                prepareStatement.setLong(1, id_venta);
                prepareStatement.setString(2, venta.getNombreCliente());
                prepareStatement.setDate(3, venta.getFechaCompra());
                prepareStatement.setLong(4, producto.getId());
                prepareStatement.setString(5, usuario.getUsuario());
                prepareStatement.setInt(6, producto.getCantidad());
                fila = prepareStatement.executeUpdate();

            }

            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(TiendaService.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(TiendaService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
*/
        //this.ventas.add(venta);

    }


}
