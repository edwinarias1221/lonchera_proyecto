package Fronted;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*; import java.io.*; import org.json.JSONObject;

@WebServlet("/api/auth/*")
public class AuthServlet extends HttpServlet {
  private final UsuarioDao dao = new UsuarioDao();

  private static JSONObject readJson(HttpServletRequest req) throws IOException {
    StringBuilder sb=new StringBuilder(); try(BufferedReader br=req.getReader()){
      String line; while((line=br.readLine())!=null) sb.append(line); }
    return new JSONObject(sb.toString());
  }
  private void write(HttpServletResponse resp,int status,JSONObject json) throws IOException {
    resp.setStatus(status); resp.setContentType("application/json; charset=UTF-8");
    resp.getWriter().write(json.toString());
  }

  @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws IOException {
    String path = req.getPathInfo(); if(path==null) path="";
    try{
      if("/registro".equals(path)){
        JSONObject b=readJson(req); Usuario u=new Usuario();
        u.setNombre(b.getString("nombre")); u.setEmail(b.getString("email"));
        boolean ok=dao.crear(u,b.getString("password")); write(resp, ok?200:400, new JSONObject().put("ok",ok));
      }else if("/login".equals(path)){
        JSONObject b=readJson(req);
        boolean ok=dao.autenticar(b.getString("email"), b.getString("password"));
        write(resp, ok?200:401, new JSONObject().put("autenticado",ok));
      }else{
        write(resp,404,new JSONObject().put("error","Ruta no encontrada"));
      }
    }catch(Exception e){ write(resp,500,new JSONObject().put("error",e.getMessage())); }
  }
}
