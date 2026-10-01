package com.orientapsi.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Asegúrate de revisar la contraseña y el puerto
    private static final String URL = "jdbc:mysql://localhost:3306/orientapsi_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "12345"; // <-- Coloca la clave real de tu MySQL Workbench

    public static Connection getConexion() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("¡Conexión exitosa a la base de datos!");
        } catch (ClassNotFoundException e) {
            System.err.println(">>> ERROR: No se encontró el Driver JDBC");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println(">>> ERROR CONEXIÓN SQL: " + e.getMessage());
            e.printStackTrace();
        }
        return con;
    }
}