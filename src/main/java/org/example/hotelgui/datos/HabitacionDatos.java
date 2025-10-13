package org.example.hotelgui.datos;

import org.example.hotelgui.model.Cliente;
import org.example.hotelgui.model.Habitacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitacionDatos {

    public List<Habitacion> findAll() throws SQLException {
        String sql = "SELECT * FROM habitacion ORDER BY id";
        try (Connection cn = DB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Habitacion> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(new Habitacion(
                        rs.getInt("id"),
                        rs.getInt("numero"),
                        rs.getInt("estado"),
                        rs.getDouble("precio"),
                        rs.getInt("capacidad"))
                );
            }
            return lista;
        }
    }

    public Habitacion findById(int id) throws SQLException {
        String sql = "SELECT * FROM habitacion WHERE id = " + id;
        try (Connection cn = DB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Habitacion encontrado = null;
            if (rs.next()) {
               encontrado = new Habitacion(
                        rs.getInt("id"),
                        rs.getInt("numero"),
                        rs.getInt("estado"),
                        rs.getDouble("precio"),
                        rs.getInt("capacidad")
                );
                return encontrado;
            }
            return null;
        }
    }

    public Habitacion insert(Habitacion h) throws SQLException {
        String sql = "INSERT INTO habitacion (numero, tipo, estado, precio, capacidad) VALUES (?, ?, ?, ?, ?)";
        try (Connection cn = DB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, h.getNumero());
            ps.setInt(2, h.getTipo());
            ps.setInt(3, h.getEstado());
            ps.setDouble(4, h.getPrecio());
            ps.setInt(5, h.getCapacidad());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return h;
                }
            }
            return null;
        }
    }

    public Habitacion update(Habitacion h) throws SQLException {
        String sql = "UPDATE habitacion SET numero = ?, tipo = ?, descripcion_tipo = ?, estado = ?, descripcion_estado = ?, precio = ?, capacidad = ? WHERE id = ?";
        try (Connection cn = DB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, h.getNumero());
            ps.setInt(2, h.getTipo());
            ps.setString(3, h.getDescripcionTipo());
            ps.setInt(4, h.getEstado());
            ps.setString(5, h.getDescripcionEstado());
            ps.setDouble(6, h.getPrecio());
            ps.setInt(7, h.getCapacidad());
            ps.setInt(8, h.getId());

            if (ps.executeUpdate() > 0) {
                return h;
            }
            return null;
        }
    }

    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM habitacion WHERE id = " + id;
        try (Connection cn = DB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            return ps.executeUpdate();
        }
    }
}

