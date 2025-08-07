package Fronted;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ProductoDao {
    private Conexion conexion;

    public ProductoDao() {
        this.conexion = new Conexion();
    }

    public void insertarProducto(String nombre, double precio) {
        String sql = "INSERT INTO productos (nombre, precio) VALUES (?, ?)";
        try (Connection conn = conexion.establecerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setDouble(2, precio);
            stmt.executeUpdate();
            System.out.println(" Producto insertado con éxito.");

        } catch (SQLException e) {
            System.out.println(" Error al insertar producto: " + e.getMessage());
        }
    }

    // Método para obtener productos desde la base de datos

    public ArrayList<Producto> obtenerProductos() {
        ArrayList<Producto> lista = new ArrayList<>();
        Connection conn = conexion.establecerConexion();

        if (conn != null) {
            String sql = "SELECT * FROM productos";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    double precio = rs.getDouble("precio");

                    Producto producto = new Producto(id, nombre, precio);
                    lista.add(producto);
                }

            } catch (SQLException e) {
                System.out.println(" Error al obtener productos: " + e.getMessage());
            }
        }

        return lista;
    }
}
