<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.orientapsi.model.Usuario" %>
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
    <title>OrientaPsi - Registrar Psicólogo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container mt-5" style="max-width: 650px;">
        <div class="card shadow-sm border-0">
            <div class="card-header bg-primary text-white py-3">
                <h4 class="mb-0 fw-bold">Registrar Nuevo Psicólogo</h4>
            </div>
            <div class="card-body p-4">
                <form action="PsicologoServlet" method="POST">
                    <div class="mb-3">
                        <label class="form-label fw-bold">Nombres</label>
                        <input type="text" name="nombres" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Apellidos</label>
                        <input type="text" name="apellidos" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Correo Electrónico</label>
                        <input type="email" name="correo" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Contraseña</label>
                        <input type="password" name="password" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Número de Colegiatura</label>
                        <input type="text" name="colegiatura" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Años de Experiencia</label>
                        <input type="number" name="experiencia" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Presentación / Biografía</label>
                        <textarea name="presentacion" class="form-control" rows="3" required></textarea>
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
                        <button type="submit" class="btn btn-primary fw-bold">Registrar Psicólogo</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>