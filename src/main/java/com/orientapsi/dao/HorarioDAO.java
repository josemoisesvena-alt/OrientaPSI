package com.orientapsi.dao;

import com.orientapsi.config.ConexionBD;
import com.orientapsi.model.HorarioDTO;
import com.orientapsi.model.PsicologoOpcionDTO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

public class HorarioDAO {


    // =========================================
    // OBTENER HORARIOS DISPONIBLES
    // =========================================
    public List<HorarioDTO> obtenerHorariosDisponibles() {

        List<HorarioDTO> lista = new ArrayList<>();


        String sql =
                "SELECT " +
                        "h.id_horario, " +
                        "h.fecha, " +
                        "h.hora_inicio, " +
                        "h.hora_fin, " +
                        "h.modalidad, " +
                        "u.nombres AS nombre_psicologo, " +
                        "u.apellidos AS apellido_psicologo " +

                        "FROM HORARIO h " +

                        "INNER JOIN PSICOLOGO p " +
                        "ON h.id_psicologo = p.id_psicologo " +

                        "INNER JOIN USUARIO u " +
                        "ON p.id_usuario = u.id_usuario " +

                        "WHERE h.estado = 'DISPONIBLE' " +
                        "AND h.fecha >= CURDATE() " +

                        "ORDER BY h.fecha ASC, h.hora_inicio ASC";


        try (
                Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                HorarioDTO h = new HorarioDTO();

                h.setIdHorario(
                        rs.getInt("id_horario")
                );

                h.setFecha(
                        rs.getDate("fecha").toString()
                );

                h.setHoraInicio(
                        rs.getTime("hora_inicio").toString()
                );

                h.setHoraFin(
                        rs.getTime("hora_fin").toString()
                );

                h.setModalidad(
                        rs.getString("modalidad")
                );

                h.setNombrePsicologo(
                        rs.getString("nombre_psicologo")
                );

                h.setApellidoPsicologo(
                        rs.getString("apellido_psicologo")
                );

                lista.add(h);
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al obtener horarios disponibles: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }


        return lista;
    }


    // =========================================
    // LISTAR PSICÓLOGOS ACTIVOS
    // =========================================
    public List<PsicologoOpcionDTO> obtenerPsicologosActivos() {

        List<PsicologoOpcionDTO> lista =
                new ArrayList<>();


        String sql =
                "SELECT " +
                        "p.id_psicologo, " +
                        "u.nombres, " +
                        "u.apellidos " +

                        "FROM PSICOLOGO p " +

                        "INNER JOIN USUARIO u " +
                        "ON p.id_usuario = u.id_usuario " +

                        "WHERE p.estado = 'ACTIVO' " +
                        "AND u.estado = 'ACTIVO' " +

                        "ORDER BY u.nombres, u.apellidos";


        try (
                Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                PsicologoOpcionDTO p =
                        new PsicologoOpcionDTO();

                p.setIdPsicologo(
                        rs.getInt("id_psicologo")
                );

                p.setNombres(
                        rs.getString("nombres")
                );

                p.setApellidos(
                        rs.getString("apellidos")
                );

                lista.add(p);
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al obtener psicólogos: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }


        return lista;
    }


    // =========================================
    // VALIDAR PSICÓLOGO ACTIVO
    // =========================================
    public boolean psicologoExisteActivo(
            int idPsicologo
    ) {

        String sql =
                "SELECT COUNT(*) " +
                        "FROM PSICOLOGO p " +
                        "INNER JOIN USUARIO u " +
                        "ON p.id_usuario = u.id_usuario " +
                        "WHERE p.id_psicologo = ? " +
                        "AND p.estado = 'ACTIVO' " +
                        "AND u.estado = 'ACTIVO'";


        try (
                Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, idPsicologo);


            try (
                    ResultSet rs = ps.executeQuery()
            ) {

                if (rs.next()) {

                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al validar psicólogo: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }


        return false;
    }


    // =========================================
    // VALIDAR CRUCE DE HORARIOS
    // =========================================
    public boolean existeCruceHorario(
            int idPsicologo,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin
    ) {

        String sql =
                "SELECT COUNT(*) " +
                        "FROM HORARIO " +

                        "WHERE id_psicologo = ? " +
                        "AND fecha = ? " +

                        "AND estado IN " +
                        "('DISPONIBLE', 'OCUPADO') " +

                        "AND hora_inicio < ? " +
                        "AND hora_fin > ?";


        try (
                Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    idPsicologo
            );

            ps.setDate(
                    2,
                    Date.valueOf(fecha)
            );

            ps.setTime(
                    3,
                    Time.valueOf(horaFin)
            );

            ps.setTime(
                    4,
                    Time.valueOf(horaInicio)
            );


            try (
                    ResultSet rs = ps.executeQuery()
            ) {

                if (rs.next()) {

                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al validar cruce de horario: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }


        return false;
    }


    // =========================================
    // REGISTRAR HORARIO
    // =========================================
    public boolean registrarHorario(
            int idPsicologo,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin,
            String modalidad
    ) {

        String sql =
                "INSERT INTO HORARIO " +
                        "(id_psicologo, " +
                        "fecha, " +
                        "hora_inicio, " +
                        "hora_fin, " +
                        "modalidad, " +
                        "estado) " +

                        "VALUES (?, ?, ?, ?, ?, 'DISPONIBLE')";


        try (
                Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    idPsicologo
            );

            ps.setDate(
                    2,
                    Date.valueOf(fecha)
            );

            ps.setTime(
                    3,
                    Time.valueOf(horaInicio)
            );

            ps.setTime(
                    4,
                    Time.valueOf(horaFin)
            );

            ps.setString(
                    5,
                    modalidad
            );


            return ps.executeUpdate() > 0;


        } catch (SQLException e) {

            System.err.println(
                    "Error al registrar horario: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }
}