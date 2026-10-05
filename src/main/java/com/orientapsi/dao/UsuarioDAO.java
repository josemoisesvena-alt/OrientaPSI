package com.orientapsi.dao;

import com.orientapsi.config.ConexionBD;
import com.orientapsi.model.Usuario;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {


    // =========================================================
    // VALIDAR LOGIN
    // =========================================================
    public Usuario validarLogin(String correo, String clave) {

        Usuario usuario = null;

        /*
         * Ya NO buscamos la contraseña directamente en SQL.
         *
         * Primero buscamos al usuario por correo,
         * después verificamos la contraseña en Java.
         */
        String sql =
                "SELECT id_usuario, id_rol, nombres, apellidos, " +
                        "correo, clave_hash, estado " +
                        "FROM USUARIO " +
                        "WHERE LOWER(TRIM(correo)) = LOWER(TRIM(?)) " +
                        "AND estado = 'ACTIVO'";


        try (
                Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, correo);


            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    String claveGuardada =
                            rs.getString("clave_hash");


                    boolean claveCorrecta = false;


                    // ==========================================
                    // CONTRASEÑA YA CIFRADA CON BCRYPT
                    // ==========================================

                    if (esHashBCrypt(claveGuardada)) {

                        try {

                            claveCorrecta =
                                    BCrypt.checkpw(
                                            clave,
                                            claveGuardada
                                    );

                        } catch (IllegalArgumentException e) {

                            claveCorrecta = false;
                        }


                    } else {

                        // ======================================
                        // CONTRASEÑA ANTIGUA EN TEXTO PLANO
                        // ======================================

                        /*
                         * Esto es temporal.
                         *
                         * Permite que tus usuarios actuales:
                         *
                         * admin123
                         * psico123
                         * paciente123
                         *
                         * todavía puedan iniciar sesión.
                         */
                        claveCorrecta =
                                clave.equals(claveGuardada);


                        /*
                         * Si la contraseña antigua es correcta,
                         * la convertimos automáticamente a BCrypt.
                         */
                        if (claveCorrecta) {

                            actualizarClaveABCrypt(
                                    rs.getInt("id_usuario"),
                                    clave
                            );
                        }
                    }


                    // ==========================================
                    // CREAR USUARIO SI LA CLAVE ES CORRECTA
                    // ==========================================

                    if (claveCorrecta) {

                        usuario = new Usuario();

                        usuario.setIdUsuario(
                                rs.getInt("id_usuario")
                        );

                        usuario.setIdRol(
                                rs.getInt("id_rol")
                        );

                        usuario.setNombres(
                                rs.getString("nombres")
                        );

                        usuario.setApellidos(
                                rs.getString("apellidos")
                        );

                        usuario.setCorreo(
                                rs.getString("correo")
                        );

                        usuario.setEstado(
                                rs.getString("estado")
                        );
                    }
                }
            }


        } catch (SQLException e) {

            System.err.println(
                    "Error en UsuarioDAO.validarLogin: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }


        return usuario;
    }



    // =========================================================
    // REGISTRAR USUARIO
    // =========================================================
    public boolean registrarUsuario(Usuario u) {

        String sql =
                "INSERT INTO USUARIO " +
                        "(id_rol, nombres, apellidos, correo, clave_hash, estado) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";


        try (
                Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            /*
             * Ciframos la contraseña antes
             * de guardarla en MySQL.
             */
            String claveSegura;

            if (esHashBCrypt(u.getClaveHash())) {

                claveSegura =
                        u.getClaveHash();

            } else {

                claveSegura =
                        BCrypt.hashpw(
                                u.getClaveHash(),
                                BCrypt.gensalt(12)
                        );
            }


            ps.setInt(
                    1,
                    u.getIdRol()
            );

            ps.setString(
                    2,
                    u.getNombres()
            );

            ps.setString(
                    3,
                    u.getApellidos()
            );

            ps.setString(
                    4,
                    u.getCorreo().trim().toLowerCase()
            );

            ps.setString(
                    5,
                    claveSegura
            );

            ps.setString(
                    6,
                    u.getEstado()
            );


            int filasAfectadas =
                    ps.executeUpdate();


            return filasAfectadas > 0;


        } catch (SQLException e) {

            System.err.println(
                    "Error al registrar usuario: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }



    // =========================================================
    // ACTUALIZAR CONTRASEÑA ANTIGUA A BCRYPT
    // =========================================================
    private void actualizarClaveABCrypt(
            int idUsuario,
            String clavePlano) {

        String nuevaClave =
                BCrypt.hashpw(
                        clavePlano,
                        BCrypt.gensalt(12)
                );


        String sql =
                "UPDATE USUARIO " +
                        "SET clave_hash = ? " +
                        "WHERE id_usuario = ?";


        try (
                Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    nuevaClave
            );

            ps.setInt(
                    2,
                    idUsuario
            );


            ps.executeUpdate();


        } catch (SQLException e) {

            System.err.println(
                    "Error al actualizar contraseña a BCrypt: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }



    // =========================================================
    // VERIFICAR SI YA ES HASH BCRYPT
    // =========================================================
    private boolean esHashBCrypt(String clave) {

        if (clave == null) {
            return false;
        }

        return clave.startsWith("$2a$")
                || clave.startsWith("$2b$")
                || clave.startsWith("$2y$");
    }
}