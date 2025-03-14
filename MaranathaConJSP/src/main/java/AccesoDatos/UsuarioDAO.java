/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AccesoDatos;

import Logica.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UsuarioDAO {
    
    
    
    private static final Logger logger = Logger.getLogger(UsuarioDAO.class.getName());
    private Connection connection;

    // Constructor sin parámetros 
    public UsuarioDAO() {
        this.connection = ConexionBD.obtenerConexion();
    }

    // Constructor con Connection 
    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    
    public Usuario autenticarUsuario(String nombreUsuario, String contrasenia) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contrasenia = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, contrasenia);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Creamos el objeto usando tu constructor de 3 parámetros
                    return new Usuario(
                        rs.getString("usuario"),      
                        rs.getString("contrasenia"),  
                        rs.getString("tipo")          
                    );
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al autenticar usuario", e);
        }
        return null;
    }

    
    public boolean insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (usuario, contrasenia, tipo) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombreUsuario());
            stmt.setString(2, usuario.getContrasenia());
            stmt.setString(3, usuario.getTipo());
            int filas = stmt.executeUpdate();
            return (filas > 0);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al insertar usuario", e);
        }
        return false;
    }

    
    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET contrasenia = ?, tipo = ? WHERE usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getContrasenia());
            stmt.setString(2, usuario.getTipo());
            stmt.setString(3, usuario.getNombreUsuario());
            int filas = stmt.executeUpdate();
            return (filas > 0);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar usuario", e);
        }
        return false;
    }

    
    public boolean eliminarUsuario(String nombreUsuario) {
        String sql = "DELETE FROM usuarios WHERE usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            int filas = stmt.executeUpdate();
            return (filas > 0);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar usuario", e);
        }
        return false;
    }
}
