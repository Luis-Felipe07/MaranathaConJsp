package AccesoDatos;

import Logica.Menu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuDAO {
    
    private static final Logger logger = Logger.getLogger(MenuDAO.class.getName());

    public Menu obtenerMenuPorId(int idMenu) {
        String sql = "SELECT * FROM menus WHERE id_menu = ?";
        Menu menu = null;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
             
            pstmt.setInt(1, idMenu);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    menu = new Menu(rs.getInt("id_menu"), 
                                    rs.getString("nombre"), 
                                    rs.getDouble("precio"), 
                                    rs.getBoolean("disponible"));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener el menú: " + e.getMessage(), e);
        }
        return menu;
    }

   public List<Menu> listarMenus() {
    List<Menu> listaMenus = new ArrayList<>();
    String sql = "SELECT * FROM menus WHERE disponible = 1";
    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement pstmt = conexion.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
            Menu menu = new Menu(rs.getInt("id_menu"), 
                                 rs.getString("nombre"), 
                                 rs.getDouble("precio"), 
                                 rs.getBoolean("disponible"));
            listaMenus.add(menu);
        }
    } catch (SQLException e) {
        logger.log(Level.SEVERE, "Error al listar los menús: " + e.getMessage(), e);
    }
    return listaMenus;
}

    
    public boolean agregarMenu(Menu menu) {
        String sql = "INSERT INTO menus (nombre, precio) VALUES (?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, menu.getNombre());
            stmt.setDouble(2, menu.getPrecio());
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                // Recupero el id generado 
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        menu.setIdMenu(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al agregar menú: " + e.getMessage(), e);
        }
        return false;
    }
    
    public boolean eliminarMenuPorNombre(String nombre) {
    // En lugar de borrar el registro, marco el menú como no disponible
    String sql = "UPDATE menus SET disponible = 0 WHERE nombre = ?";
    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement pstmt = conexion.prepareStatement(sql)) {
        pstmt.setString(1, nombre);
        int filasAfectadas = pstmt.executeUpdate();
        return filasAfectadas > 0;
    } catch (SQLException e) {
        logger.log(Level.SEVERE, "Error al eliminar (soft delete) menú: " + e.getMessage(), e);
        return false;
    }
}

}
