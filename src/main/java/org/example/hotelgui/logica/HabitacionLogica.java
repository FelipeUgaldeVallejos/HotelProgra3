package org.example.hotelgui.logica;

import org.example.hotelgui.datos.HabitacionDatos;
import org.example.hotelgui.model.Habitacion;

import java.sql.SQLException;
import java.util.List;

/** CRUD para Habitacion usando una base de datos mySql como almacenamiento. */
public class HabitacionLogica {

    private HabitacionDatos store = new HabitacionDatos();

    // --------- Lectura ---------

    public List<Habitacion> findAll() throws SQLException {
        return store.findAll();
    }

    public Habitacion findById(int id) throws SQLException {
        return store.findById(id);
    }

    // --------- Escritura ---------

    public Habitacion create(Habitacion nueva) throws SQLException {
        validarNueva(nueva);
        return store.insert(nueva);
    }

    public Habitacion update(Habitacion habitacion) throws SQLException {
        if (habitacion == null || habitacion.getId() <= 0)
            throw new IllegalArgumentException("La habitación a actualizar requiere un ID válido.");
        validarCampos(habitacion);
        return store.update(habitacion);
    }

    public boolean deleteById(int id) throws SQLException {
        if (id <= 0) return false;
        return store.delete(id) > 0;
    }

    // --------- Helpers ---------

    private void validarNueva(Habitacion h) {
        if (h == null) throw new IllegalArgumentException("Habitación nula.");
        validarCampos(h);
    }

    private void validarCampos(Habitacion h) {
        if (h.getNumero() <= 0)
            throw new IllegalArgumentException("El número de habitación debe ser mayor a cero.");
        if (h.getTipo() <= 0)
            throw new IllegalArgumentException("El tipo de habitación es obligatorio.");
        if (h.getEstado() <= 0)
            throw new IllegalArgumentException("El estado de la habitación es obligatorio.");
        if (h.getPrecio() <= 0)
            throw new IllegalArgumentException("El precio debe ser mayor a cero.");
        if (h.getCapacidad() <= 0)
            throw new IllegalArgumentException("La capacidad debe ser mayor a cero.");
    }
}
