<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.orientapsi.model.Usuario" %>

<%
    Usuario usuario =
            (Usuario) session.getAttribute("usuarioLogueado");

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

    <title>OrientaPsi - Registrar Paciente</title>

    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
            rel="stylesheet">

</head>

<body class="bg-light">

<div
        class="container mt-5 mb-5"
        style="max-width: 650px;">

    <div class="card shadow-sm border-0">

        <div class="card-header bg-success text-white py-3">

            <h4 class="mb-0 fw-bold">

                Registrar Nuevo Paciente

            </h4>

        </div>


        <div class="card-body p-4">


            <!-- ========================= -->
            <!-- MENSAJES DE ERROR -->
            <!-- ========================= -->

            <% if ("campos".equals(error)) { %>

                <div class="alert alert-warning">

                    Completa todos los campos obligatorios.

                </div>

            <% } %>


            <% if ("clave".equals(error)) { %>

                <div class="alert alert-warning">

                    La contraseña debe tener
                    al menos 6 caracteres.

                </div>

            <% } %>


            <% if ("db".equals(error)) { %>

                <div class="alert alert-danger">

                    No se pudo registrar al paciente.

                    Revisa que el correo electrónico
                    no esté registrado previamente.

                </div>

            <% } %>


            <!-- ========================= -->
            <!-- FORMULARIO -->
            <!-- ========================= -->

            <form
                    action="PacienteServlet"
                    method="POST">


                <!-- NOMBRES -->

                <div class="mb-3">

                    <label class="form-label fw-bold">

                        Nombres

                    </label>

                    <input
                            type="text"
                            name="nombres"
                            class="form-control"
                            maxlength="80"
                            required>

                </div>


                <!-- APELLIDOS -->

                <div class="mb-3">

                    <label class="form-label fw-bold">

                        Apellidos

                    </label>

                    <input
                            type="text"
                            name="apellidos"
                            class="form-control"
                            maxlength="80"
                            required>

                </div>


                <!-- CORREO -->

                <div class="mb-3">

                    <label class="form-label fw-bold">

                        Correo Electrónico

                    </label>

                    <input
                            type="email"
                            name="correo"
                            class="form-control"
                            maxlength="120"
                            required>

                </div>


                <!-- CONTRASEÑA -->

                <div class="mb-3">

                    <label class="form-label fw-bold">

                        Contraseña

                    </label>

                    <input
                            type="password"
                            name="clave"
                            class="form-control"
                            minlength="6"
                            required>

                    <div class="form-text">

                        Mínimo 6 caracteres.

                    </div>

                </div>


                <!-- FECHA DE NACIMIENTO -->

                <div class="mb-4">

                    <label class="form-label fw-bold">

                        Fecha de Nacimiento

                    </label>

                    <input
                            type="date"
                            name="fechaNacimiento"
                            id="fechaNacimiento"
                            class="form-control"
                            required>

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
                            class="btn btn-success fw-bold">

                        Registrar Paciente

                    </button>

                </div>


            </form>

        </div>

    </div>

</div>


<script>

    // Evitar fechas futuras

    const fechaNacimiento =
            document.getElementById("fechaNacimiento");

    const hoy =
            new Date();

    const anio =
            hoy.getFullYear();

    const mes =
            String(hoy.getMonth() + 1)
                    .padStart(2, "0");

    const dia =
            String(hoy.getDate())
                    .padStart(2, "0");

    fechaNacimiento.max =
            anio + "-" + mes + "-" + dia;

</script>


<script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js">
</script>

</body>

</html>