package com.orientapsi.bean;

import com.orientapsi.dao.HorarioDAO;
import com.orientapsi.model.PsicologoOpcionDTO;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.List;

@Named("horarioBean")
@RequestScoped
public class HorarioBean {

    @Inject
    private LoginBean loginBean;


    private final HorarioDAO horarioDAO =
            new HorarioDAO();


    private int idPsicologo;

    private String fecha;

    private String horaInicio;

    private String horaFin;

    private String modalidad;


    // =========================================
    // VERIFICAR ACCESO DE ADMIN
    // =========================================
    public String verificarAcceso() {

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
    // LISTAR PSICÓLOGOS
    // =========================================
    public List<PsicologoOpcionDTO> getPsicologos() {

        return horarioDAO.obtenerPsicologosActivos();
    }


    // =========================================
    // GUARDAR HORARIO
    // =========================================
    public String guardar() {

        // =========================================
        // VALIDAR SESIÓN
        // =========================================

        if (loginBean == null
                || loginBean.getUsuarioLogueado() == null
                || !loginBean.isAdministrador()) {

            return "index?faces-redirect=true";
        }


        // =========================================
        // VALIDAR CAMPOS
        // =========================================

        if (idPsicologo <= 0
                || fecha == null
                || fecha.isBlank()
                || horaInicio == null
                || horaInicio.isBlank()
                || horaFin == null
                || horaFin.isBlank()
                || modalidad == null
                || modalidad.isBlank()) {

            return "crear_horario"
                    + "?faces-redirect=true"
                    + "&resultado=campos";
        }


        try {

            LocalDate fechaHorario =
                    LocalDate.parse(fecha);

            LocalTime inicio =
                    LocalTime.parse(horaInicio);

            LocalTime fin =
                    LocalTime.parse(horaFin);


            // =========================================
            // VALIDAR FECHA
            // =========================================

            if (fechaHorario.isBefore(
                    LocalDate.now()
            )) {

                return "crear_horario"
                        + "?faces-redirect=true"
                        + "&resultado=fecha";
            }


            // =========================================
            // VALIDAR HORAS
            // =========================================

            if (!inicio.isBefore(fin)) {

                return "crear_horario"
                        + "?faces-redirect=true"
                        + "&resultado=horas";
            }


            // =========================================
            // VALIDAR PSICÓLOGO
            // =========================================

            if (!horarioDAO.psicologoExisteActivo(
                    idPsicologo
            )) {

                return "crear_horario"
                        + "?faces-redirect=true"
                        + "&resultado=psicologo";
            }


            // =========================================
            // VALIDAR MODALIDAD
            // =========================================

            if (!modalidad.equals("VIRTUAL")
                    && !modalidad.equals("PRESENCIAL")
                    && !modalidad.equals("AMBAS")) {

                return "crear_horario"
                        + "?faces-redirect=true"
                        + "&resultado=campos";
            }


            // =========================================
            // VALIDAR CRUCE
            // =========================================

            boolean existeCruce =
                    horarioDAO.existeCruceHorario(
                            idPsicologo,
                            fechaHorario,
                            inicio,
                            fin
                    );


            if (existeCruce) {

                return "crear_horario"
                        + "?faces-redirect=true"
                        + "&resultado=duplicado";
            }


            // =========================================
            // GUARDAR
            // =========================================

            boolean registrado =
                    horarioDAO.registrarHorario(
                            idPsicologo,
                            fechaHorario,
                            inicio,
                            fin,
                            modalidad
                    );


            if (registrado) {

                return "crear_horario"
                        + "?faces-redirect=true"
                        + "&resultado=success";
            }


            return "crear_horario"
                    + "?faces-redirect=true"
                    + "&resultado=error";


        } catch (Exception e) {

            System.err.println(
                    "Error en HorarioBean: "
                            + e.getMessage()
            );

            e.printStackTrace();


            return "crear_horario"
                    + "?faces-redirect=true"
                    + "&resultado=error";
        }
    }


    // =========================================
    // GETTERS Y SETTERS
    // =========================================

    public int getIdPsicologo() {
        return idPsicologo;
    }

    public void setIdPsicologo(
            int idPsicologo
    ) {

        this.idPsicologo = idPsicologo;
    }


    public String getFecha() {
        return fecha;
    }

    public void setFecha(
            String fecha
    ) {

        this.fecha = fecha;
    }


    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(
            String horaInicio
    ) {

        this.horaInicio = horaInicio;
    }


    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(
            String horaFin
    ) {

        this.horaFin = horaFin;
    }


    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(
            String modalidad
    ) {

        this.modalidad = modalidad;
    }
}