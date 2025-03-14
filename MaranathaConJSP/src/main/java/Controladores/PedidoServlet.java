package Controladores;

import AccesoDatos.MesaDAO;
import AccesoDatos.MenuDAO;
import AccesoDatos.PedidoDAO;
import Logica.Cliente;
import Logica.Mesa;
import Logica.Menu;
import Logica.Pedido;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "PedidoServlet", urlPatterns = {"/PedidoServlet"})
public class PedidoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        cargarDatosIniciales(request);
        RequestDispatcher rd = request.getRequestDispatcher("/Cliente/HacerPedido.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Valido parámetros obligatorios
        String menuIdParam = request.getParameter("menus");
        String metodoPago = request.getParameter("metodoPago");
        String nombreCliente = request.getParameter("nombre");
        String telefonoCliente = request.getParameter("telefono");

        if (menuIdParam == null || menuIdParam.isEmpty() ||
            metodoPago == null || metodoPago.isEmpty() ||
            nombreCliente == null || nombreCliente.isEmpty() ||
            telefonoCliente == null || telefonoCliente.isEmpty()) {

            request.setAttribute("mensajeError", "Todos los campos obligatorios deben ser completados");
            cargarDatosIniciales(request);
            forwardToJSP(request, response);
            return;
        }

        try {
            // Convertierto el id enviado y obtenengo el menú correspondiente
            int menuId = Integer.parseInt(menuIdParam);
            MenuDAO menuDAO = new MenuDAO();
            Menu menu = menuDAO.obtenerMenuPorId(menuId);

            if (menu == null) {
                request.setAttribute("mensajeError", "Menú seleccionado no válido");
                cargarDatosIniciales(request);
                forwardToJSP(request, response);
                return;
            }

            // Creo el objeto Pedido y asignar los datos
            Pedido pedido = new Pedido();
            pedido.setMenu(menu);
            pedido.setMetodoPago(metodoPago);

            // Proceso la mesa (si se seleccionó)
            String mesaParam = request.getParameter("mesa");
            if (mesaParam != null && !mesaParam.isEmpty()) {
                try {
                    int numMesa = Integer.parseInt(mesaParam);
                    Mesa mesa = new Mesa();
                    mesa.setNumero(numMesa);
                    pedido.setMesa(mesa);
                } catch (NumberFormatException e) {
                    System.err.println("Error en formato de mesa: " + e.getMessage());
                }
            }

            // Proceso si es a domicilio
            String esDomicilioParam = request.getParameter("esDomicilio");
            boolean esDomicilio = "Si".equals(esDomicilioParam);
            pedido.setEsDomicilio(esDomicilio);

            // Proceso  datos del cliente
            Cliente cliente = new Cliente();
            cliente.setNombre(nombreCliente.trim());
            cliente.setTelefono(telefonoCliente.trim());
            pedido.setCliente(cliente);

            // Proceso dirección si es a domicilio
            if (esDomicilio) {
                String direccion = request.getParameter("direccion");
                pedido.setDireccion(direccion != null ? direccion.trim() : "");
            }

            // Guardo el pedido en la base de datos
            PedidoDAO pedidoDAO = new PedidoDAO();
            boolean exito = pedidoDAO.agregarPedido(pedido);

            if (exito) {
                request.setAttribute("mensajeExito", "¡Pedido registrado exitosamente!");
                // Si el pedido no es a domicilio y se seleccionó una mesa, la marco como ocupada
                if (!esDomicilio && mesaParam != null && !mesaParam.isEmpty()) {
                    int numMesa = Integer.parseInt(mesaParam);
                    MesaDAO mesaDAO = new MesaDAO();
                    boolean actualizacionMesa = mesaDAO.ocuparMesa(numMesa);
                    if (!actualizacionMesa) {
                        System.err.println("No se pudo marcar la mesa " + numMesa + " como ocupada.");
                    }
                }
            } else {
                request.setAttribute("mensajeError", "Error al guardar el pedido en la base de datos");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("mensajeError", "Error en formato de datos: " + e.getMessage());
        } catch (Exception e) {
            request.setAttribute("mensajeError", "Error general del sistema: " + e.getMessage());
            e.printStackTrace();
        }

        // Recargo datos actualizados y reenvio a el JSP
        cargarDatosIniciales(request);
        forwardToJSP(request, response);
    }

    private void cargarDatosIniciales(HttpServletRequest request) {
        try {
            MesaDAO mesaDAO = new MesaDAO();
            MenuDAO menuDAO = new MenuDAO();

            List<Mesa> mesasDisponibles = mesaDAO.obtenerMesasDisponibles();
            List<Menu> listaMenus = menuDAO.listarMenus();

            request.setAttribute("mesasDisponibles", mesasDisponibles);
            request.setAttribute("listaMenus", listaMenus);

        } catch (Exception e) {
            request.setAttribute("mensajeError", "Error cargando datos iniciales: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void forwardToJSP(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/Cliente/HacerPedido.jsp");
        rd.forward(request, response);
    }
}
