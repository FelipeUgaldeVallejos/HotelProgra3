package org.example.hotelgui.logica;

import org.example.hotelgui.datos.ReservacionDatos;
import org.example.hotelgui.model.Reservacion;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/** CRUD para Reservacion usando una base de datos mySql como almacenamiento. */
public class ReservacionLogica {

    private ReservacionDatos store = new ReservacionDatos();

    // --------- Lectura ---------

    public List<Reservacion> findAll() throws SQLException {
        return store.findAll();
    }

    public Reservacion findById(int id) throws SQLException {
        return store.findById(id);
    }

    // --------- Escritura ---------

    public Reservacion create(Reservacion nueva) throws SQLException {
        validarNueva(nueva);
        return store.insert(nueva);
    }

    public Reservacion update(Reservacion reservacion) throws SQLException {
        if (reservacion == null || reservacion.getIdReservacion() <= 0)
            throw new IllegalArgumentException("La reservación a actualizar requiere un ID válido.");
        validarCampos(reservacion);
        return store.update(reservacion);
    }

    public boolean deleteById(int id) throws SQLException {
        if (id <= 0) return false;
        return store.delete(id) > 0;
    }

    // --------- Helpers ---------

    private void validarNueva(Reservacion r) {
        if (r == null) throw new IllegalArgumentException("Reservación nula.");
        validarCampos(r);
    }

    private void validarCampos(Reservacion r) {
        if (r.getHabitacion() == null || r.getHabitacion().getId() <= 0)
            throw new IllegalArgumentException("La habitación es obligatoria.");
        if (r.getCliente() == null || r.getCliente().getId() <= 0)
            throw new IllegalArgumentException("El cliente es obligatorio.");
        if (r.getFechaLlegada() == null)
            throw new IllegalArgumentException("La fecha de llegada es obligatoria.");
        if (r.getFechaLlegada().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("La fecha de llegada no puede ser en el pasado.");
        if (r.getCantidadNoches() <= 0)
            throw new IllegalArgumentException("La cantidad de noches debe ser mayor a cero.");
        if (r.getPrecioTotal() <= 0)
            throw new IllegalArgumentException("El precio total debe ser mayor a cero.");
    }
}