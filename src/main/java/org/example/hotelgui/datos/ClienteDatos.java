package org.example.hotelgui.datos;
import org.example.hotelgui.model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDatos {

    public List<Cliente> findAll() throws SQLException {
        //Construir el query que se ejecuta en MySQL
        String sql = "Select * from cliente ORDER BY id";
        //String sqlAlternativo = "Select * from cliente where nombre = 'Juan'";

        //Conectamos a la base de datos
        try(Connection cn = DB.getConnection(); //1. Conexión
            PreparedStatement ps = cn.prepareStatement(sql); //2. Preparar la sentencia sql
            ResultSet rs = ps.executeQuery()) { //3. Ejecutar la sentencia

            List<Cliente> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Cliente(
                   rs.getInt("id"),
                   rs.getString("nombre"),
                   rs.getString("primer_apellido"),
                   rs.getString("identificacion"),
                   rs.getDate("fecha_nacimiento").toLocalDate()
                ));
            }
            return list;
        }
    }

    public Cliente findById(int id) throws SQLException {
        String sql = "Select * from cliente WHERE id = " + id;
        try(Connection cn = DB.getConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            Cliente encontrado = null;
            if (rs.next()) {
                encontrado = new Cliente(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("primer_apellido"),
                    rs.getString("identificacion"),
                    rs.getDate("fecha_nacimiento").toLocalDate()
                );
            }
            return encontrado;
        }
    }

    public Cliente insert(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nombre, primer_apellido, segundo_apellido, identificacion, fecha_nacimiento) VALUES (?, ?, ?, ?, ?)";
        try(Connection cn = DB.getConnection();
            PreparedStatement ps = cn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getPrimerApellido());
            ps.setString(3, cliente.getSegundoApellido());
            ps.setString(4, cliente.getIdentificacion());
            ps.setDate(5, Date.valueOf(cliente.getFechaNacimiento()));
            ps.executeUpdate();
            try(ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return cliente;
                }
            }
            return null;
        }
    }

    public Cliente update(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente set nombre = ?, primer_apellido = ?, segundo_apellido = ? WHERE id = ?";
        try(Connection cn = DB.getConnection();
        PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getPrimerApellido());
            ps.setString(3, cliente.getSegundoApellido());
            ps.setInt(4, cliente.getId());
            if(ps.executeUpdate() > 0)
            {
                return cliente;
            }
            else
            {
                return null;
            }
        }
    }

    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id = " + id;
        try(Connection cn = DB.getConnection();
        PreparedStatement ps = cn.prepareStatement(sql)) {
            return ps.executeUpdate();
        }
    }
}
