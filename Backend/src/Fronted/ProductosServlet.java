package Fronted;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/api/productos")
public class ProductosServlet extends HttpServlet {

    private final ProductoDao dao = new ProductoDao();

    // LISTAR (GET)  y DETALLE (?id=##)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        String idParam = req.getParameter("id");

        if (idParam != null) { // detalle por id
            try {
                int id = Integer.parseInt(idParam);
                Producto p = dao.buscarProductoPorId(id);
                if (p == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().print(new JSONObject().put("error", "No existe").toString());
                    return;
                }
                resp.getWriter().print(productoToJson(p).toString());
            } catch (NumberFormatException ex) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print(new JSONObject().put("error", "id inválido").toString());
            }
            return;
        }

        // listar todos
        ArrayList<Producto> lista = dao.obtenerProductos();
        JSONArray arr = new JSONArray();
        for (Producto p : lista) arr.put(productoToJson(p));
        resp.getWriter().print(arr.toString());
    }

    // CREAR (POST)  Body: { "nombre": "...", "precio": 12345 }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        JSONObject body = readJson(req);

        if (!body.has("nombre") || !body.has("precio")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(new JSONObject().put("error", "Faltan campos").toString());
            return;
        }

        String nombre = body.getString("nombre");
        double precio = body.getDouble("precio");

        dao.insertarProducto(nombre, precio);
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().print(new JSONObject().put("ok", true).toString());
    }

    // ACTUALIZAR (PUT) Body: { "id": 1, "nombre": "...", "precio": 12345 }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        JSONObject body = readJson(req);

        if (!body.has("id") || !body.has("nombre") || !body.has("precio")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(new JSONObject().put("error", "Faltan campos").toString());
            return;
        }

        Producto p = new Producto();
        p.setId(body.getInt("id"));
        p.setNombre(body.getString("nombre"));
        p.setPrecio(body.getDouble("precio"));

        boolean ok = new ProductoDao().actualizarProducto(p);
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().print(new JSONObject().put("ok", ok).toString());
    }

    // ELIMINAR (DELETE)  /api/productos?id=##
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(new JSONObject().put("error", "id requerido").toString());
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            boolean ok = new ProductoDao().eliminarProducto(id);
            resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().print(new JSONObject().put("ok", ok).toString());
        } catch (NumberFormatException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(new JSONObject().put("error", "id inválido").toString());
        }
    }

    // Helpers
    private JSONObject productoToJson(Producto p) {
        return new JSONObject()
                .put("id", p.getId())
                .put("nombre", p.getNombre())
                .put("precio", p.getPrecio());
    }

    private JSONObject readJson(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = req.getReader()) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
        }
        return new JSONObject(sb.toString());
    }
}
