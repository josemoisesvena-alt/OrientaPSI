package com.orientapsi.model;

import java.sql.Timestamp;

public class Cita {

    private int idCita;
    private int idPaciente;

    private String motivo;
    private String estado;

    private Timestamp fechaHora;

    // Datos del psicólogo
    private String nombrePsicologo;
    private String apellidoPsicologo;

    // Datos del paciente
    private String nombrePaciente;
    private String apellidoPaciente;


    // =========================================
    // GETTERS Y SETTERS
    // =========================================

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }


    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }


    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }


    // =========================================
    // DATOS DEL PSICÓLOGO
    // =========================================

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


    // =========================================
    // DATOS DEL PACIENTE
    // =========================================

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }


    public String getApellidoPaciente() {
        return apellidoPaciente;
    }

    public void setApellidoPaciente(String apellidoPaciente) {
        this.apellidoPaciente = apellidoPaciente;
    }
}