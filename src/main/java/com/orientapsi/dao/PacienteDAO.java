package com.orientapsi.dao;

import com.orientapsi.config.ConexionBD;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PacienteDAO {


    // =========================================
    // REGISTRAR PACIENTE
    // =========================================
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


            // =========================================
            // 1. CIFRAR CONTRASEÑA
            // =========================================

            String claveHash =
                    BCrypt.hashpw(
                            clave,
                            BCrypt.gensalt(12)
                    );


            // =========================================
            // 2. INSERTAR USUARIO
            // =========================================

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


            // =========================================
            // 3. INSERTAR PACIENTE
            // =========================================

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


            // =========================================
            // 4. CONFIRMAR TRANSACCIÓN
            // =========================================

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


    // =========================================
    // RESERVAR CITA
    // =========================================
    public String reservarCita(
            int idUsuario,
            int idHorario,
            String motivo) {

        Connection con = null;


        try {

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "DAO - ID USUARIO = " + idUsuario
            );

            System.out.println(
                    "DAO - ID HORARIO = " + idHorario
            );

            System.out.println(
                    "DAO - MOTIVO = " + motivo
            );


            con = ConexionBD.getConexion();


            if (con == null) {

                System.out.println(
                        "ERROR: No existe conexión a la BD."
                );

                return "error";
            }


            con.setAutoCommit(false);


            // =========================================
            // 1. OBTENER PACIENTE
            // =========================================

            int idPaciente = -1;


            String sqlPaciente =
                    "SELECT id_paciente " +
                            "FROM PACIENTE " +
                            "WHERE id_usuario = ? " +
                            "AND estado = 'ACTIVO'";


            try (
                    PreparedStatement psPaciente =
                            con.prepareStatement(sqlPaciente)
            ) {

                psPaciente.setInt(
                        1,
                        idUsuario
                );


                try (
                        ResultSet rs =
                                psPaciente.executeQuery()
                ) {

                    if (rs.next()) {

                        idPaciente =
                                rs.getInt("id_paciente");
                    }
                }
            }


            System.out.println(
                    "DAO - ID PACIENTE = " + idPaciente
            );


            if (idPaciente == -1) {

                con.rollback();

                System.out.println(
                        "ERROR: No se encontró el paciente."
                );

                return "error";
            }


            // =========================================
            // 2. BLOQUEAR Y VERIFICAR HORARIO
            // =========================================
            //
            // FOR UPDATE bloquea la fila mientras
            // termina la transacción.
            // =========================================

            String estadoHorario = null;

            boolean fechaValida = false;


            String sqlVerificarHorario =
                    "SELECT " +
                            "id_horario, " +
                            "estado, " +
                            "fecha, " +
                            "(fecha >= CURDATE()) AS fecha_valida " +
                            "FROM HORARIO " +
                            "WHERE id_horario = ? " +
                            "FOR UPDATE";


            try (
                    PreparedStatement psVerificar =
                            con.prepareStatement(
                                    sqlVerificarHorario
                            )
            ) {

                psVerificar.setInt(
                        1,
                        idHorario
                );


                try (
                        ResultSet rs =
                                psVerificar.executeQuery()
                ) {

                    if (!rs.next()) {

                        con.rollback();

                        System.out.println(
                                "ERROR: El horario no existe."
                        );

                        return "error";
                    }


                    estadoHorario =
                            rs.getString("estado");

                    fechaValida =
                            rs.getBoolean("fecha_valida");


                    System.out.println(
                            "DAO - ESTADO HORARIO = "
                                    + estadoHorario
                    );

                    System.out.println(
                            "DAO - FECHA HORARIO = "
                                    + rs.getDate("fecha")
                    );

                    System.out.println(
                            "DAO - FECHA VÁLIDA = "
                                    + fechaValida
                    );
                }
            }


            // =========================================
            // 3. VERIFICAR DISPONIBILIDAD
            // =========================================

            if (!"DISPONIBLE".equalsIgnoreCase(
                    estadoHorario
            )) {

                con.rollback();

                System.out.println(
                        "HORARIO NO DISPONIBLE."
                );

                return "ocupado";
            }


            if (!fechaValida) {

                con.rollback();

                System.out.println(
                        "HORARIO CON FECHA PASADA."
                );

                return "ocupado";
            }


            // =========================================
            // 4. CAMBIAR HORARIO A OCUPADO
            // =========================================

            String sqlActualizarHorario =
                    "UPDATE HORARIO " +
                            "SET estado = 'OCUPADO' " +
                            "WHERE id_horario = ?";


            try (
                    PreparedStatement psHorario =
                            con.prepareStatement(
                                    sqlActualizarHorario
                            )
            ) {

                psHorario.setInt(
                        1,
                        idHorario
                );


                int filasHorario =
                        psHorario.executeUpdate();


                System.out.println(
                        "DAO - FILAS HORARIO ACTUALIZADAS = "
                                + filasHorario
                );


                if (filasHorario != 1) {

                    con.rollback();

                    System.out.println(
                            "ERROR: No se pudo ocupar el horario."
                    );

                    return "error";
                }
            }


            // =========================================
            // 5. INSERTAR CITA
            // =========================================

            String sqlCita =
                    "INSERT INTO CITA " +
                            "(id_paciente, " +
                            "id_horario, " +
                            "motivo_consulta, " +
                            "estado) " +
                            "VALUES (?, ?, ?, 'PENDIENTE')";


            try (
                    PreparedStatement psCita =
                            con.prepareStatement(sqlCita)
            ) {

                psCita.setInt(
                        1,
                        idPaciente
                );

                psCita.setInt(
                        2,
                        idHorario
                );

                psCita.setString(
                        3,
                        motivo.trim()
                );


                int filasCita =
                        psCita.executeUpdate();


                System.out.println(
                        "DAO - FILAS CITA INSERTADAS = "
                                + filasCita
                );


                if (filasCita != 1) {

                    con.rollback();

                    System.out.println(
                            "ERROR: No se pudo insertar la cita."
                    );

                    return "error";
                }
            }


            // =========================================
            // 6. CONFIRMAR TODO
            // =========================================

            con.commit();


            System.out.println(
                    "RESERVA CONFIRMADA CORRECTAMENTE."
            );

            System.out.println(
                    "========================================"
            );


            return "success";


        } catch (SQLException e) {


            if (con != null) {

                try {

                    con.rollback();

                } catch (SQLException rollbackError) {

                    rollbackError.printStackTrace();
                }
            }


            System.err.println(
                    "========================================"
            );

            System.err.println(
                    "ERROR SQL AL RESERVAR CITA"
            );

            System.err.println(
                    "Código MySQL: "
                            + e.getErrorCode()
            );

            System.err.println(
                    "SQL State: "
                            + e.getSQLState()
            );

            System.err.println(
                    "Mensaje: "
                            + e.getMessage()
            );

            System.err.println(
                    "========================================"
            );


            e.printStackTrace();


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
// REGISTRAR PACIENTE DESDE JSF
// =========================================================
    public String registrarPacienteAdmin(
            String nombres,
            String apellidos,
            String correo,
            String clave,
            java.time.LocalDate fechaNacimiento
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
            // 2. CIFRAR CONTRASEÑA
            // =========================================

            String claveHash =
                    org.mindrot.jbcrypt.BCrypt.hashpw(
                            clave,
                            org.mindrot.jbcrypt.BCrypt.gensalt(12)
                    );


            // =========================================
            // 3. REGISTRAR USUARIO
            // =========================================

            String sqlUsuario =
                    "INSERT INTO USUARIO " +
                            "(id_rol, nombres, apellidos, correo, clave_hash, estado, fecha_registro) " +
                            "VALUES (3, ?, ?, ?, ?, 'ACTIVO', NOW())";


            int idUsuario;


            try (
                    PreparedStatement ps =
                            con.prepareStatement(
                                    sqlUsuario,
                                    Statement.RETURN_GENERATED_KEYS
                            )
            ) {

                ps.setString(
                        1,
                        nombres.trim()
                );

                ps.setString(
                        2,
                        apellidos.trim()
                );

                ps.setString(
                        3,
                        correo.trim().toLowerCase()
                );

                ps.setString(
                        4,
                        claveHash
                );


                int filas =
                        ps.executeUpdate();


                if (filas == 0) {

                    con.rollback();

                    return "error";
                }


                try (
                        ResultSet rs =
                                ps.getGeneratedKeys()
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
            // 4. REGISTRAR PACIENTE
            // =========================================

            String sqlPaciente =
                    "INSERT INTO PACIENTE " +
                            "(id_usuario, fecha_nacimiento, estado) " +
                            "VALUES (?, ?, 'ACTIVO')";


            try (
                    PreparedStatement ps =
                            con.prepareStatement(sqlPaciente)
            ) {

                ps.setInt(
                        1,
                        idUsuario
                );

                ps.setDate(
                        2,
                        java.sql.Date.valueOf(fechaNacimiento)
                );


                int filas =
                        ps.executeUpdate();


                if (filas == 0) {

                    con.rollback();

                    return "error";
                }
            }


            // =========================================
            // 5. CONFIRMAR
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
                    "Error al registrar paciente: "
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
}