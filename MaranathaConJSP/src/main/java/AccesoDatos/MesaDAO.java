package AccesoDatos;

import Logica.Mesa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MesaDAO {
    private static final Logger logger = Logger.getLogger(MesaDAO.class.getName());

    public Mesa obtenerMesaPorId(int idMesa) {
        String query = "SELECT * FROM mesas WHERE id_mesa = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conexion.prepareStatement(query)) {
            
            pstmt.setInt(1, idMesa);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Mesa(
                        rs.getInt("id_mesa"),
                        rs.getInt("numero"),
                        rs.getBoolean("ocupada")
                    );
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener mesa por id: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Mesa> obtenerTodasLasMesas() {
        List<Mesa> mesas = new ArrayList<>();
        String query = "SELECT * FROM mesas";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conexion.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                mesas.add(new Mesa(
                    rs.getInt("id_mesa"),
                    rs.getInt("numero"),
                    rs.getBoolean("ocupada")
                ));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener todas las mesas: " + e.getMessage(), e);
        }
        return mesas;
    }

    public void agregarColumnaOcupada() {
        String query = "ALTER TABLE mesas ADD COLUMN ocupada TINYINT(1) DEFAULT 0";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            int filas = stmt.executeUpdate();
            logger.log(Level.INFO, "Columna 'ocupada' agregada, filas afectadas: {0}", filas);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al agregar la columna 'ocupada': " + e.getMessage(), e);
        }
    }

    public void actualizarEstadoMesa(int idMesa, boolean ocupada) {
        String query = "UPDATE mesas SET ocupada = ? WHERE id_mesa = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setBoolean(1, ocupada);
            stmt.setInt(2, idMesa);
            int filas = stmt.executeUpdate();
            if (filas > 0) {
                logger.log(Level.INFO,
                    "Estado de la mesa {0} actualizado a {1}. Filas afectadas: {2}",
                    new Object[]{idMesa, ocupada, filas});
            } else {
                logger.log(Level.WARNING, "No se actualizó el estado de la mesa {0}.", idMesa);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar el estado de la mesa: " + e.getMessage(), e);
        }
    }

    public List<Mesa> obtenerMesasDisponibles() {
        List<Mesa> mesasDisponibles = new ArrayList<>();
        String query = "SELECT * FROM mesas WHERE ocupada = 0";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conexion.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            logger.log(Level.INFO, "Consultando mesas disponibles...");
            while (rs.next()) {
                int idMesa = rs.getInt("id_mesa");
                int numero = rs.getInt("numero");
                boolean ocupada = rs.getBoolean("ocupada");
                logger.log(Level.INFO, "Mesa encontrada - ID: {0}, Número: {1}, Ocupada: {2}",
                        new Object[]{idMesa, numero, ocupada});
                
                mesasDisponibles.add(new Mesa(idMesa, numero, ocupada));
            }
            logger.log(Level.INFO, "Total de mesas disponibles: {0}", mesasDisponibles.size());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener mesas disponibles: " + e.getMessage(), e);
        }
        return mesasDisponibles;
    }

    public Mesa obtenerMesaPorNumero(int numero) {
        String query = "SELECT * FROM mesas WHERE numero = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conexion.prepareStatement(query)) {
            
            pstmt.setInt(1, numero);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Mesa(
                        rs.getInt("id_mesa"),
                        rs.getInt("numero"),
                        rs.getBoolean("ocupada")
                    );
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener mesa por número: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Mesa> obtenerMesasOcupadas() {
        List<Mesa> mesasOcupadas = new ArrayList<>();
        String sql = "SELECT * FROM mesas WHERE ocupada = 1";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int idMesa = rs.getInt("id_mesa");
                int numeroMesa = rs.getInt("numero");
                boolean ocupada = rs.getBoolean("ocupada");
                mesasOcupadas.add(new Mesa(idMesa, numeroMesa, ocupada));
            }
            logger.log(Level.INFO, "Total de mesas ocupadas: {0}", mesasOcupadas.size());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener mesas ocupadas: " + e.getMessage(), e);
        }
        return mesasOcupadas;
    }

    
    public void actualizarEstadoMesas() {
        String sql = "UPDATE mesas m "
                   + "SET ocupada = EXISTS ("
                   + "    SELECT 1 FROM pedidos p "
                   + "    WHERE CAST(p.mesa AS UNSIGNED) = m.numero"
                   + " )";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            int filasActualizadas = stmt.executeUpdate();
            logger.log(Level.INFO, "Mesas actualizadas: {0}", filasActualizadas);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar estado de mesas: " + e.getMessage(), e);
        }
    }

    public boolean ocuparMesa(int numeroMesa) {
        boolean exito = false;
        String sql = "UPDATE mesas SET ocupada = 1 WHERE numero = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, numeroMesa);
            int filasAfectadas = stmt.executeUpdate();
            exito = (filasAfectadas > 0);
            if (exito) {
                logger.log(Level.INFO,
                    "Mesa con número {0} marcada como ocupada. Filas afectadas: {1}",
                    new Object[]{numeroMesa, filasAfectadas});
            } else {
                logger.log(Level.WARNING,
                    "No se encontró mesa con número {0} para marcar como ocupada.",
                    numeroMesa);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                "Error al ocupar la mesa con número " + numeroMesa + ": " + e.getMessage(), e);
        }
        return exito;
    }

    public boolean liberarMesa(int numeroMesa) {
        boolean exito = false;
        String sql = "UPDATE mesas SET ocupada = 0 WHERE numero = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, numeroMesa);
            int filasAfectadas = stmt.executeUpdate();
            exito = (filasAfectadas > 0);
            if (exito) {
                logger.log(Level.INFO,
                    "Mesa con número {0} marcada como libre. Filas afectadas: {1}",
                    new Object[]{numeroMesa, filasAfectadas});
            } else {
                logger.log(Level.WARNING,
                    "No se encontró mesa con número {0} para marcar como libre.",
                    numeroMesa);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                "Error al liberar la mesa con número " + numeroMesa + ": " + e.getMessage(), e);
        }
        return exito;
    }
}
