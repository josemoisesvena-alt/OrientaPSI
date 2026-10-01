<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.orientapsi.model.Usuario" %>
<%@ page import="com.orientapsi.config.ConexionBD" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    if (usuario == null || usuario.getIdRol() != 2) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>OrientaPsi - Panel del Psicólogo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <!-- Navegación -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
        <div class="container">
            <a class="navbar-brand fw-bold" href="#">OrientaPsi</a>
            <div class="d-flex align-items-center">
                <span class="navbar-text text-white me-3">
                    Psic. <strong><%= usuario.getNombres() %> <%= usuario.getApellidos() %></strong>
                </span>
                <a href="LogoutServlet" class="btn btn-outline-light btn-sm">Cerrar Sesión</a>
            </div>
        </div>
    </nav>

    <div class="container mb-5">
        <div class="p-4 bg-white rounded shadow-sm mb-4">
            <h3 class="fw-bold text-primary">Mi Agenda Profesional</h3>
            <p class="text-muted mb-0">Consulta las citas confirmadas y los pacientes asignados a tus horarios.</p>
        </div>

        <!-- Tabla de Citas Asignadas -->
        <div class="card shadow-sm border-0">
            <div class="card-header bg-white py-3">
                <h5 class="mb-0 fw-bold text-primary">Citas Confirmadas</h5>
            </div>
            <div class="card-body p-0">
                <table class="table table-hover mb-0 align-middle">
                    <thead class="table-light">
                        <tr>
                            <th># Cita</th>
                            <th>Fecha y Hora</th>
                            <th>Paciente</th>
                            <th>Motivo</th>
                            <th>Modalidad</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            try (Connection con = ConexionBD.getConexion();
                                 PreparedStatement ps = con.prepareStatement(
                                     "SELECT c.id_cita, h.fecha, h.hora_inicio, h.modalidad, c.motivo_consulta, " +
                                     "u.nombres AS p_nombres, u.apellidos AS p_apellidos " +
                                     "FROM CITA c " +
                                     "INNER JOIN HORARIO h ON c.id_horario = h.id_horario " +
                                     "INNER JOIN PACIENTE p ON c.id_paciente = p.id_paciente " +
                                     "INNER JOIN USUARIO u ON p.id_usuario = u.id_usuario " +
                                     "WHERE h.id_psicologo = ? AND c.estado = 'CONFIRMADA'")) {

                                ps.setInt(1, usuario.getIdUsuario());
                                try (ResultSet rs = ps.executeQuery()) {
                                    boolean hayCitas = false;
                                    while (rs.next()) {
                                        hayCitas = true;
                        %>
                                        <tr>
                                            <td>#<%= rs.getInt("id_cita") %></td>
                                            <td><%= rs.getString("fecha") %> <%= rs.getString("hora_inicio") %></td>
                                            <td><%= rs.getString("p_nombres") %> <%= rs.getString("p_apellidos") %></td>
                                            <td><%= rs.getString("motivo_consulta") %></td>
                                            <td><span class="badge bg-info text-dark"><%= rs.getString("modalidad") %></span></td>
                                        </tr>
                        <%
                                    }
                                    if (!hayCitas) {
                        %>
                                        <tr>
                                            <td colspan="5" class="text-center text-muted py-4">No tienes citas confirmadas en este momento.</td>
                                        </tr>
                        <%
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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