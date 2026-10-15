
package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Conexion {

    public Connection conectar() {

        Connection conexion = null;

        String url = "jdbc:oracle:thin:@//localhost:1521/FREEPDB1";
        String usuario = "SYSTEM";
        String password = "Admin--";

        try {

            conexion = DriverManager.getConnection(
                    url,
                    usuario,
                    password
            );

            System.out.println("Conexion realizada correctamente");

        } catch (SQLException e) {

            System.out.println(
                    "Error de conexion: " + e.getMessage()
            );
        }

        return conexion;
    }

    public Map<String, Object> mostrarPuntos(String telefono) {

        Map<String, Object> datos = new LinkedHashMap<>();
        ArrayList<Map<String, Object>> historial = new ArrayList<>();

        String sqlCliente =
                "SELECT ID_CLIENTE, NOMBRE, TELEFONO, PUNTOS "
                + "FROM CLIENTE WHERE TELEFONO = ?";

        String sqlPuntos =
                "SELECT ID_MOVIMIENTO, FECHA, TIPO, PUNTOS "
                + "FROM MOVIMIENTO_PUNTOS "
                + "WHERE ID_CLIENTE = ? "
                + "ORDER BY FECHA DESC, ID_MOVIMIENTO DESC";

        try (Connection cn = conectar()) {

            if (cn == null) {
                datos.put("error", "No se pudo conectar a Oracle");
                return datos;
            }

            int idCliente = 0;

            try (PreparedStatement ps = cn.prepareStatement(sqlCliente)) {

                ps.setString(1, telefono);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        idCliente = rs.getInt("ID_CLIENTE");

                        datos.put("idCliente", idCliente);
                        datos.put("nombre", rs.getString("NOMBRE"));
                        datos.put("telefono", rs.getString("TELEFONO"));
                        datos.put("puntosDisponibles", rs.getInt("PUNTOS"));

                    } else {

                        datos.put("mensaje", "Cliente no encontrado");
                        return datos;
                    }
                }
            }

            try (PreparedStatement ps = cn.prepareStatement(sqlPuntos)) {

                ps.setInt(1, idCliente);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        Map<String, Object> movimiento = new LinkedHashMap<>();

                        movimiento.put(
                                "idMovimiento",
                                rs.getInt("ID_MOVIMIENTO")
                        );

                        movimiento.put(
                                "fecha",
                                rs.getTimestamp("FECHA").toString()
                        );

                        movimiento.put(
                                "tipo",
                                rs.getString("TIPO")
                        );

                        movimiento.put(
                                "puntos",
                                rs.getInt("PUNTOS")
                        );

                        historial.add(movimiento);
                    }
                }
            }

            datos.put("historialPuntos", historial);

        } catch (Exception e) {

            datos.clear();
            datos.put("error", "Error al consultar historial de puntos");

            System.out.println(e.getMessage());
        }

        return datos;
    }

    public Map<String, Object> mostrarCanjes(String telefono) {

        Map<String, Object> datos = new LinkedHashMap<>();
        ArrayList<Map<String, Object>> historial = new ArrayList<>();

        String sqlCliente =
                "SELECT ID_CLIENTE, NOMBRE, TELEFONO, PUNTOS "
                + "FROM CLIENTE WHERE TELEFONO = ?";

        String sqlCanjes =
                "SELECT c.ID_CANJE, c.FECHA, "
                + "c.PUNTOS_UTILIZADOS, r.NOMBRE AS RECOMPENSA "
                + "FROM CANJE c "
                + "INNER JOIN RECOMPENSA r "
                + "ON c.ID_RECOMPENSA = r.ID_RECOMPENSA "
                + "WHERE c.ID_CLIENTE = ? "
                + "ORDER BY c.FECHA DESC, c.ID_CANJE DESC";

        try (Connection cn = conectar()) {

            if (cn == null) {
                datos.put("error", "No se pudo conectar a Oracle");
                return datos;
            }

            int idCliente = 0;

            try (PreparedStatement ps = cn.prepareStatement(sqlCliente)) {

                ps.setString(1, telefono);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        idCliente = rs.getInt("ID_CLIENTE");

                        datos.put("idCliente", idCliente);
                        datos.put("nombre", rs.getString("NOMBRE"));
                        datos.put("telefono", rs.getString("TELEFONO"));
                        datos.put("puntosDisponibles", rs.getInt("PUNTOS"));

                    } else {

                        datos.put("mensaje", "Cliente no encontrado");
                        return datos;
                    }
                }
            }

            try (PreparedStatement ps = cn.prepareStatement(sqlCanjes)) {

                ps.setInt(1, idCliente);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        Map<String, Object> canje = new LinkedHashMap<>();

                        canje.put(
                                "idCanje",
                                rs.getInt("ID_CANJE")
                        );

                        canje.put(
                                "fecha",
                                rs.getTimestamp("FECHA").toString()
                        );

                        canje.put(
                                "recompensa",
                                rs.getString("RECOMPENSA")
                        );

                        canje.put(
                                "puntosUtilizados",
                                rs.getInt("PUNTOS_UTILIZADOS")
                        );

                        historial.add(canje);
                    }
                }
            }

            datos.put("historialCanjes", historial);

        } catch (Exception e) {

            datos.clear();
            datos.put("error", "Error al consultar historial de canjes");

            System.out.println(e.getMessage());
        }

        return datos;
    }
}
