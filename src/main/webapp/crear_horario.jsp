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
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>OrientaPsi - Crear Horario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container mt-5" style="max-width: 600px;">
        <div class="card shadow-sm border-0">
            <div class="card-header bg-primary text-white py-3">
                <h4 class="mb-0 fw-bold">Crear Nuevo Horario</h4>
            </div>
            <div class="card-body p-4">
                <form action="HorarioServlet" method="POST">
                    <div class="mb-3">
                        <label class="form-label fw-bold">Psicólogo</label>
                        <select name="idPsicologo" class="form-select" required>
                            <option value="">-- Selecciona un psicólogo --</option>
                            <%
                                try (Connection con = ConexionBD.getConexion();
                                     PreparedStatement ps = con.prepareStatement(
                                         "SELECT p.id_psicologo, u.nombres, u.apellidos FROM PSICOLOGO p INNER JOIN USUARIO u ON p.id_usuario = u.id_usuario")) {
                                    try (ResultSet rs = ps.executeQuery()) {
                                        while (rs.next()) {
                            %>
                                            <option value="<%= rs.getInt("id_psicologo") %>">
                                                Psic. <%= rs.getString("nombres") %> <%= rs.getString("apellidos") %>
                                            </option>
                            <%
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            %>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Fecha</label>
                        <input type="date" name="fecha" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Hora de Inicio</label>
                        <input type="time" name="horaInicio" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Hora de Fin</label>
                        <input type="time" name="horaFin" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Modalidad</label>
                        <select name="modalidad" class="form-select" required>
                            <option value="VIRTUAL">VIRTUAL</option>
                            <option value="PRESENCIAL">PRESENCIAL</option>
                            <option value="AMBAS">AMBAS</option>
                        </select>
                    </div>
                    <div class="d-flex justify-content-between">
                        <a href="AdminServlet" class="btn btn-secondary">Cancelar</a>
                        <button type="submit" class="btn btn-primary fw-bold">Guardar Horario</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>