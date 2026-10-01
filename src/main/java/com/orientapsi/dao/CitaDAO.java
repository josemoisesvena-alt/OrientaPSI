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

    public List<Cita> obtenerCitasPendientes() {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT c.id_cita, c.id_paciente, c.motivo_consulta, c.estado, " +
                "CONCAT(h.fecha, ' ', h.hora_inicio) AS fecha_hora " +
                "FROM CITA c " +
                "INNER JOIN HORARIO h ON c.id_horario = h.id_horario " +
                "WHERE c.estado = 'PENDIENTE'";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cita c = new Cita();
                c.setIdCita(rs.getInt("id_cita"));
                c.setIdPaciente(rs.getInt("id_paciente"));
                c.setMotivo(rs.getString("motivo_consulta"));
                c.setEstado(rs.getString("estado"));
                c.setFechaHora(rs.getTimestamp("fecha_hora"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar citas: " + e.getMessage());
        }
        return lista;
    }

    public boolean cambiarEstadoCita(int idCita, String nuevoEstado) {
        String sql = "UPDATE CITA SET estado = ? WHERE id_cita = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idCita);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado de cita: " + e.getMessage());
            return false;
        }
    }
}