package org.example.hotelgui.logica;

import org.example.hotelgui.datos.ClienteDatos;
import org.example.hotelgui.model.Cliente;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/** CRUD para Cliente usando una base de datos mySql como almacenamiento. */
public class ClienteLogica {

    private ClienteDatos store = new ClienteDatos();

    // --------- Lectura ---------

    public List<Cliente> findAll() throws SQLException {
        return store.findAll();
    }

    public Cliente findById(int id) throws SQLException {
        return store.findById(id);
    }

    /*public List<Cliente> searchByNombreOApellido(String texto) {
        String q = (texto == null) ? "" : texto.trim().toLowerCase();
        ClienteConector data = store.load();
        return data.getClientes().stream()
                .filter(x ->
                        (x.getNombre() != null && x.getNombre().toLowerCase().contains(q)) ||
                                (x.getPrimerApellido() != null && x.getPrimerApellido().toLowerCase().contains(q)) ||
                                (x.getSegundoApellido() != null && x.getSegundoApellido().toLowerCase().contains(q)) ||
                                (x.getIdentificacion() != null && x.getIdentificacion().toLowerCase().contains(q))
                )
                .map(ClienteMapper::toModel)
                .collect(Collectors.toList());
    }*/

    // --------- Escritura ---------

    public Cliente create(Cliente nuevo) throws SQLException {
        validarNuevo(nuevo);

        return store.insert(nuevo);
    }

    public Cliente update(Cliente cliente) throws SQLException {
        if (cliente == null || cliente.getId() <= 0)
            throw new IllegalArgumentException("El cliente a actualizar requiere un ID válido.");
        validarCampos(cliente);
        return store.update(cliente);
    }

    public boolean deleteById(int id) throws SQLException {
        if (id <= 0) return false;

        return store.delete(id) > 0 ? true : false;
    }

    // --------- Helpers ---------

    private void validarNuevo(Cliente c) {
        if (c == null) throw new IllegalArgumentException("Cliente nulo.");
        validarCampos(c);
    }

    private void validarCampos(Cliente c) {
        if (c.getNombre() == null || c.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (c.getPrimerApellido() == null || c.getPrimerApellido().isBlank())
            throw new IllegalArgumentException("El primer apellido es obligatorio.");
        if (c.getFechaNacimiento() != null && c.getFechaNacimiento().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
    }
}
