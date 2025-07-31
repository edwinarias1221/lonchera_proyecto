
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/procesarPedido")
public class PedidosServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Recoger los datos del formulario
        String nombre = request.getParameter("nombre");
        String telefono = request.getParameter("telefono");
        String tipoPedido = request.getParameter("tipo_pedido");
        String mesa = request.getParameter("mesa");
        String direccion = request.getParameter("direccion");
        String observaciones = request.getParameter("observaciones");

        // Conectar a la base de datos
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = new Conexion().getConexion();
            
            String sql = "INSERT INTO pedidos (nombre, telefono, tipo_pedido, mesa, direccion, observaciones) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, telefono);
            stmt.setString(3, tipoPedido);
            stmt.setString(4, mesa);
            stmt.setString(5, direccion);
            stmt.setString(6, observaciones);
            
            stmt.executeUpdate();

            // Redirigir a una página de confirmación o volver al menú
            response.sendRedirect("confirmacion.html");

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error al guardar el pedido");
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
