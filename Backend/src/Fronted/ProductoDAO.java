package Fronted;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ProductoDao {

    private final Conexion conexion;

    public ProductoDao() {
        this.conexion = new Conexion();
    }

    // INSERT: crea un producto
    public void insertarProducto(String nombre, double precio) {
        String sql = "INSERT INTO productos (nombre, precio) VALUES (?, ?)";
        try (Connection conn = conexion.establecerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setDouble(2, precio);
            stmt.executeUpdate();
            System.out.println("Producto insertado con éxito.");

        } catch (SQLException e) {
            System.out.println("Error al insertar producto: " + e.getMessage());
        }
    }

    // SELECT *: lista todos
    public ArrayList<Producto> obtenerProductos() {
        ArrayList<Producto> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, precio FROM productos";

        try (Connection conn = conexion.establecerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                lista.add(new Producto(id, nombre, precio));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener productos: " + e.getMessage());
        }
        return lista;
    }

    // SELECT by id
    public Producto buscarProductoPorId(int idBuscado) {
        String sql = "SELECT id, nombre, precio FROM productos WHERE id = ?";
        try (Connection conn = conexion.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idBuscado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    double precio = rs.getDouble("precio");
                    return new Producto(id, nombre, precio);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar producto por id: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean actualizarProducto(Producto p) {
        String sql = "UPDATE productos SET nombre = ?, precio = ? WHERE id = ?";
        try (Connection conn = conexion.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setInt(3, p.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean eliminarProducto(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (Connection conn = conexion.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println("[DAO] eliminarProducto id=" + id + " rows=" + rows);
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("[DAO] eliminarProducto error: " + e.getMessage());
            return false;
        }
    }
}
