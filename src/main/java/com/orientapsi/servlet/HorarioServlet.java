package com.orientapsi.servlet;

import com.orientapsi.config.ConexionBD;
import com.orientapsi.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/HorarioServlet")
public class HorarioServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Usuario usuario =
                (Usuario) session.getAttribute("usuarioLogueado");

        // Solo administrador
        if (usuario == null || usuario.getIdRol() != 1) {
            response.sendRedirect("index.jsp");
            return;
        }

        String idPsicologoStr =
                request.getParameter("idPsicologo");

        String fecha =
                request.getParameter("fecha");

        String horaInicio =
                request.getParameter("horaInicio");

        String horaFin =
                request.getParameter("horaFin");

        String modalidad =
                request.getParameter("modalidad");


        // =========================
        // VALIDAR CAMPOS
        // =========================
        if (idPsicologoStr == null
                || idPsicologoStr.isBlank()
                || fecha == null
                || fecha.isBlank()
                || horaInicio == null
                || horaInicio.isBlank()
                || horaFin == null
                || horaFin.isBlank()
                || modalidad == null
                || modalidad.isBlank()) {

            response.sendRedirect(
                    "crear_horario.jsp?error=campos"
            );

            return;
        }


        int idPsicologo;

        try {

            idPsicologo =
                    Integer.parseInt(idPsicologoStr);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "crear_horario.jsp?error=psicologo"
            );

            return;
        }


        // =========================
        // VALIDAR HORAS
        // =========================
        if (horaInicio.compareTo(horaFin) >= 0) {

            response.sendRedirect(
                    "crear_horario.jsp?error=horas"
            );

            return;
        }


        // =========================
        // SQL PARA DETECTAR
        // SOLAPAMIENTO
        // =========================

        String sqlVerificar =
                "SELECT id_horario " +
                        "FROM HORARIO " +
                        "WHERE id_psicologo = ? " +
                        "AND fecha = ? " +
                        "AND ( " +
                        "    ? < hora_fin " +
                        "    AND ? > hora_inicio " +
                        ")";


        String sqlInsertar =
                "INSERT INTO HORARIO " +
                        "(id_psicologo, fecha, hora_inicio, " +
                        "hora_fin, modalidad, estado) " +
                        "VALUES (?, ?, ?, ?, ?, 'DISPONIBLE')";


        try (Connection con =
                     ConexionBD.getConexion()) {

            if (con == null) {

                response.sendRedirect(
                        "crear_horario.jsp?error=conexion"
                );

                return;
            }


            // =========================
            // VERIFICAR DUPLICADOS
            // O SOLAPAMIENTOS
            // =========================

            try (PreparedStatement psVerificar =
                         con.prepareStatement(sqlVerificar)) {

                psVerificar.setInt(
                        1,
                        idPsicologo
                );

                psVerificar.setString(
                        2,
                        fecha
                );

                psVerificar.setString(
                        3,
                        horaInicio
                );

                psVerificar.setString(
                        4,
                        horaFin
                );


                try (ResultSet rs =
                             psVerificar.executeQuery()) {

                    if (rs.next()) {

                        response.sendRedirect(
                                "crear_horario.jsp?error=duplicado"
                        );

                        return;
                    }
                }
            }


            // =========================
            // INSERTAR HORARIO
            // =========================

            try (PreparedStatement psInsertar =
                         con.prepareStatement(sqlInsertar)) {

                psInsertar.setInt(
                        1,
                        idPsicologo
                );

                psInsertar.setString(
                        2,
                        fecha
                );

                psInsertar.setString(
                        3,
                        horaInicio
                );

                psInsertar.setString(
                        4,
                        horaFin
                );

                psInsertar.setString(
                        5,
                        modalidad
                );


                int filas =
                        psInsertar.executeUpdate();


                if (filas > 0) {

                    response.sendRedirect(
                            "AdminServlet?horario=success"
                    );

                } else {

                    response.sendRedirect(
                            "crear_horario.jsp?error=db"
                    );
                }
            }


        } catch (SQLException e) {

            System.err.println(
                    "Error al crear horario: "
                            + e.getMessage()
            );

            e.printStackTrace();


            // Código MySQL para duplicado UNIQUE
            if (e.getErrorCode() == 1062) {

                response.sendRedirect(
                        "crear_horario.jsp?error=duplicado"
                );

            } else {

                response.sendRedirect(
                        "crear_horario.jsp?error=db"
                );
            }
        }
    }
}