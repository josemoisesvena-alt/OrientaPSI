<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>OrientaPsi - Iniciar Sesión</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light d-flex align-items-center vh-100">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-4">
                <div class="card shadow-sm border-0">
                    <div class="card-body p-4 text-center">
                        <h3 class="mb-3 text-primary font-weight-bold">OrientaPsi</h3>
                        <p class="text-muted small mb-4">Centro Psicológico Mente Sana S.A.C.</p>

                        <% if (request.getParameter("error") != null) { %>
                            <div class="alert alert-danger py-2 small" role="alert">
                                Credenciales incorrectas o usuario inactivo.
                            </div>
                        <% } %>

                        <form action="LoginServlet" method="POST">
                            <div class="mb-3 text-start">
                                <label for="correo" class="form-label small font-weight-bold">Correo Electrónico</label>
                                <input type="email" class="form-control" id="correo" name="correo" required placeholder="ejemplo@correo.com">
                            </div>
                            <div class="mb-3 text-start">
                                <label for="clave" class="form-label small font-weight-bold">Contraseña</label>
                                <input type="password" class="form-control" id="clave" name="clave" required placeholder="******">
                            </div>
                            <button type="submit" class="btn btn-primary w-100 mt-2">Ingresar al Sistema</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>