package com.orientapsi.dao;

import com.orientapsi.config.ConexionBD;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PsicologoDAO {


    // =========================================================
    // REGISTRAR PSICÓLOGO
    // =========================================================
    public String registrarPsicologo(
            String nombres,
            String apellidos,
            String correo,
            String clave,
            String colegiatura,
            int experiencia,
            String presentacion,
            String modalidad
    ) {

        Connection con = null;

        try {

            con = ConexionBD.getConexion();

            if (con == null) {
                return "error";
            }

            con.setAutoCommit(false);


            // =========================================
            // 1. VALIDAR CORREO DUPLICADO
            // =========================================

            String sqlCorreo =
                    "SELECT id_usuario " +
                            "FROM USUARIO " +
                            "WHERE correo = ?";


            try (
                    PreparedStatement ps =
                            con.prepareStatement(sqlCorreo)
            ) {

                ps.setString(
                        1,
                        correo.trim().toLowerCase()
                );


                try (
                        ResultSet rs =
                                ps.executeQuery()
                ) {

                    if (rs.next()) {

                        con.rollback();

                        return "duplicado";
                    }
                }
            }


            // =========================================
            // 2. VALIDAR COLEGIATURA DUPLICADA
            // =========================================

            String sqlColegiatura =
                    "SELECT id_psicologo " +
                            "FROM PSICOLOGO " +
                            "WHERE numero_colegiatura = ?";


            try (
                    PreparedStatement ps =
                            con.prepareStatement(sqlColegiatura)
            ) {

                ps.setString(
                        1,
                        colegiatura.trim()
                );


                try (
                        ResultSet rs =
                                ps.executeQuery()
                ) {

                    if (rs.next()) {

                        con.rollback();

                        return "duplicado";
                    }
                }
            }


            // =========================================
            // 3. CIFRAR CONTRASEÑA
            // =========================================

            String claveHash =
                    BCrypt.hashpw(
                            clave,
                            BCrypt.gensalt(12)
                    );


            // =========================================
            // 4. REGISTRAR USUARIO
            // =========================================

            String sqlUsuario =
                    "INSERT INTO USUARIO " +
                            "(id_rol, nombres, apellidos, correo, clave_hash, estado) " +
                            "VALUES (2, ?, ?, ?, ?, 'ACTIVO')";


            int idUsuario;


            try (
                    PreparedStatement psUsuario =
                            con.prepareStatement(
                                    sqlUsuario,
                                    Statement.RETURN_GENERATED_KEYS
                            )
            ) {

                psUsuario.setString(
                        1,
                        nombres.trim()
                );

                psUsuario.setString(
                        2,
                        apellidos.trim()
                );

                psUsuario.setString(
                        3,
                        correo.trim().toLowerCase()
                );

                psUsuario.setString(
                        4,
                        claveHash
                );


                int filas =
                        psUsuario.executeUpdate();


                if (filas == 0) {

                    con.rollback();

                    return "error";
                }


                try (
                        ResultSet rs =
                                psUsuario.getGeneratedKeys()
                ) {

                    if (!rs.next()) {

                        con.rollback();

                        return "error";
                    }


                    idUsuario =
                            rs.getInt(1);
                }
            }


            // =========================================
            // 5. REGISTRAR PSICÓLOGO
            // =========================================

            String sqlPsicologo =
                    "INSERT INTO PSICOLOGO " +
                            "(id_usuario, numero_colegiatura, presentacion, modalidad, anios_experiencia, estado) " +
                            "VALUES (?, ?, ?, ?, ?, 'ACTIVO')";


            try (
                    PreparedStatement psPsicologo =
                            con.prepareStatement(sqlPsicologo)
            ) {

                psPsicologo.setInt(
                        1,
                        idUsuario
                );

                psPsicologo.setString(
                        2,
                        colegiatura.trim()
                );

                psPsicologo.setString(
                        3,
                        presentacion.trim()
                );

                psPsicologo.setString(
                        4,
                        modalidad
                );

                psPsicologo.setInt(
                        5,
                        experiencia
                );


                int filasPsicologo =
                        psPsicologo.executeUpdate();


                if (filasPsicologo == 0) {

                    con.rollback();

                    return "error";
                }
            }


            // =========================================
            // 6. CONFIRMAR TRANSACCIÓN
            // =========================================

            con.commit();

            return "success";


        } catch (SQLException e) {

            if (con != null) {

                try {

                    con.rollback();

                } catch (SQLException ex) {

                    ex.printStackTrace();
                }
            }


            System.err.println(
                    "Error al registrar psicólogo: "
                            + e.getMessage()
            );

            e.printStackTrace();


            if (e.getErrorCode() == 1062) {

                return "duplicado";
            }


            return "error";


        } finally {

            if (con != null) {

                try {

                    con.setAutoCommit(true);

                    con.close();

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }


    // =========================================================
    // OBTENER ID DEL PSICÓLOGO POR ID DE USUARIO
    // =========================================================
    public int obtenerIdPsicologoPorUsuario(int idUsuario) {

        String sql =
                "SELECT id_psicologo " +
                        "FROM PSICOLOGO " +
                        "WHERE id_usuario = ?";


        try (
                Connection con =
                        ConexionBD.getConexion();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    idUsuario
            );


            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    return rs.getInt(
                            "id_psicologo"
                    );
                }
            }


        } catch (SQLException e) {

            System.err.println(
                    "Error al obtener id_psicologo: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }


        return -1;
    }
}