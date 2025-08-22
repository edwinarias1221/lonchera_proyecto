
package Fronted;

public class PruebaProductoDao {
    public static void main(String[] args) {
        ProductoDao dao = new ProductoDao();
        System.out.println("Conectando y listando...");
        var lista = dao.obtenerProductos();
        System.out.println("Total productos: " + lista.size());
        for (var p : lista) {
            System.out.println(p.getId() + " - " + p.getNombre() + " - " + p.getPrecio());
        }
    }
}
