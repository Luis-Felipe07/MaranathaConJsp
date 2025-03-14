package AccesoDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionBD {
    
    private static final Logger logger = Logger.getLogger(ConexionBD.class.getName());
    private static final String URL = "jdbc:mysql://localhost:3306/bdmaranathaconjdbc";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    public static Connection obtenerConexion() {
        Connection conexion = null;
        try {
            // Cargó el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            logger.log(Level.INFO, "Conexión exitosa a la base de datos.");
            inicializarBaseDeDatos(conexion);  
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error: no se encontró el driver MySQL. " + e.getMessage(), e);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al conectar con la base de datos: " + e.getMessage(), e);
        }
        return conexion;
    }

    private static void inicializarBaseDeDatos(Connection conexion) {
        String crearTablaUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (" 
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "usuario VARCHAR(50) UNIQUE NOT NULL, "
                + "contrasenia VARCHAR(50) NOT NULL, "
                + "tipo ENUM('admin', 'cliente') NOT NULL)";
        
        String insertarUsuarios = "INSERT INTO usuarios (usuario, contrasenia, tipo) VALUES " 
                + "('admin', 'admin123', 'admin'), " 
                + "('cliente', 'cliente123', 'cliente') " 
                + "ON DUPLICATE KEY UPDATE usuario=usuario";
        
        String crearTablaPedidos = "CREATE TABLE IF NOT EXISTS pedidos (" 
                + "id_pedido INT AUTO_INCREMENT PRIMARY KEY, " 
                + "nombre_cliente VARCHAR(50) NOT NULL, " 
                + "telefono VARCHAR(20), " 
                + "menu_elegido VARCHAR(50) NOT NULL, " 
                + "metodo_pago VARCHAR(50) NOT NULL, " 
                + "es_domicilio ENUM('Si','No') NOT NULL, " 
                + "direccion VARCHAR(100), " 
                + "mesa VARCHAR(20)" 
                + ")";
        
        String crearTablaMesas = "CREATE TABLE IF NOT EXISTS mesas (" 
                + "id_mesa INT AUTO_INCREMENT PRIMARY KEY, " 
                + "numero INT NOT NULL, " 
                + "ocupada TINYINT(1) DEFAULT 0)";
        
        String insertarMesas = "INSERT INTO mesas (numero, ocupada) " 
                + "SELECT 1, 0 WHERE NOT EXISTS (SELECT 1 FROM mesas WHERE numero = 1) " 
                + "UNION ALL SELECT 2, 0 WHERE NOT EXISTS (SELECT 1 FROM mesas WHERE numero = 2) " 
                + "UNION ALL SELECT 3, 0 WHERE NOT EXISTS (SELECT 1 FROM mesas WHERE numero = 3) " 
                + "UNION ALL SELECT 4, 0 WHERE NOT EXISTS (SELECT 1 FROM mesas WHERE numero = 4)";
        
        String crearTablaMenus = "CREATE TABLE IF NOT EXISTS menus (" 
                + "id_menu INT AUTO_INCREMENT PRIMARY KEY, " 
                + "nombre VARCHAR(100) NOT NULL, " 
                + "precio DOUBLE NOT NULL, " 
                + "disponible TINYINT(1) DEFAULT 1"
                + ")";
        
        try (Statement stmt = conexion.createStatement()) {
            stmt.execute(crearTablaUsuarios);
            logger.log(Level.INFO, "Tabla 'usuarios' creada o ya existente.");
            
            stmt.execute(insertarUsuarios);
            logger.log(Level.INFO, "Usuarios predefinidos insertados o ya existentes.");
            
            stmt.execute(crearTablaPedidos);
            logger.log(Level.INFO, "Tabla 'pedidos' creada o ya existente.");
            
            stmt.execute(crearTablaMesas);
            logger.log(Level.INFO, "Tabla 'mesas' creada o ya existente.");
            
            stmt.execute(insertarMesas);
            logger.log(Level.INFO, "Mesas predeterminadas insertadas si no existían.");
            
            stmt.execute(crearTablaMenus);
            logger.log(Level.INFO, "Tabla 'menus' creada o ya existente.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al inicializar la base de datos: " + e.getMessage(), e);
        }
    }
}
