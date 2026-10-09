package com.orientapsi.bean;

import com.orientapsi.dao.CitaDAO;
import com.orientapsi.model.Cita;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.util.List;

@Named
@RequestScoped
public class CitaBean {

    private final CitaDAO citaDAO = new CitaDAO();

    public List<Cita> getCitasPendientes() {
        return citaDAO.obtenerCitasPendientes();
    }

    public String aprobar(int idCita) {

        boolean exito =
                citaDAO.confirmarCita(idCita);

        if (exito) {
            return "admin?faces-redirect=true&cita=aprobada";
        }

        return "admin?faces-redirect=true&cita=error";
    }

    public String cancelar(int idCita) {

        boolean exito =
                citaDAO.cancelarCita(idCita);

        if (exito) {
            return "admin?faces-redirect=true&cita=cancelada";
        }

        return "admin?faces-redirect=true&cita=error";
    }
}