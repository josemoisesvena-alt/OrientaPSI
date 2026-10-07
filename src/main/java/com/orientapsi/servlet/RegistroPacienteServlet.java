package com.orientapsi.servlet;

import com.orientapsi.dao.PacienteDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/RegistroPacienteServlet")
public class RegistroPacienteServlet extends HttpServlet {

    private final PacienteDAO pacienteDAO = new PacienteDAO();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String correo = request.getParameter("correo");
        String clave = request.getParameter("clave");
        String fechaNacimiento = request.getParameter("fechaNacimiento");

        if (nombres == null || nombres.isBlank()
                || apellidos == null || apellidos.isBlank()
                || correo == null || correo.isBlank()
                || clave == null || clave.isBlank()
                || fechaNacimiento == null || fechaNacimiento.isBlank()) {

            response.sendRedirect(
                    "registro_paciente_publico.jsp?error=campos"
            );

            return;
        }

        if (clave.length() < 6) {

            response.sendRedirect(
                    "registro_paciente_publico.jsp?error=clave"
            );

            return;
        }

        boolean registrado =
                pacienteDAO.registrarPaciente(
                        nombres.trim(),
                        apellidos.trim(),
                        correo.trim().toLowerCase(),
                        clave,
                        fechaNacimiento
                );

        if (registrado) {

            response.sendRedirect(
                    "index.jsp?registro=success"
            );

        } else {

            response.sendRedirect(
                    "registro_paciente_publico.jsp?error=db"
            );
        }
    }
}