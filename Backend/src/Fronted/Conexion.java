package Fronted;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL =
        "jdbc:mysql://localhost:3306/la_lonchera?useSSL=false&serverTimezone=America/Bogota&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = ""; // si tienes clave, ponla aquí

    public Connection establecerConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // carga del driver
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver MySQL (com.mysql.cj.jdbc.Driver)", e);
        }
        Connection conn = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
conn.setAutoCommit(true);
return conn;


    }
}


