<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.orientapsi.model.Cita" %>
<%@ page import="com.orientapsi.model.Usuario" %>

<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

    if (usuario == null || usuario.getIdRol() != 1) {
        response.sendRedirect("index.jsp");
        return;
    }

    List<Cita> listaCitasPendientes =
            (List<Cita>) request.getAttribute("listaCitasPendientes");

    String estadoCita = request.getParameter("cita");
%>

<!DOCTYPE html>
<html lang="es">

<head>

    <meta charset="UTF-8">

    <title>OrientaPsi - Admin</title>

    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
            rel="stylesheet">

</head>

<body class="bg-light">


<!-- ====================================== -->
<!-- NAVEGACIÓN -->
<!-- ====================================== -->

<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">

    <div class="container">

        <a
                class="navbar-brand fw-bold"
                href="AdminServlet">

            OrientaPsi - Admin

        </a>


        <div class="d-flex align-items-center">

            <span class="navbar-text text-white me-3">

                Admin:

                <strong>
                    <%= usuario.getNombres() %>
                    <%= usuario.getApellidos() %>
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


    <!-- ====================================== -->
    <!-- ENCABEZADO -->
    <!-- ====================================== -->

    <div class="p-4 bg-white rounded shadow-sm mb-4">

        <h3 class="fw-bold text-dark">

            Gestión General del Sistema

        </h3>

        <p class="text-muted mb-0">

            Módulo para la gestión de citas,
            pacientes, especialistas y horarios.

        </p>

    </div>



    <!-- ====================================== -->
    <!-- MENSAJES DE CITAS -->
    <!-- ====================================== -->

    <% if ("aprobada".equals(estadoCita)) { %>

        <div class="alert alert-success alert-dismissible fade show">

            La cita fue confirmada correctamente.

            <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    <% } %>


    <% if ("cancelada".equals(estadoCita)) { %>

        <div class="alert alert-success alert-dismissible fade show">

            La cita fue cancelada correctamente
            y el horario volvió a estar disponible.

            <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    <% } %>


    <% if ("noDisponible".equals(estadoCita)) { %>

        <div class="alert alert-warning alert-dismissible fade show">

            La cita ya fue procesada,
            está cancelada o no existe.

            <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    <% } %>


    <% if ("accionInvalida".equals(estadoCita)) { %>

        <div class="alert alert-warning alert-dismissible fade show">

            La acción solicitada no es válida.

            <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    <% } %>


    <% if ("error".equals(estadoCita)) { %>

        <div class="alert alert-danger alert-dismissible fade show">

            Ocurrió un error al procesar la cita.

            <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    <% } %>



    <!-- ====================================== -->
    <!-- MENSAJE HORARIO -->
    <!-- ====================================== -->

    <% if ("success".equals(request.getParameter("horario"))) { %>

        <div class="alert alert-success alert-dismissible fade show">

            El horario fue creado correctamente.

            <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    <% } %>



    <!-- ====================================== -->
    <!-- MENSAJE PSICÓLOGO -->
    <!-- ====================================== -->

    <% if ("success".equals(request.getParameter("psicologo"))) { %>

        <div class="alert alert-success alert-dismissible fade show">

            El psicólogo fue registrado correctamente.

            <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    <% } %>



    <!-- ====================================== -->
    <!-- MENSAJE PACIENTE -->
    <!-- ====================================== -->

    <% if ("success".equals(request.getParameter("paciente"))) { %>

        <div class="alert alert-success alert-dismissible fade show">

            El paciente fue registrado correctamente.

            <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    <% } %>



    <!-- ====================================== -->
    <!-- MÓDULOS -->
    <!-- ====================================== -->

    <div class="row g-3 mb-4">


        <!-- PSICÓLOGOS -->

        <div class="col-md-3">

            <div class="card shadow-sm border-0 h-100">

                <div class="card-body">

                    <h5 class="fw-bold text-primary">

                        Gestionar Especialistas

                    </h5>

                    <p class="text-muted small">

                        Registra nuevos psicólogos
                        y crea sus credenciales de acceso.

                    </p>

                    <a
                            href="registrar_psicologo.jsp"
                            class="btn btn-primary btn-sm">

                        + Registrar Psicólogo

                    </a>

                </div>

            </div>

        </div>



        <!-- PACIENTES -->

        <div class="col-md-3">

            <div class="card shadow-sm border-0 h-100">

                <div class="card-body">

                    <h5 class="fw-bold text-success">

                        Gestionar Pacientes

                    </h5>

                    <p class="text-muted small">

                        Registra nuevos pacientes
                        y crea sus credenciales de acceso.

                    </p>

                    <a
                            href="registrar_paciente.jsp"
                            class="btn btn-success btn-sm">

                        + Registrar Paciente

                    </a>

                </div>

            </div>

        </div>



        <!-- HORARIOS -->

        <div class="col-md-3">

            <div class="card shadow-sm border-0 h-100">

                <div class="card-body">

                    <h5 class="fw-bold text-primary">

                        Gestionar Horarios

                    </h5>

                    <p class="text-muted small">

                        Abre nuevos turnos disponibles
                        para los pacientes.

                    </p>

                    <a
                            href="crear_horario.jsp"
                            class="btn btn-primary btn-sm">

                        + Crear Horario

                    </a>

                </div>

            </div>

        </div>



        <!-- REPORTES -->

        <div class="col-md-3">

            <div class="card shadow-sm border-0 h-100">

                <div class="card-body">

                    <h5 class="fw-bold text-primary">

                        Reportes Generales

                    </h5>

                    <p class="text-muted small">

                        Consulta métricas de atención
                        e historial de citas.

                    </p>

                    <button
                            class="btn btn-outline-secondary btn-sm"
                            disabled>

                        Generar Reporte

                    </button>

                </div>

            </div>

        </div>


    </div>



    <!-- ====================================== -->
    <!-- CITAS PENDIENTES -->
    <!-- ====================================== -->

    <div
            class="card shadow-sm border-0"
            id="tablaCitas">


        <div class="card-header bg-white py-3">

            <h5 class="mb-0 fw-bold text-dark">

                Solicitudes de Citas Pendientes

            </h5>

        </div>


        <div class="card-body p-0">


            <div class="table-responsive">


                <table class="table table-hover mb-0 align-middle">


                    <thead class="table-light">

                    <tr>

                        <th>ID Cita</th>

                        <th>Fecha y Hora</th>

                        <th>Psicólogo</th>

                        <th>Paciente</th>

                        <th>Motivo</th>

                        <th>Estado</th>

                        <th>Acciones</th>

                    </tr>

                    </thead>


                    <tbody>


                    <%

                        if (listaCitasPendientes != null
                                && !listaCitasPendientes.isEmpty()) {

                            for (Cita c : listaCitasPendientes) {

                    %>


                    <tr>


                        <!-- ID CITA -->

                        <td>

                            #<%= c.getIdCita() %>

                        </td>



                        <!-- FECHA Y HORA -->

                        <td>

                            <%= c.getFechaHora() %>

                        </td>



                        <!-- PSICÓLOGO -->

                        <td>

                            Psic.

                            <strong>

                                <%= c.getNombrePsicologo() %>
                                <%= c.getApellidoPsicologo() %>

                            </strong>

                        </td>



                        <!-- PACIENTE -->

                        <td>

                            <strong>

                                <%= c.getNombrePaciente() %>
                                <%= c.getApellidoPaciente() %>

                            </strong>

                        </td>



                        <!-- MOTIVO -->

                        <td>

                            <%= c.getMotivo() %>

                        </td>



                        <!-- ESTADO -->

                        <td>

                            <span class="badge bg-warning text-dark">

                                PENDIENTE

                            </span>

                        </td>



                        <!-- ACCIONES -->

                        <td>


                            <!-- APROBAR -->

                            <form
                                    action="CitaServlet"
                                    method="POST"
                                    class="d-inline">

                                <input
                                        type="hidden"
                                        name="idCita"
                                        value="<%= c.getIdCita() %>">

                                <input
                                        type="hidden"
                                        name="accion"
                                        value="CONFIRMADA">

                                <button
                                        type="submit"
                                        class="btn btn-success btn-sm">

                                    Aprobar

                                </button>

                            </form>



                            <!-- CANCELAR -->

                            <form
                                    action="CitaServlet"
                                    method="POST"
                                    class="d-inline">

                                <input
                                        type="hidden"
                                        name="idCita"
                                        value="<%= c.getIdCita() %>">

                                <input
                                        type="hidden"
                                        name="accion"
                                        value="CANCELADA">

                                <button
                                        type="submit"
                                        class="btn btn-danger btn-sm"
                                        onclick="return confirm('¿Seguro que deseas cancelar esta cita?');">

                                    Cancelar

                                </button>

                            </form>


                        </td>


                    </tr>


                    <%

                            }

                        } else {

                    %>


                    <tr>

                        <td
                                colspan="7"
                                class="text-center text-muted py-4">

                            No hay citas pendientes
                            por aprobar en este momento.

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