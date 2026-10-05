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

@WebServlet("/ReservarCitaServlet")
public class ReservarCitaServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        Usuario usuario =
                (Usuario) session.getAttribute("usuarioLogueado");

        // Solo pacientes
        if (usuario == null || usuario.getIdRol() != 3) {
            response.sendRedirect("index.jsp");
            return;
        }

        String idHorarioStr =
                request.getParameter("idHorario");

        String motivo =
                request.getParameter("motivo");


        // ==========================================
        // VALIDAR CAMPOS
        // ==========================================

        if (idHorarioStr == null
                || idHorarioStr.isBlank()
                || motivo == null
                || motivo.isBlank()) {

            response.sendRedirect(
                    "paciente_dashboard.jsp?reserva=error"
            );

            return;
        }


        motivo = motivo.trim();

        // Tu columna motivo_consulta es VARCHAR(250)
        if (motivo.length() > 250) {

            response.sendRedirect(
                    "paciente_dashboard.jsp?reserva=error"
            );

            return;
        }


        int idHorario;

        try {

            idHorario =
                    Integer.parseInt(idHorarioStr);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "paciente_dashboard.jsp?reserva=error"
            );

            return;
        }


        Connection con = null;

        try {

            con = ConexionBD.getConexion();

            if (con == null) {

                response.sendRedirect(
                        "paciente_dashboard.jsp?reserva=error"
                );

                return;
            }


            // Iniciar transacción
            con.setAutoCommit(false);


            // ==========================================
            // 1. OBTENER ID DEL PACIENTE
            // ==========================================

            int idPaciente = -1;

            String sqlPaciente =
                    "SELECT id_paciente " +
                            "FROM PACIENTE " +
                            "WHERE id_usuario = ? " +
                            "AND estado = 'ACTIVO'";


            try (PreparedStatement psPaciente =
                         con.prepareStatement(sqlPaciente)) {

                psPaciente.setInt(
                        1,
                        usuario.getIdUsuario()
                );


                try (ResultSet rs =
                             psPaciente.executeQuery()) {

                    if (rs.next()) {

                        idPaciente =
                                rs.getInt("id_paciente");
                    }
                }
            }


            // No existe perfil de paciente
            if (idPaciente == -1) {

                con.rollback();

                response.sendRedirect(
                        "paciente_dashboard.jsp?reserva=error"
                );

                return;
            }


            // ==========================================
            // 2. BLOQUEAR / OCUPAR EL HORARIO
            // ==========================================
            //
            // Esto es lo más importante.
            //
            // Solo cambia a OCUPADO si todavía está
            // DISPONIBLE.
            //
            // Si otro paciente acaba de reservarlo,
            // executeUpdate() devolverá 0.
            // ==========================================

            String sqlHorario =
                    "UPDATE HORARIO " +
                            "SET estado = 'OCUPADO' " +
                            "WHERE id_horario = ? " +
                            "AND estado = 'DISPONIBLE' " +
                            "AND fecha >= CURDATE()";


            int horarioActualizado;


            try (PreparedStatement psHorario =
                         con.prepareStatement(sqlHorario)) {

                psHorario.setInt(
                        1,
                        idHorario
                );


                horarioActualizado =
                        psHorario.executeUpdate();
            }


            // Si devuelve 0 significa:
            // - ya está ocupado
            // - no existe
            // - o es una fecha pasada

            if (horarioActualizado == 0) {

                con.rollback();

                response.sendRedirect(
                        "paciente_dashboard.jsp?reserva=ocupado"
                );

                return;
            }


            // ==========================================
            // 3. INSERTAR LA CITA
            // ==========================================

            String sqlCita =
                    "INSERT INTO CITA " +
                            "(id_paciente, id_horario, motivo_consulta, estado) " +
                            "VALUES (?, ?, ?, 'PENDIENTE')";


            try (PreparedStatement psCita =
                         con.prepareStatement(sqlCita)) {

                psCita.setInt(
                        1,
                        idPaciente
                );

                psCita.setInt(
                        2,
                        idHorario
                );

                psCita.setString(
                        3,
                        motivo
                );


                int filas =
                        psCita.executeUpdate();


                if (filas == 0) {

                    con.rollback();

                    response.sendRedirect(
                            "paciente_dashboard.jsp?reserva=error"
                    );

                    return;
                }
            }


            // ==========================================
            // 4. CONFIRMAR TRANSACCIÓN
            // ==========================================

            con.commit();


            response.sendRedirect(
                    "paciente_dashboard.jsp?reserva=success"
            );


        } catch (SQLException e) {

            // ==========================================
            // ROLLBACK SI OCURRE ALGÚN ERROR
            // ==========================================

            if (con != null) {

                try {

                    con.rollback();

                } catch (SQLException rollbackError) {

                    rollbackError.printStackTrace();
                }
            }


            System.err.println(
                    "Error al reservar cita: "
                            + e.getMessage()
            );

            e.printStackTrace();


            /*
             * MySQL error 1062 =
             * clave duplicada.
             *
             * Como CITA.id_horario es UNIQUE,
             * también tenemos protección desde MySQL.
             */
            if (e.getErrorCode() == 1062) {

                response.sendRedirect(
                        "paciente_dashboard.jsp?reserva=ocupado"
                );

            } else {

                response.sendRedirect(
                        "paciente_dashboard.jsp?reserva=error"
                );
            }


        } finally {

            // ==========================================
            // CERRAR CONEXIÓN
            // ==========================================

            if (con != null) {

                try {

                    con.setAutoCommit(true);
                    con.close();

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }
}