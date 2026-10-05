package com.orientapsi.servlet;

import com.orientapsi.dao.CitaDAO;
import com.orientapsi.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/CitaServlet")
public class CitaServlet extends HttpServlet {

    private final CitaDAO citaDAO = new CitaDAO();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        Usuario usuario =
                (Usuario) session.getAttribute("usuarioLogueado");

        // Solo administrador
        if (usuario == null || usuario.getIdRol() != 1) {
            response.sendRedirect("index.jsp");
            return;
        }

        String idCitaStr = request.getParameter("idCita");
        String accion = request.getParameter("accion");

        if (idCitaStr == null
                || idCitaStr.isBlank()
                || accion == null
                || accion.isBlank()) {

            response.sendRedirect(
                    "AdminServlet?cita=error"
            );

            return;
        }

        int idCita;

        try {

            idCita = Integer.parseInt(idCitaStr);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "AdminServlet?cita=error"
            );

            return;
        }

        boolean exito;

        switch (accion) {

            case "CONFIRMADA":

                exito =
                        citaDAO.confirmarCita(idCita);

                break;

            case "CANCELADA":

                exito =
                        citaDAO.cancelarCita(idCita);

                break;

            default:

                response.sendRedirect(
                        "AdminServlet?cita=accionInvalida"
                );

                return;
        }

        if (exito) {

            if ("CONFIRMADA".equals(accion)) {

                response.sendRedirect(
                        "AdminServlet?cita=aprobada"
                );

            } else {

                response.sendRedirect(
                        "AdminServlet?cita=cancelada"
                );
            }

        } else {

            response.sendRedirect(
                    "AdminServlet?cita=noDisponible"
            );
        }
    }
}