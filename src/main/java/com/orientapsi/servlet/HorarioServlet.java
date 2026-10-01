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
import java.sql.SQLException;

@WebServlet("/HorarioServlet")
public class HorarioServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        // Validar que solo el administrador pueda crear horarios
        if (usuario == null || usuario.getIdRol() != 1) {
            response.sendRedirect("index.jsp");
            return;
        }

        String idPsicologoStr = request.getParameter("idPsicologo");
        String fecha = request.getParameter("fecha");
        String horaInicio = request.getParameter("horaInicio");
        String horaFin = request.getParameter("horaFin");
        String modalidad = request.getParameter("modalidad");

        if (idPsicologoStr == null || idPsicologoStr.isEmpty() ||
                fecha == null || fecha.isEmpty() ||
                horaInicio == null || horaInicio.isEmpty() ||
                horaFin == null || horaFin.isEmpty() ||
                modalidad == null || modalidad.isEmpty()) {
            response.sendRedirect("crear_horario.jsp?error=campos");
            return;
        }

        int idPsicologo = Integer.parseInt(idPsicologoStr);

        String sql = "INSERT INTO HORARIO (id_psicologo, fecha, hora_inicio, hora_fin, modalidad, estado) VALUES (?, ?, ?, ?, ?, 'DISPONIBLE')";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPsicologo);
            ps.setString(2, fecha);
            ps.setString(3, horaInicio);
            ps.setString(4, horaFin);
            ps.setString(5, modalidad);

            ps.executeUpdate();

            // Redirigir al panel del admin con éxito
            response.sendRedirect("AdminServlet?horario=success");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("crear_horario.jsp?error=db");
        }
    }
}