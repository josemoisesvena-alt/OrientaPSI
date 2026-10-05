package com.orientapsi.servlet;

import com.orientapsi.dao.CitaDAO;
import com.orientapsi.model.Cita;
import com.orientapsi.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {

    private final CitaDAO citaDAO = new CitaDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // ==========================================
        // 1. VALIDAR SESIÓN
        // ==========================================

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


        // ==========================================
        // 2. OBTENER CITAS PENDIENTES
        // ==========================================

        List<Cita> listaCitasPendientes =
                citaDAO.obtenerCitasPendientes();


        // ==========================================
        // 3. ENVIAR DATOS AL JSP
        // ==========================================

        request.setAttribute(
                "listaCitasPendientes",
                listaCitasPendientes
        );


        // ==========================================
        // 4. FORWARD AL DASHBOARD
        // ==========================================

        request.getRequestDispatcher(
                "admin_dashboard.jsp"
        ).forward(request, response);
    }


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}