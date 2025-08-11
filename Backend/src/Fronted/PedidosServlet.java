package Fronted;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import Fronted.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/pedidos")
public class PedidosServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        try {
            JSONObject jsonPedido = new JSONObject(sb.toString());

            String nombre = jsonPedido.getString("nombre");
            String telefono = jsonPedido.getString("telefono");
            String direccion = jsonPedido.getString("direccion");
            String barrio = jsonPedido.getString("barrio");
            String instrucciones = jsonPedido.getString("instrucciones");
            String metodoPago = jsonPedido.getString("metodo_pago");
            String mesa = jsonPedido.optString("mesa", null);

            JSONArray productos = jsonPedido.getJSONArray("productos");

            Connection conn = null;
            PreparedStatement stmtPedido = null;
            PreparedStatement stmtDetalle = null;

            try {
                conn = new Conexion().establecerConexion();
                conn.setAutoCommit(false);

                String sqlPedido = "INSERT INTO pedidos (nombre, telefono, direccion, barrio, instrucciones, metodo_pago, mesa) VALUES (?, ?, ?, ?, ?, ?, ?)";
                stmtPedido = conn.prepareStatement(sqlPedido, PreparedStatement.RETURN_GENERATED_KEYS);
                stmtPedido.setString(1, nombre);
                stmtPedido.setString(2, telefono);
                stmtPedido.setString(3, direccion);
                stmtPedido.setString(4, barrio);
                stmtPedido.setString(5, instrucciones);
                stmtPedido.setString(6, metodoPago);
                stmtPedido.setString(7, mesa);

                int filasInsertadas = stmtPedido.executeUpdate();

                int idPedidoGenerado = -1;
                ResultSet generatedKeys = stmtPedido.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idPedidoGenerado = generatedKeys.getInt(1);
                }

                String sqlDetalle = "INSERT INTO pedido_detalle (id_pedido, id_producto, cantidad) VALUES (?, ?, ?)";
                stmtDetalle = conn.prepareStatement(sqlDetalle);

                for (int i = 0; i < productos.length(); i++) {
                    JSONObject producto = productos.getJSONObject(i);
                    int idProducto = producto.getInt("id");
                    int cantidad = producto.getInt("cantidad");

                    stmtDetalle.setInt(1, idPedidoGenerado);
                    stmtDetalle.setInt(2, idProducto);
                    stmtDetalle.setInt(3, cantidad);
                    stmtDetalle.addBatch();
                }

                stmtDetalle.executeBatch();
                conn.commit();

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("Pedido registrado con éxito");

            } catch (SQLException e) {
                if (conn != null) conn.rollback();
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Error en la base de datos: " + e.getMessage());
            } finally {
                try {
                    if (stmtPedido != null) stmtPedido.close();
                    if (stmtDetalle != null) stmtDetalle.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Error procesando el pedido: " + e.getMessage());
        }
    }
}
