package com.orientapsi.dao;

import com.orientapsi.config.ConexionBD;
import com.orientapsi.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario validarLogin(String correo, String clave) {
        Usuario usuario = null;
        String sql = "SELECT * FROM USUARIO WHERE LOWER(TRIM(correo)) = LOWER(TRIM(?)) AND TRIM(clave_hash) = TRIM(?)";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, clave);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setIdRol(rs.getInt("id_rol"));
                    usuario.setNombres(rs.getString("nombres"));
                    usuario.setApellidos(rs.getString("apellidos"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setEstado(rs.getString("estado"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en UsuarioDAO.validarLogin: " + e.getMessage());
        }
        return usuario;
    }

    public boolean registrarUsuario(Usuario u) {
        String sql = "INSERT INTO USUARIO (id_rol, nombres, apellidos, correo, clave_hash, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, u.getIdRol());
            ps.setString(2, u.getNombres());
            ps.setString(3, u.getApellidos());
            ps.setString(4, u.getCorreo());
            ps.setString(5, u.getClaveHash());
            ps.setString(6, u.getEstado());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }
}