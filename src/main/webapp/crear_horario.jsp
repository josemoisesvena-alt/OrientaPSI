<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.orientapsi.model.Usuario" %>
<%@ page import="com.orientapsi.config.ConexionBD" %>
<%@ page import="java.sql.*" %>

<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

    if (usuario == null || usuario.getIdRol() != 1) {
        response.sendRedirect("index.jsp");
        return;
    }

    String error = request.getParameter("error");
%>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">

    <title>OrientaPsi - Crear Horario</title>

    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
            rel="stylesheet">
</head>

<body class="bg-light">

<div class="container mt-5 mb-5" style="max-width: 600px;">

    <div class="card shadow-sm border-0">

        <div class="card-header bg-primary text-white py-3">

            <h4 class="mb-0 fw-bold">
                Crear Nuevo Horario
            </h4>

        </div>

        <div class="card-body p-4">

            <!-- MENSAJES DE ERROR -->

            <% if ("duplicado".equals(error)) { %>

                <div class="alert alert-warning">
                    El psicólogo ya tiene un horario registrado
                    que se cruza con ese rango de horas.
                </div>

            <% } %>

            <% if ("horas".equals(error)) { %>

                <div class="alert alert-danger">
                    La hora de inicio debe ser menor
                    que la hora de fin.
                </div>

            <% } %>

            <% if ("campos".equals(error)) { %>

                <div class="alert alert-danger">
                    Completa todos los campos.
                </div>

            <% } %>

            <% if ("psicologo".equals(error)) { %>

                <div class="alert alert-danger">
                    El psicólogo seleccionado no es válido.
                </div>

            <% } %>

            <% if ("conexion".equals(error)) { %>

                <div class="alert alert-danger">
                    No se pudo conectar con la base de datos.
                </div>

            <% } %>

            <% if ("db".equals(error)) { %>

                <div class="alert alert-danger">
                    Ocurrió un error al guardar el horario.
                </div>

            <% } %>


            <!-- FORMULARIO -->

            <form action="HorarioServlet" method="POST">

                <!-- PSICOLOGO -->

                <div class="mb-3">

                    <label class="form-label fw-bold">
                        Psicólogo
                    </label>

                    <select
                            name="idPsicologo"
                            class="form-select"
                            required>

                        <option value="">
                            -- Selecciona un psicólogo --
                        </option>

                        <%

                            String sqlPsicologos =
                                    "SELECT " +
                                    "p.id_psicologo, " +
                                    "u.nombres, " +
                                    "u.apellidos " +

                                    "FROM PSICOLOGO p " +

                                    "INNER JOIN USUARIO u " +
                                    "ON p.id_usuario = u.id_usuario " +

                                    "WHERE p.estado = 'ACTIVO' " +
                                    "AND u.estado = 'ACTIVO' " +

                                    "ORDER BY u.nombres, u.apellidos";

                            try (
                                    Connection con =
                                            ConexionBD.getConexion();

                                    PreparedStatement ps =
                                            con.prepareStatement(sqlPsicologos);

                                    ResultSet rs =
                                            ps.executeQuery()
                            ) {

                                while (rs.next()) {

                        %>

                        <option value="<%= rs.getInt("id_psicologo") %>">

                            Psic.
                            <%= rs.getString("nombres") %>
                            <%= rs.getString("apellidos") %>

                        </option>

                        <%

                                }

                            } catch (SQLException e) {

                                e.printStackTrace();
                            }

                        %>

                    </select>

                </div>


                <!-- FECHA -->

                <div class="mb-3">

                    <label class="form-label fw-bold">
                        Fecha
                    </label>

                    <input
                            type="date"
                            name="fecha"
                            id="fecha"
                            class="form-control"
                            required>

                </div>


                <!-- HORA INICIO -->

                <div class="mb-3">

                    <label class="form-label fw-bold">
                        Hora de Inicio
                    </label>

                    <input
                            type="time"
                            name="horaInicio"
                            class="form-control"
                            required>

                </div>


                <!-- HORA FIN -->

                <div class="mb-3">

                    <label class="form-label fw-bold">
                        Hora de Fin
                    </label>

                    <input
                            type="time"
                            name="horaFin"
                            class="form-control"
                            required>

                </div>


                <!-- MODALIDAD -->

                <div class="mb-4">

                    <label class="form-label fw-bold">
                        Modalidad
                    </label>

                    <select
                            name="modalidad"
                            class="form-select"
                            required>

                        <option value="">
                            -- Selecciona una modalidad --
                        </option>

                        <option value="VIRTUAL">
                            VIRTUAL
                        </option>

                        <option value="PRESENCIAL">
                            PRESENCIAL
                        </option>

                        <option value="AMBAS">
                            AMBAS
                        </option>

                    </select>

                </div>


                <!-- BOTONES -->

                <div class="d-flex justify-content-between">

                    <a
                            href="AdminServlet"
                            class="btn btn-secondary">

                        Cancelar

                    </a>

                    <button
                            type="submit"
                            class="btn btn-primary fw-bold">

                        Guardar Horario

                    </button>

                </div>

            </form>

        </div>

    </div>

</div>


<script>

    // Evita seleccionar fechas anteriores a hoy

    const fechaInput = document.getElementById("fecha");

    const hoy = new Date();

    const anio = hoy.getFullYear();

    const mes =
            String(hoy.getMonth() + 1)
                    .padStart(2, "0");

    const dia =
            String(hoy.getDate())
                    .padStart(2, "0");

    fechaInput.min =
            anio + "-" + mes + "-" + dia;

</script>


<script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js">
</script>

</body>

</html>