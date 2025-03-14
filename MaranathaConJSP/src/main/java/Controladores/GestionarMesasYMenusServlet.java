package Controladores;

import AccesoDatos.MesaDAO;
import AccesoDatos.MenuDAO;
import Logica.Mesa;
import Logica.Menu;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "GestionarMesasYMenusServlet", urlPatterns = {"/GestionarMesasYMenusServlet"})
public class GestionarMesasYMenusServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(GestionarMesasYMenusServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        // Capturo mensajes enviados por redirect 
        String mensajeExito = request.getParameter("mensajeExito");
        String mesaError = request.getParameter("mesaError");
        String menuError = request.getParameter("menuError");
        
        if (mensajeExito != null) {
            request.setAttribute("mensajeExito", mensajeExito);
        }
        if (mesaError != null) {
            request.setAttribute("mesaError", mesaError);
        }
        if (menuError != null) {
            request.setAttribute("menuError", menuError);
        }
        
        // Cargo los datos de mesas ocupadas, mesas disponibles y menús al entrar
        cargarDatos(request);
        
        // Ahora hago el forward con todos los atributos configurados
        RequestDispatcher rd = request.getRequestDispatcher("/Admin/GestionarMesasYMenus.jsp");
        rd.forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        String mensajeExito = null;
        String mesaError = null;
        String menuError = null;
        
        if ("liberarMesa".equals(accion)) {
            try {
                int idMesa = Integer.parseInt(request.getParameter("idMesa"));
                MesaDAO mesaDAO = new MesaDAO();
                // Libero la mesa actualizando su estado a libre 
                mesaDAO.actualizarEstadoMesa(idMesa, false);
                mensajeExito = "Mesa liberada correctamente.";
                logger.log(Level.INFO, "Mesa con id {0} liberada.", idMesa);
            } catch (NumberFormatException e) {
                mesaError = "ID de mesa inválido.";
                logger.log(Level.SEVERE, "Error al parsear el ID de mesa: {0}", e.getMessage());
            }
        } else if ("agregarMenu".equals(accion)) {
            String nombreMenu = request.getParameter("nombreMenu");
            String precioStr = request.getParameter("precioMenu");
            try {
                double precioMenu = Double.parseDouble(precioStr);
                Menu nuevoMenu = new Menu();
                nuevoMenu.setNombre(nombreMenu);
                nuevoMenu.setPrecio(precioMenu);
                nuevoMenu.setDisponible(true);
                MenuDAO menuDAO = new MenuDAO();
                boolean agregado = menuDAO.agregarMenu(nuevoMenu);
                if (agregado) {
                    mensajeExito = "Menú agregado correctamente.";
                    logger.log(Level.INFO, "Menú {0} agregado con éxito.", nombreMenu);
                } else {
                    menuError = "No se pudo agregar el menú.";
                    logger.log(Level.WARNING, "Fallo al agregar el menú {0}.", nombreMenu);
                }
            } catch (NumberFormatException e) {
                menuError = "Precio inválido para el menú.";
                logger.log(Level.SEVERE, "Error al parsear el precio del menú: {0}", e.getMessage());
            }
        } else if ("eliminarMenu".equals(accion)) {
            String menuEliminar = request.getParameter("menuEliminar");
            MenuDAO menuDAO = new MenuDAO();
            boolean eliminado = menuDAO.eliminarMenuPorNombre(menuEliminar);
            if (eliminado) {
                mensajeExito = "Menú eliminado correctamente.";
                logger.log(Level.INFO, "Menú {0} eliminado.", menuEliminar);
            } else {
                menuError = "No se pudo eliminar el menú.";
                logger.log(Level.WARNING, "Fallo al eliminar el menú {0}.", menuEliminar);
            }
        }
        
        // Construyo la URL de redirección con los mensajes para mostrarlos en la interfaz
        String redirectURL = request.getContextPath() + "/GestionarMesasYMenusServlet";
        StringBuilder params = new StringBuilder();
        if (mensajeExito != null) {
            params.append("mensajeExito=").append(URLEncoder.encode(mensajeExito, StandardCharsets.UTF_8.toString()));
        }
        if (mesaError != null) {
            if (params.length() > 0) { params.append("&"); }
            params.append("mesaError=").append(URLEncoder.encode(mesaError, StandardCharsets.UTF_8.toString()));
        }
        if (menuError != null) {
            if (params.length() > 0) { params.append("&"); }
            params.append("menuError=").append(URLEncoder.encode(menuError, StandardCharsets.UTF_8.toString()));
        }
        if (params.length() > 0) {
            redirectURL += "?" + params.toString();
        }
        
        response.sendRedirect(redirectURL);
    }
    
    private void cargarDatos(HttpServletRequest request) {
        MesaDAO mesaDAO = new MesaDAO();
        MenuDAO menuDAO = new MenuDAO();
        
        List<Mesa> mesasOcupadas = mesaDAO.obtenerMesasOcupadas();
        List<Mesa> mesasDisponibles = mesaDAO.obtenerMesasDisponibles();
        List<Menu> listaMenus = menuDAO.listarMenus();
        
        request.setAttribute("mesasOcupadas", mesasOcupadas);
        request.setAttribute("mesasDisponibles", mesasDisponibles);
        request.setAttribute("listaMenus", listaMenus);
    }
}