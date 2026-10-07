<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String error = request.getParameter("error");
%>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1">

    <title>OrientaPsi - Crear Cuenta de Paciente</title>

    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
            rel="stylesheet">

    <style>

        body {
            background-color: #f8f9fa;
        }

        .register-card {
            border-radius: 15px;
        }

    </style>

</head>

<body>


<div
        class="container d-flex justify-content-center align-items-center"
        style="min-height: 100vh;">


    <div
            class="card shadow-sm border-0 register-card"
            style="width: 100%; max-width: 550px;">


        <div class="card-body p-4">


            <!-- ====================================== -->
            <!-- ENCABEZADO -->
            <!-- ====================================== -->

            <div class="text-center mb-4">

                <h2 class="fw-bold text-primary">

                    OrientaPsi

                </h2>

                <h5 class="fw-bold">

                    Crear Cuenta de Paciente

                </h5>

                <p class="text-muted">

                    Centro Psicológico Mente Sana S.A.C.

                </p>

            </div>


            <!-- ====================================== -->
            <!-- MENSAJES DE ERROR -->
            <!-- ====================================== -->

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

                    No se pudo crear la cuenta.

                    Revisa que el correo electrónico
                    no esté registrado previamente.

                </div>

            <% } %>


            <!-- ====================================== -->
            <!-- FORMULARIO -->
            <!-- ====================================== -->

            <form
                    action="RegistroPacienteServlet"
                    method="POST">


                <!-- NOMBRES -->

                <div class="mb-3">

                    <label
                            for="nombres"
                            class="form-label fw-bold">

                        Nombres

                    </label>

                    <input
                            type="text"
                            class="form-control"
                            id="nombres"
                            name="nombres"
                            maxlength="80"
                            placeholder="Ingrese sus nombres"
                            required>

                </div>


                <!-- APELLIDOS -->

                <div class="mb-3">

                    <label
                            for="apellidos"
                            class="form-label fw-bold">

                        Apellidos

                    </label>

                    <input
                            type="text"
                            class="form-control"
                            id="apellidos"
                            name="apellidos"
                            maxlength="80"
                            placeholder="Ingrese sus apellidos"
                            required>

                </div>


                <!-- CORREO -->

                <div class="mb-3">

                    <label
                            for="correo"
                            class="form-label fw-bold">

                        Correo Electrónico

                    </label>

                    <input
                            type="email"
                            class="form-control"
                            id="correo"
                            name="correo"
                            maxlength="120"
                            placeholder="ejemplo@correo.com"
                            required>

                </div>


                <!-- CONTRASEÑA -->

                <div class="mb-3">

                    <label
                            for="clave"
                            class="form-label fw-bold">

                        Contraseña

                    </label>

                    <input
                            type="password"
                            class="form-control"
                            id="clave"
                            name="clave"
                            minlength="6"
                            placeholder="Mínimo 6 caracteres"
                            required>

                    <div class="form-text">

                        La contraseña debe tener
                        al menos 6 caracteres.

                    </div>

                </div>


                <!-- FECHA DE NACIMIENTO -->

                <div class="mb-4">

                    <label
                            for="fechaNacimiento"
                            class="form-label fw-bold">

                        Fecha de Nacimiento

                    </label>

                    <input
                            type="date"
                            class="form-control"
                            id="fechaNacimiento"
                            name="fechaNacimiento"
                            required>

                </div>


                <!-- ====================================== -->
                <!-- BOTÓN CREAR CUENTA -->
                <!-- ====================================== -->

                <button
                        type="submit"
                        class="btn btn-success w-100 fw-bold">

                    Crear Cuenta

                </button>


                <!-- ====================================== -->
                <!-- VOLVER -->
                <!-- ====================================== -->

                <a
                        href="index.jsp"
                        class="btn btn-outline-secondary w-100 mt-2">

                    Volver al Inicio

                </a>


            </form>


            <hr class="my-4">


            <div class="text-center">

                <p class="text-muted mb-2">

                    ¿Ya tienes una cuenta?

                </p>

                <a
                        href="index.jsp#acceso"
                        class="text-decoration-none fw-bold">

                    Iniciar Sesión

                </a>

            </div>


        </div>

    </div>

</div>


<!-- ====================================== -->
<!-- VALIDAR FECHA -->
<!-- ====================================== -->

<script>

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