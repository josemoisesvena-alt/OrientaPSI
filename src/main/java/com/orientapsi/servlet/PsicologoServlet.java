package com.orientapsi.servlet;

import com.orientapsi.dao.PsicologoDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/PsicologoServlet")
public class PsicologoServlet extends HttpServlet {

    private final PsicologoDAO psicologoDAO = new PsicologoDAO();

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

        String colegiatura = request.getParameter("colegiatura");
        String presentacion = request.getParameter("presentacion");
        String modalidad = request.getParameter("modalidad");
        String experienciaStr = request.getParameter("experiencia");

        // Validar campos obligatorios
        if (nombres == null || nombres.isBlank()
                || apellidos == null || apellidos.isBlank()
                || correo == null || correo.isBlank()
                || clave == null || clave.isBlank()
                || colegiatura == null || colegiatura.isBlank()
                || modalidad == null || modalidad.isBlank()
                || experienciaStr == null || experienciaStr.isBlank()) {

            response.sendRedirect(
                    "registrar_psicologo.jsp?error=campos"
            );

            return;
        }

        // Validar contraseña mínima
        if (clave.length() < 6) {

            response.sendRedirect(
                    "registrar_psicologo.jsp?error=clave"
            );

            return;
        }

        try {

            int experiencia =
                    Integer.parseInt(experienciaStr);

            if (experiencia < 0) {

                response.sendRedirect(
                        "registrar_psicologo.jsp?error=experiencia"
                );

                return;
            }

            boolean exito =
                    psicologoDAO.registrarPsicologo(
                            nombres.trim(),
                            apellidos.trim(),
                            correo.trim().toLowerCase(),
                            clave,
                            colegiatura.trim(),
                            presentacion != null
                                    ? presentacion.trim()
                                    : "",
                            modalidad.trim(),
                            experiencia
                    );

            if (exito) {

                response.sendRedirect(
                        "AdminServlet?psicologo=success"
                );

            } else {

                response.sendRedirect(
                        "registrar_psicologo.jsp?error=db"
                );
            }

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "registrar_psicologo.jsp?error=experiencia"
            );
        }
    }
}