package Controladores;

import AccesoDatos.PedidoDAO;
import Logica.MenusVendidosDTO;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "GestionarMenusYVentasServlet", urlPatterns = {"/GestionarMenusYVentasServlet"})
public class GestionarMenusYVentasServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(GestionarMenusYVentasServlet.class.getName());
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Muestro el JSP sin datos o con datos previamente cargados
        RequestDispatcher rd = request.getRequestDispatcher("/Admin/GestionarMenusYVentas.jsp");
        rd.forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("verDatos".equals(accion)) {
            try {
                PedidoDAO pedidoDAO = new PedidoDAO();
                List<MenusVendidosDTO> menusVendidos = pedidoDAO.obtenerMenusVendidos();
                double totalGanancias = pedidoDAO.obtenerTotalGanancias();
                request.setAttribute("menusVendidos", menusVendidos);
                request.setAttribute("totalGanancias", totalGanancias);
                request.setAttribute("mensajeExito", "Datos actualizados correctamente.");
                logger.log(Level.INFO, "Datos de menús vendidos y ganancias actualizados.");
            } catch(Exception e) {
                request.setAttribute("mensajeError", "Error al obtener datos: " + e.getMessage());
                logger.log(Level.SEVERE, "Error en GestionarMenusYVentasServlet: {0}", e.getMessage());
                e.printStackTrace();
            }
            RequestDispatcher rd = request.getRequestDispatcher("/Admin/GestionarMenusYVentas.jsp");
            rd.forward(request, response);
        } else {
            doGet(request, response);
        }
    }
}
