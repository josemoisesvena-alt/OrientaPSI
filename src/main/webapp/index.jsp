<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="es">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1">

    <title>OrientaPsi - Centro Psicológico Mente Sana</title>

    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
            rel="stylesheet">

    <style>

        body {
            background-color: #f8f9fa;
        }

        .hero {
            background: linear-gradient(
                    135deg,
                    #0d6efd,
                    #6f42c1
            );

            color: white;
            padding: 80px 0;
        }

        .hero h1 {
            font-weight: 700;
        }

        .section-title {
            font-weight: 700;
        }

        .service-card {
            transition: transform 0.2s ease;
        }

        .service-card:hover {
            transform: translateY(-5px);
        }

        .login-card {
            border-radius: 15px;
        }

        footer {
            background-color: #212529;
            color: white;
        }

    </style>

</head>


<body>


<!-- ====================================================== -->
<!-- NAVEGACIÓN -->
<!-- ====================================================== -->

<nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top">

    <div class="container">

        <a
                class="navbar-brand fw-bold"
                href="#">

            OrientaPsi

        </a>


        <button
                class="navbar-toggler"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#menuPrincipal">

            <span class="navbar-toggler-icon"></span>

        </button>


        <div
                class="collapse navbar-collapse"
                id="menuPrincipal">

            <ul class="navbar-nav ms-auto">

                <li class="nav-item">

                    <a
                            class="nav-link"
                            href="#inicio">

                        Inicio

                    </a>

                </li>


                <li class="nav-item">

                    <a
                            class="nav-link"
                            href="#nosotros">

                        Nosotros

                    </a>

                </li>


                <li class="nav-item">

                    <a
                            class="nav-link"
                            href="#servicios">

                        Servicios

                    </a>

                </li>


                <li class="nav-item">

                    <a
                            class="nav-link"
                            href="#modalidades">

                        Modalidades

                    </a>

                </li>


                <li class="nav-item">

                    <a
                            class="nav-link"
                            href="#acceso">

                        Iniciar Sesión

                    </a>

                </li>

            </ul>

        </div>

    </div>

</nav>



<!-- ====================================================== -->
<!-- HERO / PRESENTACIÓN -->
<!-- ====================================================== -->

<section
        class="hero"
        id="inicio">

    <div class="container">

        <div class="row align-items-center">


            <div class="col-lg-7 mb-4 mb-lg-0">

                <h1 class="display-4 mb-3">

                    Centro Psicológico
                    Mente Sana S.A.C.

                </h1>


                <h4 class="mb-4">

                    Bienestar emocional,
                    acompañamiento profesional
                    y atención psicológica accesible.

                </h4>


                <p class="lead">

                    En OrientaPsi buscamos facilitar
                    el acceso a servicios de orientación
                    y atención psicológica mediante
                    un sistema moderno de gestión de citas.

                </p>


                <div class="mt-4">

                    <a
                            href="#acceso"
                            class="btn btn-light btn-lg me-2">

                        Iniciar Sesión

                    </a>


                    <a
                            href="registro_paciente_publico.jsp"
                            class="btn btn-outline-light btn-lg">

                        Crear Cuenta

                    </a>

                </div>

            </div>


            <div class="col-lg-5">

                <div
                        class="bg-white text-dark p-4 rounded shadow">

                    <h4 class="fw-bold text-primary">

                        Atención Psicológica

                    </h4>

                    <p class="mb-2">

                        Reserva tus citas de manera
                        rápida y sencilla.

                    </p>

                    <hr>

                    <p class="mb-1">

                        ✔ Atención personalizada

                    </p>

                    <p class="mb-1">

                        ✔ Psicólogos profesionales

                    </p>

                    <p class="mb-1">

                        ✔ Citas presenciales

                    </p>

                    <p class="mb-0">

                        ✔ Atención virtual

                    </p>

                </div>

            </div>

        </div>

    </div>

</section>



<!-- ====================================================== -->
<!-- NOSOTROS -->
<!-- ====================================================== -->

<section
        class="py-5 bg-white"
        id="nosotros">

    <div class="container">

        <div class="row align-items-center">

            <div class="col-lg-6">

                <h2 class="section-title mb-4">

                    ¿Quiénes somos?

                </h2>

                <p>

                    El Centro Psicológico Mente Sana S.A.C.
                    brinda orientación y acompañamiento
                    psicológico a personas que buscan
                    mejorar su bienestar emocional,
                    personal y familiar.

                </p>

                <p>

                    OrientaPsi facilita la comunicación
                    entre pacientes, psicólogos y
                    administradores mediante una plataforma
                    web que permite organizar horarios,
                    reservar citas y gestionar la atención.

                </p>

            </div>


            <div class="col-lg-6">

                <div class="card border-0 shadow-sm">

                    <div class="card-body p-4">

                        <h4 class="text-primary fw-bold">

                            Nuestro objetivo

                        </h4>

                        <p class="mb-0">

                            Ofrecer un entorno accesible,
                            organizado y confiable para que
                            los pacientes puedan encontrar
                            atención psicológica y reservar
                            una cita con el profesional
                            adecuado.

                        </p>

                    </div>

                </div>

            </div>

        </div>

    </div>

</section>



<!-- ====================================================== -->
<!-- SERVICIOS -->
<!-- ====================================================== -->

<section
        class="py-5"
        id="servicios">

    <div class="container">


        <div class="text-center mb-5">

            <h2 class="section-title">

                Nuestros Servicios

            </h2>

            <p class="text-muted">

                Atención orientada al bienestar
                psicológico y emocional.

            </p>

        </div>


        <div class="row g-4">


            <div class="col-md-4">

                <div
                        class="card service-card shadow-sm border-0 h-100">

                    <div class="card-body p-4">

                        <h4 class="text-primary">

                            Psicología Individual

                        </h4>

                        <p class="text-muted mb-0">

                            Espacio profesional para abordar
                            dificultades emocionales,
                            personales, familiares
                            o académicas.

                        </p>

                    </div>

                </div>

            </div>


            <div class="col-md-4">

                <div
                        class="card service-card shadow-sm border-0 h-100">

                    <div class="card-body p-4">

                        <h4 class="text-success">

                            Orientación Psicológica

                        </h4>

                        <p class="text-muted mb-0">

                            Acompañamiento para identificar
                            necesidades, desarrollar
                            estrategias y fortalecer
                            habilidades personales.

                        </p>

                    </div>

                </div>

            </div>


            <div class="col-md-4">

                <div
                        class="card service-card shadow-sm border-0 h-100">

                    <div class="card-body p-4">

                        <h4 class="text-primary">

                            Seguimiento

                        </h4>

                        <p class="text-muted mb-0">

                            Organización de sesiones
                            y seguimiento mediante
                            citas previamente programadas
                            con el especialista.

                        </p>

                    </div>

                </div>

            </div>


        </div>

    </div>

</section>



<!-- ====================================================== -->
<!-- MODALIDADES -->
<!-- ====================================================== -->

<section
        class="py-5 bg-white"
        id="modalidades">

    <div class="container">

        <div class="text-center mb-5">

            <h2 class="section-title">

                Modalidades de Atención

            </h2>

        </div>


        <div class="row justify-content-center g-4">


            <div class="col-md-5">

                <div class="card border-0 shadow-sm h-100">

                    <div class="card-body p-4 text-center">

                        <h4 class="text-primary">

                            Atención Presencial

                        </h4>

                        <p class="text-muted">

                            Sesiones realizadas de manera
                            presencial en el centro
                            psicológico previa reserva.

                        </p>

                    </div>

                </div>

            </div>


            <div class="col-md-5">

                <div class="card border-0 shadow-sm h-100">

                    <div class="card-body p-4 text-center">

                        <h4 class="text-success">

                            Atención Virtual

                        </h4>

                        <p class="text-muted">

                            Sesiones psicológicas mediante
                            herramientas virtuales según
                            disponibilidad del especialista.

                        </p>

                    </div>

                </div>

            </div>


        </div>

    </div>

</section>



<!-- ====================================================== -->
<!-- ACCESO AL SISTEMA -->
<!-- ====================================================== -->

<section
        class="py-5"
        id="acceso">

    <div class="container">


        <div class="text-center mb-4">

            <h2 class="section-title">

                Acceso a OrientaPsi

            </h2>

            <p class="text-muted">

                Ingresa con tus credenciales
                para gestionar tus citas.

            </p>

        </div>


        <div class="row justify-content-center">

            <div class="col-lg-5 col-md-7">


                <div
                        class="card login-card shadow border-0">

                    <div class="card-body p-4">


                        <div class="text-center mb-4">

                            <h3
                                    class="text-primary fw-bold">

                                OrientaPsi

                            </h3>

                            <p class="text-muted small">

                                Centro Psicológico
                                Mente Sana S.A.C.

                            </p>

                        </div>


                        <!-- =================================== -->
                        <!-- REGISTRO EXITOSO -->
                        <!-- =================================== -->

                        <%
                            if ("success".equals(
                                    request.getParameter("registro")
                            )) {
                        %>

                        <div
                                class="alert alert-success py-2">

                            Tu cuenta fue creada correctamente.
                            Ya puedes iniciar sesión.

                        </div>

                        <%
                            }
                        %>


                        <!-- =================================== -->
                        <!-- ERROR DE LOGIN -->
                        <!-- =================================== -->

                        <%
                            if (request.getParameter("error") != null) {
                        %>

                        <div
                                class="alert alert-danger py-2 small">

                            Credenciales incorrectas
                            o usuario inactivo.

                        </div>

                        <%
                            }
                        %>


                        <!-- =================================== -->
                        <!-- FORMULARIO LOGIN -->
                        <!-- =================================== -->

                        <form
                                action="LoginServlet"
                                method="POST">


                            <div class="mb-3">

                                <label
                                        for="correo"
                                        class="form-label">

                                    Correo Electrónico

                                </label>

                                <input
                                        type="email"
                                        class="form-control"
                                        id="correo"
                                        name="correo"
                                        required
                                        placeholder="ejemplo@correo.com">

                            </div>


                            <div class="mb-3">

                                <label
                                        for="clave"
                                        class="form-label">

                                    Contraseña

                                </label>

                                <input
                                        type="password"
                                        class="form-control"
                                        id="clave"
                                        name="clave"
                                        required
                                        placeholder="******">

                            </div>


                            <button
                                    type="submit"
                                    class="btn btn-primary w-100">

                                Ingresar al Sistema

                            </button>


                        </form>


                        <!-- =================================== -->
                        <!-- REGISTRO PACIENTE -->
                        <!-- =================================== -->

                        <hr class="my-4">


                        <div class="text-center">

                            <p class="text-muted mb-2">

                                ¿Aún no tienes una cuenta?

                            </p>


                            <a
                                    href="registro_paciente_publico.jsp"
                                    class="btn btn-outline-success w-100">

                                Crear cuenta como paciente

                            </a>

                        </div>


                    </div>

                </div>


            </div>

        </div>

    </div>

</section>



<!-- ====================================================== -->
<!-- INFORMACIÓN -->
<!-- ====================================================== -->

<section class="py-5 bg-white">

    <div class="container">

        <div class="row g-4">


            <div class="col-md-4">

                <h5 class="fw-bold">

                    OrientaPsi

                </h5>

                <p class="text-muted">

                    Plataforma para la gestión
                    digital de citas psicológicas.

                </p>

            </div>


            <div class="col-md-4">

                <h5 class="fw-bold">

                    Para pacientes

                </h5>

                <p class="text-muted">

                    Crea tu cuenta, revisa los
                    horarios disponibles y solicita
                    una cita con un especialista.

                </p>

            </div>


            <div class="col-md-4">

                <h5 class="fw-bold">

                    Para especialistas

                </h5>

                <p class="text-muted">

                    Consulta las citas confirmadas
                    y organiza tu atención de acuerdo
                    con tus horarios disponibles.

                </p>

            </div>


        </div>

    </div>

</section>



<!-- ====================================================== -->
<!-- FOOTER -->
<!-- ====================================================== -->

<footer class="py-4">

    <div class="container text-center">

        <p class="mb-1 fw-bold">

            Centro Psicológico Mente Sana S.A.C.

        </p>

        <p class="small mb-0">

            Sistema Web OrientaPsi

        </p>

    </div>

</footer>



<script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js">
</script>


</body>

</html>