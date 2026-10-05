package com.orientapsi.servlet;

import com.orientapsi.dao.UsuarioDAO;
import com.orientapsi.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String correo = request.getParameter("correo");
        String clave = request.getParameter("clave");

        // ==========================================
        // VALIDAR CAMPOS
        // ==========================================

        if (correo == null
                || correo.isBlank()
                || clave == null
                || clave.isBlank()) {

            response.sendRedirect(
                    "index.jsp?error=campos"
            );

            return;
        }

        correo = correo.trim().toLowerCase();

        // ==========================================
        // VALIDAR CREDENCIALES
        // ==========================================

        Usuario usuario =
                usuarioDAO.validarLogin(
                        correo,
                        clave
                );

        if (usuario == null) {

            response.sendRedirect(
                    "index.jsp?error=credenciales"
            );

            return;
        }

        // ==========================================
        // CREAR SESIÓN
        // ==========================================

        HttpSession session =
                request.getSession(true);

        // Opcional pero recomendable
        session.setMaxInactiveInterval(30 * 60);

        session.setAttribute(
                "usuarioLogueado",
                usuario
        );

        // ==========================================
        // REDIRIGIR SEGÚN ROL
        // ==========================================

        switch (usuario.getIdRol()) {

            case 1:

                response.sendRedirect(
                        "AdminServlet"
                );

                break;

            case 2:

                response.sendRedirect(
                        "psicologo_dashboard.jsp"
                );

                break;

            case 3:

                response.sendRedirect(
                        "paciente_dashboard.jsp"
                );

                break;

            default:

                session.invalidate();

                response.sendRedirect(
                        "index.jsp?error=rol"
                );

                break;
        }
    }
}