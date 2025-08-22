package Fronted;
import java.sql.*;
public class UsuarioDao {
  private final Conexion conexion = new Conexion();

  public boolean crear(Usuario u, String passwordPlano) throws SQLException {
    String sql="INSERT INTO usuarios (nombre,email,password_hash) VALUES (?,?,SHA2(?,256))";
    try(Connection conn=conexion.establecerConexion();
        PreparedStatement ps=conn.prepareStatement(sql)){
      ps.setString(1,u.getNombre()); ps.setString(2,u.getEmail()); ps.setString(3,passwordPlano);
      return ps.executeUpdate()>0;
    }
  }

  public boolean autenticar(String email, String passwordPlano) throws SQLException {
    String sql="SELECT id FROM usuarios WHERE email=? AND password_hash=SHA2(?,256)";
    try(Connection conn=conexion.establecerConexion();
        PreparedStatement ps=conn.prepareStatement(sql)){
      ps.setString(1,email); ps.setString(2,passwordPlano);
      try(ResultSet rs=ps.executeQuery()){ return rs.next(); }
    }
  }
}
