package com.orientapsi.servlet;

import com.orientapsi.config.ConexionBD;
import com.orientapsi.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/ReservarCitaServlet")
public class ReservarCitaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || usuario.getIdRol() != 3) {
            response.sendRedirect("index.jsp");
            return;
        }

        String idHorarioStr = request.getParameter("idHorario");
        String motivo = request.getParameter("motivo");

        if (idHorarioStr == null || idHorarioStr.isEmpty() || motivo == null || motivo.isEmpty()) {
            response.sendRedirect("paciente_dashboard.jsp?reserva=error");
            return;
        }

        int idHorario = Integer.parseInt(idHorarioStr);

        try (Connection con = ConexionBD.getConexion()) {
            con.setAutoCommit(false); // Transacción segura

            // 1. Obtener el id_paciente correspondiente al id_usuario logueado
            int idPaciente = -1;
            String sqlPaciente = "SELECT id_paciente FROM PACIENTE WHERE id_usuario = ?";
            try (PreparedStatement psP = con.prepareStatement(sqlPaciente)) {
                psP.setInt(1, usuario.getIdUsuario());
                try (ResultSet rsP = psP.executeQuery()) {
                    if (rsP.next()) {
                        idPaciente = rsP.getInt("id_paciente");
                    }
                }
            }

            if (idPaciente == -1) {
                con.rollback();
                response.sendRedirect("paciente_dashboard.jsp?reserva=error");
                return;
            }

            // 2. Insertar la cita con estado PENDIENTE
            String sqlCita = "INSERT INTO CITA (id_paciente, id_horario, motivo_consulta, estado) VALUES (?, ?, ?, 'PENDIENTE')";
            try (PreparedStatement psC = con.prepareStatement(sqlCita)) {
                psC.setInt(1, idPaciente);
                psC.setInt(2, idHorario);
                psC.setString(3, motivo);
                psC.executeUpdate();
            }

            // 3. Actualizar el horario a OCUPADO para que ya no aparezca disponible
            String sqlHorario = "UPDATE HORARIO SET estado = 'OCUPADO' WHERE id_horario = ?";
            try (PreparedStatement psH = con.prepareStatement(sqlHorario)) {
                psH.setInt(1, idHorario);
                psH.executeUpdate();
            }

            con.commit(); // Confirmar cambios
            response.sendRedirect("paciente_dashboard.jsp?reserva=success");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("paciente_dashboard.jsp?reserva=error");
        }
    }
}