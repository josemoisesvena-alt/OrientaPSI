<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.orientapsi.model.Usuario" %>
<%@ page import="com.orientapsi.config.ConexionBD" %>
<%@ page import="java.sql.*" %>

<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

    // Solo pacientes
    if (usuario == null || usuario.getIdRol() != 3) {
        response.sendRedirect("index.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">

<head>

    <meta charset="UTF-8">

    <title>
        OrientaPsi - Panel Paciente
    </title>

    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
            rel="stylesheet">

</head>

<body class="bg-light">


<!-- ========================= -->
<!-- NAVEGACIÓN -->
<!-- ========================= -->

<nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">

    <div class="container">

        <a
                class="navbar-brand fw-bold"
                href="#">

            OrientaPsi

        </a>


        <div class="d-flex align-items-center">

            <span class="navbar-text text-white me-3">

                Bienvenido,

                <strong>
                    <%= usuario.getNombres() %>
                </strong>

            </span>


            <a
                    href="LogoutServlet"
                    class="btn btn-outline-light btn-sm">

                Cerrar Sesión

            </a>

        </div>

    </div>

</nav>


<div class="container mb-5">


    <!-- ========================= -->
    <!-- ENCABEZADO -->
    <!-- ========================= -->

    <div class="p-4 bg-white rounded shadow-sm mb-4">

        <h3 class="fw-bold text-primary">
            Panel del Paciente
        </h3>

        <p class="text-muted mb-0">

            Desde aquí podrás explorar psicólogos disponibles
            y solicitar tus citas de orientación.

        </p>

    </div>


    <!-- ========================= -->
    <!-- MENSAJES -->
    <!-- ========================= -->

    <% if ("success".equals(request.getParameter("reserva"))) { %>

        <div
                class="alert alert-success alert-dismissible fade show"
                role="alert">

            ¡Tu cita ha sido solicitada con éxito!

            Espera la aprobación del administrador.

            <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    <% } %>


    <% if ("error".equals(request.getParameter("reserva"))) { %>

        <div
                class="alert alert-danger alert-dismissible fade show"
                role="alert">

            Hubo un error al procesar tu solicitud.

            Inténtalo nuevamente.

            <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    <% } %>


    <% if ("ocupado".equals(request.getParameter("reserva"))) { %>

        <div
                class="alert alert-warning alert-dismissible fade show"
                role="alert">

            Ese horario ya fue reservado por otro paciente.

            Selecciona otro horario disponible.

            <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    <% } %>


    <!-- ========================= -->
    <!-- SOLICITAR CITA -->
    <!-- ========================= -->

    <div class="card shadow-sm border-0">

        <div class="card-header bg-white py-3">

            <h5 class="mb-0 fw-bold text-primary">
                Solicitar una Nueva Cita
            </h5>

        </div>


        <div class="card-body">

            <form
                    action="ReservarCitaServlet"
                    method="POST">


                <div class="row g-3">


                    <!-- HORARIO -->

                    <div class="col-md-6">

                        <label class="form-label fw-bold">

                            Seleccionar Horario Disponible

                        </label>


                        <select
                                name="idHorario"
                                class="form-select"
                                required>


                            <option value="">

                                -- Selecciona una fecha y hora --

                            </option>


                            <%

                                String sqlHorarios =

                                        "SELECT " +
                                        "h.id_horario, " +
                                        "h.fecha, " +
                                        "h.hora_inicio, " +
                                        "h.hora_fin, " +
                                        "h.modalidad, " +
                                        "u.nombres, " +
                                        "u.apellidos " +

                                        "FROM HORARIO h " +

                                        "INNER JOIN PSICOLOGO p " +
                                        "ON h.id_psicologo = p.id_psicologo " +

                                        "INNER JOIN USUARIO u " +
                                        "ON p.id_usuario = u.id_usuario " +

                                        "WHERE h.estado = 'DISPONIBLE' " +

                                        "AND p.estado = 'ACTIVO' " +
                                        "AND u.estado = 'ACTIVO' " +

                                        "AND h.fecha >= CURDATE() " +

                                        "ORDER BY " +
                                        "h.fecha ASC, " +
                                        "h.hora_inicio ASC";


                                try (

                                        Connection con =
                                                ConexionBD.getConexion();

                                        PreparedStatement ps =
                                                con.prepareStatement(sqlHorarios);

                                        ResultSet rs =
                                                ps.executeQuery()

                                ) {


                                    while (rs.next()) {

                            %>


                            <option
                                    value="<%= rs.getInt("id_horario") %>">

                                Psic.
                                <%= rs.getString("nombres") %>
                                <%= rs.getString("apellidos") %>

                                -

                                <%= rs.getDate("fecha") %>

                                <%= rs.getTime("hora_inicio") %>
                                -
                                <%= rs.getTime("hora_fin") %>

                                -

                                <%= rs.getString("modalidad") %>

                            </option>


                            <%

                                    }


                                } catch (SQLException e) {

                                    e.printStackTrace();
                                }

                            %>


                        </select>

                    </div>


                    <!-- MOTIVO -->

                    <div class="col-md-6">

                        <label class="form-label fw-bold">

                            Motivo de Consulta

                        </label>


                        <input
                                type="text"
                                name="motivo"
                                class="form-control"
                                maxlength="250"
                                placeholder="Ej. Ansiedad, estrés académico, orientación..."
                                required>

                    </div>


                    <!-- BOTÓN -->

                    <div class="col-12 text-end">

                        <button
                                type="submit"
                                class="btn btn-primary px-4 fw-bold">

                            Solicitar Cita

                        </button>

                    </div>


                </div>

            </form>

        </div>

    </div>


    <!-- ========================= -->
    <!-- MIS CITAS -->
    <!-- ========================= -->

    <div class="card shadow-sm border-0 mt-4">


        <div class="card-header bg-white py-3">

            <h5 class="mb-0 fw-bold text-primary">

                Mis Citas Solicitadas

            </h5>

        </div>


        <div class="card-body p-0">


            <div class="table-responsive">


                <table class="table table-hover mb-0 align-middle">


                    <thead class="table-light">

                    <tr>

                        <th>#</th>

                        <th>Fecha y Hora</th>

                        <th>Psicólogo / Modalidad</th>

                        <th>Motivo</th>

                        <th>Estado</th>

                    </tr>

                    </thead>


                    <tbody>


                    <%

                        try (

                                Connection con =
                                        ConexionBD.getConexion();

                                PreparedStatement psId =
                                        con.prepareStatement(

                                                "SELECT id_paciente " +
                                                "FROM PACIENTE " +
                                                "WHERE id_usuario = ?"

                                        )

                        ) {


                            psId.setInt(
                                    1,
                                    usuario.getIdUsuario()
                            );


                            try (
                                    ResultSet rsId =
                                            psId.executeQuery()
                            ) {


                                if (rsId.next()) {


                                    int idPaciente =
                                            rsId.getInt(
                                                    "id_paciente"
                                            );


                                    String sqlCitas =

                                            "SELECT " +

                                            "c.id_cita, " +
                                            "c.motivo_consulta, " +
                                            "c.estado, " +

                                            "h.fecha, " +
                                            "h.hora_inicio, " +
                                            "h.hora_fin, " +
                                            "h.modalidad, " +

                                            "u.nombres, " +
                                            "u.apellidos " +

                                            "FROM CITA c " +

                                            "INNER JOIN HORARIO h " +
                                            "ON c.id_horario = h.id_horario " +

                                            "INNER JOIN PSICOLOGO p " +
                                            "ON h.id_psicologo = p.id_psicologo " +

                                            "INNER JOIN USUARIO u " +
                                            "ON p.id_usuario = u.id_usuario " +

                                            "WHERE c.id_paciente = ? " +

                                            "ORDER BY " +
                                            "h.fecha DESC, " +
                                            "h.hora_inicio DESC";


                                    try (

                                            PreparedStatement psC =
                                                    con.prepareStatement(
                                                            sqlCitas
                                                    )

                                    ) {


                                        psC.setInt(
                                                1,
                                                idPaciente
                                        );


                                        try (
                                                ResultSet rsC =
                                                        psC.executeQuery()
                                        ) {


                                            boolean hayCitas = false;


                                            while (rsC.next()) {


                                                hayCitas = true;


                                                String estado =
                                                        rsC.getString(
                                                                "estado"
                                                        );


                                                String badgeClass =
                                                        "bg-warning text-dark";


                                                if ("CONFIRMADA".equals(estado)) {

                                                    badgeClass =
                                                            "bg-success";

                                                } else if ("CANCELADA".equals(estado)) {

                                                    badgeClass =
                                                            "bg-danger";

                                                } else if ("COMPLETADA".equals(estado)) {

                                                    badgeClass =
                                                            "bg-primary";
                                                }

                    %>


                    <tr>


                        <td>

                            #<%= rsC.getInt("id_cita") %>

                        </td>


                        <td>

                            <%= rsC.getDate("fecha") %>

                            <br>

                            <small class="text-muted">

                                <%= rsC.getTime("hora_inicio") %>

                                -

                                <%= rsC.getTime("hora_fin") %>

                            </small>

                        </td>


                        <td>

                            <strong>

                                Psic.

                                <%= rsC.getString("nombres") %>

                                <%= rsC.getString("apellidos") %>

                            </strong>

                            <br>

                            <span class="badge bg-info text-dark">

                                <%= rsC.getString("modalidad") %>

                            </span>

                        </td>


                        <td>

                            <%= rsC.getString("motivo_consulta") %>

                        </td>


                        <td>

                            <span class="badge <%= badgeClass %>">

                                <%= estado %>

                            </span>

                        </td>


                    </tr>


                    <%

                                            }


                                            if (!hayCitas) {

                    %>


                    <tr>

                        <td
                                colspan="5"
                                class="text-center text-muted py-4">

                            Aún no has solicitado ninguna cita.

                        </td>

                    </tr>


                    <%

                                            }


                                        }

                                    }


                                } else {

                    %>


                    <tr>

                        <td
                                colspan="5"
                                class="text-center text-warning py-4">

                            Tu usuario todavía no tiene
                            un perfil de paciente registrado.

                        </td>

                    </tr>


                    <%

                                }

                            }


                        } catch (SQLException e) {

                            e.printStackTrace();

                    %>


                    <tr>

                        <td
                                colspan="5"
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