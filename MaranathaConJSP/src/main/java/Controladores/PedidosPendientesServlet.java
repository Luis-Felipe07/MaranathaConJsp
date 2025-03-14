package Controladores;

import AccesoDatos.PedidoDAO;
import Logica.Pedido;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "PedidosPendientesServlet", urlPatterns = {"/PedidosPendientesServlet"})
public class PedidosPendientesServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(PedidosPendientesServlet.class.getName());
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        cargarPedidos(request);
        RequestDispatcher rd = request.getRequestDispatcher("/Admin/PedidosPendientes.jsp");
        rd.forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        String mensajeExito = null;
        String mensajeError = null;
        PedidoDAO pedidoDAO = new PedidoDAO();
        
        if ("eliminar".equals(accion)) {
            String idPedidoStr = request.getParameter("idPedido");
            try {
                int idPedido = Integer.parseInt(idPedidoStr);
                boolean eliminado = pedidoDAO.eliminarPedido(idPedido);
                if (eliminado) {
                    mensajeExito = "Pedido eliminado exitosamente.";
                } else {
                    mensajeError = "No se pudo eliminar el pedido.";
                }
            } catch (NumberFormatException e) {
                mensajeError = "ID de pedido inválido.";
                logger.log(Level.SEVERE, "Error al parsear el ID de pedido: {0}", e.getMessage());
            }
        }
        
        request.setAttribute("mensajeExito", mensajeExito);
        request.setAttribute("mensajeError", mensajeError);
        cargarPedidos(request);
        RequestDispatcher rd = request.getRequestDispatcher("/Admin/PedidosPendientes.jsp");
        rd.forward(request, response);
    }
    
    private void cargarPedidos(HttpServletRequest request) {
        PedidoDAO pedidoDAO = new PedidoDAO();
        
        List<Pedido> pedidos = pedidoDAO.obtenerPedidos();
        request.setAttribute("pedidosPendientes", pedidos);
    }
}
