
package Fronted;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Fronted.security.PasswordUtil;

public class UsuarioDao {
    private final Conexion conexion = new Conexion(); // ya la tienes

    public boolean crear(Usuario u, String plainPassword) throws SQLException {
        String sql = "INSERT INTO usuarios(nombre, email, pass_hash, activo) VALUES(?, ?, ?, 1)";
        String pass = PasswordUtil.hash(plainPassword);
        try (Connection conn = conexion.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, pass);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean autenticar(String email, String plainPassword) throws SQLException {
        String sql = "SELECT pass_hash, activo FROM usuarios WHERE email = ?";
        try (Connection conn = conexion.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return false;
                if (rs.getInt("activo") == 0) return false;
                String stored = rs.getString("pass_hash");
                return PasswordUtil.verify(plainPassword, stored);
            }
        }
    }
}



