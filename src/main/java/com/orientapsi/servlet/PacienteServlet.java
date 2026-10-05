package com.orientapsi.servlet;

import com.orientapsi.dao.PacienteDAO;
import com.orientapsi.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/PacienteServlet")
public class PacienteServlet extends HttpServlet {

    private final PacienteDAO pacienteDAO = new PacienteDAO();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // =========================================
        // VALIDAR SESIÓN DEL ADMINISTRADOR
        // =========================================

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        Usuario usuario =
                (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || usuario.getIdRol() != 1) {
            response.sendRedirect("index.jsp");
            return;
        }


        // =========================================
        // RECIBIR DATOS DEL FORMULARIO
        // =========================================

        String nombres =
                request.getParameter("nombres");

        String apellidos =
                request.getParameter("apellidos");

        String correo =
                request.getParameter("correo");

        String clave =
                request.getParameter("clave");

        String fechaNacimiento =
                request.getParameter("fechaNacimiento");


        // =========================================
        // VALIDAR CAMPOS
        // =========================================

        if (nombres == null || nombres.isBlank()
                || apellidos == null || apellidos.isBlank()
                || correo == null || correo.isBlank()
                || clave == null || clave.isBlank()
                || fechaNacimiento == null || fechaNacimiento.isBlank()) {

            response.sendRedirect(
                    "registrar_paciente.jsp?error=campos"
            );

            return;
        }


        // =========================================
        // VALIDAR CONTRASEÑA
        // =========================================

        if (clave.length() < 6) {

            response.sendRedirect(
                    "registrar_paciente.jsp?error=clave"
            );

            return;
        }


        // =========================================
        // REGISTRAR PACIENTE
        // =========================================

        boolean exito =
                pacienteDAO.registrarPaciente(
                        nombres.trim(),
                        apellidos.trim(),
                        correo.trim().toLowerCase(),
                        clave,
                        fechaNacimiento
                );


        // =========================================
        // REDIRECCIÓN
        // =========================================

        if (exito) {

            response.sendRedirect(
                    "AdminServlet?paciente=success"
            );

        } else {

            response.sendRedirect(
                    "registrar_paciente.jsp?error=db"
            );
        }
    }
}