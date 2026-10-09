package com.orientapsi.bean;

import com.orientapsi.dao.UsuarioDAO;
import com.orientapsi.model.Usuario;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String correo;
    private String clave;

    private Usuario usuarioLogueado;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();


    // =========================================
    // INICIAR SESIÓN
    // =========================================
    public String iniciarSesion() {

        if (correo == null || correo.trim().isEmpty()
                || clave == null || clave.trim().isEmpty()) {

            return "index?faces-redirect=true";
        }

        usuarioLogueado = usuarioDAO.validarLogin(
                correo.trim().toLowerCase(),
                clave
        );

        if (usuarioLogueado == null) {

            clave = null;

            return "index?faces-redirect=true";
        }


        int rol = usuarioLogueado.getIdRol();


        if (rol == 1) {

            limpiarCredenciales();

            return "admin?faces-redirect=true";

        } else if (rol == 2) {

            limpiarCredenciales();

            return "psicologo?faces-redirect=true";

        } else if (rol == 3) {

            limpiarCredenciales();

            return "paciente?faces-redirect=true";

        }


        usuarioLogueado = null;

        limpiarCredenciales();

        return "index?faces-redirect=true";
    }


    // =========================================
    // CERRAR SESIÓN
    // =========================================
    public String cerrarSesion() {

        usuarioLogueado = null;

        correo = null;
        clave = null;

        return "index?faces-redirect=true";
    }


    // =========================================
    // LIMPIAR CREDENCIALES
    // =========================================
    private void limpiarCredenciales() {

        correo = null;
        clave = null;
    }


    // =========================================
    // VALIDAR SI HAY SESIÓN
    // =========================================
    public boolean isSesionActiva() {

        return usuarioLogueado != null;
    }


    // =========================================
    // VALIDACIÓN DE ROLES
    // =========================================
    public boolean isAdministrador() {

        return usuarioLogueado != null
                && usuarioLogueado.getIdRol() == 1;
    }


    public boolean isPsicologo() {

        return usuarioLogueado != null
                && usuarioLogueado.getIdRol() == 2;
    }


    public boolean isPaciente() {

        return usuarioLogueado != null
                && usuarioLogueado.getIdRol() == 3;
    }


    // =========================================
    // GETTERS Y SETTERS
    // =========================================

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


    public Usuario getUsuarioLogueado() {

        return usuarioLogueado;
    }


    public void setUsuarioLogueado(Usuario usuarioLogueado) {

        this.usuarioLogueado = usuarioLogueado;
    }
}