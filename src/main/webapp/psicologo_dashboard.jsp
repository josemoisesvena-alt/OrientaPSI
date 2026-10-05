<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.orientapsi.model.Usuario" %>
<%@ page import="com.orientapsi.config.ConexionBD" %>
<%@ page import="com.orientapsi.dao.PsicologoDAO" %>

<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

    if (usuario == null || usuario.getIdRol() != 2) {
        response.sendRedirect("index.jsp");
        return;
    }

    // Obtener el verdadero id_psicologo usando el id_usuario logueado
    PsicologoDAO psicologoDAO = new PsicologoDAO();

    int idPsicologo =
            psicologoDAO.obtenerIdPsicologoPorUsuario(
                    usuario.getIdUsuario()
            );

    // Si no existe registro en PSICOLOGO, no podrá consultar agenda
    if (idPsicologo == -1) {
        idPsicologo = 0;
    }
%>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">

    <title>OrientaPsi - Panel del Psicólogo</title>

    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
            rel="stylesheet">

</head>

<body class="bg-light">

<!-- Navegación -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">

    <div class="container">

        <a class="navbar-brand fw-bold" href="#">
            OrientaPsi
        </a>

        <div class="d-flex align-items-center">

            <span class="navbar-text text-white me-3">

                Psic.

                <strong>
                    <%= usuario.getNombres() %>
                    <%= usuario.getApellidos() %>
                </strong>

            </span>

            <a href="LogoutServlet"
               class="btn btn-outline-light btn-sm">

                Cerrar Sesión

            </a>

        </div>

    </div>

</nav>


<div class="container mb-5">

    <!-- Encabezado -->
    <div class="p-4 bg-white rounded shadow-sm mb-4">

        <h3 class="fw-bold text-primary">
            Mi Agenda Profesional
        </h3>

        <p class="text-muted mb-0">
            Consulta las citas confirmadas y los pacientes
            asignados a tus horarios.
        </p>

    </div>


    <!-- Verificación del perfil -->
    <% if (idPsicologo == 0) { %>

        <div class="alert alert-warning">

            <strong>Advertencia:</strong>

            Tu usuario tiene rol de psicólogo,
            pero no existe un registro asociado
            en la tabla PSICOLOGO.

            Comunícate con el administrador.

        </div>

    <% } %>


    <!-- Tabla de Citas -->
    <div class="card shadow-sm border-0">

        <div class="card-header bg-white py-3">

            <h5 class="mb-0 fw-bold text-primary">
                Citas Confirmadas
            </h5>

        </div>


        <div class="card-body p-0">

            <div class="table-responsive">

                <table class="table table-hover mb-0 align-middle">

                    <thead class="table-light">

                    <tr>

                        <th># Cita</th>

                        <th>Fecha</th>

                        <th>Hora</th>

                        <th>Paciente</th>

                        <th>Motivo</th>

                        <th>Modalidad</th>

                    </tr>

                    </thead>


                    <tbody>

                    <%

                        if (idPsicologo != 0) {

                            String sql =
                                    "SELECT " +
                                    "c.id_cita, " +
                                    "h.fecha, " +
                                    "h.hora_inicio, " +
                                    "h.hora_fin, " +
                                    "h.modalidad, " +
                                    "c.motivo_consulta, " +
                                    "u.nombres AS p_nombres, " +
                                    "u.apellidos AS p_apellidos " +

                                    "FROM CITA c " +

                                    "INNER JOIN HORARIO h " +
                                    "ON c.id_horario = h.id_horario " +

                                    "INNER JOIN PACIENTE p " +
                                    "ON c.id_paciente = p.id_paciente " +

                                    "INNER JOIN USUARIO u " +
                                    "ON p.id_usuario = u.id_usuario " +

                                    "WHERE h.id_psicologo = ? " +

                                    "AND c.estado = 'CONFIRMADA' " +

                                    "ORDER BY h.fecha ASC, h.hora_inicio ASC";


                            try (
                                    Connection con =
                                            ConexionBD.getConexion();

                                    PreparedStatement ps =
                                            con.prepareStatement(sql)
                            ) {

                                // AQUÍ VA EL ID DEL PSICÓLOGO
                                // NO EL ID DEL USUARIO
                                ps.setInt(1, idPsicologo);


                                try (
                                        ResultSet rs =
                                                ps.executeQuery()
                                ) {

                                    boolean hayCitas = false;


                                    while (rs.next()) {

                                        hayCitas = true;

                    %>

                    <tr>

                        <td>
                            #<%= rs.getInt("id_cita") %>
                        </td>


                        <td>
                            <%= rs.getDate("fecha") %>
                        </td>


                        <td>

                            <%= rs.getTime("hora_inicio") %>

                            -

                            <%= rs.getTime("hora_fin") %>

                        </td>


                        <td>

                            <strong>

                                <%= rs.getString("p_nombres") %>

                                <%= rs.getString("p_apellidos") %>

                            </strong>

                        </td>


                        <td>

                            <%= rs.getString("motivo_consulta") %>

                        </td>


                        <td>

                            <span class="badge bg-info text-dark">

                                <%= rs.getString("modalidad") %>

                            </span>

                        </td>

                    </tr>


                    <%

                                    }


                                    if (!hayCitas) {

                    %>

                    <tr>

                        <td colspan="6"
                            class="text-center text-muted py-5">

                            No tienes citas confirmadas
                            en este momento.

                        </td>

                    </tr>

                    <%

                                    }

                                }

                            } catch (SQLException e) {

                                e.printStackTrace();

                    %>

                    <tr>

                        <td colspan="6"
                            class="text-center text-danger py-4">

                            Error al cargar las citas.

                            <br>

                            <small>
                                <%= e.getMessage() %>
                            </small>

                        </td>

                    </tr>

                    <%

                            }

                        }

                    %>

                    </tbody>

                </table>

            </div>

        </div>

    </div>

</div>


<script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js">
</script>

</body>

</html>