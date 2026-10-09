package com.orientapsi.model;

public class HorarioDTO {

    private int idHorario;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private String modalidad;
    private String nombrePsicologo;
    private String apellidoPsicologo;

    public HorarioDTO() {
    }

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getNombrePsicologo() {
        return nombrePsicologo;
    }

    public void setNombrePsicologo(String nombrePsicologo) {
        this.nombrePsicologo = nombrePsicologo;
    }

    public String getApellidoPsicologo() {
        return apellidoPsicologo;
    }

    public void setApellidoPsicologo(String apellidoPsicologo) {
        this.apellidoPsicologo = apellidoPsicologo;
    }
}