package org.example.hotelgui.datos;

import org.example.hotelgui.model.Cliente;
import org.example.hotelgui.model.Habitacion;
import org.example.hotelgui.model.Reservacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservacionDatos {

    public List<Reservacion> findAll() throws SQLException {
        String sql = "SELECT * FROM reservacion ORDER BY id_reservacion";
        try (Connection cn = DB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Reservacion> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(new Reservacion(
                        rs.getInt("id_reservacion"),
                        new HabitacionDatos().findById(rs.getInt("id_habitacion")),
                        new ClienteDatos().findById(rs.getInt("id_cliente")),
                        rs.getDate("fecha_llegada").toLocalDate(),
                        rs.getInt("cantidad_noches"),
                        rs.getDouble("precio_total")
                ));
            }
            return lista;
        }
    }

    public Reservacion findById(int id) throws SQLException {
        String sql = "SELECT * FROM reservacion WHERE id_reservacion = " + id;
        try (Connection cn = DB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Reservacion encontrado = null;
            if (rs.next()) {
                encontrado = new Reservacion(
                        rs.getInt("id_reservacion"),
                        new HabitacionDatos().findById(rs.getInt("id_habitacion")),
                        new ClienteDatos().findById(rs.getInt("id_cliente")),
                        rs.getDate("fecha_llegada").toLocalDate(),
                        rs.getInt("cantidad_noches"),
                        rs.getDouble("precio_total")
                );
            }
            return encontrado;
        }
    }

    public Reservacion insert(Reservacion reservacion) throws SQLException {
        String sql = "INSERT INTO reservacion (id_habitacion, id_cliente, fecha_reservacion, fechaLlegada, cantidadNoches, fechaSalida, precioTotal, descuento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection cn = DB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, reservacion.getHabitacion().getId());
            ps.setInt(2, reservacion.getCliente().getId());
            ps.setDate(3, Date.valueOf(reservacion.getFechaReservacion()));
            ps.setDate(4, Date.valueOf(reservacion.getFechaLlegada()));
            ps.setInt(5, reservacion.getCantidadNoches());
            ps.setString(6, reservacion.getFechaSalida());
            ps.setDouble(7, reservacion.getPrecioTotal());
            ps.setDouble(8, reservacion.getDescuento());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return reservacion;
                }
            }
            return null;
        }
    }

    public Reservacion update(Reservacion reservacion) throws SQLException {
       String sql = "UPDATE reservacion SET id_habitacion = ?, id_cliente = ?, fecha_reservacion = ?, fecha_llegada = ?, cantidad_noches = ?, fecha_salida = ?, precio_total = ?, descuento = ? WHERE id_reservacion = ?";
        try (Connection cn = DB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, reservacion.getHabitacion().getId());
            ps.setInt(2, reservacion.getCliente().getId());
            ps.setDate(3, Date.valueOf(reservacion.getFechaReservacion()));
            ps.setDate(4, Date.valueOf(reservacion.getFechaLlegada()));
            ps.setInt(5, reservacion.getCantidadNoches());
            ps.setString(6, reservacion.getFechaSalida());
            ps.setDouble(7, reservacion.getPrecioTotal());
            ps.setDouble(8, reservacion.getDescuento());
            ps.setInt(9, reservacion.getIdReservacion());

            if (ps.executeUpdate() > 0) {
                return reservacion;
            }
            return null;
        }
    }

    public int delete(int idReservacion) throws SQLException {
        String sql = "DELETE FROM reservacion WHERE id_reservacion = " + idReservacion;
        try (Connection cn = DB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            return ps.executeUpdate();
        }
    }
}
