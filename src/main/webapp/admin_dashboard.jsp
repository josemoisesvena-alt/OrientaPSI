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

    // Recuperar la lista enviada por el AdminServlet
    List<Cita> listaCitasPendientes = (List<Cita>) request.getAttribute("listaCitasPendientes");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>OrientaPsi - Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <!-- Navegación -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
        <div class="container">
            <a class="navbar-brand fw-bold" href="#">OrientaPsi - Admin</a>
            <div class="d-flex align-items-center">
                <span class="navbar-text text-white me-3">
                    Admin: <strong><%= usuario.getNombres() %> <%= usuario.getApellidos() %></strong>
                </span>
                <a href="LogoutServlet" class="btn btn-outline-light btn-sm">Cerrar Sesión</a>
            </div>
        </div>
    </nav>

    <div class="container mb-5">
        <div class="p-4 bg-white rounded shadow-sm mb-4">
            <h3 class="fw-bold text-dark">Gestión General del Sistema</h3>
            <p class="text-muted mb-0">Módulo para la aprobación de citas, registro de especialistas y reportería.</p>
        </div>

        <!-- Botones de Acción / Módulos -->
        <div class="row g-3 mb-4">
            <div class="col-md-4">
                <div class="card shadow-sm border-0 h-100">
                    <div class="card-body">
                        <h5 class="fw-bold text-primary">Gestionar Especialistas</h5>
                        <p class="text-muted small">Registra nuevos psicólogos en el sistema y asigna sus credenciales de acceso.</p>
                        <a href="registrar_psicologo.jsp" class="btn btn-primary btn-sm">+ Registrar Psicólogo</a>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card shadow-sm border-0 h-100">
                    <div class="card-body">
                        <h5 class="fw-bold text-primary">Gestionar Horarios</h5>
                        <p class="text-muted small">Abre nuevos turnos disponibles para que los pacientes puedan seleccionarlos.</p>
                        <a href="crear_horario.jsp" class="btn btn-primary btn-sm">+ Crear Horario</a>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card shadow-sm border-0 h-100">
                    <div class="card-body">
                        <h5 class="fw-bold text-primary">Reportes Generales</h5>
                        <p class="text-muted small">Consulta métricas de atención, flujo de usuarios e historial de citas.</p>
                        <a href="#" class="btn btn-outline-secondary btn-sm disabled">Generar Reporte</a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Tabla de Solicitudes de Citas Pendientes -->
        <div class="card shadow-sm border-0" id="tablaCitas">
            <div class="card-header bg-white py-3">
                <h5 class="mb-0 fw-bold text-dark">Solicitudes de Citas Pendientes</h5>
            </div>
            <div class="card-body p-0">
                <table class="table table-hover mb-0 align-middle">
                    <thead class="table-light">
                        <tr>
                            <th>ID Cita</th>
                            <th>Fecha y Hora</th>
                            <th>Motivo</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            if (listaCitasPendientes != null && !listaCitasPendientes.isEmpty()) {
                                for (Cita c : listaCitasPendientes) {
                        %>
                                    <tr>
                                        <td>#<%= c.getIdCita() %></td>
                                        <td><%= c.getFechaHora() %></td>
                                        <td><%= c.getMotivo() %></td> <!-- Cambiado de getMotivoConsulta() a getMotivo() -->
                                        <td><span class="badge bg-warning text-dark">PENDIENTE</span></td>
                                        <td>
                                            <form action="AprobarCitaServlet" method="POST" style="display:inline;">
                                                <input type="hidden" name="idCita" value="<%= c.getIdCita() %>">
                                                <button type="submit" class="btn btn-success btn-sm">Aprobar</button>
                                            </form>
                                        </td>
                                    </tr>
                        <%
                                }
                            } else {
                        %>
                                <tr>
                                    <td colspan="5" class="text-center text-muted py-4">No hay citas pendientes por aprobar en este momento.</td>
                                </tr>
                        <%


                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>