package Modelo;

public class Usuario {

    private String usuario;
    private String nombre;
    private String password;
    private CarroCompra carrito;

    public Usuario(String usuario, String nombre, String password) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.password = password;
        this.carrito = new CarroCompra();

    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CarroCompra getCarrito() {
        return carrito;
    }

    public void setCarrito(CarroCompra carrito) {
        this.carrito = carrito;
    }

    public boolean existeProductoCarrito(long idProducto)
    {
        for(int i = 0; i < carrito.getListaProductos().size(); i++)
        {
            if(carrito.getListaProductos().get(i).getId() == idProducto)
            {
                return true;
            }
        }
        return false;
    }



}
