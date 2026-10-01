package com.orientapsi.servlet;

import com.orientapsi.dao.UsuarioDAO;
import com.orientapsi.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/PsicologoServlet")
public class PsicologoServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String correo = request.getParameter("correo");
        String clave = request.getParameter("clave");

        Usuario nuevoPsicologo = new Usuario();
        nuevoPsicologo.setIdRol(2); // Rol 2: Psicólogo
        nuevoPsicologo.setNombres(nombres);
        nuevoPsicologo.setApellidos(apellidos);
        nuevoPsicologo.setCorreo(correo);
        nuevoPsicologo.setClaveHash(clave);
        nuevoPsicologo.setEstado("ACTIVO");

        boolean exito = usuarioDAO.registrarUsuario(nuevoPsicologo);

        if (exito) {
            response.sendRedirect("admin_dashboard.jsp?status=success");
        } else {
            response.sendRedirect("admin_dashboard.jsp?status=error");
        }
    }
}