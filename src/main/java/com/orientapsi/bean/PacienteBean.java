package com.orientapsi.bean;

import com.orientapsi.dao.CitaDAO;
import com.orientapsi.dao.HorarioDAO;
import com.orientapsi.dao.PacienteDAO;

import com.orientapsi.model.Cita;
import com.orientapsi.model.HorarioDTO;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named("pacienteBean")
@RequestScoped
public class PacienteBean {
    private String nombres;
    private String apellidos;
    private String correo;
    private String clave;
    private String fechaNacimiento;
    @Inject
    private LoginBean loginBean;

    private final HorarioDAO horarioDAO =
            new HorarioDAO();

    private final PacienteDAO pacienteDAO =
            new PacienteDAO();

    private final CitaDAO citaDAO =
            new CitaDAO();

    private String motivo;


    // =========================================
    // OBTENER HORARIOS DISPONIBLES
    // =========================================
    public List<HorarioDTO> getHorariosDisponibles() {

        return horarioDAO.obtenerHorariosDisponibles();
    }


    // =========================================
    // OBTENER MIS CITAS
    // =========================================
    public List<Cita> getMisCitas() {

        if (loginBean == null
                || loginBean.getUsuarioLogueado() == null) {

            return List.of();
        }


        return citaDAO.obtenerCitasPorUsuario(
                loginBean
                        .getUsuarioLogueado()
                        .getIdUsuario()
        );
    }


    // =========================================
    // RESERVAR CITA
    // =========================================
    public String reservar(int idHorario) {

        System.out.println(
                "======================================"
        );

        System.out.println(
                "ID HORARIO RECIBIDO = "
                        + idHorario
        );

        System.out.println(
                "MOTIVO = "
                        + motivo
        );

        System.out.println(
                "======================================"
        );


        // =========================================
        // VALIDAR SESIÓN
        // =========================================
        if (loginBean == null
                || loginBean.getUsuarioLogueado() == null) {

            return "index?faces-redirect=true";
        }


        // =========================================
        // VALIDAR QUE SEA PACIENTE
        // =========================================
        if (!loginBean.isPaciente()) {

            return "index?faces-redirect=true";
        }


        // =========================================
        // VALIDAR ID DEL HORARIO
        // =========================================
        if (idHorario <= 0) {

            System.out.println(
                    "ERROR: ID de horario inválido."
            );

            return "paciente?faces-redirect=true&reserva=error";
        }


        // =========================================
        // VALIDAR MOTIVO
        // =========================================
        if (motivo == null
                || motivo.trim().isEmpty()) {

            System.out.println(
                    "ERROR: El motivo está vacío."
            );

            return "paciente?faces-redirect=true&reserva=error";
        }


        motivo = motivo.trim();


        // =========================================
        // VALIDAR LONGITUD
        // motivo_consulta VARCHAR(250)
        // =========================================
        if (motivo.length() > 250) {

            System.out.println(
                    "ERROR: El motivo supera los 250 caracteres."
            );

            return "paciente?faces-redirect=true&reserva=error";
        }


        // =========================================
        // OBTENER ID DEL USUARIO
        // =========================================
        int idUsuario =
                loginBean
                        .getUsuarioLogueado()
                        .getIdUsuario();


        System.out.println(
                "ID USUARIO = "
                        + idUsuario
        );


        // =========================================
        // RESERVAR EN BASE DE DATOS
        // =========================================
        String resultado =
                pacienteDAO.reservarCita(
                        idUsuario,
                        idHorario,
                        motivo
                );


        System.out.println(
                "RESULTADO DAO = "
                        + resultado
        );


        // =========================================
        // RESERVA EXITOSA
        // =========================================
        if ("success".equalsIgnoreCase(resultado)) {

            motivo = null;

            System.out.println(
                    "RESERVA REALIZADA CORRECTAMENTE."
            );

            return "paciente?faces-redirect=true&reserva=success";
        }


        // =========================================
        // HORARIO NO DISPONIBLE
        // =========================================
        if ("ocupado".equalsIgnoreCase(resultado)) {

            System.out.println(
                    "EL HORARIO NO ESTÁ DISPONIBLE."
            );

            return "paciente?faces-redirect=true&reserva=ocupado";
        }


        // =========================================
        // ERROR GENERAL
        // =========================================
        System.out.println(
                "ERROR GENERAL EN LA RESERVA."
        );

        return "paciente?faces-redirect=true&reserva=error";
    }
    // =========================================
// REGISTRO PÚBLICO DEL PACIENTE
// =========================================
    public String registrarPublico() {

        if (nombres == null
                || nombres.trim().isEmpty()
                || apellidos == null
                || apellidos.trim().isEmpty()
                || correo == null
                || correo.trim().isEmpty()
                || clave == null
                || clave.trim().isEmpty()
                || fechaNacimiento == null
                || fechaNacimiento.trim().isEmpty()) {

            return "registro_paciente_publico"
                    + "?faces-redirect=true"
                    + "&resultado=campos";
        }


        if (clave.length() < 6) {

            return "registro_paciente_publico"
                    + "?faces-redirect=true"
                    + "&resultado=clave";
        }


        try {

            java.time.LocalDate fecha =
                    java.time.LocalDate.parse(
                            fechaNacimiento
                    );


            if (fecha.isAfter(
                    java.time.LocalDate.now()
            )) {

                return "registro_paciente_publico"
                        + "?faces-redirect=true"
                        + "&resultado=fecha";
            }


            String resultado =
                    pacienteDAO.registrarPacienteAdmin(
                            nombres,
                            apellidos,
                            correo,
                            clave,
                            fecha
                    );


            if ("success".equalsIgnoreCase(resultado)) {

                return "registro_paciente_publico"
                        + "?faces-redirect=true"
                        + "&resultado=success";
            }


            if ("duplicado".equalsIgnoreCase(resultado)) {

                return "registro_paciente_publico"
                        + "?faces-redirect=true"
                        + "&resultado=duplicado";
            }


            return "registro_paciente_publico"
                    + "?faces-redirect=true"
                    + "&resultado=error";


        } catch (Exception e) {

            e.printStackTrace();

            return "registro_paciente_publico"
                    + "?faces-redirect=true"
                    + "&resultado=error";
        }
    }

    // =========================================
    // GETTER DEL MOTIVO
    // =========================================
    public String getMotivo() {

        return motivo;
    }


    // =========================================
    // SETTER DEL MOTIVO
    // =========================================
    public void setMotivo(String motivo) {

        this.motivo = motivo;
    }
    // =========================================
// VERIFICAR ACCESO DE ADMIN
// =========================================
    public String verificarAccesoAdmin() {

        if (loginBean == null
                || loginBean.getUsuarioLogueado() == null) {

            return "index?faces-redirect=true";
        }

        if (!loginBean.isAdministrador()) {

            return "index?faces-redirect=true";
        }

        return null;
    }
    // =========================================
// REGISTRAR PACIENTE
// =========================================
    public String registrarPaciente() {

        if (loginBean == null
                || loginBean.getUsuarioLogueado() == null
                || !loginBean.isAdministrador()) {

            return "index?faces-redirect=true";
        }


        if (nombres == null
                || nombres.trim().isEmpty()
                || apellidos == null
                || apellidos.trim().isEmpty()
                || correo == null
                || correo.trim().isEmpty()
                || clave == null
                || clave.trim().isEmpty()
                || fechaNacimiento == null
                || fechaNacimiento.trim().isEmpty()) {

            return "registrar_paciente"
                    + "?faces-redirect=true"
                    + "&resultado=campos";
        }


        if (clave.length() < 6) {

            return "registrar_paciente"
                    + "?faces-redirect=true"
                    + "&resultado=clave";
        }


        try {

            java.time.LocalDate fecha =
                    java.time.LocalDate.parse(
                            fechaNacimiento
                    );


            if (fecha.isAfter(
                    java.time.LocalDate.now()
            )) {

                return "registrar_paciente"
                        + "?faces-redirect=true"
                        + "&resultado=fecha";
            }


            String resultado =
                    pacienteDAO.registrarPacienteAdmin(
                            nombres,
                            apellidos,
                            correo,
                            clave,
                            fecha
                    );


            if ("success".equalsIgnoreCase(resultado)) {

                return "registrar_paciente"
                        + "?faces-redirect=true"
                        + "&resultado=success";
            }


            if ("duplicado".equalsIgnoreCase(resultado)) {

                return "registrar_paciente"
                        + "?faces-redirect=true"
                        + "&resultado=duplicado";
            }


            return "registrar_paciente"
                    + "?faces-redirect=true"
                    + "&resultado=error";


        } catch (Exception e) {

            e.printStackTrace();

            return "registrar_paciente"
                    + "?faces-redirect=true"
                    + "&resultado=error";
        }
    }
    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }


    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }


    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }


    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }


    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(
            String fechaNacimiento
    ) {

        this.fechaNacimiento =
                fechaNacimiento;
    }
}