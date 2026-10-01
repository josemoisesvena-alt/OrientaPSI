package com.orientapsi.servlet;

import com.orientapsi.dao.CitaDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/CitaServlet")
public class CitaServlet extends HttpServlet {

    private final CitaDAO citaDAO = new CitaDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idCita = Integer.parseInt(request.getParameter("idCita"));
        String accion = request.getParameter("accion"); // "CONFIRMADA" o "CANCELADA"

        boolean exito = citaDAO.cambiarEstadoCita(idCita, accion);

        if (exito) {
            response.sendRedirect("admin_dashboard.jsp?citaStatus=success");
        } else {
            response.sendRedirect("admin_dashboard.jsp?citaStatus=error");
        }
    }
}