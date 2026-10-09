package com.orientapsi.bean;

import com.orientapsi.dao.CitaDAO;
import com.orientapsi.dao.PsicologoDAO;
import com.orientapsi.model.Cita;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named("psicologoBean")
@RequestScoped
public class PsicologoBean {

    @Inject
    private LoginBean loginBean;


    private final CitaDAO citaDAO =
            new CitaDAO();

    private final PsicologoDAO psicologoDAO =
            new PsicologoDAO();


    // =========================================
    // CAMPOS DE REGISTRO
    // =========================================

    private String nombres;

    private String apellidos;

    private String correo;

    private String clave;

    private String colegiatura;

    private Integer experiencia;

    private String presentacion;

    private String modalidad;


    // =========================================
    // OBTENER CITAS DEL PSICÓLOGO LOGUEADO
    // =========================================
    public List<Cita> getMisCitas() {

        if (loginBean == null
                || loginBean.getUsuarioLogueado() == null) {

            return List.of();
        }


        if (!loginBean.isPsicologo()) {

            return List.of();
        }


        int idUsuario =
                loginBean
                        .getUsuarioLogueado()
                        .getIdUsuario();


        return citaDAO.obtenerCitasPorPsicologo(
                idUsuario
        );
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
    // REGISTRAR PSICÓLOGO
    // =========================================
    public String registrar() {

        // =========================================
        // VALIDAR ADMIN
        // =========================================

        if (loginBean == null
                || loginBean.getUsuarioLogueado() == null
                || !loginBean.isAdministrador()) {

            return "index?faces-redirect=true";
        }


        // =========================================
        // VALIDAR CAMPOS
        // =========================================

        if (nombres == null
                || nombres.trim().isEmpty()
                || apellidos == null
                || apellidos.trim().isEmpty()
                || correo == null
                || correo.trim().isEmpty()
                || clave == null
                || clave.trim().isEmpty()
                || colegiatura == null
                || colegiatura.trim().isEmpty()
                || experiencia == null
                || presentacion == null
                || presentacion.trim().isEmpty()
                || modalidad == null
                || modalidad.trim().isEmpty()) {

            return "registrar_psicologo"
                    + "?faces-redirect=true"
                    + "&resultado=campos";
        }


        // =========================================
        // VALIDAR CONTRASEÑA
        // =========================================

        if (clave.length() < 6) {

            return "registrar_psicologo"
                    + "?faces-redirect=true"
                    + "&resultado=clave";
        }


        // =========================================
        // VALIDAR EXPERIENCIA
        // =========================================

        if (experiencia < 0) {

            return "registrar_psicologo"
                    + "?faces-redirect=true"
                    + "&resultado=experiencia";
        }


        // =========================================
        // VALIDAR MODALIDAD
        // =========================================

        if (!modalidad.equals("VIRTUAL")
                && !modalidad.equals("PRESENCIAL")
                && !modalidad.equals("AMBAS")) {

            return "registrar_psicologo"
                    + "?faces-redirect=true"
                    + "&resultado=campos";
        }


        // =========================================
        // REGISTRAR EN DAO
        // =========================================

        String resultado =
                psicologoDAO.registrarPsicologo(
                        nombres,
                        apellidos,
                        correo,
                        clave,
                        colegiatura,
                        experiencia,
                        presentacion,
                        modalidad
                );


        // =========================================
        // RESULTADO
        // =========================================

        if ("success".equals(resultado)) {

            return "registrar_psicologo"
                    + "?faces-redirect=true"
                    + "&resultado=success";
        }


        if ("duplicado".equals(resultado)) {

            return "registrar_psicologo"
                    + "?faces-redirect=true"
                    + "&resultado=duplicado";
        }


        return "registrar_psicologo"
                + "?faces-redirect=true"
                + "&resultado=error";
    }


    // =========================================
    // GETTERS Y SETTERS
    // =========================================

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


    public String getColegiatura() {
        return colegiatura;
    }

    public void setColegiatura(String colegiatura) {
        this.colegiatura = colegiatura;
    }


    public Integer getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(Integer experiencia) {
        this.experiencia = experiencia;
    }


    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }


    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }
}