package Fronted;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import Fronted.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/pedidos")
public class PedidosServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String telefono = request.getParameter("telefono");
        String direccion = request.getParameter("direccion");
        String barrio = request.getParameter("barrio");
        String instrucciones = request.getParameter("instrucciones");
        String metodoPago = request.getParameter("metodo_pago");
        String mesa = request.getParameter("mesa");

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = new Conexion().establecerConexion(); // <- Esta es la línea correcta

            String sql = "INSERT INTO pedidos (nombre, telefono, direccion, barrio, instrucciones, metodo_pago, mesa) VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nombre);
            stmt.setString(2, telefono);
            stmt.setString(3, direccion);
            stmt.setString(4, barrio);
            stmt.setString(5, instrucciones);
            stmt.setString(6, metodoPago);
            stmt.setString(7, mesa);

            int filasInsertadas = stmt.executeUpdate();
            // Obtener el ID del pedido insertado
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            int idPedidoGenerado = -1;
            if (generatedKeys.next()) {
                idPedidoGenerado = generatedKeys.getInt(1);
            }

            if (filasInsertadas > 0) {
                response.getWriter().println("¡Pedido recibido correctamente!");
            } else {
                response.getWriter().println("Error al registrar el pedido.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error en la base de datos: " + e.getMessage());
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
