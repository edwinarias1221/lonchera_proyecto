package Fronted;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ProductoDao productoDao = new ProductoDao();
        ArrayList<Producto> productos = productoDao.obtenerProductos();

        System.out.println("Lista de productos registrados:");
        for (Producto p : productos) {
            System.out.println("ID: " + p.getId() + " | Nombre: " + p.getNombre() + " | Precio: $" + p.getPrecio());
        }
    }
}

