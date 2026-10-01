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

@WebServlet("/AprobarCitaServlet")
public class AprobarCitaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        // Validar que sea administrador (Rol 1)
        if (usuario == null || usuario.getIdRol() != 1) {
            response.sendRedirect("index.jsp");
            return;
        }

        String idCitaStr = request.getParameter("idCita");

        if (idCitaStr != null && !idCitaStr.isEmpty()) {
            int idCita = Integer.parseInt(idCitaStr);

            try (Connection con = ConexionBD.getConexion();
                 PreparedStatement ps = con.prepareStatement("UPDATE CITA SET estado = 'CONFIRMADA' WHERE id_cita = ?")) {
                ps.setInt(1, idCita);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Redirigir de regreso al servlet del administrador para refrescar la lista
        response.sendRedirect("AdminServlet");
    }
}