package com.orientapsi.servlet;

import com.orientapsi.dao.CitaDAO;
import com.orientapsi.model.Cita;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {

    private final CitaDAO citaDAO = new CitaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener la lista de citas pendientes usando el DAO
        List<Cita> listaCitasPendientes = citaDAO.obtenerCitasPendientes();

        // Guardar la lista en los atributos de la petición
        request.setAttribute("listaCitasPendientes", listaCitasPendientes);

        // Redirigir (hacer forward) hacia el panel del administrador para pintar los datos
        request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}