package AccesoDatos;

import Logica.Cliente;
import Logica.Menu;
import Logica.Mesa;
import Logica.MenusVendidosDTO;
import Logica.Pedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PedidoDAO {

    private static final Logger logger = Logger.getLogger(PedidoDAO.class.getName());
    
    
    public boolean agregarPedido(Pedido pedido) {
        String sql = "INSERT INTO pedidos (nombre_cliente, telefono, menu_elegido, "
                   + "metodo_pago, es_domicilio, direccion, mesa) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            
            // Lleno los campos con datos del Pedido
            pstmt.setString(1, pedido.getCliente().getNombre());
            pstmt.setString(2, pedido.getCliente().getTelefono());
            pstmt.setString(3, pedido.getMenu().getNombre());
            pstmt.setString(4, pedido.getMetodoPago());
            pstmt.setString(5, pedido.isEsDomicilio() ? "Si" : "No");
            pstmt.setString(6, pedido.getDireccion());
            
            // En la tabla 'pedidos', la columna 'mesa' es VARCHAR(20) y se almacenará el número de la mesa.
            if (pedido.getMesa() != null) {
                // Uso el número de mesa, no el ID
                pstmt.setString(7, String.valueOf(pedido.getMesa().getNumero()));
            } else {
                pstmt.setNull(7, java.sql.Types.VARCHAR);
            }
            
            int filasAfectadas = pstmt.executeUpdate();
            logger.log(Level.INFO, "Pedido agregado correctamente. Filas afectadas: {0}", filasAfectadas);
            return filasAfectadas > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al agregar el pedido: " + e.getMessage(), e);
            return false;
        }
    }
    
    
    public List<Pedido> obtenerPedidos() {
        List<Pedido> listaPedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos";
        
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Pedido pedido = new Pedido();
                
                
                pedido.setIdPedido(rs.getInt("id_pedido"));

                // Reconstruyo el Cliente a partir de nombre_cliente y telefono
                Cliente cliente = new Cliente();
                cliente.setNombre(rs.getString("nombre_cliente"));
                cliente.setTelefono(rs.getString("telefono"));
                pedido.setCliente(cliente);

                // Reconstruyo el Menu a partir del menu_elegido 
                Menu menu = new Menu();
                menu.setNombre(rs.getString("menu_elegido"));
                pedido.setMenu(menu);

                // Asigno el método de pago, domicilio y dirección
                pedido.setMetodoPago(rs.getString("metodo_pago"));
                pedido.setEsDomicilio("Si".equalsIgnoreCase(rs.getString("es_domicilio")));
                pedido.setDireccion(rs.getString("direccion"));

                // Recupero la mesa a partir de la columna 'mesa' (almacenada como texto)
                String mesaStr = rs.getString("mesa");
                if (mesaStr != null && !mesaStr.isEmpty()) {
                    try {
                        int mesaNumero = Integer.parseInt(mesaStr);
                        MesaDAO mesaDao = new MesaDAO();
                        // Busco la mesa por el número
                        pedido.setMesa(mesaDao.obtenerMesaPorNumero(mesaNumero));
                    } catch (NumberFormatException nfe) {
                        logger.log(Level.WARNING, "El valor de la columna 'mesa' no es numérico: {0}", mesaStr);
                    }
                }
                
                listaPedidos.add(pedido);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener los pedidos: " + e.getMessage(), e);
        }
        
        return listaPedidos;
    }
    
    /**
     * Obtiengo la cantidad vendida de cada menú 
     */
    public List<MenusVendidosDTO> obtenerMenusVendidos() {
        List<MenusVendidosDTO> lista = new ArrayList<>();
        String sql = "SELECT p.menu_elegido AS nombre_menu, COUNT(*) AS total_ventas "
                   + "FROM pedidos p "
                   + "JOIN menus m ON p.menu_elegido = m.nombre "
                   + "GROUP BY p.menu_elegido";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String nombreMenu = rs.getString("nombre_menu");
                int totalVentas = rs.getInt("total_ventas");
                
                MenusVendidosDTO dto = new MenusVendidosDTO(nombreMenu, totalVentas);
                lista.add(dto);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener los menús vendidos: " + e.getMessage(), e);
        }
        
        return lista;
    }
    
    /**
     * Calculo la suma de los precios de todos los menús vendidos.
     */
    public double obtenerTotalGanancias() {
        double total = 0;
        String sql = "SELECT SUM(m.precio) AS total_ganancias "
                   + "FROM pedidos p "
                   + "JOIN menus m ON p.menu_elegido = m.nombre";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                total = rs.getDouble("total_ganancias");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener el total de ganancias: " + e.getMessage(), e);
        }
        
        return total;
    }
    
    public boolean eliminarPedido(int idPedido) {
    String sql = "DELETE FROM pedidos WHERE id_pedido = ?";
    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement pstmt = conexion.prepareStatement(sql)) {
        pstmt.setInt(1, idPedido);
        int filasAfectadas = pstmt.executeUpdate();
        return filasAfectadas > 0;
    } catch (SQLException e) {
        logger.log(Level.SEVERE, "Error al eliminar el pedido: " + e.getMessage(), e);
        return false;
    }
}

}
