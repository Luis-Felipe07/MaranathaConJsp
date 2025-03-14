package AccesoDatos;

import Logica.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteDAO {

    private static final Logger logger = Logger.getLogger(ClienteDAO.class.getName());
    private Connection connection;

    // Constructor sin parámetros 
    public ClienteDAO() {
        this.connection = ConexionBD.obtenerConexion();
    }

    // Constructor con Connection 
    public ClienteDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Obtengo una lista de clientes únicos extraídos de la tabla 'pedidos'.
     *  utilizo DISTINCT para considerar solo combinaciones únicas de nombre y teléfono.
     */
    public List<Cliente> getAllClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT DISTINCT nombre_cliente, telefono FROM pedidos";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setNombre(rs.getString("nombre_cliente"));
                cliente.setTelefono(rs.getString("telefono"));
                // Como en 'pedidos' no  almaceno email,  asigno null o un valor por defecto
                cliente.setEmail(null);
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener clientes: " + e.getMessage(), e);
        }
        return clientes;
    }
}
