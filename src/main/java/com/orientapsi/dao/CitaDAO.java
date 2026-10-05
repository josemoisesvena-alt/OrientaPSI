package com.orientapsi.dao;

import com.orientapsi.config.ConexionBD;
import com.orientapsi.model.Cita;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class CitaDAO {


    // =========================================================
    // OBTENER CITAS PENDIENTES
    // =========================================================
    public List<Cita> obtenerCitasPendientes() {

        List<Cita> lista = new ArrayList<>();

        String sql =
                "SELECT " +
                        "c.id_cita, " +
                        "c.id_paciente, " +
                        "c.motivo_consulta, " +
                        "c.estado, " +
                        "CONCAT(h.fecha, ' ', h.hora_inicio) AS fecha_hora, " +

                        "uPsi.nombres AS nombre_psicologo, " +
                        "uPsi.apellidos AS apellido_psicologo, " +

                        "uPac.nombres AS nombre_paciente, " +
                        "uPac.apellidos AS apellido_paciente " +

                        "FROM CITA c " +

                        "INNER JOIN HORARIO h " +
                        "ON c.id_horario = h.id_horario " +

                        "INNER JOIN PSICOLOGO p " +
                        "ON h.id_psicologo = p.id_psicologo " +

                        "INNER JOIN USUARIO uPsi " +
                        "ON p.id_usuario = uPsi.id_usuario " +

                        "INNER JOIN PACIENTE pa " +
                        "ON c.id_paciente = pa.id_paciente " +

                        "INNER JOIN USUARIO uPac " +
                        "ON pa.id_usuario = uPac.id_usuario " +

                        "WHERE c.estado = 'PENDIENTE' " +

                        "ORDER BY h.fecha ASC, h.hora_inicio ASC";


        try (
                Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Cita c = new Cita();

                c.setIdCita(
                        rs.getInt("id_cita")
                );

                c.setIdPaciente(
                        rs.getInt("id_paciente")
                );

                c.setMotivo(
                        rs.getString("motivo_consulta")
                );

                c.setEstado(
                        rs.getString("estado")
                );

                c.setFechaHora(
                        rs.getTimestamp("fecha_hora")
                );

                // Psicólogo
                c.setNombrePsicologo(
                        rs.getString("nombre_psicologo")
                );

                c.setApellidoPsicologo(
                        rs.getString("apellido_psicologo")
                );

                // Paciente
                c.setNombrePaciente(
                        rs.getString("nombre_paciente")
                );

                c.setApellidoPaciente(
                        rs.getString("apellido_paciente")
                );

                lista.add(c);
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar citas pendientes: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        return lista;
    }



    // =========================================================
    // CONFIRMAR CITA
    // =========================================================
    public boolean confirmarCita(int idCita) {

        String sql =
                "UPDATE CITA " +
                        "SET estado = 'CONFIRMADA' " +
                        "WHERE id_cita = ? " +
                        "AND estado = 'PENDIENTE'";


        try (
                Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, idCita);

            int filas =
                    ps.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al confirmar cita: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }



    // =========================================================
    // CANCELAR CITA Y LIBERAR HORARIO
    // =========================================================
    public boolean cancelarCita(int idCita) {

        Connection con = null;

        try {

            con = ConexionBD.getConexion();

            if (con == null) {
                return false;
            }

            con.setAutoCommit(false);


            // =========================================
            // 1. OBTENER ID DEL HORARIO
            // =========================================

            int idHorario = -1;

            String sqlBuscar =
                    "SELECT id_horario " +
                            "FROM CITA " +
                            "WHERE id_cita = ? " +
                            "AND estado IN ('PENDIENTE', 'CONFIRMADA')";


            try (
                    PreparedStatement psBuscar =
                            con.prepareStatement(sqlBuscar)
            ) {

                psBuscar.setInt(1, idCita);

                try (
                        ResultSet rs =
                                psBuscar.executeQuery()
                ) {

                    if (rs.next()) {

                        idHorario =
                                rs.getInt("id_horario");
                    }
                }
            }


            if (idHorario == -1) {

                con.rollback();

                return false;
            }


            // =========================================
            // 2. CANCELAR CITA
            // =========================================

            String sqlCancelar =
                    "UPDATE CITA " +
                            "SET estado = 'CANCELADA' " +
                            "WHERE id_cita = ? " +
                            "AND estado IN ('PENDIENTE', 'CONFIRMADA')";


            try (
                    PreparedStatement psCancelar =
                            con.prepareStatement(sqlCancelar)
            ) {

                psCancelar.setInt(1, idCita);

                int filas =
                        psCancelar.executeUpdate();


                if (filas == 0) {

                    con.rollback();

                    return false;
                }
            }


            // =========================================
            // 3. LIBERAR HORARIO
            // =========================================

            String sqlHorario =
                    "UPDATE HORARIO " +
                            "SET estado = 'DISPONIBLE' " +
                            "WHERE id_horario = ?";


            try (
                    PreparedStatement psHorario =
                            con.prepareStatement(sqlHorario)
            ) {

                psHorario.setInt(
                        1,
                        idHorario
                );

                int filasHorario =
                        psHorario.executeUpdate();


                if (filasHorario == 0) {

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
                    "Error al cancelar cita: "
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



    // =========================================================
    // OBTENER CITA POR ID
    // =========================================================
    public Cita obtenerCitaPorId(int idCita) {

        String sql =
                "SELECT " +
                        "c.id_cita, " +
                        "c.id_paciente, " +
                        "c.motivo_consulta, " +
                        "c.estado, " +
                        "CONCAT(h.fecha, ' ', h.hora_inicio) AS fecha_hora, " +

                        "uPsi.nombres AS nombre_psicologo, " +
                        "uPsi.apellidos AS apellido_psicologo, " +

                        "uPac.nombres AS nombre_paciente, " +
                        "uPac.apellidos AS apellido_paciente " +

                        "FROM CITA c " +

                        "INNER JOIN HORARIO h " +
                        "ON c.id_horario = h.id_horario " +

                        "INNER JOIN PSICOLOGO p " +
                        "ON h.id_psicologo = p.id_psicologo " +

                        "INNER JOIN USUARIO uPsi " +
                        "ON p.id_usuario = uPsi.id_usuario " +

                        "INNER JOIN PACIENTE pa " +
                        "ON c.id_paciente = pa.id_paciente " +

                        "INNER JOIN USUARIO uPac " +
                        "ON pa.id_usuario = uPac.id_usuario " +

                        "WHERE c.id_cita = ?";


        try (
                Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, idCita);


            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    Cita c = new Cita();

                    c.setIdCita(
                            rs.getInt("id_cita")
                    );

                    c.setIdPaciente(
                            rs.getInt("id_paciente")
                    );

                    c.setMotivo(
                            rs.getString("motivo_consulta")
                    );

                    c.setEstado(
                            rs.getString("estado")
                    );

                    c.setFechaHora(
                            rs.getTimestamp("fecha_hora")
                    );

                    // Psicólogo
                    c.setNombrePsicologo(
                            rs.getString("nombre_psicologo")
                    );

                    c.setApellidoPsicologo(
                            rs.getString("apellido_psicologo")
                    );

                    // Paciente
                    c.setNombrePaciente(
                            rs.getString("nombre_paciente")
                    );

                    c.setApellidoPaciente(
                            rs.getString("apellido_paciente")
                    );

                    return c;
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al obtener cita: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        return null;
    }
}