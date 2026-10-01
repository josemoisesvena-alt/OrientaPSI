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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");
        String clave = request.getParameter("clave");

        Usuario usuario = usuarioDAO.validarLogin(correo, clave);

        if (usuario != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogueado", usuario);

            switch (usuario.getIdRol()) {
                case 1:
                    response.sendRedirect("AdminServlet"); // Cambiado de admin_dashboard.jsp a AdminServlet
                    return;
                case 2:
                    response.sendRedirect("psicologo_dashboard.jsp");
                    return;
                case 3:
                    response.sendRedirect("paciente_dashboard.jsp");
                    return;
                default:
                    response.sendRedirect("index.jsp?error=rol");
                    return;
            }
        } else {
            response.sendRedirect("index.jsp?error=credenciales");
        }
    }
}