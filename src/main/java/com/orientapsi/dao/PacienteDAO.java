package com.orientapsi.dao;

import com.orientapsi.config.ConexionBD;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PacienteDAO {

    public boolean registrarPaciente(
            String nombres,
            String apellidos,
            String correo,
            String clave,
            String fechaNacimiento) {

        Connection con = null;

        try {

            con = ConexionBD.getConexion();

            if (con == null) {
                return false;
            }

            con.setAutoCommit(false);

            // =========================
            // 1. CIFRAR CONTRASEÑA
            // =========================

            String claveHash =
                    BCrypt.hashpw(
                            clave,
                            BCrypt.gensalt(12)
                    );


            // =========================
            // 2. INSERTAR USUARIO
            // =========================

            String sqlUsuario =
                    "INSERT INTO USUARIO " +
                            "(id_rol, nombres, apellidos, correo, clave_hash, estado) " +
                            "VALUES (3, ?, ?, ?, ?, 'ACTIVO')";

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

                    return false;
                }


                try (
                        ResultSet rs =
                                psUsuario.getGeneratedKeys()
                ) {

                    if (!rs.next()) {

                        con.rollback();

                        return false;
                    }

                    idUsuario =
                            rs.getInt(1);
                }
            }


            // =========================
            // 3. INSERTAR PACIENTE
            // =========================

            String sqlPaciente =
                    "INSERT INTO PACIENTE " +
                            "(id_usuario, fecha_nacimiento, estado) " +
                            "VALUES (?, ?, 'ACTIVO')";


            try (
                    PreparedStatement psPaciente =
                            con.prepareStatement(sqlPaciente)
            ) {

                psPaciente.setInt(
                        1,
                        idUsuario
                );

                psPaciente.setString(
                        2,
                        fechaNacimiento
                );


                int filasPaciente =
                        psPaciente.executeUpdate();


                if (filasPaciente == 0) {

                    con.rollback();

                    return false;
                }
            }


            // =========================
            // 4. CONFIRMAR
            // =========================

            con.commit();

            return true;


        } catch (SQLException e) {

            if (con != null) {

                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            System.err.println(
                    "Error al registrar paciente: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;


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
}