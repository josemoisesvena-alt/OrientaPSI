package com.orientapsi.model;

public class PsicologoOpcionDTO {

    private int idPsicologo;
    private String nombres;
    private String apellidos;


    public PsicologoOpcionDTO() {
    }


    public PsicologoOpcionDTO(
            int idPsicologo,
            String nombres,
            String apellidos
    ) {

        this.idPsicologo = idPsicologo;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }


    public int getIdPsicologo() {
        return idPsicologo;
    }

    public void setIdPsicologo(int idPsicologo) {
        this.idPsicologo = idPsicologo;
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


    public String getNombreCompleto() {
        return "Psic. " + nombres + " " + apellidos;
    }
}